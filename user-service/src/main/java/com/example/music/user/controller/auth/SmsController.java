package com.example.music.user.controller.auth;

import com.example.music.common.annotation.ApiLog;
import com.example.music.common.rep.HttpResponse;
import com.example.music.user.service.RedisTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private RedisTokenService redisTokenService;

    @PostMapping("send")
    @ApiLog(module = "user",desc = "发送验证码")
    public Object sendCode(@RequestParam(value = "type") String type,
                           @RequestParam("phone") String phone,
                           @RequestParam("area") String area) {
        return HttpResponse.success(redisTokenService.generateSmsCode(phone, area,type));
    }
}
