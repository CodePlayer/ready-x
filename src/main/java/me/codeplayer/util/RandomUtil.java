package me.codeplayer.util;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 用于协助处理与随机生成有关的工具类
 *
 * @author Ready
 * @since 2014-10-15
 */
public abstract class RandomUtil {

	/**
	 * 返回min(包括)和max(包括)之间的一个随机整数
	 *
	 * @param min 返回的最小值
	 * @param max 返回的最大值
	 */
	public static int getInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	/**
	 * 随机生成指定长度的数字字符串
	 */
	public static String getIntString(int length) {
		if (length <= 0) {
			return "";
		}
		final StringBuilder sb = new StringBuilder(length);
		final int batch = 15; // 不可超过15
		int remain = length;
		final ThreadLocalRandom random = ThreadLocalRandom.current();
		do {
			final int expect = Math.min(batch, remain);
			final long val = random.nextLong(0L, (long) Math.pow(10, expect));
			StringUtil.zeroFill(sb, val, expect);
			remain -= batch;
		} while (remain > 0);
		return sb.toString();
	}

	/**
	 * 随机返回0 ~ 1之间(不包括1)的双精度浮点数
	 */
	public static double getDouble() {
		return ThreadLocalRandom.current().nextDouble();
	}

	/**
	 * 随机返回0 ~ 1之间(不包括1)的单精度浮点数
	 */
	public static float getFloat() {
		return ThreadLocalRandom.current().nextFloat();
	}

	/**
	 * 随机返回一个long值<br>
	 * 因为 <code>java.util.Random</code> 类使用只以 48 位表示的种子，所以此算法不会返回所有可能的 long值。
	 */
	public static long getLong() {
		return ThreadLocalRandom.current().nextLong();
	}

	/**
	 * 随机返回一个boolean值
	 */
	public static boolean getBoolean() {
		return ThreadLocalRandom.current().nextBoolean();
	}

	/**
	 * 根据指定的字符数组，随机返回其中的一个字符<br>
	 * 如果字符数组为空，则默认返回一个空格字符<code>' '</code>
	 */
	public static char getChar(char[] chars) {
		switch (chars.length) {
			case 0:
				return ' ';
			case 1:
				return chars[0];
			default:
				return chars[ThreadLocalRandom.current().nextInt(0, chars.length)];
		}
	}

	/**
	 * 根据指定的字符串，随机返回其中的一个字符<br>
	 * 如果为空字符串，则默认返回一个空格字符<code>' '</code>
	 */
	public static char getChar(String str) {
		final int length = str.length();
		switch (length) {
			case 0:
				return ' ';
			case 1:
				return str.charAt(0);
			default:
				return str.charAt(ThreadLocalRandom.current().nextInt(0, length));
		}
	}

	/**
	 * 随机抽取指定字符数组中的字符，生成指定长度的字符串
	 *
	 * @param chars 用于提供字符来源的字符数组
	 * @param length 生成的字符串的长度。如果长度小于1，则返回空字符串
	 */
	public static String getString(final char[] chars, final int length) {
		if (length < 1) {
			return "";
		}
		char[] newChars = new char[length];
		if (chars.length == 1) {
			Arrays.fill(newChars, chars[0]);
		} else {
			final ThreadLocalRandom random = ThreadLocalRandom.current();
			for (int i = 0; i < length; i++) {
				newChars[i] = chars[random.nextInt(chars.length)];
			}
		}
		return new String(newChars);
	}

	/**
	 * 随机抽取指定字符串中的字符，生成指定长度的字符串
	 *
	 * @param str 用于提供字符来源的字符串
	 * @param length 生成的字符串的长度。如果长度小于1，则返回空字符串
	 */
	public static String getString(final String str, final int length) {
		if (length < 1) {
			return "";
		}
		final char[] newChars = new char[length];
		final int max = str.length();
		if (max == 1) {
			Arrays.fill(newChars, str.charAt(0));
		} else {
			final ThreadLocalRandom random = ThreadLocalRandom.current();
			for (int i = 0; i < length; i++) {
				newChars[i] = str.charAt(random.nextInt(max));
			}
		}
		return new String(newChars);
	}

}