package me.ready.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 用于商业运算的常用计算工具类
 * 
 * @author Ready
 * @date 2012-9-27
 */
public class Arith {

	protected BigDecimal value;

	/**
	 * 构造一个为指定值的商业计算数
	 * 
	 * @param d
	 */
	public Arith(double d) {
		value = new BigDecimal(Double.toString(d));
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
	public Arith add(double d) {
		value = value.add(new BigDecimal(Double.toString(d)));
		return this;
	}

	/**
	 * 商业减法运算
	 * 
	 * @param d 指定的减数
	 * @return
	 */
	public Arith minus(double d) {
		return add(-d);
	}

	/**
	 * 商业乘法运算
	 * 
	 * @param d 指定的乘数
	 * @return
	 */
	public Arith multiply(double d) {
		value = value.multiply(new BigDecimal(Double.toString(d)));
		return this;
	}

	/**
	 * 商业除法运算
	 * 
	 * @param d 指定的除数
	 * @return
	 */
	public Arith divide(double d) {
		value = value.divide(new BigDecimal(Double.toString(d)));
		return this;
	}

	/**
	 * 设置精度(即精确到的小数位数)
	 * 
	 * @param newScale 指定的精确位数
	 * @param roundingMode 设置应用的舍入模式(四舍五入、向上舍入、向下舍去等)
	 * @return
	 */
	public Arith setScale(int newScale, RoundingMode roundingMode) {
		value.setScale(newScale, roundingMode);
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
	 * 转换为int值
	 * 
	 * @return
	 */
	public double intValue() {
		return value.intValue();
	}

	/**
	 * 转换为long值
	 * 
	 * @return
	 */
	public double longValue() {
		return value.longValue();
	}

	/**
	 * 转换为float值
	 * 
	 * @return
	 */
	public double floatValue() {
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
	 * 以四舍五入的方式使指定小数精确到指定的小数位数
	 * 
	 * @param d 指定的小数
	 * @param scale 指定的小数精确位数
	 * @return
	 */
	public static final double round(double d, int scale) {
		Assert.notTrue(scale < 0, "四舍五入时，指定的精确小数位数不能小于0!");
		return new BigDecimal(Double.toString(d)).setScale(scale, RoundingMode.HALF_UP).doubleValue();
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
	 * 输出数值字符串
	 */
	public String toString() {
		return value.toString();
	}
}
