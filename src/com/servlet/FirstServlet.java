package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ki264 on 2017/4/1.
 */
@WebServlet(name = "FirstServlet")
public class FirstServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.log("執行doGet方法...");
        this.execute(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.log("執行doPost方法...");
        this.execute(request, response);
    }

    @Override
    protected long getLastModified(HttpServletRequest req) {
        this.log("執行getLastModified方法...");
        return -1;
    }

    private void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String param = request.getParameter("param");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("<HEAD><TITLE>A Servlet</TITLE></HEAD>");
        out.println("<BODY>");
        out.println("以" + method + "方式存取該頁面。取到的param參數為：" + param + "<br/>");
        out.println("<form action='" + requestURI + "'method='get'>" +
                "<input type='text' name='param' value='param string'>" +
                "<input type='submit' value='以GET方式查詢頁面" + requestURI + "'></form>");
        out.println("<form action='" + requestURI + "' method='post'>" +
                "<input type='text' name='param' value='param string'>" +
                "<input type='submit' value='以POST方式傳送到頁面" + requestURI + "'></form>");

        out.println("<script>documment.write('本頁面最後更新時間：'+documment.lastModified);</script>");
        out.println("</BODY>");
        out.println("</HTML>");

        out.flush();
        out.close();
    }
}
