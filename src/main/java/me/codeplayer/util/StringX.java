package me.codeplayer.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import me.codeplayer.util.CharConverter.CharCase;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 用于对字符串类型的数据进行常用处理操作的工具类
 *
 * @author Ready
 * @since 2012-10-29
 */
public abstract class StringX {

	/**
	 * 用于在2-16进制之间进行转换的【大写形式】映射字符数组
	 */
	protected static final char[] DIGITS = "0123456789ABCDEF".toCharArray();
	/**
	 * 用于在2-16进制之间进行转换的【小写形式】映射字符数组
	 */
	protected static final char[] digits = "0123456789abcdef".toCharArray();

	/**
	 * 获取指定字符串的Unicode编码，例如：“中国”将返回 <code>"\u4E2D\u56FD"</code><br>
	 * 此方法返回的编码中，字母均采用大写形式，此外，本方法采用 {@link StringBuilder} 作为字符容器
	 *
	 * @param source 指定字符串不能为 null，否则将引发空指针异常
	 * @since 0.0.1
	 */
	public static String unicode(String source) {
		byte[] bytes = source.getBytes(StandardCharsets.UTF_16);// 转为UTF-16字节数组
		int length = bytes.length;
		if (length > 2) {// 由于转换出来的字节数组前两位属于UNICODE固定标记，因此要过滤掉
			int i = 2;
			StringBuilder sb = new StringBuilder((length - i) * 3);
			boolean isOdd = false;
			for (; i < length; i++) {
				//noinspection AssignmentUsedAsCondition
				if (isOdd = !isOdd) {
					sb.append("\\u");
				}
				sb.append(DIGITS[bytes[i] >> 4 & 0xf]);
				sb.append(DIGITS[bytes[i] & 0xf]);
			}
			return sb.toString();
		}
		return source;
	}

	/**
	 * 获取指定字符串的Unicode编码，例如："中国"将返回"\u4E2D\u56FD"<br>
	 * 此方法返回的编码中，字母均采用大写形式，此外，由于编码的长度是已知的，本方法采用char数组作为字符容器，省略了StringBuilder追加时的长度判断以及封装损耗，性能更加优异
	 *
	 * @param source 指定字符串不能为null，否则将引发空指针异常
	 * @since 0.0.1
	 */
	public static String fastUnicode(String source) {
		byte[] bytes = source.getBytes(StandardCharsets.UTF_16); // 转为UTF-16字节数组
		int length = bytes.length;
		if (length > 2) {
			int i = 2;
			char[] chars = new char[(length - i) * 3];
			int index = 0;
			boolean isOdd = false;
			for (; i < length; i++) {
				//noinspection AssignmentUsedAsCondition
				if (isOdd = !isOdd) {
					chars[index++] = '\\';
					chars[index++] = 'u';
				}
				chars[index++] = DIGITS[bytes[i] >> 4 & 0xf];
				chars[index++] = DIGITS[bytes[i] & 0xf];
			}
			return new String(chars);
		}
		return "";
	}

	/**
	 * 获取指定字符序列的字符长度
	 *
	 * @return 对应的字符长度，如果 {@code cs} 为 {@code null}，则返回 0
	 * @since 0.4.2
	 */
	public static int length(@Nullable CharSequence cs) {
		return cs == null ? 0 : cs.length();
	}

	/**
	 * 根据将要追加的多个字符序列获得适当初始容量的 {@code StringBuilder}
	 *
	 * @param extraCapacity 除指定的字符序列外应额外预留的字符容量
	 * @since 0.4.2
	 */
	public static StringBuilder getBuilder(final int extraCapacity, @Nullable CharSequence s1, @Nullable CharSequence s2, @Nullable CharSequence s3, @Nullable CharSequence s4) {
		final int size1 = length(s1), size2 = length(s2), size3 = length(s3), size4 = length(s4);
		final StringBuilder sb = new StringBuilder(extraCapacity + size1 + size2 + size3 + size4);
		if (size1 > 0) {
			sb.append(s1);
		}
		if (size2 > 0) {
			sb.append(s2);
		}
		if (size3 > 0) {
			sb.append(s3);
		}
		if (size4 > 0) {
			sb.append(s4);
		}
		return sb;
	}

	/**
	 * 根据将要追加的多个字符序列获得适当初始容量的 {@code StringBuilder}
	 *
	 * @param extraCapacity 除指定的字符序列外应额外预留的字符容量
	 * @since 0.4.2
	 */
	public static StringBuilder getBuilder(final int extraCapacity, @Nullable CharSequence s1, @Nullable CharSequence s2, @Nullable CharSequence s3) {
		return getBuilder(extraCapacity, s1, s2, s3, null);
	}

	/**
	 * 根据将要追加的多个字符序列获得适当初始容量的 {@code StringBuilder}
	 *
	 * @param extraCapacity 除指定的字符序列外应额外预留的字符容量
	 * @since 0.4.2
	 */
	public static StringBuilder getBuilder(final int extraCapacity, @Nullable CharSequence s1, @Nullable CharSequence s2) {
		final int size1 = length(s1), size2 = length(s2);
		final StringBuilder sb = new StringBuilder(extraCapacity + size1 + size2);
		if (size1 > 0) {
			sb.append(s1);
		}
		if (size2 > 0) {
			sb.append(s2);
		}
		return sb;
	}

	/**
	 * 根据将要追加的多个字符序列获得适当初始容量的 {@code StringBuilder}
	 *
	 * @param extraCapacity 除指定的字符序列外应额外预留的字符容量
	 * @since 0.4.2
	 */
	public static StringBuilder getBuilder(final int extraCapacity, @Nullable CharSequence s1) {
		return getBuilder(extraCapacity, s1, null);
	}

	/**
	 * 拼接两个字符串（忽略其中为 null 的参数）
	 *
	 * @since 3.0.0
	 */
	public static String concat(@Nullable String a, @Nullable String b) {
		if (a == null || a.isEmpty()) {
			return b == null ? "" : b;
		}
		if (b == null || b.isEmpty()) {
			return a;
		}
		return a.concat(b);
	}

	/**
	 * 拼接多个字符串（忽略其中为 null 的参数）
	 *
	 * @since 3.0.0
	 */
	public static String concat(@Nullable String a, @Nullable String b, @Nullable String c) {
		int aSize = length(a), bSize, cSize;
		if (aSize == 0) {
			return concat(b, c);
		} else if ((bSize = length(b)) == 0) {
			return concat(a, c);
		} else if ((cSize = length(c)) == 0) {
			return concat(a, b);
		}
		//noinspection StringBufferReplaceableByString
		return new StringBuilder(aSize + bSize + cSize).append(a).append(b).append(c).toString();
	}

