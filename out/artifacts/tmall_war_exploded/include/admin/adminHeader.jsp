<%--
  Created by IntelliJ IDEA.
  User: me
  Date: 2020/2/11
  Time: 10:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <script src="js/jquery/2.0.0/jquery.min.js"></script>
    <link rel="stylesheet" href="css/bootstrap/3.3.6/bootstrap.min.css">
    <script src="js/bootstrap/3.3.6/bootstrap.min.js"></script>
    <link rel="stylesheet" href="css/back/style.css">
    
    <script>
        function checkEmpty(id,name) {
            var value = $("#" + id).val();
            if(value.length == 0)
            {
                alert(name + "不能为空");
                $("#" + id)[0].focus();//似乎用doc对象和jq对象都可以获得焦点
                return false;
            }
            return true;
        }

        function checkNumber(id,name) {
            var value = $("#" + id).val();
            if(value.length == 0)
            {
                alert(name + "不能为空");
                $("#" + id)[0].focus();
                return false;
            }
            if(isNaN(value))
            {
                alert(name + "必须是数字");
                $("#" + id)[0].focus();
                return false;
            }
            return true;
        }

        function checkInt(id,name) {
            var value = $("#" + id).val();
            if(value.length == 0)
            {
                alert(name + "不能为空");
                $("#" + id)[0].focus();
                return false;
            }
            if(parseInt(value) != value)
            {
                alert(name + "必须是整数");
                $("#" + id)[0].focus();
                return false;
            }
            return true;
        }

        // 最外层加上$(function () {})就相当于$(document).ready(function () {})
        $(function () {
            $("a").click(function () {
                var deleteLink = $(this).attr("deleteLink");
                console.log(deleteLink);
                if(deleteLink == "true")
                {
                    var f = confirm("确认删除？")
                    return f == true;
                }
            });
        })
    </script>
</head>
<body>

