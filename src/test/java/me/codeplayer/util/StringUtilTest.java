package me.codeplayer.util;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilTest implements WithAssertions {

	@Test
	public void unicode() {
		// 将字符串转为Unicode码
		assertThat(StringUtil.unicode("中国")).isEqualTo("\\u4E2D\\u56FD");
		assertThat(StringUtil.unicode("张三丰")).isEqualTo("\\u5F20\\u4E09\\u4E30");
		assertThat(StringUtil.unicode("ABCD")).isEqualTo("\\u0041\\u0042\\u0043\\u0044");
		assertThat(StringUtil.unicode("*/45sfga$")).isEqualTo("\\u002A\\u002F\\u0034\\u0035\\u0073\\u0066\\u0067\\u0061\\u0024");
	}

	@Test
	public void fastUnicode() {
		// 将字符串转为Unicode码，与unicode()相比，性能更优异
		assertThat(StringUtil.fastUnicode("中国")).isEqualTo("\\u4E2D\\u56FD");
		assertThat(StringUtil.fastUnicode("张三丰")).isEqualTo("\\u5F20\\u4E09\\u4E30");
		assertThat(StringUtil.fastUnicode("ABCD")).isEqualTo("\\u0041\\u0042\\u0043\\u0044");
		assertThat(StringUtil.fastUnicode("*/45sfga$")).isEqualTo("\\u002A\\u002F\\u0034\\u0035\\u0073\\u0066\\u0067\\u0061\\u0024");
	}

	@Test
	public void replaceChars() {
		// 将第6个及其后的字符替换为*
		assertThat(StringUtil.replaceChars("helloworld", '*', 5))
				.isEqualTo("hello*****");

		// 将第6~8的字符替换为#
		assertThat(StringUtil.replaceChars("helloworld", '#', 5, 8))
				.isEqualTo("hello###ld");

		// 将最后六位字符替换为*
		assertThat(StringUtil.replaceChars("511622199141566456", '*', -6))
				.isEqualTo("511622199141******");
		// 将倒数第3~6的字符替换为*
		assertThat(StringUtil.replaceChars("511622199141566456", '*', -6, -2))
				.isEqualTo("511622199141****56");
		// 将倒数第3~7的字符替换为*
		assertThat(StringUtil.replaceChars("511622199141566456", '*', -6, -1))
				.isEqualTo("511622199141*****6");
	}

	@Test
	public void replaceSubstring() {
		// 将第3个及其后的字符替换为"***"
		assertThat(StringUtil.replaceSubstring("helloworld", "***", 2))
				.isEqualTo("he***");

		// 将第3~5个字符替换为"***"
		assertThat(StringUtil.replaceSubstring("helloworld", "***", 2, 5))
				.isEqualTo("he***world");

		// 将第3~倒数第二个字符替换为"***"
		assertThat(StringUtil.replaceSubstring("helloworld", "***", 2, -1))
				.isEqualTo("he***d");
	}

	@Test
	public void limitChars() {
		// 限制字符串的长度最大为15个字符，并默认附加三个句点表示省略(长度不足15，直接返回原字符串)
		assertThat(StringUtil.limitChars("张三李四王五", 15))
				.isEqualTo("张三李四王五");
		// 限制字符串的长度最大为7个字符，并默认附加三个句点表示省略
		assertThat(StringUtil.limitChars("abcdefghijkmln", 7))
				.isEqualTo("abcdefg...");
		// 限制字符串的长度最大为7个字符，附加指定的后缀
		assertThat(StringUtil.limitChars("abcdefghijkmln", 7, "~~~"))
				.isEqualTo("abcdefg~~~");
		// 限制字符串的长度最大为7个字符，不附加后缀
		assertThat(StringUtil.limitChars("abcdefghijkmln", 7, ""))
				.isEqualTo("abcdefg");
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

	@Test
	public void isBlank() {
		assertTrue(StringUtil.isBlank(""));
		assertTrue(StringUtil.isBlank(null));
		assertTrue(StringUtil.isBlank("   "));
		assertFalse(StringUtil.isBlank(" y  "));
	}

	@Test
	public void notBlank() {
		assertFalse(StringUtil.notBlank(""));
		assertFalse(StringUtil.notBlank(null));
		assertFalse(StringUtil.notBlank("   "));
		assertTrue(StringUtil.notBlank(" x  "));
	}

	@Test
	public void concat() {
		assertEquals("ab", StringUtil.concat("a", "b"));
		assertSame("b", StringUtil.concat(null, "b"));
		assertSame("a", StringUtil.concat("a", null));

		assertEquals("abc", StringUtil.concat("a", "b", "c"));
		assertEquals("ac", StringUtil.concat("a", null, "c"));

		assertEquals("abcde", StringUtil.concats("a", "b", "c", "d", "e"));
		assertEquals("abd", StringUtil.concats("a", "b", null, "d", null));
	}

	@Test
	public void escape() {
		String str = "\"Hello'World\\\"";
		char[] escapedChars = "'\"".toCharArray();
		char escapeChar = '\\';
		String escaped = StringUtil.escape(str, escapeChar, escapedChars);
		assertEquals("\\\"Hello\\'World\\\\\\\"", escaped);

		assertEquals(str, StringUtil.unescape(escaped, escapeChar, escapedChars));
	}

	@Test
	public void containsWord() {
		assertTrue(StringUtil.containsWord("edit view submit", "view", " "));
		assertFalse(StringUtil.containsWord("edit,view,submit", "view", " "));

		assertTrue(StringUtil.containsWord("edit,view submit", "submit", ", "));
		assertFalse(StringUtil.containsWord("edit,viewsubmit ", "submit", ", "));
	}

	@Test
	public void trimAll() {
		assertEquals("", StringUtil.trimAll(null));
		assertEquals("", StringUtil.trimAll("    "));
		assertEquals("codeplayer", StringUtil.trimAll("  code player"));
		assertEquals("codeplayer", StringUtil.trimAll("code  player  "));
		assertEquals("codeplayer", StringUtil.trimAll(" code player "));
	}

}