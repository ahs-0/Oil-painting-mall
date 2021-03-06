<%@page contentType="text/html;charset=utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>油画列表</title>
	<link rel="stylesheet" type="text/css" href="css\list.css">
	<script src="js/jquery-3.4.1.min.js" type="text/javascript"></script>
	<script src="js/sweetalert2.js" type="text/javascript"></script>
	<script>
		function showPreview(previewObj){
			let preview = $(previewObj).attr("data-preview");
			let pname = $(previewObj).attr("data-pname");
			Swal.fire({
				title: pname,
				html: "<img src='" + preview + "' style='width:361px;height:240px'>",
				showCloseButton: true,
				showConfirmButton: false
			})
		}
		//删除数据
		function del(delObj) {
			var id = $(delObj).attr("data-id");
			// alert(id);
			var pname = $(delObj).attr("data-pname");
			// alert(pname);
			var preview = $(delObj).attr("data-preview");
			// alert(preview);
			Swal.fire({
				title: "确定要删除[" + pname + "]油画吗?",
				html: "<img src='" + preview + "' style='width:361px;height:240px'>",
				showCancelButton: true,
				confirmButtonText: "是",
				cancelButtonText: "否"
			}).then(function (result) {
				if (result.value == true) {
					//alert("你点了'是'按钮")
					$.ajax({
						url: "/mgallery_war_exploded/management?method=delete&id=" + id,
						type: "get",
						dataType: "json",
						success: function (json) {
							if (json.result == "ok") {
								window.location.reload();
							} else {
								Swal.fire({
									title: json.result
								})
							}
							//console.log(json);
						}
					})
				}
			})
		}
	</script>
</head>
<body>
	<div class="container">
		<fieldset>
			<legend>油画列表</legend>
			<div style="height: 40px">
				<a href="/mgallery_war_exploded/management?method=show_create" class="btn-button">新增</a>
			</div>
			<!-- 油画列表 -->
			<table cellspacing="0px">
				<thead>
					<tr style="width: 150px;">
						<th style="width: 100px">分类</th>
						<th style="width: 150px;">名称</th>
						<th style="width: 100px;">价格</th>
						<th style="width: 400px">描述</th>
						<th style="width: 100px">操作</th>
					</tr>
				</thead>
				<c:forEach items="${requestScope.pageModel.pageData }" var="painting">
					<tr>
						<td>
							<c:choose>
								<c:when test="${painting.category==1 }">现实主义</c:when>
								<c:when test="${painting.category==2 }">抽象主义</c:when>
								<c:otherwise>未知的类型</c:otherwise>
							</c:choose>
						</td>
						<td>${painting.pname }</td>
						<td><fmt:formatNumber pattern="¥0.00" value="${painting.price }"/> </td>
						<td>${painting.description }</td>
						<td>
							<a class="oplink" data-preview="${painting.preview}" data-pname="${painting.pname }" href="javascript:void(0)" onclick="showPreview(this)">预览</a>
							<a class="oplink" href="/mgallery_war_exploded/management?method=show_update&id=${painting.id}">修改</a>
							<a class="oplink" href="javascript:void(0)" data-id="${painting.id }" data-pname="${painting.pname }" data-preview="${painting.preview }" onclick="del(this)">删除</a>
						</td>
					</tr>
				</c:forEach>
			</table>
			<!-- 分页组件 -->
			<ul class="page">
				<li><a href="/mgallery_war_exploded/management?method=list&p=1">首页</a></li>
				<li><a href="/mgallery_war_exploded/management?method=list&p=${requestScope.pageModel.hasPreviousPage?requestScope.pageModel.page-1:1}">上页</a></li>
				<c:forEach begin="1" end="${requestScope.pageModel.totalPages}" step="1" var="pno">
					<li ${pno==requestScope.pageModel.page?"class='active'":""}>
						<a href="/mgallery_war_exploded/management?method=list&p=${pno }">${pno}</a>
					</li>
				</c:forEach>
				<li><a href="/mgallery_war_exploded/management?method=list&p=${requestScope.pageModel.hasNextPage?requestScope.pageModel.page+1:requestScope.pageModel.totalPages}">下页</a></li>
				<li><a href="/mgallery_war_exploded/management?method=list&p=${requestScope.pageModel.totalPages}">尾页</a></li>
			</ul>
		</fieldset>
	</div>

</body>
</html>
