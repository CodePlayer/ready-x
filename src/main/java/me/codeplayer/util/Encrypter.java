package me.codeplayer.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 封装常用数据加密算法的加密器<br>
 * 内部主要包含MD5、SHA-1等不可逆算法以及DES可逆算法的常用处理方法<br>
 * <strong>备注</strong>：MD5 加密强度是 128bit，SHA-1 的加密强度是 160bit<br>
 * 因此 SHA-1 比 MD5 的加密强度更高，并且更不易受密码分析的攻击<br>
 * 相对的，在同样的硬件上，SHA-1 比 MD5 的运行速度要稍慢
 *
 * @author Ready
 * @since 2012-10-21
 */
public abstract class Encrypter {

	/**
	 * 获取对应的摘要算法
	 */
	public static MessageDigest getMessageDigest(String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("Unexpected algorithm:" + algorithm, e);
		}
	}

	/**
	 * 将指定的字符串通过MD5加密算法进行加密，并返回加密后32位的MD5值<br>
	 * 如果字符串为 null，将引发空指针异常
	 */
	public static String md5(String input) {
		return encode(input, "MD5");
	}

	/**
	 * 将指定的字符串通过MD5加密算法进行加密，并返回加密后16位的MD5值<br>
	 * 为了保持一致，内部统一使用UTF-8编码获取字符串的字节数组<br>
	 * 如果字符串为 null，将引发空指针异常
	 */
	public static String md5For16(String input) {
		byte[] bytes = encode(JavaX.getUtf8Bytes(input), "MD5");
		return bytes2Hex(bytes, 4, 12);
	}

	/**
	 * 将指定的字节数组通过MD5加密算法进行加密，并返回加密后的32位MD5值<br>
	 * 如果数组为null，则引发空指针异常
	 */
	public static byte[] md5(final byte[] buf) {
		return encode(buf, "MD5");
	}

	/**
	 * 将指定的字符串通过SHA-1加密算法进行加密，并返回加密后的40位SHA-1值<br>
	 * 为了保持一致，内部统一使用UTF-8编码获取字符串的字节数组<br>
	 * 如果字符串为null，将引发空指针异常
	 */
	public static String sha1(String input) {
		return encode(input, "SHA");
	}

	/**
	 * 将指定的字节数组通过SHA-1加密算法进行加密，并返回加密后的字节数组<br>
	 * 如果数组为null，则引发空指针异常
	 */
	public static byte[] sha1(final byte[] buf) {
		return encode(buf, "SHA");
	}

	/**
	 * 以指定的算法对指定的字节数组进行加密运算，并返回加密后的字节数组<br>
	 * <b>注意：</b>算法名称必须是MessageDigest对象支持的算法，否则可能引发异常
	 *
	 * @param buf 指定的字节数组
	 * @param algorithm 算法名称
	 */
	public static byte[] encode(final byte[] buf, String algorithm) {
		return getMessageDigest(algorithm).digest(buf);
	}

	/**
	 * 以指定的算法对指定的字符串进行加密运算，并返回加密后的十六进制的字符串值<br>
	 * <b>注意：</b>算法名称必须是MessageDigest对象支持的算法，否则可能引发异常
	 *
	 * @param input 指定的字符串
	 * @param algorithm 算法名称，如“MD5”、“SHA”、“SHA-256”、“SHA-384”、“SHA-512”。
	 */
	public static String encode(String input, String algorithm, boolean upperCase) {
		return bytes2Hex(getMessageDigest(algorithm).digest(JavaX.getUtf8Bytes(input)), upperCase);
	}

	/**
	 * 以指定的算法对指定的字符串进行加密运算，并返回加密后的十六进制的字符串值<br>
	 * <b>注意：</b>算法名称必须是MessageDigest对象支持的算法，否则可能引发异常
	 *
	 * @param input 指定的字符串
	 * @param algorithm 算法名称，如“MD5”、“SHA”、“SHA-256”、“SHA-384”、“SHA-512”。
	 */
	public static String encode(String input, String algorithm) {
		return encode(input, algorithm, false);
	}

	/**
	 * 将指定字节数组转为十六进制形式的字符串<br>
	 * 数组不能为null，否则引发空指针异常
	 */
	public static String bytes2Hex(final byte[] bytes, boolean upperCase) {
		return StringX.toHexString(bytes, 0, bytes.length, upperCase);
	}

	/**
	 * 将指定字节数组转为十六进制形式的字符串<br>
	 * 数组不能为null，否则引发空指针异常
	 */
	public static String bytes2Hex(final byte[] bytes) {
		return StringX.toHexString(bytes);
	}

	/**
	 * 将指定字节数组转为十六进制形式的字符串<br>
	 * 数组不能为null，否则引发空指针异常
	 *
	 * @param bytes 指定的字节数组
	 * @param start 开始转换的索引
	 * @param end 结束转换的索引
	 * @param digitTable 十六进制字符表
	 */
	public static String bytes2Hex(final byte[] bytes, final int start, final int end, final char[] digitTable) {
		return JavaHelper.toHexString(bytes, start, end, digitTable);
	}

	/**
	 * 将指定字节数组转为十六进制形式的字符串<br>
	 * 数组不能为null，否则引发空指针异常
	 *
	 * @param bytes 指定的字节数组
	 * @param start 开始转换的索引
	 * @param end 结束转换的索引
	 * @param digitTable 十六进制字符表
	 */
	public static String bytes2Hex(final byte[] bytes, final int start, final int end, String digitTable) {
		return JavaHelper.toHexString(bytes, start, end, digitTable);
	}

	/**
	 * 将指定字节数组转为十六进制形式的字符串<br>
	 * 数组不能为null，否则引发空指针异常
	 *
	 * @param bytes 指定的字节数组
	 * @param start 开始转换的索引
	 * @param end 结束转换的索引
	 */
	public static String bytes2Hex(final byte[] bytes, final int start, final int end) {
		return StringX.toHexString(bytes, start, end, false);
	}

}