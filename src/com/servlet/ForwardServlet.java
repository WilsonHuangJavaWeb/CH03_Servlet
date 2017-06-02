package com.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by WilsonHuang on 2017/6/2.
 */
@WebServlet(name = "ForwardServlet")
public class ForwardServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String destination = request.getParameter("destination");

        if ("file".equals(destination)) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/web.xml");
            requestDispatcher.forward(request, response);

        } else if ("jsp".equals(destination)) {
            request.setAttribute("date", new Date());
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/forward.jsp");
            requestDispatcher.forward(request, response);

        } else if ("servlet".equals(destination)) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/servlet/LifeCycleServlet");
            requestDispatcher.forward(request, response);

        } else {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println("缺少參數。用法:" + request.getRequestURL() + "?destination=jsp 或者 file 或者 servlet ");
        }
    }
}
