<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Date date = (Date) request.getAttribute("date");
%>
<html>
<head>
    <title>Forward 跳躍</title>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
<div align="center"><br/>
    <fieldset style="width: 90%">
        <legend>Forward 跳躍</legend>
        <br/>
        <div style="">
            <div>從 ForwardServlet 中取到的 Date 為</div>
            <div><%= new SimpleDateFormat("yyyy-mm-dd HH:mm:ss:SSS").format(date)%>
            </div>
        </div>
        <br/>
        <div style="">
            <div align="center">
                <input type="button"
                       onclick="location='<%=request.getContextPath()%>/servlet/ForwardServlet?destination=servlet';"
                       value="跳躍到 Servlet" class="button">
                <input type="button"
                       onclick="location='<%=request.getContextPath()%>/servlet/ForwardServlet?destination=file';"
                       value="跳躍到 web.xml" class="button">
                <input type="button"
                       onclick="location='<%=request.getContextPath()%>/servlet/ForwardServlet?destination=jsp';"
                       value="跳躍到 JSP" class="button">
            </div>
        </div>
    </fieldset>
</div>
</body>
</html>
