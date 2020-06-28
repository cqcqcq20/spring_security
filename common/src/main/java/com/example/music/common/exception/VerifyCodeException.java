package com.example.music.common.exception;

public class VerifyCodeException extends RuntimeException implements CommonErrorCode {

    private int code;

    public VerifyCodeException(String s) {
        super(s);
        this.code = CommonErrorCode.VERIFY_CODE_ERROR;
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
