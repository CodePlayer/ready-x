package me.ready.util;

import org.junit.Test;

public class RandomUtilTest {

	@Test
	public void getInt() {
		// for (int i = 0; i < 100; i++) {
		// System.out.println(RandomUtil.getInt(-17, 17));
		// }
		System.out.println(RandomUtil.getIntString(5));
		System.out.println(RandomUtil.getString("abcdefghijklmnopqrstuvwxyz", 6));
	}
}
