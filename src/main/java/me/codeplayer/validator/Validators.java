package me.codeplayer.validator;

import java.math.*;
import java.util.function.*;

import javax.annotation.*;

import me.codeplayer.util.*;

/**
 * 用于进行 Pipeline 初始化及附属校验器定义的辅助工具类
 * 
 * @since 2.3.0
 * @author Ready
 * @date 2019-4-24
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
	public static final Function<Object, String> trim = StringUtil::trim;
	public static final Function<Object, String> toSring = StringUtil::toString;
	public static final Function<String, String> lower = String::toLowerCase;
	public static final Function<String, String> upper = String::toUpperCase;
	/*
	 * assert group
	 */
	public static final Predicate<Object> assertNotEmpty = StringUtil::notEmpty;
	public static final Predicate<Object> assertNotBlank = StringUtil::notBlank;
	public static final Predicate<Object> assertIsNumber = NumberUtil::isNumber;
	public static final Predicate<String> assertIsNonNegative = NumberUtil::isNumeric;
	public static final Predicate<Number> assertPositive = NumberUtil::isPositive;
	public static final Predicate<Number> assertNonNegative = NumberUtil::isNonNegative;

	/*
	 * dynamic group
	 */
	/**
	 * 返回字符串长度校验器
	 * 
	 * @param min 如果 ≤ -1，则表示允许为 null；
	 * @param max 如果 ≤ -1，则表示不限制最大值
	 * @return
	 */
	public static final Predicate<CharSequence> assertLength(int min, int max) {
		return val -> val == null && min <= -1
				||
				val != null && val.length() >= min && (max <= -1 || max >= val.length());
	}

	public static final Predicate<Integer> assertRange(int min, int max) {
		return val -> val != null && val >= min && val <= max;
	}

	public static final Predicate<Long> assertRange(long min, long max) {
		return val -> val != null && val >= min && val <= max;
	}

	public static final Predicate<Double> assertRange(double min, double max) {
		return val -> val != null && val >= min && val <= max;
	}

	public static final Predicate<BigDecimal> assertRange(@Nullable BigDecimal min, @Nullable BigDecimal max) {
		return val -> val != null && (min == null || val.compareTo(min) >= 0) && (max == null || max.compareTo(val) >= 0);
	}
}
