package me.ready.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定哪些字段可以被序列化为JSON字符串
 * 
 * @package com.p2p.common.util
 * @author Ready
 * @date 2015年3月2日
 * @since
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONFilter {

	String[] allow() default {};
}
