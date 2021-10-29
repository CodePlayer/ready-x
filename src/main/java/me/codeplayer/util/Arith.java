package me.codeplayer.util;

import java.math.*;

import me.codeplayer.util.ChineseNumber.*;

/**
 * 用于商业运算的常用计算工具类
 *
 * @author Ready
 * @date 2012-9-27
 */
public class Arith {

	// constant
	/** 表示数值 0(零) 的BigDecimal */
	public static final BigDecimal ZERO = BigDecimal.ZERO;
	/** 表示数值 1(一) 的BigDecimal */
	public static final BigDecimal ONE = BigDecimal.ONE;
	/** 表示数值 10(一十) 的BigDecimal */
	public static final BigDecimal TEN = BigDecimal.TEN;
	/** 表示数值 100(一百) 的BigDecimal */
	public static final BigDecimal HUNDRED = new BigDecimal(100);
	/** 表示数值 1000(一千) 的BigDecimal */
	public static final BigDecimal THOUSAND = new BigDecimal(1000);
	/** 表示数值 10 000(一万) 的BigDecimal */
	public static final BigDecimal MYRIAD = new BigDecimal(10000);
	/** 表示数值 100 000 000(一亿) 的BigDecimal */
	public static final BigDecimal HANDRED_MILLION = new BigDecimal(10000_0000);
	// property
	protected BigDecimal value;

	/**
	 * 构造一个为指定值的商业计算数
	 */
	public Arith(double d) {
		this(Double.toString(d));
	}

	/**
	 * 构造一个为指定值(字符串形式)的商业计算数
	 */
	public Arith(String d) {
		value = new BigDecimal(d);
	}

	/**
	 * 构造一个为指定double值的商业计算数
	 */
	public Arith(BigDecimal d) {
		value = Assert.notNull(d);
	}

	/**
	 * 构造一个指定long值的商业计算数
	 */
	public Arith(long d) {
		value = new BigDecimal(d);
	}

	/**
	 * 构造一个指定int值的商业计算数
	 */
	public Arith(int d) {
		value = new BigDecimal(d);
	}

	/**
	 * 构造一个指定boolean值的商业计算数。 boolean值true=1，false=0
	 */
	public Arith(boolean d) {
		this(d ? 1 : 0);
	}

	/**
	 * 构造一个指定Object值的商业计算数
	 */
	public Arith(Object d) {
		if (d == null) {
			throw new NullPointerException();
		}
		if (d instanceof BigDecimal) {
			value = (BigDecimal) d;
		} else if (d instanceof Number) {
			Number n = (Number) d;
			long val = n.longValue();
			if (val == n.doubleValue()) {
				value = new BigDecimal(val);
			} else {
				value = new BigDecimal(d.toString());
			}
		} else {
			value = new BigDecimal(d.toString());
		}
	}

	/**
	 * 构造一个默认值为0的商业计算数
	 */
	public Arith() {
		value = BigDecimal.ZERO;
	}

	/**
	 * 商业加法运算
	 *
	 * @param d 指定的加数
	 */
	public Arith add(BigDecimal d) {
		value = value.add(d);
		return this;
	}

	/**
	 * 商业加法运算
	 *
	 * @param d 指定的加数
	 */
	public Arith add(String d) {
		return add(new BigDecimal(d));
	}

	/**
	 * 商业加法运算
	 *
	 * @param d 指定的加数
	 */
	public Arith add(double d) {
		return add(Double.toString(d));
	}

	/**
	 * 商业加法运算
	 *
	 * @param d 指定的加数
	 */
	public Arith add(long d) {
		return add(BigDecimal.valueOf(d));
	}

	/**
	 * 商业减法运算
	 *
	 * @param d 指定的减数
	 */
	public Arith minus(BigDecimal d) {
		value = value.subtract(d);
		return this;
	}

	/**
	 * 商业减法运算
	 *
	 * @param d 指定的减数
	 */
	public Arith minus(String d) {
		return minus(new BigDecimal(d));
	}

	/**
	 * 商业减法运算
	 *
	 * @param d 指定的减数
	 */
	public Arith minus(double d) {
		return minus(Double.toString(d));
	}

