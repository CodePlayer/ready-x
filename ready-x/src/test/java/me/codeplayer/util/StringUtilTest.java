package me.codeplayer.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilTest {

	@Test
	public void unicode() {
		// 将字符串转为Unicode码
		assertEquals("\\u4E2D\\u56FD", StringUtil.unicode("中国"));
		assertEquals("\\u5F20\\u4E09\\u4E30", StringUtil.unicode("张三丰"));
		assertEquals("\\u0041\\u0042\\u0043\\u0044", StringUtil.unicode("ABCD"));
		assertEquals("\\u002A\\u002F\\u0034\\u0035\\u0073\\u0066\\u0067\\u0061\\u0024", StringUtil.unicode("*/45sfga$"));
	}

	@Test
	public void fastUnicode() {
		// 将字符串转为Unicode码，与unicode()相比，性能更优异
		assertEquals("\\u4E2D\\u56FD", StringUtil.fastUnicode("中国"));
		assertEquals("\\u5F20\\u4E09\\u4E30", StringUtil.fastUnicode("张三丰"));
		assertEquals("\\u0041\\u0042\\u0043\\u0044", StringUtil.fastUnicode("ABCD"));
		assertEquals("\\u002A\\u002F\\u0034\\u0035\\u0073\\u0066\\u0067\\u0061\\u0024", StringUtil.fastUnicode("*/45sfga$"));
	}

	@Test
	public void replaceChars() {
		// 将第6个及其后的字符替换为*
		assertEquals("hello*****", StringUtil.replaceChars("helloworld", '*', 5));
		// 将第6~8的字符替换为#
		assertEquals("hello###ld", StringUtil.replaceChars("helloworld", '#', 5, 8));
		// 将最后六位字符替换为*
		assertEquals("511622199141******", StringUtil.replaceChars("511622199141566456", '*', -6));
		// 将倒数第3~6的字符替换为*
		assertEquals("511622199141****56", StringUtil.replaceChars("511622199141566456", '*', -6, -2));
	}

	@Test
	public void replaceSubstring() {
		// 将第3个及其后的字符替换为"***"
		assertEquals("he***", StringUtil.replaceSubstring("helloworld", "***", 2));
		// 将第3~5个字符替换为"***"
		assertEquals("he***world", StringUtil.replaceSubstring("helloworld", "***", 2, 5));
		// 将第3~倒数第二个字符替换为"***"
		assertEquals("he***d", StringUtil.replaceSubstring("helloworld", "***", 2, -1));
	}

	@Test
	public void limitChars() {
		// 限制字符串的长度最大为15个字符，并默认附加三个句点表示省略(长度不足15，直接返回原字符串)
		assertEquals("张三李四王五", StringUtil.limitChars("张三李四王五", 15));
		// 限制字符串的长度最大为7个字符，并默认附加三个句点表示省略
		assertEquals("abcdefg...", StringUtil.limitChars("abcdefghijkmln", 7));
		// 限制字符串的长度最大为7个字符，附加指定的后缀
		assertEquals("abcdefg~~~", StringUtil.limitChars("abcdefghijkmln", 7, "~~~"));
		// 限制字符串的长度最大为7个字符，不附加后缀
		assertEquals("abcdefg", StringUtil.limitChars("abcdefghijkmln", 7, ""));
	}

	@Test
	public void isEmpty() {
		assertTrue(StringUtil.isEmpty(""));
		assertTrue(StringUtil.isEmpty(null));
		assertFalse(StringUtil.isEmpty("   "));
	}

	@Test
	public void notEmpty() {
		assertFalse(StringUtil.notEmpty(""));
		assertFalse(StringUtil.notEmpty(null));
		assertTrue(StringUtil.notEmpty("   "));
	}
}
