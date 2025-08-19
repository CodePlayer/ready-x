package me.codeplayer.util;

import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Condition;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RandomUtilTest implements WithAssertions {

	@Test
	public void getString() {
		assertThat(RandomUtil.getIntString(5))
				.hasSize(5)
				.has(new Condition<>(NumberUtil::isNumeric, "must be numeric"));

		assertThat(RandomUtil.getString("abcdefghijklmnopqrstuvwxyz", 6))
				.hasSize(6);
	}

	@Test
	public void nextInt() {
		// 测试范围随机数生成
		int min = 5;
		int max = 10;
		Set<Integer> results = new HashSet<>();
		for (int i = 0; i < 1000; i++) {
			int result = RandomUtil.nextInt(min, max);
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
			long result = RandomUtil.nextLong(min, max);
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
			double result = RandomUtil.nextDouble(min, max);
			assertThat(result).isGreaterThanOrEqualTo(min).isLessThan(max);
		}

		// 测试 [0,1) 范围的双精度浮点数生成
		for (int i = 0; i < 100; i++) {
			double result = RandomUtil.nextDouble();
			assertThat(result).isGreaterThanOrEqualTo(0.0).isLessThan(1.0);
		}
	}

	@Test
	public void nextBytes() {
		byte[] bytes = new byte[10];
		RandomUtil.nextBytes(bytes);
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
			String result = RandomUtil.getIntString(length);
			assertThat(result).hasSize(length);
			if (length > 0) {
				assertThat(result).matches("\\d*");
			}
		}
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 5, 10, 15, 20 })
	public void getIntStringWithVariousLengths(int length) {
		String result = RandomUtil.getIntString(length);
		assertThat(result).hasSize(length);
		if (length > 0) {
			assertThat(result).matches("\\d+");
		}
	}

	@Test
	public void nextFloat() {
		// 测试生成 [0,1) 范围的单精度浮点数
		for (int i = 0; i < 100; i++) {
			float result = RandomUtil.nextFloat();
			assertThat(result).isGreaterThanOrEqualTo(0.0f).isLessThan(1.0f);
		}
	}

	@Test
	public void nextLongNoParams() {
		// 测试生成任意长整型值
		Set<Long> results = new HashSet<>();
		for (int i = 0; i < 100; i++) {
			results.add(RandomUtil.nextLong());
		}
		assertThat(results.size()).isGreaterThan(90); // 几乎所有值都应该不同
	}

	@Test
	public void nextBoolean() {
		// 测试生成布尔值
		Set<Boolean> results = new HashSet<>();
		for (int i = 0; i < 100; i++) {
			results.add(RandomUtil.nextBoolean());
		}
		assertThat(results).contains(true, false);
	}

	@Test
	public void getChar() {
		char[] chars = { 'a', 'b', 'c', 'd', 'e' };
		Set<Character> results = new HashSet<>();
		for (int i = 0; i < 100; i++) {
			results.add(RandomUtil.getChar(chars));
		}
		assertThat(results).isSubsetOf('a', 'b', 'c', 'd', 'e');
	}

	@Test
	public void getCharFromEmptyCharArray() {
		char[] emptyChars = {};
		char result = RandomUtil.getChar(emptyChars);
		assertThat(result).isEqualTo(' ');
	}

	@Test
	public void getCharFromNullCharArray() {
		assertThrows(NullPointerException.class, () -> RandomUtil.getChar((char[]) null));
	}

	@Test
	public void getCharFromSingleCharArray() {
		char[] singleChar = { 'x' };
		for (int i = 0; i < 10; i++) {
			char result = RandomUtil.getChar(singleChar);
			assertThat(result).isEqualTo('x');
		}
	}

	@Test
	public void getCharFromString() {
		String chars = "abcde";
		Set<Character> results = new HashSet<>();
		for (int i = 0; i < 100; i++) {
			results.add(RandomUtil.getChar(chars));
		}
		assertThat(results).isSubsetOf('a', 'b', 'c', 'd', 'e');
	}

	@Test
	public void getCharFromEmptyString() {
		String emptyString = "";
		char result = RandomUtil.getChar(emptyString);
		assertThat(result).isEqualTo(' ');
	}

	@Test
	public void getCharFromNullString() {
		assertThrows(NullPointerException.class, () -> RandomUtil.getChar((String) null));
	}

	@Test
	public void getCharFromSingleCharString() {
		String singleChar = "x";
		for (int i = 0; i < 10; i++) {
			char result = RandomUtil.getChar(singleChar);
			assertThat(result).isEqualTo('x');
		}
	}

	@Test
	public void getStringFromCharArray() {
		char[] chars = { 'x', 'y', 'z' };
		String result = RandomUtil.getString(chars, 10);
		assertThat(result).hasSize(10);
		assertThat(result).matches("[xyz]*");
	}

	@Test
	public void getStringFromCharArrayWithZeroLength() {
		char[] chars = { 'a', 'b', 'c' };
		String result = RandomUtil.getString(chars, 0);
		assertThat(result).isEmpty();
	}

	@Test
	public void getStringFromEmptyCharArray() {
		char[] emptyChars = {};
		assertThrows(IllegalArgumentException.class, () -> RandomUtil.getString(emptyChars, 5));
	}

	@Test
	public void getStringFromNullCharArray() {
		assertThrows(NullPointerException.class, () -> RandomUtil.getString((char[]) null, 5));
	}

	@Test
	public void getStringFromSingleCharArray() {
		char[] singleChar = { 'a' };
		String result = RandomUtil.getString(singleChar, 5);
		assertThat(result).isEqualTo("aaaaa");
	}

	@Test
	public void getStringFromString() {
		String source = "xyz";
		String result = RandomUtil.getString(source, 10);
		assertThat(result).hasSize(10);
		assertThat(result).matches("[xyz]*");
	}

	@Test
	public void getStringFromStringWithZeroLength() {
		String source = "abc";
		String result = RandomUtil.getString(source, 0);
		assertThat(result).isEmpty();
	}

	@Test
	public void getStringFromEmptyString() {
		String emptyString = "";
		assertThrows(IllegalArgumentException.class, () -> RandomUtil.getString(emptyString, 5));
	}

	@Test
	public void getStringFromNullString() {
		assertThrows(NullPointerException.class, () -> RandomUtil.getString((String) null, 5));
	}

	@Test
	public void getStringFromSingleCharString() {
		String singleChar = "a";
		String result = RandomUtil.getString(singleChar, 5);
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
		int result = RandomUtil.nextInt(min, max);
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
		long result = RandomUtil.nextLong(min, max);
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
		double result = RandomUtil.nextDouble(min, max);
		assertThat(result).isGreaterThanOrEqualTo(min).isLessThan(max);
	}

	@Test
	public void deprecatedMethods() {
		// 测试已弃用的方法仍然可以正常工作
		int intResult = RandomUtil.getInt(1, 5);
		assertThat(intResult).isGreaterThanOrEqualTo(1).isLessThanOrEqualTo(5);

		float floatResult = RandomUtil.getFloat();
		assertThat(floatResult).isGreaterThanOrEqualTo(0.0f).isLessThan(1.0f);

		long longResult = RandomUtil.getLong();
		// 只需验证返回了一个long值

		boolean boolResult = RandomUtil.getBoolean();
		// 只需验证返回了一个boolean值
	}

}