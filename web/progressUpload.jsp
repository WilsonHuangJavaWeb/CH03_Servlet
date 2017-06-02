<%--
  Created by IntelliJ IDEA.
  User: ki264
  Date: 2017/6/1
  Time: 下午 02:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ProgressUpload</title>
</head>

<style type="text/css">
    body, td, div {
        font-size: 12px;
        font-family: "Source Code Pro";
    }

    #progressBar {
        width: 400px;
        height: 12px;
        background: #ffffff;
        border: 1px;
        solid-color: #000000;
        padding: 1px;
    }

    #progressBarItem {
        width: 30%;
        height: 100%;
        background: #FF0000;
    }
</style>

<body>

<iframe name="upload_iframe" width="0" height="0"></iframe>
<form action="servlet/upload/ProgressUploadServlet" method="post"
      enctype="multipart/form-data" target="upload_iframe"
      onsubmit="showStatus();">

    <input type="file" name="file1" style="width: 350px;"><br/>
    <input type="file" name="file2" style="width: 350px;"><br/>
    <input type="file" name="file3" style="width: 350px;"><br/>
    <input type="file" name="file4" style="width: 350px;">
    <input type="submit" value="開始上傳" name="btnSubmit">
</form>

<div id="status" style="display: none;">
    上傳進度條:
    <div id="progressBar">
        <div id="progressBarItem"></div>
    </div>
    <div id="statusInfo"></div>
</div>

<br/>
<br/>
<br/>
<br/>
<br/>

<script type="text/javascript">
    var _finished = true;
    function $(obj) {
        return document.getElementById(obj);
    }

    function showStatus() {
        _finished = false;
        $('status').style.display = 'block';
        $('progressBarItem').style.width = '1%';
        $('btnSubmit').disabled = true;
        setTimeout("requestStatus()", 1000);
    }

    function requestStatus() {
        if (_finished) {
            return;
        }
        var req = createRequest();
        req.open("GET", "servlet/upload/ProgressUploadServlet");
        req.onreadystatechange = function () {
            callback(req);
        }
        req.send(null);
        setTimeout("requestStatus()", 1000);
    }

    function createRequest() {
        if (window.XMLHttpRequest) {
            return new XMLHttpRequest();
        } else {
            try {
                return new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e) {
                return new ActiveXObject("Microsoft.XMLHTTP");
            }
        }
        return null;
    }

    function callback(req) {
        if (req.readyState == 4) {
            if (req.status != 200) {
                _debug("發生錯誤。req.status:" + req.status + "");
                return;
            }
            _debug("status.jsp 傳回值:" + req.responseText);

            var ss = req.responsetext.split("||");
            $('progressBarItem').style.width = '' + ss[0] + '%';
            $('statusInfo').innerHTML =
                '已完成百分比:' + ss[0] +
                '%<br/>已完成數(M):' + ss[1] +
                '<br/>文件總長度(M):' + ss[2] +
                '<br/>傳輸速率(K):' + ss[3] +
                '<br/>已用時間(S):' + ss[4] +
                '<br/>估計總時間(S):' + ss[5] +
                '<br/>估計剩餘時間(S):' + ss[6] +
                '<br/>正在上傳第幾個文件:' + ss[7];

            if (ss[1] == ss[2]) {
                _finished = true;
                $('statusInfo').innerHTML += "<br/><br/><br/>上傳已完成。";
                $('btnSubmit').disabled = false;
            }
        }
    }

    function _debug(obj) {
        var div = document.createElement("DIV");
        div.innerHTML = "[debug]:" + obj;
        document.body.appendChild(div);
    }
</script>
</body>
</html>
