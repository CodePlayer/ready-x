package me.codeplayer.util;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import static org.junit.jupiter.api.Assertions.*;

public class JavaXTest {

	@Test
	@EnabledOnJre(JRE.JAVA_8)
	public void javaVersion8() throws IllegalAccessException {
		assertEquals(8, JavaX.javaVersion);
		assertEquals(8, JavaX.JVM_VERSION);
		assertFalse(JavaX.isJava9OrHigher);

		final String str = "Hello";
		final char[] chars = JavaX.getCharArray(str);
		assertSame(chars, FieldUtils.getDeclaredField(String.class, "value", true).get(str));
		assertNull(JavaX.initErrorLast);

		assertEquals(str, JavaX.asciiStringJDK8(str.getBytes(StandardCharsets.ISO_8859_1), 0, chars.length));
		assertEquals(str, JavaX.latin1StringJDK8(str.getBytes(StandardCharsets.ISO_8859_1), 0, chars.length));
	}

	static void java9OrHigher(int majorVersion) {
		assertEquals(majorVersion, JavaX.javaVersion);
		assertEquals(majorVersion, JavaX.JVM_VERSION);
		assertTrue(JavaX.isJava9OrHigher);

		final String str = "Hello";
		Assert.isTrue(JavaX.STRING_CODER.applyAsInt(str) == JavaX.LATIN1);
		final byte[] chars = JavaX.STRING_VALUE.apply(str);
		try {
			assertSame(chars, FieldUtils.getDeclaredField(String.class, "value", true).get(str));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		assertNull(JavaX.initErrorLast);
		Assert.isTrue(JavaX.STRING_CODER.applyAsInt("中文") == JavaX.UTF16);
	}

	@Test
	@EnabledOnJre(JRE.JAVA_11)
	public void javaVersion11() {
		java9OrHigher(11);
	}

	@Test
	@EnabledOnJre(JRE.JAVA_17)
	public void javaVersion17() {
		java9OrHigher(17);
	}

	@Test
	@EnabledOnJre(JRE.JAVA_21)
	public void javaVersion21() {
		java9OrHigher(21);
	}

	@Test
	@EnabledOnJre(JRE.JAVA_24)
	public void javaVersion24() {
		java9OrHigher(24);
	}

}