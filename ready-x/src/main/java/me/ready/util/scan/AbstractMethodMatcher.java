package me.ready.util.scan;

import java.lang.reflect.Method;

public abstract class AbstractMethodMatcher implements MethodMatcher {

	protected MethodMatcher methodMatcher;

	public AbstractMethodMatcher(MethodMatcher methodMatcher) {
		this.methodMatcher = methodMatcher;
	}

	public boolean match(Method method) {
		return (methodMatcher == null || methodMatcher.match(method)) && matchMethod(method);
	}

	public abstract boolean matchMethod(Method method);
}