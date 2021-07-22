package com.service.rest.common.response;

import java.util.Arrays;
import java.util.List;

/**
 * 에러 메시지 상세
 */
public class RestErrorMessage {
    int code;
	List<String> errorMessage;
	String refererUrl;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	public List<String> getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessages(List<String> errorMessage) {
		this.errorMessage = errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = Arrays.asList(errorMessage);
	}
	
	public String getRefererUrl() {
		return refererUrl;
	}
	public void setRefererUrl(String refererUrl) {
		this.refererUrl = refererUrl;
	}
	
	public RestErrorMessage(int code, List<String> errorMessage, String refererUrl) {
		super();
		this.code = code;
		this.errorMessage = errorMessage;
		this.refererUrl = refererUrl;
	}
	public RestErrorMessage(int code, String errorMessage, String refererUrl) {
		super();
		this.code = code;
		this.errorMessage = Arrays.asList(errorMessage);
		this.refererUrl = refererUrl;
	}
}
