package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ki264 on 2017/6/4.
 */
@WebServlet(name = "ThreadSafetyServlet")
public class ThreadSafetyServlet extends HttpServlet {

    private String name;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        name = request.getParameter("name");
        response.getWriter().println("您好," + name + ". 您使用了POST方法傳送資料。");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        name = request.getParameter("name");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        response.getWriter().println("您好," + name + ". 您使用了GET方法傳送資料。");
    }
}
