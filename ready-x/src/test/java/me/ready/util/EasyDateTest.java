package me.ready.util;

import org.junit.Test;

public class EasyDateTest {

	@Test
	public void test() {
		EasyDate d = new EasyDate();
		System.out.println(d);
		d.addDay(365);
		System.err.println(d);
	}
}
