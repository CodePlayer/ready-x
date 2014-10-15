package me.ready.util;

import org.junit.Test;

public class RandomUtilTest {

	@Test
	public void testGetInt() {
		for (int i = 0; i < 100; i++) {
			System.out.println(RandomUtil.getInt(-17, 17));
		}
	}
}
