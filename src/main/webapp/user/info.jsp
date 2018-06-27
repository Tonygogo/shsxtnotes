<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
<div class="data_list">
	<div class="data_list_title"><span class="glyphicon glyphicon-edit"></span>&nbsp;个人中心 </div>
	<div class="container-fluid">
	  <div class="row" style="padding-top: 20px;">
	  	<div class="col-md-8">
	  		<form class="form-horizontal" method="post" action="user" enctype="multipart/form-data" >
			  <div class="form-group">
			  	<input type="hidden" name="action" value="updateInfo">
			    <label for="nickName" class="col-sm-2 control-label">昵称:</label>
			    <div class="col-sm-3">
			      <input class="form-control" name="nick" id="nickName" placeholder="昵称" value="${user.nick}">
			    </div>
			    <label for="img" class="col-sm-2 control-label">头像:</label>
			    <div class="col-sm-5">
			    	<input type="file" id="img" name="img">
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="mood" class="col-sm-2 control-label">心情:</label>
			    <div class="col-sm-10">
			      <textarea class="form-control" name="mood" id="mood" rows="3">${user.mood }</textarea>
			    </div>
			  </div>			 
			  <div class="form-group">
			    <div class="col-sm-offset-2 col-sm-10">
			      <button type="submit" id="btn" class="btn btn-success">修改</button>
			      &nbsp;&nbsp;<span style="color:red" id="msg"></span>
			    </div>
			  </div>
			</form>
	  	</div>
  		<div class="col-md-4">
  			<img style="width:260px;height:200px" src="user?action=userHead&fn=${user.head }">
  		</div>
	  </div>
	</div>	
</div>

<script type="text/javascript">
	// 给昵称文本框绑定失焦和聚焦事件
	$("#nickName").blur(function() { // 失焦
		var nickName = $("#nickName").val();
		if (nickName == null || nickName.length < 1) {
			$("#msg").html("用户昵称不能为空！");
			// 设置按钮不可用
			$("#btn").prop("disabled",true);
			return;
		}
		// 验证昵称的唯一性
		/* $.getJSON("user",{"action":"checkNick","nick":nickName},function(data){ */
		$.post("user",{"action":"checkNick","nick":nickName},function(data){
			console.log(data);
			// 将json字符串转换成json对象
			/* data = eval("("+data+")");
			console.log(data); */
			// 提示信息
			$("#msg").html(data.msg);
			if (data.code == 1) { // 可用
				// 设置按钮可用
				$("#btn").prop("disabled",false);
			} else { // 不可用
				// 设置按钮不可用
				$("#btn").prop("disabled",true);
			}
		});
		
	}).focus(function() { // 聚焦
		$("#msg").html(""); // 清空提示信息
	});
	
</script>

</html>
