<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/6
  Time: 8:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>评价器接口测试</title>
    <script type="text/javascript" src="js/jquery-2.1.3.min.js"></script>
    <script type="text/javascript" src="js/json2.js"></script>
    <script type="text/javascript">
        var idx = 1;
        function login(){
            var idcard = $("#idcard").val();
            var centerid = $("#centerid").val();
            var counterid = $("#counterid").val();
            if(centerid.length==0){
                alert("中心mac不能为空！")
                return;
            }
            if(counterid.length==0){
                alert("窗口不能为空！")
                return;
            }
            if(idcard.length==0){
                alert("工号不能为空！")
                return;
            }
            $.post("evalctl.action",{"method":"STATE_EVALUATE", "json":"{\"centerid\":"+centerid+",\"counterid\":"+counterid+",\"serviceper_no\":\""+idcard+"\",\"state\":0}"},
            function(rest){
                $("#msg").prepend("<div>"+idx + ": 登录，" + JSON.stringify(rest)+"</div>");
                idx++;
            });
        }

        function welcome(){
            var centerid = $("#centerid").val();
            var counterid = $("#counterid").val();
            if(centerid.length==0){
                alert("中心mac不能为空！")
                return;
            }
            if(counterid.length==0){
                alert("窗口不能为空！")
                return;
            }
            $.post("evalctl.action",{"method":"STATE_EVALUATE", "json":"{\"centerid\":"+centerid+",\"counterid\":"+counterid+",\"serviceper_no\":\""+idcard+"\",\"state\":1}"},
                function(rest){
                    $("#msg").prepend("<div>"+idx + ": 欢迎，" + JSON.stringify(rest)+"</div>");
                    idx++;
                });
        }

        function apprise(){
            var centerid = $("#centerid").val();
            var counterid = $("#counterid").val();
            if(centerid.length==0){
                alert("中心mac不能为空！")
                return;
            }
            if(counterid.length==0){
                alert("窗口不能为空！")
                return;
            }
            $.post("evalctl.action",{"method":"STATE_EVALUATE", "json":"{\"centerid\":"+centerid+",\"id\":\"9c1c3ffd-551b-4644-9c9b-57956c6e0b90\",\"counterid\":"+counterid+",\"serviceper_no\":\""+idcard+"\",\"state\":2}"},
                function(rest){
                    $("#msg").prepend("<div>"+idx + ": 评价，" + JSON.stringify(rest)+"</div>");
                    idx++;
                });
        }
    </script>
</head>
<body>
中心mac：<input type="text" id="centerid">
窗口mac：<input type="text" id="counterid">
工号：<input type="text" id="idcard">
<input type="button" value="登录" onclick="login()"/>
<input type="button" value="欢迎光临" onclick="welcome()"/>
<input type="button" value="请评价" onclick="apprise()"/>
<div id="msg"></div>
</body>
</html>
