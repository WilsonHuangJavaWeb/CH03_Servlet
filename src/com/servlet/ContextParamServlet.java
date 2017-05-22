package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ki264 on 2017/5/22.
 */
@WebServlet(name = "ContextParamServlet")
public class ContextParamServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");//設定response編碼
        response.setContentType("text/html");//設定response輸出類型

        PrintWriter out = response.getWriter();//取得out物件

        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("   <HEAD><TITLE>讀取上下文參數</TITLE></HEAD>");
        out.println("   <link rel='stylesheet' type='text/css' href='../css/style.css'>");
        out.println("   <BODY>");
        out.println("       <div align=center><br/>");
        out.println("           <fieldset style='width:90%'><legend>所有的上下文參數</legend><br/>");

        ServletContext servletContext = getServletConfig().getServletContext();//取得上下文
        String uploadFolder = servletContext.getInitParameter("upload folder");//取得參數
        String allowedFileType = servletContext.getInitParameter("allowed file type");//取得參數

        out.println("           <div class='line'>");
        out.println("               <div align='left' class='leftDiv'>上傳資料夾</div>");
        out.println("               <div align='left' class='rightDiv'>" + uploadFolder + "</div>");
        out.println("           </div>");
        out.println("           <div class='line'>");
        out.println("               <div align='left' class='leftDiv'>實際磁碟路徑</div>");
        out.println("               <div align='left' class='rightDiv'>" + servletContext.getRealPath(uploadFolder) + "</div>");
        out.println("           </div>");
        out.println("           <div class='line'>");
        out.println("               <div align='left' class='leftDiv'>允許上傳的類型</div>");
        out.println("               <div align='left' class='rightDiv'>" + allowedFileType + "</div>");
        out.println("           </div>");
        out.println("           </fieldset></div>");
        out.println("   </BODY>");
        out.println("</HEML>");
        out.flush();
        out.close();
    }
}
