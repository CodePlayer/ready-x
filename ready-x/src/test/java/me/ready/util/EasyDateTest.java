package me.ready.util;

import java.util.Calendar;

import org.junit.Test;

public class EasyDateTest {

	// @Test
	public void test() {
		EasyDate d = new EasyDate();
		System.out.println(d);
		d.addDay(365);
		System.err.println(d);
	}

	// @Test
	public void testGetLastDayOfMonth() {
		EasyDate d = new EasyDate(2013, 2, 5);
		Assert.isTrue(d.getLastDayOfMonth() == 28, "error");
		d = new EasyDate(2014, 11, 5);
		Assert.isTrue(d.getLastDayOfMonth() == 30, "error");
		d = new EasyDate(2014, 12, 1);
		Assert.isTrue(d.getLastDayOfMonth() == 31, "error");
		d = new EasyDate(2000, 2, 1);
		Assert.isTrue(d.getLastDayOfMonth() == 29, "error");
	}

	// @Test
	public void testEndOf() {
		EasyDate d = new EasyDate(2013, 2, 5);
		d.endOf(Calendar.MONTH);
		System.out.println(d.toLocaleString());
	}

	// @Test
	public void testBeginOf() {
		EasyDate d = new EasyDate(2013, 2, 5, 23, 12, 55);
		d.beginOf(Calendar.YEAR);
		System.out.println(d.toLocaleString());
	}

	@Test
	public void testGetTimeZoneOffset() {
		EasyDate d = new EasyDate(2013, 2, 5, 23, 12, 55);
		System.out.println(d.getTimeZoneOffset());
		d.setTimeZoneOffset(60);
		System.out.println(d.getTimeZoneOffset());
		System.out.println(d.toLocaleString());
	}
}
