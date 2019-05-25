package me.codeplayer.validator;

import java.util.function.*;

/**
 * 标识具有属性访问功能的实体
 * 
 * @since 2.3.0
 * @author Ready
 * @date 2019-4-24
 * @param <T>
 * @param <R>
 */
public interface PropertyAccessor<T, R> {

	Function<? super T, R> getGetter();

	BiConsumer<? super T, R> getSetter();
}
