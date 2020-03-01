<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>

<script>
    $(function () {
        var stock = ${p.stock};
        $(".productNumberSetting").keyup(function () {
                var num = $(".productNumberSetting").val();
                num = parseInt(num);
                if(isNaN(num))
                    num = 1;
                if(num <= 0)
                    num = 1;
                if(num > stock)
                    num = stock;
                $(".productNumberSetting").val(num);
            });

        $(".increaseNumber").click(function(){
            var num= $(".productNumberSetting").val();
            num++;
            if(num>stock)
                num = stock;
            $(".productNumberSetting").val(num);
        });

        $(".decreaseNumber").click(function(){
            var num= $(".productNumberSetting").val();
            --num;
            if(num<=0)
                num=1;
            $(".productNumberSetting").val(num);
        });

        $(".addCartLink").click(function () {
                var page = "forecheckLogin";
                $.get
                (
                    page,
                    function (result) {
                        if(result == "success")
                        {
                            var pid = ${p.id};
                            var num = $(".productNumberSetting").val();
                            var addCartPage = "foreaddCart";
                            $.get
                            (
                                addCartPage,
                                {"pid":pid,"num":num},
                                function (result)
                                {
                                    if(result == "success")
                                    {
                                        $(".addCartButton").html("已加入购物车");
                                        $(".addCartButton").attr("disabled","disabled");
                                        $(".addCartButton").css("background-color","lightgray")
                                        $(".addCartButton").css("border-color","lightgray")
                                        $(".addCartButton").css("color","black")
                                    }
                                    else;
                                }
                            )
                        }
                        else
                            $("#loginModal").modal('show');
                    });
                return false;
            });

        $(".buyLink").click(function () {
            var page = "forecheckLogin";
            $.get(
                page,
                function (result) {
                    if(result == "success")
                    {
                        var num = $(".productNumberSetting").val();
                        location.href = $(".buyLink").attr("href") + "&num=" + num;
                    }
                    else
                        $("#loginModal").modal('show');
                }
            );
            return false;
        });

        $("button.loginSubmitButton").click(function () {
                var name = $("#name").val();
                var password = $("#password").val();
                if(name.length == 0 || password.length == 0)
                {
                    $("span.errorMessage").html("请输入账号密码");
                    $("div.loginErrorMessageDiv").show();
                    return false;
                }
                var page = "foreloginAjax";
                $.get(
                    page,
                    {"name":name,"password":password},
                    function (result)
                    {
                        if(result == "success")
                        {
                            location.reload();
                        }
                        else
                        {
                            $("span.errorMessage").html("账号密码错误");
                            $("div.loginErrorMessageDiv").show();
                        }
                    }
                );
                return true;
            });

        $("img.smallImage").mouseenter(function () {
                var bigImageURL = $(this).attr("bigImageURL");
                $("img.bigImg").attr("src",bigImageURL);//每一次重新设置src的时候都会触发load事件
            }
        );

        var imgHasLoad = false;//用于判断图片是否已经预加载过了

        $("img.bigImg").load(function () {
            if (!imgHasLoad) {
                $("img.smallImage").each(
                    function () {
                        var bigImageURL = $(this).attr("bigImageURL");
                        //对于这个img对象，如果不加var，那么浏览器会认为这是一个全局变量，所以不加var的话在div.img4load中
                        //只会存在一张图片，加了var的话每次就是不同的对象那么在div.img4load也会存在所有需要预加载的大图
                        var img = new Image();
                        img.src = bigImageURL;
                        //下面这个函数注释了也行，因为上面两行代码浏览器已经加载好了图片，在目录下面已经存在了对应资源
                        //加上应该相当于更合适吧
                        img.onload = function () {
                            console.log(img.src);
                            $("div.img4load").append($(img));
                        }
                    }
                );
                imgHasLoad = true;
            }
        });
    });
</script>

<div class="imgAndInfo">
    <div class="imgInimgAndInfo">
        <img src="img/productSingle/${p.firstProductImage.id}.jpg" class="bigImg">
        <div class="smallImageDiv">
            <c:forEach items="${p.productSingleImages}" var="pi">
                <img src="img/productSingle_small/${pi.id}.jpg"
                     bigImageURL="img/productSingle/${pi.id}.jpg" class="smallImage">
            </c:forEach>
        </div>
        <div class="img4load hidden"></div>
    </div>

    <div class="infoInimgAndInfo">
        <div class="productTitle">
            ${p.name}
        </div>
        <div class="productSubTitle">
            ${p.subTitle}
        </div>
        <div class="productPrice">
            <div class="juhuasuan">
                <span class="juhuasuanBig" >聚划算</span>
                <span>此商品即将参加聚划算，<span class="juhuasuanTime">1天19小时</span>后开始，</span>
            </div>
            <div class="productPriceDiv">
                <div class="gouwujuanDiv"><img height="16px" src="img/site/gouwujuan.png">
                    <span> 全天猫实物商品通用</span>
                </div>
                <div class="originalDiv">
                    <span class="originalPriceDesc">价格</span>
                    <span class="originalPriceYuan">¥</span>
                    <span class="originalPrice">
                        <fmt:formatNumber type="number" value="${p.originalPrice}" minFractionDigits="2"/>
                    </span>
                </div>
                <div class="promotionDiv">
                    <span class="promotionPriceDesc">促销价 </span>
                    <span class="promotionPriceYuan">¥</span>
                    <span class="promotionPrice">
                        <fmt:formatNumber type="number" value="${p.promotePrice}" minFractionDigits="2"/>
                    </span>
                </div>
            </div>
        </div>
        <div class="productSaleAndReviewNumber">
            <div>销量 <span class="redColor boldWord"> ${p.saleCount }</span></div>
            <div>累计评价 <span class="redColor boldWord"> ${p.reviewCount}</span></div>
        </div>
        <div class="productNumber">
            <span>数量</span>
            <span>
                <span class="productNumberSettingSpan">
                    <input class="productNumberSetting" type="text" value="1">
                </span>
                <span class="arrow">
                    <a href="#nowhere" class="increaseNumber">
                    <span class="updown">
                        <img src="img/site/increase.png">
                    </span>
                    </a>
                    <span class="updownMiddle"> </span>
                    <a href="#nowhere"  class="decreaseNumber">
                    <span class="updown">
                        <img src="img/site/decrease.png">
                    </span>
                    </a>
                </span>
                件
            </span>
            <span>库存${p.stock}件</span>
        </div>
        <div class="serviceCommitment">
            <span class="serviceCommitmentDesc">服务承诺</span>
            <span class="serviceCommitmentLink">
                <a href="#nowhere">正品保证</a>
                <a href="#nowhere">极速退款</a>
                <a href="#nowhere">赠运费险</a>
                <a href="#nowhere">七天无理由退换</a>
            </span>
        </div>

        <div class="buyDiv">
            <a class="buyLink" href="forebuyone?pid=${p.id}"><button class="buyButton">立即购买</button></a>
            <a href="#nowhere" class="addCartLink"><button class="addCartButton"><span class="glyphicon glyphicon-shopping-cart"></span>加入购物车</button></a>
        </div>
    </div>

    <div style="clear:both"></div>

</div>