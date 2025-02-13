package me.codeplayer.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.annotation.Nullable;

/**
 * 对数值类型的数据(包含字节)进行相应处理的工具类
 *
 * @author Ready
 * @since 2012-10-29
 */
public abstract class NumberUtil {

	/**
	 * 将int类型的变量转为二进制字符串
	 */
	public static String int2Byte(int i) {
		int index = 32;
		char[] chars = new char[index];
		do {
			chars[--index] = StringUtil.digits[(i & 1)];
			i >>>= 1;
		} while (i != 0);
		return new String(chars, index, 32 - index);
	}

	/**
	 * 以byte形式返回指定的值<br>
	 * 如果指定的值为null或无法转为byte形式，将报错
	 *
	 * @param value 指定的对象
	 */
	public static byte getByte(Object value) {
		Assert.notNull(value);
		return value instanceof Number ? ((Number) value).byteValue() : Byte.parseByte(value.toString());
	}

	/**
	 * 以byte形式返回指定的值<br>
	 * 如果指定的值为null或无法转为byte形式，将返回指定的<code>defaultValue</code>
	 *
	 * @param value 指定的对象
	 * @param defaultValue 指定的默认值
	 */
	public static byte getByte(Object value, byte defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		if (value instanceof Number) {
			return ((Number) value).byteValue();
		}
		if (value instanceof CharSequence) {
			final CharSequence cs = (CharSequence) value;
			if (cs.length() == 0) {
				return defaultValue;
			}
			return Byte.parseByte(value.toString());
		}
		throw new IllegalArgumentException("Unexpected byte value:" + value);
	}

	/**
	 * 以short形式返回指定的值<br>
	 * 如果指定的值为null或无法转为short形式，将报错
	 *
	 * @param value 指定的对象
	 */
	public static short getShort(Object value) {
		Assert.notNull(value);
		return value instanceof Number ? ((Number) value).shortValue() : Short.parseShort(value.toString());
	}

	/**
	 * 以short形式返回指定的值<br>
	 * 如果指定的值为null或无法转为short形式，将返回指定的<code>defaultValue</code>
	 *
	 * @param value 指定的对象
	 * @param defaultValue 指定的默认值
	 */
	public static short getShort(Object value, short defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		if (value instanceof Number) {
			return ((Number) value).shortValue();
		}
		if (value instanceof CharSequence) {
			final CharSequence cs = (CharSequence) value;
			if (cs.length() == 0) {
				return defaultValue;
			}
			return Short.parseShort(value.toString());
		}
		throw new IllegalArgumentException("Unexpected short value:" + value);
	}

	/**
	 * 以int形式返回指定的值<br>
	 * 如果指定的值为null或无法转为int形式，将报错
	 *
	 * @param value 指定的对象
	 */
	public static int getInt(Object value) {
		Assert.notNull(value);
		return value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(value.toString());
	}

	/**
	 * 以int形式返回指定的值<br>
	 * 如果指定的值为 null 或无法转为 int 形式，将返回指定的<code>defaultValue</code>
	 *
	 * @param value 指定的对象
	 * @param defaultValue 指定的默认值
	 */
	public static int getInt(Object value, int defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		if (value instanceof CharSequence) {
			final CharSequence cs = (CharSequence) value;
			if (cs.length() == 0) {
				return defaultValue;
			}
			return Integer.parseInt(value.toString());
		}
		throw new IllegalArgumentException("Unexpected int value:" + value);
	}

	/**
	 * 以Integer形式返回指定的值<br>
	 * 如果指定的值为null或无法转为Integer形式，将返回指定的<code>defaultValue</code>
	 */
	public static Integer getInteger(Object value, Integer defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		if (value instanceof Number) {
			return value.getClass() == Integer.class ? (Integer) value : ((Number) value).intValue();
		}
		if (value instanceof CharSequence) {
			final CharSequence cs = (CharSequence) value;
			if (cs.length() == 0) {
				return defaultValue;
			}
			return Integer.parseInt(value.toString());
		}
		throw new IllegalArgumentException("Unexpected int value:" + value);
	}

	/**
	 * 以long形式返回指定的值<br>
	 * 如果指定的值为null或无法转为long形式，将报错
	 *
	 * @param value 指定的对象
	 */
	public static long getLong(Object value) {
		Assert.notNull(value);
		return value instanceof Number ? ((Number) value).longValue() : Long.parseLong(value.toString());
	}

