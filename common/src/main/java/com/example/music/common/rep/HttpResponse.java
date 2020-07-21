package com.example.music.common.rep;

import com.example.music.common.exception.BasicErrorCode;
import com.example.music.common.exception.ErrorCode;
import com.example.music.common.serializer.ResponseSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(using = ResponseSerializer.class)
public class HttpResponse<T> {

    public static final String MSG_SUCCESS = "success";
    public static final String MSG_FAILURE = "failure";

    private int code;

    private String msg = MSG_SUCCESS;

    private T payload;

    private HashMap<String,String> header;

    public HttpResponse() {
    }

    public HttpResponse(int code) {
        this.code = code;
    }

    public HttpResponse(T payload) {
        this.payload = payload;
    }

    public HttpResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
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

    public HttpResponse addHeader(String key,String value) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(key,value);
        return this;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public static <T> HttpResponse<T> success() {
        return new HttpResponse<T>(BasicErrorCode.SUCCESS);
    }

    public static <T> HttpResponse<T> success(int code) {
        return new HttpResponse<T>(code);
    }

    public static <T> HttpResponse<T> success(T payload) {
        return new HttpResponse<T>(payload);
    }

    public static <T> HttpResponse<T> failure() {
        return new HttpResponse<T>(BasicErrorCode.ERROR_CODE_COMMON);
    }

    public static <T> HttpResponse<T> failure(int code) {
        return new HttpResponse<T>(code,MSG_FAILURE);
    }

    public static <T> HttpResponse<T> failure(ErrorCode code) {
        return new HttpResponse<T>(code.getCode(),code.getMsg());
    }

    public static <T> HttpResponse<T> failure(int code,String msg) {
        return new HttpResponse<T>(code,msg);
    }

    public static <T> HttpResponse<T> failure(String msg) {
        return new HttpResponse<T>(BasicErrorCode.ERROR_CODE_COMMON.setMsg(msg));
    }

}
