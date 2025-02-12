package me.codeplayer.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 对数值类型的数据(包含字节)进行相应处理的工具类
 *
 * @author Ready
 * @since 2012-10-29
 */
public abstract class NumberX {

	static final Function<String, Integer> string2Integer = Integer::valueOf;
	static final Function<String, Long> string2Long = Long::valueOf;

	/**
	 * 以 byte 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @throws NullPointerException 如果 <code>value</code> 为 null，将报错
	 * @throws NumberFormatException 如果无法转为 byte 形式，将报错
	 */
	public static byte getByte(Object value) {
		return value instanceof Number ? ((Number) value).byteValue() : Byte.parseByte(value.toString());
	}

	static <E extends Number> E castString2Number(@Nonnull Object value, Function<? super String, E> converter, @Nullable E defaultIfEmpty) {
		if (value instanceof CharSequence) {
			final CharSequence cs = (CharSequence) value;
			if (cs.length() == 0) {
				return defaultIfEmpty;
			}
			return converter.apply(value.toString());
		}
		throw new IllegalArgumentException("Unexpected number value:" + value);
	}

	/**
	 * 以  byte 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @param defaultIfEmpty 如果 <code>value</code> 为 null 或 空字符串，将默认返回该参数
	 * @throws NumberFormatException 如果无法转为 byte 形式，将报错
	 * @throws IllegalArgumentException 如果 <code>value</code> 非 Number、CharSequence 类型，将报错
	 */
	public static Byte getByte(@Nullable Object value, @Nullable Byte defaultIfEmpty) {
		if (value == null) {
			return defaultIfEmpty;
		} else if (value instanceof Byte) {
			return (Byte) value;
		} else if (value instanceof Number) {
			return ((Number) value).byteValue();
		}
		return castString2Number(value, Byte::valueOf, defaultIfEmpty);
	}

	/**
	 * 以 short 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @throws NullPointerException 如果 <code>value</code> 为 null，将报错
	 * @throws NumberFormatException 如果无法转为 short 形式，将报错
	 */
	public static short getShort(Object value) {
		return value instanceof Number ? ((Number) value).shortValue() : Short.parseShort(value.toString());
	}

	/**
	 * 以 Short 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @param defaultIfEmpty 如果 <code>value</code> 为 null 或 空字符串，将默认返回该参数
	 * @throws NumberFormatException 如果无法转为 Short 形式，将报错
	 * @throws IllegalArgumentException 如果 <code>value</code> 非 Number、CharSequence 类型，将报错
	 */
	public static Short getShort(@Nullable Object value, @Nullable Short defaultIfEmpty) {
		if (value == null) {
			return defaultIfEmpty;
		} else if (value instanceof Short) {
			return (Short) value;
		} else if (value instanceof Number) {
			return ((Number) value).shortValue();
		}
		return castString2Number(value, Short::valueOf, defaultIfEmpty);
	}

	/**
	 * 以 int 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @throws NullPointerException 如果 <code>value</code> 为 null，将报错
	 * @throws NumberFormatException 如果无法转为 int 形式，将报错
	 */
	public static int getInt(Object value) {
		return value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(value.toString());
	}

	/**
	 * 以 int 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @param defaultIfEmpty 如果 <code>value</code> 为 null 或 空字符串，将默认返回该参数
	 * @throws NumberFormatException 如果无法转为 int 形式，将报错
	 * @throws IllegalArgumentException 如果 <code>value</code> 非 Number、CharSequence 类型，将报错
	 */
	public static int getInt(@Nullable Object value, int defaultIfEmpty) {
		if (value == null) {
			return defaultIfEmpty;
		}
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		if (value instanceof CharSequence) {
			final CharSequence cs = (CharSequence) value;
			if (cs.length() == 0) {
				return defaultIfEmpty;
			}
			return Integer.parseInt(value.toString());
		}
		throw new IllegalArgumentException("Unexpected int value:" + value);
	}

