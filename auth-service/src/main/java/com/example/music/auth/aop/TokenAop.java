package com.example.music.auth.aop;

import com.example.music.auth.basic.aop.LogRecordBuilder;
import com.example.music.common.exception.BasicErrorCode;
import com.example.music.common.exception.ErrorCode;
import com.example.music.common.rep.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于基于oauth2运行时出现的错误
 */
@Aspect
@Component
public class TokenAop extends LogRecordBuilder {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Around(value = "execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))")
    public ResponseEntity<?> doSomething(ProceedingJoinPoint joinPoint) throws Throwable {
        ResponseEntity<?> proceed = (ResponseEntity<?>) joinPoint.proceed();
        JSONObject message = connectMessage(joinPoint, -1,"auth", "获取accessToken");
        if (proceed.getBody() instanceof OAuth2AccessToken) {
            HttpResponse<?> success = HttpResponse.success(proceed.getBody());
            message.put("code", success.getCode());
            message.put("msg", success.getMsg());
            proceed = ResponseEntity.ok(success);
        }
        kafkaTemplate.send("service-request-log",message.toString());
        return proceed;
    }

    @AfterThrowing(throwing = "ex" ,pointcut = "execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))")
    public void doAfterThrowing(JoinPoint joinPoint,Throwable ex) throws Throwable {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        JSONObject message = connectMessage(joinPoint, -1,"auth", "获取accessToken");
        HttpResponse<Object> failure = HttpResponse.failure(BasicErrorCode.ERROR_CODE_SERVER_500);
        if (ex instanceof ErrorCode) {
            ErrorCode errorCode = (ErrorCode) ex;
            message.put("code", errorCode.getCode());
            message.put("msg", errorCode.getMsg());
            failure = HttpResponse.failure(errorCode);
        } else if (ex instanceof OAuth2Exception) {
            OAuth2Exception oAuth2Exception = (OAuth2Exception) ex;
            int code = BasicErrorCode.VALIDATOR_FAILURE_ERROR.getCode();
            message.put("code", code);
            message.put("msg", oAuth2Exception.getMessage());
            failure = HttpResponse.failure(code,oAuth2Exception.getMessage());
        } else {
            message.put("code", BasicErrorCode.ERROR_CODE_SERVER_500.getCode());
            message.put("msg", BasicErrorCode.ERROR_CODE_SERVER_500.getMsg());
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), failure);
        } catch (Exception e) {
            throw new ServletException();
        }
        kafkaTemplate.send("service-request-log",message.toString());
    }

    private String getExStack(Throwable ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        if (stackTrace != null && stackTrace.length > 0) {
            return String.format("%s(%s)[%s]",ex.getMessage(),stackTrace[0].getClassName(),stackTrace[0].getLineNumber());
        }
        return ex.getMessage();
    }
}
