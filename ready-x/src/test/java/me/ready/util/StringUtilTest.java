package me.ready.util;

import org.junit.Test;

public class StringUtilTest {

	// @Test
	public void replace() {
		System.out.println(StringUtil.replaceChars("511622199141566456", '*', -6));
		String.format("", 12.454545);
	}

	@Test
	public void limitChars() {
		System.out.println(StringUtil.limitChars("张三李四王五", 15));
	}
}
