<%--
  Created by IntelliJ IDEA.
  User: ki264
  Date: 2017/6/2
  Time: 上午 11:20
  To change this template use File | Settings | File Templates.
--%>
<%@page import="java.text.DecimalFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<jsp:directive.page import="com.servlet.upload.UploadStatus"/>
<%!
    DecimalFormat format = new DecimalFormat("0.00");

    // 输出保留两位小数
    public String format(double d){
        return format.format(d);
    }
%>
<%
    out.clear();

    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragrma", "no-cache");
    response.setDateHeader("Expires", 0);

    UploadStatus status = (UploadStatus) session.getAttribute("uploadStatus");

    if(status == null){
        response.getWriter().println("没有上传信息");
        return;
    }

    long startTime = status.getStartTime();
    long currentTime = System.currentTimeMillis();

    // 已传输的时间 单位：s
    long time = (currentTime - startTime)/1000 + 1;

    // 传输速度 单位：byte/s
    double velocity = ((double)status.getBytesRead()) / (double)time;

    // 估计总时间 单位：s
    double totalTime = status.getContentLength() / velocity;

    // 估计剩余时间 单位：s
    double timeLeft = totalTime - time;

    // 已完成的百分比
    int percent = (int)(100 * (double)status.getBytesRead() / (double)status.getContentLength());

    // 已完成数 单位：M
    double length = ((double)status.getBytesRead())/1024/1024;

    // 总长度 单位：M
    double totalLength = ((double)status.getContentLength()) / 1024 / 1024;

    // 格式：百分比||已完成数(M)||文件总长度(M)||传输速率(K)||已用时间(s)||估计总时间(s)||估计剩余时间(s)||正在上传第几个文件
    String value = percent + "||" + length + "||" + totalLength + "||" + velocity + "||" + time + "||" + totalTime + "||" + timeLeft + "||" + status.getItems();

    out.println(value);
%>