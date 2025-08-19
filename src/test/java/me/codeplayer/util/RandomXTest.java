package me.codeplayer.util;

import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Condition;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class RandomXTest implements WithAssertions {

	@Test
	public void getString() {
		assertThat(RandomX.getIntString(0)).isEqualTo("");
		assertThat(RandomX.getIntString(-1)).isEqualTo("");
		for (int i = 1; i <= 30; i++) {
			assertThat(RandomX.getIntString(i))
					.hasSize(i)
					.has(new Condition<>(NumberX::isNumeric, "must be numeric"));
		}

		assertThat(RandomX.nextString("abcdefghijklmnopqrstuvwxyz", 6))
				.hasSize(6);
	}

	@Test
	public void getInt_NormalRange_ShouldReturnInRange() {
		int min = 10;
		int max = 20;
		for (int i = 0; i < 1000; i++) {
			int result = RandomX.nextInt(min, max);
			assertTrue(result >= min && result < max, "Result should be within range");
		}
	}

	@Test
	public void getInt_SingleValueRange_ShouldReturnTheValue() {
		int value = 5;
		for (int i = 0; i < 1000; i++) {
			int result = RandomX.nextInt(value, value + 1);
			assertEquals(value, result, "Result should be the value itself");
		}
	}

	@Test
	public void getInt_NegativeRange_ShouldReturnInRange() {
		int min = -20;
		int max = -10;
		for (int i = 0; i < 1000; i++) {
			int result = RandomX.nextInt(min, max);
			assertTrue(result >= min && result < max, "Result should be within negative range");
		}
	}

	@Test
	public void getInt_ZeroAndPositiveRange_ShouldReturnInRange() {
		int min = 0;
		int max = 10;
		for (int i = 0; i < 1000; i++) {
			int result = RandomX.nextInt(min, max);
			assertTrue(result >= min && result < max, "Result should be within zero and positive range");
		}
	}

	@Test
	public void getInt_BoundaryValues_ShouldIncludeMinAndMax() {
		final int min = 1, max = 10;
		boolean minFound = false;
		boolean maxFound = false;
		for (int i = 0; i < 10000; i++) {
			int result = RandomX.nextInt(min, max + 1);
			if (result == min) {
				minFound = true;
			}
			if (result == max) {
				maxFound = true;
			}
			if (minFound && maxFound) {
				break;
			}
		}
		assertTrue(minFound, "Minimum value should be found");
		assertTrue(maxFound, "Maximum value should be found");
	}

	@Test
	public void getChar_EmptyArray_ShouldReturnSpace() {
		assertEquals(' ', RandomX.nextChar(new char[] {}));
	}

	@Test
	public void getChar_SingleChar_ShouldReturnChar() {
		assertEquals('a', RandomX.nextChar(new char[] { 'a' }));
	}

	@Test
	public void getChar_MultipleChars_ShouldReturnRandomChar() {
		char[] chars = { 'a', 'b', 'c' };
		char result = RandomX.nextChar(chars);
		assertTrue(result == 'a' || result == 'b' || result == 'c', "Result should be one of the chars in the array");
	}

	@Test
	public void getChar_EmptyString_ShouldReturnSpace() {
		assertEquals(' ', RandomX.nextChar(""));
	}

	@Test
	public void getChar_SingleCharString_ShouldReturnChar() {
		assertEquals('a', RandomX.nextChar("a"));
	}

	@Test
	public void getChar_MultipleCharsString_ShouldReturnRandomChar() {
		String str = "abc";
		char result = RandomX.nextChar(str);
		assertTrue(result == 'a' || result == 'b' || result == 'c', "Result should be one of the chars in the string");
	}

	@Test
	public void getString_LengthLessThanOne_ShouldReturnEmptyString() {
		assertEquals("", RandomX.nextString(new char[] { 'a', 'b', 'c' }, 0));
	}

	@Test
	public void getString_SingleChar_ShouldReturnRepeatedChar() {
		assertEquals("aaa", RandomX.nextString(new char[] { 'a' }, 3));
	}

	@Test
	public void getString_MultipleChars_ShouldReturnRandomString() {
		String result = RandomX.nextString(new char[] { 'a', 'b', 'c' }, 5);
		assertEquals(5, result.length());
		for (char c : result.toCharArray()) {
			assertTrue(c == 'a' || c == 'b' || c == 'c', "Result should only contain chars from the array");
		}
	}

	@Test
	public void getString_LengthLessThanOne_ShouldReturnEmptyStringForString() {
		assertEquals("", RandomX.nextString("abc", 0));
	}

	@Test
	public void getString_SingleCharString_ShouldReturnRepeatedChar() {
		assertEquals("aaa", RandomX.nextString("a", 3));
	}

	@Test
	public void getString_MultipleCharsString_ShouldReturnRandomString() {
		String result = RandomX.nextString("abc", 5);
		assertEquals(5, result.length());
		for (char c : result.toCharArray()) {
			assertTrue(c == 'a' || c == 'b' || c == 'c', "Result should only contain chars from the string");
		}
	}

	@Test
	public void getFloat_ShouldReturnFloatBetweenZeroAndOne() {
		int same = 0;
		float prev = 0f;
		for (int i = 0; i < 1000; i++) {
			float result = RandomX.nextFloat();
			assertTrue(result >= 0.0f && result < 1.0f, "Result should be between 0.0 and 1.0");
			if (result == prev) {
				same++;
			}
			prev = result;
		}
		assertTrue(same < 50);
	}

	@Test
	public void nextInt() {
		// 测试范围随机数生成
		int min = 5;
		int max = 10;
		Set<Integer> results = new HashSet<>();
		for (int i = 0; i < 1000; i++) {
			int result = RandomX.nextInt(min, max);
			assertThat(result).isGreaterThanOrEqualTo(min).isLessThan(max);
			results.add(result);
		}
		// 验证确实生成了范围内的不同值
		assertThat(results).hasSize(max - min);
	}

	@Test
	public void nextLong() {
		// 测试范围长整型随机数生成
		long min = 100L;
		long max = 200L;
		Set<Long> results = new HashSet<>();
		for (int i = 0; i < 1000; i++) {
			long result = RandomX.nextLong(min, max);
			assertThat(result).isGreaterThanOrEqualTo(min).isLessThan(max);
			results.add(result);
		}
		// 验证确实生成了范围内的不同值
		assertThat(results.size()).isGreaterThan(1);
	}

	@Test
	public void nextDouble() {
		// 测试范围双精度浮点数生成
		double min = 1.5;
		double max = 5.8;
		for (int i = 0; i < 100; i++) {
			double result = RandomX.nextDouble(min, max);
			assertThat(result).isGreaterThanOrEqualTo(min).isLessThan(max);
		}

		// 测试 [0,1) 范围的双精度浮点数生成
		for (int i = 0; i < 100; i++) {
			double result = RandomX.nextDouble();
			assertThat(result).isGreaterThanOrEqualTo(0.0).isLessThan(1.0);
		}
	}

	@Test
	public void nextBytes() {
		byte[] bytes = new byte[10];
		RandomX.nextBytes(bytes);
		// 验证数组已被填充（虽然理论上可能全为0，但概率极低）
		boolean allZero = true;
		for (byte b : bytes) {
			if (b != 0) {
				allZero = false;
				break;
			}
		}
		// 这个断言可能偶尔失败，但概率极低
		assertThat(allZero).isFalse();
	}

	@Test
	public void getIntString() {
		// 测试生成指定长度的数字字符串
		for (int length = 0; length <= 20; length++) {
			String result = RandomX.getIntString(length);
			assertThat(result).hasSize(length);
			if (length > 0) {
				assertThat(result).matches("\\d*");
			}
		}
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 5, 10, 15, 20 })
	public void getIntStringWithVariousLengths(int length) {
		String result = RandomX.getIntString(length);
		assertThat(result).hasSize(length);
		if (length > 0) {
			assertThat(result).matches("\\d+");
		}
	}

	@Test
	public void nextFloat() {
		// 测试生成 [0,1) 范围的单精度浮点数
		for (int i = 0; i < 100; i++) {
			float result = RandomX.nextFloat();
			assertThat(result).isGreaterThanOrEqualTo(0.0f).isLessThan(1.0f);
		}
	}

	@Test
	public void nextLongNoParams() {
		// 测试生成任意长整型值
		Set<Long> results = new HashSet<>();
		for (int i = 0; i < 100; i++) {
			results.add(RandomX.nextLong());
		}
		assertThat(results.size()).isGreaterThan(90); // 几乎所有值都应该不同
	}

	@Test
	public void nextBoolean() {
		// 测试生成布尔值
		Set<Boolean> results = new HashSet<>();
		for (int i = 0; i < 100; i++) {
			results.add(RandomX.nextBoolean());
		}
		assertThat(results).contains(true, false);
	}

	@Test
	public void getChar() {
		char[] chars = { 'a', 'b', 'c', 'd', 'e' };
		Set<Character> results = new HashSet<>();
		for (int i = 0; i < 100; i++) {
			results.add(RandomX.nextChar(chars));
		}
		assertThat(results).isSubsetOf('a', 'b', 'c', 'd', 'e');
	}

	@Test
	public void getCharFromEmptyCharArray() {
		char[] emptyChars = {};
		char result = RandomX.nextChar(emptyChars);
		assertThat(result).isEqualTo(' ');
	}

	@Test
	public void getCharFromNullCharArray() {
		assertThrows(NullPointerException.class, () -> RandomX.nextChar((char[]) null));
	}

	@Test
	public void getCharFromSingleCharArray() {
		char[] singleChar = { 'x' };
		for (int i = 0; i < 10; i++) {
			char result = RandomX.nextChar(singleChar);
			assertThat(result).isEqualTo('x');
		}
	}

	@Test
	public void getCharFromString() {
		String chars = "abcde";
		Set<Character> results = new HashSet<>();
		for (int i = 0; i < 100; i++) {
			results.add(RandomX.nextChar(chars));
		}
		assertThat(results).isSubsetOf('a', 'b', 'c', 'd', 'e');
	}

	@Test
	public void getCharFromEmptyString() {
		String emptyString = "";
		char result = RandomX.nextChar(emptyString);
		assertThat(result).isEqualTo(' ');
	}

	@Test
	public void getCharFromNullString() {
		assertThrows(NullPointerException.class, () -> RandomX.nextChar((String) null));
	}

	@Test
	public void getCharFromSingleCharString() {
		String singleChar = "x";
		for (int i = 0; i < 10; i++) {
			char result = RandomX.nextChar(singleChar);
			assertThat(result).isEqualTo('x');
		}
	}

	@Test
	public void getStringFromCharArray() {
		char[] chars = { 'x', 'y', 'z' };
		String result = RandomX.nextString(chars, 10);
		assertThat(result).hasSize(10);
		assertThat(result).matches("[xyz]*");
	}

	@Test
	public void getStringFromCharArrayWithZeroLength() {
		char[] chars = { 'a', 'b', 'c' };
		String result = RandomX.nextString(chars, 0);
		assertThat(result).isEmpty();
	}

	@Test
	public void getStringFromEmptyCharArray() {
		char[] emptyChars = {};
		assertThrows(IllegalArgumentException.class, () -> RandomX.nextString(emptyChars, 5));
	}

	@Test
	public void getStringFromNullCharArray() {
		assertThrows(NullPointerException.class, () -> RandomX.nextString((char[]) null, 5));
	}

	@Test
	public void getStringFromSingleCharArray() {
		char[] singleChar = { 'a' };
		String result = RandomX.nextString(singleChar, 5);
		assertThat(result).isEqualTo("aaaaa");
	}

	@Test
	public void getStringFromString() {
		String source = "xyz";
		String result = RandomX.nextString(source, 10);
		assertThat(result).hasSize(10);
		assertThat(result).matches("[xyz]*");
	}

	@Test
	public void getStringFromStringWithZeroLength() {
		String source = "abc";
		String result = RandomX.nextString(source, 0);
		assertThat(result).isEmpty();
	}

	@Test
	public void getStringFromEmptyString() {
		String emptyString = "";
		assertThrows(IllegalArgumentException.class, () -> RandomX.nextString(emptyString, 5));
	}

	@Test
	public void getStringFromNullString() {
		assertThrows(NullPointerException.class, () -> RandomX.nextString((String) null, 5));
	}

	@Test
	public void getStringFromSingleCharString() {
		String singleChar = "a";
		String result = RandomX.nextString(singleChar, 5);
		assertThat(result).isEqualTo("aaaaa");
	}

	@ParameterizedTest
	@CsvSource({
			"1, 5",
			"0, 10",
			"-5, -1",
			"100, 200"
	})
	public void nextIntWithValidRange(int min, int max) {
		int result = RandomX.nextInt(min, max);
		assertThat(result).isGreaterThanOrEqualTo(min).isLessThan(max);
	}

	@ParameterizedTest
	@CsvSource({
			"1, 5",
			"0, 10",
			"-5, -1",
			"100, 200"
	})
	public void nextLongWithValidRange(long min, long max) {
		long result = RandomX.nextLong(min, max);
		assertThat(result).isGreaterThanOrEqualTo(min).isLessThan(max);
	}

	@ParameterizedTest
	@CsvSource({
			"1.0, 5.0",
			"0.0, 10.0",
			"-5.0, -1.0",
			"100.0, 200.0"
	})
	public void nextDoubleWithValidRange(double min, double max) {
		double result = RandomX.nextDouble(min, max);
		assertThat(result).isGreaterThanOrEqualTo(min).isLessThan(max);
	}

}