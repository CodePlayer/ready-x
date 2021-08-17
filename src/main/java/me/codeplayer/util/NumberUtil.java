package me.codeplayer.util;

import java.math.*;

import javax.annotation.*;

/**
 * 对数值类型的数据(包含字节)进行相应处理的工具类
 *
 * @author Ready
 * @date 2012-10-29
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
		Assert.notNull(value, "将要转换为整数的值不能为null!");
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
		} else if ("".equals(value)) {
			return defaultValue;
		} else {
			try {
				return Byte.parseByte(value.toString());
			} catch (Exception e) {
				return defaultValue;
			}
		}
	}

	/**
	 * 以short形式返回指定的值<br>
	 * 如果指定的值为null或无法转为short形式，将报错
	 *
	 * @param value 指定的对象
	 */
	public static short getShort(Object value) {
		Assert.notNull(value, "将要转换为整数的值不能为null!");
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
		} else if ("".equals(value)) {
			return defaultValue;
		} else {
			try {
				return Short.parseShort(value.toString());
			} catch (Exception e) {
				return defaultValue;
			}
		}
	}

	/**
	 * 以int形式返回指定的值<br>
	 * 如果指定的值为null或无法转为int形式，将报错
	 *
	 * @param value 指定的对象
	 */
	public static int getInt(Object value) {
		Assert.notNull(value, "将要转换为整数的值不能为null!");
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
		} else if (value instanceof CharSequence && ((CharSequence) value).length() == 0) {
			return defaultValue;
		} else {
			try {
				return Integer.parseInt(value.toString());
			} catch (Exception e) {
				return defaultValue;
			}
		}
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
		} else if (value instanceof CharSequence && ((CharSequence) value).length() == 0) {
			return defaultValue;
		} else {
			try {
				return Integer.valueOf(value.toString());
			} catch (Exception e) {
				return defaultValue;
			}
		}
	}

	/**
	 * 以long形式返回指定的值<br>
	 * 如果指定的值为null或无法转为long形式，将报错
	 *
	 * @param value 指定的对象
	 */
	public static long getLong(Object value) {
		Assert.notNull(value, "将要转换为整数的值不能为null!");
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
		} else if (value instanceof CharSequence && ((CharSequence) value).length() == 0) {
			return defaultValue;
		} else {
			try {
				return Long.parseLong(value.toString());
			} catch (Exception e) {
				return defaultValue;
			}
		}
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
		} else if (value instanceof CharSequence && ((CharSequence) value).length() == 0) {
			return defaultValue;
		} else {
			try {
				return Long.valueOf(value.toString());
			} catch (Exception e) {
				return defaultValue;
			}
		}
	}

	/**
	 * 以float形式返回指定的值<br>
	 * 如果指定的值为null或无法转为float形式，将报错
	 *
	 * @param value 指定的对象
	 */
	public static float getFloat(Object value) {
		Assert.notNull(value, "将要转换为整数或小数的值不能为null!");
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
		} else if ("".equals(value)) {
			return defaultValue;
		} else {
			try {
				return Float.parseFloat(value.toString());
			} catch (Exception e) {
				return defaultValue;
			}
		}
	}

	/**
	 * 以double形式返回指定的值<br>
	 * 如果指定的值为null或无法转为double形式，将报错
	 *
	 * @param value 指定的对象
	 */
	public static double getDouble(Object value) {
		Assert.notNull(value, "将要转换为整数或小数的值不能为null!");
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
		} else if ("".equals(value)) {
			return defaultValue;
		} else {
			try {
				return Double.parseDouble(value.toString());
			} catch (Exception e) {
				return defaultValue;
			}
		}
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
		try {
			return getBigDecimal(value);
		} catch (Exception e) {
			return defaultValue == null ? null : getBigDecimal(defaultValue);
		}
	}

	/**
	 * 判断字符串内容是否为整数形式<br>
	 * 前面带0，例如"0012"仍为整数，返回true<br>
	 * 如果字符串为null，返回false<br>
	 * 如果字符串前后有空格，请先去除空格后再调用此方法，否则返回false<br>
	 *
	 * @param str 指定的字符串
	 */
	public static boolean isNumber(String str) {
		if (str == null) { // 为空则返回false
			return false;
		}
		char[] chars = str.toCharArray();
		if (chars.length < 1) { // 为空字符串则返回false
			return false;
		}
		for (char c : chars) {
			if (!Character.isDigit((int) c)) { // 不为数字则返回false
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
	 * 以尽可能快的速度判断指定字符串是否为整数形式(仅限十进制)
	 *
	 * @param str 指定的字符串
	 */
	public static boolean isNumeric(String str) {
		int length;
		if (str == null || (length = str.length()) == 0) {
			return false;
		}
		char[] chars = str.toCharArray();
		do {
			// 48-57
			if (chars[--length] < '0' || chars[length] > '9') {
				return false;
			}
		} while (length > 0);
		return true;
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