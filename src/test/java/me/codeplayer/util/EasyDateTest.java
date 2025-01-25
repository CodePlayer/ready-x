package me.codeplayer.util;

import java.util.*;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static org.junit.Assert.*;

public class EasyDateTest implements WithAssertions {

	@SuppressWarnings("deprecation")
	@Test
	public void easyDate() {
		Date now = new Date();
		EasyDate d = new EasyDate(now);
		assertEquals(now.getYear() + 1900, d.getYear());
		assertEquals(now.getMonth() + 1, d.getMonth());
		assertEquals(now.getDate(), d.getDay());
		assertEquals(now.getHours(), d.getHour());
		assertEquals(now.getMinutes(), d.getMinute());
		assertEquals(now.getSeconds(), d.getSecond());

		d = new EasyDate(2014, 12, 2, 6);
		assertEquals(2014, d.getYear());
		assertEquals(12, d.getMonth());
		assertEquals(2, d.getDay());
		assertEquals(6, d.getHour());
	}

	@Test
	public void getLastDayOfMonth() {
		EasyDate d = new EasyDate(2013, 2, 5);
		assertEquals(28, d.getLastDayOfMonth());

		d = new EasyDate(2014, 11, 5);
		assertEquals(30, d.getLastDayOfMonth());

		d = new EasyDate(2014, 12, 1);
		assertEquals(31, d.getLastDayOfMonth());

		d = new EasyDate(2000, 2, 1);
		assertEquals(29, d.getLastDayOfMonth());
	}

	@Test
	public void beginOf() {
		EasyDate d = new EasyDate(2013, 2, 5, 23, 12, 55);
		d.beginOf(Calendar.MONTH);
		assertEquals(2013, d.getYear());
		assertEquals(2, d.getMonth());
		assertEquals(1, d.getDay());
		assertEquals(0, d.getHour());
		assertEquals(0, d.getMinute());
		assertEquals(0, d.getSecond());
		assertEquals(0, d.getMillisecond());
	}

	@Test
	public void endOf() {
		EasyDate d = new EasyDate(2013, 2, 5);
		d.endOf(Calendar.MONTH);
		assertEquals(2013, d.getYear());
		assertEquals(2, d.getMonth());
		assertEquals(28, d.getDay());
		assertEquals(23, d.getHour());
		assertEquals(59, d.getMinute());
		assertEquals(59, d.getSecond());
		assertEquals(999, d.getMillisecond());
	}

	@Test
	public void timeZoneOffset() {
		TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");
		final int localTimeZoneOffset = timeZone.getRawOffset() / 1000 / 60;

		EasyDate d = new EasyDate().setTimeZone(timeZone).resetAs(2013, 2, 5, 23, 12, 55);

		assertThat(d.getTimeZoneOffset()).isEqualTo(localTimeZoneOffset);

		d.setTimeZoneOffset(0);
		assertEquals(0, d.getTimeZoneOffset());

		assertEquals("2013-02-05 15:12:55", d.toDateTimeString());
		assertEquals("20130205", d.toShortString());
		assertEquals("2013-02-05", d.toString());

		d.setTimeZoneOffset(150);
		assertEquals("2013-02-05 17:42:55.000", d.toLongString());
	}

	@Test
	public void isSameAs() {
		EasyDate a = new EasyDate(2015, 2, 28, 0, 0, 0);
		EasyDate b = new EasyDate(2015, 3, 28, 22, 59, 59);
		assertTrue(a.isSameAs(b, Calendar.YEAR));

		assertFalse(a.isSameAs(b, Calendar.HOUR));
	}

	@Test
	public void isSameDay() {
		Date a = new EasyDate(2015, 3, 28, 22, 59, 59).toDate();
		Date b = new EasyDate(2015, 3, 28).toDate();
		assertTrue(EasyDate.isSameDay(a, b));

		b = new EasyDate(2015, 3, 29).toDate();
		assertFalse(EasyDate.isSameDay(a, b));
	}

	@Test
	public void toStr() {
		Date a = new EasyDate(2015, 3, 28, 22, 58, 59).toDate();
		Date b = new EasyDate(2015, 3, 28).toDate();
		assertEquals("2015-03-28", EasyDate.toString(a));

		final Date endOf = EasyDate.endOf(b, Calendar.MONTH);
		assertEquals("2015-03-31 23:59:59", EasyDate.toDateTimeString(endOf));
		assertEquals("2015-03-31 23:59:59.999", EasyDate.toLongString(endOf));
	}

}