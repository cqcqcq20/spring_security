package com.example.music.user.controller;

import com.example.music.common.annotation.ApiLog;
import com.example.music.common.rep.HttpResponse;
import com.example.music.common.users.UserEntity;
import com.example.music.user.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("/profile")
    @ApiLog(module = "user",desc = "获取用户信息")
    public HttpResponse<UserEntity> profile(Principal principal) {
        return HttpResponse.success(customUserDetailsService.loadUserByUserId(principal.getName()));
    }

    @PostMapping("avatar")
    @ApiLog(module = "user",desc = "更新用户头像")
    public HttpResponse<?> updateAvatar(Principal principal, @RequestParam("avatar") MultipartFile file) {
        //todo 文件大小、类型判断
        String path = file.getOriginalFilename();
        customUserDetailsService.updateAvatar(principal.getName(),path);
        UserEntity userEntity = customUserDetailsService.loadUserByUserId(principal.getName());
        userEntity.setAvatar(path);
        return HttpResponse.success(userEntity);
    }
}
