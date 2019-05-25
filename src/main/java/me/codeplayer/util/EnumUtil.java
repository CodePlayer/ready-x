package me.codeplayer.util;

import java.lang.reflect.*;
import java.util.function.*;

import javax.annotation.*;

/**
 * 枚举工具类
 * 
 * @since 1.0
 * @author Ready
 * @date 2019年4月16日
 */
public abstract class EnumUtil {

	/**
	 * 根据枚举类型和名称，构建对应的枚举实例
	 * 
	 * @return 返回对应的枚举值。如果找不到，则返回 null
	 */
	@Nullable
	public static final <T extends Enum<T>> T enumOf(Class<T> clazz, @Nullable String name) {
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
	 * @return
	 * @since 2.0.0
	 */
	@SuppressWarnings("unchecked")
	public static final <E extends Enum<?>> E[] getMatchedEnums(final Class<E> enumClass, @Nullable final E[] values, final Predicate<E> matcher) {
		final E[] newAarray = values == null ? enumClass.getEnumConstants() : values.clone();
		int count = 0;
		for (E e : newAarray) {
			if (matcher.test(e)) {
				newAarray[count++] = e;
			}
		}
		if (count == newAarray.length) {
			return newAarray;
		} else {
			final E[] result = (E[]) Array.newInstance(enumClass, count);
			if (count > 0) {
				System.arraycopy(newAarray, 0, result, 0, count);
			}
			return result;
		}
	}
}
