package com.service.rest.common.exception.exceptions;

import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * 추상 클래스 - 메서드 인터페이스 정의
 */
public abstract class AbstractRestBaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;
	
	public AbstractRestBaseException() {
		super();
	}
	
	public AbstractRestBaseException(String msg) {
		super(msg);
	}
	
	public AbstractRestBaseException(Throwable e) {
		super(e);
	}
	
	public AbstractRestBaseException(String errorMessage, Throwable e) {
		super(errorMessage, e);
	}

	public abstract HttpStatus getHttpStatus(); // Response Status (ex 404...)

	public abstract void setCode(int detailCode); // Response Code (ex 100001E...)
	public abstract int getCode();
	
	public abstract List<String> getErrorMessages(); // Exception Message (ex 메시지)
}
