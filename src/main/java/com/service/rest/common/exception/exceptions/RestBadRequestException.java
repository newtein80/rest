package com.service.rest.common.exception.exceptions;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

public class RestBadRequestException extends AbstractRestBaseException {

	private static final long serialVersionUID = 1L;
	
	private List<String> errorMessage;
	private int detailCode;
	
	public RestBadRequestException() {
		super();
	}
	
	public RestBadRequestException(Throwable e) {
		super(e);
	}
	
	public RestBadRequestException(String errorMessage) {
		super(errorMessage);
	}
	
	public RestBadRequestException(String errorMessage, Throwable e) {
		super(errorMessage, e);
		this.errorMessage = Arrays.asList(errorMessage);
	}
	
	public RestBadRequestException(List<String> errorMessages, Throwable e) {
		this.errorMessage = errorMessages;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.BAD_REQUEST;
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