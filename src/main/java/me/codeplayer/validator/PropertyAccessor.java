package me.codeplayer.validator;

import java.util.function.*;

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

}