package com.service.rest.security.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter // ! getter setter 자동 생성
@Setter
@Builder
@ToString
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