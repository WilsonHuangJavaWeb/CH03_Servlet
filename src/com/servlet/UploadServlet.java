package com.servlet;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        String description1 = null;
        String description2 = null;
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
            for (FileItem fileItem : list) {
                if (fileItem.isFormField()) {
                    if ("description1".equals(fileItem.getFieldName())) {
                        out.println("檢查到 description1.....<br/>");
                        description1 = new String(fileItem.getString().getBytes(), "UTF-8");
                    }
                    if ("description2".equals(fileItem.getFieldName())) {
                        out.println("檢查到 description2....<br/>");
                        description2 = new String(fileItem.getString().getBytes(), "UTF-8");
                    }
                } else if ("file1".equals(fileItem.getFieldName())) {
                    File remoteFile = new File(new String(fileItem.getName().getBytes(), "UTF-8"));
                    out.println("檢查到 File1....<br/>");
                    out.println("用戶檔案位置:" + remoteFile.getAbsolutePath() + "<br/>");
                    file1.getParentFile().mkdirs();
                    file1.createNewFile();
                    InputStream inputStream = fileItem.getInputStream();
                    OutputStream outputStream = new FileOutputStream(file1);
                    try {
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = inputStream.read(buffer)) > -1) {
                            outputStream.write(buffer, 0, len);
                        }
                        out.println("已儲存檔案" + file1.getAbsolutePath() + "<br/>");
                    } finally {
                        outputStream.close();
                        inputStream.close();
                    }
                }
                if ("file2".equals(fileItem.getFieldName())) {
                    File remoteFile = new File(new String(fileItem.getName().getBytes(), "UTF-8"));
                    out.println("檢查到file2....<br/>");
                    out.println("客戶端檔案位置:" + remoteFile.getAbsolutePath() + "<br/>");
                    file2 = new File(this.getServletContext().getRealPath("attachment"), remoteFile.getName());
                    file2.getParentFile().mkdirs();
                    file2.createNewFile();
                    InputStream inputStream = fileItem.getInputStream();
                    OutputStream outputStream = new FileOutputStream(file2);
                    try {
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = inputStream.read(buffer)) > -1) {
                            outputStream.write(buffer, 0, len);
                        }
                        out.println("已儲存檔案" + file2.getAbsolutePath() + "<br/>");
                    } finally {
                        outputStream.close();
                        inputStream.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println("請以POS方式上傳檔案");
    }
}
