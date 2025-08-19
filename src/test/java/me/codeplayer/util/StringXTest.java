package me.codeplayer.util;

import java.nio.charset.StandardCharsets;
import java.util.*;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class StringXTest implements WithAssertions {

	public static Book[] books = {
			new Book(1L),
			new Book(2L),
			new Book(3L)
	};

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
		assertThat(StringX.replaceChars(null, '*', 5)).isNull();
		assertThat(StringX.replaceChars("", '*', 5)).isEqualTo("");
		assertThat(StringX.replaceChars("abc", '*', 5)).isEqualTo("abc");
		// 将第6个及其后的字符替换为*
		assertThat(StringX.replaceChars("helloworld", '*', 5)).isEqualTo("hello*****");

		// 将第6~8的字符替换为#
		assertThat(StringX.replaceChars("helloworld", '#', 5, 8)).isEqualTo("hello###ld");

		// 将最后六位字符替换为*
		assertThat(StringX.replaceChars("511622199141566456", '*', -6)).isEqualTo("511622199141******");
		// 将倒数第3~6的字符替换为*
		assertThat(StringX.replaceChars("511622199141566456", '*', -6, -2)).isEqualTo("511622199141****56");
		// 将倒数第3~7的字符替换为*
		assertThat(StringX.replaceChars("511622199141566456", '*', -6, -1)).isEqualTo("511622199141*****6");
		assertThat(StringX.replaceChars("12中文45", '*', 1, -1)).isEqualTo("1****5");
		assertThat(StringX.replaceChars("12345", '中', 1, -1)).isEqualTo("1中中中5");
	}

	@Test
	public void replaceSubstring() {
		assertThat(StringX.replaceSubstring(null, "***", 2)).isNull();

		assertThat(StringX.replaceSubstring("", "***", 2)).isEqualTo("");

		// 将第3个及其后的字符替换为"***"
		assertThat(StringX.replaceSubstring("helloworld", "***", 2)).isEqualTo("he***");

		// 将第3~5个字符替换为"***"
		assertThat(StringX.replaceSubstring("helloworld", "***", 2, 5)).isEqualTo("he***world");

		// 将第3~倒数第二个字符替换为"***"
		assertThat(StringX.replaceSubstring("helloworld", "***", 2, -1)).isEqualTo("he***d");

		// 将倒数第二个~倒数第二个字符替换为"***"
		assertThat(StringX.replaceSubstring("helloworld", "***", -2, -1)).isEqualTo("hellowor***d");
	}

	@Test
	public void limitChars() {
		assertThat(StringX.limitChars(null, 15)).isEqualTo("");
		// 限制字符串的长度最大为15个字符，并默认附加三个句点表示省略(长度不足15，直接返回原字符串)
		assertThat(StringX.limitChars("张三李四王五", 15)).isEqualTo("张三李四王五");
		// 限制字符串的长度最大为7个字符，并默认附加三个句点表示省略
		assertThat(StringX.limitChars("abcdefghijkmln", 7)).isEqualTo("abcdefg...");
		// 限制字符串的长度最大为7个字符，附加指定的后缀
		assertThat(StringX.limitChars("abcdefghijkmln", 7, "~~~")).isEqualTo("abcdefg~~~");
		// 限制字符串的长度最大为7个字符，不附加后缀
		assertThat(StringX.limitChars("abcdefghijkmln", 7, "")).isEqualTo("abcdefg");
	}

	@Test
	public void zeroFill() {
		assertThat(StringX.zeroFill(123, 5)).isEqualTo("00123");
		assertThat(StringX.zeroFill(123456, 5)).isEqualTo("123456");
		assertThat(StringX.zeroFill(12345, 5)).isEqualTo("12345");

		assertThat(StringX.zeroFill("023", 2)).isEqualTo("023");
		assertThat(StringX.zeroFill("023", 5)).isEqualTo("00023");
	}

	@Test
	public void pad() {
		assertThat(StringX.pad(null, '*', 5, true)).isEqualTo("");
		assertThat(StringX.pad("abc", '*', 5, true)).isEqualTo("**abc");
		assertThat(StringX.pad("abc", '*', 5, false)).isEqualTo("abc**");

		assertThat(StringX.pad("", '*', 5, false)).isEqualTo("*****");

		assertThat(StringX.pad("abcde", '*', 5, true)).isEqualTo("abcde");
		assertThat(StringX.pad("abcde", '*', 5, false)).isEqualTo("abcde");
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

		final List<Integer> integers = Arrays.asList(1, 2, 3);
		assertThat(StringX.joinLong(integers, FunctionX.identity(), ",")).isEqualTo("1,2,3");

		String str = StringX.joinLongValue(integers, Number::longValue, ",");
		assertEquals("1,2,3", str);

		List<User> users = Collections.emptyList();
		assertThat(StringX.joinLong(users, User::getId, ",")).isEqualTo("");

		List<Book> books = Collections.emptyList();
		assertThat(StringX.joinLong(books, Entity::getId, ",")).isEqualTo("");
	}

	@Test
	public void joinIntValue() {
		assertThat(StringX.joinLongValue(Collections.emptyList(), Integer::intValue, ",")).isEqualTo("");
		assertThat(StringX.joinLongValue(Collections.singletonList(1), Integer::intValue, ",")).isEqualTo("1");
		assertThat(StringX.joinLongValue(Arrays.asList(1, 2, 3), Integer::intValue, ",")).isEqualTo("1,2,3");
	}

	@Test
	public void getInSQL_Empty_ThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> StringX.getInSQL(Collections.emptyList(), true, true));

		assertThrows(IllegalArgumentException.class, () -> StringX.getInSQL(ArrayX.of(), true, true));
	}

	@Test
	public void getInSQL_SingleElementIncludeString_ReturnsEquals() {
		assertEquals(" IN ('1')", StringX.getInSQL(Collections.singletonList("1"), true, true));

		assertEquals(" IN ('1')", StringX.getInSQL(new String[] { "1" }, true, true));

	}

	@Test
	public void getInSQL_SingleElementIncludeNumeric_ReturnsEquals() {
		assertEquals(" IN (1)", StringX.getInSQL(Collections.singletonList(1), false));

		assertEquals(" IN (1)", StringX.getInSQL(new Integer[] { 1 }, false));
	}

	@Test
	public void getInSQL_SingleElementExcludeString_ReturnsNotEquals() {
		assertEquals(" NOT IN ('1')", StringX.getInSQL(Collections.singletonList("1"), false, true));

		assertEquals(" NOT IN ('1')", StringX.getInSQL(ArrayX.of("1"), false, true));
	}

	@Test
	public void getInSQL_SingleElementExcludeNumeric_ReturnsNotEquals() {
		assertEquals(" NOT IN (1)", StringX.getInSQL(Collections.singletonList(1), false, false));
		assertEquals(" NOT IN (1)", StringX.getInSQL(ArrayX.of(1), false, false));
	}

	@Test
	public void getInSQL_MultipleElementsIncludeString_ReturnsInClause() {
		assertEquals(" IN ('1', '2', '3')", StringX.getInSQL(Arrays.asList("1", "2", "3"), true, true));

		assertEquals(" IN ('1', '2', '3')", StringX.getInSQL(ArrayX.of("1", "2", "3"), true, true));
	}

	@Test
	public void getInSQL_MultipleElementsIncludeNumeric_ReturnsInClause() {
		assertEquals(" IN (1, 2, 3)", StringX.getInSQL(Arrays.asList(1, 2, 3), true, false));
		assertEquals(" IN (1, 2, 3)", StringX.getInSQL(ArrayX.of(1, 2, 3), true, false));
	}

	@Test
	public void getInSQL_MultipleElementsExcludeString_ReturnsNotInClause() {
		assertEquals(" NOT IN ('1', '2', '3')", StringX.getInSQL(Arrays.asList("1", "2", "3"), false, true));
		assertEquals(" NOT IN ('1', '2', '3')", StringX.getInSQL(ArrayX.of("1", "2", "3"), false, true));
	}

	@Test
	public void getInSQL_MultipleElementsExcludeNumeric_ReturnsNotInClause() {
		assertEquals(" NOT IN (1, 2, 3)", StringX.getInSQL(Arrays.asList(1, 2, 3), false, false));
		assertEquals(" NOT IN (1, 2, 3)", StringX.getInSQL(ArrayX.of(1, 2, 3), false, false));
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
	public void join() {
		final Integer[] array = { 1, 2, 3 };
		final List<Integer> list = Arrays.asList(array);
		assertEquals("1,2,3", StringX.join(array, ","));
		assertEquals("1,2,3", StringX.join(array, ","));
		assertEquals("1,2,3", StringX.join(list, ","));
		assertEquals("1,2,3", StringX.join(books, Book::getId, ","));
		assertEquals("1,2,3", StringX.join(Arrays.asList(books), Entity::getId, ","));
		assertEquals("1,2,3", StringX.joinLong(Arrays.asList(books), Entity::getId, ","));
		assertEquals("1,2,3", StringX.joinLongValue(Arrays.asList(books), Entity::getId, ","));

		StringBuilder sb = new StringBuilder("Hello ");
		StringBuilder result = StringX.joinAppend(sb, array, ",");
		assertSame(sb, result);
		assertEquals("Hello 1,2,3", result.toString());
	}

	@Test
	public void joinAppend() {
		Integer[] array = { 1, 2, 3 };
		List<Integer> list = Arrays.asList(array);

		assertEquals("1,2,3", StringX.joinAppend(list, (sb, t) -> sb.append(t.intValue()), ","));
		assertEquals("1,2,3", StringX.joinAppend(null, array, StringX::append, ",", 0).toString());

		{
			StringBuilder builder = StringX.joinAppend(null, list, (sb, t) -> sb.append(t.intValue()), ",", 1);
			assertEquals((1 + 1) * 3 + 4, builder.capacity());
			assertEquals("1,2,3", builder.toString());
		}
		{
			StringBuilder builder = StringX.joinAppend(null, list, (sb, t) -> sb.append(t.intValue()), ",");
			assertEquals((6 + 1) * 3 + 4, builder.capacity());
			assertEquals("1,2,3", builder.toString());
		}
		{
			StringBuilder builder = StringX.joinAppend(null, list, (sb, t) -> sb.append(t.intValue()), ",");
			assertEquals((6 + 1) * 3 + 4, builder.capacity());
			assertEquals("1,2,3", builder.toString());
		}
	}

	@Test
	public void split() {
		List<Integer> vals = StringX.split("1,22,333", ",", String::length);
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

	@ParameterizedTest
	@CsvSource(delimiter = '|', nullValues = "null", value = {
			"null  |  ','  |  0",
			"''  |  ','  |  0",
			"abc  |  ','  |  1",
			"'a,b,c'  |  ','  |  3",
			"'a,,b,,,c'  |  ','  |  6",
			"',,'  |  ','  |  3"
	})
	void splitCount(String values, char sep, int expected) {
		assertEquals(expected, StringX.splitCount(values, sep));
	}

	@Test
	void split_withSlice() {
		// 测试 null 输入
		assertNull(StringX.split(null, ',', Slice::asString));

		// 测试 空字符串 输入
		assertEquals(0, StringX.split("", ',', Slice::asString).size());

		// 测试无分隔符情况
		List<String> result1 = StringX.split("abc", ',', Slice::asString);
		assertNotNull(result1);
		assertEquals(1, result1.size());
		assertEquals("abc", result1.get(0));

		// 测试正常拆分情况
		List<String> result2 = StringX.split("a,b,c", ',', Slice::asString);
		assertNotNull(result2);
		assertEquals(3, result2.size());
		assertEquals("a", result2.get(0));
		assertEquals("b", result2.get(1));
		assertEquals("c", result2.get(2));

		{
			// 测试开头有分隔符
			List<String> result3 = StringX.split(",abc", ',', Slice::asString);
			assertNotNull(result3);
			assertEquals(1, result3.size());
			assertEquals("abc", result3.get(0));

			// 测试结尾有分隔符
			List<String> result4 = StringX.split("abc,", ',', Slice::asString);
			assertNotNull(result4);
			assertEquals(1, result4.size());
			assertEquals("abc", result4.get(0));

			// 测试连续分隔符
			List<String> result5 = StringX.split("a,,c", ',', Slice::asString);
			assertNotNull(result5);
			assertEquals(2, result5.size());
			assertEquals("a", result5.get(0));
			assertEquals("c", result5.get(1));
		}

		{
			// 测试开头有分隔符
			List<String> result3 = StringX.split(",abc", ',', Slice::parseString);
			assertNotNull(result3);
			assertEquals(2, result3.size());
			assertEquals("", result3.get(0));
			assertEquals("abc", result3.get(1));

			// 测试结尾有分隔符
			List<String> result4 = StringX.split("abc,", ',', Slice::parseString);
			assertNotNull(result4);
			assertEquals(1, result4.size());
			assertEquals("abc", result4.get(0));

			// 测试连续分隔符
			List<String> result5 = StringX.split("a,,c", ',', Slice::parseString);
			assertNotNull(result5);
			assertEquals(3, result5.size());
			assertEquals("a", result5.get(0));
			assertEquals("", result5.get(1));
			assertEquals("c", result5.get(2));
		}

		// 测试 mapper 返回 null 的情况
		Slice<String> nullMapper = (str, start, end) -> {
			String substring = str.substring(start, end);
			// 如果是"skip"则返回null
			return "skip".equals(substring) ? null : substring;
		};
		List<String> result6 = StringX.split("a,skip,c", ',', nullMapper);
		assertNotNull(result6);
		assertEquals(2, result6.size());
		assertEquals("a", result6.get(0));
		assertEquals("c", result6.get(1));
	}

	@Test
	void removeStart() {
		// 测试 str 为null
		String result1 = StringX.removeStart(null, "pre", Slice::parseString);
		assertEquals("", result1);

		// 测试str为空
		String result2 = StringX.removeStart("", "pre", Slice::parseString);
		assertEquals("", result2);

		// 测试remove为null
		String result3 = StringX.removeStart("prefix_content", null, Slice::parseString);
		assertEquals("prefix_content", result3);

		// 测试remove为空
		String result4 = StringX.removeStart("prefix_content", "", Slice::parseString);
		assertEquals("prefix_content", result4);

		// 测试不以remove开头
		String result5 = StringX.removeStart("prefix_content", "post", Slice::parseString);
		assertEquals("prefix_content", result5);

		// 测试正常移除前缀
		String result6 = StringX.removeStart("prefix_content", "prefix_", Slice::parseString);
		assertEquals("content", result6);

		// 测试完全匹配
		String result7 = StringX.removeStart("prefix", "prefix", Slice::parseString);
		assertEquals("", result7);
	}

	@Test
	void removeEnd() {
		// 测试str为null
		String result1 = StringX.removeEnd(null, "post", Slice::parseString);
		assertEquals("", result1);

		// 测试str为空
		String result2 = StringX.removeEnd("", "post", Slice::parseString);
		assertEquals("", result2);

		// 测试remove为null
		String result3 = StringX.removeEnd("content_postfix", null, Slice::parseString);
		assertEquals("content_postfix", result3);

		// 测试remove为空
		String result4 = StringX.removeEnd("content_postfix", "", Slice::parseString);
		assertEquals("content_postfix", result4);

		// 测试不以remove结尾
		String result5 = StringX.removeEnd("content_postfix", "pre", Slice::parseString);
		assertEquals("content_postfix", result5);

		// 测试正常移除后缀
		String result6 = StringX.removeEnd("content_postfix", "_postfix", Slice::parseString);
		assertEquals("content", result6);

		// 测试完全匹配
		String result7 = StringX.removeEnd("postfix", "postfix", Slice::parseString);
		assertEquals("", result7);
	}

	@Test
	void substringBefore() {
		// 测试str为null
		String result1 = StringX.substringBefore(null, '_', Slice::parseString);
		assertEquals("", result1);

		// 测试str为空
		String result2 = StringX.substringBefore("", '_', Slice::parseString);
		assertEquals("", result2);

		// 测试找不到分隔符
		String result3 = StringX.substringBefore("content", '_', Slice::parseString);
		assertEquals("content", result3);

		// 测试找到分隔符
		String result4 = StringX.substringBefore("content_before_after", '_', Slice::parseString);
		assertEquals("content", result4);

		// 测试分隔符在第一个位置
		String result5 = StringX.substringBefore("_content", '_', Slice::parseString);
		assertEquals("", result5);
	}

	@Test
	void substringAfterLast() {
		// 测试str为null
		String result1 = StringX.substringAfterLast(null, '_', Slice::parseString);
		assertEquals("", result1);

		// 测试str为空
		String result2 = StringX.substringAfterLast("", '_', Slice::parseString);
		assertEquals("", result2);

		// 测试找不到分隔符
		String result3 = StringX.substringAfterLast("content", '_', Slice::parseString);
		assertEquals("content", result3);

		// 测试找到分隔符
		String result4 = StringX.substringAfterLast("content_before_after", '_', Slice::parseString);
		assertEquals("after", result4);

		// 测试分隔符在最后一个位置
		String result5 = StringX.substringAfterLast("content_", '_', Slice::parseString);
		assertEquals("", result5);
	}

	@Test
	void splitAsLongListWithChar() {
		// 测试null输入
		assertNull(StringX.splitAsLongList(null, ','));

		// 测试空字符串输入
		assertEquals(0, StringX.splitAsLongList("", ',').size());

		// 测试正常情况
		List<Long> result = StringX.splitAsLongList("1,2,3", ',');
		assertNotNull(result);
		assertEquals(3, result.size());
		assertEquals(1L, result.get(0));
		assertEquals(2L, result.get(1));
		assertEquals(3L, result.get(2));

		// 测试包含无效数字
		assertThrows(NumberFormatException.class, () -> StringX.splitAsLongList("1,abc,3", ','));
	}

	@Test
	void splitAsLongList() {
		// 测试null输入
		assertNull(StringX.splitAsLongList(null));

		// 测试空字符串输入
		assertEquals(0, StringX.splitAsLongList("").size());

		// 测试正常情况
		List<Long> result = StringX.splitAsLongList("1,2,3,");
		assertNotNull(result);
		assertEquals(3, result.size());
		assertEquals(1L, result.get(0));
		assertEquals(2L, result.get(1));
		assertEquals(3L, result.get(2));
	}

	@Test
	void splitAsIntListWithChar() {
		// 测试 null 输入
		assertNull(StringX.splitAsIntList(null, ','));

		// 测试空字符串输入
		assertEquals(0, StringX.splitAsIntList("", ',').size());

		// 测试正常情况
		List<Integer> result = StringX.splitAsIntList("1,2,3", ',');
		assertNotNull(result);
		assertEquals(3, result.size());
		assertEquals(1, result.get(0));
		assertEquals(2, result.get(1));
		assertEquals(3, result.get(2));

		// 测试包含无效数字
		assertThrows(NumberFormatException.class, () -> StringX.splitAsIntList("1,abc,3", ','));
	}

	@Test
	void splitAsIntList() {
		// 测试null输入
		assertNull(StringX.splitAsIntList(null));

		// 测试空字符串输入
		assertEquals(0, StringX.splitAsIntList("").size());

		// 测试正常情况
		List<Integer> result = StringX.splitAsIntList("1,2,3");
		assertNotNull(result);
		assertEquals(3, result.size());
		assertEquals(1, result.get(0));
		assertEquals(2, result.get(1));
		assertEquals(3, result.get(2));
	}

	@Test
	void splitIntAsList() {
		// 测试 null 输入
		assertNull(StringX.splitIntAsList(null, ',', String::valueOf));

		// 测试空字符串输入
		assertEquals(0, StringX.splitIntAsList("", ',', String::valueOf).size());

		// 测试正常情况
		List<User> result = StringX.splitIntAsList("1,2,3", ',', User::new);
		assertNotNull(result);
		assertEquals(3, result.size());
		assertEquals(1, result.get(0).getId());
		assertEquals(2, result.get(1).getId());
		assertEquals(3, result.get(2).getId());

		// 测试包含无效数字
		assertThrows(NumberFormatException.class, () -> StringX.splitIntAsList("1,abc,3", ',', String::valueOf));
	}

	@Test
	void splitLongAsList() {
		// 测试 null 输入
		assertNull(StringX.splitLongAsList(null, ',', String::valueOf));

		// 测试空字符串输入
		assertEquals(0, StringX.splitLongAsList("", ',', String::valueOf).size());

		// 测试正常情况
		List<String> result = StringX.splitLongAsList("1,2,3,", ',', String::valueOf);
		assertNotNull(result);
		assertEquals(3, result.size());
		assertEquals("1", result.get(0));
		assertEquals("2", result.get(1));
		assertEquals("3", result.get(2));

		// 测试包含无效数字
		assertThrows(NumberFormatException.class, () -> StringX.splitLongAsList("1,abc,3", ',', String::valueOf));
	}

	@Test
	void splitAsStringList() {
		// 测试null输入
		assertNull(StringX.splitAsStringList(null));

		// 测试空字符串输入
		assertEquals(0, StringX.splitAsStringList("").size());

		// 测试正常情况
		List<String> result = StringX.splitAsStringList("a,b,c");
		assertNotNull(result);
		assertEquals(3, result.size());
		assertEquals("a", result.get(0));
		assertEquals("b", result.get(1));
		assertEquals("c", result.get(2));

		// 测试包含空字符串
		List<String> result2 = StringX.splitAsStringList("a,,c");
		assertNotNull(result2);
		assertEquals(2, result2.size());
		assertEquals("a", result2.get(0));
		assertEquals("c", result2.get(1));
	}

	@Test
	void splitAsStringListWithChar() {
		// 测试null输入
		assertNull(StringX.splitAsStringList(null, ','));

		// 测试空字符串输入
		assertEquals(0, StringX.splitAsStringList("", ',').size());

		// 测试正常情况
		List<String> result = StringX.splitAsStringList("a,b,c", ',');
		assertNotNull(result);
		assertEquals(3, result.size());
		assertEquals("a", result.get(0));
		assertEquals("b", result.get(1));
		assertEquals("c", result.get(2));
	}

}