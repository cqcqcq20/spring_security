package com.example.music.auth.basic.aop;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogRecordBuilder {

    public static final String[] hidden = new String[] {
            "[\\s\\S]{0,}password[\\s\\S]{0,}",
            "[\\s\\S]{0,}pwd[\\s\\S]{0,}",
            "code",
    };

    protected JSONObject connectMessage(JoinPoint joinPoint,long principal, String module, String desc) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        JSONObject message = new JSONObject();
        message.put("module", module);
        message.put("desc", desc);
        long uid = -1;
        if (principal > 0) {
            uid = principal;
        }

        message.put("uid", uid);

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
        message.put("time", dateFormat.format(now));//时间

        message.put("ip", getIpAddr(request));
        message.put("port", request.getRemotePort());
        message.put("url", String.format("%s%s", module, request.getRequestURI()));//url
        message.put("method", request.getMethod());//请求方法
        message.put("entry", joinPoint.getSignature().getName());//调用方法
        message.put("params", mapToString(request.getParameterMap()));//参数

        return message;
    }

    //如果不需要ip地址，这段可以省略
    public String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
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

    private String mapToString(Map<String, String[]> stringStringMap) {
        Set<Map.Entry<String, String[]>> entries = stringStringMap.entrySet();
        Iterator<Map.Entry<String, String[]>> iterator = entries.iterator();
        Map<String, String> params = new HashMap<>();
        while (iterator.hasNext()) {
            Map.Entry<String, String[]> next = iterator.next();

            String value = next.getValue() != null && next.getValue().length > 0 ? next.getValue()[0] : "N/A";
            String key = next.getKey();
            for (String s : hidden) {
                if (key.matches(s)) {
                    value = "*";
                    break;
                }
            }

            params.put(key, value);
        }
        return JSON.toJSONString(params);
    }
}
