package com.example.music.common.exception;

public class ValidatorException extends RuntimeException implements CommonErrorCode {

    private int code;

    public ValidatorException(String s, int code) {
        super(s);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return getMessage();
    }
}
