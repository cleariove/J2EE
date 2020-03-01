<%--
  Created by IntelliJ IDEA.
  User: SinGai
  Date: 2020/2/26
  Time: 12:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <script src="js/jquery/2.0.0/jquery.min.js"></script>
    <link href="css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
    <script src="js/bootstrap/3.3.6/bootstrap.min.js"></script>
    <link href="css/fore/style.css" rel="stylesheet">
    <script>
        function formatMoney(num)
        {
            num = num.toString().replace(/[$,]/,'');
            if(isNaN(num))
                num = "0";
            var sign = (num === (num = Math.abs(num)));
            //Math.floor()为向下取整,这句代码用于保留两位小数四舍五入
            //下面五行代码将金额的整数部分存入num中，小数部分存入cents中
            num = Math.floor((num * 100) + 0.500001);
            cents = num % 100;
            num = Math.floor(num /100).toString();
            if(cents < 10)
                cents = '0' + cents;
            //这里时每三位都需要加一个","
            //先加最后的逗号，这样子比较方便就可以不用管每次添加后的长度变化
            for(var i = 0;i < (num.length - i - 1)/ 3;i++)
                num = num.substring(0,num.length - 3 - 4 * i) + ',' + num.substring(num.length - 3 - 4 * i);
            return (sign ? '' : '-') + num + '.' + cents;
        }

        function checkEmpty(id, name){
            var value = $("#"+id).val();
            if(value.length==0){

                $("#"+id)[0].focus();
                return false;
            }
            return true;
        }

        $(function () {
            $("a.productDetailTopReviewLink").click(
                function ()
                {
                    $("div.productReviewDiv").show();
                    $("div.productDetailDiv").hide();
                }
            );
            $("a.productReviewTopPartSelectedLink").click(
                function()
                {
                    $("div.productReviewDiv").hide();
                    $("div.productDetailDiv").show();
                }
            );

            $("span.leaveMessageTextareaSpan").hide();

            $("img.leaveMessageImg").click(
                function()
                {
                    $(this).hide();
                    $("span.leaveMessageTextareaSpan").show();
                    $("div.orderItemSumDiv").css("height","100px");
                }
            );

            $("div#footer a[href$=#nowhere]").click(function(){
                alert("模仿天猫的连接，并没有跳转到实际的页面");
            });

            $("a.wangwanglink").click(function(){
                alert("模仿旺旺的图标，并不会打开旺旺");
            });

            $("a.notImplementLink").click(function(){
                alert("这个功能没做，蛤蛤~");
            });
        });
    </script>
</head>
<body>

