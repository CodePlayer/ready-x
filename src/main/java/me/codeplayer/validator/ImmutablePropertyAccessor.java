package me.codeplayer.validator;

import java.util.function.*;

/**
 * 不可变的属性访问器封装实体
 *
 * @author Ready
 * @date 2019-4-24
 * @since 2.3.0
 */
public class ImmutablePropertyAccessor<T, R> implements PropertyAccessor<T, R> {

	protected final Function<? super T, R> getter;
	protected final BiConsumer<? super T, R> setter;

	public ImmutablePropertyAccessor(Function<? super T, R> getter, BiConsumer<? super T, R> setter) {
		this.getter = getter;
		this.setter = setter;
	}

	public Function<? super T, R> getGetter() {
		return getter;
	}

	public BiConsumer<? super T, R> getSetter() {
		return setter;
	}

}