package com.example.music.common.annotation;

import com.example.music.common.exception.CommonErrorCode;
import com.example.music.common.utils.Validator;
import com.example.music.common.utils.ValidatorUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckParam {

    Validator value() default Validator.NotNull;

    String express() default "";

    String argName();

    String msg() default "";

    int code() default CommonErrorCode.VALIDATOR_FAILURE_ERROR;

}
