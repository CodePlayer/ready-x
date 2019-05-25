package me.codeplayer.util;

import java.io.*;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * 可逆的数据算法工具，实现DES加密算法，利用指定的密钥对字符串或字节数组进行加密或解密
 * 
 * @author Ready
 * @date 2012-11-30
 */
public class DES {

	Key key;

	/**
	 * 利用指定的字符串密钥构造一个DES工具实例
	 * 
	 * @param key
	 */
	public DES(String key) {
		setKey(key);
	}

	/**
	 * 利用指定的字节数组密钥构造一个DES工具实例
	 * 
	 * @param key
	 */
	public DES(byte[] key) {
		setKey(key);
	}

	/**
	 * 利用默认的密钥构造一个DES工具实例
	 */
	public DES() {
		setKey("me.codeplayer");
	}

	/**
	 * 根据字符串参数生成KEY
	 */
	public void setKey(String strKey) {
		try {
			setKey(strKey.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 根据直接数组参数生成KEY
	 * 
	 * @param byteKey
	 */
	public void setKey(byte[] byteKey) {
		try {
			this.key = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(byteKey));
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 输入指定的明文，使用指定的密钥进行加密，并返回加密后的字符串
	 * 
	 * @param plaintext 指定的明文
	 * @param encoding 指定的字符编码，例如"UTF-8"、"GBK"
	 * @return
	 */
	public String encode(String plaintext, String encoding) {
		try {
			return new String(java.util.Base64.getEncoder().encode(encode(plaintext.getBytes(encoding))));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 输入指定的明文，使用指定的密钥进行加密，并返回加密后的字符串<br>
	 * 内部使用UTF-8编码进行处理
	 * 
	 * @param plaintext 指定的明文
	 * @return
	 */
	public String encode(String plaintext) {
		return encode(plaintext, "UTF-8");
	}

	/**
	 * 解密指定的密文字符串，并以明文方式返回
	 * 
	 * @param ciphertext 指定的密文字符串
	 * @param encoding 指定的字符编码，例如"UTF-8"、"GBK"
	 * @return
	 */
	public String decode(String ciphertext, String encoding) {
		try {
			return new String(decode(java.util.Base64.getDecoder().decode(ciphertext.getBytes(encoding))), encoding);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 解密指定的密文字符串，并以明文方式返回<br>
	 * 内部使用UTF-8编码进行处理
	 * 
	 * @param ciphertext 指定的密文字符串
	 * @return
	 */
	public String decode(String ciphertext) {
		return decode(ciphertext, "UTF-8");
	}

	/**
	 * 加密指定的字节数组，并返回加密后的字节数组
	 * 
	 * @param srcBytes
	 * @return
	 */
	public byte[] encode(byte[] srcBytes) {
		return process(srcBytes, true);
	}

	/**
	 * 解密以byte[]密文输入,以byte[]明文输出
	 * 
	 * @param srcBytes
	 * @return
	 */
	public byte[] decode(byte[] srcBytes) {
		return process(srcBytes, false);
	}

	/**
	 * 加密或解密处理指定的字节数组
	 * 
	 * @param srcBytes 直接数组
	 * @param encrypt 如果为true表示加密，否则表示解密。
	 * @return
	 */
	public byte[] process(byte[] srcBytes, boolean encrypt) {
		try {
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(srcBytes);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
}