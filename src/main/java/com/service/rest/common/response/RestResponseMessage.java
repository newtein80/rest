package com.service.rest.common.response;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import com.service.rest.common.exception.exceptions.AbstractRestBaseException;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.NoHandlerFoundException;

public class RestResponseMessage {
    private static final String DEFAULT_KEY = "result";

    /**
     * 응답 코드 ex 100001E
     */
	private int code;
    /**
     * Http 응답 코드 ex 404
     */
	private boolean status;
    /**
     * 메시지
     */
	private String message;
    /**
     * 응답 시간
     */
	private Date timestamp; // java.util.Date;
    /**
     * 응답 데이터
     */
	private Map<String, Object> data;
    /**
     * 에러 발생 시 상세
     */
	private RestErrorMessage error;

	public RestResponseMessage() {
		this(HttpStatus.OK);
	}
	
	public RestResponseMessage(HttpStatus httpStatus) {
		this.code = httpStatus.value();
		this.status = (httpStatus.is4xxClientError() || httpStatus.is5xxServerError()) ? false : true ;
		this.message = httpStatus.getReasonPhrase();
		this.timestamp = new Date();
		this.data = new HashMap<String, Object>();
	}
	
	public RestResponseMessage(AbstractRestBaseException ex, String refererUrl, HttpStatus i_httpStatus) {
		HttpStatus httpStatus = i_httpStatus;//ex.getHttpStatus();
		this.code = httpStatus.value();
		this.status = (httpStatus.is4xxClientError() || httpStatus.is5xxServerError()) ? false : true ;
		this.message = httpStatus.getReasonPhrase();
		this.timestamp = new Date();
		this.data = new HashMap<String, Object>();
		this.error = new RestErrorMessage(ex.getCode(), ex.getErrorMessages(), refererUrl);
	}
	
	public RestResponseMessage(ServletException ex, String refererUrl, HttpStatus i_httpStatus) {
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		
		if (ex instanceof HttpRequestMethodNotSupportedException) {
			httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
		}
		else if (ex instanceof HttpMediaTypeNotSupportedException) {
			httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
		}
		else if (ex instanceof NoHandlerFoundException) {
			httpStatus = HttpStatus.NOT_FOUND;
		}
		else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		this.code = httpStatus.value();
		this.status = (httpStatus.is4xxClientError() || httpStatus.is5xxServerError()) ? false : true ;
		this.message = httpStatus.getReasonPhrase();
		this.timestamp = new Date();
		this.data = new HashMap<String, Object>();
		this.error = new RestErrorMessage(2001, ex.getMessage(), refererUrl);
	}
	
	public RestResponseMessage(HttpStatus status, Object result) {
		this(status);
		this.data.put(DEFAULT_KEY, result);
	}
	
	public void add(String key, Object result) {
		this.data.put(key, result);
	}
	
	public void remove(String key) {
		this.data.remove(key);
	}

	public static String getDefaultKey() {
		return DEFAULT_KEY;
	}

	public int getCode() {
		return code;
	}

	public boolean isStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public RestErrorMessage getError() {
		return error;
	}
}
