<%@page contentType="text/html;charset=utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Document</title>
	<link rel="stylesheet" type="text/css" href="css\common.css">
	<script type="text/javascript" src="js\js1.js"></script>
</head>
<body>
	<c:if test="${param.c!=null}">
		<c:set var="categoryParam" value="&c=${param.c}"/>
	</c:if>
	<c:if test="${param.c==null}">
		<c:set var="categoryParam" value=""/>
	</c:if>
	<div class="header">
		<div class="logo">
			<img src="image\logo.png" alt="">
		</div>
		<div class="menu"   onclick="show_menu()" onmouseleave="show_menu1()">
			 <div class="menu_title" ><a href="###">内容分类</a></div>
			 <ul id="title">
				<li><a href="/mgallery_war_exploded/page?c=1">现实主义</a></li>
				<li><a href="/mgallery_war_exploded/page?c=2">抽象主义</a></li>
			 </ul>
		</div>
		<div class="auth">
			<ul>
				<li><a href="#">登录</a></li>
				<li><a href="#">注册</a></li>
			</ul>
		</div>
	</div>
	<div class="content">
	  <div class="banner">
  		<img src="image/welcome.png" class="banner-img">
	  </div>
	  <div class="img-content">
		<ul>
			<c:forEach items="${requestScope.pageModel.pageData}" var="painting">
			<li>
				<img src="${painting.preview}" class="img-li">
				<div class="info">
					<h3>${painting.pname}</h3>
					<p>
						${painting.description}
					</p>
					<div class="img-btn">
						<div class="price"><fmt:formatNumber value="${painting.price}" pattern="￥0.00"/> </div>
							<a href="#" class="cart">
						       <div class="btn">
							      <img src="image/cart.svg">
						       </div>
						    </a>
					</div>
				</div>
			</li>
			</c:forEach>
		</ul>
	  </div>
	  <div class="page-nav">
		<ul>
			<li><a href="/mgallery_war_exploded/page?p=1${categoryParam}">首页</a></li>
			<li><a href="/mgallery_war_exploded/page?p=${requestScope.pageModel.hasPreviousPage?requestScope.pageModel.page-1:1}${categoryParam}">上一页</a></li>
			<c:forEach begin="1" end="${requestScope.pageModel.totalPages}" step="1" var="pno">
				<li>
					<span ${pno==requestScope.pageModel.page?"class='first-page'":""}>
						<a href="/mgallery_war_exploded/page?p=${pno}${categoryParam}">
								${pno}
						</a>
					</span>
				</li>
			</c:forEach>
			<li><a href="/mgallery_war_exploded/page?p=${requestScope.pageModel.hasNextPage?requestScope.pageModel.page+1:requestScope.pageModel.totalPages}${categoryParam}">下一页</a></li>
			<li><a href="/mgallery_war_exploded/page?p=${requestScope.pageModel.totalPages}${categoryParam}">尾页</a></li>
		</ul>
	  </div>
	</div>
	<div class="footer">
		<p><span>M-GALLARY</span>©2020 POWERED BY IMOOC.INC&nbsp;&nbsp;&nbsp;&nbsp;<a href="/mgallery_war_exploded/management.html">管理面板</a></p>

	</div>
</body>
</html>