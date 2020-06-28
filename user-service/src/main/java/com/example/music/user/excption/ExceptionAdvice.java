package com.example.music.user.excption;

import com.example.music.common.exception.AppException;
import com.example.music.common.exception.CommonErrorCode;
import com.example.music.common.exception.VerifyCodeException;
import com.example.music.common.rep.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.OK)
    public HttpResponse<?> apiException(Exception exception) {
        return HttpResponse.failure(CommonErrorCode.ERROR_CODE_COMMON,exception.getMessage());
    }

    @ExceptionHandler(VerifyCodeException.class)
    @ResponseStatus(code = HttpStatus.OK)
    public HttpResponse<?> apiException(VerifyCodeException exception) {
        return HttpResponse.failure(exception.getCode(),exception.getMessage());
    }
}
