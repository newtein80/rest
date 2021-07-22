package com.service.rest.security.exception;

import java.util.Arrays;
import java.util.List;

import com.service.rest.common.exception.exceptions.AbstractRestBaseException;

import org.springframework.http.HttpStatus;

public class TokenRefreshException extends AbstractRestBaseException {

	private static final long serialVersionUID = 1L;
	
	private List<String> errorMessage;
	private int detailCode;
	
	public TokenRefreshException() {
		super();
		this.errorMessage = Arrays.asList("The token has expired. Please make a new signin request.[DEFAULT MESSAGE]");
	}
	
	public TokenRefreshException(Throwable e) {
		super(e);
	}
	
	public TokenRefreshException(String errorMessage) {
		super(errorMessage);
	}
	
	public TokenRefreshException(String errorMessage, Throwable e) {
		super(errorMessage, e);
		this.errorMessage = Arrays.asList(errorMessage);
	}
	
	public TokenRefreshException(List<String> errorMessages, Throwable e) {
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