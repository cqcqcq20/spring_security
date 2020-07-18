package com.example.music.auth.aop;

import com.example.music.common.rep.HttpResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TokenAop {

    @Around(value = "execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))")
    public Object doSomething(ProceedingJoinPoint joinPoint) throws Throwable {
        ResponseEntity proceed = (ResponseEntity) joinPoint.proceed();
        if (proceed != null && proceed.getBody() instanceof OAuth2AccessToken) {
            return ResponseEntity.ok(HttpResponse.success(proceed.getBody()));
        }
        return proceed;
    }
}
