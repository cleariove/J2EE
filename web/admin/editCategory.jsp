<%--
  Created by IntelliJ IDEA.
  User: me
  Date: 2020/2/15
  Time: 10:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../include/admin/adminHeader.jsp"%>
<%@ include file="../include/admin/adminNavigator.jsp"%>
<title>分类编辑</title>
<script>
    $(function () {
        $("#editForm").submit(function () {
            return checkEmpty("name", "分类名称");
        })
    })
</script>

<div class="workingArea">

    <ol class="breadcrumb">
        <li><a href="admin_category_list">所有分类</a></li>
        <li class="active">编辑分类</li>
    </ol>

    <div class="panel panel-warning editDiv">
        <div class="panel-heading">编辑分类</div>
        <div class="panel-body">
            <form method="post" id="editForm" action="admin_category_update"  enctype="multipart/form-data">
                <table class="editTable">
                    <tr>
                        <td>分类名称</td>
                        <td><input  id="name" name="name" value="${c.name}" type="text" class="form-control"></td>
                    </tr>
                    <tr>
                        <td>分类图片</td>
                        <td>
                            <input id="categoryPic" accept="image/*" type="file" name="filepath" />
                        </td>
                    </tr>
                    <tr class="submitTR">
                        <td colspan="2" align="center">
                            <input type="hidden" name="id" value="${c.id}">
                            <button type="submit" class="btn btn-success">提 交</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>

    <%@ include file="../include/admin/adminFooter.jsp"%>
</div>