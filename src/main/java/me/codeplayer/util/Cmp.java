package me.codeplayer.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 用于进行数值比较的工具类
 */
public class Cmp {

	/**
	 * 判断两个整数是否值相等（有参数为 null 时返回 false）
	 */
	public static boolean eqVal(@Nullable Integer n1, @Nullable Integer n2) {
		return n1 != null && n2 != null && n1.intValue() == n2.intValue();
	}

	/**
	 * 判断两个整数是否值相等（有参数为 null 时返回 false）
	 */
	public static boolean eqVal(@Nullable Long n1, @Nullable Long n2) {
		return n1 != null && n2 != null && n1.longValue() == n2.longValue();
	}

	/**
	 * 判断两个整数是否值相等（参数 val 为 null 时返回 false）
	 */
	public static boolean eq(@Nullable Integer val, int i2) {
		return val != null && val == i2;
	}

	/**
	 * 判断两个整数是否值相等（参数 val 为 null 时返回 false）
	 */
	public static boolean eq(@Nullable Long val, long i2) {
		return val != null && val == i2;
	}

	/**
	 * 判断两个整数是否值相等（有参数为 null 时返回 false）
	 */
	public static boolean eq(Object o1, Object o2) {
		return o1 != null && o1.equals(o2);
	}

	/**
	 * 指示指定的 val 是否为 null 或 等于 0
	 */
	public static Integer zeroToNull(@Nullable Integer val) {
		return val != null && val == 0 ? null : val;
	}

	/**
	 * 指示指定的 val 是否为 null 或 等于 0
	 */
	public static Long zeroToNull(@Nullable Long val) {
		return val != null && val == 0 ? null : val;
	}

	/**
	 * 指示指定的 val 是否为 null 或 等于 0
	 */
	public static Double zeroToNull(@Nullable Double val) {
		return val != null && val == 0 ? null : val;
	}

	/**
	 * 指示指定的 val 是否为 null 或 等于 0
	 */
	public static Float zeroToNull(@Nullable Float val) {
		return val != null && val == 0 ? null : val;
	}

	/**
	 * 指示指定的 val 是否为 null 或 等于 0
	 */
	public static BigInteger zeroToNull(@Nullable BigInteger val) {
		return val != null && val.compareTo(BigInteger.ZERO) == 0 ? null : val;
	}

	/**
	 * 指示指定的 val 是否为 null 或 等于 0
	 */
	public static BigDecimal zeroToNull(@Nullable BigDecimal val) {
		return val != null && val.compareTo(BigDecimal.ZERO) == 0 ? null : val;
	}

	/**
	 * 返回指定的 val，如果该参数为 null 时，则返回 0
	 */
	@Nonnull
	public static Integer nullToZero(@Nullable Integer val) {
		return val == null ? 0 : val;
	}

	/**
	 * 返回指定的 val，如果该参数为 null 时，则返回 0
	 */
	@Nonnull
	public static Long nullToZero(@Nullable Long val) {
		return val == null ? 0L : val;
	}

	/**
	 * 返回指定的 val，如果该参数为 null 时，则返回 0
	 */
	@Nonnull
	public static Double nullToZero(@Nullable Double val) {
		// 此处 Double.valueOf(0) 不能简写成 0 或 0D，否则会产生额外的拆箱/装箱开销 */
		return val == null ? Double.valueOf(0) : val;
	}

	/**
	 * 返回指定的 val，如果该参数为 null 时，则返回 0
	 */
	@Nonnull
	public static Float nullToZero(@Nullable Float val) {
		// 此处 Float.valueOf(0) 不能简写成 0 或 0F，否则会产生额外的拆箱/装箱开销 */
		return val == null ? Float.valueOf(0) : val;
	}

	/**
	 * 返回指定的 val，如果该参数为 null 时，则返回 0
	 */
	@Nonnull
	public static BigDecimal nullToZero(@Nullable BigDecimal val) {
		return val == null ? BigDecimal.ZERO : val;
	}

	/**
	 * 返回指定的 val，如果该参数为 null 时，则返回 0
	 */
	@Nonnull
	public static BigInteger nullToZero(@Nullable BigInteger val) {
		return val == null ? BigInteger.ZERO : val;
	}

	/**
	 * 指示 val 是否为 null 或 ≥ min
	 */
	public static boolean geOrNull(@Nullable Integer val, int min) {
		return val == null || val >= min;
	}

	/**
	 * 指示 val 是否为 null 或 {@code ＞ min}
	 */
	public static boolean gtOrNull(@Nullable Integer val, int min) {
		return val == null || val > min;
	}

	/**
	 * 指示 val 是否 {@code ≥  min}
	 *
	 * @return {@code val} 为 null 时，则返回 false
	 */
	public static boolean ge(@Nullable Integer val, int min) {
		return val != null && val >= min;
	}

	/**
	 * 指示 val 是否 {@code ＞  min}
	 *
	 * @return {@code val} 为 null 时，则返回 false
	 */
	public static boolean gt(@Nullable Integer val, int min) {
		return val != null && val > min;
	}

	/**
	 * 指示 val 是否为 null 或 ≤ min
	 */
	public static boolean leOrNull(@Nullable Integer val, int min) {
		return val == null || val <= min;
	}

	/**
	 * 指示 val 是否为 null 或 ＜ min
	 */
	public static boolean ltOrNull(@Nullable Integer val, int min) {
		return val == null || val < min;
	}

