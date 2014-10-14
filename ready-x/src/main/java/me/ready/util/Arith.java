package me.ready.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 用于商业运算的常用计算工具类
 * @author Ready
 * @date 2012-9-27
 */
public class Arith {

	// 禁止创建实例
	private Arith() {}
	/**
	 * 表示数值0的BigDecimal静态常量
	 */
	public static final BigDecimal ZERO = new BigDecimal(0);
	/**
	 * 表示数值1的BigDecimal静态常量 
	 */
	public static final BigDecimal ONE = new BigDecimal(1);

	/**
	 * 商业加法运算
	 * @param a 加数1
	 * @param b 加数2
	 * @return
	 */
	public static double add(double a, double b) {
		return new BigDecimal(Double.toString(a)).add(new BigDecimal(Double.toString(b))).doubleValue();
	}

	/**
	 * 商业减法运算
	 * @param a 被减数
	 * @param b 减数
	 * @return
	 */
	public static double minus(double a, double b) {
		return add(a, -b);
	}

	/**
	 * 商业乘法运算
	 * @param a 乘数1
	 * @param b 乘数2
	 * @return
	 */
	public static double multiply(double a, double b) {
		return new BigDecimal(Double.toString(a)).multiply(new BigDecimal(Double.toString(b))).doubleValue();
	}

	/**
	 * 商业乘法运算(四舍五入)
	 * @param a 乘数1
	 * @param b 乘数2
	 * @param scale 小数部分的精确位数
	 * @return
	 */
	public static double multiply(double a, double b, int scale) {
		return new BigDecimal(Double.toString(a)).multiply(new BigDecimal(Double.toString(b))).divide(new BigDecimal(1), scale, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 商业乘法运算(四舍五入)<br>
	 * <strong>注意：</strong>此方法的有效位数包含整数部分在内<br>
	 * 将precision设为long类型只是为了重载的需要
	 * @param a 乘数1
	 * @param b 乘数2
	 * @param precision 包含整数部分的有效位数
	 * @return
	 */
	public static double multiply(double a, double b, long precision) {
		MathContext context = new MathContext((int) precision, RoundingMode.HALF_UP);
		return new BigDecimal(Double.toString(a)).multiply(new BigDecimal(Double.toString(b)), context).doubleValue();
	}

	/**
	 * 商业除法运算(四舍五入)
	 * @param a 被除数
	 * @param b 除数
	 * @param scale 小数精确度位数
	 * @return
	 */
	public static double divide(double a, double b, int scale) {
		Assert.notTrue(scale < 0, "指定的小数位数不能小于0！");
		return new BigDecimal(Double.toString(a)).divide(new BigDecimal(Double.toString(b)), scale, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 以四舍五入的方式使指定小数精确到指定的小数位数
	 * @param d 指定的小数
	 * @param scale 指定的小数精确位数
	 * @return
	 */
	public static double round(double d, int scale) {
		Assert.notTrue(scale < 0, "四舍五入时，指定的精确小数位数不能小于0！");
		return new BigDecimal(Double.toString(d)).divide(ONE, scale, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 以指定舍入方式使指定小数精确到指定的小数位数
	 * @param d 指定的小数
	 * @param scale 指定的小数精确位数
	 * @param mode 指定的舍入模式
	 * @return
	 */
	public static double truncate(double d, int scale, RoundingMode mode) {
		Assert.notTrue(scale < 0, "指定的精确小数位数不能小于0！");
		return new BigDecimal(Double.toString(d)).divide(ONE, scale, mode).doubleValue();
	}
}
