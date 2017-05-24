package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

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
        response.setContentType("text/html");

        String word = request.getParameter("word");
        String type = request.getParameter("type");
        String allowedAdult = request.getParameter("allowedAdult");
        boolean adultOK = "true".equals(allowedAdult);

        PrintWriter out = response.getWriter();
        out.println("<!doctype html public \"-//w3c//dtd html 4.01 transitional//en\"");
        out.println("<html>");
        out.println("   <head><title>" + word + " 搜尋結果</title><head>");
        out.println("   <body>");
        out.println("   <div style='float:left; height:40px; '><img src='https://s1.yimg.com/rz/d/yahoo_frontpage_zh-Hant-TW_s_f_p_bestfit_frontpage_2x.png'></div>");
        out.println("   <form action='" + request.getRequestURI() + " method='get'>");
        out.println("   <div style='height:40px; '>");
        out.println("       <input type='radio' name='type' value='web' " + (type.equals("web") ? "checked" : "") + ">網頁");
        out.println("       <input type='radio' name='type' value='news' " + (type.equals("news") ? "checked" : "") + ">新聞");
        out.println("       <input type='radio' name='type' value='image' " + (type.equals("iamge") ? "checked" : "") + ">影像");
        out.println("       <input type='radio' name='type' value='video' " + (type.equals("video") ? "checked" : "") + ">");
        out.println("       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        out.println("       <input type='checkbox' name='allowedAdult' value='true' " + (adultOK ? "checked" : "") + ">允許成人內容<br/>");
        out.println("       <input type='text' name='word' value='" + word + "' style='width:300px; '><input type='submit' value='用雅虎搜尋' style='width:100px; '>");
        out.println("   </div>");
        out.println("   </form>");


    }

}
