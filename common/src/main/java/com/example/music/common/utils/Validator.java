package com.example.music.common.utils;

import com.example.music.common.exception.CommonErrorCode;
import com.example.music.common.utils.ValidatorUtil;

import java.util.function.BiFunction;

public enum Validator {

    NotNull("参数不能为空", ValidatorUtil::isNotNull),

    FileContentType("文件类型错误", ValidatorUtil::isContentType);

    public String msg;
    public BiFunction<Object, String, Boolean> fun;

    Validator(String msg, BiFunction<Object, String, Boolean> fun) {
        this.msg = msg;
        this.fun = fun;
    }

}