	/**
	 * 指示 val 是否 ≤ max
	 */
	public static boolean le(@Nullable Integer val, int max) {
		return val != null && val <= max;
	}

	/**
	 * 指示 val 是否 {@code ＜ max}
	 *
	 * @return {@code val} 为 null 时，则返回 false
	 */
	public static boolean lt(@Nullable Integer val, int max) {
		return val != null && val < max;
	}

	/**
	 * 指示 val 是否为 null 或 ≥ min
	 */
	public static boolean geOrNull(@Nullable Long val, long min) {
		return val == null || val >= min;
	}

	/**
	 * 指示 val 是否为 null 或 {@code ＞ min}
	 */
	public static boolean gtOrNull(@Nullable Long val, long min) {
		return val == null || val > min;
	}

	/**
	 * 指示 val 是否 {@code ≥ min}
	 *
	 * @return {@code val} 为 null 时，则返回 false
	 */
	public static boolean ge(@Nullable Long val, long min) {
		return val != null && val >= min;
	}

	/**
	 * 指示 val 是否 {@code ＞  min}
	 *
	 * @return {@code val} 为 null 时，则返回 false
	 */
	public static boolean gt(@Nullable Long val, long min) {
		return val != null && val > min;
	}

	/**
	 * 指示 val 是否为 null 或 ≤ max
	 */
	public static boolean leOrNull(@Nullable Long val, long max) {
		return val == null || val <= max;
	}

	/**
	 * 指示 val 是否为 null 或 ＜ max
	 */
	public static boolean ltOrNull(@Nullable Long val, long max) {
		return val == null || val < max;
	}

	/**
	 * 指示 val 是否 ≤ {@code max}
	 *
	 * @return {@code val} 为 null 时，则返回 false
	 */
	public static boolean le(@Nullable Long val, long max) {
		return val != null && val <= max;
	}

	/**
	 * 指示 val 是否 ＜ {@code max}
	 *
	 * @return {@code val} 为 null 时，则返回 false
	 */
	public static boolean lt(@Nullable Long val, long max) {
		return val != null && val < max;
	}

	/**
	 * 指示 val 是否 {@code ＞  min}
	 *
	 * @return {@code val} 为 null 时，则返回 false
	 */
	public static <T extends Comparable<T>> boolean gt(@Nullable T val, T min) {
		return val != null && val.compareTo(min) > 0;
	}

	/**
	 * 指示 val 是否为 null 或 {@code ＞ min}
	 */
	public static <T extends Comparable<T>> boolean gtOrNull(@Nullable T val, T min) {
		return val == null || val.compareTo(min) > 0;
	}

	/**
	 * 指示 val 是否 {@code ≥ min}
	 *
	 * @return {@code val} 为 null 时，则返回 false
	 */
	public static <T extends Comparable<T>> boolean ge(@Nullable T val, T min) {
		return val != null && val.compareTo(min) >= 0;
	}

	/**
	 * 指示 val 是否为 null 或 ≥ min
	 */
	public static <T extends Comparable<T>> boolean geOrNull(@Nullable T val, T min) {
		return val == null || val.compareTo(min) >= 0;
	}

	/**
	 * 指示 val 是否 ≤ {@code max}
	 *
	 * @return {@code val} 为 null 时，则返回 false
	 */
	public static <T extends Comparable<T>> boolean le(@Nullable T val, T max) {
		return val != null && val.compareTo(max) <= 0;
	}

	/**
	 * 指示 val 是否为 null 或 ≤ max
	 */
	public static <T extends Comparable<T>> boolean leOrNull(@Nullable T val, T max) {
		return val == null || val.compareTo(max) <= 0;
	}

	/**
	 * 指示 val 是否 ＜ {@code max}
	 *
	 * @return {@code val} 为 null 时，则返回 false
	 */
	public static <T extends Comparable<T>> boolean lt(@Nullable T val, T max) {
		return val != null && val.compareTo(max) < 0;
	}

	/**
	 * 指示 val 是否为 null 或 ＜ max
	 */
	public static <T extends Comparable<T>> boolean ltOrNull(@Nullable T val, T max) {
		return val == null || val.compareTo(max) < 0;
	}

	/**
	 * 指示 指定的 {@code val} 是否在 {@code min} 和 {@code max} 之间（闭区间）
	 *
	 * @return 如果 {@code val} 为 null，则返回 false
	 */
	public static boolean between(@Nullable Integer val, int min, int max) {
		return val != null && val >= min && val <= max;
	}

	/**
	 * 指示 指定的 {@code val} 是否在 {@code min} 和 {@code max} 之间（闭区间）
	 *
	 * @return 如果 {@code val} 为 null，则返回 false
	 */
	public static boolean between(@Nullable Long val, long min, long max) {
		return val != null && val >= min && val <= max;
	}

	/**
	 * 指示 指定的 {@code val} 是否在 {@code min} 和 {@code max} 之间（闭区间）
	 *
	 * @return 如果 {@code val}、{@code min} 和 {@code max} 任一为 null，则返回 false
	 */
	public static <T extends Comparable<T>> boolean between(@Nullable T val, @Nullable T min, @Nullable T max) {
		return val != null && min != null && max != null && val.compareTo(min) >= 0 && val.compareTo(max) <= 0;
	}

}