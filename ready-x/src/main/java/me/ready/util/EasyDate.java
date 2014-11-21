package me.ready.util;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 实现常用日期扩展方法的日期工具类(实现Comparable可比较接口、Cloneable可复制接口)
 * 
 * @author Ready
 * @date 2012-9-24
 */
public class EasyDate implements Comparable<Object>, Cloneable, Serializable {

	/**
	 * yyyy-MM-dd格式的日期转换器
	 */
	public static final String DATE = "yyyy-MM-dd";
	/**
	 * yyyy-MM-dd HH:mm:ss格式的日期转换器
	 */
	public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyyMMdd格式的日期转换器
	 */
	public static final String SHORT_DATE = "yyyyMMdd";
	/**
	 * yyyyMM格式的日期转换器
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
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Calendar calendar;

	/**
	 * 初始化日历对象相关设置
	 * 
	 * @param calendar
	 */
	protected void setCalendar(Calendar calendar) {
		this.calendar = calendar;
		this.calendar.setLenient(false);
		this.calendar.setFirstDayOfWeek(Calendar.MONDAY);
	}

	/**
	 * 将常用日期对象封装为当前日期类实例对象
	 * 
	 * @param date
	 */
	public EasyDate(java.util.Date date) {
		setCalendar(new GregorianCalendar());
		calendar.setTimeInMillis(date.getTime());
	}

	/**
	 * 构造一个当前时间的日期实例对象
	 */
	public EasyDate() {
		setCalendar(new GregorianCalendar());
	}

	/**
	 * 根据指定的毫秒数构造对应的实例对象
	 * 
	 * @param date
	 */
	public EasyDate(long date) {
		setCalendar(new GregorianCalendar());
		calendar.setTimeInMillis(date);
	}

	/**
	 * 根据相对于指定时间的偏移值构造一个对应的实例对象<br>
	 * 例如，当前时间为：2012-10-10 例如要创建一个2013-10-10的时间对象，new EasyDate(null, 1, 0, 0)即可;<br>
	 * 创建一个2011-8-10的时间对象，new EasyDate(null, -1, -2, 0)或new EasyDate(null, 0,
	 * -14, 0)
	 * 
	 * @param date 指定的时间，作为偏移量的参考对象，如果为null，则默认使用当前时间作为参考对象<br>
	 *            该对象支持java.util.Date、me.ready.util.EasyDate、java.util.
	 *            Calendar等对象及其子类实例
	 * @param offsetYear 相对于当前时间的年份偏移量
	 * @param offsetMonth 相对于当前时间的月份偏移量
	 * @param doffsetDay 相对于当前时间的日期偏移量
	 */
	public EasyDate(Object date, int offsetYear, int offsetMonth, int offsetDay) {
		this(getTimeOfDate(date));
		if (offsetYear != 0) {
			calendar.add(Calendar.YEAR, offsetYear);
		}
		if (offsetMonth != 0) {
			calendar.add(Calendar.MONDAY, offsetMonth);
		}
		if (offsetDay != 0) {
			calendar.add(Calendar.DAY_OF_MONTH, offsetDay);
		}
	}

