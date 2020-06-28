package com.example.music.common.rep;

import com.example.music.common.exception.CommonErrorCode;

public class HttpResponse<T> {



    public static final String MSG_SUCCESS = "success";
    public static final String MSG_FAILURE = "failure";

    private int code = CommonErrorCode.SUCCESS;

    private String msg = MSG_SUCCESS;

    private T payload;

    public HttpResponse() {
    }

    public HttpResponse(int code) {
        this.code = code;
    }

    public HttpResponse(T payload) {
        this.payload = payload;
    }

    public HttpResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public HttpResponse(int code, String msg, T payload) {
        this.code = code;
        this.msg = msg;
        this.payload = payload;
    }

    public HttpResponse code(int code) {
        this.code = code;
        return this;
    }

    public HttpResponse msg(String msg) {
        this.msg = msg;
        return this;
    }

    public HttpResponse payload(T payload) {
        this.payload = payload;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public static <T> HttpResponse<T> success() {
        return new HttpResponse<T>();
    }

    public static <T> HttpResponse<T> success(int code) {
        return new HttpResponse<T>(code);
    }

    public static <T> HttpResponse<T> success(T payload) {
        return new HttpResponse<T>(payload);
    }

    public static <T> HttpResponse<T> failure() {
        return new HttpResponse<T>(CommonErrorCode.ERROR_CODE_COMMON,MSG_FAILURE);
    }

    public static <T> HttpResponse<T> failure(int code) {
        return new HttpResponse<T>(code,MSG_FAILURE);
    }

    public static <T> HttpResponse<T> failure(CommonErrorCode code) {
        return new HttpResponse<T>(code.getCode(),code.getMsg());
    }

    public static <T> HttpResponse<T> failure(int code,String msg) {
        return new HttpResponse<T>(code,msg);
    }

    public static <T> HttpResponse<T> failure(String msg) {
        return new HttpResponse<T>(CommonErrorCode.ERROR_CODE_COMMON,msg);
    }

}
