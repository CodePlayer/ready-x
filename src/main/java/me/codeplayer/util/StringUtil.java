package me.codeplayer.util;

import java.io.UnsupportedEncodingException;

/**
 * 用于对字符串类型的数据进行常用处理操作的工具类
 * 
 * @author Ready
 * @date 2012-10-29
 */
public abstract class StringUtil {

	/**
	 * 用于在2-16进制之间进行转换的映射字符数组
	 */
	protected static final char[] digits = "0123456789ABCDEF".toCharArray();

	/**
	 * 获取指定字符串的Unicode编码，例如：“中国”将返回“\u4E2D\u56FD”<br>
	 * 此方法返回的编码中，字母均采用大写形式，此外，本方法采用StringBuilder作为字符容器
	 * 
	 * @param src 指定字符串不能为null，否则将引发空指针异常
	 * @return
	 * @since 0.0.1
	 */
	public static String unicode(String src) {
		byte[] bytes = src.getBytes(Charsets.UTF_16);// 转为UTF-16字节数组
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
	 * @since 0.0.1
	 */
	public static String fastUnicode(String str) {
		byte[] bytes = str.getBytes(Charsets.UTF_16);// 转为UTF-16字节数组
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
	 * @param size 元素个数
	 * @param shift 每个元素参与拼接的平均字符数相对于2的位移量
	 * @return
	 * @since 0.0.1
	 */
	public static final StringBuilder getBuilder(int size, int shift) {
		int capacity = size << shift;
		if (capacity < 16) {
			capacity = 16;
		}
		return new StringBuilder(capacity);
	}

	/**
	 * 获取指定字符序列的字符长度
	 * 
	 * @param cs
	 * @return 对应的字符长度，如果{@code cs}为{@code null}，则返回 0
	 * @since 0.4.2
	 */
	public static final int length(final CharSequence cs) {
		return cs == null ? 0 : cs.length();
	}

	/**
	 * 根据将要追加的多个字符序列获得适当初始容量的 {@code StringBuilder}
	 * 
	 * @param extra 除指定的字符序列外应额外预留的字符容量
	 * @param s1
	 * @param s2
	 * @param s3
	 * @param s4
	 * @return
	 * @since 0.4.2
	 */
	public static final StringBuilder getBuilder(final int extra, final CharSequence s1, final CharSequence s2, final CharSequence s3, final CharSequence s4) {
		return new StringBuilder(extra + length(s1) + length(s2) + length(s3) + length(s4));
	}

	/**
	 * 根据将要追加的多个字符序列获得适当初始容量的 {@code StringBuilder}
	 * 
	 * @param extra 除指定的字符序列外应额外预留的字符容量
	 * @param s1
	 * @param s2
	 * @param s3
	 * @return
	 * @since 0.4.2
	 */
	public static final StringBuilder getBuilder(final int extra, final CharSequence s1, final CharSequence s2, final CharSequence s3) {
		return new StringBuilder(extra + length(s1) + length(s2) + length(s3));
	}

	/**
	 * 根据将要追加的多个字符序列获得适当初始容量的 {@code StringBuilder}
	 * 
	 * @param extra 除指定的字符序列外应额外预留的字符容量
	 * @param s1
	 * @param s2
	 * @return
	 * @since 0.4.2
	 */
	public static final StringBuilder getBuilder(final int extra, final CharSequence s1, final CharSequence s2) {
		return new StringBuilder(extra + length(s1) + length(s2));
	}

	/**
	 * 根据将要追加的多个字符序列获得适当初始容量的 {@code StringBuilder}
	 * 
	 * @param extra 除指定的字符序列外应额外预留的字符容量
	 * @param s1
	 * @return
	 * @since 0.4.2
	 */
	public static final StringBuilder getBuilder(final int extra, final CharSequence s1) {
		return new StringBuilder(extra + length(s1));
	}

	/**
	 * 判断指定的字符串是否为空<br>
	 * 如果字符串为null、空字符串，则返回true，否则返回false<br>
	 * <b>注意：</b>本方法不会去除字符串两边的空格，如果需要对字符串进行去除两边空格后的判断，请使用{@link StringUtil#isBlank(String str)}方法
	 * 
	 * @param str
	 * @return
	 * @since 0.0.1
	 */
	public static final boolean isEmpty(CharSequence str) {
		return str == null || str.length() == 0; // 后面的表达式相当于"".equals(str)，但比其性能稍好
	}

	/**
	 * 判断指定的字符串是否不为空<br>
	 * 如果指定字符串不为null、空字符串，则返回true，否则返回false<br>
	 * <b>注意：</b>本方法不会去除字符串两边的空格，如果需要对字符串进行去除两边空格后的判断，请使用 {@link StringUtil#notBlank(String str)}方法
	 * 
	 * @param str
	 * @return
	 * @since 0.0.1
	 */
	public static final boolean notEmpty(CharSequence str) {
		return str != null && str.length() > 0;
	}

	/**
	 * 判断指定的对象是否为空<br>
	 * 如果对象(或其toSring()返回值)为null、空字符串，则返回true<br>
	 * <b>注意：</b>本方法不会去除字符串两边的空格，如果需要对字符串进行去除两边空格后的判断，请使用isBlank(Object obj)方法
	 * 
	 * @param obj 指定的对象
	 * @return
	 * @since 0.0.1
	 */
	public static final boolean isEmpty(Object obj) {
		return obj == null || obj.toString().length() == 0; // 后面的表达式相当于"".equals(str)，但比其性能稍好
	}

	/**
	 * 判断指定的对象是否不为空<br>
	 * 如果对象(或其toSring()返回值)不为null、空字符串，则返回true，否则返回false<br>
	 * <b>注意：</b>本方法不会去除字符串两边的空格，如果需要对字符串进行去除两边空格后的判断，请使用{@link StringUtil#isBlank(Object obj)}方法
	 * 
	 * @param obj 指定的对象
	 * @return
	 * @since 0.0.1
	 */
	public static final boolean notEmpty(Object obj) {
		return obj != null && obj.toString().length() > 0;
	}

	/**
	 * 判断指定数组中是否存在为空的值(null、空字符串),如果是将返回true<br>
	 * 本方法接受多个参数，如果其中有任意一个为空，就返回true 如果指定的key不存在也返回true
	 * 
	 * @param values 指定的数组
	 * @return
	 * @see me.codeplayer.util.StringUtil#isEmpty(Object)
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
	 * @since 0.0.1
	 */
	public static final boolean isBlank(CharSequence str) {
		if (str == null) {
			return true;
		}
		int length = str.length();
		for (int i = 0; i < length; i++) {
			if (str.charAt(i) > ' ') { // ' '即'\u0020'，参考 java.lang.String.trim() 方法的实现
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断指定的字符串是否不为空<br>
	 * 如果字符串不为null、空字符串、空格字符串，则返回true，否则返回false<br>
	 * <b>注意：</b>本方法会先去除字符串两边的空格，再判断
	 * 
	 * @param str
	 * @return
	 * @since 0.0.1
	 */
	public static final boolean notBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * 判断指定的对象是否为空<br>
	 * 如果对象(或其toSring()返回值)为null、空字符串、空格字符串，则返回true<br>
	 * <b>注意：</b>本方法会先去除字符串两边的空格，再判断
	 * 
	 * @param obj 指定的对象
	 * @return
	 * @since 0.0.1
	 */
	public static final boolean isBlank(Object obj) {
		return obj == null || isBlank(obj.toString());
	}

	/**
	 * 判断指定的对象是否为空<br>
	 * 如果对象(或其toSring()返回值)不为null、空字符串、空格字符串，则返回true，否则返回false<br>
	 * <b>注意：</b>本方法会先去除字符串两边的空格，再判断
	 * 
	 * @param obj 指定的对象
	 * @return
	 * @since 0.0.1
	 */
	public static final boolean notBlank(Object obj) {
		return !isBlank(obj);
	}

	/**
	 * 判断指定数组中是否为空的值(null、空字符串、空格字符串),如果是，将返回true<br>
	 * 本方法接受多个参数，如果其中有任意一个为空，就返回true<br>
	 * 本方法会去除两边的空格后再判断 如果指定的key不存在也返回true
	 * 
	 * @param values 指定的数组
	 * @return
	 * @since 0.0.1
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
	 * @since 0.0.1
	 */
	public static final String toString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	/**
	 * 以去除两边空格的String形式返回对象值，如果对象为null，则返回""
	 * 
	 * @param obj 指定的对象
	 * @return
	 * @since 0.0.1
	 */
	public static final String trim(Object obj) {
		return obj == null ? "" : obj.toString().trim();
	}

	/**
	 * 以String的形式返回对象值，如果对象为null或空格字符串，则返回"&amp;nbsp;"
	 * 
	 * @param obj 指定的对象
	 * @return
	 * @since 0.0.1
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
	 * @since 0.0.1
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
	 * @since 0.0.1
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
	 * @since 0.0.1
	 */
	public static final String zeroFill(String str, int maxLength) {
		return pad(str, '0', maxLength, true);
	}

	/**
	 * 如果字符串不足指定位数，则在其左侧或右侧补充指定的字符，直到指定位数<br>
	 * 如果字符串=null，则返回空字符串""<br>
	 * 如果字符串位数大于指定位数，则返回原字符串
	 * 
	 * @param str 指定的字符串
	 * @param ch 指定的字符
	 * @param maxLength 指定位数，不能小于1
	 * @param left 是在字符串左侧添加，还是右侧添加。true=左侧，false=右侧
	 * @return
	 * @since 0.0.1
	 */
	private static final String pad(String str, char ch, int maxLength, boolean left) {
		if (str == null)
			return "";
		if (maxLength < 1)
			throw new IllegalArgumentException("指定位数不能小于1!");
		int length = str.length();
		if (maxLength > length) {
			int diffSize = maxLength - length;
			char[] chars = new char[maxLength]; // 直接采用高效的char数组形式构建字符串
			// Arrays.fill(chars, '0'); //内部也是循环赋值，直接循环赋值效率更高
			if (left) {
				for (int i = 0; i < diffSize; i++) {
					chars[i] = ch;
				}
				System.arraycopy(str.toCharArray(), 0, chars, diffSize, length); // 此方法由JVM底层实现，因此效率相对较高
			} else {
				for (int i = diffSize; i < maxLength; i++) {
					chars[i] = ch;
				}
				System.arraycopy(str.toCharArray(), 0, chars, 0, length);
			}
			str = new String(chars);
		}
		return str;
	}

	/**
	 * 如果字符串不足指定位数，则在前面补充指定的字符，直到指定位数<br>
	 * 如果字符串=null，则返回空字符串""<br>
	 * 如果字符串位数大于指定位数，则返回原字符串
	 * 
	 * @param str 字符串
	 * @param ch 指定的字符
	 * @param maxLength 指定位数，不能小于1
	 * @return
	 * @since 0.0.1
	 */
	public static final String leftPad(String str, char ch, int maxLength) {
		return pad(str, ch, maxLength, true);
	}

	/**
	 * 如果字符串不足指定位数，则在后面补充指定的字符，直到指定位数<br>
	 * 如果字符串=null，则返回空字符串""<br>
	 * 如果字符串位数大于指定位数，则返回原字符串
	 * 
	 * @param str 字符串
	 * @param ch 指定的字符
	 * @param maxLength 指定位数，不能小于1
	 * @return
	 * @since 0.0.1
	 */
	public static final String rightPad(String str, char ch, int maxLength) {
		return pad(str, ch, maxLength, false);
	}

	/**
	 * 去除字符串两侧的空白字符<br>
	 * 如果为null则返回空字符串""
	 * 
	 * @param str
	 * @return
	 * @since 0.0.1
	 */
	public static final String trim(String str) {
		return str == null ? "" : str.trim();
	}

	/**
	 * 将指定字符串的 <code>beginIndex</code> 到 <code>endIndex</code> (不包括 <code>endIndex</code> ) 之间的字符全部替换为字符 <code>ch</code>
	 * 
	 * @param str 指定的字符串
	 * @param ch 指定的字符
	 * @param beginIndex 指定的字符串起始索引(可以为负数，表示 <code>beginIndex + str.length()</code> )
	 * @param endIndex 指定的字符串结束索引(可以为负数，表示 <code>endIndex + str.length()</code> )
	 * @return
	 * @since 0.1.1
	 */
	public static final String replaceChars(String str, char ch, int beginIndex, int endIndex) {
		final int length = str.length();
		int[] range = ensureRangeSafe(beginIndex, endIndex, length);
		if (range == null) {
			return str;
		}
		final char[] chars = str.toCharArray();
		for (int i = range[0]; i < range[1]; i++) {
			chars[i] = ch;
		}
		return new String(chars);
	}

	/**
	 * 确保起始和结束索引的范围边界安全，并返回范围安全的 [开始索引, 结束索引] 数组<br>
	 * 如果索引参数为负，自动加上 <code>length</code>，如果处理后的 <code>beginIndex > endIndex</code>，则自动对调位置<br>
	 * 方法起始索引或结束索引超出合理边界，方法内会尽可能地根据 <code>length</code> 进行调整：<br>
	 * 1、如果 <code>beginIndex < -length </code> ，则 <code>beginIndex = 0</code><br>
	 * 2、如果 <code>endIndex > length </code> ，则 <code>endIndex = length</code>
	 * 
	 * @param beginIndex 起始索引
	 * @param endIndex 结束索引
	 * @param length 指定数组、集合或字符串的长度，不能小于0
	 * @return 如果范围安全则返回对应的范围数组，否则返回null
	 * @author Ready
	 * @since 0.4.2
	 */
	public static final int[] ensureRangeSafe(int beginIndex, int endIndex, final int length) {
		if (length <= 0 || beginIndex == endIndex) {
			return null;
		}
		if (beginIndex < 0) { // 确保 beginIndex 参数安全
			beginIndex += length;
			if (beginIndex < 0) {
				beginIndex = 0;
			}
		} else if (beginIndex >= length) {
			return null;
		}
		if (endIndex < 0) { // 确保 endIndex 参数安全
			endIndex += length;
			if (endIndex < 0) {
				return null;
			}
		} else if (endIndex > length) {
			endIndex = length;
		}
		int[] range = new int[] { beginIndex, endIndex };
		if (beginIndex > endIndex) {
			range[0] = endIndex;
			range[1] = beginIndex;
		}
		return range;
	}

	/**
	 * 将指定字符串的 <code>beginIndex</code> 到末尾之间的字符全部替换为字符 <code>ch</code>
	 * 
	 * @param str 指定的字符串
	 * @param ch 指定的字符
	 * @param beginIndex 指定的字符串起始索引(可以为负数，表示 <code>beginIndex + str.length()</code> )
	 * @return
	 * @since 0.1.1
	 */
	public static final String replaceChars(String str, char ch, int beginIndex) {
		return replaceChars(str, ch, beginIndex, str.length());
	}

	/**
	 * 将指定字符串的 <code>beginIndex</code> 到 <code>endIndex</code> (不包括 <code>endIndex</code> ) 之间的子字符串全部替换为指定的字符串 <code>replacement</code>
	 * 
	 * @param str 指定的字符串
	 * @param replacement 指定的字符串
	 * @param beginIndex 指定的字符串起始索引(可以为负数，表示 <code>beginIndex + str.length()</code> )
	 * @param endIndex 指定的字符串结束索引(可以为负数，表示 <code>endIndex + str.length()</code> )
	 * @return
	 * @since 0.1.1
	 */
	public static final String replaceSubstring(String str, String replacement, int beginIndex, int endIndex) {
		if (replacement == null || replacement.length() == 0) {
			return str;
		}
		final int length = str.length();
		int[] range = ensureRangeSafe(beginIndex, endIndex, length);
		if (range == null) {
			return str;
		}
		beginIndex = range[0];
		endIndex = range[1];
		StringBuilder sb = new StringBuilder(replacement.length() + length - endIndex + beginIndex);
		if (beginIndex > 0) {
			sb.append(str, 0, beginIndex);
		}
		sb.append(replacement);
		if (endIndex < length) {
			sb.append(str, endIndex, length);
		}
		return sb.toString();
	}

	/**
	 * 将指定字符串的 <code>beginIndex</code> 及其后的子字符串全部替换为指定的字符串 <code>replacement</code>
	 * 
	 * @param str 指定的字符串
	 * @param replacement 指定的字符串
	 * @param beginIndex 指定的字符串起始索引(可以为负数，表示 <code>beginIndex + str.length()</code> )
	 * @return
	 * @since 0.1.1
	 */
	public static final String replaceSubstring(String str, String replacement, int beginIndex) {
		return replaceSubstring(str, replacement, beginIndex, str.length());
	}

	/**
	 * 将字符串从指定字符集编码转换为目标字符集编码
	 * 
	 * @param str 指定的字符串
	 * @param originalCharset 原始字符集编码
	 * @param targetCharset 目标字符集编码
	 * @return
	 * @since 0.3.9
	 */
	public static final String transEncoding(String str, String originalCharset, String targetCharset) {
		try {
			return new String(str.getBytes(originalCharset), targetCharset);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 将指定的URI参数字符串转换为目标字符集编码<br>
	 * 本方法实际上是将字符串从ISO-8859-1编码转换为指定的目标编码
	 * 
	 * @param str 指定的URI参数字符串
	 * @param targetCharset 目标字符集编码
	 * @return
	 * @since 0.3.9
	 */
	public static final String transEncodingForURI(String str, String targetCharset) {
		return transEncoding(str, "ISO-8859-1", targetCharset);
	}

	/**
	 * 颠倒(反转)字符串的字符顺序，并返回颠倒后的字符串<br>
	 * 如果字符串为null，则返回空字符串""
	 * 
	 * @param str 指定的字符串
	 * @return
	 * @since 0.0.1
	 */
	public static final String reverse(CharSequence str) {
		if (str == null) {
			return "";
		}
		return new StringBuilder(str).reverse().toString();
	}

	/**
	 * 判断指定字符串是否以指定的单个字符开头
	 * 
	 * @param str 指定的字符串
	 * @param firstChar 指定的单个字符
	 * @return
	 * @since 0.4.2
	 */
	public static final boolean startWith(String str, char firstChar) {
		return str != null && str.length() > 0 && str.charAt(0) == firstChar;
	}

	/**
	 * 使用指定的转义字符对用于LIKE语句的字符串进行转义，以防止SQL语句注入
	 * 
	 * @param likeStr 指定的字符串
	 * @param escapeChar 转义字符
	 * @param appendLikeWildcard 是否需要在字符串两侧添加通配符'%'
	 * @return
	 * @since 0.3.5
	 */
	public static final String escapeSQLLike(String likeStr, char escapeChar, boolean appendLikeWildcard) {
		if (StringUtil.isEmpty(likeStr)) {
			return "";
		}
		boolean modified = false;
		final char[] strChars = likeStr.toCharArray();
		int length = strChars.length + 2;
		if (appendLikeWildcard) {
			length += 2;
		}
		StringBuilder sb = new StringBuilder(length);
		String searchChars = "\\\'_%;";
		if (appendLikeWildcard) {
			sb.append('%');
		}
		for (int i = 0; i < strChars.length; i++) {
			if (searchChars.indexOf(strChars[i], 0) != -1) {
				modified = true;
				sb.append(escapeChar);
			}
			sb.append(strChars[i]);
		}
		if (appendLikeWildcard) {
			sb.append('%');
		}
		return appendLikeWildcard || modified ? sb.toString() : likeStr;
	}

	/**
	 * 将指定的用于LIKE语句的字符串转义，以防止SQL语句注入<br>
	 * 该方法默认使用'\'进行转义操作
	 * 
	 * @param likeStr 指定的字符串
	 * @param appendLikeWildcard 是否需要在字符串两侧添加通配符'%'
	 * @return
	 * @since 0.3.5
	 */
	public static final String escapeSQLLike(String likeStr, boolean appendLikeWildcard) {
		return escapeSQLLike(likeStr, '\\', appendLikeWildcard);
	}

	/**
	 * 将指定的用于LIKE语句的字符串转义，以防止SQL语句注入<br>
	 * 该方法默认使用'\'进行转义操作
	 * 
	 * @param likeStr 指定的字符串
	 * @return
	 * @since 0.3.5
	 */
	public static final String escapeSQLLike(String likeStr) {
		return escapeSQLLike(likeStr, '\\', false);
	}

	/**
	 * 检测指定字符串中是否存在指定的单词
	 * 
	 * @param container 待检测的字符串
	 * @param searchedWord 指定的单词
	 * @param seperatorChars 单词两侧必须是指定的字符之一或位于字符串 {@code container }的首/尾位置
	 * @return
	 * @author Ready
	 * @since 0.4.2
	 */
	public static final boolean containsWord(String container, String searchedWord, String seperatorChars) {
		if (searchedWord == null)
			return false;
		int startIndex = container.indexOf(searchedWord);
		if (startIndex == -1) {
			return false;
		}
		if (startIndex == 0 || seperatorChars.indexOf(container.charAt(startIndex - 1)) != -1) {
			int endPos = startIndex + searchedWord.length();
			if (endPos == container.length() || seperatorChars.indexOf(container.charAt(endPos)) != -1) {
				return true;
			}
		}
		return false;
	}
}
