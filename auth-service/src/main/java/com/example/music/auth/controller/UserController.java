package com.example.music.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    @GetMapping("oauth/user")
    public Object user(Principal principal) {
        return 122;
    }
}
