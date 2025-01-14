package me.codeplayer.util;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringXTest implements WithAssertions {

	@Test
	public void unicode() {
		// 将字符串转为Unicode码
		assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> StringX.unicode(null));
		assertThat(StringX.unicode("中国")).isEqualTo("\\u4E2D\\u56FD");
		assertThat(StringX.unicode("张三丰")).isEqualTo("\\u5F20\\u4E09\\u4E30");
		assertThat(StringX.unicode("ABCD")).isEqualTo("\\u0041\\u0042\\u0043\\u0044");
		assertThat(StringX.unicode("*/45sfga$")).isEqualTo("\\u002A\\u002F\\u0034\\u0035\\u0073\\u0066\\u0067\\u0061\\u0024");
	}

	@Test
	public void fastUnicode() {
		// 将字符串转为Unicode码，与unicode()相比，性能更优异
		assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> StringX.fastUnicode(null));
		assertThat(StringX.fastUnicode("中国")).isEqualTo("\\u4E2D\\u56FD");
		assertThat(StringX.fastUnicode("张三丰")).isEqualTo("\\u5F20\\u4E09\\u4E30");
		assertThat(StringX.fastUnicode("ABCD")).isEqualTo("\\u0041\\u0042\\u0043\\u0044");
		assertThat(StringX.fastUnicode("*/45sfga$")).isEqualTo("\\u002A\\u002F\\u0034\\u0035\\u0073\\u0066\\u0067\\u0061\\u0024");
	}

	@Test
	public void replaceChars() {
		assertThat(StringX.replaceChars(null, '*', 5))
				.isNull();
		assertThat(StringX.replaceChars("", '*', 5))
				.isEqualTo("");
		assertThat(StringX.replaceChars("abc", '*', 5))
				.isEqualTo("abc");
		// 将第6个及其后的字符替换为*
		assertThat(StringX.replaceChars("helloworld", '*', 5))
				.isEqualTo("hello*****");

		// 将第6~8的字符替换为#
		assertThat(StringX.replaceChars("helloworld", '#', 5, 8))
				.isEqualTo("hello###ld");

		// 将最后六位字符替换为*
		assertThat(StringX.replaceChars("511622199141566456", '*', -6))
				.isEqualTo("511622199141******");
		// 将倒数第3~6的字符替换为*
		assertThat(StringX.replaceChars("511622199141566456", '*', -6, -2))
				.isEqualTo("511622199141****56");
		// 将倒数第3~7的字符替换为*
		assertThat(StringX.replaceChars("511622199141566456", '*', -6, -1))
				.isEqualTo("511622199141*****6");
	}

	@Test
	public void replaceSubstring() {
		assertThat(StringX.replaceSubstring(null, "***", 2))
				.isNull();

		assertThat(StringX.replaceSubstring("", "***", 2))
				.isEqualTo("");

		// 将第3个及其后的字符替换为"***"
		assertThat(StringX.replaceSubstring("helloworld", "***", 2))
				.isEqualTo("he***");

		// 将第3~5个字符替换为"***"
		assertThat(StringX.replaceSubstring("helloworld", "***", 2, 5))
				.isEqualTo("he***world");

		// 将第3~倒数第二个字符替换为"***"
		assertThat(StringX.replaceSubstring("helloworld", "***", 2, -1))
				.isEqualTo("he***d");

		// 将倒数第二个~倒数第二个字符替换为"***"
		assertThat(StringX.replaceSubstring("helloworld", "***", -2, -1))
				.isEqualTo("hellowor***d");
	}

	@Test
	public void limitChars() {
		assertThat(StringX.limitChars(null, 15))
				.isEqualTo("");
		// 限制字符串的长度最大为15个字符，并默认附加三个句点表示省略(长度不足15，直接返回原字符串)
		assertThat(StringX.limitChars("张三李四王五", 15))
				.isEqualTo("张三李四王五");
		// 限制字符串的长度最大为7个字符，并默认附加三个句点表示省略
		assertThat(StringX.limitChars("abcdefghijkmln", 7))
				.isEqualTo("abcdefg...");
		// 限制字符串的长度最大为7个字符，附加指定的后缀
		assertThat(StringX.limitChars("abcdefghijkmln", 7, "~~~"))
				.isEqualTo("abcdefg~~~");
		// 限制字符串的长度最大为7个字符，不附加后缀
		assertThat(StringX.limitChars("abcdefghijkmln", 7, ""))
				.isEqualTo("abcdefg");
	}

	@Test
	public void isEmpty() {
		assertTrue(StringX.isEmpty(""));
		assertTrue(StringX.isEmpty(null));
		assertFalse(StringX.isEmpty("   "));
	}

	@Test
	public void notEmpty() {
		assertFalse(StringX.notEmpty(""));
		assertFalse(StringX.notEmpty(null));
		assertTrue(StringX.notEmpty("   "));
	}

	@Test
	public void isBlank() {
		assertTrue(StringX.isBlank(""));
		assertTrue(StringX.isBlank(null));
		assertTrue(StringX.isBlank("   "));
		assertFalse(StringX.isBlank(" y  "));
	}

	@Test
	public void notBlank() {
		assertFalse(StringX.notBlank(""));
		assertFalse(StringX.notBlank(null));
		assertFalse(StringX.notBlank("   "));
		assertTrue(StringX.notBlank(" x  "));
	}

	@Test
	public void concat() {
		assertEquals("", StringX.concat(null, null));
		assertEquals("ab", StringX.concat("a", "b"));
		assertSame("b", StringX.concat(null, "b"));
		assertSame("a", StringX.concat("a", null));

		assertEquals("abc", StringX.concat("a", "b", "c"));
		assertEquals("ac", StringX.concat("a", null, "c"));

		assertEquals("abcde", StringX.concats("a", "b", "c", "d", "e"));
		assertEquals("abd", StringX.concats("a", "b", null, "d", null));
	}

	@Test
	public void escape() {
		String str = "\"Hello'World\\\"";
		char[] escapedChars = "'\"".toCharArray();
		char escapeChar = '\\';
		String escaped = StringX.escape(str, escapeChar, escapedChars);
		assertEquals("\\\"Hello\\'World\\\\\\\"", escaped);

		assertEquals(str, StringX.unescape(escaped, escapeChar, escapedChars));
	}

	@Test
	public void containsWord() {
		assertFalse(StringX.containsWord(null, "view", " "));
		assertFalse(StringX.containsWord("", "view", " "));
		assertFalse(StringX.containsWord("", null, " "));
		assertTrue(StringX.containsWord("", "", " "));
		assertFalse(StringX.containsWord(null, "", " "));
		assertTrue(StringX.containsWord("edit view submit", "view", " "));
		assertFalse(StringX.containsWord("edit,view,submit", "view", " "));

		assertTrue(StringX.containsWord("edit,view submit", "submit", ", "));
		assertFalse(StringX.containsWord("edit,viewsubmit ", "submit", ", "));
	}

	@Test
	public void trimAll() {
		assertEquals("", StringX.trimAll(null));
		assertEquals("", StringX.trimAll("    "));
		assertEquals("codeplayer", StringX.trimAll("  code player"));
		assertEquals("codeplayer", StringX.trimAll("code  player  "));
		assertEquals("codeplayer", StringX.trimAll(" code player "));
	}

}