package me.codeplayer.util;

import org.assertj.core.api.Condition;
import org.assertj.core.api.WithAssertions;
import org.junit.Assert;
import org.junit.Test;

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
			Assert.assertTrue("Result should be within range", result >= min && result <= max);
		}
	}

	@Test
	public void getInt_SingleValueRange_ShouldReturnTheValue() {
		int value = 5;
		for (int i = 0; i < 1000; i++) {
			int result = RandomX.getInt(value, value);
			Assert.assertTrue("Result should be the value itself", result == value);
		}
	}

	@Test
	public void getInt_NegativeRange_ShouldReturnInRange() {
		int min = -20;
		int max = -10;
		for (int i = 0; i < 1000; i++) {
			int result = RandomX.getInt(min, max);
			Assert.assertTrue("Result should be within negative range", result >= min && result <= max);
		}
	}

	@Test
	public void getInt_ZeroAndPositiveRange_ShouldReturnInRange() {
		int min = 0;
		int max = 10;
		for (int i = 0; i < 1000; i++) {
			int result = RandomX.getInt(min, max);
			Assert.assertTrue("Result should be within zero and positive range", result >= min && result <= max);
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
		Assert.assertTrue("Minimum value should be found", minFound);
		Assert.assertTrue("Maximum value should be found", maxFound);
	}

	@Test
	public void getChar_EmptyArray_ShouldReturnSpace() {
		Assert.assertEquals(' ', RandomX.getChar(new char[] {}));
	}

	@Test
	public void getChar_SingleChar_ShouldReturnChar() {
		Assert.assertEquals('a', RandomX.getChar(new char[] { 'a' }));
	}

	@Test
	public void getChar_MultipleChars_ShouldReturnRandomChar() {
		char[] chars = { 'a', 'b', 'c' };
		char result = RandomX.getChar(chars);
		Assert.assertTrue("Result should be one of the chars in the array", result == 'a' || result == 'b' || result == 'c');
	}

	@Test
	public void getChar_EmptyString_ShouldReturnSpace() {
		Assert.assertEquals(' ', RandomX.getChar(""));
	}

	@Test
	public void getChar_SingleCharString_ShouldReturnChar() {
		Assert.assertEquals('a', RandomX.getChar("a"));
	}

	@Test
	public void getChar_MultipleCharsString_ShouldReturnRandomChar() {
		String str = "abc";
		char result = RandomX.getChar(str);
		Assert.assertTrue("Result should be one of the chars in the string", result == 'a' || result == 'b' || result == 'c');
	}

	@Test
	public void getString_LengthLessThanOne_ShouldReturnEmptyString() {
		Assert.assertEquals("", RandomX.getString(new char[] { 'a', 'b', 'c' }, 0));
	}

	@Test
	public void getString_SingleChar_ShouldReturnRepeatedChar() {
		Assert.assertEquals("aaa", RandomX.getString(new char[] { 'a' }, 3));
	}

	@Test
	public void getString_MultipleChars_ShouldReturnRandomString() {
		String result = RandomX.getString(new char[] { 'a', 'b', 'c' }, 5);
		Assert.assertEquals(5, result.length());
		for (char c : result.toCharArray()) {
			Assert.assertTrue("Result should only contain chars from the array", c == 'a' || c == 'b' || c == 'c');
		}
	}

	@Test
	public void getString_LengthLessThanOne_ShouldReturnEmptyStringForString() {
		Assert.assertEquals("", RandomX.getString("abc", 0));
	}

	@Test
	public void getString_SingleCharString_ShouldReturnRepeatedChar() {
		Assert.assertEquals("aaa", RandomX.getString("a", 3));
	}

	@Test
	public void getString_MultipleCharsString_ShouldReturnRandomString() {
		String result = RandomX.getString("abc", 5);
		Assert.assertEquals(5, result.length());
		for (char c : result.toCharArray()) {
			Assert.assertTrue("Result should only contain chars from the string", c == 'a' || c == 'b' || c == 'c');
		}
	}

}