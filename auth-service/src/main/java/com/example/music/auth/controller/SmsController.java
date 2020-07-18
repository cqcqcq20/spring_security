package com.example.music.auth.controller;

import com.example.music.auth.basic.aop.ApiLog;
import com.example.music.auth.service.RedisTokenService;
import com.example.music.common.annotation.CheckParam;
import com.example.music.common.annotation.CheckParams;
import com.example.music.common.rep.HttpResponse;
import com.example.music.common.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private RedisTokenService redisTokenService;

    @PostMapping("send")
    @ApiLog(module = "auth" ,desc = "发送验证码")
    @CheckParams({
            @CheckParam(value = Validator.NotNull, argName = "type" , msg = "手机号不能为空"),
            @CheckParam(value = Validator.NotNull, argName = "phone" , msg = "区号不能为空"),
            @CheckParam(value = Validator.NotNull, argName = "area" , msg = "验证码不能为空"),
    })
    public HttpResponse<?> sendCode(String type, String phone, String area) {
        return HttpResponse.success(redisTokenService.generateSmsCode(phone, area,type));
    }
}
