<%@ page import="tmall.util.Page" %><%--
  Created by IntelliJ IDEA.
  User: me
  Date: 2020/2/11
  Time: 17:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<nav>
    <ul class="pagination">
<%--        首页按钮--%>
        <li <c:if test="${!page.hasPrevious}"> class="disabled"</c:if>>
            <a href="?page.start=0${page.param}" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </a>
        </li>
<%--        上一页按钮--%>
        <li <c:if test="${!page.hasPrevious}"> class="disabled" </c:if>>
            <a href="?page.start=${page.start - page.count}${page.param}" aria-label="Previous">
                <span aria-hidden="true">&lsaquo;</span>
            </a>
        </li>
<%--        所有页码--%>
        <c:forEach begin="0" end="${page.totalPage - 1}" varStatus="status">
<%--            只相对当前页面的前两页和后4页--%>
            <c:if test="${status.index - page.start/page.count <= 4 && status.index - page.start/page.count >= -2}">
                <%--            如果是当前页面页码--%>
                <li <c:if test="${status.index*page.count == page.start}"> class="disabled" </c:if>>
                <a href="?page.start=${status.index*page.count}${page.param}"
                   <c:if test="${status.index*page.count==page.start}">class="current"</c:if>>
                        ${status.count}
                </a>
                </li>
            </c:if>
        </c:forEach>

<%--        下一页按钮--%>
        <li <c:if test="${!page.hasNext}">class="disabled"</c:if>>
            <a href="?page.start=${page.start+page.count}${page.param}" aria-label="Next">
                <span aria-hidden="true">&rsaquo;</span>
            </a>
        </li>
<%--        尾页按钮--%>
        <li <c:if test="${!page.hasNext}">class="disabled"</c:if>>
            <a href="?page.start=${page.last}${page.param}" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </a>
        </li>
    </ul>
</nav>

