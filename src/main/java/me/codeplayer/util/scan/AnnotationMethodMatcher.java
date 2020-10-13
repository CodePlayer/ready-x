package me.codeplayer.util.scan;

import java.lang.annotation.*;
import java.lang.reflect.*;

import me.codeplayer.util.scan.MethodMatcher.*;

/**
 * 注解方法过滤器
 *
 * @author Ready
 * @date 2015年2月4日
 */
public class AnnotationMethodMatcher extends AbstractMethodMatcher {

	protected Class<? extends Annotation> annotationClass;

	public AnnotationMethodMatcher(Class<? extends Annotation> annotationClass) {
		super(null);
		this.annotationClass = annotationClass;
	}

	public AnnotationMethodMatcher(MethodMatcher methodMatcher, Class<? extends Annotation> annotationClass) {
		super(methodMatcher);
		this.annotationClass = annotationClass;
	}

	public boolean matchMethod(Method method) {
		return method.isAnnotationPresent(annotationClass);
	}
}