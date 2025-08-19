package me.codeplayer.util;

/**
 * 为不同 Java 版本进行 multi-release 适配的 Java 辅助工具类
 */
public class JavaHelper {

	public static int parseInt(String str, int start, int end) {
		return Integer.parseInt(str.substring(start, end), 10);
	}

	public static long parseLong(String str, int start, int end) {
		return Long.parseLong(str.substring(start, end), 10);
	}

}