package me.codeplayer.util;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.Date;
import java.util.*;

import javax.annotation.*;

import org.apache.commons.lang3.time.*;

import static java.util.Calendar.*;

/**
 * 实现常用日期扩展方法的日期工具类(实现Comparable可比较接口、Cloneable可复制接口)
 *
 * @author Ready
 * @since 2012-9-24
 */
public class EasyDate implements Comparable<Object>, Cloneable, Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * yyyy-MM-dd 格式的日期转换器
	 */
	public static final String DATE = "yyyy-MM-dd";
	/**
	 * yyyy-MM-dd HH:mm:ss 格式的日期转换器
	 */
	public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyyMMdd 格式的日期转换器
	 */
	public static final String SHORT_DATE = "yyyyMMdd";
	/**
	 * yyyyMM 格式的日期转换器
	 */
	public static final String YM_DATE = "yyyyMM";
	/**
	 * GMT标准格式的日期转换器[d MMM yyyy HH:mm:ss 'GMT']
	 */
	public static final String GMT_DATE = "d MMM yyyy HH:mm:ss 'GMT'";
	/**
	 * Internet GMT标准格式的日期转换器[EEE, d MMM yyyy HH:mm:ss 'GMT']
	 */
	public static final String GMT_NET_DATE = "EEE, d MMM yyyy HH:mm:ss 'GMT'";
	/**
	 * 一分钟的毫秒数
	 */
	public static final long MILLIS_OF_MINUTE = 1000 * 60;
	/**
	 * 一小时的毫秒数
	 */
	public static final long MILLIS_OF_HOUR = MILLIS_OF_MINUTE * 60;
	/**
	 * 一天的毫秒数
	 */
	public static final long MILLIS_OF_DAY = MILLIS_OF_HOUR * 24;
	private Calendar calendar;

	/**
	 * 初始化日历对象相关设置
	 */
	protected void initCalendar(Calendar calendar) {
		this.calendar = calendar;
		this.calendar.setLenient(false);
		this.calendar.setFirstDayOfWeek(MONDAY);
	}

	/**
	 * 将常用日期对象封装为当前日期类实例对象
	 */
	public EasyDate(java.util.Date date) {
		this(date.getTime());
	}

	/**
	 * 构造一个当前时间的日期实例对象
	 */
	public EasyDate() {
		initCalendar(new GregorianCalendar());
	}

	/**
	 * 根据指定的毫秒数构造对应的实例对象
	 */
	public EasyDate(long date) {
		this();
		setTime(date);
	}

	/**
	 * 根据相对于指定时间的偏移值构造一个对应的实例对象<br>
	 * 例如，当前时间为：2012-10-10 例如要创建一个2013-10-10的时间对象，new EasyDate(null, 1, 0, 0)即可;<br>
	 * 创建一个2011-8-10的时间对象，new EasyDate(null, -1, -2, 0)或new EasyDate(null, 0, -14, 0)
	 *
	 * @param date 指定的时间，作为偏移量的参考对象，如果为null，则默认使用当前时间作为参考对象<br>
	 * 该对象支持java.util.Date、me.codeplayer.util.EasyDate、java.util. Calendar等对象及其子类实例
	 * @param offsetYear 相对于当前时间的年份偏移量
	 * @param offsetMonth 相对于当前时间的月份偏移量
	 * @param offsetDay 相对于当前时间的日期偏移量
	 */
	private EasyDate(@Nullable Object date, int offsetYear, int offsetMonth, int offsetDay) {
		this(getTimeOfDate(date));
		if (offsetYear != 0) {
			calendar.add(YEAR, offsetYear);
		}
		if (offsetMonth != 0) {
			calendar.add(MONTH, offsetMonth);
		}
		if (offsetDay != 0) {
			calendar.add(DAY_OF_MONTH, offsetDay);
		}
	}

	/**
	 * 根据相对于指定时间的偏移值构造一个对应的实例对象<br>
	 * 例如，当前时间为：2012-10-10 例如要创建一个2013-10-10的时间对象，new EasyDate(null, 1, 0, 0)即可;<br>
	 * 创建一个2011-8-10的时间对象，new EasyDate(null, -1, -2, 0)或new EasyDate(null, 0, -14, 0)
	 *
	 * @param date 指定的时间，作为偏移量的参考对象，如果为null，则默认使用当前时间作为参考对象<br>
	 * 该对象支持{@link java.util.Date}、{@link me.codeplayer.util.EasyDate}、 {@link java.util.Calendar}等对象及其子类实例
	 * @param offsetYear 相对于当前时间的年份偏移量
	 * @param offsetMonth 相对于当前时间的月份偏移量
	 * @param offsetDay 相对于当前时间的日期偏移量
	 */
	public EasyDate(Date date, int offsetYear, int offsetMonth, int offsetDay) {
		this((Object) date, offsetYear, offsetMonth, offsetDay);
	}

	/**
	 * 根据相对于指定时间的偏移值构造一个对应的实例对象<br>
	 * 例如，当前时间为：2012-10-10 例如要创建一个2013-10-10的时间对象，new EasyDate(null, 1, 0, 0)即可;<br>
	 * 创建一个2011-8-10的时间对象，new EasyDate(null, -1, -2, 0)或new EasyDate(null, 0, -14, 0)
	 *
	 * @param date 指定的时间，作为偏移量的参考对象，如果为null，则默认使用当前时间作为参考对象<br>
	 * 该对象支持{@link java.util.Date}、{@link me.codeplayer.util.EasyDate}、 {@link java.util.Calendar}等对象及其子类实例
	 * @param offsetYear 相对于当前时间的年份偏移量
	 * @param offsetMonth 相对于当前时间的月份偏移量
	 * @param offsetDay 相对于当前时间的日期偏移量
	 */
	public EasyDate(EasyDate date, int offsetYear, int offsetMonth, int offsetDay) {
		this((Object) date, offsetYear, offsetMonth, offsetDay);
	}

	/**
	 * 根据相对于指定时间的偏移值构造一个对应的实例对象<br>
	 * 例如，当前时间为：2012-10-10 例如要创建一个2013-10-10的时间对象，new EasyDate(null, 1, 0, 0)即可;<br>
	 * 创建一个2011-8-10的时间对象，new EasyDate(null, -1, -2, 0)或new EasyDate(null, 0, -14, 0)
	 *
	 * @param date 指定的日历对象，作为偏移量的参考对象，如果为null，则默认使用当前时间作为参考对象<br>
	 * @param offsetYear 相对于当前时间的年份偏移量
	 * @param offsetMonth 相对于当前时间的月份偏移量
	 * @param offsetDay 相对于当前时间的日期偏移量
	 */
	public EasyDate(Calendar date, int offsetYear, int offsetMonth, int offsetDay) {
		this((Object) date, offsetYear, offsetMonth, offsetDay);
	}

	/**
	 * 返回自 1970 年 1 月 1 日 00:00:00 GMT 以来指定日期对象表示的毫秒数。<br>
	 * 如果为null，则默认为当前时间
	 */
	public static long getTimeOfDate(@Nullable Object date) {
		long theTime;
		if (date == null) {
			theTime = System.currentTimeMillis();
		} else if (date instanceof Date) {
			theTime = ((Date) date).getTime();
		} else if (date instanceof EasyDate) {
			theTime = ((EasyDate) date).getTime();
		} else if (date instanceof Calendar) {
			theTime = ((Calendar) date).getTimeInMillis();
		} else {
			throw new ClassCastException(date.getClass().getName());
		}
		return theTime;
	}

	/**
	 * 根据年、月、日、时、分、秒、毫秒部分的值构造对应的实例对象
	 *
	 * @param year 年份，如2012
	 * @param month 月份，如12
	 * @param day 日
	 * @param args （可选，依次为）时、分、秒、毫秒
	 */
	public EasyDate(int year, int month, int day, int... args) {
		this();
		resetAs(year, month, day, args);
	}

	/**
	 * 根据年、月、日、时、分、秒、毫秒部分的值重置当前实例对象
	 *
	 * @param year 年份，如2012
	 * @param month 月份，如12
	 * @param day 日
	 * @param args （可选，依次为）时、分、秒、毫秒（均默认为 0）
	 */
	public EasyDate resetAs(final int year, final int month, final int day, final int... args) {
		final int size = args == null ? 0 : args.length;
		return setYear(year)
				.setMonth(month)
				.setDay(day)
				.setHour(size > 0 ? args[0] : 0)
				.setMinute(size > 1 ? args[1] : 0)
				.setSecond(size > 2 ? args[2] : 0)
				.setMillisecond(size > 3 ? args[3] : 0);

	}

	/**
	 * 获取日期的年，例如：2012
	 */
	public int getYear() {
		return calendar.get(YEAR);
	}

	/**
	 * 设置日期的年，例如：2012
	 */
	public EasyDate setYear(int year) {
		calendar.set(YEAR, year);
		return this;
	}

	/**
	 * 追加指定的年数，例如：当前年是2012，调用addYear(2)，则年份为2014
	 *
	 * @param year 指定的年数，可以为负数
	 */
	public EasyDate addYear(int year) {
		calendar.add(YEAR, year);
		return this;
	}

	/**
	 * 获取日期的月；返回值为1(一月)~12(十二月)
	 */
	public int getMonth() {
		return calendar.get(MONTH) + 1;
	}

	/**
	 * 设置日期的月；值为1(一月)~12(十二月)
	 */
	public EasyDate setMonth(int month) {
		calendar.set(MONTH, month - 1);
		return this;
	}

	/**
	 * 追加指定的月数，例如：当前是2012-05-12，调用addMonth(2)，则为2012-07-12
	 *
	 * @param month 指定的月数，可以为负数
	 */
	public EasyDate addMonth(int month) {
		calendar.add(MONTH, month);
		return this;
	}

	/**
	 * 设置日期的年、月、日、时、分、秒、毫秒等部分的值<br>
	 * 如果未指定指定部分的值，则不会进行该部分的设置
	 *
	 * @since 0.3
	 */
	public EasyDate set(int year, int month, int day, int... args) {
		calendar.set(YEAR, year);
		calendar.set(MONTH, month - 1);
		calendar.set(DAY_OF_MONTH, day);
		if (args.length > 0) {
			int[] fields = new int[] { HOUR_OF_DAY, MINUTE, SECOND, MILLISECOND };
			int i = 0;
			do {
				calendar.set(fields[i], args[i]);
			} while (++i < args.length);
		}
		return this;
	}

	/**
	 * 获取日期的日；月份的第一天返回1
	 */
	public int getDay() {
		return calendar.get(DAY_OF_MONTH);
	}

	/**
	 * 获取指定的日期是该年的第几天
	 */
	public int getDayOfYear() {
		return calendar.get(DAY_OF_YEAR);
	}

	/**
	 * 设置日期的日；月份的第一天为1
	 */
	public EasyDate setDay(int day) {
		calendar.set(DAY_OF_MONTH, day);
		return this;
	}

	/**
	 * 追加指定的天数，例如：当前是2012-05-12，调用addDay(2)，则为2012-05-14
	 *
	 * @param day 指定的天数，可以为负数
	 */
	public EasyDate addDay(int day) {
		calendar.add(DAY_OF_MONTH, day);
		return this;
	}

	/**
	 * 获取日期的星期；返回值为1(星期一)~7(星期天)
	 */
	public int getWeekDay() {
		int weekday = calendar.get(DAY_OF_WEEK);
		return weekday == SUNDAY ? 7 : --weekday;
	}

	/**
	 * 获取日期的时，返回值0~23
	 */
	public int getHour() {
		return calendar.get(HOUR_OF_DAY);
	}

	/**
	 * 设置日期的时，值为0~23
	 */
	public EasyDate setHour(int hour) {
		calendar.set(HOUR_OF_DAY, hour);
		return this;
	}

	/**
	 * 追加指定的小时数，例如：当前是2012-05-12 12:12:56，调用addHour(3)，则为2012-05-12 15:12:56
	 *
	 * @param hour 指定的小时数，可以为负数
	 */
	public EasyDate addHour(int hour) {
		calendar.add(HOUR_OF_DAY, hour);
		return this;
	}

	/**
	 * 获取日期的分，返回值0~59
	 */
	public int getMinute() {
		return calendar.get(MINUTE);
	}

	/**
	 * 设置日期的分，值为0~59
	 *
	 * @param minute 指定的分钟数
	 */
	public EasyDate setMinute(int minute) {
		calendar.set(MINUTE, minute);
		return this;
	}

	/**
	 * 追加指定的分钟数，例如：当前是2012-05-12 09:12:56，调用addMinute(3)，则为2012-05-12 09:15:56
	 *
	 * @param minute 指定的分钟数，可以为负数
	 */
	public EasyDate addMinute(int minute) {
		calendar.add(MINUTE, minute);
		return this;
	}

	/**
	 * 获取日期的秒，返回值0~59
	 */
	public int getSecond() {
		return calendar.get(SECOND);
	}

	/**
	 * 设置日期的秒，值为0~59
	 */
	public EasyDate setSecond(int second) {
		calendar.set(SECOND, second);
		return this;
	}

	/**
	 * 追加指定的秒数，例如：当前是2012-05-12 09:12:56 123，调用addMillisecond(123)，则为2012-05-12 09:12:56 246
	 *
	 * @param ms 指定的毫秒数，可以为负数
	 */
	public EasyDate addMillisecond(int ms) {
		calendar.add(MILLISECOND, ms);
		return this;
	}

	/**
	 * 获取日期的毫秒部分的值，返回值0~999
	 */
	public int getMillisecond() {
		return calendar.get(MILLISECOND);
	}

	/**
	 * 设置日期的毫秒部分的值，值为0~999
	 */
	public EasyDate setMillisecond(int ms) {
		calendar.set(MILLISECOND, ms);
		return this;
	}

	/**
	 * 追加指定的秒数，例如：当前是2012-05-12 09:12:56，调用addSecond(3)，则为2012-05-12 09:12:59
	 *
	 * @param second 指定的秒数，可以为负数
	 */
	public EasyDate addSecond(int second) {
		calendar.add(SECOND, second);
		return this;
	}

	/**
	 * 获取日期的时间值，以毫秒为单位
	 */
	public long getTime() {
		return calendar.getTimeInMillis();
	}

	/**
	 * 设置日期的时间值，以毫秒为单位
	 */
	public EasyDate setTime(long date) {
		calendar.setTimeInMillis(date);
		return this;
	}

	/**
	 * 追加指定的毫秒数
	 *
	 * @param time 指定的毫秒数，可以为负数
	 */
	public EasyDate addTime(long time) {
		calendar.setTimeInMillis(calendar.getTimeInMillis() + time);
		return this;
	}

	/**
	 * 以指定日期对象来重新设置日期
	 */
	public EasyDate setDate(java.util.Date date) {
		calendar.setTime(date);
		return this;
	}

	/**
	 * 获取日期当前月份的星期数
	 */
	public int getWeeksOfMonth() {
		return calendar.get(WEEK_OF_MONTH);
	}

	/**
	 * 获取日期当前年份的星期数
	 */
	public int getWeeksOfYear() {
		return calendar.get(WEEK_OF_YEAR);
	}

	/**
	 * 获取内置的日历对象
	 */
	public Calendar getCalendar() {
		return this.calendar;
	}

	/**
	 * 根据日期字符串的长度智能转换为对应的日期实例对象<br>
	 * 长度和格式对应如下(找不到对应格式将报错)：<br>
	 * 6=201206(年月)<br>
	 * 8=20120126(年月日)<br>
	 * 10=2012-01-02(年-月-日)<br>
	 * 19=2012-01-02 13:22:56(年-月-日 时:分:秒)
	 */
	public static Date smartParseDate(String date) {
		if (date == null) {
			throw new NullPointerException();
		}
		int length = date.length(); // 字符串长度
		String format;
		switch (length) {
		case 10:// 2012-01-02
			format = DATE;
			break;
		case 19:// 2012-01-02 13:22:56
			format = DATETIME;
			break;
		case 8:// 20120126
			format = SHORT_DATE;
			break;
		case 6:// 201206
			format = YM_DATE;
			break;
		default:
			throw new IllegalArgumentException("Unable to parse the date string because of unexpected format:" + date);
		}
		return parseDate(format, date);
	}

	/**
	 * 根据日期字符串的长度智能转换为对应的日期实例对象<br>
	 * 长度和格式对应如下(找不到对应格式将报错)：<br>
	 * 6=201206(年月)<br>
	 * 8=20120126(年月日)<br>
	 * 10=2012-01-02(年-月-日)<br>
	 * 19=2012-01-02 13:22:56(年-月-日 时:分:秒)
	 */
	public static EasyDate smartParse(String date) {
		return new EasyDate(smartParseDate(date));
	}

	/**
	 * 将指定的java.util.Calendar对象转为EasyDate日期对象<br>
	 * 如果指定对象对null，则返回null
	 */
	public static EasyDate valueOf(final Object dateObj) {
		if (dateObj == null) {
			throw new NullPointerException();
		}
		if (dateObj instanceof EasyDate) {
			return (EasyDate) dateObj;
		}
		return new EasyDate(getTimeOfDate(dateObj));
	}

	/**
	 * 将指定的字符串转为EasyDate日期对象<br>
	 * 如果指定对象对null，则返回null
	 */
	public static EasyDate valueOf(String date) {
		if (date == null) {
			return null;
		}
		return smartParse(date);
	}

	/**
	 * 将指定格式的字符串转为对应的日期实例对象
	 *
	 * @param format 一般情况无需自己创建，可直接调用EasyDate.DATE、EasyDate.DATETIME、EasyDate. SHORT_DATE等内置的日期转换对象
	 * @param date 日期字符串
	 */
	public static EasyDate parse(DateFormat format, String date) {
		try {
			return new EasyDate(format.parse(date));
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 将指定格式的字符串转为对应的日期实例对象
	 *
	 * @param format 指定的格式字符串，例如“yyyy-MM-dd”，内部将使用 {@link FastDateFormat} 进行转换
	 * @param dateStr 日期字符串
	 */
	public static Date parseDate(String format, String dateStr) {
		FastDateFormat formatter = FastDateFormat.getInstance(format);
		final Date date;
		try {
			date = formatter.parse(dateStr);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
		return date;
	}

	/**
	 * 将指定格式的字符串转为对应的日期实例对象
	 *
	 * @param format 指定的格式字符串，例如“yyyy-MM-dd”，内部将使用 {@link FastDateFormat} 进行转换
	 * @param dateStr 日期字符串
	 */
	public static EasyDate parse(String format, String dateStr) {
		return new EasyDate(parseDate(format, dateStr));
	}

	/**
	 * 转为java.util.Date
	 */
	public java.util.Date toDate() {
		return new java.util.Date(calendar.getTimeInMillis());
	}

	/**
	 * 转为java.sql.Date
	 */
	public java.sql.Date toSqlDate() {
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	/**
	 * 转为java.sql.Timestamp
	 */
	public Timestamp toTimestamp() {
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 转为java.sql.Time
	 */
	public Time toTime() {
		return new Time(calendar.getTimeInMillis());
	}

	/**
	 * 与指定日期进行比较，如果大于指定的日期返回正数；等于返回0；小于返回负数
	 *
	 * @param date 支持 {@link Date}、{@link Calendar}、{@link EasyDate} 等对象及其子类的比较
	 */
	public int compareTo(Object date) {
		if (this == date) {
			return 0;
		}
		if (date == null) {
			throw new NullPointerException();
		}
		long diff = calendar.getTimeInMillis() - getTimeOfDate(date);
		if (diff == 0)
			return 0;
		return diff > 0 ? 1 : -1;
	}

	/**
	 * 计算并返回当前日期与指定日期之间基于指定单位和舍入模式的差值<br>
	 * 如果当前日期大于等于指定日期，则返回正数，否则返回负数
	 *
	 * @param date 与当前日期进行比较的日期
	 * @param field 指定的日期字段，返回值将以此为单位返回两个日期的差距值
	 * @param roundingMode 舍入模式
	 */
	public long calcDifference(Object date, int field, RoundingMode roundingMode) {
		if (date == null) {
			throw new NullPointerException();
		}
		long theMillis = getTimeOfDate(date),
				diff = getTime() - theMillis; // 毫秒值差距

		if (diff == 0) {
			return 0;
		}
		switch (field) {
		case YEAR:
		case MONTH:
			boolean isMax = diff > 0;
			EasyDate me = new EasyDate(getTime());
			EasyDate other = new EasyDate(theMillis);
			EasyDate min = isMax ? other : me,
					max = isMax ? me : other;
			int diffOfYear = max.getYear() - min.getYear();
			if (diffOfYear > 0) {
				min.addYear(diffOfYear);
			}
			if (field == MONTH) {
				int diffOfMonth = max.getMonth() - min.getMonth();
				if (diffOfMonth != 0) {
					min.addMonth(diffOfMonth);
				}
				diff = diffOfYear * 12L + diffOfMonth;
			} else {
				diff = diffOfYear;
			}
			long prefix = max.getTime() - min.getTime();
			if (prefix == 0) {
				return diff;
			}
			long suffix;
			if (prefix > 0) {
				min.calendar.add(field, 1);
				suffix = min.getTime() - max.getTime();
			} else {
				suffix = -prefix;
				diff--;
				min.calendar.add(field, -1);
				prefix = max.getTime() - min.getTime();
			}
			double base = suffix > prefix ? 0.1 : 0.9;
			diff = new BigDecimal(diff + base).setScale(0, roundingMode).longValue();
			return isMax ? diff : -diff;
		default:
			long unit = getMillisOfUnit(field);
			return BigDecimal.valueOf(diff).divide(BigDecimal.valueOf(unit), roundingMode).longValue();
		}
	}

	/**
	 * 计算并返回当前日期与指定日期之间基于指定单位和向上取整模式的差值<br>
	 * 如果当前日期大于等于指定日期，则返回正数，否则返回负数
	 *
	 * @param date 与当前日期进行比较的日期
	 * @param field 指定的日期字段，返回值将以此为单位返回两个日期的差距值
	 */
	public long calcDifference(Object date, int field) {
		return calcDifference(date, field, RoundingMode.CEILING);
	}

	/**
	 * 计算并返回当前日期与指定日期之间基于向上取整模式的天数差值<br>
	 * 如果当前日期大于等于指定日期，则返回正数，否则返回负数
	 *
	 * @param dateObj 与当前日期进行比较的日期
	 */
	public int calcDifference(Object dateObj) {
		return (int) calcDifference(dateObj, DAY_OF_MONTH, RoundingMode.CEILING);
	}

	/**
	 * 判断指定年份是否为闰年
	 *
	 * @param year 例如2012
	 */
	public boolean isLeapYear(int year) {
		return ((GregorianCalendar) calendar).isLeapYear(year);
	}

	/**
	 * 判断当前日期年份是否为闰年
	 */
	public boolean isLeapYear() {
		return ((GregorianCalendar) calendar).isLeapYear(getYear());
	}

	/**
	 * 获取指定日历单位所对应的毫秒值，单位仅支持"天"及其以下的单位
	 *
	 * @param field 该方法支持的字段有{@link Calendar#YEAR}、{@link Calendar#MONTH}、 {@link Calendar#DAY_OF_MONTH}、 {@link Calendar#HOUR_OF_DAY}、 {@link Calendar#MINUTE}、{@link Calendar#SECOND}
	 * @author Ready
	 * @since 0.3.6
	 */
	public static long getMillisOfUnit(int field) {
		switch (field) {
		case DAY_OF_YEAR:
		case DAY_OF_MONTH:
			return MILLIS_OF_DAY;
		case HOUR:
		case HOUR_OF_DAY:
			return MILLIS_OF_HOUR;
		case MINUTE:
			return MILLIS_OF_MINUTE;
		case SECOND:
			return 1000;
		case MILLISECOND:
			return 1;
		default:
			throw new IllegalArgumentException(String.valueOf(field));
		}
	}

	/**
	 * 比较两个以毫秒数表示的时间值是否处于同一年/月/天/小时/分钟/秒/毫秒
	 *
	 * @param a 时间 a
	 * @param b 时间 b
	 * @param inField 指定用于判断是否为同一值的时间单位字段，可以使用Calendar类的单位常量
	 * @author Ready
	 * @see Calendar#YEAR
	 * @see Calendar#MONTH
	 * @see Calendar#WEEK_OF_YEAR
	 * @see Calendar#WEEK_OF_MONTH
	 * @see Calendar#DAY_OF_YEAR
	 * @see Calendar#DAY_OF_MONTH
	 * @see Calendar#HOUR
	 * @see Calendar#HOUR_OF_DAY
	 * @see Calendar#MINUTE
	 * @see Calendar#SECOND
	 * @see Calendar#MILLISECOND
	 * @since 0.3.6
	 */
	public static boolean isSameAs(long a, long b, int inField) {
		if (a == b) {
			return true;
		}
		if (inField > WEEK_OF_MONTH) {
			long unit = getMillisOfUnit(inField);
			long diff = a - b;
			if (diff > -unit && diff < unit) {
				long offset = (a + TimeZone.getDefault().getRawOffset()) % unit - diff;
				return 0 <= offset && offset < unit;
			}
		} else {
			Calendar ac = Calendar.getInstance();
			ac.setTimeInMillis(a);
			Calendar bc = Calendar.getInstance();
			bc.setTimeInMillis(b);
			switch (inField) {
			case WEEK_OF_YEAR:
			case WEEK_OF_MONTH:
				if (ac.get(WEEK_OF_MONTH) != bc.get(WEEK_OF_MONTH))
					break;
			case MONTH:
				if (ac.get(MONTH) != bc.get(MONTH))
					break;
			case YEAR:
				return ac.get(ERA) == bc.get(ERA) && ac.get(YEAR) == bc.get(YEAR);
			default:
				throw new IllegalArgumentException(String.valueOf(inField));
			}
		}
		return false;
	}

	/**
	 * 当前时间是否与指定时间是否处于同一年/月/天/小时/分钟/秒/毫秒<br>
	 * 时间对象可以为：{@link Date}、{@link EasyDate}、{@link Calendar}
	 *
	 * @param inField 指定用于判断是否为同一值的时间单位字段，可以使用Calendar类的单位常量
	 * @author Ready
	 * @see Calendar#YEAR
	 * @see Calendar#MONTH
	 * @see Calendar#WEEK_OF_MONTH
	 * @see Calendar#DAY_OF_MONTH
	 * @see Calendar#HOUR_OF_DAY
	 * @see Calendar#MINUTE
	 * @see Calendar#SECOND
	 * @see Calendar#MILLISECOND
	 * @since 0.3.6
	 */
	public boolean isSameAs(Object date, int inField) {
		return isSameAs(getTime(), getTimeOfDate(date), inField);
	}

	/**
	 * 比较两个时间值是否处于同一年/月/天/小时/分钟/秒/毫秒<br>
	 * 时间对象可以为：{@link Date}、{@link EasyDate}、{@link Calendar}
	 *
	 * @param a 时间 a
	 * @param b 时间 b
	 * @param inField 指定用于判断是否为同一值的时间单位字段，可以使用Calendar类的单位常量
	 * @author Ready
	 * @see Calendar#YEAR
	 * @see Calendar#MONTH
	 * @see Calendar#WEEK_OF_MONTH
	 * @see Calendar#DAY_OF_MONTH
	 * @see Calendar#HOUR_OF_DAY
	 * @see Calendar#MINUTE
	 * @see Calendar#SECOND
	 * @see Calendar#MILLISECOND
	 * @since 0.3.6
	 */
	public static boolean isSameAs(Object a, Object b, int inField) {
		return isSameAs(getTimeOfDate(a), getTimeOfDate(b), inField);
	}

	/**
	 * 判断是否在指定日期的时间之后
	 */
	public boolean after(Object date) {
		return compareTo(date) > 0;
	}

	/**
	 * 获取当前月的最后一天
	 *
	 * @since 0.3
	 */
	public int getLastDayOfMonth() {
		return calendar.getActualMaximum(DAY_OF_MONTH);
	}

	/**
	 * 将当前实例设置为指定时间字段范围内所能表示的最小值
	 *
	 * @param field 该方法支持的字段有{@link Calendar#YEAR}、{@link Calendar#MONTH}、 {@link Calendar#DAY_OF_MONTH}、 {@link Calendar#HOUR_OF_DAY}、 {@link Calendar#MINUTE}、{@link Calendar#SECOND}
	 * @since 0.3
	 */
	public EasyDate beginOf(int field) {
		switch (field) {
		case YEAR:
			calendar.set(MONTH, 0);
		case MONTH:
			calendar.set(DAY_OF_MONTH, 1);
		case DAY_OF_MONTH:
			calendar.set(HOUR_OF_DAY, 0);
		case HOUR_OF_DAY:
			calendar.set(MINUTE, 0);
		case MINUTE:
			calendar.set(SECOND, 0);
		case SECOND:
			calendar.set(MILLISECOND, 0);
			break;
		default:
			throw new IllegalArgumentException(String.valueOf(field));
		}
		return this;
	}

	/**
	 * 将当前实例设置为指定时间字段范围内所能表示的最小值
	 *
	 * @param field 该方法支持的字段有{@link Calendar#YEAR}、{@link Calendar#MONTH}、 {@link Calendar#DAY_OF_MONTH}、 {@link Calendar#HOUR_OF_DAY}、 {@link Calendar#MINUTE}、{@link Calendar#SECOND}
	 * @since 0.3
	 */
	@SuppressWarnings("deprecation")
	public static Date beginOf(Date d, int field) {
		switch (field) {
		case YEAR:
			d.setMonth(JANUARY);
		case MONTH:
			d.setDate(1);
		case DAY_OF_MONTH:
			d.setHours(0);
		case HOUR_OF_DAY:
		case HOUR:
			d.setMinutes(0);
		case MINUTE:
			d.setSeconds(0);
		case SECOND:
			d.setTime(d.getTime() / 1000 * 1000);
			break;
		default:
			throw new IllegalArgumentException(String.valueOf(field));
		}
		return d;
	}

	/**
	 * 设置时区
	 *
	 * @param timeZone 指定的时区
	 * @since 3.0.0
	 */
	public EasyDate setTimeZone(TimeZone timeZone) {
		calendar.getTimeInMillis(); // 强迫更新时间，否则之前的时区设置可能不生效
		calendar.setTimeZone(timeZone);
		return this;
	}

	/**
	 * 获取当前时间对象的时区设置
	 *
	 * @since 3.0.0
	 */
	public TimeZone getTimeZone() {
		return calendar.getTimeZone();
	}

	/**
	 * 设置本地时间相对于GMT时间的偏移分钟数
	 *
	 * @param inMinutes 偏移分钟数
	 * @since 0.3
	 */
	public EasyDate setTimeZoneOffset(int inMinutes) {
		StringBuilder timeZoneID = new StringBuilder(10).append("GMT").append((inMinutes > 0 ? '+' : '-')).append(inMinutes / 60);
		int min = inMinutes % 60;
		if (min > 0) {
			timeZoneID.append(':');
			if (min < 10) {
				timeZoneID.append('0');
			}
			timeZoneID.append(min);
		}
		return setTimeZone(TimeZone.getTimeZone(timeZoneID.toString()));
	}

	/**
	 * 获取本地时间相对于GMT时间的偏移分钟数
	 *
	 * @since 0.3
	 */
	public int getTimeZoneOffset() {
		TimeZone timeZone = calendar.getTimeZone();
		return timeZone.getOffset(calendar.getTimeInMillis()) / 60000;
	}

	/**
	 * 将当前实例设置为指定时间字段所能表示的最大值
	 *
	 * @param field 该方法支持的字段有{@link Calendar#YEAR}、{@link Calendar#MONTH}、 {@link Calendar#DAY_OF_MONTH}、 {@link Calendar#HOUR_OF_DAY}、 {@link Calendar#MINUTE}、{@link Calendar#SECOND}
	 * @since 0.3
	 */
	public EasyDate endOf(int field) {
		switch (field) {
		case YEAR:
			calendar.set(MONTH, DECEMBER);
		case MONTH:
			calendar.set(DAY_OF_MONTH, calendar.getActualMaximum(DAY_OF_MONTH));
		case DAY_OF_MONTH:
			calendar.set(HOUR_OF_DAY, 23);
		case HOUR_OF_DAY:
		case HOUR:
			calendar.set(MINUTE, 59);
		case MINUTE:
			calendar.set(SECOND, 59);
		case SECOND:
			calendar.set(MILLISECOND, 999);
			break;
		default:
			throw new IllegalArgumentException(String.valueOf(field));
		}
		return this;
	}

	/**
	 * 将当前实例设置为指定时间字段所能表示的最大值
	 *
	 * @param field 该方法支持的字段有{@link Calendar#YEAR}、{@link Calendar#MONTH}、 {@link Calendar#DAY_OF_MONTH}、 {@link Calendar#HOUR_OF_DAY}、 {@link Calendar#MINUTE}、{@link Calendar#SECOND}
	 * @since 0.3
	 */
	@SuppressWarnings("deprecation")
	public static Date endOf(Date d, int field) {
		switch (field) {
		case YEAR:
			d.setMonth(DECEMBER);
		case MONTH:
			d.setDate(getMaxDayOfMonth(d));
		case DAY_OF_MONTH:
			d.setHours(23);
		case HOUR_OF_DAY:
		case HOUR:
			d.setMinutes(59);
		case MINUTE:
			d.setSeconds(59);
		case SECOND:
			d.setTime(d.getTime() / 1000L * 1000L + 999L);
			break;
		default:
			throw new IllegalArgumentException(String.valueOf(field));
		}
		return d;
	}

	@SuppressWarnings("deprecation")
	public static int getMaxDayOfMonth(Date d) {
		final int month = d.getMonth();
		switch (month) {
		case JANUARY:
		case MARCH:
		case MAY:
		case JULY:
		case AUGUST:
		case OCTOBER:
		case DECEMBER:
			return 31;
		case FEBRUARY:
			int year = d.getYear() + 1900;
			if (year % 4 == 0 && (year % 400 == 0 || year % 100 != 0)) {
				return 29;
			}
			return 28;
		default:
			return 30;
		}
	}

	/**
	 * 判断是否在指定日期的时间之前
	 */
	public boolean before(Object date) {
		return compareTo(date) < 0;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = result * 31 + ((calendar == null) ? 0 : calendar.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof EasyDate) {
			EasyDate other = (EasyDate) obj;
			return calendar != null && calendar.equals(other.calendar);
		}
		return false;
	}

	/**
	 * 返回"yyyy-MM-dd"格式的字符串
	 */
	@Override
	public String toString() {
		return toString(getYear(), getMonth(), getDay());
	}

	/**
	 * 返回"yyyy-MM-dd"格式的字符串
	 */
	@SuppressWarnings("deprecation")
	public static String toString(Date d) {
		return toString(d.getYear() + 1900, d.getMonth() + 1, d.getDate());
	}

	static String toString(int year, int month, int day) {
		// "0000-00-00".toCharArray();
		char[] chars = new char[] { '0', '0', '0', '0', '-', '0', '0', '-', '0', '0' };
		formatNormalDate(chars, 0, year, month, day);
		return new String(chars);
	}

	/**
	 * 返回"yyyy年MM月dd日"格式的字符串
	 */
	public String toDateString() {
		return toDateString(getYear(), getMonth(), getDay());
	}

	/**
	 * 返回"yyyy年MM月dd日"格式的字符串
	 */
	public static String toDateString(int year, int month, int day) {
		// "0000年00月00日".toCharArray();
		char[] chars = new char[] { '0', '0', '0', '0', '年', '0', '0', '月', '0', '0', '日' };
		formatNormalDate(chars, 0, year, month, day);
		return new String(chars);
	}

	/**
	 * 返回"yyyy年MM月dd日"格式的字符串
	 */
	@SuppressWarnings("deprecation")
	public static String toDateString(Date d) {
		return toDateString(d.getYear() + 1900, d.getMonth() + 1, d.getDate());
	}

	/**
	 * 返回使用日期格式化工具格式化后的字符串
	 *
	 * @since 0.3
	 */
	public String toString(Format format) {
		if (format instanceof DateFormat) {
			TimeZone timeZone = calendar.getTimeZone();
			if (timeZone.getRawOffset() != TimeZone.getDefault().getRawOffset()) {
				((DateFormat) format).setTimeZone(timeZone);
			}
		}
		return format.format(toDate());
	}

	/**
	 * 返回"yyyy-MM-dd HH:mm:ss"格式的字符串
	 */
	public String toDateTimeString() {
		return toDateTimeString(getYear(), getMonth(), getDay(), getHour(), getMinute(), getSecond());
	}

	/**
	 * 返回"yyyy-MM-dd HH:mm:ss"格式的字符串
	 */
	@SuppressWarnings("deprecation")
	public static String toDateTimeString(Date d) {
		return toDateTimeString(d.getYear() + 1900, d.getMonth() + 1, d.getDate(), d.getHours(), d.getMinutes(), d.getSeconds());
	}

	/**
	 * 返回"yyyy-MM-dd HH:mm:ss"格式的字符串
	 */
	public static String toDateTimeString(int year, int month, int day, int hour, int minute, int second) {
		final char[] chars = "0000-00-00 00:00:00".toCharArray();
		formatNormalDateTime(chars, year, month, day, hour, minute, second);
		return new String(chars);
	}

	/**
	 * 返回"yyyyMMdd"格式的字符串
	 */
	public String toShortString() {
		return toShortString(getYear(), getMonth(), getDay());
	}

	/**
	 * 返回"yyyyMMdd"格式的字符串
	 */
	@SuppressWarnings("deprecation")
	public static String toShortString(Date d) {
		return toShortString(d.getYear() + 1900, d.getMonth() + 1, d.getDate());
	}

	/**
	 * 返回"yyyyMMdd"格式的字符串
	 */
	public static String toShortString(int year, int month, int day) {
		final char[] chars = "00000000".toCharArray();
		fillNumberToChars(chars, year, 0, 4);
		fillNumberToChars(chars, month, 4, 2);
		fillNumberToChars(chars, day, 6, 2);
		return new String(chars);
	}

	/**
	 * 返回"yyyy-MM-dd HH:mm:ss.SSS"格式的字符串
	 */
	public String toLongString() {
		return toLongString(getYear(), getMonth(), getDay(), getHour(), getMinute(), getSecond(), getMillisecond());
	}

	/**
	 * 返回"yyyy-MM-dd HH:mm:ss.SSS"格式的字符串
	 */
	@SuppressWarnings("deprecation")
	public static String toLongString(Date d) {
		return toLongString(d.getYear() + 1900, d.getMonth() + 1, d.getDate(), d.getHours(), d.getMinutes(), d.getSeconds(), (int) (d.getTime() % 1000));
	}

	/**
	 * 返回"yyyy-MM-dd HH:mm:ss.SSS"格式的字符串
	 */
	public static String toLongString(int year, int month, int day, int hour, int minute, int second, int ms) {
		final char[] chars = "0000-00-00 00:00:00.000".toCharArray();
		formatNormalDateTime(chars, year, month, day, hour, minute, second);
		fillNumberToChars(chars, ms, 20, 3);
		return new String(chars);
	}

	protected static void formatNormalDateTime(char[] chars, int year, int month, int day, int hour, int minute, int second) {
		formatNormalDate(chars, 0, year, month, day);
		formatNormalTime(chars, 11, hour, minute, second);
	}

	public static void formatNormalDate(char[] chars, final int offset, final int year, final int month, final int day) {
		fillNumberToChars(chars, year, offset, 4);
		fillNumberToChars(chars, month, offset + 5, 2);
		fillNumberToChars(chars, day, offset + 8, 2);
	}

	public static void formatNormalTime(char[] chars, final int offset, final int hour, final int minute, final int second) {
		fillNumberToChars(chars, hour, offset, 2);
		fillNumberToChars(chars, minute, offset + 3, 2);
		fillNumberToChars(chars, second, offset + 6, 2);
	}

	/**
	 * 返回GMT标准格式的字符串，例如：1 Dec 2012 15:05:00 GMT
	 */
	public String toGMTString() {
		return FastDateFormat.getInstance(GMT_DATE).format(toDate());
	}

	/**
	 * 返回Internet GMT标准格式的字符串,例如：Sat, 1 Dec 2012 23:05:00 GMT
	 */
	public String toGMTNetString() {
		return FastDateFormat.getInstance(GMT_NET_DATE).format(toDate());
	}

	@Override
	public Object clone() {
		EasyDate date = null;
		try {
			date = (EasyDate) super.clone();
			if (calendar != null) {
				date.calendar = (Calendar) calendar.clone();
			}
		} catch (CloneNotSupportedException e) {
			// ignore exception
		}
		return date;
	}

	/**
	 * 将指定的数字设置到指定的字符数组中的指定索引处，并填充指定的长度，如果数字的长度不够，则在前面填充0
	 *
	 * @param chars 指定的字符数组
	 * @param number 指定的数字
	 * @param start 指定的起始索引
	 * @param length 指定的长度
	 */
	public static void setNumberToChars(char[] chars, int number, int start, int length) {
		int end = start + length;
		while (length-- > 0) {
			chars[--end] = (char) ('0' + (number % 10));
			number /= 10;
		}
	}

	/**
	 * 将指定的数字设置到指定的字符数组中的指定索引处，从右向左依次填充，并最多填充指定的长度
	 *
	 * @param chars 指定的字符数组
	 * @param number 指定的数字
	 * @param start 指定的起始索引
	 * @param length 指定的长度
	 */
	public static void fillNumberToChars(char[] chars, int number, int start, int length) {
		int end = start + length;
		while (number > 0 && length-- > 0) {
			chars[--end] = (char) ('0' + (number % 10));
			number /= 10;
		}
	}

	/**
	 * 判断两个日期对象是否是同一天（不考虑时区差异）
	 */
	@SuppressWarnings("deprecation")
	public static boolean isSameDay(final Date a, final Date b) {
		if (a == null || b == null) {
			return false;
		}
		return a == b || (Math.abs(a.getTime() - b.getTime()) < MILLIS_OF_DAY && a.getYear() == b.getYear()
				&& a.getMonth() == b.getMonth() && a.getDate() == b.getDate());
	}

}
