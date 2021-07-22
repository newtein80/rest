package com.service.rest.common.response;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * 에러 상태 상세
 */
public class RestErrorStatus {
    // String error;
	private List<String> errors;
    private HttpStatus httpStatus;
    private int errorcode;

	public RestErrorStatus(HttpStatus httpStatus, int errorcode, List<String> errors) {
        super();
        this.httpStatus = httpStatus;
        this.errorcode = errorcode;
        this.errors = errors;
    }

	public RestErrorStatus(HttpStatus httpStatus, int errorcode, String error) {
        super();
        this.httpStatus = httpStatus;
        this.errorcode = errorcode;
        errors = Arrays.asList(error); // String  -> List<String>
    }

	/**
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * @param errors the errors to set
	 */
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	/**
	 * @return the httpStatus
	 */
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	/**
	 * @param httpStatus the httpStatus to set
	 */
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	/**
	 * @return the errorcode
	 */
	public int getErrorcode() {
		return errorcode;
	}

	/**
	 * @param errorcode the errorcode to set
	 */
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}

	public void setError(String error) {
		this.errors = Arrays.asList(error);
	}
}
