package com.example.music.common.utils;

import java.util.function.BiFunction;

public enum Validator {

    NotNull("参数不能为空", ValidatorUtil::isNotNull),

    FileContentType("文件类型错误", ValidatorUtil::isContentType),

    MatchRegex("校验未通过", ValidatorUtil::isMatchRegex),

    Password("密码不合法", ValidatorUtil::password);

    public String msg;
    public BiFunction<Object, String, Boolean> fun;

    Validator(String msg, BiFunction<Object, String, Boolean> fun) {
        this.msg = msg;
        this.fun = fun;
    }

}
