package me.codeplayer.util;

/**
 * 为不同 Java 版本进行 multi-release 适配的 Java 辅助工具类
 */
public class JavaHelper {

	/** 该方法没有实际用途，仅用于判断多版本 jar 是否生效 */
	public static boolean newFeature() {
		return false;
	}

	public static int parseInt(String str, int start, int end) {
		return Integer.parseInt(str.substring(start, end), 10);
	}

	public static long parseLong(String str, int start, int end) {
		return Long.parseLong(str.substring(start, end), 10);
	}

	/**
	 * 将指定字节数组转为十六进制形式的字符串<br>
	 * 数组不能为 null，否则引发空指针异常
	 *
	 * @param bytes 指定的字节数组
	 * @param start 开始转换的索引
	 * @param end 结束转换的索引
	 * @param charTable 将数值作为下标的进制转换字符映射表，形如 "0123456789ABCDEF" 的 {@code char[] } 形式【仅支持 Latin1 字符】
	 * @throws NullPointerException 如果 bytes 或 charTable 为 null
	 * @throws ArrayIndexOutOfBoundsException 如果索引超出范围
	 */
	public static String toHexString(final byte[] bytes, final int start, final int end, final char[] charTable) {
		final int length = end - start;
		if (start < 0 || end > bytes.length || length < 0) {
			// 如果结尾索引或截取长度大于数组长度
			throw new ArrayIndexOutOfBoundsException(end);
		}
		final char[] chars = new char[length << 1];
		int c = 0;
		for (int i = start; i < end; i++) {
			chars[c++] = charTable[bytes[i] >> 4 & 0xf];
			chars[c++] = charTable[bytes[i] & 0xf];
		}
		return JavaUtil.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
	}

	/**
	 * 将指定字节数组转为十六进制形式的字符串<br>
	 * 数组不能为 null，否则引发空指针异常
	 *
	 * @param bytes 指定的字节数组
	 * @param start 开始转换的索引
	 * @param end 结束转换的索引
	 * @param charTable 将数值作为下标的进制转换字符映射表，形如 "0123456789ABCDEF" 的 {@code byte[] } 形式【仅支持 Latin1 字符】
	 * @throws NullPointerException 如果 bytes 或 charTable 为 null
	 * @throws ArrayIndexOutOfBoundsException 如果索引超出范围
	 */
	public static String toHexString(final byte[] bytes, final int start, final int end, final byte[] charTable) {
		final int length = end - start;
		if (start < 0 || end > bytes.length || length < 0) {
			// 如果结尾索引或截取长度大于数组长度
			throw new ArrayIndexOutOfBoundsException(end);
		}
		final char[] chars = new char[length << 1];
		int c = 0;
		for (int i = start; i < end; i++) {
			chars[c++] = (char) charTable[bytes[i] >> 4 & 0xf];
			chars[c++] = (char) charTable[bytes[i] & 0xf];
		}
		return JavaUtil.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
	}

	/**
	 * 将指定字节数组转为十六进制形式的字符串<br>
	 * 数组不能为 null，否则引发空指针异常
	 *
	 * @param bytes 指定的字节数组
	 * @param start 开始转换的索引
	 * @param end 结束转换的索引
	 * @param charTable 将数值作为下标的进制转换字符映射表，形如 "0123456789ABCDEF" 的形式
	 * @throws NullPointerException 如果 bytes 或 charTable 为 null
	 * @throws ArrayIndexOutOfBoundsException 如果索引超出范围
	 */
	public static String toHexString(final byte[] bytes, final int start, final int end, final String charTable) {
		return toHexString(bytes, start, end, JavaUtil.getCharArray(charTable));
	}

	/**
	 * 获取指定字符串的Unicode编码，例如："中国" 将返回 <code>"\ u4E2D\ u56FD"</code> <br>
	 *
	 * @param source 指定字符串不能为 null，否则将引发空指针异常
	 * @param full 是否对全部字符都使用 Unicode 形式表示。以 "A" 为例， <code>true</code>将返回 "A"，否则输出 <code>"\ u0041"</code>
	 * @param digitTable 将数值作为下标的进制转换字符映射表，形如 "0123456789ABCDEF" 的字符串
	 * @throws NullPointerException 如果 source 或 digitTable 为 null
	 */
	public static String unicode(String source, final boolean full, String digitTable) {
		if (!source.isEmpty()) {// 由于转换出来的字节数组前两位属于UNICODE固定标记，因此要过滤掉
			final char[] sourceChars = JavaUtil.getCharArray(source),
					charTable = JavaUtil.getCharArray(digitTable);
			if (full) {
				final char[] chars = new char[sourceChars.length * 6];
				int c = 0;
				for (char ch : sourceChars) {
					chars[c++] = '\\';
					chars[c++] = 'u';
					chars[c++] = charTable[(ch >> 12) & 0xF];
					chars[c++] = charTable[(ch >> 8) & 0xF];
					chars[c++] = charTable[(ch >> 4) & 0xF];
					chars[c++] = charTable[ch & 0xF];
				}
				return JavaUtil.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
			}
			final StringBuilder sb = new StringBuilder(sourceChars.length * 3);
			for (char ch : sourceChars) {
				if (ch > 0x80) { // 非 ASCII 字符转为 \\uXXXX
					sb.append("\\u");
					sb.append(charTable[(ch >> 12) & 0xF]);
					sb.append(charTable[(ch >> 8) & 0xF]);
					sb.append(charTable[(ch >> 4) & 0xF]);
					sb.append(charTable[ch & 0xF]);
				} else { // ASCII 直接保留
					sb.append(ch);
				}
			}
			return sb.toString();
		}
		return "";
	}

}