package me.ready.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

/**
 * 判断指定对象的指定属性是否可以序列化为JSON字符串的过滤器
 * 
 * @package com.p2p.common.util
 * @author Ready
 * @date 2015年3月2日
 * @since
 * 
 */
@SuppressWarnings("rawtypes")
public class JSONPropertyPreFilter implements PropertyPreFilter {

	private JSONPropertyPreFilter() {
	}

	private static Map<Class, ImmutableSet<String>> properties = null;
	private static JSONPropertyPreFilter instance = null;

	public static final JSONPropertyPreFilter getInstance() {
		if (instance == null) {
			instance = new JSONPropertyPreFilter();
			properties = new ConcurrentHashMap<Class, ImmutableSet<String>>();
		}
		return instance;
	}

	public boolean apply(JSONSerializer serializer, Object object, String name) {
		if (object != null) {
			Class<?> clazz = object.getClass();
			ImmutableSet<String> set = properties.get(clazz);
			if (set == null) {
				JSONFilter filter = clazz.getAnnotation(JSONFilter.class);
				if (filter != null) {
					Builder<String> builder = ImmutableSet.builder();
					String[] names = filter.allow();
					for (int i = 0; i < names.length; i++) {
						builder.add(names[i]);
					}
					set = builder.build();
					properties.put(clazz, set);
				}
			}
			if (set != null) {
				return set.contains(name);
			}
		}
		return true;
	}
}
