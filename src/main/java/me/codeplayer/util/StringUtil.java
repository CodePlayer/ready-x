package me.codeplayer.util;

import java.nio.charset.*;

import javax.annotation.*;

import org.apache.commons.lang3.*;

import me.codeplayer.util.CharConverter.*;

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
	 * 此方法返回的编码中，字母均采用大写形式，此外，本方法采用 {@link StringBuilder} 作为字符容器
	 *
	 * @param src 指定字符串不能为null，否则将引发空指针异常
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
	 * @param str 指定字符串不能为null，否则将引发空指针异常
	 * @since 0.0.1
	 */
	public static String fastUnicode(String str) {
		byte[] bytes = str.getBytes(Charsets.UTF_16); // 转为UTF-16字节数组
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
	 * @param size  元素个数
	 * @param shift 每个元素参与拼接的平均字符数相对于2的位移量
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
	 * @return 对应的字符长度，如果 {@code cs} 为 {@code null}，则返回 0
	 * @since 0.4.2
	 */
	public static final int length(final CharSequence cs) {
		return cs == null ? 0 : cs.length();
	}

	/**
	 * 根据将要追加的多个字符序列获得适当初始容量的 {@code StringBuilder}
	 *
	 * @param extra 除指定的字符序列外应额外预留的字符容量
	 * @since 0.4.2
	 */
	public static final StringBuilder getBuilder(final int extra, final CharSequence s1, final CharSequence s2, final CharSequence s3, final CharSequence s4) {
		return new StringBuilder(extra + length(s1) + length(s2) + length(s3) + length(s4));
	}

	/**
	 * 根据将要追加的多个字符序列获得适当初始容量的 {@code StringBuilder}
	 *
	 * @param extra 除指定的字符序列外应额外预留的字符容量
	 * @since 0.4.2
	 */
	public static final StringBuilder getBuilder(final int extra, final CharSequence s1, final CharSequence s2, final CharSequence s3) {
		return new StringBuilder(extra + length(s1) + length(s2) + length(s3));
	}

	/**
	 * 根据将要追加的多个字符序列获得适当初始容量的 {@code StringBuilder}
	 *
	 * @param extra 除指定的字符序列外应额外预留的字符容量
	 * @since 0.4.2
	 */
	public static final StringBuilder getBuilder(final int extra, final CharSequence s1, final CharSequence s2) {
		return new StringBuilder(extra + length(s1) + length(s2));
	}

	/**
	 * 根据将要追加的多个字符序列获得适当初始容量的 {@code StringBuilder}
	 *
	 * @param extra 除指定的字符序列外应额外预留的字符容量
	 * @since 0.4.2
	 */
	public static final StringBuilder getBuilder(final int extra, final CharSequence s1) {
		return new StringBuilder(extra + length(s1));
	}

	/**
	 * 拼接两个字符串（忽略其中为 null 的参数）
	 *
	 * @since 3.0.0
	 */
	@Nullable
	public static String concat(@Nullable String a, @Nullable String b) {
		if (a == null)
			return b;
		if (b == null)
			return a;
		return a.concat(b);
	}

	/**
	 * 拼接多个字符串（忽略其中为 null 的参数）
	 *
	 * @since 3.0.0
	 */
	public static String concat(@Nullable String a, @Nullable String b, @Nullable String c) {
		final int aSize = length(a), bSize = length(b), cSize = length(c);
		final StringBuilder sb = new StringBuilder(aSize + bSize + cSize);
		if (aSize > 0)
			sb.append(a);
		if (bSize > 0)
			sb.append(b);
		if (cSize > 0)
			sb.append(c);
		return sb.toString();
	}

	/**
	 * 拼接多个字符串（忽略其中为 null 的参数）
	 *
	 * @since 3.0.0
	 */
	public static StringBuilder append(@Nullable StringBuilder sb, String... strs) {
		int size = 0;
		for (int i = 0; i < strs.length; i++) {
			size += length(strs[i]);
		}
		sb = initBuilder(sb, size);
		for (int i = 0; i < strs.length; i++) {
			if (strs[i] != null) {
				sb.append(strs[i]);
			}
		}
		return sb;
	}

	/**
	 * 拼接多个字符串（忽略其中为 null 的参数）
	 *
	 * @since 3.0.0
	 */
	public static String concats(String... strs) {
		return append(null, strs).toString();
	}

	/**
	 * 初始化一个还可以添加 {@code appendLength} 个字符的 StringBuilder
	 *
	 * @param sb           如果为null，内部会自动创建
	 * @param appendLength 还需要扩展的字符数
	 * @since 3.0.0
	 */
	public static final StringBuilder initBuilder(@Nullable StringBuilder sb, int appendLength) {
		if (sb == null) {
			sb = new StringBuilder(appendLength);
		} else {
			sb.ensureCapacity(sb.length() + appendLength);
		}
		return sb;
	}

	/**
	 * 判断指定的字符串是否为空<br>
	 * 如果字符串为null、空字符串，则返回true，否则返回false<br>
	 * <b>注意：</b>本方法不会去除字符串两边的空格，如果需要对字符串进行去除两边空格后的判断，请使用 {@link #isBlank(CharSequence)} 方法
	 *
	 * @since 0.0.1
	 */
	public static final boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0; // 后面的表达式相当于"".equals(cs)，但比其性能稍好
	}

	/**
	 * 判断指定的字符串是否不为空<br>
	 * 如果指定字符串不为null、空字符串，则返回true，否则返回false<br>
	 * <b>注意：</b>本方法不会去除字符串两边的空格，如果需要对字符串进行去除两边空格后的判断，请使用 {@link #notBlank(CharSequence)} 方法
	 *
	 * @since 0.0.1
	 */
	public static final boolean notEmpty(final CharSequence cs) {
		return cs != null && cs.length() > 0;
	}

	/**
	 * 判断指定的字符序列数组是否存在不为空的元素<br>
	 * 如果数组中存在不为null、空字符串的元素，则返回true，否则返回false<br>
	 *
	 * @since 1.0.2
	 */
	public static final boolean isAnyNotEmpty(final CharSequence... css) {
		if (css != null && css.length > 0) {
			for (final CharSequence cs : css) {
				if (notEmpty(cs)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断指定的对象是否为空<br>
	 * 如果对象(或其toSring()返回值)为null、空字符串，则返回true<br>
	 * <b>注意：</b>本方法不会去除字符串两边的空格，如果需要对字符串进行去除两边空格后的判断，请使用isBlank(Object obj)方法
	 *
	 * @param obj 指定的对象
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
	 * @see me.codeplayer.util.StringUtil#isEmpty(Object)
	 */
	public static final boolean hasEmpty(Object... values) {
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
	 * @since 0.0.1
	 */
	public static final boolean notBlank(CharSequence cs) {
		return !isBlank(cs);
	}

	/**
	 * 判断指定的对象是否为空<br>
	 * 如果对象(或其toSring()返回值)为null、空字符串、空格字符串，则返回true<br>
	 * <b>注意：</b>本方法会先去除字符串两边的空格，再判断
	 *
	 * @param obj 指定的对象
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
	 * @since 0.0.1
	 */
	public static final boolean hasBlank(Object... values) {
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
	 * @since 0.0.1
	 */
	public static final String toString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	/**
	 * 以去除两边空格的String形式返回对象值，如果对象为null，则返回""
	 *
	 * @param obj 指定的对象
	 * @since 0.0.1
	 */
	public static final String trim(Object obj) {
		return obj == null ? "" : obj.toString().trim();
	}

	/**
	 * 以String的形式返回对象值，如果对象为null或空格字符串，则返回"&amp;nbsp;"
	 *
	 * @param obj 指定的对象
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
	 * @param str       指定的字符串
	 * @param maxLength 最大限制长度
	 * @param suffix    超出长度时添加的指定后缀,如果不需要，可以为null
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
			return suffix == null ? str : str.concat(suffix);
		}
	}

	/**
	 * 如果指定字符串超过限制长度<code>maxLength</code>,则返回限制长度前面的部分字符串<br>
	 * 如果指定字符串==null，则返回空字符串<br>
	 * 如果字符串超出指定长度，则返回maxLength前面的部分，并在末尾加上后缀“...”
	 *
	 * @param str       指定的字符串
	 * @param maxLength 最大限制长度
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
	 * @param str       字符串
	 * @param maxLength 指定位数，不能小于1
	 * @since 0.0.1
	 */
	public static final String zeroFill(String str, int maxLength) {
		return pad(str, '0', maxLength, true);
	}

	/**
	 * 如果字符串不足指定位数，则在其左侧或右侧补充指定的字符，直到指定位数<br>
	 * 如果字符串 = null，则返回空字符串""<br>
	 * 如果字符串位数大于指定位数，则返回原字符串
	 *
	 * @param str       指定的字符串
	 * @param ch        指定的字符
	 * @param maxLength 指定位数，不能小于1
	 * @param left      是在字符串左侧添加，还是右侧添加。true=左侧，false=右侧
	 * @since 0.0.1
	 */
	static final String pad(String str, char ch, int maxLength, boolean left) {
		if (str == null)
			return "";
		if (maxLength < 1)
			throw new IllegalArgumentException("Argument 'maxLength' can not be less than 1:" + maxLength);
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
	 * @param str       字符串
	 * @param ch        指定的字符
	 * @param maxLength 指定位数，不能小于1
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
	 * @param str       字符串
	 * @param ch        指定的字符
	 * @param maxLength 指定位数，不能小于1
	 * @since 0.0.1
	 */
	public static final String rightPad(String str, char ch, int maxLength) {
		return pad(str, ch, maxLength, false);
	}

	/**
	 * 去除字符串两侧的空白字符<br>
	 * 如果为null则返回空字符串""
	 *
	 * @since 0.0.1
	 */
	public static final String trim(String str) {
		return str == null ? "" : str.trim();
	}

	/**
	 * 将指定字符串的 <code>beginIndex</code> 到 <code>endIndex</code> (不包括 <code>endIndex</code> ) 之间的字符全部替换为字符 <code>ch</code>
	 *
	 * @param str        指定的字符串
	 * @param ch         指定的字符
	 * @param beginIndex 指定的字符串起始索引(可以为负数，表示 <code>beginIndex + str.length()</code> )
	 * @param endIndex   指定的字符串结束索引(可以为负数，表示 <code>endIndex + str.length()</code> )
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
	 * @param endIndex   结束索引
	 * @param length     指定数组、集合或字符串的长度，不能小于0
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
	 * @param str        指定的字符串
	 * @param ch         指定的字符
	 * @param beginIndex 指定的字符串起始索引(可以为负数，表示 <code>beginIndex + str.length()</code> )
	 * @since 0.1.1
	 */
	public static final String replaceChars(String str, char ch, int beginIndex) {
		return replaceChars(str, ch, beginIndex, str.length());
	}

	/**
	 * 将指定字符串的 <code>beginIndex</code> 到 <code>endIndex</code> (不包括 <code>endIndex</code> ) 之间的子字符串全部替换为指定的字符串 <code>replacement</code>
	 *
	 * @param str         指定的字符串
	 * @param replacement 指定的字符串
	 * @param beginIndex  指定的字符串起始索引(可以为负数，表示 <code>beginIndex + str.length()</code> )
	 * @param endIndex    指定的字符串结束索引(可以为负数，表示 <code>endIndex + str.length()</code> )
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
	 * @param str         指定的字符串
	 * @param replacement 指定的字符串
	 * @param beginIndex  指定的字符串起始索引(可以为负数，表示 <code>beginIndex + str.length()</code> )
	 * @since 0.1.1
	 */
	public static final String replaceSubstring(String str, String replacement, int beginIndex) {
		return replaceSubstring(str, replacement, beginIndex, str.length());
	}

	/**
	 * 将字符串从指定字符集编码转换为目标字符集编码
	 *
	 * @param str             指定的字符串
	 * @param originalCharset 原始字符集编码
	 * @param targetCharset   目标字符集编码
	 * @since 0.3.9
	 */
	public static final String convertCharset(String str, Charset originalCharset, Charset targetCharset) {
		return new String(str.getBytes(originalCharset), targetCharset);
	}

	/**
	 * 将指定的URI参数字符串转换为目标字符集编码<br>
	 * 本方法实际上是将字符串从ISO-8859-1编码转换为指定的目标编码
	 *
	 * @param str           指定的URI参数字符串
	 * @param targetCharset 目标字符集编码
	 * @since 0.3.9
	 */
	public static final String convertCharsetForURI(String str, Charset targetCharset) {
		return convertCharset(str, Charsets.ISO_8859_1, targetCharset);
	}

	/**
	 * 颠倒(反转)字符串的字符顺序，并返回颠倒后的字符串<br>
	 * 如果字符串为null，则返回空字符串""
	 *
	 * @param str 指定的字符串
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
	 * @param str       指定的字符串
	 * @param firstChar 指定的单个字符
	 * @since 0.4.2
	 */
	public static final boolean startsWith(final String str, final char firstChar) {
		return str != null && str.length() > 0 && str.charAt(0) == firstChar;
	}

	/**
	 * 判断指定字符串是否以指定的单个字符结尾
	 *
	 * @param str      指定的字符串
	 * @param lastChar 指定的单个字符
	 * @since 0.4.2
	 */
	public static final boolean endsWith(final String str, final char lastChar) {
		return str != null && str.length() > 0 && str.charAt(str.length() - 1) == lastChar;
	}

	/**
	 * 使用转义字符转义字符串中指定的字符
	 *
	 * @param sb           用于字符拼接的 StringBuilder
	 * @param str          需要转义的字符串
	 * @param escapeChar   转义字符
	 * @param escapedChars 需要被转义的字符的数组
	 */
	public static StringBuilder escape(@Nullable StringBuilder sb, final String str, final char escapeChar, final char[] escapedChars) {
		sb = initBuilder(sb, str.length() + 4);
		final int length = str.length();
		for (int i = 0; i < length; i++) {
			char ch = str.charAt(i);
			if (ch == escapeChar || ArrayUtils.contains(escapedChars, ch)) {
				sb.append(escapeChar);
			}
			sb.append(ch);
		}
		return sb;
	}

	/**
	 * 使用转义字符转义字符串中指定的字符
	 *
	 * @param str          需要转义的字符串
	 * @param escapeChar   转义字符
	 * @param escapedChars 需要被转义的字符的数组
	 */
	public static String escape(final String str, final char escapeChar, final char[] escapedChars) {
		return escape(null, str, escapeChar, escapedChars).toString();
	}

	/**
	 * 解除对使用 {@link #escape(StringBuilder, String, char, char[])} 方法转义后的字符串的转义
	 *
	 * @param escapedStr   被转义后的字符串
	 * @param escapeChar   转义字符
	 * @param escapedChars 已经被转义过的字符的数组
	 */
	public static StringBuilder unescape(@Nullable StringBuilder sb, final String escapedStr, final char escapeChar, final char[] escapedChars) {
		final int length = escapedStr.length();
		sb = initBuilder(sb, length);
		for (int i = 0; i < length;) {
			char ch = escapedStr.charAt(i++);
			if (ch == escapeChar && i < length) { // ch == '\'
				char next = escapedStr.charAt(i);
				if (next == escapeChar || ArrayUtils.contains(escapedChars, next)) { // next == ? || next == '\'
					sb.append(next);
					i++;
					continue;
				}
			}
			sb.append(ch);
		}
		return sb;
	}

	/**
	 * 解除对使用 {@link #escape(StringBuilder, String, char, char[])} 方法转义后的字符串的转义
	 *
	 * @param escapedStr   被转义后的字符串
	 * @param escapeChar   转义字符
	 * @param escapedChars 已经被转义过的字符的数组
	 */
	public static String unescape(final String escapedStr, final char escapeChar, final char[] escapedChars) {
		return unescape(null, escapedStr, escapeChar, escapedChars).toString();
	}

	/**
	 * 使用指定的转义字符对用于LIKE语句的字符串进行转义，以防止SQL语句注入
	 *
	 * @param sb                    用于拼接字符的 StringBuilder，如果为 null，则内部会在需要时自动创建
	 * @param likeStr               指定的字符串
	 * @param escapeChar            转义字符
	 * @param appendWildcardAtStart 是否需要在字符串开头添加通配符'%'
	 * @param appendWildcardAtEnd   是否需要在字符串末尾添加通配符'%'
	 * @return 如果 {@code sb} 为 null，且 {@code likeStr } 为空时，返回 null
	 * @since 3.0.0
	 */
	@Nullable
	public static final StringBuilder escapeSQLLike(@Nullable StringBuilder sb, final String likeStr, final char escapeChar, final boolean appendWildcardAtStart, final boolean appendWildcardAtEnd) {
		if (StringUtil.isEmpty(likeStr)) {
			return sb;
		}
		final char[] strChars = likeStr.toCharArray();
		int length = strChars.length + 2;
		if (appendWildcardAtStart || appendWildcardAtEnd) {
			length += 2;
		}
		sb = initBuilder(sb, length);
		final String searchChars = "\\'_%;";
		if (appendWildcardAtStart) {
			sb.append('%');
		}
		for (char ch : strChars) {
			if (searchChars.indexOf(ch, 0) != -1) {
				sb.append(escapeChar);
			}
			sb.append(ch);
		}
		if (appendWildcardAtEnd) {
			sb.append('%');
		}
		return sb;
	}

	/**
	 * 使用指定的转义字符对用于LIKE语句的字符串进行转义，以防止SQL语句注入
	 *
	 * @param likeStr               指定的字符串
	 * @param escapeChar            转义字符
	 * @param appendWildcardAtStart 是否需要在字符串开头添加通配符'%'
	 * @param appendWildcardAtEnd   是否需要在字符串末尾添加通配符'%'
	 * @since 2.9
	 */
	public static final String escapeSQLLike(final String likeStr, final char escapeChar, final boolean appendWildcardAtStart, final boolean appendWildcardAtEnd) {
		final StringBuilder sb = escapeSQLLike(null, likeStr, escapeChar, appendWildcardAtStart, appendWildcardAtEnd);
		if (sb == null || sb.length() == likeStr.length()) { // modified
			return likeStr;
		}
		return sb.toString();
	}

	/**
	 * 使用指定的转义字符对用于LIKE语句的字符串进行转义，以防止SQL语句注入
	 *
	 * @param likeStr               指定的字符串
	 * @param appendWildcardAtStart 是否需要在字符串开头添加通配符'%'
	 * @param appendWildcardAtEnd   是否需要在字符串末尾添加通配符'%'
	 * @since 2.9
	 */
	public static final String escapeSQLLike(final String likeStr, final boolean appendWildcardAtStart, final boolean appendWildcardAtEnd) {
		return escapeSQLLike(likeStr, '\\', appendWildcardAtStart, appendWildcardAtEnd);
	}

	/**
	 * 使用指定的转义字符对用于LIKE语句的字符串进行转义，以防止SQL语句注入
	 *
	 * @param likeStr              指定的字符串
	 * @param escapeChar           转义字符
	 * @param appendWildcardAtBoth 是否需要在字符串两侧都添加通配符'%'
	 * @since 0.3.5
	 */
	public static final String escapeSQLLike(final String likeStr, final char escapeChar, final boolean appendWildcardAtBoth) {
		return escapeSQLLike(likeStr, escapeChar, appendWildcardAtBoth, appendWildcardAtBoth);
	}

	/**
	 * 将指定的用于LIKE语句的字符串转义，以防止SQL语句注入<br>
	 * 该方法默认使用'\'进行转义操作
	 *
	 * @param likeStr            指定的字符串
	 * @param appendLikeWildcard 是否需要在字符串两侧添加通配符'%'
	 * @since 0.3.5
	 */
	public static final String escapeSQLLike(final String likeStr, final boolean appendLikeWildcard) {
		return escapeSQLLike(likeStr, '\\', appendLikeWildcard);
	}

	/**
	 * 将指定的用于LIKE语句的字符串转义，以防止SQL语句注入<br>
	 * 该方法默认使用'\'进行转义操作
	 *
	 * @param likeStr 指定的字符串
	 * @since 0.3.5
	 */
	public static final String escapeSQLLike(final String likeStr) {
		return escapeSQLLike(likeStr, '\\', false);
	}

	/**
	 * 检测指定字符串中是否存在指定的单词
	 *
	 * @param container      待检测的字符串
	 * @param search         指定的单词
	 * @param seperatorChars 单词两侧必须是指定的字符之一或位于字符串 {@code container }的首/尾位置
	 * @param fastMode       是否启用快速模式。快速模式：如果在 {@code container} 中第一次检索到该单词，就直接在此处进行周边字符的匹配测试，并返回测试结果。 <br>
	 *                       哪怕后面还会再次出现该单词，也不再继续向后检查。请参考重载方法 {@link #containsWord(String, String, String)} 方法上的注释
	 * 
	 * @author Ready
	 * @since 2.0.0
	 */
	public static final boolean containsWord(final String container, final String search, final String seperatorChars, final boolean fastMode) {
		if (container == null || search == null)
			return false;
		final int cLength = container.length(), sLength = search.length();
		if (cLength == sLength) {
			return container.equals(search);
		} else if (cLength > sLength) {
			int fromIndex = 0;
			int startIndex;
			// 需要考虑 containsWord("12123,123", "123", ",") 这种特殊情况
			while ((startIndex = container.indexOf(search, fromIndex)) != -1) {
				fromIndex = startIndex + sLength; // as endIndex
				if ((startIndex == 0 || seperatorChars.indexOf(container.charAt(startIndex - 1)) != -1)
						&& (fromIndex == cLength || seperatorChars.indexOf(container.charAt(fromIndex)) != -1)) {
					return true;
				}
				if (fastMode || fromIndex + sLength > cLength) {
					break;
				}
			}
		}
		return false;
	}

	/**
	 * 检测指定字符串中是否存在指定的单词<br>
	 * 该方法采用快速模式，对于类似 {@code containsWord("abc123,123", "123", ",") } 等特殊情况无法保证100%可靠；如果想要保证可靠性，建议使用 {@link #containsWord(String, String, String, boolean) }
	 *
	 * @param container      待检测的字符串
	 * @param searchedWord   指定的单词
	 * @param seperatorChars 单词两侧必须是指定的字符之一或位于字符串 {@code container }的首/尾位置
	 * @see {@link #containsWord(String, String, String, boolean) }
	 * @since 0.4.2
	 */
	public static final boolean containsWord(final String container, final String searchedWord, final String seperatorChars) {
		return containsWord(container, searchedWord, seperatorChars, true);
	}

	/**
	 * 将字符串的首字母大写
	 */
	public static String capitalize(String str) {
		return replaceChar(str, 0, CharCase.UPPER);
	}

	/**
	 * 将字符串的首字母小写
	 */
	public static String decapitalize(String str) {
		return replaceChar(str, 0, CharCase.LOWER);
	}

	/**
	 * 替换字符串中指定位置的字符，并返回替换后的结果
	 * 
	 * @throws IndexOutOfBoundsException 索引越界时会抛出该异常。不过请注意：如果 {@code str} 为 null 时，将直接返回 null，而不会抛出 NPE
	 */
	public static String replaceChar(String str, int charIndex, CharConverter converter) throws IndexOutOfBoundsException {
		if (str == null || str.length() == 0) {
			return str;
		}
		char ch = str.charAt(charIndex);
		char replaced = converter.apply(ch);
		if (ch == replaced) {
			return str;
		}
		final char[] chars = str.toCharArray();
		chars[charIndex] = replaced;
		return new String(chars);
	}
}