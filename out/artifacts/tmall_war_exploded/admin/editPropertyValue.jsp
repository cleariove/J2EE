<%--
  Created by IntelliJ IDEA.
  User: SinGai
  Date: 2020/2/24
  Time: 23:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<title>编辑产品属性值</title>

<script>
    $(function () {
        $("input.pvValue").keyup(function ()
            {
                var value = $(this).val();
                var uri = "admin_product_updatePropertyValue";
                var pvid = $(this).attr("pvid");
                var parentSpan = $(this).parent("span");
                $.post(
                        uri,
                        {"value":value, "pvid":pvid},
                        function(result) {
                            if(result == "success")
                                parentSpan.css("border","1px solid green");
                            else
                                parentSpan.css("border","1px solid red");
                        }
                );
            }
        )
    });
</script>

<div class="workingArea">
    <ol class="breadcrumb">
        <li><a href="admin_category_list">所有分类</a></li>
        <li><a href="admin_product_list?cid=${p.category.id}">${p.category.name}</a></li>
        <li class="active">${p.name}</li>
        <li class="active">产品属性编辑</li>
    </ol>

    <div class="editPVDiv">
        <c:forEach items="${pv}" var="v">
            <div class="eachPV">
                <span class="pvName">${v.property.name}</span>
                <span class="pvValue">
                    <input type="text" value="${v.value}" class="pvValue" pvid="${v.id}">
                </span>
            </div>
        </c:forEach>
        <div style="clear:both"></div>
    </div>
</div>
<%@include file="../include/admin/adminFooter.jsp"%>