	/**
	 * 以long形式返回指定的值<br>
	 * 如果指定的值为null或无法转为long形式，将返回指定的<code>defaultValue</code>
	 *
	 * @param value 指定的对象
	 * @param defaultValue 指定的默认值
	 */
	public static long getLong(Object value, long defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		if (value instanceof CharSequence) {
			final CharSequence cs = (CharSequence) value;
			if (cs.length() == 0) {
				return defaultValue;
			}
			return Long.parseLong(value.toString());
		}
		throw new IllegalArgumentException("Unexpected long value:" + value);
	}

	/**
	 * 以Long形式返回指定的值<br>
	 * 如果指定的值为null或无法转为Long形式，将返回指定的<code>defaultValue</code>
	 *
	 * @param value 指定的对象
	 * @param defaultValue 指定的默认值
	 */
	public static Long getLong(Object value, Long defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		if (value instanceof Number) {
			return Long.class == value.getClass() ? (Long) value : ((Number) value).longValue();
		}
		if (value instanceof CharSequence) {
			final CharSequence cs = (CharSequence) value;
			if (cs.length() == 0) {
				return defaultValue;
			}
			return Long.parseLong(value.toString());
		}
		throw new IllegalArgumentException("Unexpected long value:" + value);
	}

	/**
	 * 以float形式返回指定的值<br>
	 * 如果指定的值为null或无法转为float形式，将报错
	 *
	 * @param value 指定的对象
	 */
	public static float getFloat(Object value) {
		Assert.notNull(value);
		return value instanceof Number ? ((Number) value).floatValue() : Float.parseFloat(value.toString());
	}

	/**
	 * 以float形式返回指定的值<br>
	 * 如果指定的值为null或无法转为float形式，将返回指定的<code>defaultValue</code>
	 *
	 * @param value 指定的对象
	 * @param defaultValue 指定的默认值
	 */
	public static float getFloat(Object value, float defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		if (value instanceof Number) {
			return ((Number) value).floatValue();
		}
		if (value instanceof CharSequence) {
			final CharSequence cs = (CharSequence) value;
			if (cs.length() == 0) {
				return defaultValue;
			}
			return Float.parseFloat(value.toString());
		}
		throw new IllegalArgumentException("Unexpected float value:" + value);
	}

	/**
	 * 以double形式返回指定的值<br>
	 * 如果指定的值为null或无法转为double形式，将报错
	 *
	 * @param value 指定的对象
	 */
	public static double getDouble(Object value) {
		Assert.notNull(value);
		return value instanceof Number ? ((Number) value).doubleValue() : Double.parseDouble(value.toString());
	}

	/**
	 * 以double形式返回指定的值<br>
	 * 如果指定的值为null或无法转为double形式，将返回指定的<code>defaultValue</code>
	 *
	 * @param value 指定的对象
	 * @param defaultValue 指定的默认值
	 */
	public static double getDouble(Object value, double defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		if (value instanceof CharSequence) {
			final CharSequence cs = (CharSequence) value;
			if (cs.length() == 0) {
				return defaultValue;
			}
			return Double.parseDouble(value.toString());
		}
		throw new IllegalArgumentException("Unexpected double value:" + value);
	}

	/**
	 * 以BigDecimal形式返回指定的值<br>
	 * 如果指定的值为null或无法转为BigDecimal形式，将报错
	 *
	 * @param value 指定的对象
	 */
	public static BigDecimal getBigDecimal(Object value) {
		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		}
		if (value instanceof BigInteger) {
			return new BigDecimal((BigInteger) value);
		}
		if (value instanceof Integer || value instanceof Long) {
			return BigDecimal.valueOf(((Number) value).longValue());
		}
		return new BigDecimal(value.toString());
	}

	/**
	 * 以BigDecimal形式返回指定的值<br>
	 * 如果指定的值为null或无法转为BigDecimal形式，将返回指定的<code>defaultValue</code>
	 *
	 * @param value 指定的对象
	 * @param defaultIfEmpty 指定的默认值
	 */
	public static BigDecimal getBigDecimal(Object value, Object defaultIfEmpty) {
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
			} else {
				return allowZero ? val.doubleValue() >= 0 : val.doubleValue() > 0;
			}
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