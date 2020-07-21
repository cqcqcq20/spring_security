package com.example.music.common.exception;

public enum BasicErrorCode implements ErrorCode {
    SUCCESS(0,"成功"),
    ERROR_CODE_COMMON(10001),
    ERROR_CODE_SERVER_500(500,"服务发生异常或服务器繁忙"),
    VALIDATOR_FAILURE_ERROR(10002,"参数错误"),
    ;

    private int code;

    private String msg;

    BasicErrorCode(int code) {
        this.code = code;
    }

    BasicErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public BasicErrorCode setCode(int code) {
        this.code = code;
        return this;
    }

    public BasicErrorCode setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
