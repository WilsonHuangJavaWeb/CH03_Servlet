package com.servlet.upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ki264 on 2017/6/1.
 */
@WebServlet(name = "ProgressUploadServlet")
public class ProgressUploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UploadStatus uploadStatus = new UploadStatus();//上傳狀態
        UploadListener uploadListener = new UploadListener(uploadStatus);//監聽器
        request.getSession(true).setAttribute("uploadStatus", uploadStatus);//把狀態放在Session裡

        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());//解析
        upload.setProgressListener(uploadListener);//設定上傳listener


        try {
            List itemList = upload.parseRequest(request);//傳送所有參數

            for (Iterator it = itemList.iterator(); it.hasNext(); ) {//檢查所有參數

                FileItem item = (FileItem) it.next();

                if (item.isFormField()) {//如果是表單資料
                    System.out.println("FormField: " + item.getFieldName() + " = " + item.getString());
                } else {//否則就是上傳的檔案
                    System.out.println("File: " + item.getName());

                    String fileName = item.getName().replace("/", "\\");//統一Linux與Windows的路徑分隔符號
                    fileName = fileName.substring(fileName.lastIndexOf("\\"));

                    File saved = new File("C:\\upload_test", fileName);//建立檔案物件
                    saved.getParentFile().mkdirs();//保證路徑存在

                    InputStream inputStream = item.getInputStream();//傳送的檔案內容
                    OutputStream outputStream = new FileOutputStream(saved);//輸出串流

                    byte[] tmp = new byte[1024];//快取記憶體
                    int len = -1;//快取記憶的實際長度

                    while ((len = inputStream.read(tmp)) != -1) {
                        outputStream.write(tmp, 0, len);//寫入檔案
                    }

                    outputStream.close();//關閉串流
                    inputStream.close();//關閉串流

                    response.getWriter().println("已儲存檔案:" + saved);
                }

            }


        } catch (Exception e) {
            response.getWriter().println("上傳發生錯誤:" + e.getMessage());

        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store");//禁止瀏覽器快取記憶體
        response.setHeader("Pragrma", "no-cache");//禁止瀏覽器快取記憶體
        response.setDateHeader("Expires", 0);//禁止瀏覽器快取記憶體

        UploadStatus status = (UploadStatus) request.getSession(true).getAttribute("uploadStatus");//從Session中讀取上傳資訊

        if (status == null) {//如果沒有上傳資訊
            response.getWriter().println("no upload!!!");
            return;
        }

        long startTime = status.getStartTime();//開始上傳時間
        long currentTime = System.currentTimeMillis();//現在時間
        long time = (currentTime - startTime) / 1000 + 1;//已傳輸的時間，單位:s
        double velocity = ((double) status.getBytesRead()) / (double) time;//傳送速率，單位:byte/s
        double totalTime = status.getContentLength() / velocity;//估計總時間，單位:s
        double timeLeft = totalTime - time;//估計剩餘時間，單位:s
        int percent = (int) (100 * (double) status.getBytesRead() / (double) status.getContentLength());//已完成百分比
        double length = ((double) status.getBytesRead()) / 1024 / 1024;//已完成數，單位:M
        double totalLength = ((double) status.getContentLength()) / 1024 / 1024;//總長度，單位:M

        //格式:百分比||已完成數(M)||檔案總長度(M)||傳輸速率(K)||已用時間(S)||估計總時間(S)||估計剩餘時間(S)||正在上傳第幾個檔案
        String value = percent + "||" + length + "||" + totalLength + "||" + velocity + "||" + time + "||" + totalTime + "||" + timeLeft + "||" + status.getItems();

        response.getWriter().println(value);

    }
}
