package me.codeplayer.util;

import java.util.*;

import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EasyDateTest implements WithAssertions {

	/** 2009-02-14 07:31:30 GMT+8 星期六 */
	private Date baseDate;
	/** 2009-02-14 07:31:30 GMT+8 星期六 */
	private Calendar baseCalendar;

	@Before
	public void setUp() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		baseDate = new Date(1234567890000L); // 2009-02-14 07:31:30 GMT+8
		baseCalendar = Calendar.getInstance();
		baseCalendar.setTime(baseDate);
	}

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
		assertEqualsYmdHms(d, 2014, 12, 2, 6, 0, 0);
		assertEquals("2014年12月02日", d.toDateString());
		assertEquals("2014年12月02日", EasyDate.toDateString(d.toSqlDate()));
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
		final Date date = d.toDate();
		d.beginOf(Calendar.MONTH);
		assertEqualsYmdHms(d, 2013, 2, 1, 0, 0, 0);
		assertEquals(0, d.getMillisecond());

		EasyDate copy = EasyDate.valueOf(EasyDate.beginOf(date, Calendar.MONTH));
		assertEqualsYmdHms(copy, 2013, 2, 1, 0, 0, 0);

		// 2009-02-14 07:31:30 GMT+8 星期六 => 2009-02-09 07:31:30 GMT+8 星期一
		d.setDate(baseDate).beginOf(Calendar.DAY_OF_WEEK);
		assertEqualsYmdHms(d, 2009, 2, 9, 0, 0, 0);
		assertEquals(0, d.getMillisecond());

		// 2009-02-14 07:31:30 GMT+8 星期六 => 2009-02-08 07:31:30 GMT+8 星期日
		d.getCalendar().setFirstDayOfWeek(Calendar.SUNDAY);
		d.setDate(baseDate).beginOf(Calendar.DAY_OF_WEEK);
		assertEqualsYmdHms(d, 2009, 2, 8, 0, 0, 0);
		assertEquals(0, d.getMillisecond());
	}

	@Test
	public void endOf() {
		EasyDate d = new EasyDate(2013, 2, 5);
		final Date date = d.toDate();
		d.endOf(Calendar.MONTH);
		assertEqualsYmdHms(d, 2013, 2, 28, 23, 59, 59);
		assertEquals(999, d.getMillisecond());

		final Locale defLocale = Locale.getDefault();
		Locale.setDefault(Locale.CHINA);

		// JDK 9+ 输出的格式是后者
		assertTrue(ArrayX.ins(d.toGMTNetString(), "星期四, 28 二月 2013 23:59:59 GMT", "周四, 28 2月 2013 23:59:59 GMT"));
		assertTrue(ArrayX.ins(EasyDate.valueOf(baseDate).toGMTString(), "14 二月 2009 07:31:30 GMT", "14 2月 2009 07:31:30 GMT"));
		Locale.setDefault(defLocale);

		assertEquals("20130205", EasyDate.toShortString(date));
		EasyDate copy = EasyDate.valueOf(EasyDate.endOf(date, Calendar.MONTH));
		assertEqualsYmdHms(copy, 2013, 2, 28, 23, 59, 59);

		String longString = EasyDate.toLongString(baseDate);
		assertEquals("2009-02-14 07:31:30.000", longString);
		assertEquals(baseDate, EasyDate.parseDate("yyyy-MM-dd HH:mm:ss.SSS", longString));

		// 2009-02-14 07:31:30 GMT+8 星期六 => 2009-02-15 07:31:30 GMT+8 星期一
		d.setDate(baseDate).endOf(Calendar.DAY_OF_WEEK);
		assertEqualsYmdHms(d, 2009, 2, 15, 23, 59, 59);
		assertEquals(999, d.getMillisecond());

		// 2009-02-14 07:31:30 GMT+8 星期六 => 2009-02-14 07:31:30 GMT+8 星期六
		d.getCalendar().setFirstDayOfWeek(Calendar.SUNDAY);
		d.setDate(baseDate).endOf(Calendar.DAY_OF_WEEK);
		assertEqualsYmdHms(d, 2009, 2, 14, 23, 59, 59);
		assertEquals(999, d.getMillisecond());

		// 2009-02-15 07:31:30 GMT+8 星期日 => 2009-02-21 07:31:30 GMT+8 星期六
		d = EasyDate.valueOf(baseDate);
		d.getCalendar().setFirstDayOfWeek(Calendar.SUNDAY);
		d.addDay(1).endOf(Calendar.DAY_OF_WEEK);
		assertEqualsYmdHms(d, 2009, 2, 21, 23, 59, 59);
		assertEquals(999, d.getMillisecond());
	}

	private static void assertEqualsYmdHms(EasyDate d, int year, int month, int day, int hour, int minute, int second) {
		assertEquals(year, d.getYear());
		assertEquals(month, d.getMonth());
		assertEquals(day, d.getDay());
		assertEquals(hour, d.getHour());
		assertEquals(minute, d.getMinute());
		assertEquals(second, d.getSecond());
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
		Date a = new EasyDate(2015, 3, 28, 22, 58, 59).toTimestamp();
		Date b = new EasyDate(2015, 3, 28).toDate();
		assertEquals("2015-03-28", EasyDate.toString(a));

		final Date endOf = EasyDate.endOf(b, Calendar.MONTH);
		assertEquals("2015-03-31 23:59:59", EasyDate.toDateTimeString(endOf));
		assertEquals("2015-03-31 23:59:59.999", EasyDate.toLongString(endOf));
	}

	@Test
	public void EasyDate_WithZeroMillis_ShouldSetCalendarToEpoch() {
		final EasyDate d = new EasyDate(0, TimeZone.getTimeZone("GMT"));
		assertEqualsYmdHms(d, 1970, 1, 1, 0, 0, 0);
	}

	@Test
	public void EasyDate_WithPositiveMillis_ShouldSetCorrectDate() {
		EasyDate easyDate = new EasyDate(EasyDate.MILLIS_OF_DAY, TimeZone.getTimeZone("GMT"));
		assertEqualsYmdHms(easyDate, 1970, 1, 2, 0, 0, 0);
	}

	@Test
	public void EasyDate_WithNegativeMillis_ShouldSetCorrectDate() {
		EasyDate d = new EasyDate(-EasyDate.MILLIS_OF_DAY, TimeZone.getTimeZone("GMT"));
		assertEqualsYmdHms(d, 1969, 12, 31, 0, 0, 0);
	}

	@Test
	public void EasyDate_WithDifferentTimeZone_ShouldAdjustTime() {
		// UTC-8，夏令时 UTC-7
		EasyDate d = new EasyDate(0, TimeZone.getTimeZone("PST"));
		assertEqualsYmdHms(d, 1969, 12, 31, 16, 0, 0);
	}

	@Test
	public void EasyDate_WithDaylightSavingTime_ShouldAdjustTime() {
		// UTC-5，夏令时 UTC-4
		EasyDate d = new EasyDate(1152000000000L, TimeZone.getTimeZone("America/New_York"));
		assertEqualsYmdHms(d, 2006, 7, 4, 4, 0, 0);
	}

	@Test
	public void EasyDate_DateNull_ShouldUseCurrentTime() {
		final long nowInMs = System.currentTimeMillis();
		EasyDate d = new EasyDate((Date) null, 0, 0, 0);
		assertNotNull(d);
		assertEquals(nowInMs, d.getTime(), 100);
	}

	static Calendar createCalender(Date baseDate, int offsetYears, int offsetMonths, int offsetDays) {
		Calendar expected = Calendar.getInstance();
		expected.setTime(baseDate);
		expected.add(Calendar.YEAR, offsetYears);
		expected.add(Calendar.MONTH, offsetMonths);
		expected.add(Calendar.DATE, offsetDays);
		return expected;
	}

	@Test
	public void EasyDate_DateProvided_ShouldApplyOffsets() {
		EasyDate easyDate = new EasyDate(baseDate, 1, 2, 3);
		Calendar expected = createCalender(baseDate, 1, 2, 3);
		final Date expectedTime = expected.getTime();
		assertEquals(expectedTime, easyDate.toDate());
		assertEquals(expectedTime, new EasyDate(baseCalendar, 1, 2, 3).toDate());
		assertEquals(expectedTime, new EasyDate(new EasyDate(baseDate), 1, 2, 3).toDate());
	}

	@Test
	public void EasyDate_ZeroOffsets_ShouldReturnSameDate() {
		EasyDate easyDate = new EasyDate(baseDate, 0, 0, 0);
		assertEquals(baseDate, easyDate.toDate());
	}

	@Test
	public void EasyDate_PositiveOffsets_ShouldApplyCorrectly() {
		EasyDate easyDate = new EasyDate(baseDate, 1, 1, 1);
		Calendar expected = createCalender(baseDate, 1, 1, 1);
		assertEquals(expected.getTime(), easyDate.toDate());
	}

	@Test
	public void EasyDate_NegativeOffsets_ShouldApplyCorrectly() {
		EasyDate easyDate = new EasyDate(baseDate, -1, -1, -1);
		Calendar expected = createCalender(baseDate, -1, -1, -1);
		assertEquals(expected.getTime(), easyDate.toDate());
	}

	@Test
	public void EasyDate_MixedOffsets_ShouldApplyCorrectly() {
		EasyDate easyDate = new EasyDate(baseDate, 1, -2, 3);
		Calendar expected = createCalender(baseDate, 1, -2, 3);
		Date expectedTime = expected.getTime();
		assertEquals(expectedTime, easyDate.toDate());
		final Calendar c = baseCalendar;
		assertEquals(new Date(c.getTimeInMillis() + 123), easyDate.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), 123).toDate());
	}

	@Test
	public void fields() {
		final EasyDate d = new EasyDate(baseDate); // 2009-02-14 07:31:30 GMT+8 星期六
		assertEquals(baseCalendar.getTime(), d.getCalendar().getTime());
		assertEqualsYmdHms(d, 2009, 2, 14, 7, 31, 30);
		assertFalse(d.isLeapYear());
		assertEquals(45, d.getDayOfYear());
		assertEquals(28, d.getLastDayOfMonth());
		assertEquals(28, EasyDate.getMaxDayOfMonth(baseDate));
		assertEquals(6, d.getWeekDay());
		assertEquals(3, d.getWeeksOfMonth());
		assertEquals(7, d.getWeeksOfYear());
		assertEquals(480, d.getTimeZoneOffset());
		assertEquals(1234567890000L, d.getTime());
		assertEquals(baseDate, d.toDate());
	}

	@Test
	public void addFields() {
		final EasyDate d = new EasyDate(baseDate); // 2009-02-14 07:31:30 GMT+8 星期六
		// 2011-03-15 08:32:31.123 GMT+8
		d.addYear(2).addMonth(1).addDay(1).addHour(1).addMinute(1).addSecond(1).addMillisecond(123);
		assertEqualsYmdHms(d, 2011, 3, 15, 8, 32, 31);

		// 2008-01-14 05:30:30 GMT+8
		d.addYear(-3).addMonth(-2).addDay(-1).addHour(-3).addMinute(-2).addSecond(-1).addMillisecond(-123);
		assertEqualsYmdHms(d, 2008, 1, 14, 5, 30, 30);

		assertEquals(baseDate.getTime() + 123, d.setDate(baseDate).addMillisecond(123).getTime());
		assertEquals(baseDate.getTime() + 123, d.setDate(baseDate).addTime(123).getTime());
	}

	@Test
	public void isLeapYear() {
		final EasyDate d = new EasyDate();

		assertTrue(EasyDate.isLeapYears(2004));
		assertTrue(d.isLeapYear(2004));

		assertTrue(EasyDate.isLeapYears(2000));
		assertTrue(d.isLeapYear(2000));

		assertFalse(EasyDate.isLeapYears(1900));
		assertFalse(d.isLeapYear(1900));

		assertFalse(EasyDate.isLeapYears(1901));
		assertFalse(d.isLeapYear(1901));

		d.resetAs(2004, 1, 1); // 2004年是闰年
		assertTrue(d.isLeapYear());
		d.resetAs(2000, 1, 1); // 2004年是闰年
		assertTrue(d.isLeapYear());
		d.resetAs(1901, 1, 1); // 1901年不是闰年
		assertFalse(d.isLeapYear());
		d.resetAs(2100, 1, 1); // 2100年不是闰年
		assertFalse(d.isLeapYear());
	}

	@Test(expected = NullPointerException.class)
	public void smartParseDate_NullInput_ThrowsNullPointerException() {
		EasyDate.smartParseDate(null);
	}

	@Test
	public void smartParseDate() {
		assertEquals("2012-01-02 00:00:00.000", new EasyDate(EasyDate.smartParseDate("2012-01-02")).toLongString());
		assertEquals("2012-01-02 13:22:56.000", new EasyDate(EasyDate.smartParseDate("2012-01-02 13:22:56")).toLongString());
		assertEquals("2012-01-26 00:00:00.000", new EasyDate(EasyDate.smartParseDate("20120126")).toLongString());
		assertEquals("2012-06-01 00:00:00.000", new EasyDate(EasyDate.smartParseDate("201206")).toLongString());

		assertEquals("2012-01-02 00:00:00.000", EasyDate.smartParse("2012-01-02").toLongString());
		assertEquals("2012-01-02 13:22:56.000", EasyDate.smartParse("2012-01-02 13:22:56").toLongString());
		assertEquals("2012-01-26 00:00:00.000", EasyDate.smartParse("20120126").toLongString());
		assertEquals("2012-06-01 00:00:00.000", EasyDate.smartParse("201206").toLongString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void smartParseDate_UnexpectedLength_ThrowsIllegalArgumentException() {
		EasyDate.smartParseDate("2012-01");
	}

	@Test(expected = NullPointerException.class)
	public void smartParse_NullInput_ThrowsNullPointerException() {
		EasyDate.smartParse(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void smartParse_UnexpectedLength_ThrowsIllegalArgumentException() {
		EasyDate.smartParse("2012-01");
	}

	@Test
	public void setWeekDay() {
		// 2009-02-14 07:31:30 GMT+8 星期六
		EasyDate d = EasyDate.valueOf(baseCalendar);
		assertEqualsYmdHms(d, 2009, 2, 14, 7, 31, 30);
		d.setWeekDay(1);
		assertEqualsYmdHms(d, 2009, 2, 9, 7, 31, 30);
		d.setWeekDay(3);
		assertEqualsYmdHms(d, 2009, 2, 11, 7, 31, 30);
		d.setWeekDay(7);
		assertEqualsYmdHms(d, 2009, 2, 15, 7, 31, 30);
	}

	@Test
	public void setWeekDay_DifferentWeekDay_ChangesDate() {
		// 2009-02-14 07:31:30 GMT+8 星期六
		EasyDate d = EasyDate.valueOf("2009-02-14 07:31:30");
		assertEqualsYmdHms(d, 2009, 2, 14, 7, 31, 30);
		d.setWeekDay(8);
		assertEqualsYmdHms(d, 2009, 2, 16, 7, 31, 30);
	}

	@Test
	public void setWeekDay_WeekdayWrapAround_CorrectDate() {
		// 2009-02-14 07:31:30 GMT+8 星期六
		EasyDate d = (EasyDate) EasyDate.valueOf(new EasyDate(baseDate.getTime())).clone();
		EasyDate result = d.setWeekDay(-1); // 周一 再减去2天
		assertEqualsYmdHms(result, 2009, 2, 7, 7, 31, 30);
	}

}