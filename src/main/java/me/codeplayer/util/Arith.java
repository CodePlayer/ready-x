package me.codeplayer.util;

import java.math.*;
import javax.annotation.Nullable;

/**
 * 用于商业运算的常用计算工具类
 *
 * @author Ready
 * @since 2012-9-27
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
	 * 构造一个为指定 double 值的商业计算数
	 */
	public Arith(double val) {
		value = toBigDecimal(val);
	}

	/**
	 * 构造一个为指定数值（字符串形式）的商业计算数
	 */
	public Arith(String val) {
		value = new BigDecimal(val);
	}

	/**
	 * 构造一个为指定 BigDecimal 值的商业计算数
	 */
	public Arith(BigDecimal val) {
		value = Assert.notNull(val);
	}

	/**
	 * 构造一个为指定 BigInteger 值的商业计算数
	 */
	public Arith(BigInteger val) {
		value = new BigDecimal(val);
	}

	/**
	 * 构造一个指定long值的商业计算数
	 */
	public Arith(long d) {
		value = BigDecimal.valueOf(d);
	}

	/**
	 * 构造一个指定boolean值的商业计算数。 boolean值true=1，false=0
	 */
	public Arith(boolean b) {
		this.value = b ? BigDecimal.ONE : BigDecimal.ZERO;
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
		return add(toBigDecimal(d));
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
		return minus(toBigDecimal(d));
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
		return multiply(toBigDecimal(d));
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
	 * @deprecated 不建议使用，请使用{@link #divide(BigDecimal, int, RoundingMode)}替代
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
	 * @deprecated 不建议使用，请使用{@link #divide(String, int, RoundingMode)}替代
	 */
	public Arith divide(String d) throws ArithmeticException {
		return divide(new BigDecimal(d));
	}

	/**
	 * 商业除法运算
	 *
	 * @param d 指定的除数
	 * @throws ArithmeticException 如果无法除尽或除数为0则会抛出该异常，无法除尽时请使用{@link #divide(double, int, RoundingMode)}替代
	 * @deprecated 不建议使用，请使用{@link #divide(double, int, RoundingMode)}替代
	 */
	public Arith divide(double d) {
		return divide(toBigDecimal(d));
	}

	/**
	 * 商业除法运算
	 *
	 * @param d 指定的除数
	 * @throws ArithmeticException 如果无法除尽或除数为0则会抛出该异常，无法除尽时请使用{@link #divide(long, int, RoundingMode)}替代
	 * @deprecated 不建议使用，请使用{@link #divide(long, int, RoundingMode)}替代
	 */
	public Arith divide(long d) {
		return divide(BigDecimal.valueOf(d));
	}

	/**
	 * 商业除法运算
	 *
	 * @param d 指定的除数
	 * @param scale 指定的精确位数
	 * @param roundingMode 设置应用的舍入模式(四舍五入、向上舍入、向下舍去等)
	 */
	public Arith divide(BigDecimal d, int scale, RoundingMode roundingMode) {
		value = value.divide(d, scale, roundingMode);
		return this;
	}

	/**
	 * 商业除法运算
	 *
	 * @param d 指定的除数
	 * @param scale 指定的精确位数
	 * @param roundingMode 设置应用的舍入模式(四舍五入、向上舍入、向下舍去等)
	 */
	public Arith divide(String d, int scale, RoundingMode roundingMode) {
		return divide(new BigDecimal(d), scale, roundingMode);
	}

	/**
	 * 商业除法运算
	 *
	 * @param d 指定的除数
	 * @param scale 指定的精确位数
	 * @param roundingMode 设置应用的舍入模式(四舍五入、向上舍入、向下舍去等)
	 */
	public Arith divide(double d, int scale, RoundingMode roundingMode) {
		return divide(toBigDecimal(d), scale, roundingMode);
	}

	/**
	 * 商业除法运算
	 *
	 * @param d 指定的除数
	 * @param scale 指定的精确位数
	 * @param roundingMode 设置应用的舍入模式(四舍五入、向上舍入、向下舍去等)
	 */
	public Arith divide(long d, int scale, RoundingMode roundingMode) {
		return divide(BigDecimal.valueOf(d), scale, roundingMode);
	}

	/**
	 * 以四舍五入的舍入方式进行商业除法运算
	 *
	 * @param d 指定的除数
	 * @param scale 指定的精确位数
	 */
	public Arith divideRound(BigDecimal d, int scale) {
		return divide(d, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 以四舍五入的舍入方式进行商业除法运算
	 *
	 * @param d 指定的除数
	 * @param scale 指定的精确位数
	 */
	public Arith divideRound(String d, int scale) {
		return divide(d, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 以四舍五入的舍入方式进行商业除法运算
	 *
	 * @param d 指定的除数
	 * @param scale 指定的精确位数
	 */
	public Arith divideRound(double d, int scale) {
		return divide(toBigDecimal(d), scale, RoundingMode.HALF_UP);
	}

	/**
	 * 以四舍五入的舍入方式进行商业除法运算
	 *
	 * @param d 指定的除数
	 * @param scale 指定的精确位数
	 */
	public Arith divideRound(long d, int scale) {
		return divide(BigDecimal.valueOf(d), scale, RoundingMode.HALF_UP);
	}

	/**
	 * 设置精度(即精确到的小数位数)
	 *
	 * @param newScale 指定的精确位数
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
		if (value < 0 || newScale > 10) {
			return round(value, newScale);
		}
		final double factor = Math.pow(10, newScale + 1);
		final double target = value * factor;
		if (target < Long.MIN_VALUE || target > Long.MAX_VALUE || Math.abs(target) > Double.MAX_VALUE) {
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
	 * 将当前数值进行 向上舍入，并保留指定的小数位数
	 */
	public Arith ceil(int newScale) {
		return setScale(newScale, RoundingMode.CEILING);
	}

	/**
	 * 将当前数值进行 向上舍入 到相邻的整数
	 */
	public Arith ceil() {
		return ceil(0);
	}

	/**
	 * 将当前数值进行 向下舍去，并保留指定的小数位数
	 */
	public Arith floor(int newScale) {
		return setScale(newScale, RoundingMode.FLOOR);
	}

	/**
	 * 将当前数值进行 向下舍去 到相邻的整数
	 */
	public Arith floor() {
		return floor(0);
	}

	/**
	 * 转换为 BigDecimal
	 */
	public BigDecimal toBigDecimal() {
		return value;
	}

	/**
	 * 转换为 BigDecimal
	 */
	public BigDecimal value() {
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
		return toBigDecimal(a).add(toBigDecimal(b)).doubleValue();
	}

	/**
	 * 商业减法运算
	 *
	 * @param a 被减数
	 * @param b 减数
	 */
	public static double minus(double a, double b) {
		return toBigDecimal(a).add(toBigDecimal(-b)).doubleValue();
	}

	/**
	 * 商业乘法运算
	 *
	 * @param a 乘数1
	 * @param b 乘数2
	 */
	public static double multiply(double a, double b) {
		return toBigDecimal(a).multiply(toBigDecimal(b)).doubleValue();
	}

	/**
	 * 商业乘法运算
	 *
	 * @param a 乘数1
	 * @param b 乘数2
	 * @param scale 小数部分的精确位数
	 */
	public static double multiply(double a, double b, int scale, RoundingMode roundingMode) {
		return toBigDecimal(a).multiply(toBigDecimal(b)).setScale(scale, roundingMode).doubleValue();
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
	 * @param precision 包含整数部分的有效位数，0 表示不限制
	 */
	public static double multiplyInContext(double a, double b, long precision) {
		MathContext context = new MathContext((int) precision, RoundingMode.HALF_UP);
		return toBigDecimal(a).multiply(toBigDecimal(b), context).doubleValue();
	}

	/**
	 * 商业除法运算(四舍五入)
	 *
	 * @param a 被除数
	 * @param b 除数
	 * @param scale 小数精确度位数
	 */
	public static double divide(double a, double b, int scale) {
		return toBigDecimal(a).divide(toBigDecimal(b), scale, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 以四舍五入({@link RoundingMode#HALF_UP})的方式使指定小数精确到指定的小数位数
	 *
	 * @param d 指定的小数
	 * @param scale 指定的小数精确位数
	 */
	public static double round(double d, int scale) {
		return scale(d, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 以 {@link RoundingMode#HALF_EVEN} 的方式使指定小数精确到指定的小数位数
	 *
	 * @param d 指定的小数
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
		return toBigDecimal(d).setScale(scale, mode).doubleValue();
	}

	/**
	 * 以指定的舍入方式使指定小数精确到指定的小数位数
	 *
	 * @param val 指定的小数
	 * @param scale 指定的小数精确位数
	 */
	public static BigDecimal fastScale(double val, int scale, RoundingMode mode) {
		BigDecimal d = toBigDecimal(val);
		return d.scale() <= scale ? d : d.setScale(scale, mode);
	}

	/**
	 * 向上取整，返回大于或等于指定数值的最小整数
	 *
	 * @param d 指定的数值
	 */
	public static long ceilToLong(double d) {
		return (long) Math.ceil(d);
	}

	/**
	 * 向下取整，返回小于或等于指定数值的最大整数
	 *
	 * @param d 指定的数值
	 */
	public static long floorToLong(double d) {
		return (long) Math.floor(d);
	}

	/**
	 * 判断两个数值a和b的大小
	 *
	 * @return 如果：
	 * <ul>
	 *     <li>a > b ，则返回 1</li>
	 *     <li>a == b ，则返回 0</li>
	 *     <li>a < b ，则返回 -1</li>
	 * </ul>
	 */
	public static int compareTo(BigDecimal a, BigDecimal b) {
		return a == b ? 0 : a.compareTo(b);
	}

	/**
	 * 判断两个数值a和b的大小
	 *
	 * @return 如果：
	 * <ul>
	 * <li>a > b ，则返回 1</li>
	 * <li>a == b ，则返回 0</li>
	 * <li>a < b ，则返回 -1</li>
	 * </ul>
	 */
	public static int compareTo(BigDecimal a, double b) {
		return a.compareTo(toBigDecimal(b));
	}

	/**
	 * 判断两个数值a和b的大小
	 *
	 * @return 如果：
	 * <ul>
	 * <li>a > b ，则返回 1</li>
	 * <li>a == b ，则返回 0</li>
	 * <li>a < b ，则返回 -1</li>
	 * </ul>
	 */
	public static int compareTo(BigDecimal a, long b) {
		return a.compareTo(BigDecimal.valueOf(b));
	}

	/**
	 * 判断两个数值a和b的大小
	 *
	 * @return 如果：
	 * <ul>
	 * <li>a > b ，则返回 1</li>
	 * <li>a == b ，则返回 0</li>
	 * <li>a < b ，则返回 -1</li>
	 * </ul>
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
	 *
	 * @see BigDecimal#toPlainString()
	 */
	@Override
	public String toString() {
		return value.toPlainString();
	}

	/**
	 * 输出数值字符串
	 *
	 * @see BigDecimal#toString()
	 */
	public String toRawString() {
		return value.toString();
	}

	/**
	 * 输出以四舍五入模式保留指定小数位精度的数值字符串
	 */
	public String toString(int scale) {
		return value.setScale(scale, RoundingMode.HALF_UP).toPlainString();
		// return value.divide(BigDecimal.ONE, scale, RoundingMode.HALF_UP).toString();
	}

	/**
	 * 构造一个指定Object值的商业计算数
	 */
	public static Arith valueOf(Object value) throws NullPointerException {
		if (value instanceof Number) {
			if (value instanceof BigDecimal) {
				return new Arith((BigDecimal) value);
			} else if (value instanceof BigInteger) {
				return new Arith(((BigInteger) value));
			}
			final Number n = (Number) value;
			long val = n.longValue();
			if (val == n.doubleValue()) {
				return new Arith(val);
			}
		} else if (value instanceof Boolean) {
			return new Arith(((Boolean) value));
		}
		return new Arith(value.toString());
	}

	/**
	 * 构造一个指定Object值的商业计算数
	 */
	public static Arith valueOfOrZero(@Nullable Object value) {
		return value == null ? new Arith(BigDecimal.ZERO) : valueOf(value);
	}

	/**
	 * 将指定的 double 转换为对应的 BigDecimal
	 * <p>
	 * 如果是整数，会自动进行优化处理，避免 new BigDedimal(Double.toString(val)) 的字符串转换及解析开销
	 *
	 * @since 4.0.0
	 */
	public static BigDecimal toBigDecimal(double val) {
		final long n = (long) val;
		return n == val ? BigDecimal.valueOf(n) : BigDecimal.valueOf(val);
	}

	/**
	 * 将指定的整数转换为对应的 BigDecimal（内部对于常用数值进行缓存处理）
	 *
	 * @since 2.8
	 */
	public static BigDecimal toBigDecimal(int n) {
		switch (n) {
			case 0:
				return ZERO;
			case 1:
				return ONE;
			case 10:
				return TEN;
			case 100:
				return HUNDRED;
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