	/**
	 * 以 Integer 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @param defaultIfEmpty 如果 <code>value</code> 为 null 或 空字符串，将默认返回该参数
	 * @throws NumberFormatException 如果无法转为 Integer 形式，将报错
	 * @throws IllegalArgumentException 如果 <code>value</code> 非 Number、CharSequence 类型，将报错
	 */
	public static Integer getInteger(@Nullable Object value, @Nullable Integer defaultIfEmpty) {
		if (value == null) {
			return defaultIfEmpty;
		} else if (value instanceof Integer) {
			return (Integer) value;
		} else if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		return castString2Number(value, string2Integer, defaultIfEmpty);
	}

	/**
	 * 以 long 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @throws NullPointerException 如果 <code>value</code> 为 null，将报错
	 * @throws NumberFormatException 如果无法转为 long 形式，将报错
	 */
	public static long getLong(Object value) {
		return value instanceof Number ? ((Number) value).longValue() : Long.parseLong(value.toString());
	}

	/**
	 * 以 long 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @param defaultIfEmpty 如果 <code>value</code> 为 null 或 空字符串，将默认返回该参数
	 * @throws NumberFormatException 如果无法转为 long 形式，将报错
	 * @throws IllegalArgumentException 如果 <code>value</code> 非 Number、CharSequence 类型，将报错
	 */
	public static long getLong(@Nullable Object value, long defaultIfEmpty) {
		if (value == null) {
			return defaultIfEmpty;
		}
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		if (value instanceof CharSequence) {
			final CharSequence cs = (CharSequence) value;
			if (cs.length() == 0) {
				return defaultIfEmpty;
			}
			return Long.parseLong(value.toString());
		}
		throw new IllegalArgumentException("Unexpected long value:" + value);
	}

	/**
	 * 以 Long 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @param defaultIfEmpty 如果 <code>value</code> 为 null 或 空字符串，将默认返回该参数
	 * @throws NumberFormatException 如果无法转为 Long 形式，将报错
	 * @throws IllegalArgumentException 如果 <code>value</code> 非 Number、CharSequence 类型，将报错
	 */
	public static Long getLong(@Nullable Object value, @Nullable Long defaultIfEmpty) {
		if (value == null) {
			return defaultIfEmpty;
		} else if (value instanceof Long) {
			return (Long) value;
		} else if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		return castString2Number(value, string2Long, defaultIfEmpty);
	}

	/**
	 * 以 float 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @throws NullPointerException 如果 <code>value</code> 为 null，将报错
	 * @throws NumberFormatException 如果无法转为 float 形式，将报错
	 */
	public static float getFloat(Object value) {
		return value instanceof Number ? ((Number) value).floatValue() : Float.parseFloat(value.toString());
	}

	/**
	 * 以 Float 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @param defaultIfEmpty 如果 <code>value</code> 为 null 或 空字符串，将默认返回该参数
	 * @throws NumberFormatException 如果无法转为 Float 形式，将报错
	 * @throws IllegalArgumentException 如果 <code>value</code> 非 Number、CharSequence 类型，将报错
	 */
	public static Float getFloat(@Nullable Object value, @Nullable Float defaultIfEmpty) {
		if (value == null) {
			return defaultIfEmpty;
		} else if (value instanceof Float) {
			return (Float) value;
		} else if (value instanceof Number) {
			return ((Number) value).floatValue();
		}
		return castString2Number(value, Float::valueOf, defaultIfEmpty);
	}

	/**
	 * 以 double 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @throws NullPointerException 如果 <code>value</code> 为 null，将报错
	 * @throws NumberFormatException 如果无法转为 double 形式，将报错
	 */
	public static double getDouble(Object value) {
		return value instanceof Number ? ((Number) value).doubleValue() : Double.parseDouble(value.toString());
	}

	/**
	 * 以 Double 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @param defaultIfEmpty 如果 <code>value</code> 为 null 或 空字符串，将默认返回该参数
	 * @throws NumberFormatException 如果无法转为 Double 形式，将报错
	 * @throws IllegalArgumentException 如果 <code>value</code> 非 Number、CharSequence 类型，将报错
	 */
	public static Double getDouble(@Nullable Object value, @Nullable Double defaultIfEmpty) {
		if (value == null) {
			return defaultIfEmpty;
		} else if (value instanceof Double) {
			return (Double) value;
		} else if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		return castString2Number(value, Double::valueOf, defaultIfEmpty);
	}

