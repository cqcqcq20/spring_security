package com.example.music.user.controller.auth;

import com.example.music.common.annotation.CheckParam;
import com.example.music.common.annotation.CheckParams;
import com.example.music.common.exception.BasicErrorCode;
import com.example.music.common.exception.ErrorCode;
import com.example.music.common.rep.HttpResponse;
import com.example.music.common.users.UserEntity;
import com.example.music.auth.basic.aop.ApiLog;
import com.example.music.common.utils.Validator;
import com.example.music.user.config.token.CustomRemoteTokenServices;
import com.example.music.user.excption.UserErrorCode;
import com.example.music.user.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController()
@RequestMapping("/password")
public class PasswordController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomRemoteTokenServices customRemoteTokenServices;

    @Value("${app.password.matches}")
    private String matches;

    @PostMapping("reset")
    @PreAuthorize("hasAnyAuthority('app')")
    @ApiLog(module = "user",desc = "修改密码")
    @CheckParams({
            @CheckParam(value = Validator.Password, argName = "oldPassword"),
            @CheckParam(value = Validator.Password, argName = "password"),
            @CheckParam(value = Validator.NotNull, argName = "refreshToken"),
    })
    public Object reset(Principal principal, String oldPassword, String password,String refreshToken) {

        UserEntity userDetails = customUserDetailsService.loadUserByUserId(principal.getName());
        if (!passwordEncoder.matches(oldPassword,userDetails.getPassword())) {
            return HttpResponse.failure(BasicErrorCode.VALIDATOR_FAILURE_ERROR);
        }
//        if (!password.matches(matches)) {
//            return HttpResponse.failure(PasswordCode.PASSWORD_WORD_INVALID);
//        }
        if (customUserDetailsService.updatePasswordById(principal.getName(),password) <= 0) {
            return HttpResponse.failure(BasicErrorCode.VALIDATOR_FAILURE_ERROR);
        }

        customRemoteTokenServices.refreshToken(refreshToken);

        return HttpResponse.success();
    }
}
