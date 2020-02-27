<%--
  Created by IntelliJ IDEA.
  User: SinGai
  Date: 2020/2/26
  Time: 23:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<script>
    $(function () {
       $("div.productsAsideCategorys div.row a").each(
           function ()
           {
               var v = Math.round(Math.random() *6);
               if(v == 1)
                   $(this).css("color","#87CEFA");
           }
       )
    });
</script>
<c:forEach items="${categories}" var="c">
    <div cid="${c.id}" class="productsAsideCategorys">
        <c:forEach items="${c.productsByRow}" var="pr">
            <div class="row show1">
                <c:forEach items="${pr}" var="p">
                    <c:if test="${!empty p.name}">
                        <a href="foreproduct?pid=${p.id}">
<%--                            取出产品的小标题，将产品的小标题作为产品名称，产品名称比较长--%>
                            <c:forEach items="${fn:split(p.name, ' ')}" var="title" varStatus="st">
                                <c:if test="${st.index==0}">
                                    ${fn:substring(title, 0, 5)}
                                </c:if>
                            </c:forEach>
                        </a>
                    </c:if>
                </c:forEach>
                <div class="seperator"></div>
            </div>
        </c:forEach>
    </div>
</c:forEach>
