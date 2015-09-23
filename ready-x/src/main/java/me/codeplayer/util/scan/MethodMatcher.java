package me.codeplayer.util.scan;

import java.lang.reflect.Method;

/**
 * 方法过滤器
 * 
 * @package me.codeplayer.util
 * @author Ready
 * @date 2015年2月4日
 * @since
 * 
 */
public interface MethodMatcher {

	/**
	 * 判断指定的方法是否符合条件，如果符合则返回true，否则返回false
	 * 
	 * @param method
	 * @return
	 */
	boolean match(Method method);
}
