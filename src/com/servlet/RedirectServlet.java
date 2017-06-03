package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by WilsonHuang on 2017/6/3.
 */
@WebServlet(name = "RedirectServlet")
public class RedirectServlet extends HttpServlet {

    Map<String, Integer> map = new HashMap<String, Integer>();


    @Override
    public void init() throws ServletException {
        map.put("/download/setup.exe", 0);
        map.put("/download/application.zip", 0);
        map.put("/download/01.mp3", 0);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getParameter("filename");

        if (fileName != null) {
            int hit = map.get(fileName);//取得下載次數
            map.put(fileName, ++hit);//下載次數+1後儲存
            response.sendRedirect(request.getContextPath() + fileName);//重新導向到檔案
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");

            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"");
            out.println("<html>");
            out.println("   <head><title>檔案下載</title></head>");
            out.println("   <link rel='stylesheet' type='text/css' href='../css/style.css'>");
            out.println("<body><br/>");

            out.println("<fieldset align=center style=width:90%><legend>檔案下載</legend>");
            out.println("<table width=100%>");
            out.println("<tr>");
            out.println("   <td><b>檔案名</b></td>");
            out.println("   <td><b>下載次數</b></td>");
            out.println("   <td><b>下載</b></td>");
            out.println("</tr>");

            for (Entry<String, Integer> entry : map.entrySet()) {
                out.println("<tr>");
                out.println("<td>" + entry.getKey() + "</td>");
                out.println("<td>" + entry.getValue() + "</td>");
                out.println("<td><a href='" + request.getRequestURI() + "?filename=" + entry.getKey() +
                        "' target='_blank' onclick='location=location;'>下載</a></td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</legend>");
            out.println("</body>");
            out.println("</html>");
            out.flush();
            out.close();

        }
    }

    @Override
    public void destroy() {
        map = null;
    }
}
