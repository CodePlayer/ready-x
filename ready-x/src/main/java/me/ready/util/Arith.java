package me.ready.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import me.ready.util.ChineseNumber.ChineseNumberStyle;

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
	public static final BigDecimal HANDRED = new BigDecimal(100);
	/** 表示数值 1000(一千) 的BigDecimal */
	public static final BigDecimal THOUSAND = new BigDecimal(1000);
	/** 表示数值 10 000(一万) 的BigDecimal */
	public static final BigDecimal MYRIAD = new BigDecimal(10000);
	/** 表示数值 100 000 000(一亿) 的BigDecimal */
	public static final BigDecimal HANDRED_MILLION = new BigDecimal(100000000);
	// property
	protected BigDecimal value;

	/**
	 * 构造一个为指定值的商业计算数
	 * 
	 * @param d
	 */
	public Arith(double d) {
		this(Double.toString(d));
	}

	/**
	 * 构造一个为指定值(字符串形式)的商业计算数
	 * 
	 * @param d
	 */
	public Arith(String d) {
		value = new BigDecimal(d);
	}

	/**
	 * 构造一个为指定double值的商业计算数
	 * 
	 * @param d
	 */
	public Arith(BigDecimal d) {
		if (d == null) {
			throw new NullPointerException("BigDecimal对象不能为null");
		}
		value = d;
	}

	/**
	 * 构造一个指定long值的商业计算数
	 * 
	 * @param d
	 */
	public Arith(long d) {
		value = new BigDecimal(d);
	}

	/**
	 * 构造一个指定int值的商业计算数
	 * 
	 * @param d
	 */
	public Arith(int d) {
		value = new BigDecimal(d);
	}

	/**
	 * 构造一个指定boolean值的商业计算数。 boolean值true=1，false=0
	 * 
	 * @param d
	 */
	public Arith(boolean d) {
		this(d ? 1 : 0);
	}

	/**
	 * 构造一个指定Object值的商业计算数
	 * 
	 * @param d
	 */
	public Arith(Object d) {
		if (d == null) {
			throw new NullPointerException("Object对象不能为null");
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
	 * @return
	 */
	public Arith add(BigDecimal d) {
		value = value.add(d);
		return this;
	}

	/**
	 * 商业加法运算
	 * 
	 * @param d 指定的加数
	 * @return
	 */
	public Arith add(String d) {
		return add(new BigDecimal(d));
	}

	/**
	 * 商业加法运算
	 * 
	 * @param d 指定的加数
	 * @return
	 */
	public Arith add(double d) {
		return add(Double.toString(d));
	}

	/**
	 * 商业加法运算
	 * 
	 * @param d 指定的加数
	 * @return
	 */
	public Arith add(long d) {
		return add(BigDecimal.valueOf(d));
	}

	/**
	 * 商业减法运算
	 * 
	 * @param d 指定的减数
	 * @return
	 */
	public Arith minus(BigDecimal d) {
		value = value.subtract(d);
		return this;
	}

	/**
	 * 商业减法运算
	 * 
	 * @param d 指定的减数
	 * @return
	 */
	public Arith minus(String d) {
		return minus(new BigDecimal(d));
	}

	/**
	 * 商业减法运算
	 * 
	 * @param d 指定的减数
	 * @return
	 */
	public Arith minus(double d) {
		return minus(Double.toString(d));
	}

	/**
	 * 商业减法运算
	 * 
	 * @param d 指定的减数
	 * @return
	 */
	public Arith minus(long d) {
		return minus(BigDecimal.valueOf(d));
	}

	/**
	 * 商业乘法运算
	 * 
	 * @param d 指定的乘数
	 * @return
	 * @throws ArithmeticException 如果无法除尽或除数为0则会抛出该异常，无法除尽时请使用{@code Arith#divide(BigDecimal, int, RoundingMode)}替代
	 */
	public Arith multiply(BigDecimal d) throws ArithmeticException {
		value = value.multiply(d);
		return this;
	}

	/**
	 * 商业乘法运算
	 * 
	 * @param d 指定的乘数
	 * @return
	 */
	public Arith multiply(String d) {
		return multiply(new BigDecimal(d));
	}

	/**
	 * 商业乘法运算
	 * 
	 * @param d 指定的乘数
	 * @return
	 */
	public Arith multiply(double d) {
		return multiply(Double.toString(d));
	}

	/**
	 * 商业乘法运算
	 * 
	 * @param d 指定的乘数
	 * @return
	 */
	public Arith multiply(long d) {
		return multiply(BigDecimal.valueOf(d));
	}

	/**
	 * 商业除法运算
	 * 
	 * @param d 指定的除数
	 * @return
	 * @throws ArithmeticException 如果无法除尽或除数为0则会抛出该异常，无法除尽时请使用{@code Arith#divide(BigDecimal, int, RoundingMode)}替代
	 */
	public Arith divide(BigDecimal d) throws ArithmeticException {
		value = value.divide(d);
		return this;
	}

	/**
	 * 商业除法运算
	 * 
	 * @param d 指定的除数
	 * @return
	 * @throws ArithmeticException 如果无法除尽或除数为0则会抛出该异常，无法除尽时请使用{@code Arith#divide(String, int, RoundingMode)}替代
	 */
	public Arith divide(String d) throws ArithmeticException {
		return divide(new BigDecimal(d));
	}

	/**
	 * 商业除法运算
	 * 
	 * @param d 指定的除数
	 * @return
	 * @throws ArithmeticException 如果无法除尽或除数为0则会抛出该异常，无法除尽时请使用{@code Arith#divide(double, int, RoundingMode)}替代
	 */
	public Arith divide(double d) {
		return divide(Double.toString(d));
	}

	/**
	 * 商业除法运算
	 * 
	 * @param d 指定的除数
	 * @return
	 * @throws ArithmeticException 如果无法除尽或除数为0则会抛出该异常，无法除尽时请使用{@code Arith#divide(long, int, RoundingMode)}替代
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
	 * @return
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
	 * @return
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
	 * @return
	 */
	public Arith divide(double d, int scale, RoundingMode roundingMode) {
		return divide(Double.toString(d), scale, roundingMode);
	}

	/**
	 * 商业除法运算
	 * 
	 * @param d 指定的除数
	 * @param scale 指定的精确位数
	 * @param roundingMode 设置应用的舍入模式(四舍五入、向上舍入、向下舍去等)
	 * @return
	 */
	public Arith divide(long d, int scale, RoundingMode roundingMode) {
		return divide(BigDecimal.valueOf(d), scale, roundingMode);
	}

	/**
	 * 以四舍五入的舍入方式进行商业除法运算
	 * 
	 * @param d 指定的除数
	 * @param scale 指定的精确位数
	 * @return
	 */
	public Arith divideRound(BigDecimal d, int scale) {
		return divide(d, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 以四舍五入的舍入方式进行商业除法运算
	 * 
	 * @param d 指定的除数
	 * @param scale 指定的精确位数
	 * @return
	 */
	public Arith divideRound(String d, int scale) {
		return divide(d, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 以四舍五入的舍入方式进行商业除法运算
	 * 
	 * @param d 指定的除数
	 * @param scale 指定的精确位数
	 * @return
	 */
	public Arith divideRound(double d, int scale) {
		return divide(Double.toString(d), scale, RoundingMode.HALF_UP);
	}

	/**
	 * 以四舍五入的舍入方式进行商业除法运算
	 * 
	 * @param d 指定的除数
	 * @param scale 指定的精确位数
	 * @return
	 */
	public Arith divideRound(long d, int scale) {
		return divide(BigDecimal.valueOf(d), scale, RoundingMode.HALF_UP);
	}

	/**
	 * 设置精度(即精确到的小数位数)
	 * 
	 * @param newScale 指定的精确位数
	 * @param roundingMode 设置应用的舍入模式(四舍五入、向上舍入、向下舍去等)
	 * @return
	 */
	public Arith setScale(int newScale, RoundingMode roundingMode) {
		value = value.setScale(newScale, roundingMode);
		return this;
	}

	/**
	 * 设置四舍五入的精度(即精确到的小数位数)
	 * 
	 * @param newScale 指定的精确位数
	 * @return
	 */
	public Arith round(int newScale) {
		return setScale(newScale, RoundingMode.HALF_UP);
	}

	/**
	 * 将当前BigDecimal进行向上舍入到相邻的整数
	 * 
	 * @return
	 */
	public Arith ceil() {
		return setScale(0, RoundingMode.CEILING);
	}

	/**
	 * 将当前BigDecimal进行向下舍去到相邻的整数
	 * 
	 * @return
	 */
	public Arith floor() {
		return setScale(0, RoundingMode.FLOOR);
	}

	/**
	 * 转换为BigDecimal
	 * 
	 * @return
	 */
	public BigDecimal toBigDecimal() {
		return value;
	}

	/**
	 * 转换为BigInteger
	 * 
	 * @return
	 */
	public BigInteger toBigInteger() {
		return value.toBigInteger();
	}

	/**
	 * 转换为double值
	 * 
	 * @return
	 */
	public double doubleValue() {
		return value.doubleValue();
	}

	/**
	 * 转换为四舍五入精确到指定小数位的double值
	 * 
	 * @return
	 */
	public double doubleValue(int scale) {
		return value.setScale(scale, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 转换为int值
	 * 
	 * @return
	 */
	public int intValue() {
		return value.intValue();
	}

	/**
	 * 转换为long值
	 * 
	 * @return
	 */
	public long longValue() {
		return value.longValue();
	}

	/**
	 * 转换为float值
	 * 
	 * @return
	 */
	public float floatValue() {
		return value.floatValue();
	}

	/**
	 * 转换为byte值
	 * 
	 * @return
	 */
	public byte byteValue() {
		return value.byteValue();
	}

	/**
	 * 转换为short值
	 * 
	 * @return
	 */
	public short shortValue() {
		return value.shortValue();
	}

	/**
	 * 商业加法运算
	 * 
	 * @param a 加数1
	 * @param b 加数2
	 * @param more 更多的其他加数
	 * @return
	 */
	public static final double add(double a, double b) {
		return new BigDecimal(Double.toString(a)).add(new BigDecimal(Double.toString(b))).doubleValue();
	}

	/**
	 * 商业减法运算
	 * 
	 * @param a 被减数
	 * @param b 减数
	 * @return
	 */
	public static final double minus(double a, double b) {
		return new BigDecimal(Double.toString(a)).add(new BigDecimal(Double.toString(-b))).doubleValue();
	}

	/**
	 * 商业乘法运算
	 * 
	 * @param a 乘数1
	 * @param b 乘数2
	 * @return
	 */
	public static final double multiply(double a, double b) {
		return new BigDecimal(Double.toString(a)).multiply(new BigDecimal(Double.toString(b))).doubleValue();
	}

	/**
	 * 商业乘法运算
	 * 
	 * @param a 乘数1
	 * @param b 乘数2
	 * @param scale 小数部分的精确位数
	 * @param 舍入模式
	 * @return
	 */
	public static final double multiply(double a, double b, int scale, RoundingMode roundingMode) {
		return new BigDecimal(Double.toString(a)).divide(new BigDecimal(Double.toString(b))).setScale(scale, roundingMode).doubleValue();
	}

	/**
	 * 商业乘法运算(四舍五入)
	 * 
	 * @param a 乘数1
	 * @param b 乘数2
	 * @param scale 小数部分的精确位数
	 * @return
	 */
	public static final double multiply(double a, double b, int scale) {
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
	 * @return
	 */
	public static final double multiply(double a, double b, long precision) {
		MathContext context = new MathContext((int) precision, RoundingMode.HALF_UP);
		return new BigDecimal(Double.toString(a)).multiply(new BigDecimal(Double.toString(b)), context).doubleValue();
	}

	/**
	 * 商业除法运算(四舍五入)
	 * 
	 * @param a 被除数
	 * @param b 除数
	 * @param scale 小数精确度位数
	 * @return
	 */
	public static final double divide(double a, double b, int scale) {
		Assert.notTrue(scale < 0, "指定的小数位数不能小于0!");
		return new BigDecimal(Double.toString(a)).divide(new BigDecimal(Double.toString(b)), scale, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 以四舍五入({@link RoundingMode#HALF_UP})的方式使指定小数精确到指定的小数位数
	 * 
	 * @param d 指定的小数
	 * @param scale 指定的小数精确位数
	 * @return
	 */
	public static final double round(double d, int scale) {
		return scale(d, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 以 {@link RoundingMode#HALF_EVEN} 的方式使指定小数精确到指定的小数位数
	 * 
	 * @param d 指定的小数
	 * @param scale 指定的小数精确位数
	 * @return
	 */
	public static final double even(double d, int scale) {
		return scale(d, scale, RoundingMode.HALF_EVEN);
	}

	/**
	 * 以指定的舍入方式使指定小数精确到指定的小数位数
	 * 
	 * @param num 指定的小数
	 * @param scale 指定的小数精确位数
	 * @return
	 */
	public static final double scale(double d, int scale, RoundingMode mode) {
		Assert.notTrue(scale < 0, "执行舍入时，指定的精确小数位数不能小于0!");
		return new BigDecimal(Double.toString(d)).setScale(scale, mode).doubleValue();
	}

	/**
	 * 向上取整，返回大于或等于指定数值的最小整数
	 * 
	 * @param d 指定的数值
	 * @return
	 */
	public static final long ceil(double d) {
		return (long) Math.ceil(d);
	}

	/**
	 * 向下取整，返回小于或等于指定数值的最大整数
	 * 
	 * @param d 指定的数值
	 * @return
	 */
	public static final long floor(double d) {
		return (long) Math.floor(d);
	}

	/**
	 * 以指定舍入方式使指定小数精确到指定的小数位数
	 * 
	 * @param d 指定的小数
	 * @param scale 指定的小数精确位数
	 * @param mode 指定的舍入模式
	 * @return
	 */
	public static final double truncate(double d, int scale, RoundingMode mode) {
		Assert.notTrue(scale < 0, "指定的精确小数位数不能小于0!");
		return new BigDecimal(Double.toString(d)).setScale(scale, mode).doubleValue();
	}

	/**
	 * 判断两个数值a和b的大小
	 * 
	 * @param a
	 * @param b
	 * @return 如果：
	 *         <ul>
	 *         <li>a > b ，则返回 1</li>
	 *         <li>a == b ，则返回 0</li>
	 *         <li>a < b ，则返回 -1</li>
	 *         </ul>
	 */
	public static final int compareTo(BigDecimal a, BigDecimal b) {
		return a.compareTo(b);
	}

	/**
	 * 判断两个数值a和b的大小
	 * 
	 * @param a
	 * @param b
	 * @return 如果：
	 *         <ul>
	 *         <li>a > b ，则返回 1</li>
	 *         <li>a == b ，则返回 0</li>
	 *         <li>a < b ，则返回 -1</li>
	 *         </ul>
	 */
	public static final int compareTo(BigDecimal a, double b) {
		return a.compareTo(BigDecimal.valueOf(b));
	}

	/**
	 * 判断两个数值a和b的大小
	 * 
	 * @param a
	 * @param b
	 * @return 如果：
	 *         <ul>
	 *         <li>a > b ，则返回 1</li>
	 *         <li>a == b ，则返回 0</li>
	 *         <li>a < b ，则返回 -1</li>
	 *         </ul>
	 */
	public static final int compareTo(BigDecimal a, long b) {
		return a.compareTo(BigDecimal.valueOf(b));
	}

	/**
	 * 判断两个数值a和b的大小
	 * 
	 * @param a
	 * @param b
	 * @return 如果：
	 *         <ul>
	 *         <li>a > b ，则返回 1</li>
	 *         <li>a == b ，则返回 0</li>
	 *         <li>a < b ，则返回 -1</li>
	 *         </ul>
	 */
	public static final int compareTo(BigDecimal a, String b) {
		return a.compareTo(new BigDecimal(b));
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
		return new ChineseNumber(ignoreFractionalPart ? value.setScale(0).toString() : value.toString(), upperCase ? ChineseNumberStyle.UPPER_CASE : ChineseNumberStyle.LOWER_CASE).toString();
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
	 * 
	 */
	public String toMoneyUpperCase() {
		return new ChineseNumber(value.setScale(2).toString(), ChineseNumberStyle.MONEY).toString();
	}
}
