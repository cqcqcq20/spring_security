package com.example.music.auth.basic.aop;

import com.example.music.common.exception.BasicErrorCode;
import com.example.music.common.exception.ErrorCode;
import com.example.music.common.rep.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.Ordered;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于记录apiLog修饰的请求
 */
@Aspect
@Component
@Slf4j
public class LogAspect extends LogRecordBuilder implements Ordered {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public int getOrder() {
        return 0;
    }

    @AfterReturning(value = "@annotation(apiLog)" ,returning = "response")
    public void doAfterReturn(JoinPoint joinPoint, ApiLog apiLog,Object response) throws Throwable {
        JSONObject message = connectMessage(joinPoint,getPrincipal(),  apiLog.module(),apiLog.desc());
        if (response instanceof HttpResponse) {
            HttpResponse<?> httpResponse = (HttpResponse<?>) response;
            message.put("code", httpResponse.getCode());
            message.put("msg", httpResponse.getMsg());
        }
        sendToQueue( message.toString());
        log.info(message.toString());
    }

    private long getPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null && !"anonymousUser".equals(principal) && principal instanceof String) {
            return Long.parseLong(principal.toString());
        }
        return -1;
    }

    @AfterThrowing(throwing = "ex", value = "@annotation(apiLog)")
    public void doThrowing(JoinPoint joinPoint, Throwable ex, ApiLog apiLog) throws Throwable {
        JSONObject message = connectMessage(joinPoint,getPrincipal(), apiLog.module(),apiLog.desc());
        HttpResponse<?> failure = HttpResponse.failure(BasicErrorCode.ERROR_CODE_SERVER_500);
        if (ex instanceof ErrorCode) {
            ErrorCode errorCode = (ErrorCode) ex;
            message.put("code", errorCode.getCode());
            message.put("msg", errorCode.getMsg());
            failure = HttpResponse.failure(errorCode);
        } else if (ex.getStackTrace() != null && ex.getStackTrace().length > 0) {
            StackTraceElement stackTraceElement = ex.getStackTrace()[0];
            String msg = String.format("%s[%d]", stackTraceElement.getClassName(), stackTraceElement.getLineNumber());
            message.put("code", failure.getCode());
            message.put("msg", msg);
        }
        log.info(message.toString());
        sendToQueue(message.toString());

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), failure);
        } catch (Exception e) {
            throw new ServletException();
        }
    }

    private void sendToQueue(String data) {
        kafkaTemplate.send("service-request-log", data);
    }

}
