<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>名片生成二维码</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  <script type="text/javascript" src="/js/jquery-1.11.3.js"></script>
  <script type="text/javascript" src="/js/jquery.validate.js"></script>
  <script type="text/javascript" src="/js/jquery.validate.min.js"></script>
  <script type="text/javascript">
   
$().ready(function() {
    $("#commentForm").validate();
});
  </script>
   <style>
.error{
	color:red;
}
</style>
  </head>
  <body>
    <h1 style="text-align:center">名片</h1>
    <form action="<c:url value='/QrcodeServlet2'/>"  id="cardForm" method="post" enctype="multipart/form-data">
     <table width="400" align="center" border="1">
      <tr>
	<td>头像:</td><td><input type="file" name="myfile" style="width:320px;"/></td>
	</tr>
     <tr>
	<td>用户名:</td><td><input type="text" name="username"  minlength="2" style="width:320px;" required /></td>
	</tr>
	<tr>
	<td>手机号:</td><td><input type="text" name="number"  required style="width:320px;"/></td>
	</tr>
	<tr>
	<td>单位:</td><td><input type="text" name="org"  style="width:320px;"/></td>
	</tr>
	<tr>
	<td>职位:</td><td><input type="text" name="title"  style="width:320px;"/></td>
	</tr>
	<tr>
	<td>邮箱:</td><td><input type="email" name="email"  required style="width:320px;"/></td>
	</tr>
	<tr>
	<td>地址:</td><td><input type="text" name="address"  style="width:320px;"/></td>
	</tr>
	<tr>
	<td colspan="2" style="text-align:center" >
		<input type="submit" value="生成二维码"/>
	</td>
	</tr>
</table>
    </form>
  </body>
</html>