	/**
	 * 商业减法运算
	 *
	 * @param d 指定的减数
	 */
	public Arith minus(long d) {
		return minus(BigDecimal.valueOf(d));
	}

	/**
	 * 商业乘法运算
	 *
	 * @param d 指定的乘数
	 * @throws ArithmeticException 如果无法除尽或除数为0则会抛出该异常，无法除尽时请使用{@link #divide(BigDecimal, int, RoundingMode)}替代
	 */
	public Arith multiply(BigDecimal d) throws ArithmeticException {
		value = value.multiply(d);
		return this;
	}

	/**
	 * 商业乘法运算
	 *
	 * @param d 指定的乘数
	 */
	public Arith multiply(String d) {
		return multiply(new BigDecimal(d));
	}

	/**
	 * 商业乘法运算
	 *
	 * @param d 指定的乘数
	 */
	public Arith multiply(double d) {
		return multiply(Double.toString(d));
	}

	/**
	 * 商业乘法运算
	 *
	 * @param d 指定的乘数
	 */
	public Arith multiply(long d) {
		return multiply(BigDecimal.valueOf(d));
	}

	/**
	 * 商业除法运算
	 *
	 * @param d 指定的除数
	 * @throws ArithmeticException 如果无法除尽或除数为0则会抛出该异常，无法除尽时请使用{@link #divide(BigDecimal, int, RoundingMode)}替代
	 */
	public Arith divide(BigDecimal d) throws ArithmeticException {
		value = value.divide(d);
		return this;
	}

	/**
	 * 商业除法运算
	 *
	 * @param d 指定的除数
	 * @throws ArithmeticException 如果无法除尽或除数为0则会抛出该异常，无法除尽时请使用{@link #divide(String, int, RoundingMode)}替代
	 */
	public Arith divide(String d) throws ArithmeticException {
		return divide(new BigDecimal(d));
	}

	/**
	 * 商业除法运算
	 *
	 * @param d 指定的除数
	 * @throws ArithmeticException 如果无法除尽或除数为0则会抛出该异常，无法除尽时请使用{@link #divide(double, int, RoundingMode)}替代
	 */
	public Arith divide(double d) {
		return divide(Double.toString(d));
	}

	/**
	 * 商业除法运算
	 *
	 * @param d 指定的除数
	 * @throws ArithmeticException 如果无法除尽或除数为0则会抛出该异常，无法除尽时请使用{@link #divide(long, int, RoundingMode)}替代
	 */
	public Arith divide(long d) {
		return divide(BigDecimal.valueOf(d));
	}

	/**
	 * 商业除法运算
	 *
	 * @param d            指定的除数
	 * @param scale        指定的精确位数
	 * @param roundingMode 设置应用的舍入模式(四舍五入、向上舍入、向下舍去等)
	 */
	public Arith divide(BigDecimal d, int scale, RoundingMode roundingMode) {
		value = value.divide(d, scale, roundingMode);
		return this;
	}

	/**
	 * 商业除法运算
	 *
	 * @param d            指定的除数
	 * @param scale        指定的精确位数
	 * @param roundingMode 设置应用的舍入模式(四舍五入、向上舍入、向下舍去等)
	 */
	public Arith divide(String d, int scale, RoundingMode roundingMode) {
		return divide(new BigDecimal(d), scale, roundingMode);
	}

	/**
	 * 商业除法运算
	 *
	 * @param d            指定的除数
	 * @param scale        指定的精确位数
	 * @param roundingMode 设置应用的舍入模式(四舍五入、向上舍入、向下舍去等)
	 */
	public Arith divide(double d, int scale, RoundingMode roundingMode) {
		return divide(Double.toString(d), scale, roundingMode);
	}

	/**
	 * 商业除法运算
	 *
	 * @param d            指定的除数
	 * @param scale        指定的精确位数
	 * @param roundingMode 设置应用的舍入模式(四舍五入、向上舍入、向下舍去等)
	 */
	public Arith divide(long d, int scale, RoundingMode roundingMode) {
		return divide(BigDecimal.valueOf(d), scale, roundingMode);
	}

