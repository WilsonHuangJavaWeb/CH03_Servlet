package com.servlet;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ki264 on 2017/5/25.
 */
@WebServlet(name = "UploadServlet")
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 7523024737218332088L;

    @SuppressWarnings("nuchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File file1 = null;
        File file2 = null;

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!doctype html public\"-//w3c//dtd html 4.01 transitional//en\">");
        out.println("<html>");
        out.println("   <head><title>A Servlet</title></head>");
        out.println("   <link rel='stylesheet' type='text/css' hreh='../css/style.css'>");
        out.println("   <body>");
        out.println("       <div align='center'><br/>");
        out.println("           <fieldset style='width:90%'><legend>上傳文件</legend>");
        out.println("               <div class='line'>");
        out.println("                   <div align='left' class='leftDiv'>上傳日誌:</div>");
        out.println("                   <div align='left' class='rightDiv'>");

        DiskFileUpload diskFileUpload = new DiskFileUpload();
        try {
            List<FileItem> list = diskFileUpload.parseRequest(request);
            out.println(" 檢查所有的FileItem...<br/>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println("請以POS方式上傳檔案");
    }
}
