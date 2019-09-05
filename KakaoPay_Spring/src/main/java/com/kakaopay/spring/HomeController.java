package com.kakaopay.spring;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		return "index";
	}

	@RequestMapping(value = "/pay.do", method = RequestMethod.POST)
	public String pay(Model model, HttpServletRequest request) {
		RestTemplate restTemplate = new RestTemplate();
		String uri = "https://kapi.kakao.com/v1/payment/ready";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "KakaoAK ff69179230343e4b33048124ae42ebd0");
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
//        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        
        
        
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("cid", "TC0ONETIME");
		params.add("partner_order_id", request.getParameter("partner_order_id"));
		params.add("partner_user_id", request.getParameter("partner_user_id"));
		params.add("item_name", request.getParameter("item_name"));
		params.add("quantity", request.getParameter("quantity"));
		params.add("total_amount", request.getParameter("total_amount"));
		params.add("tax_free_amount", request.getParameter("tax_free_amount"));
		params.add("approval_url", request.getParameter("approval_url"));
		params.add("cancel_url", request.getParameter("cancel_url"));
		params.add("fail_url", request.getParameter("fail_url"));

		HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);
//		KakaoPayDto dto = restTemplate.postForObject(uri, body, KakaoPayDto.class);
//		System.out.println(dto);
		System.out.println(restTemplate.postForObject(uri, body, String.class));

		return "index";
	}
}
