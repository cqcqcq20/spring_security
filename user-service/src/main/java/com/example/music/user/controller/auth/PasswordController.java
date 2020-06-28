package com.example.music.user.controller.auth;

import com.example.music.common.annotation.ApiLog;
import com.example.music.common.rep.HttpResponse;
import com.example.music.common.users.UserEntity;
import com.example.music.user.config.token.SmsCodeAuthenticationToken;
import com.example.music.user.excption.PasswordCode;
import com.example.music.user.mapper.UserMapper;
import com.example.music.user.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Value("${app.password.matches}")
    private String matches;

    @PostMapping("forget")
    @ApiLog(module = "user",desc = "重置密码")
    public Object forget(@RequestParam(value = "phone") String phone,
                         @RequestParam("area") String area,
                         @RequestParam("code") String code,
                         @RequestParam("password") String password) {
        Authentication authentication = authenticationManager.authenticate(new SmsCodeAuthenticationToken(
                phone, code, area, "forget"
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Principal userPrincipal = (Principal) authentication.getPrincipal();

        if (customUserDetailsService.updatePasswordById(userPrincipal.getName(),password) <= 0) {
            return HttpResponse.failure(PasswordCode.PASSWORD_UPDATE_FAILURE);
        }

        return HttpResponse.success();
    }

    @PostMapping("reset")
    @PreAuthorize("hasAnyAuthority('app')")
    @ApiLog(module = "user",desc = "修改密码")
    public Object reset(Principal principal, @RequestParam(value = "origin") String origin,
                        @RequestParam("password") String password) {

        UserEntity userDetails = customUserDetailsService.loadUserByUserId(principal.getName());
        if (!passwordEncoder.matches(origin,userDetails.getPassword())) {
            return HttpResponse.failure(PasswordCode.PASSWORD_WORD_NOT_MATCHES);
        }
        if (!password.matches(matches)) {
            return HttpResponse.failure(PasswordCode.PASSWORD_WORD_INVALID);
        }
        if (customUserDetailsService.updatePasswordById(principal.getName(),password) <= 0) {
            return HttpResponse.failure(PasswordCode.PASSWORD_UPDATE_FAILURE);
        }

        return HttpResponse.success();
    }
}