	/**
	 * 以 BigDecimal 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @throws NullPointerException 如果 <code>value</code> 为 null，将报错
	 * @throws NumberFormatException 如果无法转为 BigDecimal 形式，将报错
	 */
	public static BigDecimal getBigDecimal(Object value) {
		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		} else if (value instanceof BigInteger) {
			return new BigDecimal((BigInteger) value);
		} else if (value instanceof Integer || value instanceof Long) {
			return BigDecimal.valueOf(((Number) value).longValue());
		}
		return new BigDecimal(value.toString());
	}

	/**
	 * 以 BigDecimal 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @param defaultIfEmpty 如果 <code>value</code> 为 null 或 空字符串，将默认返回该参数。如果该参数不是 BigDecimal 类型，将尝试自动转换
	 * @throws NumberFormatException 如果无法转为 BigDecimal 形式，将报错
	 * @throws IllegalArgumentException 如果 <code>value</code> 非 Number、CharSequence 类型，将报错
	 */
	public static BigDecimal getBigDecimal(@Nullable Object value, @Nullable Object defaultIfEmpty) {
		if (value instanceof Number) {
			return getBigDecimal(value);
		}
		boolean empty = value == null;
		if (!empty && value instanceof CharSequence) {
			final CharSequence cs = (CharSequence) value;
			if (cs.length() > 0) {
				return getBigDecimal(value);
			}
			empty = true;
		}
		if (empty) {
			return defaultIfEmpty == null ? null : getBigDecimal(defaultIfEmpty);
		}
		throw new IllegalArgumentException("Unexpected decimal value:" + value);
	}

	/**
	 * 判断指定的字符串内容是否为整数形式
	 * <pre><code>
	 * isNumber("123") == true
	 * isNumber("00123") == true
	 *
	 * isNumber(null) == false
	 * isNumber("") == false
	 * isNumber("  ") == false
	 * isNumber("-123") == false
	 * isNumber("123.45") == false
	 * </code></pre>
	 *
	 * @param cs 指定的字符串
	 * @see Character#isDigit(char)
	 */
	public static boolean isNumber(CharSequence cs) {
		if (cs == null) { // 为空则返回false
			return false;
		}
		final int length = cs.length();
		if (length == 0) { // 为空字符串则返回false
			return false;
		}
		for (int i = 0; i < length; i++) {
			if (!Character.isDigit(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符串内容是否为指定位数的整数形式<br>
	 * 前面带0，例如"0012"属于4位整数<br>
	 * 如果字符串为null，返回false<br>
	 * 如果字符串前后有空格，请先去除空格后再调用此方法，否则返回false
	 *
	 * @param length 指定位数大小
	 */
	public static boolean isNumber(String str, int length) {
		if (length < 0) {
			throw new IllegalArgumentException("length can not be less than 0: " + length);
		}
		return str != null && str.length() == length && isNumber(str);
	}

	/**
	 * 判断指定对象是否为整数类型或能够转为整数形式
	 */
	public static boolean isNumber(Object value) {
		if (value instanceof Number) {
			if (value instanceof Integer || value instanceof Long || value instanceof BigInteger) {
				return true;
			} else if (value instanceof BigDecimal) {
				BigDecimal bd = (BigDecimal) value;
				return bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0;
			}
			Number num = (Number) value;
			return num.longValue() == num.doubleValue();
		}
		return value != null && isNumber(value.toString());
	}

	/**
	 * 以尽可能快的速度判断指定字符串是否为整数形式（仅限十进制 <code>[0-9]</code> ）
	 *
	 * @param cs 指定的字符串
	 */
	public static boolean isNumeric(final CharSequence cs) {
		return cs != null && scanNumeric(cs, 0, cs.length()) == -1;
	}

	/**
	 * 以尽可能快的速度判断指定字符序列片段是否全部都是数字（仅限十进制 <code>[0-9]</code> ）
	 *
	 * @param cs 指定的字符串
	 * @param start 开始位置（包括）
	 * @param end 结束位置（不包括）
	 */
	public static boolean isNumeric(final CharSequence cs, int start, int end) {
		return cs != null && cs.length() > 0 && scanNumeric(cs, start, end) == -1;
	}

	/**
	 * 以尽可能快的速度判断指定字符序列片段是否全部都是数字（仅限十进制 <code>[0-9]</code> ）
	 *
	 * @param cs 指定的字符串
	 * @param start 开始位置（包括）
	 * @param end 结束位置（不包括）
	 * @return 返回第一个非数字的字符下标，如果全部是数字，则返回 -1
	 */
	static int scanNumeric(final CharSequence cs, int start, int end) {
		for (int i = start; i < end; i++) {
			char ch = cs.charAt(i);
			if (ch < '0' || ch > '9') {
				return i;
			}
		}
		return start < end ? -1 : end;
	}

	/**
	 * 判断字符串内容是否为整数或小数形式（主要用于常规输入，不支持科学计数法，否则将返回 false）
	 * <pre><code>
	 * isDecimal("0", *) == true
	 * isDecimal("12", *) == true
	 * isDecimal("12.3", *) == true
	 * isDecimal("-12.3", true) == true
	 * isDecimal("0012", *) == true
	 *
	 * isDecimal("", *) == false
	 * isDecimal(" ", *) == false
	 * isDecimal(" 123", *) == false
	 * isDecimal("12E3", *) == false
	 * isDecimal("0xff", *) == false
	 * isDecimal(null, *) == false
	 * </code></pre>
	 *
	 * @param str 需要判断的字符串
	 * @param allowNegative 是否允许负数也返回 true
	 */
	public static boolean isDecimal(String str, boolean allowNegative) {
		final int length;
		if (str == null || (length = str.length()) == 0) {
			return false;
		}
		final int fromIndex = allowNegative && str.charAt(0) == '-' ? 1 : 0;
		final int errPos = scanNumeric(str, fromIndex, length);
		return errPos == -1 || fromIndex < errPos && errPos < length && str.charAt(errPos) == '.' && scanNumeric(str, errPos + 1, length) == -1;
	}

	/**
	 * 判断字符串内容是否为整数或小数形式（主要用于常规输入，不兼容负数、科学计数法，否则将返回 false）
	 * <pre><code>
	 * isDecimal("12") == true
	 * isDecimal("12.3") == true
	 * isDecimal("0012") == true
	 *
	 * isDecimal("") == false
	 * isDecimal(" ") == false
	 * isDecimal("-12.3") == false
	 * isDecimal(" 123") == false
	 * isDecimal("12E3") == false
	 * isDecimal("0xff") == false
	 * isDecimal(null) == false
	 * </code></pre>
	 *
	 * @param str 需要判断的字符串
	 */
	public static boolean isDecimal(String str) {
		return isDecimal(str, false);
	}

	/**
	 * 判断指定对象是否为整数或小数形式
	 * <pre><code>
	 * isDecimal(12.3) == true
	 * isDecimal(12.3F) == true
	 * isDecimal(12L) == true
	 * isDecimal("0012") == true
	 * isDecimal(-12) == true
	 * isDecimal("12") == true
	 * isDecimal("-12") == true
	 * isDecimal("12.3") == true
	 *
	 * isDecimal("") == false
	 * isDecimal(" ") == false
	 * isDecimal(" 123") == false
	 * isDecimal("12E3") == false
	 * isDecimal("0xff") == false
	 * isDecimal(null) == false
	 * </code></pre>
	 *
	 * @param value 需要判断的对象
	 */
	public static boolean isDecimal(Object value) {
		return value != null && (value instanceof Number || isDecimal(value.toString()));
	}

	/**
	 * 指示指定的数字是否是正数
	 *
	 * @since 2.3.0
	 */
	protected static boolean isPositive(@Nullable final Number val, final boolean allowZero) {
		if (val != null) {
			if (val instanceof Integer) {
				return allowZero ? val.intValue() >= 0 : val.intValue() > 0;
			} else if (val instanceof Long) {
				return allowZero ? val.longValue() >= 0 : val.longValue() > 0;
			}
			return allowZero ? val.doubleValue() >= 0 : val.doubleValue() > 0;
		}
		return false;
	}

	/**
	 * 指示指定的数字是否是正数
	 *
	 * @since 2.3.0
	 */
	public static boolean isPositive(@Nullable final Number val) {
		return isPositive(val, false);
	}

	/**
	 * 指示指定的数字是否是非负数
	 *
	 * @since 2.3.0
	 */
	public static boolean isNonNegative(@Nullable final Number val) {
		return isPositive(val, true);
	}

}