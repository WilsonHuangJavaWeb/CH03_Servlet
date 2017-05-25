package com.servlet;

import com.yahoo.search.ImageSearchRequest;
import com.yahoo.search.ImageSearchResult;
import com.yahoo.search.ImageSearchResults;
import com.yahoo.search.NewsSearchRequest;
import com.yahoo.search.NewsSearchResult;
import com.yahoo.search.NewsSearchResults;
import com.yahoo.search.SearchClient;
import com.yahoo.search.VideoSearchRequest;
import com.yahoo.search.VideoSearchResult;
import com.yahoo.search.VideoSearchResults;
import com.yahoo.search.WebSearchRequest;
import com.yahoo.search.WebSearchResult;
import com.yahoo.search.WebSearchResults;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by ki264 on 2017/5/24.
 */
@WebServlet(name = "SearchServlet")
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        // 搜索關鍵字
        String word = request.getParameter("word");
        // 搜索類型
        String type = request.getParameter("type");
        // 是否允許成人內容。如果勾選，則為 "true"，否則為 null.
        String allowedAdult = request.getParameter("allowedAdult");

        boolean adultOk = "true".equals(allowedAdult);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("  <HEAD><TITLE>" + word + " 搜索結果</TITLE></HEAD>");
        out.println("<style>");
        out.println("	body, div {font-size:12px; padding:2px; margin:0px; }");
        out.println(
                "	.imgDiv{float:left; width: 172px; height:250px;  margin:2px; padding:2px; border:1px pink solid; overflow:hidden; }");
        out.println("</style>");
        out.println("  <BODY>");

        out.println("<div style='float:left; height:40px; '><img src='../images/yahoo.gif'></div>");
        out.println("<form action='" + request.getRequestURI() + "' method='get'>");
        out.println("	<div style='height:40px; '>");
        out.println(
                "		<input type='radio' name='type' value='web' " + (type.equals("web") ? "checked" : "") + ">網頁");
        out.println("		<input type='radio' name='type' value='news' " + (type.equals("news") ? "checked" : "")
                + ">新聞");
        out.println("		<input type='radio' name='type' value='image' " + (type.equals("image") ? "checked" : "")
                + ">影像");
        out.println("		<input type='radio' name='type' value='video' " + (type.equals("video") ? "checked" : "")
                + ">視訊");
        out.println("		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        out.println("		<input type='checkbox' name='allowedAdult' value='true' " + (adultOk ? "checked" : "")
                + ">允許成人內容 <br/>");
        out.println("		<input type='text' name='word' value='" + word
                + "' style='width:300px; '> <input type='submit' value='用雅虎搜索' style='width:100px; '>");
        out.println("	</div>");
        out.println("</form>");

        SearchClient client = new SearchClient("javasdktest");

        try {
            if ("image".equals(type)) {
                ImageSearchRequest searchRequest = new ImageSearchRequest(URLEncoder.encode(word, "UTF-8"));
                // 是否顯示成人內容
                searchRequest.setAdultOk(adultOk);
                // 查詢記錄數
                searchRequest.setResults(20);
                // 從第 0 條記錄開始顯示
                searchRequest.setStart(BigInteger.valueOf(0));

                double startTime = System.currentTimeMillis();
                ImageSearchResults results = client.imageSearch(searchRequest);
                double endTime = System.currentTimeMillis();

                out.println(
                        "<div align=right style='width:100%; background: #FFDDDD; height:25px; padding:2px; border-top:1px solid #FF9999; margin-bottom:5px; '>");
                out.println("	總共 " + results.getTotalResultsAvailable() + " 條資料，總用時 " + (endTime - startTime) / 1000
                        + " 秒。");
                out.println("</div>");

                for (ImageSearchResult result : results.listResults()) {
                    out.println("<div class='imgDiv'>");
                    out.println("	<div align='center'><a href=\"" + result.getClickUrl()
                            + "\" target=_blank><img width=160 height=120 src=\"" + result.getThumbnail().getUrl()
                            + "\" border='0'></a></div>");
                    out.println("	<div align='center'><a href=\"" + result.getRefererUrl() + "\" target=_blank>"
                            + result.getTitle() + "</a></div>");
                    out.println("	<div align='center'>" + result.getWidth() + "x" + result.getHeight() + " "
                            + result.getFileFormat() + "</div>");
                    out.println("	<div>" + (result.getSummary() == null ? "" : result.getSummary()) + "</div>");
                    out.println("</div>");
                }
            } else if ("web".equals(type)) {
                WebSearchRequest searchRequest = new WebSearchRequest(URLEncoder.encode(word, "UTF-8"));
                // 是否顯示成人內容
                searchRequest.setAdultOk(adultOk);
                // 查詢記錄數
                searchRequest.setResults(20);
                // 從第 0 條記錄開始顯示
                searchRequest.setStart(BigInteger.valueOf(0));

                double startTime = System.currentTimeMillis();
                WebSearchResults results = client.webSearch(searchRequest);
                double endTime = System.currentTimeMillis();

                out.println(
                        "<div align=right style='width:100%; background: #FFDDDD; height:25px; padding:2px; border-top:1px solid #FF9999; margin-bottom:5px; '>");
                out.println("	總共 " + results.getTotalResultsAvailable() + " 條資料，總用時 " + (endTime - startTime) / 1000
                        + " 秒。");
                out.println("</div>");
                for (WebSearchResult result : results.listResults()) {
                    out.println("<div style='margin:8px; width:500px; '>");
                    out.println("	<div><a href=\"" + result.getClickUrl() + "\" target=_blank><b>" + result.getTitle()
                            + "</b></a> 檔案格式：" + result.getMimeType() + "</div>");
                    out.println("	<div>網址：<a href=\"" + result.getUrl() + "\" target=_blank>" + result.getUrl()
                            + "</a></div>");
                    out.println("	<div>" + result.getSummary()
                            + (result.getCache() == null ? ""
                            : " [<a href=\"" + result.getCache().getUrl() + "\" target=_blank>網頁快照</a>]")
                            + "</div>");

                    out.println("</div>");
                }
            } else if ("news".equals(type)) {

                NewsSearchRequest searchRequest = new NewsSearchRequest(URLEncoder.encode(word, "UTF-8"));
                // 是否顯示成人內容
                // searchRequest.setAdultOk(adultOk);
                // 查詢記錄數
                searchRequest.setResults(20);
                // 從第 0 條記錄開始顯示
                searchRequest.setStart(BigInteger.valueOf(0));

                double startTime = System.currentTimeMillis();
                NewsSearchResults results = client.newsSearch(searchRequest);
                double endTime = System.currentTimeMillis();

                out.println(
                        "<div align=right style='width:100%; background: #FFDDDD; height:25px; padding:2px; border-top:1px solid #FF9999; margin-bottom:5px; '>");
                out.println("	總共 " + results.getTotalResultsAvailable() + " 條資料，總用時 " + (endTime - startTime) / 1000
                        + " 秒。");
                out.println("</div>");
                for (NewsSearchResult result : results.listResults()) {
                    out.println("<div style='margin:8px; width:500px; '>");
                    out.println("	<div><a href=\"" + result.getClickUrl() + "\" target=_blank><b>" + result.getTitle()
                            + "</b></a></div>");
                    out.println("	<div>網址：<a href=\"" + result.getUrl() + "\" target=_blank>" + result.getUrl()
                            + "</a></div>");
                    out.println("	<div>" + result.getSummary() + "</div>");
                    out.println("</div>");
                }
            } else if ("video".equals(type)) {
                VideoSearchRequest searchRequest = new VideoSearchRequest(URLEncoder.encode(word, "UTF-8"));
                // 是否顯示成人內容
                searchRequest.setAdultOk(adultOk);
                // 查詢記錄數
                searchRequest.setResults(20);
                // 從第 0 條記錄開始顯示
                searchRequest.setStart(BigInteger.valueOf(0));

                double startTime = System.currentTimeMillis();
                VideoSearchResults results = client.videoSearch(searchRequest);
                double endTime = System.currentTimeMillis();

                out.println(
                        "<div align=right style='width:100%; background: #FFDDDD; height:25px; padding:2px; border-top:1px solid #FF9999; margin-bottom:5px; '>");
                out.println("	總共 " + results.getTotalResultsAvailable() + " 條資料，總用時 " + (endTime - startTime) / 1000
                        + " 秒。");
                out.println("</div>");

                for (VideoSearchResult result : results.listResults()) {
                    out.println("<div class='imgDiv'>");
                    out.println("	<div align='center'><a href=\"" + result.getClickUrl()
                            + "\" target=_blank><img width=160 height=120 src=\"" + result.getThumbnail().getUrl()
                            + "\" border='0'></a></div>");
                    out.println("	<div align='center'><a href=\"" + result.getRefererUrl() + "\" target=_blank>"
                            + result.getTitle() + "</a></div>");
                    out.println("	<div align='center'>" + result.getWidth() + "x" + result.getHeight() + " "
                            + result.getFileFormat() + "</div>");
                    out.println("	<div>" + (result.getSummary() == null ? "" : result.getSummary()) + "</div>");
                    out.println("</div>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<font color=red>Exception: " + e.getMessage() + "</font>");
        }

        out.println("  </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }

}
