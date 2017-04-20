package com.servlet;


import com.util.IpUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
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
    private String getLocale(Locale locale) {
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
    private String getNavigator(String userAgent) {
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

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");                         //設定response編碼方式
        response.setContentType("text/html");                           //設定文件類型為HTML類型
        String authType = request.getAuthType();
        String localAddr = request.getLocalAddr();                      //本機IP，即伺服器IP
        String localName = request.getLocalName();                      //本機名稱，即伺服器名稱
        int localPort = request.getLocalPort();                         //本機通訊埠號，即Tomcat通訊埠號
        Locale locale = request.getLocale();                           //使用者的語言環境
        String contextPath = request.getContextPath();                  //context路徑
        String method = request.getMethod();                            //GET 還是 POST
        String pathInfo = request.getPathInfo();
        String pahtTranslated = request.getPathTranslated();
        String protocol = request.getProtocol();                        //協定，此為HTTP協定
        String queryString = request.getQueryString();                  //查詢字串，即？後面的字串
        String remoteAddr = request.getRemoteAddr();                    //遠端IP，即用戶端IP
        int port = request.getRemotePort();                             //遠端通訊埠，即用戶端通訊埠
        String remoteUser = request.getRemoteUser();                    //遠端使用者
        String requestedSessionId = request.getRequestedSessionId();    //用戶端Session的ID
        String requestURI = request.getRequestURI();                    //使用者請求的URI
        StringBuffer requestURL = request.getRequestURL();              //使用者請求的URL
        String scheme = request.getScheme();                            //協定頭，這裡為http
        String serverName = request.getServerName();                    //伺服器名稱
        int serverPort = request.getServerPort();                       //伺服器通訊埠
        String servletPath = request.getServletPath();                  //Servlet的路徑
        Principal userPrincipal = request.getUserPrincipal();
        String accept = request.getHeader("accept");                //瀏覽器支援格式
        String referer = request.getHeader("referer");              //從哪個頁面按一下鏈接到了本頁
        String userAgent = request.getHeader("user-agent");         //User Agent資訊，包括作業系統類型及版本號、瀏覽器類型及版本號
        String serverInfo = this.getServletContext().getServerInfo();

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");

        // 這裡<title></title>之間的資訊在瀏覽器中顯示為標題
        out.println("  <HEAD><TITLE>Request Servlet</TITLE></HEAD>");
        out.println("  <style>body, font, td, div {font-size:12px; line-height:18px; }</style>");
        out.println("  <BODY>");
        out.println("<b>您的IP為</b> " + remoteAddr + "<b>，位於</b> " + getAddress(remoteAddr) +
                "<b>；您使用</b> " + getOS(userAgent) + " <b>操作系統</b>，" + getNavigator(userAgent)
                + " <b>。您使用</b> " + getLocale(locale) + "。<br/>");
        out.println("<b>服務器IP為</b> " + localAddr + "<b>，位於</b> " + getAddress(localAddr) +
                "<b>；服務器使用</b> " + serverPort + " <b>通訊埠，您的瀏覽器使用了</b> " + port +
                " <b>通訊埠存取本網頁。</b><br/>");
        out.println("<b>服務器軟件為</b>：" + serverInfo + "。<b>服務器名稱為</b> " + localName + "。<br/>");
        out.println("<b>您的瀏覽器接受</b> " + getAccept(accept) + "。<br/>");
        out.println("<b>您從</b> " + referer + " <b>存取到該頁面。</b><br/>");
        out.println("<b>使用的協定為</b> " + protocol + "。<b>URL協定頭</b> " + scheme +
                "，<b>服務器名稱</b> " + serverName + "，<b>您存取的URI為</b> " + requestURI
                + "。<br/>");
        out.println("<b>該 Servlet 路徑為</b> " + servletPath + "，<b>該 Servlet 類名為</b> "
                + this.getClass().getName() + "。<br/>");
        out.println("<b>本應用程式在硬碟的根目錄為</b> " + this.getServletContext().getRealPath("") +
                "，<b>網絡相對路徑為</b> " + contextPath + "。 <br/>");
        out.println("<br/>");
        out.println("<br/><br/><a href=" + requestURI + "> 點擊更新本頁面 </a>");
        out.println("  </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }
}
