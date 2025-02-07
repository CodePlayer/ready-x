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
public abstract class NumberX {

	/**
	 * 以byte形式返回指定的值<br>
	 * 如果指定的值为null或无法转为byte形式，将报错
	 *
	 * @param value 指定的对象
	 */
	public static byte getByte(Object value) {
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
	 * @param defaultValue 指定的默认值
	 */
	public static BigDecimal getBigDecimal(Object value, Object defaultValue) {
		if (value == null) {
			return defaultValue == null ? null : getBigDecimal(defaultValue);
		}
		if (value instanceof Number || value instanceof CharSequence) {
			return getBigDecimal(value);
		}
		throw new IllegalArgumentException("Unexpected decimal value:" + value);
	}

	/**
	 * 判断字符串内容是否为整数形式<br>
	 * 前面带0，例如"0012"仍为整数，返回true<br>
	 * 如果字符串为null，返回false<br>
	 * 如果字符串前后有空格，请先去除空格后再调用此方法，否则返回false<br>
	 *
	 * @param cs 指定的字符串
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
			throw new IllegalArgumentException("指定长度不能小于0!");
		}
		return str != null && str.length() == length && isNumber(str);
	}

	/**
	 * 判断指定对象是否为整数类型或能够转为整数形式
	 */
	public static boolean isNumber(Object obj) {
		if (obj instanceof Number) {
			Number num = (Number) obj;
			return num.longValue() == num.doubleValue();
		} else {
			return obj != null && isNumber(obj.toString());
		}
	}

	/**
	 * 以尽可能快的速度判断指定字符串是否为整数形式（仅限十进制 <code>[0-9]</code> ）
	 *
	 * @param cs 指定的字符串
	 */
	public static boolean isNumeric(final CharSequence cs) {
		return cs != null && isNumeric0(cs, 0, cs.length());
	}

	/**
	 * 以尽可能快的速度判断指定字符序列片段是否全部都是数字（仅限十进制 <code>[0-9]</code> ）
	 *
	 * @param cs 指定的字符串
	 * @param start 开始位置（包括）
	 * @param end 结束位置（不包括）
	 */
	public static boolean isNumeric(final CharSequence cs, int start, int end) {
		if (cs == null || cs.length() == 0) {
			return false;
		}
		return isNumeric0(cs, start, end);
	}

	/**
	 * 以尽可能快的速度判断指定字符序列片段是否全部都是数字（仅限十进制 <code>[0-9]</code> ）
	 *
	 * @param cs 指定的字符串
	 * @param start 开始位置（包括）
	 * @param end 结束位置（不包括）
	 */
	static boolean isNumeric0(final CharSequence cs, int start, int end) {
		for (int i = start; i < end; i++) {
			char ch = cs.charAt(i);
			if (ch < '0' || ch > '9') {
				return false;
			}
		}
		return start < end;
	}

	/**
	 * 判断指定对象的字符串形式是否为整数形式
	 */
	public static boolean isInt(Object value) {
		return value != null && (value instanceof Integer || isNumber(value.toString()));
	}

	/**
	 * 判断字符串内容是否为整数或小数形式<br>
	 * 前面带0，例如"0012"仍为整数，返回true<br>
	 * 如果字符串为null，返回false<br>
	 * 如果字符串前后有空格，请先去除空格后再调用此方法，否则返回false<br>
	 * 此方法性能是使用正则表达式验证性能的4-9倍
	 *
	 * @param str 需要判断的字符串
	 */
	public static boolean isDouble(String str) {
		if (str == null) {
			return false;
		}
		int pointPos = str.indexOf('.', 0) + 1;
		if (pointPos == str.length()) { // "."在最后一位
			return false;
		} else if (pointPos > 0) { // 有"."
			return isNumeric(str.substring(0, pointPos - 1)) && isNumeric(str.substring(pointPos));
		} else {
			return isNumeric(str);
		}
	}

	/**
	 * 判断指定对象是否为整数或小数形式<br>
	 * 前面带0，例如"0012"仍为整数，返回true<br>
	 * 如果字符串为null，返回false<br>
	 * 如果字符串前后有空格，请先去除空格后再调用此方法，否则返回 false
	 *
	 * @param value 需要判断的对象
	 */
	public static boolean isDouble(Object value) {
		return value != null && (value instanceof Number || isDouble(value.toString()));
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