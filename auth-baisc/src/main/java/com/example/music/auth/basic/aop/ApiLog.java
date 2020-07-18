package com.example.music.auth.basic.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ApiLog {

    String module();

    String desc();

}
