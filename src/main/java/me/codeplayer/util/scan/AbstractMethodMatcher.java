package me.codeplayer.util.scan;

import java.lang.reflect.*;

public abstract class AbstractMethodMatcher implements MethodMatcher {

	protected MethodMatcher methodMatcher;

	public AbstractMethodMatcher(MethodMatcher methodMatcher) {
		this.methodMatcher = methodMatcher;
	}

	public boolean test(Method method) {
		return (methodMatcher == null || methodMatcher.test(method)) && matchMethod(method);
	}

	public abstract boolean matchMethod(Method method);
}