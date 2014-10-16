package me.ready.util;

import java.io.UnsupportedEncodingException;

import me.ready.e.LogicException;

/**
 * 用于对字符串类型的数据进行常用处理操作的工具类
 * 
 * @author Ready
 * @date 2012-10-29
 */
public class StringUtil {

	/**
	 * 用于在2-16进制之间进行转换的映射字符数组
	 */
	protected static final char[] digits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 获取指定字符串的Unicode编码，例如：“中国”将返回“\u4e2d\u56fd”<br>
	 * 此方法返回的编码中，字母均采用小写形式，此外，本方法采用StringBuilder作为字符容器
	 * 
	 * @param src 指定字符串不能为null，否则将引发空指针异常
	 * @return
	 */
	public static String unicode(String src) {
		byte[] bytes;
		try {
			bytes = src.getBytes("UTF-16");// 转为UTF-16字节数组
		} catch (UnsupportedEncodingException e) {
			throw new LogicException(e);
		}
		int length = bytes.length;
		if (length > 2) {// 由于转换出来的字节数组前两位属于UNICODE固定标记，因此要过滤掉
			int i = 2;
			StringBuilder sb = new StringBuilder((length - i) * 3);
			boolean isOdd = false;
			for (; i < length; i++) {
				if (isOdd = !isOdd) {
					sb.append("\\u");
				}
				sb.append(digits[bytes[i] >> 4 & 0xf]);
				sb.append(digits[bytes[i] & 0xf]);
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	/**
	 * 获取指定字符串的Unicode编码，例如："中国"将返回"\u4E2D\u56FD"<br>
	 * 此方法返回的编码中，字母均采用大写形式，此外，由于编码的长度是已知的，本方法采用char数组作为字符容器，省略了StringBuilder追加时的长度判断以及封装损耗，性能更加优异
	 * 
	 * @param src 指定字符串不能为null，否则将引发空指针异常
	 * @return
	 */
	public static String fastUnicode(String str) {
		byte[] bytes;
		try {
			bytes = str.getBytes("UTF-16");// 转为UTF-16字节数组
		} catch (UnsupportedEncodingException e) {
			throw new LogicException(e);
		}
		int length = bytes.length;
		if (length > 2) {
			int i = 2;
			char[] chars = new char[(length - i) * 3];
			int index = 0;
			boolean isOdd = false;
			for (; i < length; i++) {
				if (isOdd = !isOdd) {
					chars[index++] = '\\';
					chars[index++] = 'u';
				}
				chars[index++] = digits[bytes[i] >> 4 & 0xf];
				chars[index++] = digits[bytes[i] & 0xf];
			}
			return new String(chars);
		} else {
			return "";
		}
	}

	/**
	 * 根据每个元素的平均大小和元素的格式创建一个适用于其容量的StringBuilder<br>
	 * 内部采用<code>length << shift </code>来获取设置初始容量<br>
	 * 并且生成的StringBuilder的最小容量为16(StringBuilder的默认容量)
	 * 
	 * @param length 元素个数
	 * @param shift 每个元素参与拼接的平均字符数相对于2的位移量
	 * @return
	 */
	public static final StringBuilder getBuilder(int length, int shift) {
		int capacity = length << shift;
		if (capacity < 16) {
			capacity = 16;
		}
		return new StringBuilder(capacity);
	}

	/**
	 * 判断指定的字符串是否为空<br>
	 * 如果字符串为null、空字符串，则返回true<br>
	 * <b>注意：</b>本方法不会去除字符串两边的空格，如果需要对字符串进行去除两边空格后的判断，请使用isBlank(String str)方法
	 * 
	 * @param str
	 * @return
	 */
	public static final boolean isEmpty(String str) {
		return str == null || str.length() == 0; // 后面的表达式相当于"".equals(str)，但比其性能稍好
	}

	/**
	 * 判断指定的对象是否为空<br>
	 * 如果对象(或其toSring()返回值)为null、空字符串，则返回true<br>
	 * <b>注意：</b>本方法不会去除字符串两边的空格，如果需要对字符串进行去除两边空格后的判断，请使用isBlank(Object obj)方法
	 * 
	 * @param obj 指定的对象
	 * @return
	 */
	public static final boolean isEmpty(Object obj) {
		return obj == null || obj.toString().length() == 0; // 后面的表达式相当于"".equals(str)，但比其性能稍好
	}

	/**
	 * 判断指定数组中是否存在为空的值(null、空字符串),如果是将返回true<br>
	 * 本方法接受多个参数，如果其中有任意一个为空，就返回true 如果指定的key不存在也返回true
	 * 
	 * @param values 指定的数组
	 * @return
	 * @see me.ready.util.StringUtil#isEmpty(Object)
	 */
	public boolean hasEmpty(Object... values) {
		int length = ArrayUtil.getLength(values, true);
		do {
			if (isEmpty(values[--length])) {
				return true;
			}
		} while (length > 0);
		return false;
	}

	/**
	 * 判断指定的字符串是否为空<br>
	 * 如果字符串为null、空字符串、空格字符串，则返回true<br>
	 * <b>注意：</b>本方法会先去除字符串两边的空格，再判断
	 * 
	 * @param str
	 * @return
	 */
	public static final boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 判断指定的对象是否为空<br>
	 * 如果对象(或其toSring()返回值)为null、空字符串、空格字符串，则返回true<br>
	 * <b>注意：</b>本方法会先去除字符串两边的空格，再判断
	 * 
	 * @param obj 指定的对象
	 * @return
	 */
	public static final boolean isBlank(Object obj) {
		return obj == null || obj.toString().trim().length() == 0;
	}

	/**
	 * 判断指定数组中是否为空的值(null、空字符串、空格字符串),如果是，将返回true<br>
	 * 本方法接受多个参数，如果其中有任意一个为空，就返回true<br>
	 * 本方法会去除两边的空格后再判断 如果指定的key不存在也返回true
	 * 
	 * @param values 指定的数组
	 * @return
	 */
	public boolean hasBlank(Object... values) {
		int length = ArrayUtil.getLength(values, true);
		do {
			if (isBlank(values[--length])) {
				return true;
			}
		} while (length > 0);
		return false;
	}

	/**
	 * 以String的形式返回对象值，如果对象为null，则返回""
	 * 
	 * @param obj 指定的对象
	 * @return
	 */
	public static final String toString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	/**
	 * 以去除两边空格的String形式返回对象值，如果对象为null，则返回""
	 * 
	 * @param obj 指定的对象
	 * @return
	 */
	public static final String trim(Object obj) {
		return obj == null ? "" : obj.toString().trim();
	}

	/**
	 * 以String的形式返回对象值，如果对象为null或空格字符串，则返回"&amp;nbsp;"
	 * 
	 * @param obj 指定的对象
	 * @return
	 */
	public static final String trim4Html(Object obj) {
		String str;
		return obj != null && (str = obj.toString().trim()).length() > 0 ? str : "&nbsp;";
	}

	/**
	 * 如果指定字符串超过限制长度<code>maxLength</code>,则返回限制长度前面的部分字符串<br>
	 * 如果指定字符串==null，则返回空字符串<br>
	 * 如果字符串超出指定长度，则返回maxLength前面的部分，并在末尾加上后缀<code>suffix</code>
	 * 
	 * @param str 指定的字符串
	 * @param maxLength 最大限制长度
	 * @param suffix 超出长度时添加的指定后缀,如果不需要，可以为null
	 * @return
	 */
	public static final String limitChars(String str, int maxLength, String suffix) {
		if (str == null) {
			return "";
		}
		str = str.trim();
		int length = str.length();
		if (length <= maxLength) {
			return str;
		} else {
			str = str.substring(0, maxLength);
			return suffix == null ? suffix : str + suffix;
		}
	}

	/**
	 * 如果指定字符串超过限制长度<code>maxLength</code>,则返回限制长度前面的部分字符串<br>
	 * 如果指定字符串==null，则返回空字符串<br>
	 * 如果字符串超出指定长度，则返回maxLength前面的部分，并在末尾加上后缀“...”
	 * 
	 * @param str 指定的字符串
	 * @param maxLength 最大限制长度
	 * @return
	 */
	public static final String limitChars(String str, int maxLength) {
		return limitChars(str, maxLength, "...");
	}

	/**
	 * 如果字符串不足指定位数，则前面补0，直到指定位数<br>
	 * 如果字符串=null，则返回空字符串""<br>
	 * 如果字符串位数大于指定位数，则返回原字符串
	 * 
	 * @param str 字符串
	 * @param maxLength 指定位数，不能小于1
	 * @return
	 */
	public static final String zeroFill(String str, int maxLength) {
		return fill(str, '0', maxLength);
	}

	/**
	 * 如果字符串不足指定位数，则在前面补充指定的字符，直到指定位数<br>
	 * 如果字符串=null，则返回空字符串""<br>
	 * 如果字符串位数大于指定位数，则返回原字符串
	 * 
	 * @param str 字符串
	 * @param maxLength 指定位数，不能小于1
	 * @return
	 */
	public static final String fill(String str, char ch, int maxLength) {
		if (str == null)
			return "";
		if (maxLength < 1)
			throw new LogicException("指定位数不能小于1!");
		int length = str.length();
		if (maxLength > length) {
			int diffSize = maxLength - length;
			char[] chars = new char[maxLength]; // 直接采用高效的char数组形式构建字符串
			for (int i = 0; i < diffSize; i++) {
				chars[i] = ch;
			}
			// Arrays.fill(chars, '0'); //内部也是循环赋值，直接循环赋值效率更高
			System.arraycopy(str.toCharArray(), 0, chars, diffSize, length); // 此方法由JVM底层实现，因此效率相对较高
			str = new String(chars);
		}
		return str;
	}

	/**
	 * 去除字符串两侧的空白字符<br>
	 * 如果为null则返回空字符串""
	 * 
	 * @param str
	 * @return
	 */
	public static final String trim(String str) {
		return str == null ? "" : str.trim();
	}
}
