package com.service.rest.security.exception;

import java.util.Arrays;
import java.util.List;

import com.service.rest.common.exception.exceptions.AbstractRestBaseException;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AbstractRestBaseException {

	private static final long serialVersionUID = 1L;
	
	private List<String> errorMessage;
	private int detailCode;
	
	public UserNotFoundException() {
		super();
		this.errorMessage = Arrays.asList("User not found.[DEFAULT MESSAGE]");
	}
	
	public UserNotFoundException(Throwable e) {
		super(e);
	}
	
	public UserNotFoundException(String errorMessage) {
		super(errorMessage);
	}
	
	public UserNotFoundException(String errorMessage, Throwable e) {
		super(errorMessage, e);
		this.errorMessage = Arrays.asList(errorMessage);
	}
	
	public UserNotFoundException(List<String> errorMessages, Throwable e) {
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