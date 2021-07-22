package com.service.rest.common.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 공통 응답 클래스 - 기본 응답
 */
@Getter
@Setter
public class Response {
    private String response;
    private String message;
    private Object data;

    public Response(String response, String message, Object data) {
        this.response = response;
        this.message = message;
        this.data = data;
    }
}
