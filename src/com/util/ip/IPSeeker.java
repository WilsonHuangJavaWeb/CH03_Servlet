package com.util.ip;
/*
* LumaQQ - Java QQ Client
*
* Copyright (C) 2004 luma <stubma@163.com>
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


/**
 * <pre>
 * 用來讀取QQwry.dat檔案，以根據ip獲得好友位置，QQwry.dat的格式是
 * 一. 檔案頭，共8字節
 * 	   1. 第一個起始IP的絕對偏移， 4字節
 *     2. 最後一個起始IP的絕對偏移， 4字節
 * 二. "結束地址/國家/區域"記錄區
 *     四字節ip地址後跟的每一條記錄分成兩個部分
 *     1. 國家記錄
 *     2. 地區記錄
 *     但是地區記錄是不一定有的。而且國家記錄和地區記錄都有兩種形式
 *     1. 以0結束的字串
 *     2. 4個字節，一個字節可能為0x1或0x2
 * 		  a. 為0x1時，表示在絕對偏移後還跟著一個區域的記錄，注意是絕對偏移之後，而不是這四個字節之後
 *        b. 為0x2時，表示在絕對偏移後沒有區域記錄
 *        不管為0x1還是0x2，後三個字節都是實際國家名的檔案內絕對偏移
 * 		  如果是地區記錄，0x1和0x2的含義不明，但是如果出現這兩個字節，也肯定是跟著3個字節偏移，如果不是
 *        則為0結尾字串
 * 三. "起始地址/結束地址偏移"記錄區
 *     1. 每條記錄7字節，按照起始地址從小到大排列
 *        a. 起始IP地址，4字節
 *        b. 結束ip地址的絕對偏移，3字節
 *
 * 注意，這個檔案裡的ip地址和所有的偏移量均採用little-endian格式，而java是採用
 * big-endian格式的，要注意轉換
 * </pre>
 *
 * @author 馬若劼
 */
public class IPSeeker {
    /**
     * <pre>
     * 用來封裝ip相關資訊，目前只有兩個字段，ip所在的國家和地區
     * </pre>
     *
     * @author 馬若劼
     */
    private class IPLocation {
        public String country;
        public String area;

        public IPLocation() {
            country = area = "";
        }

        public IPLocation getCopy() {
            IPLocation ret = new IPLocation();
            ret.country = country;
            ret.area = area;
            return ret;
        }
    }

    private static final File IP_FILE = new File(IPSeeker.class.getResource("").getFile(), "QQWry.dat");

    // 一些固定常數，例如記錄長度等等
    private static final int IP_RECORD_LENGTH = 7;
    private static final byte AREA_FOLLOWED = 0x01;
    private static final byte NO_AREA = 0x2;

    // 用來做為cache，查詢一個ip時首先檢視cache，以減少不必要的重複尋找
    private Hashtable ipCache;
    // 隨機檔案存取類
    private RandomAccessFile ipFile;
    // 記憶體映射檔案
    private MappedByteBuffer mbb;
    // 單一模式實例
    private static IPSeeker instance = new IPSeeker();
    // 起始地區的開始和結束的絕對偏移
    private long ipBegin, ipEnd;
    // 為提昇效率而採用的臨時變數
    private IPLocation loc;
    private byte[] buf;
    private byte[] b4;
    private byte[] b3;

    /**
     * 私有建構函數
     */
    private IPSeeker() {
        ipCache = new Hashtable();
        loc = new IPLocation();
        buf = new byte[100];
        b4 = new byte[4];
        b3 = new byte[3];
        try {
            ipFile = new RandomAccessFile(IP_FILE, "r");
        } catch (FileNotFoundException e) {
            System.out.println(IP_FILE.getAbsolutePath());
            System.out.println(IP_FILE);
            System.out.println("IP地址資訊檔案沒有找到，IP顯示功能將無法使用");
            ipFile = null;

        }
        // 如果開啟檔案成功，讀取檔案頭資訊
        if (ipFile != null) {
            try {
                ipBegin = readLong4(0);
                ipEnd = readLong4(4);
                if (ipBegin == -1 || ipEnd == -1) {
                    ipFile.close();
                    ipFile = null;
                }
            } catch (IOException e) {
                System.out.println("IP地址資訊檔案格式有錯誤，IP顯示功能將無法使用");
                ipFile = null;
            }
        }
    }

