package me.codeplayer.util;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaTest {

	@Test
	public void test() {
		/*
		File src = new File("C:\\Users\\pc\\Desktop\\system-logo.png");
		File dest = new File("C:\\Users\\pc\\Desktop\\Eclipse-target-properties.png");
		FileUtil.moveFile(src, dest, true);
		*/
	}

	@Test
	public void mock() {
		try (MockedStatic<StringUtil> mocked = Mockito.mockStatic(StringUtil.class)) {
			mocked.when(() -> StringUtil.isEmpty("hello")).thenReturn(true);

			assertTrue(StringUtil.isEmpty("hello"));
			assertFalse(StringUtil.isEmpty("world"));
		}
	}

}