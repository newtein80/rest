package com.service.rest.common.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class RestRequestInterceptor implements HandlerInterceptor {

    // todo: request audit entity & jpa --> save
	// @Autowired
	// private NileRestApiService nileRestApi;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 1. API 요청이 왔을때 API이력테이블에 INSERT
		// 2. INSERT의 RETURN 값으로 SEQ를 반환하여 WebRequest의 UID 속성 값으로 사용
		// 2-1 Exception 발생 시 해당 UID를 사용하여 정보 update
		// 2-1 정상 처리시 해당 UID를 사용하여 정보 update
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> api_logMap = null;
		Map<String, Object> apiKey_checkMap = null;
		int seq = 0;

		String url = request.getRequestURI().toString();
		String userIp = request.getRemoteAddr();

		paramMap.put("method", request.getMethod());
		paramMap.put("request_url", url);
		paramMap.put("user_ip", userIp);
		paramMap.put("class_name", "");
		
        // todo: request audit entity & jpa --> save
		// api_logMap = nileRestApi.insertApiLog(paramMap);

		System.out.println("===================================");
		System.out.println("RestRequestInterceptor.preHandle - insert request info !!!!");
		System.out.println("===================================");

        // 요쳥 식별키 설정
		// seq = (Integer) api_logMap.get("seq");
		// request.setAttribute("REQUESTID", seq);
		// request.setAttribute("AUTHINFO", api_logMap);

		// if (("POST".equalsIgnoreCase(request.getMethod()) && url.contains("/nile-ws/api/v1/authinfo")) || ("GET".equalsIgnoreCase(request.getMethod()) && url.contains("/nile-ws/api/v1/connection"))) {
		// 	;
		// }
		// else {
		// 	if (request.getHeader(HttpHeaders.AUTHORIZATION) == null) {
		// 		RestUnauthorizedException _ex = new RestUnauthorizedException("API-KEY 정보가 누락되었습니다.");
		// 		_ex.setCode(987);
		// 		throw _ex;
		// 	}
		// 	else {
		// 		String _apiKey = request.getHeader(HttpHeaders.AUTHORIZATION);
		// 		apiKey_checkMap = nileRestApi.checkAuthToken(_apiKey);
				
		// 		if (apiKey_checkMap == null || 1 > apiKey_checkMap.size()) {
		// 			RestUnauthorizedException _ex = new RestUnauthorizedException("API-KEY 정보가 존재하지 않습니다.");
		// 			_ex.setCode(789);
		// 			throw _ex;
		// 		}
		// 	}			
		// }
		
		// return super.preHandle(request, response, handler);
        return true;
	}
}