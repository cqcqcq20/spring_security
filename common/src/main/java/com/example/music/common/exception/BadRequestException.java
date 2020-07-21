package com.example.music.common.exception;

public class BadRequestException extends RuntimeException implements ErrorCode {

    private int code;

    public BadRequestException(String s) {
        super(s);
        this.code = BasicErrorCode.VALIDATOR_FAILURE_ERROR.getCode();
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
