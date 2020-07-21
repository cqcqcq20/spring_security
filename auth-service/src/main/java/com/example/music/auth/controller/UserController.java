package com.example.music.auth.controller;

import com.example.music.auth.basic.aop.ApiLog;
import com.example.music.auth.config.token.SmsCodeAuthenticationToken;
import com.example.music.auth.service.CustomUserDetailsService;
import com.example.music.common.annotation.CheckParam;
import com.example.music.common.annotation.CheckParams;
import com.example.music.common.exception.BasicErrorCode;
import com.example.music.common.exception.ErrorCode;
import com.example.music.common.rep.HttpResponse;
import com.example.music.common.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/password")
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Value("${app.password.matches}")
    public String matches;

    @PostMapping("forget")
    @ApiLog(module = "auth",desc = "忘记密码")
    @CheckParams({
            @CheckParam(value = Validator.NotNull, argName = "phone" , msg = "手机号不能为空"),
            @CheckParam(value = Validator.NotNull, argName = "area" , msg = "区号不能为空"),
            @CheckParam(value = Validator.NotNull, argName = "code" , msg = "验证码不能为空"),
            @CheckParam(value = Validator.NotNull, argName = "password" , msg = "密码不合法"),
    })
    public HttpResponse<?> forget(String phone,String area,String code,String password) {
        Authentication authentication = authenticationManager.authenticate(new SmsCodeAuthenticationToken(
                phone, code, area, "forget"
        ));
        User userPrincipal = (User) authentication.getPrincipal();

        if (!customUserDetailsService.updatePasswordById(userPrincipal.getUsername(),password)) {
            return HttpResponse.failure(BasicErrorCode.VALIDATOR_FAILURE_ERROR);
        }

        return HttpResponse.success();
    }

}
