package me.codeplayer.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 封装常用数据加密算法的加密器<br>
 * 内部主要包含MD5、SHA-1等不可逆算法以及DES可逆算法的常用处理方法<br>
 * <strong>备注</strong>：MD5加密强度是128bit，SHA-1的加密强度是160bit<br>
 * 因此SHA-1比MD5的加密强度更高，并且更不易受密码分析的攻击<br>
 * 相对的，在同样的硬件上，SHA-1比MD5的运行速度要稍慢
 * 
 * @author Ready
 * @date 2012-10-21
 */
public abstract class Encrypter {

	/**
	 * 获取对应的摘要算法
	 * 
	 * @param algorithm
	 * @return
	 */
	public static final MessageDigest getMessageDigest(String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("不支持算法[" + algorithm + "]", e);
		}
	}

	/**
	 * 将指定的字符串通过MD5加密算法进行加密，并返回加密后32位的MD5值<br>
	 * 如果字符串为null，将引发空指针异常
	 * 
	 * @param input
	 * @return
	 */
	public static final String md5(String input) {
		return encode(input, "MD5");
	}

	/**
	 * 将指定的字符串通过MD5加密算法进行加密，并返回加密后16位的MD5值<br>
	 * 为了保持一致，内部统一使用UTF-8编码获取字符串的字节数组<br>
	 * 如果字符串为null，将引发空指针异常
	 * 
	 * @param input
	 * @return
	 */
	public static final String md5For16(String input) {
		byte[] bytes;
		try {
			bytes = encode(input.getBytes("UTF-8"), "MD5");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
		return bytes2Hex(bytes, 4, 12);
	}

	/**
	 * 将指定的字节数组通过MD5加密算法进行加密，并返回加密后的32位MD5值<br>
	 * 如果数组为null，则引发空指针异常
	 * 
	 * @param buf
	 * @return
	 */
	public static final byte[] md5(final byte[] buf) {
		return encode(buf, "MD5");
	}

	/**
	 * 将指定的字符串通过SHA-1加密算法进行加密，并返回加密后的40位SHA-1值<br>
	 * 为了保持一致，内部统一使用UTF-8编码获取字符串的字节数组<br>
	 * 如果字符串为null，将引发空指针异常
	 * 
	 * @param input
	 * @return
	 */
	public static final String sha1(String input) {
		return encode(input, "SHA");
	}

	/**
	 * 将指定的字节数组通过SHA-1加密算法进行加密，并返回加密后的字节数组<br>
	 * 如果数组为null，则引发空指针异常
	 * 
	 * @param buf
	 * @return
	 */
	public static final byte[] sha1(final byte[] buf) {
		return encode(buf, "SHA");
	}

	/**
	 * 使用DES可逆算法以指定的密钥字符串对指定字符串进行加密，并返回加密后的字符串<br>
	 * <b>注意：</b>本方法每次都产生一个新的DES实例，如果密钥是固定的或者加密、解密都是成对使用，建议直接使用easymapping.util.DES
	 * 
	 * @param key 指定的密钥字符串
	 * @param input 需要加密的字符串
	 * @return
	 */
	public static final String desEncode(String key, String input) {
		DES des = new DES(key);
		return des.encode(input);
	}

	/**
	 * 使用DES可逆算法以指定的密钥字符串对指定字符串进行解密，并返回解密后的字符串<br>
	 * <b>注意：</b>本方法每次都产生一个新的DES实例，如果密钥是固定的或者加密、解密都是成对使用，建议直接使用easymapping.util.DES
	 * 
	 * @param key 指定的密钥字符串
	 * @param dest 需要加密的字符串
	 * @return
	 */
	public static final String desDecode(String key, String dest) {
		DES des = new DES(key);
		return des.decode(dest);
	}

	/**
	 * 使用DES可逆算法以指定的密钥字符串对指定字节数组进行加密，并返回加密后的字节数组<br>
	 * <b>注意：</b>本方法每次都产生一个新的DES实例，如果密钥是固定的或者加密、解密都是成对使用，建议直接使用easymapping.util.DES
	 * 
	 * @param key 指定的密钥字符串
	 * @param input 需要加密的字节数组
	 * @return
	 */
	public static final byte[] desEncode(String key, final byte[] input) {
		DES des = new DES(key);
		return des.encode(input);
	}

	/**
	 * 使用DES可逆算法以指定的密钥字符串对指定字节数组进行解密，并返回解密后的字节数组<br>
	 * <b>注意：</b>本方法每次都产生一个新的DES实例，如果密钥是固定的或者加密、解密都是成对使用，建议直接使用easymapping.util.DES
	 * 
	 * @param key 指定的密钥字符串
	 * @param dest 需要解密的字节数组
	 * @return
	 */
	public static final byte[] desDecode(String key, final byte[] dest) {
		DES des = new DES(key);
		return des.decode(dest);
	}

	/**
	 * 以指定的算法对指定的字节数组进行加密运算，并返回加密后的字节数组<br>
	 * <b>注意：</b>算法名称必须是MessageDigest对象支持的算法，否则可能引发异常
	 * 
	 * @param buf 指定的字节数组
	 * @param algorithm 算法名称
	 * @return
	 */
	public static final byte[] encode(final byte[] buf, String algorithm) {
		return getMessageDigest(algorithm).digest(buf);
	}

	/**
	 * 以指定的算法对指定的字符串进行加密运算，并返回加密后的十六进制的字符串值<br>
	 * <b>注意：</b>算法名称必须是MessageDigest对象支持的算法，否则可能引发异常
	 * 
	 * @param input 指定的字符串
	 * @param algorithm 算法名称，如“MD5”、“SHA”、“SHA-256”、“SHA-384”、“SHA-512”。
	 * @return
	 */
	public static final String encode(String input, String algorithm) {
		try {
			return bytes2Hex(getMessageDigest(algorithm).digest(input.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 将指定字节数组转为十六进制形式的字符串<br>
	 * 数组不能为null，否则引发空指针异常
	 * 
	 * @param bytes
	 * @return
	 */
	public static final String bytes2Hex(final byte[] bytes) {
		return bytes2Hex(bytes, 0, bytes.length);
	}

	/**
	 * 将指定字节数组转为十六进制形式的字符串<br>
	 * 数组不能为null，否则引发空指针异常
	 * 
	 * @param bytes 指定的字节数组
	 * @param start 开始转换的索引
	 * @param end 结束转换的索引
	 * @return
	 */
	public static final String bytes2Hex(final byte[] bytes, final int start, final int end) {
		final int length = end - start;
		if (start < 0 || end > bytes.length || length < 0) {
			// 如果结尾索引或截取长度大于数组长度
			throw new ArrayIndexOutOfBoundsException(end);
		}
		final char[] chars = new char[length << 1];
		int c = 0;
		for (int i = start; i < end; i++) {
			chars[c++] = Character.forDigit((bytes[i] >> 4) & 0xf, 16);
			chars[c++] = Character.forDigit(bytes[i] & 0xf, 16);
		}
		return new String(chars);
	}
}
