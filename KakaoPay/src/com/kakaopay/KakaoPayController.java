package com.kakaopay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet("/pay.do")
public class KakaoPayController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
//	params.put("cid", "TC0ONETIME");
//	params.put("partner_order_id", "123");
//	params.put("partner_user_id", "gorany");
//	params.put("item_name", "초코파이");
//	params.put("quantity", 1);
//	params.put("total_amount", 10);
//	params.put("tax_free_amount", 0);
//	params.put("approval_url", "http://localhost:8088/KakaoPay/index.jsp");
//	params.put("cancel_url", "http://localhost:8088/KakaoPay/index.jsp");
//	params.put("fail_url", "http://localhost:8088/KakaoPay/index.jsp");
	
    public KakaoPayController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		String command = request.getParameter("command");
		if(command.equals("ready")) {
			URL url = new URL("https://kapi.kakao.com/v1/payment/ready"); // url 준비
			HttpURLConnection con = (HttpURLConnection)url.openConnection(); // url로 통신 시작
			con.setRequestMethod("POST"); // POST 방식으로 요청
			con.setRequestProperty("Authorization", "KakaoAK ff69179230343e4b33048124ae42ebd0"); // 요청 헤더 정의
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"); // 요청 헤더 정의
			con.setDoInput(true); // inputstream 으로 응답 헤더와 메시지를 읽겠다
			con.setDoOutput(true); // outputstream 으로 POST 방식의 요청을 하겠다
			
			Map<String, String> params = new HashMap<String, String>(); // 파라미터 설정
			params.put("cid", "TC0ONETIME");
			params.put("partner_order_id", request.getParameter("partner_order_id"));
			params.put("partner_user_id", request.getParameter("partner_user_id"));
			params.put("item_name", request.getParameter("item_name"));
			params.put("quantity", request.getParameter("quantity"));
			params.put("total_amount", request.getParameter("total_amount"));
			params.put("tax_free_amount", request.getParameter("tax_free_amount"));
			params.put("approval_url", request.getParameter("approval_url"));
			params.put("cancel_url", request.getParameter("cancel_url"));
			params.put("fail_url", request.getParameter("fail_url"));
			
			String string_params = new String(); // 보낼 파라미터
			for(Map.Entry<String, String> elem : params.entrySet()) {
				string_params += (elem.getKey() + "=" + elem.getValue() + "&"); // 파라미터 전송 준비
			}
			
			OutputStream out = con.getOutputStream(); // 요청 스트림 생성
			out.write(string_params.getBytes()); // 파라미터를 바이트로 요청
			out.flush(); // 스트림 버퍼 비우기
			out.close(); // 스트림 닫기
			
			// 응답받은 메시지의 길이만큼 버퍼를 생성하여 읽어들인다 (응답 스트림)
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()), con.getContentLength());
			String successUrl = null; // 준비 성공 시 연결할 url
			String tid = null; // 준비 성공시 받을 결제 고유 번호
			
			try { // 응답받은 json 파싱작업
				JSONParser parser = new JSONParser();
				JSONObject res = (JSONObject)parser.parse(in);
				successUrl = (String)res.get("next_redirect_pc_url");
				tid = (String)res.get("tid");
				HttpSession session = request.getSession();
				session.setAttribute("tid", tid);
				session.setAttribute("partner_order_id", request.getParameter("partner_order_id"));
				session.setAttribute("partner_user_id", request.getParameter("partner_user_id"));
				System.out.println(successUrl);
				System.out.println(tid);
			} catch (ParseException e) {
				e.printStackTrace();
			} finally {
				in.close(); // 응답 스트림 닫기
			}
			
			System.out.print(con.getResponseCode() + " ");
			System.out.println(con.getResponseMessage());
			System.out.println(con.getRequestMethod());
			
			/*
			try {
				dispatch(request, response, "success.jsp");
			} catch (Exception e) {
				e.printStackTrace();
			}
			*/
			response.sendRedirect(successUrl); // 결제창으로 넘어가는 url
		} else if(command.equals("approval")) { // 결제 승인 통신
			HttpSession session = request.getSession();
			String cid = "TC0ONETIME";
			String tid = (String)session.getAttribute("tid");
			String pg_token = request.getParameter("pg_token");
			String partner_order_id = (String)session.getAttribute("partner_order_id");
			String partner_user_id = (String)session.getAttribute("partner_user_id");
			//System.out.println("pg_token" + " : " + pg_token);
			//System.out.println("partner_order_id : " + partner_order_id);
			//System.out.println("partner_user_id : " + partner_user_id);
			URL url = new URL("https://kapi.kakao.com/v1/payment/approve");
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", "KakaoAK ff69179230343e4b33048124ae42ebd0");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			con.setDoInput(true);
			con.setDoOutput(true);
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("cid", cid);
			params.put("tid", tid);
			params.put("pg_token", pg_token);
			params.put("partner_order_id", partner_order_id);
			params.put("partner_user_id", partner_user_id);
			
			String string_params = new String();
			for(Map.Entry<String, String> elem : params.entrySet()) {
				string_params += (elem.getKey() + "=" + elem.getValue() + "&");
			}
			
			System.out.println("string_params : " + string_params);
			
			OutputStream out = con.getOutputStream();
			out.write(string_params.getBytes());
			out.flush();
			out.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			JSONParser parser = null;
			JSONObject res = null;
			try {
				parser = new JSONParser();
				res = (JSONObject)parser.parse(in);
				System.out.println("결제 승인 결과 : " + res.toJSONString());
			} catch (ParseException e) {
				e.printStackTrace();
			} finally {
				in.close();
			}
			
			System.out.print(con.getResponseCode() + " ");
			System.out.println(con.getResponseMessage());
			System.out.println(con.getRequestMethod());
			
			request.setAttribute("approvalRes", res.toJSONString()); // 응답받은 json 파싱해서 request 속성 설정
			
			try {
				dispatch(request, response, "approvalRes.jsp"); // 결제 승인 시 응답받은 json을 파싱한걸 가지고 forward
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void dispatch(HttpServletRequest request, HttpServletResponse response, String url) throws Exception {
		RequestDispatcher dispatch = request.getRequestDispatcher(url);
		dispatch.forward(request, response);
	}
	
}
