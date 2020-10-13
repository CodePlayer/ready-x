package me.codeplayer.util.scan;

import java.lang.reflect.*;
import java.util.function.*;

/**
 * 方法过滤器
 *
 * @author Ready
 * @date 2015年2月4日
 */
public interface MethodMatcher extends Predicate<Method> {

	abstract class AbstractMethodMatcher implements MethodMatcher {

		protected MethodMatcher methodMatcher;

		public AbstractMethodMatcher(MethodMatcher methodMatcher) {
			this.methodMatcher = methodMatcher;
		}

		@Override
		public boolean test(Method method) {
			return (methodMatcher == null || methodMatcher.test(method)) && matchMethod(method);
		}

		public abstract boolean matchMethod(Method method);
	}
}