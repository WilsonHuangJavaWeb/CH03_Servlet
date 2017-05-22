package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ki264 on 2017/5/19.
 */
@WebServlet(name = "InitParamServlet")
public class InitParamServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");//取得由使用者輸入後所傳送的username參數
        String password = request.getParameter("password");//取得由使用者輸入後所傳送的password參數

        Enumeration params = this.getInitParameterNames();//取得所有"初始化參數"的名稱

        while (params.hasMoreElements()) {//檢查所以的初始化參數
            String usernameParam = (String) params.nextElement();//取得參數名稱
            String passnameParam = getInitParameter(usernameParam);//根據參數名稱取得參數值
            if (usernameParam.equalsIgnoreCase(username) && passnameParam.equals(password)) {
                request.getRequestDispatcher("/WEB-INF/notice.html").forward(request, response);
                return;
            }
        }
        this.doGet(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");//設定response編碼方式
        request.setCharacterEncoding("UTF-8");//設定request編碼方式

        response.setContentType("text/html");//設定類型為HTML

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("<HEAD><TITLE>請登入檢視 Notice 檔案</TITLE></HEAD>");
        out.println("<style>body, td, div {font-size:12px}</style>");
        out.println("<BODY>");
        out.println("<form action='" + request.getRequestURI() + "' method='post'>");
        out.println("帳號：<input type='text' name='username' style='width:200px; '><br/>");
        out.println("密碼：<input type='password' name='password' style='width:200px; '><br/>");
        out.println("<input type='submit' value='  登入  '>");
        out.println("</form>");

        if (true) {
            out.println("<br/><br/><br/><br/><br/><br/><br/>使用者名稱、密碼為：<br/>");
            Enumeration params = this.getInitParameterNames();
            while (params.hasMoreElements()) {
                String usernameParam = (String) params.nextElement();
                String passnameParam = this.getInitParameter(usernameParam);
                out.println("[" + usernameParam + ", " + passnameParam + "], ");
            }
        }
        out.println("</BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }
}
