package com.example.music.common.annotation;

import com.example.music.common.exception.BasicErrorCode;
import com.example.music.common.exception.ErrorCode;
import com.example.music.common.utils.Validator;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckParam {

    Validator value() default Validator.NotNull;

    String express() default "";

    String argName();

    String alias() default "";

    String msg() default "";

    BasicErrorCode code() default BasicErrorCode.VALIDATOR_FAILURE_ERROR;

}
