package com.example.music.common.exception;

public interface CommonErrorCode {

    int SUCCESS = 0;

    int ERROR_CODE_COMMON = 10001;

    int ERROR_CODE_SERVER_500 = 500;

    int VERIFY_CODE_ERROR = 10020;

    int VALIDATOR_FAILURE_ERROR = 10002;

    int getCode();

    String getMsg();
}
