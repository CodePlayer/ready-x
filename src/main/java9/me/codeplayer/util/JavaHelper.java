package me.codeplayer.util;

import java.util.Arrays;

/**
 * 为不同 Java 版本进行 multi-release 适配的 Java 辅助工具类
 */
public class JavaHelper {

	/** 该方法没有实际用途，仅用于判断多版本 jar 是否生效 */
	public static boolean newFeature() {
		return true;
	}

	public static int parseInt(String str, int start, int end) {
		return Integer.parseInt(str, start, end, 10);
	}

	public static long parseLong(String str, int start, int end) {
		return Long.parseLong(str, start, end, 10);
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
		final byte[] chars = new byte[length << 1];
		int c = 0;
		for (int i = start; i < end; i++) {
			chars[c++] = (byte) charTable[bytes[i] >> 4 & 0xf];
			chars[c++] = (byte) charTable[bytes[i] & 0xf];
		}
		return JavaX.STRING_CREATOR_JDK11.apply(chars, JavaX.LATIN1);
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
		final byte[] chars = new byte[length << 1];
		int c = 0;
		for (int i = start; i < end; i++) {
			chars[c++] = charTable[bytes[i] >> 4 & 0xf];
			chars[c++] = charTable[bytes[i] & 0xf];
		}
		return JavaX.STRING_CREATOR_JDK11.apply(chars, JavaX.LATIN1);
	}

	/**
	 * 将指定字节数组转为十六进制形式的字符串<br>
	 * 数组不能为 null，否则引发空指针异常
	 *
	 * @param bytes 指定的字节数组
	 * @param start 开始转换的索引
	 * @param end 结束转换的索引
	 * @param charTable 将数值作为下标的进制转换字符映射表，形如 "0123456789ABCDEF" 的形式，仅支持 Latin1 字符集中的字符
	 * @throws NullPointerException 如果 bytes 或 charTable 为 null
	 * @throws ArrayIndexOutOfBoundsException 如果索引超出范围
	 */
	public static String toHexString(final byte[] bytes, final int start, final int end, final String charTable) {
		return toHexString(bytes, start, end, JavaX.STRING_VALUE.apply(charTable));
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
		System.out.println("Java 9 unicode");
		if (!source.isEmpty()) {
			final boolean latin1 = JavaX.STRING_CODER.applyAsInt(source) == JavaX.LATIN1;
			final byte[] bytes = JavaX.STRING_VALUE.apply(source);
			if (!full && latin1 && JavaX.isASCII(bytes)) {
				return source;
			}
			final byte[] charTable = JavaX.STRING_VALUE.apply(digitTable);
			final int length = source.length();
			int pos = 0;
			byte[] chars = new byte[length * 6];
			for (int i = 0; i < bytes.length; i++) {
				int ch = latin1 ? bytes[i] & 0xFF
						: source.charAt(i);
				if (full || ch >= 0x80) { // 非 ASCII 字符必须转为 \\uXXXX
					chars[pos++] = '\\';
					chars[pos++] = 'u';
					chars[pos++] = charTable[(ch >> 12) & 0xF];
					chars[pos++] = charTable[(ch >> 8) & 0xF];
					chars[pos++] = charTable[(ch >> 4) & 0xF];
					chars[pos++] = charTable[ch & 0xF];
				} else {
					chars[pos++] = (byte) ch;
				}
			}
			if (pos < chars.length) {
				chars = Arrays.copyOfRange(chars, 0, pos);
			}
			return JavaX.STRING_CREATOR_JDK11.apply(chars, JavaX.LATIN1);
		}
		return "";
	}

}