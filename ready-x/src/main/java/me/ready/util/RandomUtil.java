package me.ready.util;

import java.util.Arrays;
import java.util.Random;

/**
 * 用于协助处理与随机生成有关的工具类
 * 
 * @package me.ready.util
 * @author Ready
 * @date 2014-10-15
 */
public class RandomUtil {

	// 禁止实例构建
	private RandomUtil() {};

	/**
	 * 返回min(包括)和max(包括)之间的一个随机整数
	 * 
	 * @param min 返回的最小值
	 * @param max 返回的最大值
	 * @return
	 */
	public static final int getInt(int min, int max) {
		return new Random().nextInt(max - min + 1) + min;
	}

	/**
	 * 随机生成指定长度的数字字符串
	 * 
	 * @param length
	 */
	public static final String getIntString(int length) {
		if (length <= 0) {
			return "";
		}
		String str = Double.toString(Math.random()).substring(2);
		int realLen = str.length();
		if (length > realLen) {
			StringBuilder sb = new StringBuilder(length).append(str);
			int remain = length - realLen;
			while (remain > 0) {
				str = Double.toString(Math.random()).substring(2);
				realLen = str.length();
				if (remain >= realLen) {
					sb.append(str);
				} else {
					sb.append(str.substring(0, remain));
				}
				remain -= realLen;
			}
			return sb.toString();
		} else {
			return str.substring(0, length);
		}
	}

	/**
	 * 随机返回0 ~ 1之间(不包括1)的双精度浮点数
	 * 
	 * @return
	 */
	public static final double getDouble() {
		return Math.random();
	}

	/**
	 * 随机返回0 ~ 1之间(不包括1)的单精度浮点数
	 * 
	 * @return
	 */
	public static final float getFloat() {
		return new Random().nextFloat();
	}

	/**
	 * 随机返回一个long值<br>
	 * 因为 <code>java.util.Random</code> 类使用只以 48 位表示的种子，所以此算法不会返回所有可能的 long值。
	 * 
	 * @return
	 */
	public static final long getLong() {
		return new Random().nextLong();
	}

	/**
	 * 随机返回一个boolean值
	 * 
	 * @return
	 */
	public static final boolean getBoolean() {
		return new Random().nextBoolean();
	}

	/**
	 * 根据指定的字符数组，随机返回其中的一个字符<br>
	 * 如果字符数组为空，则默认返回一个空格字符<code>' '</code>
	 * 
	 * @param chars
	 * @return
	 */
	public static final char getChar(char[] chars) {
		switch (chars.length) {
		case 0:
			return ' ';
		case 1:
			return chars[0];
		default:
			return chars[new Random().nextInt(chars.length)];
		}
	}

	/**
	 * 根据指定的字符串，随机返回其中的一个字符<br>
	 * 如果为空字符串，则默认返回一个空格字符<code>' '</code>
	 * 
	 * @param str
	 * @return
	 */
	public static final char getChar(String str) {
		return getChar(str.toCharArray());
	}

	/**
	 * 随机抽取指定字符数组中的字符，生成指定长度的字符串
	 * 
	 * @param str 用于提供字符来源的字符数组
	 * @param length 生成的字符串的长度。如果长度小于1，则返回空字符串
	 * @return
	 */
	public static final String getString(char[] chars, int length) {
		if (length < 1) {
			return "";
		}
		char[] newChars = new char[length];
		if (chars.length == 1) {
			Arrays.fill(newChars, chars[0]);
		} else {
			Random random = new Random();
			for (int i = 0; i < length; i++) {
				newChars[i] = chars[random.nextInt(length)];
			}
		}
		return new String(newChars);
	}

	/**
	 * 随机抽取指定字符串中的字符，生成指定长度的字符串
	 * 
	 * @param str 用于提供字符来源的字符串
	 * @param length 生成的字符串的长度。如果长度小于1，则返回空字符串
	 * @return
	 */
	public static final String getString(String str, int length) {
		return getString(str.toCharArray(), length);
	}
}
