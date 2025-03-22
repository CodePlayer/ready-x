package me.codeplayer.validator;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 标识具有属性访问功能的实体
 *
 * @author Ready
 * @since 2019-4-24
 * @since 2.3.0
 */
public interface PropertyAccessor<T, R> {

	Function<? super T, R> getGetter();

	BiConsumer<? super T, R> getSetter();

	static <T, R> PropertyAccessor<T, R> of(Function<? super T, R> getter, BiConsumer<? super T, R> setter) {
		return new ImmutablePropertyAccessor<>(getter, setter);
	}

}