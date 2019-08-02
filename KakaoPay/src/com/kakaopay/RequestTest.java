package com.kakaopay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RequestTest {
	
	public static void main(String[] args) throws IOException {
		URL url = new URL("https://kapi.kakao.com/v1/payment/ready");
		//URL url = new URL("https://www.naver.com/");
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "KakaoAK ff69179230343e4b33048124ae42ebd0");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		con.setDoInput(true);
		con.setDoOutput(true);
		// System.out.print(con.getResponseCode() + " ");
		// System.out.println(con.getResponseMessage());
		// System.out.println(con.getRequestMethod());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", "TC0ONETIME");
		params.put("partner_order_id", "123");
		params.put("partner_user_id", "gorany");
		params.put("item_name", "초코파이");
		params.put("quantity", "1");
		params.put("total_amount", "10");
		params.put("tax_free_amount", "0");
		params.put("approval_url", "http://localhost:8088/KakaoPay/success.jsp");
		params.put("cancel_url", "http://localhost:8088/KakaoPay/success.jsp");
		params.put("fail_url", "http://localhost:8088/KakaoPay/success.jsp");
		/*
		for(Map.Entry<String, Object> elem : params.entrySet()) {
			String key = elem.getKey();
			if(elem.getValue() instanceof Integer) {
				System.out.println(key + " : " + Integer.parseInt(elem.getValue().toString()));
			} else {
			//String value = (String)elem.getValue();
				System.out.println(key + " : " + elem.getValue());
			}
		}
		*/
		
		String string_params = new String();
		for(Map.Entry<String, Object> elem : params.entrySet()) {
			string_params += (elem.getKey() + "=" + elem.getValue() + "&");
		}
		System.out.println(string_params.substring(0, string_params.length()-1));
		OutputStream out = con.getOutputStream();
		out.write(string_params.substring(0, string_params.length()).getBytes());
		out.flush();
		out.close();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String string_in = null;
		String tid = null;
		try {
			JSONParser parser = new JSONParser();
			JSONObject object = (JSONObject)parser.parse(in);
			System.out.println(object.get("next_redirect_pc_url"));
			System.out.println(object.get("tid"));
			//string_in = object.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(string_in);
		/*
		while(in.readLine() != null) {
			//string_in += in.readLine();
			System.out.println(in.readLine());
		}
		//System.out.println(string_in);
		*/
		
		in.close();
		
		System.out.print(con.getResponseCode() + " ");
		System.out.println(con.getResponseMessage());
		System.out.println(con.getRequestMethod());
		
	}

}
