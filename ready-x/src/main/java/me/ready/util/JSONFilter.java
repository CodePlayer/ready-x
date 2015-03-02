package me.ready.util;


/**
 * 指定哪些字段可以被序列化为JSON字符串
 * 
 * @package com.p2p.common.util
 * @author Ready
 * @date 2015年3月2日
 * @since
 * 
 */
public interface JSONFilter {

	String[] allowSerialize();
}
