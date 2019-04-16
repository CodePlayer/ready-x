package me.codeplayer.util.scan;

import java.lang.reflect.*;

/**
 * 修饰符方法过滤器
 * 
 * @author Ready
 * @date 2015年2月4日
 */
public class ModifiersMethodMatcher extends AbstractMethodMatcher {

	protected int modifiers;

	public ModifiersMethodMatcher(int modifiers) {
		super(null);
		this.modifiers = modifiers;
	}

	public ModifiersMethodMatcher(MethodMatcher methodMatcher, int modifiers) {
		super(methodMatcher);
		this.modifiers = modifiers;
	}

	public boolean matchMethod(Method method) {
		return (method.getModifiers() & modifiers) > 0;
	}
}