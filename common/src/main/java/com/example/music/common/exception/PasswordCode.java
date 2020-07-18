package com.example.music.common.exception;

public enum PasswordCode implements CommonErrorCode {
    PASSWORD_WORD_NOT_MATCHES(10022,"password not matches"),
    PASSWORD_WORD_INVALID(10023,"password invalid"),
    PASSWORD_UPDATE_FAILURE(10024,"password update failure");

    private int code;

    private String msg;

    PasswordCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
