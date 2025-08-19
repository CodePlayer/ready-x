package me.codeplayer.util;

import java.lang.reflect.*;
import java.util.function.*;

import javax.annotation.*;

/**
 * 枚举工具类
 *
 * @author Ready
 * @since 2019年4月16日
 * @since 1.0
 */
public abstract class EnumUtil {

	/**
	 * 根据枚举类型和名称，构建对应的枚举实例
	 *
	 * @return 返回对应的枚举值。如果找不到，则返回 null
	 */
	@Nullable
	public static <T extends Enum<T>> T of(Class<T> clazz, @Nullable String name) {
		if (StringUtil.notEmpty(name)) {
			try {
				return Enum.valueOf(clazz, name);
			} catch (Exception ignore) {
			}
		}
		return null;
	}

	/**
	 * 获取指定枚举类中 匹配指定条件的枚举数组
	 *
	 * @param enumClass 枚举类Class
	 * @param values 指定的枚举范围数组，如果为null，内部将会自动获取所有的枚举值
	 * @param matcher 枚举匹配器接口实现
	 * @since 2.0.0
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<?>> E[] getMatched(final Class<E> enumClass, @Nullable final E[] values, final Predicate<E> matcher) {
		final E[] newArray = values == null ? enumClass.getEnumConstants() : values.clone();
		int count = 0;
		for (E e : newArray) {
			if (matcher.test(e)) {
				newArray[count++] = e;
			}
		}
		if (count == newArray.length) {
			return newArray;
		}
		final E[] result = (E[]) Array.newInstance(enumClass, count);
		if (count > 0) {
			System.arraycopy(newArray, 0, result, 0, count);
		}
		return result;
	}

}