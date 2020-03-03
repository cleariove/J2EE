<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<div class="reviewDiv">
    <c:forEach items="${ois}" var="oi">
        <div class="reviewProductInfoDiv">
            <div class="reviewProductInfoImg">
                <img width="400px" height="400px" src="img/productSingle/${oi.product.firstProductImage.id}.jpg">
            </div>
            <div class="reviewProductInfoRightDiv">
                <div class="reviewProductInfoRightText">
                    ${oi.product.name}
                </div>
                <table class="reviewProductInfoTable">
                    <tr>
                        <td width="75px">成交单价:</td>
                        <td>
                        <span class="reviewProductInfoTablePrice">
                        ￥<fmt:formatNumber type="number" value="${oi.product.promotePrice}"
                                           minFractionDigits="2"/>
                        </span> 元
                        </td>
                    </tr>
                    <tr>
                        <td>配送:</td>
                        <td>快递:  0.00</td>
                    </tr>
                    <tr>
                        <td>月销量:</td>
                        <td>
                            <span class="reviewProductInfoTableSellNumber">${oi.product.saleCount}</span>件
                        </td>
                    </tr>
                    <tr>
                        <td>累计评价:</td>
                        <td>
                            <span class="reviewStasticsNumber">${oi.product.reviewCount}</span>条
                        </td>
                    </tr>
                </table>

                <div class="reviewProductInfoRightBelowDiv">
                    <span class="reviewProductInfoRightBelowImg"></span>
                    <span class="reviewProductInfoRightBelowText" >
                        现在查看的是 您所购买商品的信息于
                        <fmt:formatDate value="${o.createDate}" pattern="yyyy年MM月dd"/>下单购买了此商品
                    </span>
                </div>
            </div>
            <div style="clear:both"></div>
        </div>
        <div class="reviewStasticsDiv">
            <div class="reviewStasticsLeft">
                <div class="reviewStasticsLeftTop"></div>
                <div class="reviewStasticsLeftContent">
                    <c:if test="${empty oi.review}">
                        不知道怎么评价？
                        <a href="foreproduct?pid=${oi.product.id}">
                            来看看其他人的评价吧
                        </a>
                    </c:if>
                    <c:if test="${!empty oi.review}">
                        我的评价
                    </c:if>
                </div>
                <div class="reviewStasticsLeftFoot"></div>
            </div>
            <div class="reviewStasticsRight">
                <div class="reviewStasticsRightEmpty"></div>
                <div class="reviewStasticsFoot"></div>
            </div>
        </div>
        <c:if test="${empty oi.review}">
            <div class="makeReviewDiv">
                <form method="post" action="foredoReview">
                    <div class="makeReviewText">其他买家，需要你的建议哦！</div>
                    <table class="makeReviewTable">
                        <tr>
                            <td class="makeReviewTableFirstTD">评价商品</td>
                            <td><textarea name="content"></textarea></td>
                        </tr>
                    </table>
                    <div class="makeReviewButtonDiv">
                        <input type="hidden" name="oid" value="${o.id}">
                        <input type="hidden" name="oiid" value="${oi.id}">
                        <input type="hidden" name="pid" value="${oi.product.id}">
                        <button type="submit">提交评价</button>
                    </div>
                </form>
            </div>
        </c:if>

        <c:if test="${!empty oi.review}">
            <div class="reviewDivlistReviews">
                <div class="reviewDivlistReviewsEach">
                    <div class="reviewDate">
                        <fmt:formatDate value="${oi.review.createDate}" pattern="yyyy-MM-dd"/>
                    </div>
                    <div class="reviewContent">${oi.review.content}</div>
                    <div class="reviewUserInfo pull-right">
                            ${oi.review.user.name}
                    </div>
                </div>
            </div>
        </c:if>
    </c:forEach>
</div>