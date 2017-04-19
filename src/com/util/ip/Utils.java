package com.util.ip;

/*
 * Created on 2004-8-4
 *
 */


import java.io.UnsupportedEncodingException;

/**
 * @author LJ-silver
 */
public class Utils {
    /**
     * 從ip的字串形式得到字節陣列形式
     *
     * @param ip 字串形式的ip
     * @return 字節陣列形式的ip
     */
    public static byte[] getIpByteArrayFromString(String ip) {
        byte[] ret = new byte[4];
        java.util.StringTokenizer st = new java.util.StringTokenizer(ip, ".");
        try {
            ret[0] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[1] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[2] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[3] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    public static void main(String args[]) {
        byte[] a = getIpByteArrayFromString(args[0]);
        for (int i = 0; i < a.length; i++)
            System.out.println(a[i]);
        System.out.println(getIpStringFromBytes(a));
    }

    /**
     * 對原始字串進行解碼轉換，如果失敗，傳回原始的字串
     *
     * @param s            原始字串
     * @param srcEncoding  源解碼方式
     * @param destEncoding 目標解碼方式
     * @return 轉換解碼後的字串，失敗傳回原始字串
     */
    public static String getString(String s, String srcEncoding, String destEncoding) {
        try {
            return new String(s.getBytes(srcEncoding), destEncoding);
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    /**
     * 根據某種解碼方式將字節陣列轉換成字串
     *
     * @param b        字節陣列
     * @param encoding 解碼方式
     * @return 如果encoding不支援，傳回一個缺省解碼的字串
     */
    public static String getString(byte[] b, String encoding) {
        try {
            return new String(b, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(b);
        }
    }

    /**
     * 根據某種解碼方式將字節陣列轉換成字串
     *
     * @param b        字節陣列
     * @param offset   要轉換的起始位置
     * @param len      要轉換的長度
     * @param encoding 解碼方式
     * @return 如果encoding不支援，傳回一個缺省解碼的字串
     */
    public static String getString(byte[] b, int offset, int len, String encoding) {
        try {
            return new String(b, offset, len, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(b, offset, len);
        }
    }

    /**
     * @param ip ip的字節陣列形式
     * @return 字串形式的ip
     */
    public static String getIpStringFromBytes(byte[] ip) {
        StringBuffer sb = new StringBuffer();
        sb.append(ip[0] & 0xFF);
        sb.append('.');
        sb.append(ip[1] & 0xFF);
        sb.append('.');
        sb.append(ip[2] & 0xFF);
        sb.append('.');
        sb.append(ip[3] & 0xFF);
        return sb.toString();
    }

}