    /**
     * @return 單一實例
     */
    public static IPSeeker getInstance() {
        return instance;
    }

    /**
     * 指定一個地點的不完全名字，得到一系列包含s子字串的IP範圍記錄
     *
     * @param s 地點子字串
     * @return 包含IPEntry類型的List
     */
    public List getIPEntriesDebug(String s) {
        List ret = new ArrayList();
        long endOffset = ipEnd + 4;
        for (long offset = ipBegin + 4; offset <= endOffset; offset += IP_RECORD_LENGTH) {
            // 讀取結束IP偏移
            long temp = readLong3(offset);
            // 如果temp不等於-1，讀取IP的地點資訊
            if (temp != -1) {
                IPLocation loc = getIPLocation(temp);
                // 判斷是否這個地點裡面包含了s子字串，如果包含了，增加這個記錄到List中，如果沒有，繼續
                if (loc.country.indexOf(s) != -1 || loc.area.indexOf(s) != -1) {
                    IPEntry entry = new IPEntry();
                    entry.country = loc.country;
                    entry.area = loc.area;
                    // 得到起始IP
                    readIP(offset - 4, b4);
                    entry.beginIp = Utils.getIpStringFromBytes(b4);
                    // 得到結束IP
                    readIP(temp, b4);
                    entry.endIp = Utils.getIpStringFromBytes(b4);
                    // 增加該記錄
                    ret.add(entry);
                }
            }
        }
        return ret;
    }

