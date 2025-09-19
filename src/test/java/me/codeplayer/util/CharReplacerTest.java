package me.codeplayer.util;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CharReplacerTest {

	@Test
	// @EnabledOnJre({ JRE.JAVA_11, JRE.JAVA_17, JRE.JAVA_21 })
	public void latin1() {
		CharReplacer replacer = new Latin1CharReplacer("Hello".getBytes(StandardCharsets.US_ASCII));
		replacer.setCharAt(1, 'a');
		assertEquals("Hallo", replacer.toString());
	}

	@Test
	public void utf16() {
		CharReplacer replacer = CharReplacer.of("你好中文".toCharArray());
		replacer.setCharAt(0, 'H');
		replacer.setCharAt(1, 'i');
		assertEquals("Hi中文", replacer.toString());
	}

	@Test
	public void stringBuilder() {
		CharReplacer replacer = CharReplacer.of(new StringBuilder(18).append("你好中文，今天是0000-00-00"));
		replacer.setCharAt(0, 'H');
		replacer.setCharAt(1, 'i');
		int offset = 8;
		EasyDate.formatNormalDate(replacer, 2025, offset, 3, offset + 5, 26, offset + 8);
		assertEquals("Hi中文，今天是2025-03-26", replacer.toString());
	}

	@Test
	public void ofChars() {
		CharReplacer replacer = CharReplacer.ofChars(4, true);
		replacer.setCharAt(0, 'J');
		replacer.setCharAt(1, 'a');
		replacer.setCharAt(2, 'v');
		replacer.setCharAt(3, 'a');
		assertEquals("Java", replacer.toString());

		replacer = CharReplacer.ofChars(2, false);
		replacer.setCharAt(0, '你');
		replacer.setCharAt(1, '好');
		assertEquals("你好", replacer.toString());
	}

}