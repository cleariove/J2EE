<%--
  Created by IntelliJ IDEA.
  User: SinGai
  Date: 2020/2/26
  Time: 23:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<div class="homepageCategoryProducts">
    <c:forEach items="${categories}" var="c" varStatus="status">
        <div class="eachHomepageCategoryProducts">
            <div class="left-mark"></div>
            <span class="categoryTitle">${c.name}</span>
            <br>
            <c:forEach items="${c.products}" var="p" varStatus="st">
                <c:if test="${st.count <= 5}">
                    <div class="productItem">
                        <a href="foreproduct?pid=${p.id}">
                            <img src="img/productSingle_middle/${p.firstProductImage.id}.jpg">
                        </a>
                        <a class="productItemDescLink" href="foreproduct?pid=${p.id}">
                            <span class="productItemDesc">[热销]${fn:substring(p.name, 0, 20)}</span>
                        </a>
                        <span class="productPrice">
                            <fmt:formatNumber type="number" value="${p.promotePrice}" minFractionDigits="2" />
                        </span>
                    </div>
                </c:if>
            </c:forEach>
            <div style="clear:both"></div>
        </div>
    </c:forEach>
    <img id="endpng" class="endpng" src="img/site/end.png">
</div>