package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ki264 on 2017/5/24.
 */
@WebServlet(name = "InjectionServlet")
public class InjectionServlet extends HttpServlet {

    private @Resource(name = "hellow")
    String hellow;
    private @Resource(name = "i")
    int i;

    @Resource(name = "persons")
    private String persons;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("   <HEAD><TITLE>資源植入</TITLE></HEAD>");
        out.println("<style>body {font-size:12px; }</style>");
        out.println("<b>植入的字串</b>:<br/>&nbsp;&nbsp;-&nbsp;" + hellow + "<br/>");
        out.println("<b>植入的整數</b>:<br/>&nbsp;&nbsp;-&nbsp;" + i + "<br/>");
        out.println("<b>植入的字串陣列<b>:<br/>");
        for (String person : persons.split(",")) {
            out.println("&nbsp;&nbsp;-&nbsp;" + person + "<br/>");
        }
        out.println("   <BODY>");
        out.println("   </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }
}
