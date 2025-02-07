package me.codeplayer.validator;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.*;
import javax.annotation.Nullable;

import me.codeplayer.util.NumberX;
import me.codeplayer.util.StringX;

/**
 * 用于进行 Pipeline 初始化及附属校验器定义的辅助工具类
 *
 * @author Ready
 * @since 2019-4-24
 * @since 2.3.0
 */
public abstract class Validators {

	public static <T, R> Pipeline<T, R> of(T bean, Function<? super T, R> getter, @Nullable BiConsumer<? super T, R> setter) {
		return new Pipeline<>(bean).begin(getter, setter);
	}

	public static <T, R> Pipeline<T, R> of(T bean, Function<? super T, R> getter) {
		return new Pipeline<>(bean).begin(getter, (BiConsumer<? super T, R>) null);
	}

	public static <T, R> Pipeline<T, R> of(T bean, PropertyAccessor<? super T, R> accessor) {
		return new Pipeline<>(bean).begin(accessor);
	}

	public static <T, R> Pipeline<T, R> of(T bean) {
		return new Pipeline<>(bean);
	}

	/*
	 * format group
	 */
	public static final Function<Object, String> trim = StringX::trim;
	public static final Function<Object, String> toString = StringX::toString;
	public static final Function<String, String> lower = String::toLowerCase;
	public static final Function<String, String> upper = String::toUpperCase;
	/*
	 * assert group
	 */
	public static final Predicate<Object> assertIsNull = Objects::isNull;
	public static final Predicate<Object> assertNotNull = Objects::nonNull;
	public static final Predicate<Object> assertNotEmpty = StringX::notEmpty;
	public static final Predicate<Object> assertNotBlank = StringX::notBlank;
	public static final Predicate<Object> assertIsNumber = NumberX::isNumber;
	public static final Predicate<String> assertIsNonNegative = NumberX::isNumeric;
	public static final Predicate<Number> assertPositive = NumberX::isPositive;
	public static final Predicate<Number> assertNonNegative = NumberX::isNonNegative;

	/*
	 * dynamic group
	 */

	/**
	 * 返回字符串长度校验器
	 *
	 * @param min 如果 ≤ -1，则表示允许为 null；
	 * @param max 如果 ≤ -1，则表示不限制最大值
	 */
	public static Predicate<CharSequence> assertLength(int min, int max) {
		return val -> val == null && min <= -1
				||
				val != null && val.length() >= min && (max <= -1 || max >= val.length());
	}

	public static Predicate<Integer> assertRange(int min, int max) {
		return val -> val != null && val >= min && val <= max;
	}

	public static Predicate<Long> assertRange(long min, long max) {
		return val -> val != null && val >= min && val <= max;
	}

	public static Predicate<Double> assertRange(double min, double max) {
		return val -> val != null && val >= min && val <= max;
	}

	public static Predicate<BigDecimal> assertRange(@Nullable BigDecimal min, @Nullable BigDecimal max) {
		return val -> val != null && (min == null || val.compareTo(min) >= 0) && (max == null || max.compareTo(val) >= 0);
	}

}