package me.ready.util;

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
	public final static int getInt(int min, int max) {
		return new Random().nextInt(max - min + 1) + min;
	}

	/**
	 * 随机生成指定参数的字符串
	 * 
	 * @param length
	 */
	public final static String getString(int length) {
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
					sb.append(str.subSequence(0, remain));
				}
				remain -= realLen;
			}
			return sb.toString();
		} else {
			return str.substring(0, length);
		}
	}
}
