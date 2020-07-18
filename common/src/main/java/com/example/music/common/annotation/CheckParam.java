package com.example.music.common.annotation;

import com.example.music.common.exception.CommonErrorCode;
import com.example.music.common.utils.Validator;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckParam {

    Validator value() default Validator.NotNull;

    String express() default "";

    String argName();

    String msg() default "";

    int code() default CommonErrorCode.VALIDATOR_FAILURE_ERROR;

}