	/**
	 * 拼接多个字符串（忽略其中为 null 的参数）
	 *
	 * @since 3.0.0
	 */
	public static StringBuilder append(@Nullable StringBuilder sb, String... parts) {
		int size = 0;
		for (String str : parts) {
			size += length(str);
		}
		sb = initBuilder(sb, size);
		for (String str : parts) {
			if (str != null) {
				sb.append(str);
			}
		}
		return sb;
	}

	/**
	 * 拼接多个字符串（忽略其中为 null 的参数）
	 *
	 * @since 3.0.0
	 */
	public static String concats(String... parts) {
		return append(null, parts).toString();
	}

	/**
	 * 初始化一个还可以添加 {@code appendLength} 个字符的 StringBuilder
	 *
	 * @param sb 如果为null，内部会自动创建
	 * @param appendLength 还需要扩展的字符数
	 * @since 3.0.0
	 */
	public static StringBuilder initBuilder(@Nullable StringBuilder sb, int appendLength) {
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
	public static boolean isEmpty(@Nullable CharSequence cs) {
		return cs == null || cs.length() == 0; // 后面的表达式相当于"".equals(cs)，但比其性能稍好
	}

	/**
	 * 判断指定的字符串是否不为空<br>
	 * 如果指定字符串不为null、空字符串，则返回true，否则返回false<br>
	 * <b>注意：</b>本方法不会去除字符串两边的空格，如果需要对字符串进行去除两边空格后的判断，请使用 {@link #notBlank(CharSequence)} 方法
	 *
	 * @since 0.0.1
	 */
	public static boolean notEmpty(@Nullable CharSequence cs) {
		return cs != null && cs.length() > 0;
	}

	/**
	 * 判断指定的字符序列数组是否存在不为空的元素<br>
	 * 如果数组中存在不为null、空字符串的元素，则返回true，否则返回false<br>
	 *
	 * @since 1.0.2
	 */
	public static boolean isAnyNotEmpty(@Nullable CharSequence... css) {
		return css != null && ArrayX.matchAny(StringX::notEmpty, css);
	}

	/**
	 * 判断指定的对象是否为空<br>
	 * 如果对象(或其 toString() 返回值)为null、空字符串，则返回true<br>
	 * <b>注意：</b>本方法不会去除字符串两边的空格，如果需要对字符串进行去除两边空格后的判断，请使用isBlank(Object obj)方法
	 *
	 * @param obj 指定的对象
	 * @since 0.0.1
	 */
	public static boolean isEmpty(@Nullable Object obj) {
		return obj == null || obj.toString().isEmpty(); // 后面的表达式相当于"".equals(str)，但比其性能稍好
	}

	/**
	 * 判断指定的对象是否不为空<br>
	 * 如果对象(或其 toString() 返回值)不为null、空字符串，则返回true，否则返回false<br>
	 * <b>注意：</b>本方法不会去除字符串两边的空格，如果需要对字符串进行去除两边空格后的判断，请使用{@link #isBlank(Object obj)}方法
	 *
	 * @param obj 指定的对象
	 * @since 0.0.1
	 */
	public static boolean notEmpty(@Nullable Object obj) {
		return obj != null && !obj.toString().isEmpty();
	}

	/**
	 * 判断指定数组中是否存在为空的值(null、空字符串),如果是将返回true<br>
	 * 本方法接受多个参数，如果其中有任意一个为空，就返回true 如果指定的key不存在也返回true
	 *
	 * @param values 指定的数组
	 * @see #isEmpty(Object)
	 */
	public static boolean hasEmpty(Object... values) {
		return ArrayX.matchAny(StringX::isEmpty, values);
	}

	/**
	 * 判断指定的字符串是否为空<br>
	 * 如果字符串为null、空字符串、空格字符串，则返回true<br>
	 * <b>注意：</b>本方法会先去除字符串两边的空格，再判断
	 *
	 * @since 0.0.1
	 */
	public static boolean isBlank(CharSequence str) {
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
	public static boolean notBlank(CharSequence cs) {
		return !isBlank(cs);
	}

	/**
	 * 判断指定的对象是否为空<br>
	 * 如果对象(或其 toString() 返回值)为null、空字符串、空格字符串，则返回true<br>
	 * <b>注意：</b>本方法会先去除字符串两边的空格，再判断
	 *
	 * @param obj 指定的对象
	 * @since 0.0.1
	 */
	public static boolean isBlank(Object obj) {
		return obj == null || isBlank(obj.toString());
	}

	/**
	 * 判断指定的对象是否为空<br>
	 * 如果对象(或其 toString() 返回值)不为null、空字符串、空格字符串，则返回true，否则返回false<br>
	 * <b>注意：</b>本方法会先去除字符串两边的空格，再判断
	 *
	 * @param obj 指定的对象
	 * @since 0.0.1
	 */
	public static boolean notBlank(Object obj) {
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
	public static boolean hasBlank(Object... values) {
		return ArrayX.matchAny(StringX::isBlank, values);
	}

	/**
	 * 以String的形式返回对象值，如果对象为null，则返回""
	 *
	 * @param obj 指定的对象
	 * @since 0.0.1
	 */
	public static String toString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	/**
	 * 安全地执行指定的字符串懒加载表达式，并返回对应的字符串
	 */
	@Nullable
	public static String nullSafeGet(@Nullable Supplier<? extends CharSequence> msgSupplier) {
		return msgSupplier == null ? null : nullSafeGet(msgSupplier.get());
	}

	/**
	 * 安全地获取指定字符序列中的字符串
	 *
	 * @param cs 如果该参数为 null，则返回 null
	 */
	@Nullable
	public static String nullSafeGet(@Nullable CharSequence cs) {
		return cs == null ? null : cs.toString();
	}

	/**
	 * 以String的形式返回对象值，如果对象为null，则返回""
	 *
	 * @param obj 指定的对象
	 * @since 0.0.1
	 */
	public static String toString(Object obj, String defaultValIfNull) {
		return obj == null ? defaultValIfNull : obj.toString();
	}

	/**
	 * 以去除两边空格的String形式返回对象值，如果对象为null，则返回""
	 *
	 * @param obj 指定的对象
	 * @since 0.0.1
	 */
	public static String trim(Object obj) {
		return obj == null ? "" : obj.toString().trim();
	}

	/**
	 * 如果指定字符串超过限制长度<code>maxLength</code>,则返回限制长度前面的部分字符串<br>
	 * 如果指定字符串==null，则返回空字符串<br>
	 * 如果字符串超出指定长度，则返回maxLength前面的部分，并在末尾加上后缀<code>suffix</code>
	 *
	 * @param str 指定的字符串
	 * @param maxLength 最大限制长度
	 * @param suffix 超出长度时添加的指定后缀,如果不需要，可以为null
	 * @since 0.0.1
	 */
	@Nonnull
	public static String limitChars(@Nullable String str, int maxLength, String suffix) {
		if (str == null) {
			return "";
		}
		if (str.length() <= maxLength) {
			return str;
		}
		str = str.substring(0, maxLength);
		return isEmpty(suffix) ? str : str.concat(suffix);
	}

	/**
	 * 如果指定字符串超过限制长度<code>maxLength</code>,则返回限制长度前面的部分字符串<br>
	 * 如果指定字符串==null，则返回空字符串<br>
	 * 如果字符串超出指定长度，则返回maxLength前面的部分，并在末尾加上后缀“...”
	 *
	 * @param str 指定的字符串
	 * @param maxLength 最大限制长度
	 * @since 0.0.1
	 */
	@Nonnull
	public static String limitChars(@Nullable String str, int maxLength) {
		return limitChars(str, maxLength, "...");
	}

	/**
	 * 如果字符串不足指定位数，则前面补0，直到指定位数<br>
	 * 如果字符串=null，则返回空字符串""<br>
	 * 如果字符串位数大于指定位数，则返回原字符串
	 *
	 * @param str 字符串
	 * @param minLength 期望的最小字符数，不能小于1
	 * @since 0.0.1
	 */
	public static String zeroFill(String str, int minLength) {
		return pad(str, '0', minLength, true);
	}

	/**
	 * 如果字符串不足指定位数，则在其左侧或右侧补充指定的字符，直到指定位数<br>
	 * 如果字符串 = null，则返回空字符串""<br>
	 * 如果字符串位数大于指定位数，则返回原字符串
	 *
	 * @param str 指定的字符串
	 * @param ch 指定的字符
	 * @param minLength 期望的最小字符数，不能小于1
	 * @param leftOrRight 是在字符串左侧添加，还是右侧添加。true=左侧，false=右侧
	 * @since 0.0.1
	 */
	static String pad(@Nullable String str, char ch, int minLength, boolean leftOrRight) {
		if (str == null) {
			return "";
		}
		if (minLength < 1) {
			throw new IllegalArgumentException("minLength can not be less than 1:" + minLength);
		}
		int length = str.length();
		if (minLength <= length) {
			return str;
		}
		final StringBuilder sb = new StringBuilder(minLength);
		if (leftOrRight) {
			for (int i = length; i < minLength; i++) {
				sb.append(ch);
			}
			sb.append(str, 0, length);
		} else {
			sb.append(str, 0, length);
			do {
				sb.append(ch);
			} while (++length < minLength);
		}
		return sb.toString();
	}

	/**
	 * 返回整数的字符数
	 *
	 * @see Long#stringSize(long)
	 */
	public static int stringSize(long x) {
		int d = 1;
		if (x >= 0) {
			d = 0;
			x = -x;
		}
		long p = -10;
		for (int i = 1; i < 19; i++) {
			if (x > p) {
				return i + d;
			}
			p = 10 * p;
		}
		return 19 + d;
	}

	/**
	 * 将指定数值追加到指定不足 {@code StringBuilder} 中指定位数，如果指定数值的位数不足，则在前面补0，直到达到期望的最小位数为止。
	 *
	 * @param sb 用于拼接字符串的 {@code StringBuilder} 对象，如果为 null 则内部自动新建
	 * @param val 数值
	 * @param minLength 期望的最小位数，不应小于1
	 * @since 3.9.0
	 */
	public static StringBuilder zeroFill(@Nullable StringBuilder sb, long val, int minLength) {
		final int size = stringSize(val), expected = Math.max(minLength, size);
		if (sb == null) {
			// 为常规的 2、4 等位数进行专项优化
			sb = new StringBuilder(expected);
		} else {
			sb.ensureCapacity(sb.length() + expected);
		}

		for (int i = minLength - size; i > 0; i--) {
			sb.append('0');
		}

		sb.append(val);
		return sb;
	}

	/**
	 * 将指定 long 数值转为字符串，如果位数不足，则在前面补0，直到满足期望的最小位数{@code minLength}。
	 *
	 * @param val 数值
	 * @param minLength 期望的最小位数，不应小于1
	 * @since 3.9.0
	 */
	public static String zeroFill(long val, int minLength) {
		final int length = stringSize(val);
		if (length >= minLength) {
			return Long.toString(val);
		}
		final StringBuilder sb = new StringBuilder(minLength);
		for (int i = minLength - length; i > 0; i--) {
			sb.append('0');
		}
		return sb.append(val).toString();
	}

	/**
	 * 将指定 int 数值转为字符串。如果位数不足，则在前面补0，直到满足期望的最小位数{@code minLength}。
	 *
	 * @param val 数值
	 * @param minLength 期望的最小位数，不应小于1
	 * @since 3.9.0
	 */
	public static String zeroFill(int val, int minLength) {
		return zeroFill((long) val, minLength);
	}

	/**
	 * 如果字符串不足指定位数，则在前面补充指定的字符，直到期望的最小位数<br>
	 * 如果字符串=null，则返回空字符串""<br>
	 * 如果字符串位数大于指定位数，则返回原字符串
	 *
	 * @param str 字符串
	 * @param ch 指定的字符
	 * @param minLength 期望的最小位数，不能小于1
	 * @since 0.0.1
	 */
	public static String leftPad(String str, char ch, int minLength) {
		return pad(str, ch, minLength, true);
	}

	/**
	 * 如果字符串不足指定位数，则在后面补充指定的字符，直到期望的最小位数<br>
	 * 如果字符串=null，则返回空字符串""<br>
	 * 如果字符串位数大于指定位数，则返回原字符串
	 *
	 * @param str 字符串
	 * @param ch 指定的字符
	 * @param maxLength 期望的最小位数，不能小于1
	 * @since 0.0.1
	 */
	public static String rightPad(String str, char ch, int maxLength) {
		return pad(str, ch, maxLength, false);
	}

	/**
	 * 去除字符串两侧的空白字符<br>
	 * 如果为null则返回空字符串""
	 *
	 * @since 0.0.1
	 */
	public static String trim(String str) {
		return str == null ? "" : str.trim();
	}

	/**
	 * 去除字符串中所有的空白字符并返回处理后的字符串<br>
	 * 如果为null则返回空字符串 ""
	 *
	 * @param str 指定的字符串
	 * @since 3.8.0
	 */
	public static String trimAll(String str) {
		if (isEmpty(str)) {
			return "";
		}
		final int len = str.length();
		char[] chars = null;
		int count = 0;
		for (int i = 0; i < len; i++) {
			char ch = str.charAt(i);
			if (Character.isWhitespace(ch)) {
				if (chars == null) {
					chars = new char[len - 1];
					str.getChars(0, count = i, chars, 0);
				}
			} else if (chars != null) {
				chars[count++] = ch;
			}
		}
		if (chars == null) {
			return str;
		}
		if (count == 0) {
			return "";
		}
		return new String(chars, 0, count);
	}

	/**
	 * 将指定字符串的 <code>beginIndex</code> 到 <code>endIndex</code> (不包括 <code>endIndex</code> ) 之间的字符全部替换为字符 <code>ch</code>
	 *
	 * @param str 指定的字符串
	 * @param ch 指定的字符
	 * @param beginIndex 指定的字符串起始索引(可以为负数，表示 <code>beginIndex + str.length()</code> )
	 * @param endIndex 指定的字符串结束索引(可以为负数，表示 <code>endIndex + str.length()</code> )
	 * @since 0.1.1
	 */
	public static String replaceChars(@Nullable String str, char ch, int beginIndex, int endIndex) {
		final int length = length(str);
		int[] range = ensureRangeSafe(beginIndex, endIndex, length);
		if (range == null) {
			return str;
		}
		final CharReplacer chars = CharReplacer.of(str, ch >>> 8 == 0);
		for (int i = range[0]; i < range[1]; i++) {
			chars.setCharAt(i, ch);
		}
		return chars.toString();
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
	public static int[] ensureRangeSafe(int beginIndex, int endIndex, final int length) {
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
	 * @since 0.1.1
	 */
	public static String replaceChars(@Nullable String str, char ch, int beginIndex) {
		return str == null ? null : replaceChars(str, ch, beginIndex, str.length());
	}

	/**
	 * 将指定字符串的 <code>beginIndex</code> 到 <code>endIndex</code> (不包括 <code>endIndex</code> ) 之间的子字符串全部替换为指定的字符串 <code>replacement</code>
	 *
	 * @param str 指定的字符串
	 * @param replacement 指定的字符串
	 * @param beginIndex 指定的字符串起始索引(可以为负数，表示 <code>beginIndex + str.length()</code> )
	 * @param endIndex 指定的字符串结束索引(可以为负数，表示 <code>endIndex + str.length()</code> )
	 * @since 0.1.1
	 */
	public static String replaceSubstring(@Nullable String str, String replacement, int beginIndex, int endIndex) {
		if (replacement == null || replacement.isEmpty()) {
			return str;
		}
		final int length = length(str);
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
	 * @since 0.1.1
	 */
	public static String replaceSubstring(@Nullable String str, String replacement, int beginIndex) {
		return str == null ? null : replaceSubstring(str, replacement, beginIndex, str.length());
	}

	/**
	 * 将字符串从指定字符集编码转换为目标字符集编码
	 *
	 * @param str 指定的字符串
	 * @param originalCharset 原始字符集编码
	 * @param targetCharset 目标字符集编码
	 * @since 0.3.9
	 */
	public static String convertCharset(String str, Charset originalCharset, Charset targetCharset) {
		return new String(str.getBytes(originalCharset), targetCharset);
	}

	/**
	 * 将指定的URI参数字符串转换为目标字符集编码<br>
	 * 本方法实际上是将字符串从ISO-8859-1编码转换为指定的目标编码
	 *
	 * @param str 指定的URI参数字符串
	 * @param targetCharset 目标字符集编码
	 * @since 0.3.9
	 */
	public static String convertCharsetForURI(String str, Charset targetCharset) {
		return convertCharset(str, StandardCharsets.ISO_8859_1, targetCharset);
	}

	/**
	 * 颠倒(反转)字符串的字符顺序，并返回颠倒后的字符串<br>
	 * 如果字符串为null，则返回空字符串""
	 *
	 * @param str 指定的字符串
	 * @since 0.0.1
	 */
	public static String reverse(CharSequence str) {
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
	 * @since 0.4.2
	 */
	public static boolean startsWith(final String str, final char firstChar) {
		return str != null && !str.isEmpty() && str.charAt(0) == firstChar;
	}

	/**
	 * 判断指定字符串是否以指定的单个字符结尾
	 *
	 * @param str 指定的字符串
	 * @param lastChar 指定的单个字符
	 * @since 0.4.2
	 */
	public static boolean endsWith(final String str, final char lastChar) {
		return str != null && !str.isEmpty() && str.charAt(str.length() - 1) == lastChar;
	}

	/**
	 * 使用转义字符转义字符串中指定的字符
	 *
	 * @param sb 用于字符拼接的 StringBuilder
	 * @param str 需要转义的字符串
	 * @param escapeChar 转义字符
	 * @param escapedChars 需要被转义的字符的数组
	 */
	public static StringBuilder escape(@Nullable StringBuilder sb, final String str, final char escapeChar, final char[] escapedChars) {
		final int length = length(str);
		sb = initBuilder(sb, length + 4);
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
	 * @param str 需要转义的字符串
	 * @param escapeChar 转义字符
	 * @param escapedChars 需要被转义的字符的数组
	 */
	public static String escape(final String str, final char escapeChar, final char[] escapedChars) {
		return escape(null, str, escapeChar, escapedChars).toString();
	}

	/**
	 * 解除对使用 {@link #escape(StringBuilder, String, char, char[])} 方法转义后的字符串的转义
	 *
	 * @param escapedStr 被转义后的字符串
	 * @param escapeChar 转义字符
	 * @param escapedChars 已经被转义过的字符的数组
	 */
	public static StringBuilder unescape(@Nullable StringBuilder sb, final String escapedStr, final char escapeChar, final char[] escapedChars) {
		final int length = length(escapedStr);
		sb = initBuilder(sb, length);
		for (int i = 0; i < length; ) {
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
	 * @param escapedStr 被转义后的字符串
	 * @param escapeChar 转义字符
	 * @param escapedChars 已经被转义过的字符的数组
	 */
	public static String unescape(final String escapedStr, final char escapeChar, final char[] escapedChars) {
		return unescape(null, escapedStr, escapeChar, escapedChars).toString();
	}

	/**
	 * 使用指定的转义字符对用于LIKE语句的字符串进行转义，以防止SQL语句注入
	 *
	 * @param sb 用于拼接字符的 StringBuilder，如果为 null，则内部会在需要时自动创建
	 * @param likeStr 指定的字符串
	 * @param escapeChar 转义字符
	 * @param appendWildcardAtStart 是否需要在字符串开头添加通配符'%'
	 * @param appendWildcardAtEnd 是否需要在字符串末尾添加通配符'%'
	 * @return 如果 {@code sb} 为 null，且 {@code likeStr } 为空时，返回 null
	 * @since 3.0.0
	 */
	@Nullable
	public static StringBuilder escapeSQLLike(@Nullable StringBuilder sb, @Nullable final String likeStr, final char escapeChar, final boolean appendWildcardAtStart, final boolean appendWildcardAtEnd) {
		if (isEmpty(likeStr)) {
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
	 * @param likeStr 指定的字符串
	 * @param escapeChar 转义字符
	 * @param appendWildcardAtStart 是否需要在字符串开头添加通配符'%'
	 * @param appendWildcardAtEnd 是否需要在字符串末尾添加通配符'%'
	 * @since 2.9
	 */
	public static String escapeSQLLike(@Nullable final String likeStr, final char escapeChar, final boolean appendWildcardAtStart, final boolean appendWildcardAtEnd) {
		final StringBuilder sb = escapeSQLLike(null, likeStr, escapeChar, appendWildcardAtStart, appendWildcardAtEnd);
		if (sb == null || sb.length() == length(likeStr)) { // modified
			return likeStr;
		}
		return sb.toString();
	}

	/**
	 * 使用指定的转义字符对用于LIKE语句的字符串进行转义，以防止SQL语句注入
	 *
	 * @param likeStr 指定的字符串
	 * @param appendWildcardAtStart 是否需要在字符串开头添加通配符'%'
	 * @param appendWildcardAtEnd 是否需要在字符串末尾添加通配符'%'
	 * @since 2.9
	 */
	public static String escapeSQLLike(@Nullable final String likeStr, final boolean appendWildcardAtStart, final boolean appendWildcardAtEnd) {
		return escapeSQLLike(likeStr, '\\', appendWildcardAtStart, appendWildcardAtEnd);
	}

	/**
	 * 使用指定的转义字符对用于LIKE语句的字符串进行转义，以防止SQL语句注入
	 *
	 * @param likeStr 指定的字符串
	 * @param escapeChar 转义字符
	 * @param appendWildcardAtBoth 是否需要在字符串两侧都添加通配符'%'
	 * @since 0.3.5
	 */
	public static String escapeSQLLike(@Nullable final String likeStr, final char escapeChar, final boolean appendWildcardAtBoth) {
		return escapeSQLLike(likeStr, escapeChar, appendWildcardAtBoth, appendWildcardAtBoth);
	}

	/**
	 * 将指定的用于LIKE语句的字符串转义，以防止SQL语句注入<br>
	 * 该方法默认使用'\'进行转义操作
	 *
	 * @param likeStr 指定的字符串
	 * @param appendLikeWildcard 是否需要在字符串两侧添加通配符'%'
	 * @since 0.3.5
	 */
	public static String escapeSQLLike(@Nullable final String likeStr, final boolean appendLikeWildcard) {
		return escapeSQLLike(likeStr, '\\', appendLikeWildcard);
	}

	/**
	 * 将指定的用于LIKE语句的字符串转义，以防止SQL语句注入<br>
	 * 该方法默认使用'\'进行转义操作
	 *
	 * @param likeStr 指定的字符串
	 * @since 0.3.5
	 */
	public static String escapeSQLLike(@Nullable final String likeStr) {
		return escapeSQLLike(likeStr, '\\', false);
	}

	/**
	 * 检测指定字符串中是否存在指定的单词
	 *
	 * @param container 待检测的字符串
	 * @param search 指定的单词
	 * @param separatorChars 单词两侧必须是指定的字符之一或位于字符串 {@code container }的首/尾位置
	 * @param fastFail 是否启用快速失败模式。快速模式：如果在 {@code container} 中第一次检索到该单词，就直接在此处进行周边字符的匹配测试，并立即返回测试结果。 <br>
	 * 在快速失败模式下，对于类似 {@code containsWord("abc123,123", "123", ",") } 的特殊情况将返回 <code>false</code>。哪怕后面还会再次出现该单词，也不再继续向后检查。
	 * @author Ready
	 * @since 2.0.0
	 */
	public static boolean containsWord(@Nullable final String container, @Nullable final String search, final String separatorChars, final boolean fastFail) {
		if (container == null || search == null) {
			return false;
		}
		final int cLength = container.length(), sLength = search.length();
		if (cLength == sLength) {
			return container.equals(search);
		} else if (cLength > sLength) {
			int fromIndex = 0;
			int startIndex;
			// 需要考虑 containsWord("12123,123", "123", ",") 这种特殊情况
			while ((startIndex = container.indexOf(search, fromIndex)) != -1) {
				fromIndex = startIndex + sLength; // as endIndex
				if ((startIndex == 0 || separatorChars.indexOf(container.charAt(startIndex - 1)) != -1)
						&& (fromIndex == cLength || separatorChars.indexOf(container.charAt(fromIndex)) != -1)) {
					return true;
				}
				if (fastFail || fromIndex + sLength > cLength) {
					break;
				}
			}
		}
		return false;
	}

	/**
	 * 检测指定字符串中是否存在指定的单词
	 *
	 * @param container 待检测的字符串
	 * @param searchedWord 指定的单词
	 * @param separatorChars 单词两侧必须是指定的字符之一或位于字符串 {@code container }的首/尾位置
	 * @see #containsWord(String, String, String, boolean)
	 * @since 0.4.2
	 */
	public static boolean containsWord(@Nullable final String container, final String searchedWord, final String separatorChars) {
		return containsWord(container, searchedWord, separatorChars, false);
	}

	/**
	 * 将字符串的首字母大写
	 */
	public static String capitalize(@Nullable String str) {
		return replaceChar(str, 0, CharCase.UPPER);
	}

	/**
	 * 将字符串的首字母小写
	 */
	public static String decapitalize(@Nullable String str) {
		return replaceChar(str, 0, CharCase.LOWER);
	}

	/**
	 * 替换字符串中指定位置的字符，并返回替换后的结果
	 *
	 * @throws IndexOutOfBoundsException 索引越界时会抛出该异常。不过请注意：如果 {@code str} 为 null 时，将直接返回 null，而不会抛出 NPE
	 */
	public static String replaceChar(@Nullable String str, int charIndex, CharConverter converter) throws IndexOutOfBoundsException {
		if (str == null || str.isEmpty()) {
			return str;
		}
		char ch = str.charAt(charIndex);
		char replaced = converter.apply(ch);
		if (ch == replaced) {
			return str;
		}
		CharReplacer chars = CharReplacer.of(str, replaced >>> 8 == 0);
		chars.setCharAt(charIndex, replaced);
		return chars.toString();
	}

	/**
	 * 将集合的指定属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符
	 */
	public static <E> StringBuilder joinAppend(@Nullable StringBuilder sb, Collection<E> items, BiConsumer<StringBuilder, E> itemAppender, String delimiter) {
		return joinAppend(sb, items, itemAppender, delimiter, 0);
	}

	/**
	 * 将集合的指定属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符
	 */
	public static <E> StringBuilder joinAppend(@Nullable StringBuilder sb, Collection<E> items, BiConsumer<StringBuilder, E> itemAppender, String delimiter, int itemLength) {
		final int size = X.size(items);
		return size == 0 ? sb : doJoinAppend(sb, items, size, itemAppender, delimiter, itemLength);
	}

	/**
	 * 将集合的指定属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符
	 * @param itemLength 集合中每个元素的预期长度，如果不大于 0，则内部会根据第一个元素的长度来确定 itemLength 的值
	 */
	static <E> StringBuilder doJoinAppend(@Nullable StringBuilder sb, Collection<E> items, final int size, BiConsumer<StringBuilder, E> itemAppender, String delimiter, int itemLength) {
		final Iterator<E> it = items.iterator();
		final E val = it.next();
		if (itemLength <= 0) {
			itemLength = suggestLength(val);
		}
		sb = initBuilder(sb, size * (itemLength + delimiter.length()) + 4);
		itemAppender.accept(sb, val);
		while (it.hasNext()) {
			itemAppender.accept(sb.append(delimiter), it.next());
		}
		return sb;
	}

	/**
	 * 将集合的指定属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符
	 * @param itemLength 集合中每个元素的预期长度，如果不大于 0，则内部会根据第一个元素的长度来确定 itemLength 的值
	 */
	static <E> StringBuilder doJoinAppend(@Nullable StringBuilder sb, E[] items, final int size, BiConsumer<StringBuilder, E> itemAppender, String delimiter, int itemLength) {
		if (itemLength <= 0) {
			itemLength = suggestLength(items[0]);
		}
		sb = initBuilder(sb, size * (itemLength + delimiter.length()) + 4);
		doJoin(sb, items, size, itemAppender, delimiter);
		return sb;
	}

	static <E> void doJoin(StringBuilder sb, E[] items, int size, BiConsumer<StringBuilder, E> itemAppender, String delimiter) {
		itemAppender.accept(sb, items[0]);
		for (int i = 1; i < size; i++) {
			itemAppender.accept(sb.append(delimiter), items[i]);
		}
	}

	/**
	 * 根据给定值建议一个合适的字符长度。
	 *
	 * @param val 要评估的对象，可以是数字或字符序列类型。
	 * 如果为数字类型且数值较大，则根据其实际位数返回长度；
	 * 如果为字符序列类型，则返回其实际长度与默认长度的最大值。
	 * @return 返回建议的长度值。
	 */
	static int suggestLength(Object val) {
		int itemLength = 6; // default expect length
		if (val instanceof Number) {
			long longValue = ((Number) val).longValue();
			if (longValue >= 100_0000) {
				itemLength = stringSize(longValue);
			}
		} else if (val instanceof CharSequence) {
			itemLength = Math.max(((CharSequence) val).length(), itemLength);
		}
		return itemLength;
	}

	/**
	 * 将集合的指定属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符
	 */
	public static <E> String joinAppend(@Nullable Collection<E> items, BiConsumer<StringBuilder, E> itemAppender, String delimiter, int itemLength) {
		final int size = items == null ? 0 : items.size();
		return size == 0 ? "" : doJoinAppend(null, items, size, itemAppender, delimiter, itemLength).toString();
	}

	/**
	 * 将集合的指定属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符
	 */
	public static <E> String joinAppend(@Nullable Collection<E> items, BiConsumer<StringBuilder, E> itemAppender, String delimiter) {
		return joinAppend(items, itemAppender, delimiter, 0);
	}

	/**
	 * 将集合的指定属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符
	 */
	public static <E> String joins(@Nullable Collection<E> c, String delimiter, int itemLength) {
		final int size = c == null ? 0 : c.size();
		switch (size) {
			case 0:
				return "";
			case 1:
				return c.iterator().next().toString();
			default:
				return doJoinAppend(null, c, size, StringX::append, delimiter, itemLength).toString();
		}
	}

	/**
	 * 将集合的指定属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符
	 */
	public static <E> String joins(@Nullable Collection<E> c, String delimiter) {
		return joins(c, delimiter, 0);
	}

	/**
	 * 将集合的指定属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符
	 */
	public static <E> StringBuilder joinAppend(@Nullable StringBuilder sb, E[] items, BiConsumer<StringBuilder, E> itemAppender, String delimiter, int itemLength) {
		final int size = X.size(items);
		return size == 0 ? sb : doJoinAppend(sb, items, size, itemAppender, delimiter, itemLength);
	}

	/**
	 * 将集合的指定属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符
	 */
	public static <E> StringBuilder joinAppend(@Nullable StringBuilder sb, E[] items, String delimiter, int itemLength) {
		final int size = X.size(items);
		return size == 0 ? sb : doJoinAppend(sb, items, size, StringX::append, delimiter, itemLength);
	}

	/**
	 * 将集合的指定属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符
	 */
	public static <E> StringBuilder joinAppend(@Nullable StringBuilder sb, E[] items, String delimiter) {
		return joinAppend(sb, items, delimiter, 0);
	}

	/**
	 * 将集合的指定属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符
	 */
	public static <E> String join(@Nullable E[] array, Function<? super E, Object> getter, String delimiter, int itemLength) {
		final int size = array == null ? 0 : array.length;
		switch (size) {
			case 0:
				return "";
			case 1:
				return String.valueOf(getter.apply(array[0]));
			default:
				return doJoinAppend(null, array, size, (sb, t) -> append(sb, getter.apply(t)), delimiter, itemLength).toString();
		}
	}

	/**
	 * 将集合的指定属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符
	 */
	public static <E> String join(@Nullable E[] array, Function<? super E, Object> getter, String delimiter) {
		return join(array, getter, delimiter, 0);
	}

	/**
	 * 将集合的指定属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符
	 */
	@SuppressWarnings("unchecked")
	public static <E> String join(@Nullable E[] array, String delimiter) {
		return join(array, FunctionX.identity, delimiter, 0);
	}

	/**
	 * 将 整数集合 拼接为字符串
	 * <p><b>注意</b>：目前仅支持【整型】（例如：{@link Long }、{@link Integer}、{@link Short}、{@link Byte}）集合
	 *
	 * @param numbers 整型集合，<b>并且元素不能为 null</b>
	 * @param delimiter 分隔符，可以为 ""，但不能为 null
	 * @param itemLength 集合中每个元素的预期长度，如果不大于 0，则内部会根据第一个元素的长度来确定 itemLength 的值
	 */
	public static String join(@Nullable Collection<? extends Number> numbers, String delimiter, int itemLength) {
		final int size = numbers == null ? 0 : numbers.size();
		switch (size) {
			case 0:
				return "";
			case 1:
				return numbers.iterator().next().toString();
			default:
				return doJoinAppend(null, numbers, size, (sb, t) -> sb.append(t.longValue()), delimiter, itemLength).toString();
		}
	}

	/**
	 * 将 整数集合 拼接为字符串
	 * <p><b>注意</b>：目前仅支持【整型】（例如：{@link Long }、{@link Integer}、{@link Short}、{@link Byte}）集合
	 *
	 * @param numbers 整型集合，集合可以为 null，但如果存在元素，<b>其中的元素不能为 null</b>
	 * @param delimiter 分隔符，可以为 ""，但不能为 null
	 */
	public static String join(@Nullable Collection<? extends Number> numbers, String delimiter) {
		return join(numbers, delimiter, 0);
	}

	/**
	 * 将集合的指定属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符，可以为 ""，但不能为 null
	 * @param itemLength 集合中每个元素的预期长度，如果不大于 0，则内部会根据第一个元素的长度来确定 itemLength 的值
	 */
	public static <E> String join(@Nullable Collection<E> c, Function<? super E, Object> getter, String delimiter, int itemLength) {
		final int size = c == null ? 0 : c.size();
		switch (size) {
			case 0:
				return "";
			case 1:
				return String.valueOf(getter.apply(c.iterator().next()));
			default:
				return doJoinAppend(null, c, size, (sb, t) -> append(sb, getter.apply(t)), delimiter, itemLength).toString();
		}
	}

	/**
	 * 将集合的指定属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符，可以为 ""，但不能为 null
	 */
	public static <E> String join(@Nullable Collection<E> c, Function<? super E, Object> getter, String delimiter) {
		return join(c, getter, delimiter, 0);
	}

	/**
	 * 将集合的指定 整型 属性或输出拼接为字符串
	 * <p><b>注意</b>：目前仅支持【整型】（例如：{@link Long }、{@link Integer}、{@link Short}、{@link Byte}）集合
	 *
	 * @param delimiter 分隔符，可以为 ""，但不能为 null
	 */
	public static <T> String joinLong(@Nullable Collection<T> c, Function<? super T, Number> mapper, String delimiter) {
		return joinLongValue(c, t -> mapper.apply(t).longValue(), delimiter);
	}

	/**
	 * 将集合的指定整型属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符，可以为 ""，但不能为 null
	 * @param itemLength 集合中每个元素的预期长度，如果不大于 0，则内部会根据第一个元素的长度来确定 itemLength 的值
	 */
	public static <T> String joinLongValue(@Nullable Collection<T> c, ToLongFunction<? super T> mapper, String delimiter, int itemLength) {
		final int size = c == null ? 0 : c.size();
		switch (size) {
			case 0:
				return "";
			case 1:
				return Long.toString(mapper.applyAsLong(c instanceof List ? ((List<T>) c).get(0) : c.iterator().next()));
			default:
				return doJoinAppend(null, c, size, (sb, t) -> sb.append(mapper.applyAsLong(t)), delimiter, itemLength).toString();
		}
	}

	/**
	 * 将集合的指定整数（Long）属性或输出拼接为字符串
	 *
	 * @param delimiter 分隔符，可以为 ""，但不能为 null
	 */
	public static <T> String joinLongValue(@Nullable Collection<T> c, ToLongFunction<? super T> mapper, String delimiter) {
		return joinLongValue(c, mapper, delimiter, 0);
	}

	/**
	 * 将指定字符串数组拼接为 IN SQL子句 <br>
	 * 如果数组为空，将会引发异常 <br>
	 * 如果存在数组元素，则拼接内容形如 " IN (1, 2, 5)" 或 " IN ('1', '2', '5')"
	 *
	 * @param sb 指定的StringBuilder
	 * @param items 指定的任意数组
	 * @param isInclude 指示IN SQL是包含还是排除查询，如果是包含(true)将返回 IN，如果是排除(false)将返回 NOT IN
	 * @param isString 指示元素是否以字符串形式参与in SQL语句。如果为true，将会在每个元素两侧加上单引号"'"
	 */
	public static StringBuilder getInSQL(StringBuilder sb, Collection<?> items, boolean isInclude, boolean isString) {
		final int size = X.size(items);
		if (size == 0) {
			throw new IllegalArgumentException("Collection can not be empty:" + items);
		}
		sb = prepareInSQLBuilder(sb, isInclude, isString, size);
		if (isString) {// 如果是字符串格式
			sb.append('\'');
			doJoin(items, sb, "', '");
			sb.append("')");
		} else {// 如果是数字格式
			doJoin(items, sb, ", ");
			sb.append(')');
		}
		return sb;
	}

	/**
	 * @param size 必须 > 0
	 * @return <code>" IN ("</code> 或 <code>" NOT IN ("</code>
	 */
	static StringBuilder prepareInSQLBuilder(StringBuilder sb, boolean isInclude, boolean isString, int size) {
		if (sb == null) {
			final int factor = isString ? 10 : 8;
			sb = new StringBuilder(size * factor + (isInclude ? 4 : 8));
		}
		sb.append(isInclude ? " IN (" : " NOT IN (");
		return sb;
	}

	static void doJoin(Iterable<?> items, StringBuilder sb, String delimiter) {
		final Iterator<?> it = items.iterator();
		append(sb, it.next());
		while (it.hasNext()) {
			append(sb.append(delimiter), it.next());
		}
	}

	public static void append(final StringBuilder sb, Object val) {
		if (val instanceof Integer) {
			sb.append((int) val);
		} else if (val instanceof Long) {
			sb.append((long) val);
		} else {
			sb.append(val);
		}
	}

	public static void append(final StringBuilder sb, Integer val) {
		if (val != null) {
			sb.append((int) val);
		} else {
			sb.append("null");
		}
	}

	public static void append(final StringBuilder sb, Long val) {
		if (val != null) {
			sb.append((long) val);
		} else {
			sb.append("null");
		}
	}

	/**
	 * 将指定的数组拼接为InSQL子句并返回，内部将会根据元素个数来判断返回“=”语句还是“IN”语句<br>
	 * 如果数组为空，将会引发异常<br>
	 * 如果存在数组元素，将会返回“ IN (1, 2, 5)”或“ IN ('1', '2', '5')”
	 *
	 * @param items 指定的数组
	 * @param isInclude 指示IN SQL是包含还是排除查询，如果是包含(true)将返回=、IN，如果是排除(false)将返回!=、NOT IN
	 * @param isString 指示元素是否以字符串形式参与InSQL语句。如果为true，将会在每个元素两侧加上单引号"'"
	 */
	public static String getInSQL(Collection<?> items, boolean isInclude, boolean isString) {
		return getInSQL(null, items, isInclude, isString).toString();
	}

	/**
	 * 将指定的数组拼接为InSQL子句并返回，内部将会根据元素个数来判断返回“=”语句还是“IN”语句<br>
	 * 如果数组为空，将会引发异常<br>
	 * 如果存在数组元素，将会返回“ IN (1, 2, 5)”或“ IN ('1', '2', '5')”
	 *
	 * @param items 指定的数组
	 * @param isString 指示元素是否以字符串形式参与InSQL语句。如果为true，将会在每个元素两侧加上单引号"'"
	 */
	public static String getInSQL(Collection<?> items, boolean isString) {
		return getInSQL(null, items, true, isString).toString();
	}

	/**
	 * 将指定字符串数组拼接为 IN SQL子句 <br>
	 * 如果数组为空，将会引发异常 <br>
	 * 如果存在数组元素，则拼接内容形如 " IN (1, 2, 5)" 或 " IN ('1', '2', '5')"
	 *
	 * @param sb 指定的StringBuilder
	 * @param array 指定的任意数组
	 * @param isInclude 指示IN SQL是包含还是排除查询，如果是包含(true)将返回 IN，如果是排除(false)将返回 NOT IN
	 * @param isString 指示元素是否以字符串形式参与in SQL语句。如果为true，将会在每个元素两侧加上单引号"'"
	 */
	public static StringBuilder getInSQL(@Nullable StringBuilder sb, Object[] array, boolean isInclude, boolean isString) {
		final int length = array == null ? 0 : array.length;
		if (length == 0) {
			throw new IllegalArgumentException("Array can not be empty");
		}
		sb = prepareInSQLBuilder(sb, isInclude, isString, length);
		if (isString) {// 如果是字符串格式
			sb.append('\'');
			doJoin(sb, array, length, StringX::append, "', '");
			sb.append("')");
		} else {// 如果是数字格式
			doJoin(sb, array, length, StringX::append, ", ");
			sb.append(')');
		}
		return sb;
	}

	/**
	 * 将指定的数组拼接为InSQL子句并返回，内部将会根据元素个数来判断返回“=”语句还是“IN”语句<br>
	 * 如果数组为空，将会引发异常<br>
	 * 如果存在数组元素，将会返回“ IN (1, 2, 5)”或“ IN ('1', '2', '5')”
	 *
	 * @param items 指定的数组
	 * @param isInclude 指示IN SQL是包含还是排除查询，如果是包含(true)将返回=、IN，如果是排除(false)将返回!=、NOT IN
	 * @param isString 指示元素是否以字符串形式参与InSQL语句。如果为true，将会在每个元素两侧加上单引号"'"
	 */
	public static String getInSQL(Object[] items, boolean isInclude, boolean isString) {
		return getInSQL(null, items, isInclude, isString).toString();
	}

	/**
	 * 将指定的数组拼接为InSQL子句并返回，内部将会根据元素个数来判断返回“=”语句还是“IN”语句<br>
	 * 如果数组为空，将会引发异常<br>
	 * 如果存在数组元素，将会返回“ IN (1, 2, 5)”或“ IN ('1', '2', '5')”
	 *
	 * @param items 指定的数组
	 * @param isString 指示元素是否以字符串形式参与InSQL语句。如果为 true，将会在每个元素两侧加上单引号"'"
	 */
	public static String getInSQL(Object[] items, boolean isString) {
		return getInSQL(null, items, true, isString).toString();
	}

	/**
	 * 将以指定分隔字符分隔字符串，并将拆分后的每个片段文本使用指定的 {@code mapper} 进行转换，并返回转换后的元素集合
	 *
	 * @param str 需要被拆分的字符串
	 * @param sep 分隔符
	 * @param mapper 转换器
	 * @param filter 过滤器（如果应用到对应的元素返回 false，则返回的集合中不会包含该元素 ）
	 */
	public static <T> List<T> split(@Nullable final String str, final String sep, @Nullable Predicate<? super String> filter, final Function<? super String, T> mapper) {
		if (notEmpty(str)) {
			final List<T> list = new ArrayList<>();
			int pos, start = 0;
			// ",,"
			while ((pos = str.indexOf(sep, start)) != -1) {
				addPartToList(str, mapper, filter, list, start, pos);
				start = pos + 1;
			}
			if (start <= str.length()) {
				addPartToList(str, mapper, filter, list, start, str.length());
			}
			return list;
		}
		return null;
	}

	/**
	 * 将以指定分隔字符分隔字符串，并将拆分后的每个片段文本使用指定的 {@code mapper} 进行转换，并返回转换后的元素集合
	 *
	 * @param toSplit 需要被拆分的字符串
	 * @param sep 分隔符
	 * @param mapper 转换器
	 * @param ignoreEmpty 是否忽略空子字符串（如果为 true，则忽略空字符串 ）
	 */
	public static <T> List<T> split(final String toSplit, final String sep, final Function<? super String, T> mapper, final boolean ignoreEmpty) {
		if (notEmpty(toSplit)) {
			final Predicate<String> filter = ignoreEmpty ? StringX::notEmpty : null;
			return split(toSplit, sep, filter, mapper);
		}
		return null;
	}

	/**
	 * 将以指定分隔字符分隔字符串，并将拆分后的每个片段文本使用指定的 {@code mapper} 进行转换，并返回转换后的元素集合
	 *
	 * @param toSplit 需要被拆分的字符串
	 * @param sep 分隔符
	 * @param mapper 转换器
	 */
	public static <T> List<T> split(final String toSplit, final String sep, final Function<String, T> mapper) {
		return split(toSplit, sep, mapper, true);
	}

	private static <T> void addPartToList(String str, Function<? super String, T> mapper, @Nullable Predicate<? super String> filter, List<T> toList, int start, int end) {
		final String substr = start == end ? "" : str.substring(start, end);
		if (filter == null || filter.test(substr)) {
			T part = mapper.apply(substr);
			toList.add(part);
		}
	}

}