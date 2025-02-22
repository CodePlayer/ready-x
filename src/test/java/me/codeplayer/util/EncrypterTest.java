package me.codeplayer.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EncrypterTest {

	@Test
	public void md5_Null_ShouldThrowException() {
		assertThrows(NullPointerException.class, () -> Encrypter.md5((String) null));
		assertThrows(NullPointerException.class, () -> Encrypter.md5((byte[]) null));
	}

	@Test
	public void md5_EmptyString_ShouldReturnZeroHash() {
		String result = Encrypter.md5("");
		assertEquals("d41d8cd98f00b204e9800998ecf8427e", result);
	}

	@Test
	public void md5_NormalInput_ShouldReturnCorrectHash() {
		String result = Encrypter.md5("hello");
		assertEquals("5d41402abc4b2a76b9719d911017c592", result);
	}

	@Test
	public void md5For16_NullInput_ShouldThrowException() {
		assertThrows(NullPointerException.class, () -> Encrypter.md5For16(null));
	}

	@Test
	public void md5For16_EmptyString_ShouldReturnZeroHash() {
		String result = Encrypter.md5For16("");
		assertEquals("8f00b204e9800998", result);
	}

	@Test
	public void md5For16_NormalInput_ShouldReturnCorrectHash() {
		String result = Encrypter.md5For16("hello");
		assertEquals("bc4b2a76b9719d91", result);
	}

	@Test
	public void md5_ByteArray_NullInput_ShouldThrowException() {
		assertThrows(NullPointerException.class, () -> Encrypter.md5((byte[]) null));
	}

	@Test
	public void md5_ByteArray_EmptyArray_ShouldReturnZeroHash() {
		byte[] result = Encrypter.md5(new byte[0]);
		assertEquals("d41d8cd98f00b204e9800998ecf8427e", Encrypter.bytes2Hex(result));
	}

	@Test
	public void md5_ByteArray_NormalInput_ShouldReturnCorrectHash() {
		byte[] result = Encrypter.md5("hello".getBytes());
		assertEquals("5d41402abc4b2a76b9719d911017c592", Encrypter.bytes2Hex(result));
	}

	@Test
	public void sha1_NullInput_ShouldThrowException() {
		assertThrows(NullPointerException.class, () -> Encrypter.sha1((String) null));
		assertThrows(NullPointerException.class, () -> Encrypter.sha1((byte[]) null));
	}

	@Test
	public void sha1_EmptyString_ShouldReturnZeroHash() {
		String result = Encrypter.sha1("");
		assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", result);
	}

	@Test
	public void sha1_NormalInput_ShouldReturnCorrectHash() {
		String result = Encrypter.sha1("hello");
		assertEquals("aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d", result);
	}

	@Test
	public void sha1_ByteArray_EmptyArray_ShouldReturnZeroHash() {
		byte[] result = Encrypter.sha1(new byte[0]);
		assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", Encrypter.bytes2Hex(result));
	}

	@Test
	public void sha1_ByteArray_NormalInput_ShouldReturnCorrectHash() {
		byte[] result = Encrypter.sha1("hello".getBytes());
		assertEquals("aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d", Encrypter.bytes2Hex(result));
	}

	@Test
	public void encode_NullInput_ShouldThrowException() {
		assertThrows(NullPointerException.class, () -> Encrypter.encode((byte[]) null, "MD5"));
	}

	@Test
	public void encode_InvalidAlgorithm_ShouldThrowException() {
		assertThrows(IllegalArgumentException.class, () -> Encrypter.encode("hello".getBytes(), "INVALID_ALGORITHM"));
	}

	@Test
	public void bytes2Hex_NullInput_ShouldThrowException() {
		assertThrows(NullPointerException.class, () -> Encrypter.bytes2Hex(null));
	}

	@Test
	public void bytes2Hex_EmptyArray_ShouldReturnEmptyString() {
		String result = Encrypter.bytes2Hex(new byte[0]);
		assertEquals("", result);
	}

	@Test
	public void bytes2Hex_NormalInput_ShouldReturnCorrectHex() {
		String result = Encrypter.bytes2Hex(new byte[] { 1, 2, 3 });
		assertEquals("010203", result);
	}

	@Test
	public void bytes2Hex_WithStartEnd_ShouldReturnCorrectHex() {
		String result = Encrypter.bytes2Hex(new byte[] { 1, 2, 3, 4 }, 1, 3);
		assertEquals("0203", result);
	}

	@Test
	public void bytes2Hex_WithStartEndAndDigits_ShouldReturnCorrectHex() {
		String result = Encrypter.bytes2Hex(new byte[] { 1, 2, 3, 4 }, 1, 3, StringX.digits);
		assertEquals("0203", result);
	}

}