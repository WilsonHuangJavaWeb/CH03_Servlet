package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by WilsonHuang on 2017/6/2.
 */
@WebServlet(name = "AnnotationServlet")
public class AnnotationServlet extends HttpServlet {

    public AnnotationServlet() {
        this.log("AnnotationServlet() 建構式....");
    }

    @Override
    public void init() throws ServletException {
        this.log("init()....");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.log("doPost()....");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.log("doGet()....");

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"");
        out.println("<html>");
        out.println("<head><title>AnnotationServlet</title></head>");
        out.println("<body>");
        out.println("This is");
        out.println(this.getClass());
        out.println(", using this GET method.");
        out.println("</body>");
        out.println("</html>");
        out.flush();
        out.close();

    }

    @Override
    public void destroy() {
        this.log("destroy()....");
    }

    @PostConstruct
    public void postConstruct(){
        this.log("postConstruct()....");
    }

    @PreDestroy
    public void preDestroy(){
        this.log("preDestroy()....");
    }

    public void log(String string) {
        System.out.println(string);
    }
}