	/**
	 * 返回自 1970 年 1 月 1 日 00:00:00 GMT 以来指定日期对象表示的毫秒数。<br>
	 * 如果为null，则默认为当前时间
	 * 
	 * @param date
	 * @return
	 */
	private static final long getTimeOfDate(Object date) {
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
			throw new ClassCastException("指定的对象不是日期类型：" + date);
		}
		return theTime;
	}

	/**
	 * 根据年、月、日、时、分、秒、毫秒部分的值构造对应的实例对象
	 * 
	 * @param year 年份，如2012
	 * @param month 月份，如12
	 * @param day 日
	 * @param hh 小时
	 * @param mm 分钟
	 * @param ss 秒
	 * @param ms 毫秒
	 */
	public EasyDate(int year, int month, int day, int... args) {
		int[] theArgs = new int[] { 0, 0, 0, 0 };
		for (int i = 0; i < args.length; i++) {
			theArgs[i] = args[i];
		}
		setCalendar(new GregorianCalendar(year, month - 1, day, args[0], args[1], args[2]));
		calendar.set(Calendar.MILLISECOND, theArgs[3]);
	}

	/**
	 * 获取日期的年，例如：2012
	 * 
	 * @return
	 */
	public int getYear() {
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 设置日期的年，例如：2012
	 * 
	 * @param year
	 */
	public EasyDate setYear(int year) {
		calendar.set(Calendar.YEAR, year);
		return this;
	}

	/**
	 * 追加指定的年数，例如：当前年是2012，调用addYear(2)，则年份为2014
	 * 
	 * @param year 指定的年数，可以为负数
	 */
	public EasyDate addYear(int year) {
		calendar.add(Calendar.YEAR, year);
		return this;
	}

	/**
	 * 获取日期的月；返回值为1(一月)~12(十二月)
	 * 
	 * @return
	 */
	public int getMonth() {
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 设置日期的月；值为1(一月)~12(十二月)
	 * 
	 * @return
	 */
	public EasyDate setMonth(int month) {
		calendar.set(Calendar.MONTH, month - 1);
		return this;
	}

	/**
	 * 追加指定的月数，例如：当前是2012-05-12，调用addMonth(2)，则为2012-07-12
	 * 
	 * @param month 指定的月数，可以为负数
	 */
	public EasyDate addMonth(int month) {
		calendar.add(Calendar.MONDAY, month);
		return this;
	}

	/**
	 * 设置日期的年、月、日、时、分、秒、毫秒等部分的值<br>
	 * 如果未指定指定部分的值，则不会进行该部分的设置
	 * 
	 * @since 0.3
	 * @param year
	 * @param month
	 * @param day
	 * @param args
	 * @return
	 */
	public EasyDate set(int year, int month, int day, int... args) {
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		if (args.length > 0) {
			int[] fields = new int[] { Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND };
			int i = 0;
			do {
				calendar.set(fields[i], args[i]);
			} while (++i < args.length);
		}
		return this;
	}

	/**
	 * 获取日期的日；月份的第一天返回1
	 * 
	 * @return
	 */
	public int getDay() {
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 设置日期的日；月份的第一天为1
	 * 
	 * @return
	 */
	public EasyDate setDay(int day) {
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return this;
	}

	/**
	 * 追加指定的天数，例如：当前是2012-05-12，调用addDay(2)，则为2012-05-14
	 * 
	 * @param month 指定的天数，可以为负数
	 */
	public EasyDate addDay(int day) {
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return this;
	}

	/**
	 * 获取日期的星期；返回值为1(星期一)~7(星期天)
	 * 
	 * @return
	 */
	public int getWeekDay() {
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);
		return weekday == Calendar.SUNDAY ? 7 : --weekday;
	}

	/**
	 * 获取日期的时，返回值0~23
	 * 
	 * @return
	 */
	public int getHour() {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 设置日期的时，值为0~23
	 * 
	 * @return
	 */
	public EasyDate setHour(int hour) {
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		return this;
	}

	/**
	 * 追加指定的小时数，例如：当前是2012-05-12 12:12:56，调用addHour(3)，则为2012-05-12 15:12:56
	 * 
	 * @param hour 指定的小时数，可以为负数
	 */
	public EasyDate addHour(int hour) {
		calendar.add(Calendar.HOUR_OF_DAY, hour);
		return this;
	}

	/**
	 * 获取日期的分，返回值0~59
	 * 
	 * @return
	 */
	public int getMinute() {
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 设置日期的分，值为0~59
	 * 
	 * @param minute 指定的分钟数
	 * @return
	 */
	public EasyDate setMinute(int minute) {
		calendar.set(Calendar.MINUTE, minute);
		return this;
	}

	/**
	 * 追加指定的分钟数，例如：当前是2012-05-12 09:12:56，调用addMinute(3)，则为2012-05-12 09:15:56
	 * 
	 * @param minute 指定的分钟数，可以为负数
	 */
	public EasyDate addMinute(int minute) {
		calendar.add(Calendar.MINUTE, minute);
		return this;
	}

	/**
	 * 获取日期的秒，返回值0~59
	 * 
	 * @return
	 */
	public int getSecond() {
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * 设置日期的秒，值为0~59
	 * 
	 * @param second
	 */
	public EasyDate setSecond(int second) {
		calendar.set(Calendar.SECOND, second);
		return this;
	}

	/**
	 * 追加指定的秒数，例如：当前是2012-05-12 09:12:56 123，调用addMillisecond(123)，则为2012-05-12
	 * 09:12:56 246
	 * 
	 * @param ms 指定的毫秒数，可以为负数
	 */
	public EasyDate addMillisecond(int ms) {
		calendar.add(Calendar.MILLISECOND, ms);
		return this;
	}

	/**
	 * 获取日期的毫秒部分的值，返回值0~999
	 * 
	 * @return
	 */
	public int getMillisecond() {
		return calendar.get(Calendar.MILLISECOND);
	}

	/**
	 * 设置日期的毫秒部分的值，值为0~999
	 * 
	 * @param ms
	 */
	public EasyDate setMillisecond(int ms) {
		calendar.set(Calendar.MILLISECOND, ms);
		return this;
	}

	/**
	 * 追加指定的秒数，例如：当前是2012-05-12 09:12:56，调用addSecond(3)，则为2012-05-12 09:12:59
	 * 
	 * @param hour 指定的秒数，可以为负数
	 * @param second
	 */
	public EasyDate addSecond(int second) {
		calendar.add(Calendar.SECOND, second);
		return this;
	}

	/**
	 * 获取日期的时间值，以毫秒为单位
	 * 
	 * @return
	 */
	public long getTime() {
		return calendar.getTimeInMillis();
	}

	/**
	 * 设置日期的时间值，以毫秒为单位
	 * 
	 * @param date
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
	 * 
	 * @param date
	 */
	public void setDate(java.util.Date date) {
		calendar.setTime(date);
	}

	/**
	 * 获取日期当前月份的星期数
	 * 
	 * @return
	 */
	public int getWeeksOfMonth() {
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 获取日期当前年份的星期数
	 * 
	 * @return
	 */
	public int getWeeksOfYear() {
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 获取内置的日历对象
	 * 
	 * @return
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
	 * 
	 * @param date
	 * @return
	 */
	public static EasyDate smartParse(String date) {
		if (date == null) {
			throw new NullPointerException("指定的日期字符串不能为null");
		}
		int length = date.length(); // 字符串长度
		String format = null;
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
			throw new IllegalArgumentException("智能转换日期字符串时无法找到对应的DateFormat.");
		}
		return parse(format, date);
	}

	/**
	 * 将指定的java.util.Date对象转为EasyDate日期对象<br>
	 * 如果指定对象对null，则返回null
	 * 
	 * @param date
	 * @return
	 */
	public static final EasyDate valueOf(Date date) {
		if (date == null) {
			return null;
		}
		return new EasyDate(date.getTime());
	}

	/**
	 * 将指定的java.util.Calendar对象转为EasyDate日期对象<br>
	 * 如果指定对象对null，则返回null
	 * 
	 * @param date
	 * @return
	 */
	public static final EasyDate valueOf(Calendar date) {
		if (date == null) {
			return null;
		}
		return new EasyDate(date.getTime());
	}

	/**
	 * 将指定的字符串转为EasyDate日期对象<br>
	 * 如果指定对象对null，则返回null
	 * 
	 * @param date
	 * @return
	 */
	public static final EasyDate valueOf(String date) {
		if (date == null) {
			return null;
		}
		return smartParse(date);
	}

	/**
	 * 将指定格式的字符串转为对应的日期实例对象
	 * 
	 * @param format 一般情况无需自己创建，可直接调用EasyDate.DATE、EasyDate.DATETIME、EasyDate.
	 *            SHORT_DATE等内置的日期转换对象
	 * @param date 日期字符串
	 * @return
	 */
	public static EasyDate parse(DateFormat format, String date) {
		try {
			return new EasyDate(format.parse(date).getTime());
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 将指定格式的字符串转为对应的日期实例对象
	 * 
	 * @param format 指定的格式字符串，例如“yyyy-MM-dd”，内部将使用SimpleDateFormat进行转换
	 * @param date 日期字符串
	 * @return
	 */
	public static EasyDate parse(String format, String date) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return new EasyDate(dateFormat.parse(date));
		} catch (ParseException e) {
			throw new IllegalArgumentException("无法将指定的日期字符串[" + date + "]使用SimpleDateFormat按照指定的格式[" + format + "]转为日期对象");
		}
	}

	/**
	 * 转为java.util.Date
	 * 
	 * @return
	 */
	public java.util.Date toDate() {
		return new java.util.Date(calendar.getTimeInMillis());
	}

	/**
	 * 转为java.sql.Date
	 * 
	 * @return
	 */
	public java.sql.Date toSqlDate() {
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	/**
	 * 转为java.sql.Timestamp
	 * 
	 * @return
	 */
	public Timestamp toTimestamp() {
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 转为java.sql.Time
	 * 
	 * @return
	 */
	public Time toTime() {
		return new Time(calendar.getTimeInMillis());
	}

	/**
	 * 与指定日期进行比较，如果大于指定的日期返回正数；等于返回0；小于返回负数
	 * 
	 * @param date 
	 *            支持java.util.Date、java.util.Calendar、me.ready.util.EasyDate等对象及其子类的比较
	 * @return
	 */
	public int compareTo(Object date) {
		if (date == null) {
			throw new NullPointerException("用于比较的指定日期对象不能为null。");
		} else if (this == date) {
			return 0;
		}
		long diff = calendar.getTimeInMillis() - getTimeOfDate(date);
		if (diff == 0)
			return 0;
		return diff > 0 ? 1 : -1;
	}

	/**
	 * 判断指定年份是否为闰年
	 * 
	 * @param year 例如2012
	 * @return
	 */
	public boolean isLeapYear(int year) {
		return ((GregorianCalendar) calendar).isLeapYear(year);
	}

	/**
	 * 判断当前日期年份是否为闰年
	 * 
	 * @return
	 */
	public boolean isLeapYear() {
		return ((GregorianCalendar) calendar).isLeapYear(getYear());
	}

	/**
	 * 判断是否在指定日期的时间之后
	 * 
	 * @param date
	 * @return
	 */
	public boolean after(Object date) {
		return compareTo(date) > 0;
	}

	/**
	 * 获取当前月的最后一天
	 * 
	 * @since 0.3
	 * @return
	 */
	public int getLastDayOfMonth() {
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 将当前实例设置为指定时间字段范围内所能表示的最小值
	 * 
	 * @since 0.3
	 * @param field 该方法支持的字段有{@link Calendar#YEAR}、{@link Calendar#MONTH}、
	 *            {@link Calendar#DAY_OF_MONTH}、 {@link Calendar#HOUR_OF_DAY}、
	 *            {@link Calendar#MINUTE}、{@link Calendar#SECOND}
	 * @return
	 */
	public EasyDate beginOf(int field) {
		switch (field) {
		case Calendar.YEAR:
			calendar.set(Calendar.MONTH, 0);
		case Calendar.MONTH:
			calendar.set(Calendar.DAY_OF_MONTH, 1);
		case Calendar.DAY_OF_MONTH:
			calendar.set(Calendar.HOUR_OF_DAY, 0);
		case Calendar.HOUR_OF_DAY:
			calendar.set(Calendar.MINUTE, 0);
		case Calendar.MINUTE:
			calendar.set(Calendar.SECOND, 0);
		case Calendar.SECOND:
			calendar.set(Calendar.MILLISECOND, 0);
			break;
		default:
			throw new IllegalArgumentException("无法识别的日期字段:" + field);
		}
		return this;
	}

	/**
	 * 设置本地时间相对于GMT时间的偏移分钟数
	 * 
	 * @since 0.3
	 * @param minutes 偏移分钟数
	 * @return
	 */
	public EasyDate setTimeZoneOffset(int minutes) {
		StringBuilder timeZoneID = new StringBuilder().append("GMT").append((minutes > 0 ? '+' : '-')).append(minutes / 60);
		int min = minutes % 60;
		if (min > 0) {
			timeZoneID.append(':');
			if (min < 10) {
				timeZoneID.append('0');
			}
			timeZoneID.append(min);
		}
		System.out.println(timeZoneID);
		calendar.setTimeZone(TimeZone.getTimeZone(timeZoneID.toString()));
		return this;
	}

	/**
	 * 获取本地时间相对于GMT时间的偏移分钟数
	 * 
	 * @since 0.3
	 * @return
	 */
	public int getTimeZoneOffset() {
		TimeZone timeZone = calendar.getTimeZone();
		return timeZone.getOffset(calendar.getTimeInMillis()) / 60000;
	}

	/**
	 * 将当前实例设置为指定时间字段所能表示的最大值
	 * 
	 * @since 0.3
	 * @param field 该方法支持的字段有{@link Calendar#YEAR}、{@link Calendar#MONTH}、
	 *            {@link Calendar#DAY_OF_MONTH}、 {@link Calendar#HOUR_OF_DAY}、
	 *            {@link Calendar#MINUTE}、{@link Calendar#SECOND}
	 * @return
	 */
	public EasyDate endOf(int field) {
		switch (field) {
		case Calendar.YEAR:
			calendar.set(Calendar.MONTH, 11);
		case Calendar.MONTH:
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		case Calendar.DAY_OF_MONTH:
			calendar.set(Calendar.HOUR_OF_DAY, 23);
		case Calendar.HOUR_OF_DAY:
			calendar.set(Calendar.MINUTE, 59);
		case Calendar.MINUTE:
			calendar.set(Calendar.SECOND, 59);
		case Calendar.SECOND:
			calendar.set(Calendar.MILLISECOND, 999);
			break;
		default:
			throw new IllegalArgumentException("无法识别的日期字段:" + field);
		}
		return this;
	}

	/**
	 * 判断是否在指定日期的时间之前
	 * 
	 * @param date
	 * @return
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
		if (!(obj instanceof EasyDate))
			return false;
		EasyDate other = (EasyDate) obj;
		if (calendar == null) {
			if (other.calendar != null)
				return false;
		}
		return calendar.equals(other.calendar);
	}

	/**
	 * 返回yyyy-MM-dd格式的字符串
	 */
	@Override
	public String toString() {
		return new StringBuilder(Integer.toString(getYear())).append('-').append(getMonth()).append('-').append(getDay()).toString();
	}

	/**
	 * 返回使用日期格式化工具格式化后的字符串
	 * 
	 * @since 0.3
	 * @param 指定的格式化工具类
	 */
	public String toString(DateFormat format) {
		return format.format(toDate());
	}

	/**
	 * 返回yyyy-MM-dd HH:mm:ss格式的字符串
	 * 
	 * @return
	 */
	public String toLocaleString() {
		return new SimpleDateFormat(DATETIME).format(toDate());
	}

	/**
	 * 返回yyyyMMdd格式的字符串
	 * 
	 * @return
	 */
	public String toShortString() {
		return new SimpleDateFormat(SHORT_DATE).format(toDate());
	}

	/**
	 * 返回GMT标准格式的字符串，例如：1 Dec 2012 15:05:00 GMT
	 * 
	 * @return
	 */
	public String toGMTString() {
		return new SimpleDateFormat(GMT_DATE).format(toDate());
	}

	/**
	 * 返回Internet GMT标准格式的字符串,例如：Sat, 1 Dec 2012 23:05:00 GMT
	 * 
	 * @return
	 */
	public String toGMTNetString() {
		return new SimpleDateFormat(GMT_NET_DATE).format(toDate());
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
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
}
