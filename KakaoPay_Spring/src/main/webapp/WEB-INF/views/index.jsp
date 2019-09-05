<%@page import="java.net.URL"%>
<%@page import="java.net.HttpURLConnection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<% response.setContentType("text/html; charset=UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>KakaoPay</title>
</head>
<body>
	<form method="post" action="pay.do">
		가맹점 주문번호 : <input type="text" name="partner_order_id" value="123"><br>
		가맹점 회원 id : <input type="text" name="partner_user_id" value="12"><br>
		상품명 : <input type="text" name="item_name" value="초코파이"><br>
		상품 수량 : <input type="text" name="quantity" value="1"><br>
		상품 총액 : <input type="text" name="total_amount" value="10"><br>
		상품 비과세 금액 : <input type="text" name="tax_free_amount" value="10"><br>
		<input type="hidden" name="command" value="ready">
		<input type="hidden" value="TC0ONETIME" name="cid" readonly="readonly">
		<input type="hidden" value="http://localhost:8787/KakaoPay/success.jsp" name="approval_url" readonly="readonly">
		<input type="hidden" value="http://localhost:8787/KakaoPay/cancel.jsp" name="cancel_url" readonly="readonly">
		<input type="hidden" value="http://localhost:8787/KakaoPay/fail.jsp" name="fail_url" readonly="readonly">
		<input type="submit" value="결제 준비">
	</form>

</body>
</html>