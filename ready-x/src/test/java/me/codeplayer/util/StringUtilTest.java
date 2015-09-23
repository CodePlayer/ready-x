package me.codeplayer.util;

import me.codeplayer.util.StringUtil;


public class StringUtilTest {

	//	@Test
	public void unicode() {
		// 将字符串转为Unicode码		
		System.out.println(StringUtil.unicode("中国")); // \u4E2D\u56FD
		System.out.println(StringUtil.unicode("张三丰")); // \u5F20\u4E09\u4E30
		System.out.println(StringUtil.unicode("ABCD")); // \u0041\u0042\u0043\u0044
		System.out.println(StringUtil.unicode("*/45sfga$")); // \u002A\u002F\u0034\u0035\u0073\u0066\u0067\u0061\u0024
	}

	//	@Test
	public void fastUnicode() {
		// 将字符串转为Unicode码，与unicode()相比，性能更优异
		System.out.println(StringUtil.fastUnicode("中国")); // \u4E2D\u56FD
		System.out.println(StringUtil.fastUnicode("张三丰")); // \u5F20\u4E09\u4E30
		System.out.println(StringUtil.fastUnicode("ABCD")); // \u0041\u0042\u0043\u0044
		System.out.println(StringUtil.fastUnicode("*/45sfga$")); // \u002A\u002F\u0034\u0035\u0073\u0066\u0067\u0061\u0024
	}

	//	@Test
	public void replaceChars() {
		// 将第6个及其后的字符替换为*
		System.out.println(StringUtil.replaceChars("helloworld", '*', 5)); // hello*****
		// 将第6~8的字符替换为#
		System.out.println(StringUtil.replaceChars("helloworld", '#', 5, 8));// hello###ld
		// 将最后六位字符替换为*
		System.out.println(StringUtil.replaceChars("511622199141566456", '*', -6)); // 511622199141******
		// 将倒数第3~6的字符替换为*
		System.out.println(StringUtil.replaceChars("511622199141566456", '*', -6, -2)); // 511622199141****56
	}

	//	@Test
	public void replaceSubstring() {
		// 将第3个及其后的字符替换为"***"
		System.out.println(StringUtil.replaceSubstring("helloworld", "***", 2)); // he***
		// 将第3~5个字符替换为"***"
		System.out.println(StringUtil.replaceSubstring("helloworld", "***", 2, 5)); // he***world
		// 将第3~倒数第二个字符替换为"***"
		System.out.println(StringUtil.replaceSubstring("helloworld", "***", 2, -1)); // he***d
	}

	//	@Test
	public void limitChars() {
		// 限制字符串的长度最大为15个字符，并默认附加三个句点表示省略(长度不足15，直接返回原字符串)
		System.out.println(StringUtil.limitChars("张三李四王五", 15)); // 张三李四王五
		// 限制字符串的长度最大为7个字符，并默认附加三个句点表示省略
		System.out.println(StringUtil.limitChars("abcdefghijkmln", 7)); // abcdefg...
		// 限制字符串的长度最大为7个字符，附加指定的后缀
		System.out.println(StringUtil.limitChars("abcdefghijkmln", 7, "~~~")); // abcdefg~~~
		// 限制字符串的长度最大为7个字符，不附加后缀
		System.out.println(StringUtil.limitChars("abcdefghijkmln", 7, "")); // abcdefg
	}

	//	@Test
	public void isEmpty() {
		System.out.println(StringUtil.isEmpty("")); // true
		System.out.println(StringUtil.isEmpty(null)); // true
		System.out.println(StringUtil.isEmpty("   ")); // false
	}

	//	@Test
	public void notEmpty() {
		System.out.println(StringUtil.notEmpty("")); // false
		System.out.println(StringUtil.notEmpty(null)); // false
		System.out.println(StringUtil.notEmpty("   ")); // true
	}
}
