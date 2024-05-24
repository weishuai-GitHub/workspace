package com.wechat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalIntercepter {
    /**
     * 是否检查登录
     * @return
     */
    boolean checkLogin() default true;

    /**
     * 是否检查管理员
     * @return
     */ 
    boolean checkAdmin() default false;

    /**
     * 是否检查token
     * @return
     */
    boolean checkToken() default true;

}
