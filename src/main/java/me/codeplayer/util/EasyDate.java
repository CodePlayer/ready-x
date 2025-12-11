package me.codeplayer.util;

import java.io.Serializable;
import java.math.RoundingMode;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.*;
import java.time.*;
import java.util.*;

import org.apache.commons.lang3.time.FastDateFormat;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import static java.util.Calendar.*;
import static me.codeplayer.util.NumberUtil.pickValidChars;

/**
 * 实现常用日期扩展方法的日期工具类(实现Comparable可比较接口、Cloneable可复制接口)
 *
 * @author Ready
 * @since 2012-9-24
 */
public class EasyDate implements Comparable<Object>, Cloneable, Serializable {

	private static final long serialVersionUID = 1L;

	/** yyyy-MM-dd 格式的日期转换器 */
	public static final String DATE = "yyyy-MM-dd";
	/** yyyy-MM-dd HH:mm:ss 格式的日期转换器 */
	public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
	/** yyyyMMdd 格式的日期转换器 */
	public static final String SHORT_DATE = "yyyyMMdd";
	/** yyyyMM 格式的日期转换器 */
	public static final String YM_DATE = "yyyyMM";
	/** GMT标准格式的日期转换器[d MMM yyyy HH:mm:ss 'GMT'] */
	public static final String GMT_DATE = "d MMM yyyy HH:mm:ss 'GMT'";
	/** Internet GMT标准格式的日期转换器[EEE, d MMM yyyy HH:mm:ss 'GMT'] */
	public static final String GMT_NET_DATE = "EEE, d MMM yyyy HH:mm:ss 'GMT'";
	/** 一分钟的毫秒数 */
	public static final long MILLIS_OF_MINUTE = 1000 * 60;
	/** 一小时的毫秒数 */
	public static final long MILLIS_OF_HOUR = MILLIS_OF_MINUTE * 60;
	/** 一天的毫秒数 */
	public static final long MILLIS_OF_DAY = MILLIS_OF_HOUR * 24;

	private Calendar calendar;

