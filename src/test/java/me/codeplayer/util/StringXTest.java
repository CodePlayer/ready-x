package me.codeplayer.util;

import java.nio.charset.StandardCharsets;
import java.util.*;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
	public void zeroFill() {
		assertThat(StringX.zeroFill(123, 5))
				.isEqualTo("00123");
		assertThat(StringX.zeroFill(123456, 5))
				.isEqualTo("123456");
		assertThat(StringX.zeroFill(12345, 5))
				.isEqualTo("12345");

		assertThat(StringX.zeroFill("023", 2))
				.isEqualTo("023");
		assertThat(StringX.zeroFill("023", 5))
				.isEqualTo("00023");
	}

	@Test
	public void pad() {
		assertThat(StringX.pad(null, '*', 5, true))
				.isEqualTo("");
		assertThat(StringX.pad("abc", '*', 5, true))
				.isEqualTo("**abc");
		assertThat(StringX.pad("abc", '*', 5, false))
				.isEqualTo("abc**");

		assertThat(StringX.pad("", '*', 5, false))
				.isEqualTo("*****");

		assertThat(StringX.pad("abcde", '*', 5, true))
				.isEqualTo("abcde");
		assertThat(StringX.pad("abcde", '*', 5, false))
				.isEqualTo("abcde");
	}

	@Test
	public void isEmpty() {
		assertTrue(X.isEmpty(""));
		assertTrue(X.isEmpty(null));
		assertFalse(X.isEmpty("   "));
		assertFalse(X.isEmpty("\t"));
		assertFalse(X.isEmpty("\t\n"));
		assertFalse(X.isEmpty("China"));
		assertFalse(X.isEmpty("A "));
	}

	@Test
	public void notEmpty() {
		assertFalse(StringX.notEmpty(""));
		assertFalse(StringX.notEmpty(null));
		assertTrue(StringX.notEmpty("   "));
	}

	@Test
	public void isBlank() {
		assertTrue(X.isBlank(null));
		assertTrue(X.isBlank(""));
		assertTrue(X.isBlank("   "));
		assertTrue(X.isBlank("\t"));
		assertTrue(X.isBlank("\t\n"));
		assertFalse(X.isBlank(" y  "));
	}

	@Test
	public void notBlank() {
		assertFalse(StringX.notBlank(null));
		assertFalse(StringX.notBlank(""));
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

	@Test
	public void trim() {
		assertEquals("", StringX.trim(null));
		assertEquals("", StringX.trim(""));
		assertEquals("", StringX.trim("   "));
		assertEquals("hello", StringX.trim("hello"));
		assertEquals("hello", StringX.trim(" hello"));
		assertEquals("hello", StringX.trim("hello "));
		assertEquals("hello", StringX.trim("   hello   "));
		assertEquals("中文", StringX.trim("   中文   "));
	}

	@Test
	public void trim_StringWithOnlyTrailingSpaces_ReturnsTrimmedString() {
		assertEquals("hello", StringX.trim("hello   "));
	}

	@Test
	public void trim_StringWithOnlyLeadingSpaces_ReturnsTrimmedString() {
		assertEquals("hello", StringX.trim("   hello"));
	}

	@Test
	public void trim_StringWithMixedCharacters_ReturnsTrimmedString() {
		assertEquals("hello world", StringX.trim("   hello world   "));
	}

	@Test
	public void capitalize() {
		assertNull(StringX.capitalize(null));
		assertSame("", StringX.capitalize(""));
		assertEquals("A", StringX.capitalize("a"));
		assertSame(" a", StringX.capitalize(" a"));
		assertSame("A", StringX.capitalize("A"));
		assertSame("Abc", StringX.capitalize("Abc"));
		assertEquals("Abc", StringX.capitalize("abc"));
		assertEquals("Abc def", StringX.capitalize("abc def"));
		assertSame("中文", StringX.capitalize("中文"));
		assertEquals("A中文", StringX.capitalize("a中文"));
	}

	@Test
	public void decapitalize() {
		assertNull(StringX.decapitalize(null));
		assertEquals("", StringX.decapitalize(""));
		assertSame("a", StringX.decapitalize("a"));
		assertEquals("a", StringX.decapitalize("A"));
		assertSame(" A", StringX.capitalize(" A"));
		assertEquals("abc", StringX.decapitalize("Abc"));
		assertSame("abc", StringX.decapitalize("abc"));
		assertEquals("abc def", StringX.decapitalize("Abc def"));
		assertSame("中文", StringX.decapitalize("中文"));
		assertEquals("a中文", StringX.decapitalize("A中文"));
	}

	@Test
	public void escapeSQLLike() {
		assertNull(StringX.escapeSQLLike(null, false));
		assertNull(StringX.escapeSQLLike(null));
		assertEquals("", StringX.escapeSQLLike("", false));
		assertEquals("abc", StringX.escapeSQLLike("abc", false));
		assertEquals("a\\'b\\_c\\%d\\;e", StringX.escapeSQLLike("a'b_c%d;e", false));
		assertEquals("%a\\'b\\_c\\%d\\;e%", StringX.escapeSQLLike("a'b_c%d;e", true));
		assertEquals("%abc", StringX.escapeSQLLike("abc", true, false));
		assertEquals("abc%", StringX.escapeSQLLike("abc", false, true));
		assertEquals("%abc%", StringX.escapeSQLLike("abc", true));
		assertEquals("%a\\'b\\_c\\%d\\;e%", StringX.escapeSQLLike("a'b_c%d;e", true, true));
	}

	@Test
	public void reverse() {
		assertEquals("", StringX.reverse(null));
		assertEquals("", StringX.reverse(""));
		assertEquals("a", StringX.reverse("a"));
		assertEquals("cba", StringX.reverse("abc"));
		assertEquals(" edcba", StringX.reverse("abcde "));
	}

	@Test
	public void startsWith() {
		assertFalse(StringX.startsWith(null, 'a'));
		assertFalse(StringX.startsWith("", 'a'));
		assertTrue(StringX.startsWith("abc", 'a'));
		assertFalse(StringX.startsWith(" abc", 'a'));
		assertFalse(StringX.startsWith("abc", 'b'));
	}

	@Test
	public void endsWith() {
		assertFalse(StringX.endsWith(null, 'a'));
		assertFalse(StringX.endsWith("", 'a'));
		assertTrue(StringX.endsWith("abc", 'c'));
		assertFalse(StringX.endsWith("abc ", 'c'));
		assertFalse(StringX.endsWith("abc", 'b'));
	}

	@Test
	public void joins() {
		assertEquals("", StringX.joins(null, ","));
		assertEquals("", StringX.joins(Collections.emptyList(), ","));
		assertThat(StringX.joins(Collections.singletonList("a"), ",")).isEqualTo("a");
		assertThat(StringX.joins(Arrays.asList("a", "b", "c"), ",")).isEqualTo("a,b,c");
		assertThat(StringX.joins(Arrays.asList("a", "b", "c"), "")).isEqualTo("abc");
		assertThat(StringX.join(new String[] {}, ",")).isEqualTo("");
		assertThat(StringX.join(new String[] { "a" }, ",")).isEqualTo("a");
		assertThat(StringX.join(new String[] { "a", null, "c" }, ",")).isEqualTo("a,null,c");

		assertThat(StringX.join(Collections.emptyList(), ",")).isEqualTo("");
		assertThat(StringX.join(Collections.singletonList(1), ",")).isEqualTo("1");
		assertThat(StringX.join(Arrays.asList(1, 2, 3), ",")).isEqualTo("1,2,3");
		assertThat(StringX.join(Collections.emptyList(), Object::toString, ",")).isEqualTo("");
		assertThat(StringX.join(Collections.singletonList("a"), Object::toString, ",")).isEqualTo("a");
		assertThat(StringX.join(Arrays.asList("a", "b", "c"), Object::toString, ",")).isEqualTo("a,b,c");
	}

	@Test
	public void joinLongValue() {
		List<Long> longs = Collections.emptyList();
		assertThat(StringX.joinLong(longs, FunctionX.identity(), ",")).isEqualTo("");
		assertThat(StringX.joinLong(Collections.singletonList(1L), FunctionX.identity(), ",")).isEqualTo("1");
		assertThat(StringX.joinLong(Arrays.asList(1, 2, 3), FunctionX.identity(), ",")).isEqualTo("1,2,3");

		String str = StringX.joinLongValue(Arrays.asList(1, 2, 3), Number::longValue, ",");
		assertEquals("1,2,3", str);
	}

	@Test
	public void joinIntValue() {
		assertThat(StringX.joinIntValue(Collections.emptyList(), Integer::intValue, ",")).isEqualTo("");
		assertThat(StringX.joinIntValue(Collections.singletonList(1), Integer::intValue, ",")).isEqualTo("1");
		assertThat(StringX.joinIntValue(Arrays.asList(1, 2, 3), Integer::intValue, ",")).isEqualTo("1,2,3");
	}

	@Test
	public void leftPad() {
		assertEquals("", StringX.leftPad(null, '0', 5));

		assertThrows(IllegalArgumentException.class, () -> StringX.leftPad("abc", '0', 0));

		assertEquals("abcdef", StringX.leftPad("abcdef", '0', 3));

		assertEquals("000abc", StringX.leftPad("abc", '0', 6));
	}

	@Test
	public void rightPad() {
		assertEquals("", StringX.rightPad(null, '0', 5));

		assertThrows(IllegalArgumentException.class, () -> StringX.rightPad("abc", '0', 0));

		assertEquals("abcdef", StringX.rightPad("abcdef", '0', 3));

		assertEquals("abc000", StringX.rightPad("abc", '0', 6));
	}

	@Test
	public void hasEmpty() {
		assertFalse(StringX.hasEmpty());

		assertFalse(StringX.hasEmpty("a", "b", "c"));

		assertTrue(StringX.hasEmpty("a", "", "c"));

		assertTrue(StringX.hasEmpty("a", null, "c"));

		assertTrue(StringX.hasEmpty("a", null, "", "c"));
	}

	@Test
	public void isAnyNotEmpty() {
		assertFalse(StringX.isAnyNotEmpty());

		assertTrue(StringX.isAnyNotEmpty("a", "b", "c"));

		assertTrue(StringX.isAnyNotEmpty("a", "", "c"));

		assertTrue(StringX.isAnyNotEmpty("a", null, "c"));

		assertTrue(StringX.isAnyNotEmpty("a", null, "", "c"));

		assertFalse(StringX.isAnyNotEmpty("", null));

		assertTrue(StringX.isAnyNotEmpty(" ", null));
	}

	@Test
	public void hasBlank() {
		assertFalse(StringX.hasBlank());

		assertFalse(StringX.hasBlank("a", "b ", "c"));

		assertFalse(StringX.hasBlank("a", "b ", "c c"));

		assertTrue(StringX.hasBlank("a", " ", "c"));

		assertTrue(StringX.hasBlank("a", null, "c"));

		assertTrue(StringX.hasBlank("a", null, " ", "c"));
	}

	@Test
	public void joinAppend() {
		StringBuilder builder = StringX.joinAppend(null, Arrays.asList(1, 2, 3), (sb, t) -> sb.append(t.intValue()), ",", 1);
		assertEquals((1 + 1) * 3 + 4, builder.capacity());
		assertEquals("1,2,3", builder.toString());

		builder = StringX.joinAppend(null, Arrays.asList(1, 2, 3), (sb, t) -> sb.append(t.intValue()), ",");
		assertEquals((6 + 1) * 3 + 4, builder.capacity());
		assertEquals("1,2,3", builder.toString());
	}

	@Test
	public void split() {
		List<Integer> vals = StringX.split("1,2,3", ",", Integer::valueOf);
		assertThat(vals)
				.hasSize(3)
				.containsExactly(1, 2, 3);
	}

	@Test
	public void convertCharset_ValidInput_ConvertsCorrectly() {
		final String source = "测试";
		String input = new String(source.getBytes(StandardCharsets.UTF_16), StandardCharsets.ISO_8859_1);
		String result = StringX.convertCharset(input, StandardCharsets.ISO_8859_1, StandardCharsets.UTF_16);
		assertNotNull(result);
		assertEquals(source, result);
	}

	@Test
	public void convertCharsetForURI_ValidInput_ConvertsCorrectly() {
		final String source = "测试";
		String input = new String(source.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
		String result = StringX.convertCharsetForURI(input, StandardCharsets.UTF_8);
		assertNotNull(result);
		assertEquals(source, result);
	}

	@Test
	public void convertCharset_EmptyString_ReturnsEmptyString() {
		final String input = "";
		String result = StringX.convertCharset(input, StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1);
		assertEquals(input, result);
	}

	@Test
	public void convertCharsetForURI_EmptyString_ReturnsEmptyString() {
		final String input = "";
		String result = StringX.convertCharsetForURI(input, StandardCharsets.UTF_8);
		assertEquals(input, result);
	}

}