	/**
	 * 以四舍五入的舍入方式进行商业除法运算
	 *
	 * @param d     指定的除数
	 * @param scale 指定的精确位数
	 */
	public Arith divideRound(BigDecimal d, int scale) {
		return divide(d, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 以四舍五入的舍入方式进行商业除法运算
	 *
	 * @param d     指定的除数
	 * @param scale 指定的精确位数
	 */
	public Arith divideRound(String d, int scale) {
		return divide(d, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 以四舍五入的舍入方式进行商业除法运算
	 *
	 * @param d     指定的除数
	 * @param scale 指定的精确位数
	 */
	public Arith divideRound(double d, int scale) {
		return divide(Double.toString(d), scale, RoundingMode.HALF_UP);
	}

	/**
	 * 以四舍五入的舍入方式进行商业除法运算
	 *
	 * @param d     指定的除数
	 * @param scale 指定的精确位数
	 */
	public Arith divideRound(long d, int scale) {
		return divide(BigDecimal.valueOf(d), scale, RoundingMode.HALF_UP);
	}

	/**
	 * 设置精度(即精确到的小数位数)
	 *
	 * @param newScale     指定的精确位数
	 * @param roundingMode 设置应用的舍入模式(四舍五入、向上舍入、向下舍去等)
	 */
	public Arith setScale(int newScale, RoundingMode roundingMode) {
		value = value.setScale(newScale, roundingMode);
		return this;
	}

	/**
	 * 设置四舍五入的精度(即精确到的小数位数)
	 *
	 * @param newScale 指定的精确位数
	 */
	public Arith round(int newScale) {
		return setScale(newScale, RoundingMode.HALF_UP);
	}

	/**
	 * 设置四舍五入的精度(即精确到的小数位数)<br>
	 * <b>注意：</b>该方法底层直接根据需求自行计算，常规情况下比 {@link #round(int) } 要快 10+倍，但目前尚处于实验性质（不过一般基本上没什么问题）
	 *
	 * @param value 指定的数值
	 * @param newScale 指定的精确位数
	 */
	public static double roundFast(final double value, final int newScale) {
		final double factor = Math.pow(10, newScale + 1);
		final double target = value * factor;
		if (newScale > 10 || target < Long.MIN_VALUE || target > Long.MAX_VALUE || Math.abs(target) > Double.MAX_VALUE) {
			return round(value, newScale);
		}
		final double adjust = 0.000000000001;
		long val = (long) (target + adjust);
		final long remainder = val % 10;
		if (remainder >= 5) {
			val += 10 - remainder;
		} else {
			val -= remainder;
		}
		return val / factor;
	}

	/**
	 * 将当前BigDecimal进行向上舍入到相邻的整数
	 */
	public Arith ceil() {
		return setScale(0, RoundingMode.CEILING);
	}

	/**
	 * 将当前BigDecimal进行向下舍去到相邻的整数
	 */
	public Arith floor() {
		return setScale(0, RoundingMode.FLOOR);
	}

	/**
	 * 转换为BigDecimal
	 */
	public BigDecimal toBigDecimal() {
		return value;
	}

	/**
	 * 转换为BigInteger
	 */
	public BigInteger toBigInteger() {
		return value.toBigInteger();
	}

	/**
	 * 转换为double值
	 */
	public double doubleValue() {
		return value.doubleValue();
	}

	/**
	 * 转换为四舍五入精确到指定小数位的double值
	 */
	public double doubleValue(int scale) {
		return value.setScale(scale, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 转换为int值
	 */
	public int intValue() {
		return value.intValue();
	}

	/**
	 * 转换为long值
	 */
	public long longValue() {
		return value.longValue();
	}

	/**
	 * 转换为float值
	 */
	public float floatValue() {
		return value.floatValue();
	}

	/**
	 * 转换为byte值
	 */
	public byte byteValue() {
		return value.byteValue();
	}

	/**
	 * 转换为short值
	 */
	public short shortValue() {
		return value.shortValue();
	}

	/**
	 * 商业加法运算
	 *
	 * @param a 加数1
	 * @param b 加数2
	 */
	public static double add(double a, double b) {
		return new BigDecimal(Double.toString(a)).add(new BigDecimal(Double.toString(b))).doubleValue();
	}

	/**
	 * 商业减法运算
	 *
	 * @param a 被减数
	 * @param b 减数
	 */
	public static double minus(double a, double b) {
		return new BigDecimal(Double.toString(a)).add(new BigDecimal(Double.toString(-b))).doubleValue();
	}

	/**
	 * 商业乘法运算
	 *
	 * @param a 乘数1
	 * @param b 乘数2
	 */
	public static double multiply(double a, double b) {
		return new BigDecimal(Double.toString(a)).multiply(new BigDecimal(Double.toString(b))).doubleValue();
	}

	/**
	 * 商业乘法运算
	 *
	 * @param a 乘数1
	 * @param b 乘数2
	 * @param scale 小数部分的精确位数
	 */
	public static double multiply(double a, double b, int scale, RoundingMode roundingMode) {
		return new BigDecimal(Double.toString(a)).multiply(new BigDecimal(Double.toString(b))).setScale(scale, roundingMode).doubleValue();
	}

	/**
	 * 商业乘法运算(四舍五入)
	 *
	 * @param a 乘数1
	 * @param b 乘数2
	 * @param scale 小数部分的精确位数
	 */
	public static double multiply(double a, double b, int scale) {
		return multiply(a, b, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 商业乘法运算(四舍五入)<br>
	 * <strong>注意：</strong>此方法的有效位数包含整数部分在内<br>
	 * 将precision设为long类型只是为了重载的需要
	 *
	 * @param a 乘数1
	 * @param b 乘数2
	 * @param precision 包含整数部分的有效位数
	 */
	public static double multiply(double a, double b, long precision) {
		MathContext context = new MathContext((int) precision, RoundingMode.HALF_UP);
		return new BigDecimal(Double.toString(a)).multiply(new BigDecimal(Double.toString(b)), context).doubleValue();
	}

	/**
	 * 商业除法运算(四舍五入)
	 *
	 * @param a 被除数
	 * @param b 除数
	 * @param scale 小数精确度位数
	 */
	public static double divide(double a, double b, int scale) {
		checkScale(scale);
		return new BigDecimal(Double.toString(a)).divide(new BigDecimal(Double.toString(b)), scale, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 以四舍五入({@link RoundingMode#HALF_UP})的方式使指定小数精确到指定的小数位数
	 *
	 * @param d     指定的小数
	 * @param scale 指定的小数精确位数
	 */
	public static double round(double d, int scale) {
		return scale(d, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 以 {@link RoundingMode#HALF_EVEN} 的方式使指定小数精确到指定的小数位数
	 *
	 * @param d     指定的小数
	 * @param scale 指定的小数精确位数
	 */
	public static double even(double d, int scale) {
		return scale(d, scale, RoundingMode.HALF_EVEN);
	}

	/**
	 * 以指定的舍入方式使指定小数精确到指定的小数位数
	 *
	 * @param d 指定的小数
	 * @param scale 指定的小数精确位数
	 */
	public static double scale(double d, int scale, RoundingMode mode) {
		checkScale(scale);
		return new BigDecimal(Double.toString(d)).setScale(scale, mode).doubleValue();
	}

	/**
	 * 向上取整，返回大于或等于指定数值的最小整数
	 *
	 * @param d 指定的数值
	 */
	public static long ceil(double d) {
		return (long) Math.ceil(d);
	}

	/**
	 * 向下取整，返回小于或等于指定数值的最大整数
	 *
	 * @param d 指定的数值
	 */
	public static long floor(double d) {
		return (long) Math.floor(d);
	}

	/**
	 * 以指定舍入方式使指定小数精确到指定的小数位数
	 *
	 * @param d 指定的小数
	 * @param scale 指定的小数精确位数
	 * @param mode 指定的舍入模式
	 */
	public static double truncate(double d, int scale, RoundingMode mode) {
		checkScale(scale);
		return new BigDecimal(Double.toString(d)).setScale(scale, mode).doubleValue();
	}

	static void checkScale(int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("Argument 'scale' can not less than 0:" + scale);
		}
	}

	/**
	 * 判断两个数值a和b的大小
	 *
	 * @return 如果：
	 *   <ul>
	 *       <li>a > b ，则返回 1</li>
	 *       <li>a == b ，则返回 0</li>
	 *       <li>a < b ，则返回 -1</li>
	 *   </ul>
	 */
	public static int compareTo(BigDecimal a, BigDecimal b) {
		return a == b ? 0 : a.compareTo(b);
	}

	/**
	 * 判断两个数值a和b的大小
	 *
	 * @return 如果：
	 *         <ul>
	 *         <li>a > b ，则返回 1</li>
	 *         <li>a == b ，则返回 0</li>
	 *         <li>a < b ，则返回 -1</li>
	 *         </ul>
	 */
	public static int compareTo(BigDecimal a, double b) {
		return a.compareTo(BigDecimal.valueOf(b));
	}

	/**
	 * 判断两个数值a和b的大小
	 *
	 * @return 如果：
	 *         <ul>
	 *         <li>a > b ，则返回 1</li>
	 *         <li>a == b ，则返回 0</li>
	 *         <li>a < b ，则返回 -1</li>
	 *         </ul>
	 */
	public static int compareTo(BigDecimal a, long b) {
		return a.compareTo(BigDecimal.valueOf(b));
	}

	/**
	 * 判断两个数值a和b的大小
	 *
	 * @return 如果：
	 *         <ul>
	 *         <li>a > b ，则返回 1</li>
	 *         <li>a == b ，则返回 0</li>
	 *         <li>a < b ，则返回 -1</li>
	 *         </ul>
	 */
	public static int compareTo(BigDecimal a, String b) {
		return a.compareTo(new BigDecimal(b));
	}

	/**
	 * 检测指定的BigDecimal是否为整数值
	 *
	 * @author Ready
	 */
	public static boolean isIntegral(BigDecimal d) {
		return d != null && (d.scale() == 0 || d.setScale(0, RoundingMode.FLOOR).compareTo(d) == 0);
	}

	/**
	 * 检测数值d能否被divisor整除(即：余数为0)
	 *
	 * @param d 被除数
	 * @param divisor 除数
	 * @author Ready
	 * @since 1.0
	 */
	public static boolean canDivideExactly(BigDecimal d, BigDecimal divisor) {
		return divisor.compareTo(BigDecimal.ZERO) != 0 && d.remainder(divisor).compareTo(BigDecimal.ZERO) == 0;
	}

	/**
	 * 输出数值字符串
	 */
	public String toString() {
		return value.toString();
	}

	/**
	 * 输出以四舍五入模式保留指定小数位精度的数值字符串
	 */
	public String toString(int scale) {
		return value.setScale(scale, RoundingMode.HALF_UP).toString();
		// return value.divide(BigDecimal.ONE, scale, RoundingMode.HALF_UP).toString();
	}

	/**
	 * 输出中文形式的数值字符串，例如："135000000"->"一亿三千五百万"
	 *
	 * @param ignoreFractionalPart 是否忽略小数部分
	 */
	public String toChineseString(boolean ignoreFractionalPart, boolean upperCase) {
		return new ChineseNumber(ignoreFractionalPart ? value.setScale(0).toString() : value.toString(), upperCase ? FormatStyle.UPPER_CASE : FormatStyle.LOWER_CASE).toString();
	}

	/**
	 * 输出大写中文形式的数值字符串，例如："135000000"->"壹亿叁千伍佰万"
	 *
	 * @param ignoreFractionalPart 是否忽略小数部分
	 */
	public String toChineseUpperCase(boolean ignoreFractionalPart) {
		return toChineseString(ignoreFractionalPart, true);
	}

	/**
	 * 输出大写中文形式的用于金额(人民币)显示的数值字符串，例如："135000000"->"壹亿叁千伍佰万元整"
	 */
	public String toMoneyUpperCase() {
		return new ChineseNumber(value.setScale(2, RoundingMode.UNNECESSARY).toString(), FormatStyle.MONEY).toString();
	}

	/**
	 * 将指定的整数转换为对应的 BigDecimal（内部对于常用数值进行缓存处理）
	 *
	 * @since 2.8
	 */
	public static BigDecimal toBigDecimal(int n) {
		switch (n) {
		case 0:
			return BigDecimal.ZERO;
		case 1:
			return BigDecimal.ONE;
		case 10:
			return BigDecimal.TEN;
		case 100:
			return Arith.HUNDRED;
		case 1000:
			return THOUSAND;
		case 10000:
			return MYRIAD;
		case 100000000:
			return HANDRED_MILLION;
		default:
			return BigDecimal.valueOf(n);
		}
	}
}