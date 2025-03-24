package me.codeplayer.util;

import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EasyDateTest implements WithAssertions {

	static final long BASE_TIME = 1234567890000L; // 2009-02-14 07:31:30 GMT+8 星期六
	static final long EARLIER_TIME = BASE_TIME - 1; // 早于 BASE_TIME 1 ms
	static final long LATER_TIME = BASE_TIME + 1; // 晚于 BASE_TIME 1 ms
	/** 2009-02-14 07:31:30 GMT+8 星期六 */
	static Date baseDate;
	/** 2009-02-14 07:31:30 GMT+8 星期六 */
	static Calendar baseCalendar;

	@BeforeAll
	public static void setUp() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		baseDate = new Date(BASE_TIME); // 2009-02-14 07:31:30 GMT+8
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
		assertEquals(JavaUtil.javaVersion < 9 ? "星期四, 28 二月 2013 23:59:59 GMT" : "周四, 28 2月 2013 23:59:59 GMT", d.toGMTNetString());
		assertEquals(JavaUtil.javaVersion < 9 ? "14 二月 2009 07:31:30 GMT" : "14 2月 2009 07:31:30 GMT", EasyDate.valueOf(baseDate).toGMTString());
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

		assertEquals(0, d.setTimeZoneOffset(0).getTimeZoneOffset());

		assertEquals("2013-02-05 15:12:55", d.toDateTimeString());
		assertEquals("20130205", d.toShortString());
		assertEquals("2013-02-05", d.toString());

		d.setTimeZoneOffset(150);
		assertEquals("2013-02-05 17:42:55.000", d.toLongString());
	}

	@Test
	public void isSameAs() {
		EasyDate a = new EasyDate(2015, 2, 28, 0, 0, 0);
		EasyDate b = new EasyDate(2015, 2, 28, 23, 59, 59);
		assertTrue(a.isSameAs(b, Calendar.YEAR));
		assertTrue(EasyDate.isSameAs(a.getTime(), b.getTime(), Calendar.DATE));

		assertFalse(EasyDate.isSameAs(a, b, Calendar.SECOND));
		assertFalse(a.isSameAs(b, Calendar.HOUR));
		assertTrue(EasyDate.isSameAs(a, b, Calendar.MONTH));
		a.set(2015, 3, 29);
		assertFalse(EasyDate.isSameAs(a, b, Calendar.DATE));
		assertFalse(EasyDate.isSameAs(a, b.toDate(), Calendar.MONTH));
		assertFalse(EasyDate.isSameAs(a, b.getCalendar(), Calendar.SECOND));
		b.setMonth(3);
		// 2015-03-29 00:00:00 GMT+8 VS 2015-03-28 23:59:59 GMT+8:00
		// 2015-03-28 21:30:00 GMT+5:30 VS 2015-03-28 21:29:59 GMT+5:30
		assertTrue(EasyDate.isSameAs(a.getTime(), b.getTime(), Calendar.HOUR, TimeZone.getTimeZone("GMT+5:30")));
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

	@Test
	public void smartParseDate_NullInput_ThrowsNullPointerException() {
		assertThrows(NullPointerException.class, () -> EasyDate.smartParseDate(null));
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

	@Test
	public void smartParseDate_UnexpectedLength_ThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> EasyDate.smartParseDate("2012-01"));
	}

	@Test
	public void smartParse_NullInput_ThrowsNullPointerException() {
		assertThrows(NullPointerException.class, () -> EasyDate.smartParse(null));
	}

	@Test
	public void smartParse_UnexpectedLength_ThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> EasyDate.smartParse("2012-01"));
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

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(EasyDate.DATE);

	@Test
	public void parse_ValidDateString_ReturnsEasyDate() {
		String validDate = "2023-10-05";
		EasyDate result = EasyDate.parse(dateFormat, validDate);
		assertEquals(validDate, result.toString());

		EasyDate result2 = EasyDate.parse(EasyDate.DATE, validDate);
		assertEquals(validDate, result2.toString());
	}

	@Test
	public void parse_InvalidDateString_ThrowsIllegalArgumentException() {
		String invalidDate = "invalid-date";
		assertThrows(IllegalArgumentException.class, () -> EasyDate.parse(dateFormat, invalidDate));
	}

	public static StringBuilder tempateFor(String pattern) {
		return new StringBuilder(pattern.length()).append(pattern);
	}

	@Test
	public void formatNormalDateTime_ValidInputs_FormatsCorrectly() {
		final StringBuilder chars = tempateFor("0000-00-00 00:00:00");
		EasyDate.formatNormalDateTime(chars, 2023, 10, 5, 14, 30, 45);
		assertEquals("2023-10-05 14:30:45", chars.toString());
	}

	@Test
	public void formatNormalDateTime_MinimumValues_FormatsCorrectly() {
		final StringBuilder chars = tempateFor("0000-00-00 00:00:00");
		EasyDate.formatNormalDateTime(chars, 0, 1, 1, 0, 0, 0);
		assertEquals("0000-01-01 00:00:00", chars.toString());
	}

	@Test
	public void formatNormalDateTime_MaximumValues_FormatsCorrectly() {
		final StringBuilder chars = tempateFor("0000-00-00 00:00:00");
		EasyDate.formatNormalDateTime(chars, 9999, 12, 31, 23, 59, 59);
		assertEquals("9999-12-31 23:59:59", chars.toString());
	}

	@Test
	public void formatNormalDateTime_SingleDigitMonthDay_FormatsCorrectly() {
		final StringBuilder chars = tempateFor("0000-00-00 00:00:00");
		EasyDate.formatNormalDateTime(chars, 2023, 1, 1, 1, 1, 1);
		assertEquals("2023-01-01 01:01:01", chars.toString());
	}

	@Test
	public void before() {
		final EasyDate base = new EasyDate(baseDate);
		assertFalse(base.before(base)); // ==

		EasyDate earlierDate = new EasyDate(EARLIER_TIME);
		assertTrue(earlierDate.before(base)); // <

		EasyDate laterDate = new EasyDate(LATER_TIME);
		assertFalse(laterDate.before(base)); // >

		assertThrows(NullPointerException.class, () -> base.before(null));
	}

	@Test
	public void after() {
		final EasyDate baseDate = new EasyDate(BASE_TIME);
		assertFalse(baseDate.after(baseDate)); // ==

		EasyDate earlierDate = new EasyDate(EARLIER_TIME);
		assertFalse(earlierDate.after(baseDate)); // <

		EasyDate laterDate = new EasyDate(LATER_TIME);
		assertTrue(laterDate.after(baseDate)); // >

		assertThrows(NullPointerException.class, () -> baseDate.after(null));
	}

	@Test
	public void calcDifference_SameDate_ShouldReturnZero() {
		EasyDate date = new EasyDate(BASE_TIME);
		assertEquals(0, date.calcDifference(new EasyDate(BASE_TIME)));
		assertEquals(0, date.calcDifference(baseDate, Calendar.YEAR));
		assertEquals(0, date.calcDifference(baseCalendar, Calendar.MONTH));
		assertEquals(0, date.calcDifference(baseCalendar, Calendar.HOUR));
		assertEquals(0, date.calcDifference(baseCalendar, Calendar.MINUTE));
		assertEquals(0, date.calcDifference(baseCalendar, Calendar.SECOND));
	}

	@Test
	public void calcDifference_DifferentYears_ShouldReturnYearDifference() {
		EasyDate a = new EasyDate(2013, 1, 1);
		EasyDate b = new EasyDate(2012, 1, 1);

		// = 1年
		assertEquals(1, a.calcDifference(b, Calendar.YEAR)); // RoundingMode.UP
		assertEquals(1, a.calcDifference(b, Calendar.YEAR, RoundingMode.DOWN));

		b.addSecond(1); // 刚刚 < 1年  "2012-01-01 00:00:01.000"

		assertEquals(1, a.calcDifference(b, Calendar.YEAR)); // RoundingMode.UP
		assertEquals(-1, b.calcDifference(a, Calendar.YEAR, RoundingMode.UP));

		assertEquals(0, a.calcDifference(b, Calendar.YEAR, RoundingMode.FLOOR));
		assertEquals(-1, b.calcDifference(a, Calendar.YEAR, RoundingMode.FLOOR));

		assertEquals(1, a.calcDifference(b, Calendar.YEAR, RoundingMode.CEILING));
		assertEquals(0, b.calcDifference(a, Calendar.YEAR, RoundingMode.CEILING));

		assertEquals(0, a.calcDifference(b, Calendar.YEAR, RoundingMode.DOWN));
		assertEquals(0, b.calcDifference(a, Calendar.YEAR, RoundingMode.DOWN));

		assertEquals(366, a.calcDifference(b, Calendar.DATE)); // RoundingMode.UP
		assertEquals(-366, b.calcDifference(a, Calendar.DATE, RoundingMode.UP));

		assertEquals(365, a.calcDifference(b, Calendar.DATE, RoundingMode.FLOOR));
		assertEquals(-366, b.calcDifference(a, Calendar.DATE, RoundingMode.FLOOR));

		assertEquals(366, a.calcDifference(b, Calendar.DATE, RoundingMode.CEILING));
		assertEquals(-365, b.calcDifference(a, Calendar.DATE, RoundingMode.CEILING));

		assertEquals(365, a.calcDifference(b, Calendar.DATE, RoundingMode.DOWN));
		assertEquals(-365, b.calcDifference(a, Calendar.DATE, RoundingMode.DOWN));

		b.setSecond(0); // "2012-01-01 00:00:00.000"
		a.addDay(-183).addSecond(1); // > 刚刚 一半  "2012-07-02 00:00:01.000"

		assertEquals(1, a.calcDifference(b, Calendar.YEAR, RoundingMode.HALF_UP));
		assertEquals(-1, b.calcDifference(a, Calendar.YEAR, RoundingMode.HALF_UP));

		assertEquals(1, a.calcDifference(b, Calendar.YEAR, RoundingMode.HALF_DOWN));
		assertEquals(-1, b.calcDifference(a, Calendar.YEAR, RoundingMode.HALF_DOWN));

		assertEquals(1, a.calcDifference(b, Calendar.YEAR, RoundingMode.HALF_EVEN));
		assertEquals(-1, b.calcDifference(a, Calendar.YEAR, RoundingMode.HALF_EVEN));

		a.setSecond(0); // == 一半  "2012-07-02 00:00:00.000"

		assertEquals(1, a.calcDifference(b, Calendar.YEAR, RoundingMode.HALF_UP));
		assertEquals(-1, b.calcDifference(a, Calendar.YEAR, RoundingMode.HALF_UP));

		assertEquals(0, a.calcDifference(b, Calendar.YEAR, RoundingMode.HALF_DOWN));
		assertEquals(0, b.calcDifference(a, Calendar.YEAR, RoundingMode.HALF_DOWN));

		assertEquals(0, a.calcDifference(b, Calendar.YEAR, RoundingMode.HALF_EVEN));
		assertEquals(0, b.calcDifference(a, Calendar.YEAR, RoundingMode.HALF_EVEN));

		a.addSecond(-1); // < 一半  "2012-07-01 23:59:59.000"

		assertEquals(0, a.calcDifference(b, Calendar.YEAR, RoundingMode.HALF_UP));
		assertEquals(0, b.calcDifference(a, Calendar.YEAR, RoundingMode.HALF_UP));

		assertEquals(0, a.calcDifference(b, Calendar.YEAR, RoundingMode.HALF_DOWN));
		assertEquals(0, b.calcDifference(a, Calendar.YEAR, RoundingMode.HALF_DOWN));

		assertEquals(0, a.calcDifference(b, Calendar.YEAR, RoundingMode.HALF_EVEN));
		assertEquals(0, b.calcDifference(a, Calendar.YEAR, RoundingMode.HALF_EVEN));

		a.addSecond(1); // "2012-07-02 00:00:00.000"
		b.addYear(-1); // "2011-01-01 00:00:00.000"
		assertEquals(2, a.calcDifference(b, Calendar.YEAR, RoundingMode.HALF_EVEN));
		assertEquals(-2, b.calcDifference(a, Calendar.YEAR, RoundingMode.HALF_EVEN));
	}

	@Test
	public void calcDifference_DifferentMonths_ShouldReturnMonthDifference() {
		EasyDate a = new EasyDate(2015, 2, 1);
		EasyDate b = new EasyDate(2015, 1, 1);
		assertEquals(1, a.calcDifference(b, Calendar.MONTH));
		assertEquals(-1, b.calcDifference(a, Calendar.MONTH));
	}

	@Test
	public void calcDifference_DifferentDays_ShouldReturnDayDifference() {
		EasyDate a = new EasyDate(2015, 1, 6);
		EasyDate b = new EasyDate(2015, 1, 1);
		assertEquals(5, a.calcDifference(b, Calendar.DATE));

		b.addHour(12);

		assertEquals(5, a.calcDifference(b, Calendar.DATE, RoundingMode.HALF_UP));
		assertEquals(-5, b.calcDifference(a, Calendar.DATE, RoundingMode.HALF_UP));

		assertEquals(4, a.calcDifference(b, Calendar.DATE, RoundingMode.HALF_DOWN));
		assertEquals(-4, b.calcDifference(a, Calendar.DATE, RoundingMode.HALF_DOWN));

		assertEquals(4, a.calcDifference(b, Calendar.DATE, RoundingMode.HALF_EVEN));
		assertEquals(-4, b.calcDifference(a, Calendar.DATE, RoundingMode.HALF_EVEN));

		a.addDay(1);
		assertEquals(6, a.calcDifference(b, Calendar.DATE, RoundingMode.HALF_EVEN));
		assertEquals(-6, b.calcDifference(a, Calendar.DATE, RoundingMode.HALF_EVEN));
	}

	@Test
	public void calcDifference_DifferentHours_ShouldReturnHourDifference() {
		EasyDate a = new EasyDate(2015, 1, 1, 1, 0, 0);
		EasyDate b = new EasyDate(2015, 1, 1, 0, 0, 0);
		assertEquals(1, a.calcDifference(b, Calendar.HOUR_OF_DAY));
	}

	@Test
	public void calcDifference_DifferentMinutes_ShouldReturnMinuteDifference() {
		EasyDate a = new EasyDate(2015, 1, 1, 0, 1, 0);
		EasyDate b = new EasyDate(2015, 1, 1, 0, 0, 0);
		assertEquals(1, a.calcDifference(b, Calendar.MINUTE));
	}

	@Test
	public void calcDifference_DifferentSeconds_ShouldReturnSecondDifference() {
		EasyDate a = new EasyDate(2015, 1, 1, 0, 0, 1);
		EasyDate b = new EasyDate(2015, 1, 1, 0, 0, 0);
		assertEquals(1, a.calcDifference(b, Calendar.SECOND));
	}

	@Test
	public void calcDifference_DifferentMillis_ShouldReturnMillisecondDifference() {
		EasyDate a = new EasyDate(BASE_TIME);
		EasyDate b = new EasyDate(EARLIER_TIME);
		assertEquals(1, a.calcDifference(b, Calendar.MILLISECOND));
	}

	@Test
	public void calcDifference_InvalidField_ShouldThrowIllegalArgumentException() {
		EasyDate a = new EasyDate(BASE_TIME);
		assertThrows(IllegalArgumentException.class, () -> a.calcDifference(new EasyDate(EARLIER_TIME), 999));
	}

	@Test
	public void toTime_WithCurrentTime_ShouldReturnCorrectTime() {
		final long nowInMs = System.currentTimeMillis();
		EasyDate easyDate = new EasyDate(nowInMs);
		assertEquals(new java.sql.Time(nowInMs), easyDate.toTime());
	}

	@Test
	public void toTime_WithSpecificDate_ShouldReturnCorrectTime() {
		EasyDate easyDate = new EasyDate(2009, 2, 14, 7, 31, 30);
		java.sql.Time time = easyDate.toTime();
		assertEquals(new java.sql.Time(7, 31, 30).toString(), time.toString());
	}

	@Test
	public void toTime_WithSpecificMillis_ShouldReturnCorrectTime() {
		EasyDate easyDate = new EasyDate(BASE_TIME);
		java.sql.Time time = easyDate.toTime();
		assertEquals(new java.sql.Time(BASE_TIME), time);
	}

	@Test
	public void formatNormalDateTime() {
		StringBuilder sb = new StringBuilder("0000-00-00 00:00:00");
		EasyDate.formatNormalDateTime(sb, 2023, 10, 5, 14, 30, 45);
		assertEquals("2023-10-05 14:30:45", sb.toString());

		EasyDate.formatNormalDate(sb, 2023, 10, 5);
		EasyDate.formatNormalTime(sb, 14, 30, 45);
		assertEquals("2023-10-05 14:30:45", sb.toString());

		sb = new StringBuilder("0000年00月00日 00时00分00秒");
		EasyDate.formatNormalDate(sb, 2023, 10, 5);
		EasyDate.formatNormalTime(sb, 14, 12, 30, 15, 45, 18);
		assertEquals("2023年10月05日 14时30分45秒", sb.toString());
	}

	public void pickChars() {
		StringBuilder sb = new StringBuilder();
	}

}