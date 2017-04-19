package com.servlet;


import com.util.IpUtil;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ki264 on 2017/4/19.
 */
@WebServlet(name = "RequestServlet")
public class RequestServlet extends HttpServlet {

    /**
     * 傳回用戶端瀏覽器接受的檔案類型
     *
     * @param accept
     * @return
     */
    private String getAccept(String accept) {
        StringBuffer buffer = new StringBuffer();
        if (accept.contains("image/gif"))
            buffer.append("GIF 檔案");
        if (accept.contains("image/x-xbitmap"))
            buffer.append("BMP 檔案");
        if (accept.contains("image/jpeg"))
            buffer.append("JPG 檔案");
        if (accept.contains("application/vnd.ms-excel"))
            buffer.append("Excel 檔案");
        if (accept.contains("application/vnd.ms-powerpoint"))
            buffer.append("PPT 檔案");
        if (accept.contains("application/msword"))
            buffer.append("Word 文件");
        return buffer.toString().replace(", $", "");
    }

    /**
     * 傳回用戶端語言環境名稱
     *
     * @param locale
     * @return
     */
    private String getLoacle(Locale locale) {
        if (Locale.SIMPLIFIED_CHINESE.equals(locale))
            return " 簡體中文 ";
        if (Locale.TRADITIONAL_CHINESE.equals(locale))
            return " 繁體中文 ";
        if (Locale.ENGLISH.equals(locale))
            return " 英文 ";
        if (Locale.JAPANESE.equals(locale))
            return " 日本 ";

        return " 未知語言環境 ";
    }

    /**
     * 傳回IP位址對應的實體位置
     *
     * @param
     * @return
     */
    private String getAddress(String ip) {
        return IpUtil.getIpAddress(ip);
    }

    /**
     * 傳回用戶端瀏覽器資訊
     *
     * @param userAgent
     * @return
     */
    private String getNavigation(String userAgent) {
        if (userAgent.indexOf("TencentTraveler") > 0)
            return "騰訊瀏覽器";
        if (userAgent.indexOf("Maxthon") > 0)
            return "Maxthon 瀏覽器";
        if (userAgent.indexOf("MyIE2") > 0)
            return "Firefox 瀏覽器";
        if (userAgent.indexOf("MSIE") > 0)
            return "IE 瀏覽器";
        if (userAgent.indexOf("Chrome") > 0)
            return "Chrome 瀏覽器";
        return "未知的瀏覽器";

    }

    /**
     * 傳回用戶端作業系統
     *
     * @param userAgent
     * @return
     */
    private String getOS(String userAgent) {
        if (userAgent.indexOf("Windows NT 5.1") > 0)
            return "Windows XP";
        if (userAgent.indexOf("Windows 98") > 0)
            return "Windows 98";
        if (userAgent.indexOf("Windows NT 5.0") > 0)
            return "Windows 2000";
        if (userAgent.indexOf("Linux") > 0)
            return "Linux";
        if (userAgent.indexOf("Unix") > 0)
            return "Unix";
        return "未知的作業系統";
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        String authType = request.getAuthType();
        String localAddr = request.getLocalAddr();
        String localName = request.getLocalName();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
