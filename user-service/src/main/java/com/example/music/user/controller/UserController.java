package com.example.music.user.controller;

import com.example.music.common.annotation.CheckParam;
import com.example.music.common.annotation.CheckParams;
import com.example.music.common.rep.HttpResponse;
import com.example.music.auth.basic.aop.ApiLog;
import com.example.music.common.users.UserEntity;
import com.example.music.common.utils.Validator;
import com.example.music.user.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('app')")
    @ApiLog(module = "user",desc = "获取用户信息")
    public HttpResponse<UserEntity> profile(Principal principal) {
        return HttpResponse.success(customUserDetailsService.loadUserByUserId(principal.getName()));
    }

    @PostMapping("avatar")
    @PreAuthorize("hasAuthority('app')")
    @ApiLog(module = "user",desc = "更新用户头像")
    @CheckParams({
            @CheckParam(value = Validator.FileContentType,argName = "avatar",express = "image/jpeg,image/png",msg = "请上传png、jpeg图片",code = 10003),
    })
    public HttpResponse<?> updateAvatar(Principal principal, @RequestParam("avatar") MultipartFile avatar) {
        String path = avatar.getOriginalFilename();
        customUserDetailsService.updateAvatar(principal.getName(),path);
        UserEntity userEntity = customUserDetailsService.loadUserByUserId(principal.getName());
        userEntity.setAvatar(path);
        return HttpResponse.success(userEntity);
    }
}
