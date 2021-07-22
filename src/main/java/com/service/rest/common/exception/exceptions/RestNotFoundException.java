package com.service.rest.common.exception.exceptions;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

public class RestNotFoundException extends AbstractRestBaseException {

	private static final long serialVersionUID = 1L;
	
	private List<String> errorMessage;
	private int detailCode;

	public RestNotFoundException() {
		super();
	}
	
	public RestNotFoundException(Throwable e) {
		super(e);
	}
	
	public RestNotFoundException(String errorMessage) {
		super(errorMessage);
	}
	
	public RestNotFoundException(String errorMessage, Throwable e) {
		super(errorMessage, e);
		this.errorMessage = Arrays.asList(errorMessage);
	}
	
	public RestNotFoundException(List<String> errorMessages, Throwable e) {
		this.errorMessage = errorMessages;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.NOT_FOUND;
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