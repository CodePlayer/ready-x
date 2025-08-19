package me.codeplayer.validator;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nullable;

import me.codeplayer.util.NumberUtil;
import me.codeplayer.util.StringUtil;

/**
 * 用于进行 Pipeline 初始化及附属校验器定义的辅助工具类
 *
 * @author Ready
 * @since 2019-4-24
 * @since 2.3.0
 */
public interface Validators {

	/*
	 * format group
	 */
	Function<Object, String> trim = StringUtil::trim;
	Function<Object, String> toString = StringUtil::toString;
	Function<String, String> lower = String::toLowerCase;
	Function<String, String> upper = String::toUpperCase;
	/*
	 * assert group
	 */
	Predicate<Object> assertIsNull = Objects::isNull;
	Predicate<Object> assertNotNull = FunctionX.nonNull();
	Predicate<Object> assertNotEmpty = StringUtil::notEmpty;
	Predicate<Object> assertNotBlank = StringUtil::notBlank;
	Predicate<Object> assertIsNumber = NumberUtil::isNumber;
	Predicate<String> assertIsNonNegative = NumberUtil::isNumeric;
	Predicate<Number> assertPositive = NumberUtil::isPositive;
	Predicate<Number> assertNonNegative = NumberUtil::isNonNegative;

	/*
	 * dynamic group
	 */

	/**
	 * 返回字符串长度校验器
	 *
	 * @param min 如果 ≤ -1，则表示允许为 null；
	 * @param max 如果 ≤ -1，则表示不限制最大值
	 */
	static Predicate<CharSequence> assertLength(int min, int max) {
		return val -> val == null && min <= -1
				||
				val != null && val.length() >= min && (max <= -1 || max >= val.length());
	}

	static Predicate<Integer> assertRange(int min, int max) {
		return val -> val != null && val >= min && val <= max;
	}

	static Predicate<Long> assertRange(long min, long max) {
		return val -> val != null && val >= min && val <= max;
	}

	static Predicate<Double> assertRange(double min, double max) {
		return val -> val != null && val >= min && val <= max;
	}

	static Predicate<BigDecimal> assertRange(@Nullable BigDecimal min, @Nullable BigDecimal max) {
		return val -> val != null && (min == null || val.compareTo(min) >= 0) && (max == null || max.compareTo(val) >= 0);
	}

}