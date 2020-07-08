package com.example.music.common.exception;

public class AppException extends RuntimeException implements CommonErrorCode {

    private int code;

    public AppException(String s) {
        this(CommonErrorCode.ERROR_CODE_COMMON,s);
    }

    public AppException(int code,String s) {
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
