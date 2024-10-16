package com.task.feign;

import lombok.Getter;

@Getter
public class TossException extends RuntimeException{
    private String code;
    private String message;
    private int status;

    public TossException( String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
