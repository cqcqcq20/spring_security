package com.example.music.user.aop;

import com.example.music.common.annotation.ApiLog;
import com.example.music.common.rep.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @Around("@annotation(apiLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, ApiLog apiLog) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Object result = null;
        JSONObject message = new JSONObject();
        message.put("module",apiLog.module());
        message.put("desc",apiLog.desc());
        long uid = -1;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null && !"anonymousUser".equals(principal)) {
            uid = Long.parseLong(principal.toString());
        }

        message.put("uid",uid);

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
        message.put("time", dateFormat.format(now));//时间

        message.put("ip", getIpAddr(request));
        message.put("port", request.getRemotePort());
        message.put("url", request.getRequestURI());//url
        message.put("method", request.getMethod());//请求方法
        message.put("entry", joinPoint.getSignature().getName());//调用方法
        message.put("params",mapToString(request.getParameterMap()));//参数

        result = joinPoint.proceed();
        if (result instanceof HttpResponse) {
            HttpResponse<?> response = (HttpResponse<?>) result;
            message.put("code",response.getCode());
            message.put("msg",response.getMsg());

        }
        kafkaTemplate.send("service-request-log",message.toString());
        log.info(message.toString());
        return result;
    }

    //如果不需要ip地址，这段可以省略
    public String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        //或者这样也行,对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        //return ipAddress!=null&&!"".equals(ipAddress)?ipAddress.split(",")[0]:null;
        return ipAddress;
    }

    private String mapToString(Map<String,String[]> stringStringMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Set<Map.Entry<String, String[]>> entries = stringStringMap.entrySet();
        Iterator<Map.Entry<String, String[]>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String[]> next = iterator.next();
            sb.append(next.getKey())
                    .append("=")
                    .append(Arrays.toString(next.getValue()));
            if (iterator.hasNext()) {
                sb.append(",");
            }
        }

        sb.append("]");
        return sb.toString();
    }
}
