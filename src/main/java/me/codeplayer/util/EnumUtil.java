package me.codeplayer.util;

import java.lang.reflect.Array;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import javax.annotation.Nullable;

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
	 * @return 返回对应的枚举值。如果找不到，则返回 {@code defaultValue}
	 */
	@Nullable
	public static <T extends Enum<T>> T of(Class<T> clazz, @Nullable String name, T defaultValue) {
		if (StringUtil.notEmpty(name)) {
			try {
				return Enum.valueOf(clazz, name);
			} catch (IllegalArgumentException ignored) {
			}
		}
		return defaultValue;
	}

	/**
	 * 根据枚举类型和名称，构建对应的枚举实例
	 *
	 * @return 返回对应的枚举值。如果找不到，则返回 null
	 */
	@Nullable
	public static <T extends Enum<T>> T of(Class<T> clazz, @Nullable String name) {
		return of(clazz, name, null);
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

	/**
	 * 基于 value 值 相对 ordinal 的偏移，获取对应的枚举
	 * 这要求枚举的 value 必须是连续的整数值
	 *
	 * @param ordinalOffset 传入 value 相对 ordinal 的偏移值 <code> (value - ordinal)</code>
	 */
	@Nullable
	public static <E extends Enum<?>> E valueOf(E[] range, @Nullable Integer value, int ordinalOffset) {
		if (value != null) {
			int ordinal = value - ordinalOffset;
			if (ordinal >= 0 && ordinal < range.length) {
				return range[ordinal];
			}
		}
		return null;
	}

	/**
	 * 返回指定枚举数组中查找其属性为指定值的枚举，如果找不到则返回 null
	 */
	@Nullable
	public static <E extends Enum<?>> E valueOf(E[] range, ToIntFunction<? super E> propertyGetter, @Nullable Integer value) {
		if (value != null) {
			final int val = value;
			for (E e : range) {
				if (propertyGetter.applyAsInt(e) == val) {
					return e;
				}
			}
		}
		return null;
	}

}