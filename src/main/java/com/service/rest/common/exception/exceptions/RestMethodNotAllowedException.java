package com.service.rest.common.exception.exceptions;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

public class RestMethodNotAllowedException extends AbstractRestBaseException {

	private static final long serialVersionUID = 1L;
	
	private List<String> errorMessage;
	private int detailCode;

	public RestMethodNotAllowedException() {
		super();
	}
	
	public RestMethodNotAllowedException(Throwable e) {
		super(e);
	}
	
	public RestMethodNotAllowedException(String errorMessage) {
		super(errorMessage);
	}
	
	public RestMethodNotAllowedException(String errorMessage, Throwable e) {
		super(errorMessage, e);
		this.errorMessage = Arrays.asList(errorMessage);
	}
	
	public RestMethodNotAllowedException(List<String> errorMessages, Throwable e) {
		this.errorMessage = errorMessages;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.METHOD_NOT_ALLOWED;
	}
	
	@Override
	public List<String> getErrorMessages() {
		return errorMessage;
	}

	@Override
	public void setCode(int code) {
		this.detailCode = code;
	}

	@Override
	public int getCode() {
		return this.detailCode;
	}

}