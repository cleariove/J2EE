<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<title>模仿天猫官网 ${p.name}</title>
<div class="categoryPictureInProductPageDiv">
    <img class="categoryPictureInProductPage" src="img/category/${p.category.id}.jpg">
</div>

<div class="productPageDiv">
    <%@include file="imgAndInfo.jsp" %>

    <%@include file="productDetail.jsp" %>

    <%@include file="productReview.jsp" %>
</div>