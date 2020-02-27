<%--
  Created by IntelliJ IDEA.
  User: SinGai
  Date: 2020/2/26
  Time: 23:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<div class="categoryMenu">
    <c:forEach items="${categories}" var="c">
        <div class="eachCategory">
            <span class="glyphicon glyphicon-link"></span>
            <a href="foreCategory?cid=${c.id}">${c.name}</a>
        </div>
    </c:forEach>
</div>
