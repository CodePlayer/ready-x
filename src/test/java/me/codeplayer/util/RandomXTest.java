package me.codeplayer.util;

import org.assertj.core.api.Condition;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomXTest implements WithAssertions {

	@Test
	public void getString() {
		assertThat(RandomX.getIntString(0)).isEqualTo("");
		for (int i = 1; i <= 30; i++) {
			assertThat(RandomX.getIntString(i))
					.hasSize(i)
					.has(new Condition<>(NumberX::isNumeric, "must be numeric"));
		}

		assertThat(RandomX.getString("abcdefghijklmnopqrstuvwxyz", 6))
				.hasSize(6);
	}

	@Test
	public void getInt_NormalRange_ShouldReturnInRange() {
		int min = 10;
		int max = 20;
		for (int i = 0; i < 1000; i++) {
			int result = RandomX.getInt(min, max);
			assertTrue(result >= min && result <= max, "Result should be within range");
		}
	}

	@Test
	public void getInt_SingleValueRange_ShouldReturnTheValue() {
		int value = 5;
		for (int i = 0; i < 1000; i++) {
			int result = RandomX.getInt(value, value);
			assertTrue(result == value, "Result should be the value itself");
		}
	}

	@Test
	public void getInt_NegativeRange_ShouldReturnInRange() {
		int min = -20;
		int max = -10;
		for (int i = 0; i < 1000; i++) {
			int result = RandomX.getInt(min, max);
			assertTrue(result >= min && result <= max, "Result should be within negative range");
		}
	}

	@Test
	public void getInt_ZeroAndPositiveRange_ShouldReturnInRange() {
		int min = 0;
		int max = 10;
		for (int i = 0; i < 1000; i++) {
			int result = RandomX.getInt(min, max);
			assertTrue(result >= min && result <= max, "Result should be within zero and positive range");
		}
	}

	@Test
	public void getInt_BoundaryValues_ShouldIncludeMinAndMax() {
		int min = 1;
		int max = 10;
		boolean minFound = false;
		boolean maxFound = false;
		for (int i = 0; i < 10000; i++) {
			int result = RandomX.getInt(min, max);
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
		assertEquals(' ', RandomX.getChar(new char[] {}));
	}

	@Test
	public void getChar_SingleChar_ShouldReturnChar() {
		assertEquals('a', RandomX.getChar(new char[] { 'a' }));
	}

	@Test
	public void getChar_MultipleChars_ShouldReturnRandomChar() {
		char[] chars = { 'a', 'b', 'c' };
		char result = RandomX.getChar(chars);
		assertTrue(result == 'a' || result == 'b' || result == 'c', "Result should be one of the chars in the array");
	}

	@Test
	public void getChar_EmptyString_ShouldReturnSpace() {
		assertEquals(' ', RandomX.getChar(""));
	}

	@Test
	public void getChar_SingleCharString_ShouldReturnChar() {
		assertEquals('a', RandomX.getChar("a"));
	}

	@Test
	public void getChar_MultipleCharsString_ShouldReturnRandomChar() {
		String str = "abc";
		char result = RandomX.getChar(str);
		assertTrue(result == 'a' || result == 'b' || result == 'c', "Result should be one of the chars in the string");
	}

	@Test
	public void getString_LengthLessThanOne_ShouldReturnEmptyString() {
		assertEquals("", RandomX.getString(new char[] { 'a', 'b', 'c' }, 0));
	}

	@Test
	public void getString_SingleChar_ShouldReturnRepeatedChar() {
		assertEquals("aaa", RandomX.getString(new char[] { 'a' }, 3));
	}

	@Test
	public void getString_MultipleChars_ShouldReturnRandomString() {
		String result = RandomX.getString(new char[] { 'a', 'b', 'c' }, 5);
		assertEquals(5, result.length());
		for (char c : result.toCharArray()) {
			assertTrue(c == 'a' || c == 'b' || c == 'c', "Result should only contain chars from the array");
		}
	}

	@Test
	public void getString_LengthLessThanOne_ShouldReturnEmptyStringForString() {
		assertEquals("", RandomX.getString("abc", 0));
	}

	@Test
	public void getString_SingleCharString_ShouldReturnRepeatedChar() {
		assertEquals("aaa", RandomX.getString("a", 3));
	}

	@Test
	public void getString_MultipleCharsString_ShouldReturnRandomString() {
		String result = RandomX.getString("abc", 5);
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
			float result = RandomX.getFloat();
			assertTrue(result >= 0.0f && result < 1.0f, "Result should be between 0.0 and 1.0");
			if (result == prev) {
				same++;
			}
			prev = result;
		}
		assertTrue(same < 50);
	}

}