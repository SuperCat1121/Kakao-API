<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<% response.setContentType("text/html; charset=UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>결제 요청중</title>
<script type="text/javascript">

	function approval() {
		var form = document.getElementsByTagName("form")[0];
		form.submit();
	}
	
	onload = function() {
		approval();
	}
	
</script>
</head>
<body>
<%
	String pg_token  = request.getParameter("pg_token");
%>
	<h1>결제 요청중</h1>
	<form method="post" action="pay.do">
		<input type="hidden" name="command" value="approval">
		<input type="hidden" name="pg_token" value="<%= pg_token %>">
	</form>
</body>
</html>