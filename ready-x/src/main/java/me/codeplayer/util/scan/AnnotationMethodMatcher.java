package me.codeplayer.util.scan;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 注解方法过滤器
 * 
 * @author Ready
 * @date 2015年2月4日
 * @since
 * 
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