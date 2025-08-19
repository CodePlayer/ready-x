package me.codeplayer.util;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 用于协助处理与随机生成有关的工具类
 *
 * @author Ready
 * @since 2014-10-15
 */
public abstract class RandomX {

	/**
	 * 返回 min(包括) 和 max(不包括) 之间的一个随机整数
	 *
	 * @param min 返回的最小值
	 * @param max 返回的最大值（不包括）
	 * @throws IllegalArgumentException 如果 min >= max
	 */
	public static int nextInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	/**
	 * 返回 min(包括) 和 max(不包括) 之间的一个随机整数
	 *
	 * @param min 返回的最小值
	 * @param max 返回的最大值(不包括)
	 * @throws IllegalArgumentException 如果 min >= max
	 */
	public static long nextLong(long min, long max) {
		return ThreadLocalRandom.current().nextLong(min, max);
	}

	/**
	 * 返回 <code> [min, max) </code> 之间的一个随机 double 数
	 *
	 * @param min 返回的最小值
	 * @param max 返回的最大值(不包括)
	 * @throws IllegalArgumentException 如果 min >= max
	 */
	public static double nextDouble(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

	/**
	 * 返回 <code> [0, 1) </code> 之间的一个随机 double 数
	 */
	public static double nextDouble() {
		return ThreadLocalRandom.current().nextDouble();
	}

	/**
	 * 使用随机数据填充满指定的字节数组
	 *
	 * @param bytes 需要填充的数组
	 * @throws NullPointerException bytes 为 null
	 */
	public static void nextBytes(byte[] bytes) {
		ThreadLocalRandom.current().nextBytes(bytes);
	}

	// 预计算的幂值，避免重复计算Math.pow(10, expect)
	private static final long[] POWERS_OF_10 = {
			1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L,
			100000000L, 1000000000L, 10000000000L, 100000000000L,
			1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L
	};

	/**
	 * 随机生成指定长度的数字字符串
	 */
	public static String getIntString(int length) {
		if (length <= 0) {
			return "";
		}
		final StringBuilder sb = new StringBuilder(length);
		final int batch = 15; // 不可超过 15
		int remain = length;
		final ThreadLocalRandom random = ThreadLocalRandom.current();
		do {
			final int expect = Math.min(batch, remain);
			final long val = random.nextLong(0L, POWERS_OF_10[expect]);
			StringX.zeroFill(sb, val, expect);
			remain -= batch;
		} while (remain > 0);
		return sb.toString();
	}

	/**
	 * 随机返回 0 ~ 1 之间(不包括 1 )的单精度浮点数
	 */
	public static float nextFloat() {
		return ThreadLocalRandom.current().nextFloat();
	}

	/**
	 * 随机返回一个 long 值
	 */
	public static long nextLong() {
		return ThreadLocalRandom.current().nextLong();
	}

	/**
	 * 随机返回一个 boolean 值
	 */
	public static boolean nextBoolean() {
		return ThreadLocalRandom.current().nextBoolean();
	}

	/**
	 * 根据指定的字符数组，随机返回其中的一个字符<br>
	 * 如果字符数组为空，则默认返回一个空格字符<code>' '</code>
	 */
	public static char nextChar(char[] chars) {
		switch (chars.length) {
			case 0:
				return ' ';
			case 1:
				return chars[0];
			default:
				return chars[nextInt(0, chars.length)];
		}
	}

	/**
	 * 根据指定的字符串，随机返回其中的一个字符<br>
	 * 如果为空字符串，则默认返回一个空格字符<code>' '</code>
	 */
	public static char nextChar(String chars) {
		final int length = chars.length();
		switch (length) {
			case 0:
				return ' ';
			case 1:
				return chars.charAt(0);
			default:
				return chars.charAt(nextInt(0, length));
		}
	}

	/**
	 * 随机抽取指定字符数组中的字符，生成指定长度的字符串
	 *
	 * @param chars 用于提供字符来源的字符数组
	 * @param length 生成的字符串的长度。如果长度小于1，则返回空字符串
	 * @throws NullPointerException 如果 chars 为 null
	 * @throws IllegalArgumentException 如果 chars 为空数组
	 */
	public static String nextString(final char[] chars, final int length) {
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
	 * @param chars 用于提供字符来源的字符串
	 * @param length 生成的字符串的长度。如果长度小于1，则返回空字符串
	 * @throws NullPointerException 如果 chars 为 null
	 * @throws IllegalArgumentException 如果 chars 为空数组
	 */
	public static String nextString(final String chars, final int length) {
		if (length < 1) {
			return "";
		}
		final int max = chars.length();
		final char[] newChars = new char[length];
		if (max == 1) {
			Arrays.fill(newChars, chars.charAt(0));
		} else {
			final ThreadLocalRandom random = ThreadLocalRandom.current();
			for (int i = 0; i < length; i++) {
				newChars[i] = chars.charAt(random.nextInt(max));
			}
		}
		return new String(newChars);
	}

}