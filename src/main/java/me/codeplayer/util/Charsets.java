package me.codeplayer.util;

import java.nio.charset.*;

/**
 * 常用的字符集常量
 *
 * @author Ready
 * @date 2014-10-23
 */
public abstract class Charsets {

	/**
	 * 美国 ASCII 标准字符集
	 */
	public static final Charset US_ASCII = StandardCharsets.US_ASCII;
	/**
	 * ISO-8859-1，ISO拉丁字母表NO.1 字符集。即也叫作"ISO-LATIN-1"
	 */
	public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;
	/**
	 * 8 位 UCS 转换格式
	 */
	public static final Charset UTF_8 = StandardCharsets.UTF_8;
	/**
	 * 16 位 UCS 转换格式，Big Endian（最低地址存放高位字节）字节顺序
	 */
	public static final Charset UTF_16BE = StandardCharsets.UTF_16BE;
	/**
	 * 16 位 UCS 转换格式，Little-endian（最高地址存放低位字节）字节顺序
	 */
	public static final Charset UTF_16LE = StandardCharsets.UTF_16LE;
	/**
	 * 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识
	 */
	public static final Charset UTF_16 = StandardCharsets.UTF_16;
	/**
	 * 中国大陆地区 GBK 字符集，主要用于常见的简体中文编码
	 */
	public static final Charset GBK = Charset.isSupported("GBK") ? Charset.forName("GBK") : null;

}