    /**
     * 指定一個地點的不完全名字，得到一系列包含s子字串的IP範圍記錄
     *
     * @param s 地點子字串
     * @return 包含IPEntry類型的List
     */
    public List getIPEntries(String s) {
        List ret = new ArrayList();
        try {
            // 映射IP資訊檔案到記憶體中
            if (mbb == null) {
                FileChannel fc = ipFile.getChannel();
                mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, ipFile.length());
                mbb.order(ByteOrder.LITTLE_ENDIAN);
            }

            int endOffset = (int) ipEnd;
            for (int offset = (int) ipBegin + 4; offset <= endOffset; offset += IP_RECORD_LENGTH) {
                int temp = readInt3(offset);
                if (temp != -1) {
                    IPLocation loc = getIPLocation(temp);
                    // 判斷是否這個地點裡面包含了s子字串，如果包含了，增加這個記錄到List中，如果沒有，繼續
                    if (loc.country.indexOf(s) != -1 || loc.area.indexOf(s) != -1) {
                        IPEntry entry = new IPEntry();
                        entry.country = loc.country;
                        entry.area = loc.area;
                        // 得到起始IP
                        readIP(offset - 4, b4);
                        entry.beginIp = Utils.getIpStringFromBytes(b4);
                        // 得到結束IP
                        readIP(temp, b4);
                        entry.endIp = Utils.getIpStringFromBytes(b4);
                        // 增加該記錄
                        ret.add(entry);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    /**
     * 從記憶體映射檔案的offset位置開始的3個字節讀取一個int
     *
     * @param offset
     * @return
     */
    private int readInt3(int offset) {
        mbb.position(offset);
        return mbb.getInt() & 0x00FFFFFF;
    }

    /**
     * 從記憶體映射檔案的目前位置開始的3個字節讀取一個int
     *
     * @return
     */
    private int readInt3() {
        return mbb.getInt() & 0x00FFFFFF;
    }

    /**
     * 根據IP得到國家名
     *
     * @param ip ip的字節陣列形式
     * @return 國家名字串
     */
    public String getCountry(byte[] ip) {
        // 檢查ip地址檔案是否正常
        if (ipFile == null)
            return "錯誤的IP資料庫檔案";
        // 儲存ip，轉換ip字節陣列為字串形式
        String ipStr = Utils.getIpStringFromBytes(ip);
        // 先檢查cache中是否已經包含有這個ip的結果，沒有再搜索檔案
        if (ipCache.containsKey(ipStr)) {
            IPLocation loc = (IPLocation) ipCache.get(ipStr);
            return loc.country;
        } else {
            IPLocation loc = getIPLocation(ip);
            ipCache.put(ipStr, loc.getCopy());
            return loc.country;
        }
    }

    /**
     * 根據IP得到國家名
     *
     * @param ip IP的字串形式
     * @return 國家名字串
     */
    public String getCountry(String ip) {
        return getCountry(Utils.getIpByteArrayFromString(ip));
    }

    /**
     * 根據IP得到地區名
     *
     * @param ip ip的字節陣列形式
     * @return 地區名字串
     */
    public String getArea(byte[] ip) {
        // 檢查ip地址檔案是否正常
        if (ipFile == null)
            return "錯誤的IP資料庫檔案";
        // 儲存ip，轉換ip字節陣列為字串形式
        String ipStr = Utils.getIpStringFromBytes(ip);
        // 先檢查cache中是否已經包含有這個ip的結果，沒有再搜索檔案
        if (ipCache.containsKey(ipStr)) {
            IPLocation loc = (IPLocation) ipCache.get(ipStr);
            return loc.area;
        } else {
            IPLocation loc = getIPLocation(ip);
            ipCache.put(ipStr, loc.getCopy());
            return loc.area;
        }
    }

    /**
     * 根據IP得到地區名
     *
     * @param ip IP的字串形式
     * @return 地區名字串
     */
    public String getArea(String ip) {
        return getArea(Utils.getIpByteArrayFromString(ip));
    }

    /**
     * 根據ip搜索ip資訊檔案，得到IPLocation結構，所搜索的ip參數從類成員ip中得到
     *
     * @param ip 要查詢的IP
     * @return IPLocation結構
     */
    private IPLocation getIPLocation(byte[] ip) {
        IPLocation info = null;
        long offset = locateIP(ip);
        if (offset != -1)
            info = getIPLocation(offset);
        if (info == null) {
            info = new IPLocation();
            info.country = "未知國家";
            info.area = "未知地區";
        }
        return info;
    }

    /**
     * 從offset位置讀取4個字節為一個long，因為java為big-endian格式，所以沒辦法
     * 用了這麼一個函數來做轉換
     *
     * @param offset
     * @return 讀取的long值，傳回-1表示讀取檔案失敗
     */
    private long readLong4(long offset) {
        long ret = 0;
        try {
            ipFile.seek(offset);
            ret |= (ipFile.readByte() & 0xFF);
            ret |= ((ipFile.readByte() << 8) & 0xFF00);
            ret |= ((ipFile.readByte() << 16) & 0xFF0000);
            ret |= ((ipFile.readByte() << 24) & 0xFF000000);
            return ret;
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * 從offset位置讀取3個字節為一個long，因為java為big-endian格式，所以沒辦法
     * 用了這麼一個函數來做轉換
     *
     * @param offset
     * @return 讀取的long值，傳回-1表示讀取檔案失敗
     */
    private long readLong3(long offset) {
        long ret = 0;
        try {
            ipFile.seek(offset);
            ipFile.readFully(b3);
            ret |= (b3[0] & 0xFF);
            ret |= ((b3[1] << 8) & 0xFF00);
            ret |= ((b3[2] << 16) & 0xFF0000);
            return ret;
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * 從目前位置讀取3個字節轉換成long
     *
     * @return
     */
    private long readLong3() {
        long ret = 0;
        try {
            ipFile.readFully(b3);
            ret |= (b3[0] & 0xFF);
            ret |= ((b3[1] << 8) & 0xFF00);
            ret |= ((b3[2] << 16) & 0xFF0000);
            return ret;
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * 從offset位置讀取四個字節的ip地址放入ip陣列中，讀取後的ip為big-endian格式，但是
     * 檔案中是little-endian形式，將會進行轉換
     *
     * @param offset
     * @param ip
     */
    private void readIP(long offset, byte[] ip) {
        try {
            ipFile.seek(offset);
            ipFile.readFully(ip);
            byte temp = ip[0];
            ip[0] = ip[3];
            ip[3] = temp;
            temp = ip[1];
            ip[1] = ip[2];
            ip[2] = temp;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 從offset位置讀取四個字節的ip地址放入ip陣列中，讀取後的ip為big-endian格式，但是
     * 檔案中是little-endian形式，將會進行轉換
     *
     * @param offset
     * @param ip
     */
    private void readIP(int offset, byte[] ip) {
        mbb.position(offset);
        mbb.get(ip);
        byte temp = ip[0];
        ip[0] = ip[3];
        ip[3] = temp;
        temp = ip[1];
        ip[1] = ip[2];
        ip[2] = temp;
    }

    /**
     * 把類成員ip和beginIp比較，注意這個beginIp是big-endian的
     *
     * @param ip      要查詢的IP
     * @param beginIp 和被查詢IP相比較的IP
     * @return 相等傳回0，ip大於beginIp則傳回1，小於傳回-1。
     */
    private int compareIP(byte[] ip, byte[] beginIp) {
        for (int i = 0; i < 4; i++) {
            int r = compareByte(ip[i], beginIp[i]);
            if (r != 0)
                return r;
        }
        return 0;
    }

    /**
     * 把兩個byte當作無符號數進行比較
     *
     * @param b1
     * @param b2
     * @return 若b1大於b2則傳回1，相等傳回0，小於傳回-1
     */
    private int compareByte(byte b1, byte b2) {
        if ((b1 & 0xFF) > (b2 & 0xFF)) // 比較是否大於
            return 1;
        else if ((b1 ^ b2) == 0)// 判斷是否相等
            return 0;
        else
            return -1;
    }

    /**
     * 這個方法將根據ip的內容，定位到包含這個ip國家地區的記錄處，傳回一個絕對偏移
     * 方法使用二分法尋找。
     *
     * @param ip 要查詢的IP
     * @return 如果找到了，傳回結束IP的偏移，如果沒有找到，傳回-1
     */
    private long locateIP(byte[] ip) {
        long m = 0;
        int r;
        // 比較第一個ip項
        readIP(ipBegin, b4);
        r = compareIP(ip, b4);
        if (r == 0)
            return ipBegin;
        else if (r < 0)
            return -1;
        // 開始二分搜索
        for (long i = ipBegin, j = ipEnd; i < j; ) {
            m = getMiddleOffset(i, j);
            readIP(m, b4);
            r = compareIP(ip, b4);
            // log.debug(Utils.getIpStringFromBytes(b));
            if (r > 0)
                i = m;
            else if (r < 0) {
                if (m == j) {
                    j -= IP_RECORD_LENGTH;
                    m = j;
                } else
                    j = m;
            } else
                return readLong3(m + 4);
        }
        // 如果循環結束了，那麼i和j必定是相等的，這個記錄為最可能的記錄，但是並非
        //     肯定就是，還要檢查一下，如果是，就傳回結束地址區的絕對偏移
        m = readLong3(m + 4);
        readIP(m, b4);
        r = compareIP(ip, b4);
        if (r <= 0)
            return m;
        else
            return -1;
    }

    /**
     * 得到begin偏移和end偏移中間位置記錄的偏移
     *
     * @param begin
     * @param end
     * @return
     */
    private long getMiddleOffset(long begin, long end) {
        long records = (end - begin) / IP_RECORD_LENGTH;
        records >>= 1;
        if (records == 0)
            records = 1;
        return begin + records * IP_RECORD_LENGTH;
    }

    /**
     * 指定一個ip國家地區記錄的偏移，傳回一個IPLocation結構
     *
     * @param offset
     * @return
     */
    private IPLocation getIPLocation(long offset) {
        try {
            // 跳過4字節ip
            ipFile.seek(offset + 4);
            // 讀取第一個字節判斷是否標誌字節
            byte b = ipFile.readByte();
            if (b == AREA_FOLLOWED) {
                // 讀取國家偏移
                long countryOffset = readLong3();
                // 跳躍至偏移處
                ipFile.seek(countryOffset);
                // 再檢查一次標誌字節，因為這個時候這個地方仍然可能是個重新導向
                b = ipFile.readByte();
                if (b == NO_AREA) {
                    loc.country = readString(readLong3());
                    ipFile.seek(countryOffset + 4);
                } else
                    loc.country = readString(countryOffset);
                // 讀取地區標誌
                loc.area = readArea(ipFile.getFilePointer());
            } else if (b == NO_AREA) {
                loc.country = readString(readLong3());
                loc.area = readArea(offset + 8);
            } else {
                loc.country = readString(ipFile.getFilePointer() - 1);
                loc.area = readArea(ipFile.getFilePointer());
            }
            return loc;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * @param offset
     * @return
     */
    private IPLocation getIPLocation(int offset) {
        // 跳過4字節ip
        mbb.position(offset + 4);
        // 讀取第一個字節判斷是否標誌字節
        byte b = mbb.get();
        if (b == AREA_FOLLOWED) {
            // 讀取國家偏移
            int countryOffset = readInt3();
            // 跳躍至偏移處
            mbb.position(countryOffset);
            // 再檢查一次標誌字節，因為這個時候這個地方仍然可能是個重新導向
            b = mbb.get();
            if (b == NO_AREA) {
                loc.country = readString(readInt3());
                mbb.position(countryOffset + 4);
            } else
                loc.country = readString(countryOffset);
            // 讀取地區標誌
            loc.area = readArea(mbb.position());
        } else if (b == NO_AREA) {
            loc.country = readString(readInt3());
            loc.area = readArea(offset + 8);
        } else {
            loc.country = readString(mbb.position() - 1);
            loc.area = readArea(mbb.position());
        }
        return loc;
    }

    /**
     * 從offset偏移開始解析後面的字節，讀出一個地區名
     *
     * @param offset
     * @return 地區名字串
     * @throws IOException
     */
    private String readArea(long offset) throws IOException {
        ipFile.seek(offset);
        byte b = ipFile.readByte();
        if (b == 0x01 || b == 0x02) {
            long areaOffset = readLong3(offset + 1);
            if (areaOffset == 0)
                return "未知地區";
            else
                return readString(areaOffset);
        } else
            return readString(offset);
    }

    /**
     * @param offset
     * @return
     */
    private String readArea(int offset) {
        mbb.position(offset);
        byte b = mbb.get();
        if (b == 0x01 || b == 0x02) {
            int areaOffset = readInt3();
            if (areaOffset == 0)
                return "未知地區";
            else
                return readString(areaOffset);
        } else
            return readString(offset);
    }

    /**
     * 從offset偏移處讀取一個以0結束的字串
     *
     * @param offset
     * @return 讀取的字串，出錯傳回空字串
     */
    private String readString(long offset) {
        try {
            ipFile.seek(offset);
            int i;
            for (i = 0, buf[i] = ipFile.readByte(); buf[i] != 0; buf[++i] = ipFile.readByte())
                ;
            if (i != 0)
                return Utils.getString(buf, 0, i, "GBK");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    /**
     * 從記憶體映射檔案的offset位置得到一個0結尾字串
     *
     * @param offset
     * @return
     */
    private String readString(int offset) {
        try {
            mbb.position(offset);
            int i;
            for (i = 0, buf[i] = mbb.get(); buf[i] != 0; buf[++i] = mbb.get())
                ;
            if (i != 0)
                return Utils.getString(buf, 0, i, "GBK");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public String getAddress(String ip) {
        String country = getCountry(ip).equals(" CZ88.NET") ? "" : getCountry(ip);
        String area = getArea(ip).equals(" CZ88.NET") ? "" : getArea(ip);
        String address = country + " " + area;
        return address.trim();
    }
} 



 