	/**
	 * 初始化日历对象相关设置
	 */
	protected void initCalendar(Calendar calendar) {
		calendar.setLenient(false);
		calendar.setFirstDayOfWeek(MONDAY);
		this.calendar = calendar;
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
	 * 根据指定的毫秒数构造对应时区的实例对象
	 */
	public EasyDate(long date, TimeZone timeZone) {
		initCalendar(new GregorianCalendar(timeZone));
		calendar.setTimeInMillis(date);
	}

	/**
	 * 根据相对于指定时间的偏移值构造一个对应的实例对象<br>
	 * 例如，当前时间为：2012-10-10 例如要创建一个2013-10-10的时间对象，<code>new EasyDate(null, 1, 0, 0)</code> 即可;<br>
	 * 创建一个2011-8-10的时间对象，<code>new EasyDate(null, -1, -2, 0)</code> 或 <code>new EasyDate(null, 0, -14, 0)</code>
	 *
	 * @param date 指定的时间，作为偏移量的参考对象，如果为null，则默认使用当前时间作为参考对象<br>
	 * 该对象支持 {@link java.util.Date}、{@link me.codeplayer.util.EasyDate}、{@link java.util.Calendar}等对象及其子类实例
	 * @param offsetYear 相对于当前时间的年份偏移量
	 * @param offsetMonth 相对于当前时间的月份偏移量
	 * @param offsetDay 相对于当前时间的日期偏移量
	 */
	private EasyDate(@Nullable Object date, int offsetYear, int offsetMonth, int offsetDay) {
		this(getTimeOfDate(date, true));
		if (offsetYear != 0) {
			calendar.add(YEAR, offsetYear);
		}
		if (offsetMonth != 0) {
			calendar.add(MONTH, offsetMonth);
		}
		if (offsetDay != 0) {
			calendar.add(Calendar.DATE, offsetDay);
		}
	}

	/**
	 * 根据相对于指定时间的偏移值构造一个对应的实例对象<br>
	 * 例如，当前时间为：2012-10-10 例如要创建一个2013-10-10的时间对象，<code>new EasyDate(null, 1, 0, 0)</code> 即可;<br>
	 * 创建一个2011-8-10的时间对象，<code>new EasyDate(null, -1, -2, 0)</code> 或 <code>new EasyDate(null, 0, -14, 0)</code>
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
	public static long getTimeOfDate(@Nullable Object date, boolean nullAsNow) {
		long theTime;
		if (date == null) {
			if (!nullAsNow) {
				throw new NullPointerException();
			}
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
	 * 返回自 1970 年 1 月 1 日 00:00:00 GMT 以来指定日期对象表示的毫秒数。<br>
	 * 如果为null，则默认为当前时间
	 */
	public static long getTimeOfDate(@NonNull Object date) {
		return getTimeOfDate(date, false);
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
		calendar.set(Calendar.DATE, day);
		if (args.length > 0) {
			int[] fields = { HOUR_OF_DAY, MINUTE, SECOND, MILLISECOND };
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
		return calendar.get(Calendar.DATE);
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
		calendar.set(Calendar.DATE, day);
		return this;
	}

	/**
	 * 设置为本周的周几
	 *
	 * @param dayOfWeek 1~7 表示 周一到周日
	 */
	public EasyDate setWeekDay(int dayOfWeek) {
		final int current = getWeekDay();
		if (current != dayOfWeek) {
			addDay(dayOfWeek - current);
		}
		return this;
	}

	/**
	 * 追加指定的天数，例如：当前是2012-05-12，调用addDay(2)，则为2012-05-14
	 *
	 * @param day 指定的天数，可以为负数
	 */
	public EasyDate addDay(int day) {
		calendar.add(Calendar.DATE, day);
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
	 * 转为 {@link java.sql.Time}
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
		long diff = calendar.getTimeInMillis() - getTimeOfDate(date);
		if (diff == 0) {
			return 0;
		}
		return diff > 0 ? 1 : -1;
	}

	/**
	 * 计算并返回当前日期与指定日期之间基于指定单位和舍入模式的差值
	 * <p>如果当前日期大于等于指定日期，则返回正数，否则返回负数
	 * <p><b>注意：</b>如果两个时间对象的时区不一致，则以 {@code this } 的时区 为准
	 *
	 * @param date 与当前日期进行比较的日期
	 * @param field 指定的日期字段，返回值将以此为单位返回两个日期的差距值
	 * @param roundingMode 舍入模式
	 */
	public long calcDifference(Object date, int field, RoundingMode roundingMode) {
		final long thisMs = getTime(), theMs = getTimeOfDate(date);
		long diff = thisMs - theMs; // 毫秒值差距

		if (diff == 0) {
			return 0;
		}
		switch (field) {
			case YEAR:
			case MONTH:
				final boolean thisIsMax = diff > 0;
				EasyDate me = this;
				EasyDate other = new EasyDate(theMs, me.getTimeZone());
				final EasyDate min = thisIsMax ? other : me, max = thisIsMax ? me : other;
				final int diffOfYear = max.getYear() - min.getYear();
				if (diffOfYear > 0) { // 将 min 调增，对齐到同一年
					min.addYear(diffOfYear);
				}
				if (field == MONTH) {
					int diffOfMonth = max.getMonth() - min.getMonth();
					if (diffOfMonth != 0) {  // 将 min 调整，对齐到同一月
						min.addMonth(diffOfMonth);
					}
					diff = diffOfYear * 12L + diffOfMonth; // 月份差距
				} else {
					diff = diffOfYear;  // 年份差距
				}
				final long maxTime = max.getTime();
				long partDiff = maxTime - min.getTime();
				if (partDiff == 0) { // 如果对齐后刚好相等，就返回计算好的差距数值
					resetMeIfNeed(min, me, thisMs);
					return thisIsMax ? diff : -diff;
				} else if (partDiff < 0) {  // 如果对齐到相同年月时，min 反而较大
					diff--; // 整数部分要 -1
				}
				switch (roundingMode) {
					case CEILING: // 向【正无穷】的方向舍入
						resetMeIfNeed(min, me, thisMs);
						return thisIsMax ? diff + 1 : -diff;
					case UP: // 向【远离 0】 的方向舍入
						resetMeIfNeed(min, me, thisMs);
						return thisIsMax ? diff + 1 : -diff - 1;
					case DOWN: // 向【靠近 0】 的方向舍去
						resetMeIfNeed(min, me, thisMs);
						return thisIsMax ? diff : -diff;
					case FLOOR: // 向【负无穷】的方向舍去
						resetMeIfNeed(min, me, thisMs);
						return thisIsMax ? diff : -diff - 1;
					case HALF_UP:
					case HALF_DOWN:
					case HALF_EVEN: {
						final long innerPartDiff, outerPartDiff;
						if (partDiff < 0) { // 如果对齐到相同年月时，min 反而较大
							outerPartDiff = -partDiff;
							min.calendar.add(field, -1);
							innerPartDiff = maxTime - min.getTime();
						} else {  // 如果对齐到相同年月时，max 反而较大
							innerPartDiff = partDiff;
							min.calendar.add(field, 1);
							outerPartDiff = min.getTime() - maxTime;
						}
						final int incr;
						if (roundingMode == RoundingMode.HALF_UP) {
							incr = innerPartDiff >= outerPartDiff ? 1 : 0;
						} else if (roundingMode == RoundingMode.HALF_DOWN) {
							incr = innerPartDiff > outerPartDiff ? 1 : 0;
						} else /* if (roundingMode == RoundingMode.HALF_EVEN) */ { // 满 0.5 时向邻近的【偶数】舍入
							incr = innerPartDiff == outerPartDiff
									// 4.5 => 4  |  5.5 => 6  | -1.5 => -2 | -2.5 => -2
									? ((diff & 1) == 1 ? 1 : 0)
									: innerPartDiff > outerPartDiff ? 1 : 0;
						}
						resetMeIfNeed(min, me, thisMs);
						return thisIsMax ? diff + incr : -diff - incr;
					}
					case UNNECESSARY:
						throw new IllegalArgumentException("Cannot round to " + diff + " because rounding mode " + roundingMode + " is not supported");
				}
			default:
				long unit = getMillisOfUnit(field);
				return Arith.toBigDecimal(diff).divide(Arith.toBigDecimal(unit), 0, roundingMode).longValue();
		}
	}

	static void resetMeIfNeed(EasyDate min, EasyDate me, final long thisMs) {
		if (min == me) {
			me.setTime(thisMs); // reset
		}
	}

	/**
	 * 计算并返回当前日期与指定日期之间基于指定单位和向上取整模式（{@link RoundingMode#UP}）的差值
	 * <p>如果当前日期大于等于指定日期，则返回正数，否则返回负数
	 * <p><b>注意：</b>如果两个时间对象的时区不一致，则以 {@code this } 的时区 为准
	 *
	 * @param date 与当前日期进行比较的日期
	 * @param field 指定的日期字段，返回值将以此为单位返回两个日期的差距值
	 */
	public long calcDifference(Object date, int field) {
		return calcDifference(date, field, RoundingMode.UP);
	}

	/**
	 * 计算并返回当前日期与指定日期之间基于向上取整模式（{@link RoundingMode#UP}）的天数差值
	 * <p>如果当前日期大于等于指定日期，则返回正数，否则返回负数
	 * <p><b>注意：</b>如果两个时间对象的时区不一致，则以 {@code this } 的时区 为准
	 *
	 * @param dateObj 与当前日期进行比较的日期
	 */
	public int calcDifference(Object dateObj) {
		return (int) calcDifference(dateObj, Calendar.DATE, RoundingMode.UP);
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
	 * 判断指定年份是否为闰年
	 *
	 * @param year 例如 2012
	 */
	public static boolean isLeapYears(int year) {
		// (year & 3) 等价于 (year % 4)
		return (year & 3) == 0 && (year % 400 == 0 || year % 100 != 0);
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
	 * @param field 该方法支持的字段有{@link Calendar#YEAR}、{@link Calendar#MONTH}、 {@link Calendar#DATE}、 {@link Calendar#HOUR_OF_DAY}、 {@link Calendar#MINUTE}、{@link Calendar#SECOND}
	 * @since 0.3.6
	 */
	public static long getMillisOfUnit(int field) {
		switch (field) {
			case DAY_OF_YEAR:
			case Calendar.DATE:
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
	 * @param timeZone 用于比较的基准时区
	 * @see Calendar#YEAR
	 * @see Calendar#MONTH
	 * @see Calendar#WEEK_OF_YEAR
	 * @see Calendar#WEEK_OF_MONTH
	 * @see Calendar#DAY_OF_YEAR
	 * @see Calendar#DATE
	 * @see Calendar#HOUR
	 * @see Calendar#HOUR_OF_DAY
	 * @see Calendar#MINUTE
	 * @see Calendar#SECOND
	 * @see Calendar#MILLISECOND
	 * @since 0.3.6
	 */
	public static boolean isSameAs(long a, long b, int inField, final TimeZone timeZone) {
		if (a == b) {
			return true;
		}
		if (inField > WEEK_OF_MONTH) { // DATE、HOUR、HOUR_OF_DAY、MINUTE、SECOND、MILLISECOND
			long unit = getMillisOfUnit(inField);
			long diff = a - b;
			if (diff > -unit && diff < unit) {
				long offset = (a + timeZone.getRawOffset()) % unit - diff;
				return 0 <= offset && offset < unit;
			}
		} else {
			final Locale locale = Locale.getDefault(Locale.Category.FORMAT);
			final Calendar ac = Calendar.getInstance(timeZone, locale);
			ac.setTimeInMillis(a);
			final Calendar bc = Calendar.getInstance(timeZone, locale);
			bc.setTimeInMillis(b);
			switch (inField) {
				case WEEK_OF_YEAR:
				case WEEK_OF_MONTH:
					if (ac.get(WEEK_OF_MONTH) != bc.get(WEEK_OF_MONTH)) {
						break;
					}
				case MONTH:
					if (ac.get(MONTH) != bc.get(MONTH)) {
						break;
					}
				case YEAR:
					return ac.get(ERA) == bc.get(ERA) && ac.get(YEAR) == bc.get(YEAR);
				default:
					throw new IllegalArgumentException(String.valueOf(inField));
			}
		}
		return false;
	}

	/**
	 * 比较两个以毫秒数表示的时间值是否处于同一年/月/天/小时/分钟/秒/毫秒
	 * <p><b>注意：</b>基于系统默认时区进行比较
	 *
	 * @param a 时间 a
	 * @param b 时间 b
	 * @param inField 指定用于判断是否为同一值的时间单位字段，可以使用Calendar类的单位常量
	 * @see Calendar#YEAR
	 * @see Calendar#MONTH
	 * @see Calendar#WEEK_OF_YEAR
	 * @see Calendar#WEEK_OF_MONTH
	 * @see Calendar#DAY_OF_YEAR
	 * @see Calendar#DATE
	 * @see Calendar#HOUR
	 * @see Calendar#HOUR_OF_DAY
	 * @see Calendar#MINUTE
	 * @see Calendar#SECOND
	 * @see Calendar#MILLISECOND
	 * @since 0.3.6
	 */
	public static boolean isSameAs(long a, long b, int inField) {
		return isSameAs(a, b, inField, TimeZone.getDefault());
	}

	/**
	 * 当前时间是否与指定时间是否处于同一年/月/天/小时/分钟/秒/毫秒<br>
	 * 时间对象可以为：{@link Date}、{@link EasyDate}、{@link Calendar}
	 *
	 * @param inField 指定用于判断是否为同一值的时间单位字段，可以使用Calendar类的单位常量
	 * @see Calendar#YEAR
	 * @see Calendar#MONTH
	 * @see Calendar#WEEK_OF_MONTH
	 * @see Calendar#DATE
	 * @see Calendar#HOUR_OF_DAY
	 * @see Calendar#MINUTE
	 * @see Calendar#SECOND
	 * @see Calendar#MILLISECOND
	 * @since 0.3.6
	 */
	public boolean isSameAs(Object date, int inField) {
		return isSameAs(getTime(), getTimeOfDate(date), inField, getTimeZone());
	}

	/**
	 * 比较两个时间值是否处于同一年/月/天/小时/分钟/秒/毫秒
	 * <p>时间对象可以为：{@link Date}、{@link EasyDate}、{@link Calendar}
	 * <p><b>注意：</b>如果两个时间对象的时区不一致，则以 {@code a } 或 系统默认时区 为准
	 *
	 * @param a 时间 a
	 * @param b 时间 b
	 * @param inField 指定用于判断是否为同一值的时间单位字段，可以使用Calendar类的单位常量
	 * @see Calendar#YEAR
	 * @see Calendar#MONTH
	 * @see Calendar#WEEK_OF_MONTH
	 * @see Calendar#DATE
	 * @see Calendar#HOUR_OF_DAY
	 * @see Calendar#MINUTE
	 * @see Calendar#SECOND
	 * @see Calendar#MILLISECOND
	 * @since 0.3.6
	 */
	public static boolean isSameAs(Object a, Object b, int inField) {
		final long time;
		final TimeZone timeZone;
		if (a == null) {
			throw new NullPointerException();
		} else if (a instanceof Date) {
			time = ((Date) a).getTime();
			timeZone = TimeZone.getDefault();
		} else if (a instanceof EasyDate) {
			final EasyDate ed = (EasyDate) a;
			time = (ed).getTime();
			timeZone = (ed).getTimeZone();
		} else if (a instanceof Calendar) {
			final Calendar c = ((Calendar) a);
			time = c.getTimeInMillis();
			timeZone = c.getTimeZone();
		} else {
			throw new ClassCastException(a.getClass().getName());
		}
		return isSameAs(time, getTimeOfDate(b), inField, timeZone);
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
		return calendar.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 将当前实例设置为指定时间字段范围内所能表示的最小值
	 *
	 * @param field 该方法支持的字段请参见参阅
	 * @see Calendar#YEAR
	 * @see Calendar#MONTH
	 * @see Calendar#DAY_OF_WEEK
	 * @see Calendar#DATE
	 * @see Calendar#HOUR_OF_DAY
	 * @see Calendar#MINUTE
	 * @see Calendar#SECOND
	 * @since 0.3
	 */
	public EasyDate beginOf(int field) {
		switch (field) {
			case YEAR:
				calendar.set(MONTH, 0);
			case MONTH:
				calendar.set(Calendar.DATE, 1);
			case DAY_OF_WEEK: {
				if (field == DAY_OF_WEEK) {
					// SUNDAY(1), MONDAY(2), TUESDAY(3), WEDNESDAY(3), THURSDAY(5), FRIDAY(6), SATURDAY(7)
					final int base = calendar.getFirstDayOfWeek(), current = calendar.get(DAY_OF_WEEK);
					if (base != current) {
						int diff = base - current;
						if (diff > 0) {
							diff -= 7;
						}
						calendar.add(Calendar.DATE, diff);
					}
				}
			}
			case Calendar.DATE:
				calendar.set(HOUR_OF_DAY, 0);
			case HOUR:
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
	 * @param field 该方法支持的字段请参见参阅
	 * @see Calendar#YEAR
	 * @see Calendar#MONTH
	 * @see Calendar#DAY_OF_WEEK
	 * @see Calendar#DATE
	 * @see Calendar#HOUR_OF_DAY
	 * @see Calendar#MINUTE
	 * @see Calendar#SECOND
	 * @since 0.3
	 */
	public static Date beginOf(Date d, int field) {
		return new EasyDate(d).beginOf(field).toDate();
	}

	/**
	 * 设置时区
	 *
	 * @param timeZone 指定的时区
	 * @since 3.0.0
	 */
	public EasyDate setTimeZone(TimeZone timeZone) {
		// 目前 JDK 在特殊场景下，存在时间计算错误，示例请参阅 me.codeplayer.util.EasyDateTest.testForTimeZone
		final long time = calendar.getTimeInMillis(); // 强迫更新时间，否则之前的时区设置可能不生效
		calendar.setTimeZone(timeZone);
		calendar.setTimeInMillis(time);
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
	 * 获取本地时间相对于GMT时间的偏移分钟数。例如：
	 * <pre><code>
	 * "GMT+8" 将返回 480
	 * "GMT-4" 将返回 -240
	 * "GMT+5:30" 将返回 330
	 * </code></pre>
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
	 * @param field 该方法支持的字段请参见参阅
	 * @see Calendar#YEAR
	 * @see Calendar#MONTH
	 * @see Calendar#DAY_OF_WEEK
	 * @see Calendar#DATE
	 * @see Calendar#HOUR_OF_DAY
	 * @see Calendar#MINUTE
	 * @see Calendar#SECOND
	 * @since 0.3
	 */
	public EasyDate endOf(int field) {
		switch (field) {
			case YEAR:
				calendar.set(MONTH, DECEMBER);
			case MONTH:
				calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
			case DAY_OF_WEEK: {
				if (field == DAY_OF_WEEK) {
					// SUNDAY(1), MONDAY(2), TUESDAY(3), WEDNESDAY(3), THURSDAY(5), FRIDAY(6), SATURDAY(7)
					final int base = calendar.getFirstDayOfWeek(), current = calendar.get(DAY_OF_WEEK);
					int diff = base + 6 - current;
					if (diff > 0) {
						if (diff > 7) {
							diff -= 7;
						}
						// SUNDAY(1) => 6, MONDAY(2) => 7, TUESDAY(3) => 1, WEDNESDAY(3) => 2, THURSDAY(5) => 3, FRIDAY(6) => 4, SATURDAY(7) => 5
						calendar.add(Calendar.DATE, diff);
					}
				}
			}
			case Calendar.DATE:
				calendar.set(HOUR_OF_DAY, 23);
			case HOUR:
			case HOUR_OF_DAY:
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
	 * @param field 该方法支持的字段请参见参阅
	 * @see Calendar#YEAR
	 * @see Calendar#MONTH
	 * @see Calendar#DAY_OF_WEEK
	 * @see Calendar#DATE
	 * @see Calendar#HOUR_OF_DAY
	 * @see Calendar#MINUTE
	 * @see Calendar#SECOND
	 * @since 0.3
	 */
	public static Date endOf(Date d, int field) {
		return new EasyDate(d).endOf(field).toDate();
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
		if (this == obj) {
			return true;
		}
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
	public static String toString(Date d) {
		ZonedDateTime zdt = toDatetime(d);
		return toString(zdt.getYear(), zdt.getMonthValue(), zdt.getDayOfMonth());
	}

	static String toString(int year, int month, int day) {
		CharReplacer chars = CharReplacer.of("0000-00-00", true);
		formatNormalDate(chars, year, 0, month, 5, day, 8);
		return chars.toString();
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
		CharReplacer chars = CharReplacer.of("0000年00月00日", false);
		formatNormalDate(chars, year, 0, month, 5, day, 8);
		return chars.toString();
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
		final CharReplacer chars = CharReplacer.of("0000-00-00 00:00:00", true);
		chars.pickValidChars(year, 0, 4);
		chars.pickValidChars(month, 5, 7);
		chars.pickValidChars(day, 8, 10);
		chars.pickValidChars(hour, 11, 13);
		chars.pickValidChars(minute, 14, 16);
		chars.pickValidChars(second, 17, 19);
		return chars.toString();
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
	public static String toShortString(Date d) {
		ZonedDateTime zdt = toDatetime(d);
		return toShortString(zdt.getYear(), zdt.getMonthValue(), zdt.getDayOfMonth());
	}

	private static ZonedDateTime toDatetime(Date d) {
		return ZonedDateTime.ofInstant(Instant.ofEpochMilli(d.getTime()), ZoneId.systemDefault());
	}

	/**
	 * 返回"yyyyMMdd"格式的字符串
	 */
	public static String toShortString(int year, int month, int day) {
		final CharReplacer chars = CharReplacer.of("00000000", true);
		chars.pickValidChars(year, 0, 4);
		chars.pickValidChars(month, 4, 6);
		chars.pickValidChars(day, 6, 8);
		return chars.toString();
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
	public static String toLongString(Date d) {
		ZonedDateTime zdt = toDatetime(d);
		return toLongString(zdt.getYear(), zdt.getMonthValue(), zdt.getDayOfMonth(), zdt.getHour(), zdt.getMinute(), zdt.getSecond(), zdt.getNano() / 1000000);
	}

	/**
	 * 返回"yyyy-MM-dd HH:mm:ss.SSS"格式的字符串
	 */
	public static String toLongString(int year, int month, int day, int hour, int minute, int second, int ms) {
		final CharReplacer chars = CharReplacer.of("0000-00-00 00:00:00.000", true);
		formatNormalDate(chars, year, 0, month, 5, day, 8);
		formatNormalTime(chars, hour, 11, minute, 14, second, 17);
		chars.pickValidChars(ms, 20, 23);
		return chars.toString();
	}

	public static void formatNormalDateTime(final StringBuilder chars, int year, int month, int day, int hour, int minute, int second) {
		formatNormalDate(chars, year, 0, month, 5, day, 8);
		formatNormalTime(chars, hour, 11, minute, 14, second, 17);
	}

	/** "...yyyy..MM..dd..." */
	public static void formatNormalDate(final StringBuilder chars,
	                                    final int year, int yearStart,
	                                    final int month, int monthStart,
	                                    final int day, int dayStart) {
		pickValidChars(year, chars, yearStart, yearStart + 4);
		pickValidChars(month, chars, monthStart, monthStart + 2);
		pickValidChars(day, chars, dayStart, dayStart + 2);
	}

	/** "...yyyy-MM-dd..." */
	public static void formatNormalDate(final StringBuilder chars, final int year, final int month, final int day) {
		formatNormalDate(chars, year, 0, month, 5, day, 8);
	}

	/** "...yyyy-MM-dd..." */
	public static void formatNormalDate(final CharReplacer chars,
	                                    final int year, int yearStart,
	                                    final int month, int monthStart,
	                                    final int day, int dayStart) {
		chars.pickValidChars(year, yearStart, yearStart + 4);
		chars.pickValidChars(month, monthStart, monthStart + 2);
		chars.pickValidChars(day, dayStart, dayStart + 2);
	}

	/** "...HH..mm..ss..." */
	public static void formatNormalTime(final StringBuilder chars,
	                                    final int hour, int hourStart,
	                                    final int minute, int minStart,
	                                    final int second, int secStart) {
		pickValidChars(hour, chars, hourStart, hourStart + 2);
		pickValidChars(minute, chars, minStart, minStart + 2);
		pickValidChars(second, chars, secStart, secStart + 2);
	}

	/** "...HH:mm:ss..." */
	public static void formatNormalTime(final StringBuilder chars, final int hour, final int minute, final int second) {
		formatNormalTime(chars, hour, 11, minute, 14, second, 17);
	}

	/** "...HH:mm:ss..." */
	public static void formatNormalTime(final CharReplacer chars,
	                                    final int hour, int hourStart,
	                                    final int minute, int minStart,
	                                    final int second, int secStart) {
		chars.pickValidChars(hour, hourStart, hourStart + 2);
		chars.pickValidChars(minute, minStart, minStart + 2);
		chars.pickValidChars(second, secStart, secStart + 2);
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
	public EasyDate clone() {
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
	 * 判断两个日期对象是否是同一天（基于系统时区）
	 */
	@SuppressWarnings("deprecation")
	public static boolean isSameDay(final Date a, final Date b) {
		if (a == null || b == null) {
			return false;
		}
		return a == b || (Math.abs(a.getTime() - b.getTime()) < MILLIS_OF_DAY && a.getDate() == b.getDate())
				&& a.getMonth() == b.getMonth() && a.getYear() == b.getYear();
	}

}