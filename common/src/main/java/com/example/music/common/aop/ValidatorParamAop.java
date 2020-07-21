package com.example.music.common.aop;

import com.alibaba.fastjson.JSONObject;
import com.example.music.common.annotation.CheckParam;
import com.example.music.common.annotation.CheckParams;
import com.example.music.common.rep.HttpResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Aspect
@Component
public class ValidatorParamAop implements Ordered {

    @Override
    public int getOrder() {
        return 0;
    }

    @Around("@annotation(checkParam)")
    public Object check1(ProceedingJoinPoint point,CheckParam checkParam) throws Throwable {
        Object obj;
        // 参数校验
        obj = doCheck(point, checkParam);
        if (obj != null) {
            return obj;
        }
        // 通过校验，继续执行原有方法
        obj = point.proceed();
        return obj;
    }

    @Around("@annotation(checkParams)") // 这里要换成自定义注解的路径
    public Object check2(ProceedingJoinPoint point,CheckParams checkParams) throws Throwable {
        Object obj;
        // 参数校验
        CheckParam[] value = checkParams.value();
        for (CheckParam item : value) {
            obj = doCheck(point,item);
            if (obj != null) {
                return obj;
            }
        }
        // 通过校验，继续执行原有方法
        obj = point.proceed();
        return obj;
    }

    /**
     * 参数校验
     *
     * @param point 切点
     * @param anno 多参数校验
     * @return 错误信息
     */
    private Object doCheck(JoinPoint point, CheckParam anno) {
        String[] paramName = this.getParamName(point);
        Object[] arguments = point.getArgs();    // 获取接口传递的所有参数
        String argName = anno.argName();
        Object value = this.getParamValue(arguments, paramName, argName);    //参数值
        boolean isValid = anno.value().fun.apply(value, anno.express());    // 执行判断 // 调用枚举类的 CheckUtil类方法
        if (isValid) {
            return null;
        }
        String alias = anno.alias();
        String msg = String.format("%s%s", StringUtils.isEmpty(alias) ? argName : alias,anno.msg());
        if ("".equals(msg)) {
            msg = argName + ": " + anno.value().msg + " " + anno.express();
        }
        int code = anno.code().getCode();
        return HttpResponse.failure(code,msg);
    }

    /**
     * 获取参数名称
     * jdk 1.8 特性
     *
     * @param joinPoint
     * @return
     */
    private String[] getParamName(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] strings = methodSignature.getParameterNames();
        return strings;
    }


    /**
     * 根据参数名称，获取参数值
     *
     * @param arguments
     * @param paramName
     * @param argName
     * @return
     */
    private Object getParamValue(Object[] arguments, String[] paramName, String argName) {
        Object value = null;
        String name = argName;
        if (argName.contains(".")) {
            name = argName.split("\\.")[0];
        }
        int index = 0;
        for (String string : paramName) {
            if (string.equals(name)) {
                value = arguments[index];    //基本类型取值	// 不做空判断，如果注解配置的参数名称不存在，则取值为null
                break;
            }
            index++;
        }
        if (argName.contains(".")) {    //从对象中取值
            argName = argName.split("\\.")[1];
            JSONObject jo = (JSONObject) JSONObject.toJSON(value);
            // 从实体对象中取值
            value = jo.get(argName);
        }
        return value;
    }


    /**
     * 获取方法
     *
     * @param joinPoint ProceedingJoinPoint
     * @return 方法
     */
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint
                        .getTarget()
                        .getClass()
                        .getDeclaredMethod(joinPoint.getSignature().getName(),
                                method.getParameterTypes());
            } catch (SecurityException | NoSuchMethodException e) {
                // log.error("" + e);
            }
        }
        return method;
    }

}
