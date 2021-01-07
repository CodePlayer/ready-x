package me.codeplayer.util;

import java.math.*;
import java.util.*;

/**
 * 将阿拉伯数字形式的整数转换为中文大写形式的字符串的工具类
 *
 * @author Ready
 * @date 2013-4-20
 */
public class ChineseNumber {

	/** 中文大单位：'亿', '万' */
	static final char[] BIG_UNITS = new char[] { '亿', '万' };
	/** 中文货币单位：'圆', '角', '分' */
	static final char[] CURRENCY_UNITS = new char[] { '圆', '角', '分' };
	// properties
	protected final String number;
	/** 为null表示忽略小数；为""表示不忽略小数，但没有小数部分 */
	protected final String fraction;
	protected final FormatStyle style;
	protected transient String text;

	protected ChineseNumber(String val, String fraction, FormatStyle style) {
		this.number = val;
		this.fraction = fraction;
		this.style = style;
	}

	public ChineseNumber(long val, FormatStyle style) {
		this(Long.toString(val), null, style);
	}

	public ChineseNumber(String fullVal, FormatStyle style) {
		int point = fullVal.indexOf('.');
		this.number = point == -1 ? fullVal : fullVal.substring(0, point);
		this.fraction = point == -1 ? null : fullVal.substring(point + 1);
		this.style = style;
	}

	public ChineseNumber(double val, FormatStyle style) {
		this(new BigDecimal(Double.toString(val)).toString(), style);
	}

	public ChineseNumber(BigDecimal d, FormatStyle style) {
		this(d.toPlainString(), style);
	}

	/**
	 * 追加整数部分的中文数值字符串
	 */
	protected StringBuilder appendIntValue(StringBuilder sb) {
		if ("0".equals(number)) {
			return sb.append(style.getNumbersText()[0]);
		}
		int length = number.length();
		// 计算cell的个数
		int size = length >> 2;
		if (length % 4 != 0) {
			size++;
		}
		// 循环截取4位数字字符串组装成cell，放入List中
		List<Cell> cells = new ArrayList<Cell>(size);
		do {
			int startIndex = length - 4;
			if (startIndex < 0) { // 如果开始索引小于0，重置为0
				startIndex = 0;
			}
			Cell cell = new Cell(number.substring(startIndex, length), style);
			cells.add(cell);
		} while ((length -= 4) > 0);
		// 反向迭代cell，从高位到低位取出
		int index = size;
		boolean leftEndWithZero = false; // 指示当前计算单元左侧的单元是否以0结尾
		while (index-- > 0) {
			Cell cell = cells.get(index);
			if (cell.chinese.length() > 0) {
				if (leftEndWithZero || cell.startWithZero) {
					sb.append(style.getNumbersText()[0]);
				}
				sb.append(cell.chinese);
				if (index > 0) { // 如果不是最后一个单元，并且当前单位不全是0，则追加单位'亿'或'万'
					sb.append(BIG_UNITS[index & 1]);
				}
			}
			leftEndWithZero = cell.endWithZero; // 传递给循环外的变量保存，便于下一个计算单元进行判断
		}
		return sb;
	}

	/**
	 * 追加整数部分的中文数值字符串
	 */
	protected StringBuilder appendDecimalValue(StringBuilder sb) {
		int len = fraction == null ? 0 : fraction.length();
		if (style == FormatStyle.MONEY) { // 如果是金额
			sb.append(CURRENCY_UNITS[0]);
			boolean empty = len == 0;
			if (!empty) {
				// 角
				char ch = fraction.charAt(0);
				if (ch != '0') {
					sb.append(style.getNumberChar(ch)).append(CURRENCY_UNITS[1]);
				} else {
					empty = true;
				}
				// 分
				if (len > 1) {
					ch = fraction.charAt(1);
					if (ch != '0') {
						if (empty) { // 如果单位"角"上对应的数值是"0"，则需要预添加一个"零"
							sb.append('零');
							empty = false;
						}
						sb.append(style.getNumberChar(ch)).append(CURRENCY_UNITS[2]);
					}
				}
			}
			if (empty) {
				sb.append('整');
			}
		} else if (len > 0) {
			sb.append('点');
			for (int i = 0; i < len; i++) {
				sb.append(style.getNumberChar(fraction.charAt(i)));
			}
		}
		return sb;
	}

	@Override
	public String toString() {
		if (text == null) {
			StringBuilder sb = new StringBuilder();
			appendIntValue(sb);
			appendDecimalValue(sb);
			text = sb.toString();
		}
		return text;
	}

	/**
	 * 格式化指定的数值为中文字符串
	 *
	 * @param d             指定的数值
	 * @param ignoreDecimal 是否忽略小数部分
	 * @param style         指定中文字符串的格式
	 * @author Ready
	 * @since 1.0
	 */
	public static final String formatNumber(double d, boolean ignoreDecimal, FormatStyle style) {
		if (ignoreDecimal) {
			return new ChineseNumber((long) d, style).toString();
		} else {
			return new ChineseNumber(d, style).toString();
		}
	}

	/**
	 * 格式化指定的数值为中文字符串
	 *
	 * @param d             指定的数值
	 * @param ignoreDecimal 是否忽略小数部分
	 * @param style         指定中文字符串的格式
	 * @author Ready
	 * @since 1.0
	 */
	public static final String formatNumber(BigDecimal d, boolean ignoreDecimal, FormatStyle style) {
		if (ignoreDecimal) {
			return new ChineseNumber(d.longValue(), style).toString();
		} else {
			return new ChineseNumber(d, style).toString();
		}
	}

	/**
	 * 格式化指定的数值为中文字符串
	 *
	 * @param d     指定的数值
	 * @param style 指定中文字符串的格式
	 * @author Ready
	 * @since 1.0
	 */
	public static final String formatNumber(long d, FormatStyle style) {
		return new ChineseNumber(d, style).toString();
	}

	/**
	 * 计算单元，接收一个4位以内的阿拉伯数字字符串，并将其转换为对应的中文大写形式
	 *
	 * @author Ready
	 * @date 2013-4-19
	 */
	public static class Cell {

		private String chinese = ""; // 中文形式的字符串
		private final String source; // 原阿拉伯数字形式的字符串
		private final FormatStyle style;
		private boolean startWithZero; // 当前计算单元是否以0开头
		private boolean endWithZero; // 当前计算单元是否以0结尾

		/**
		 * 构造函数
		 */
		public Cell(String moneyCell, FormatStyle style) {
			this.source = moneyCell;
			this.style = style;
			init();
		}

		/**
		 * 初始化
		 */
		public void init() {
			int length;
			if (source != null && (length = source.length()) > 0) {
				StringBuilder money = new StringBuilder();
				boolean preHasValidValue = false; // 前面是否有有效值(不为0)
				boolean lastIsZero = false; // 上一个是否为0
				int index = length - 1;
				for (int i = 0; i < length; i++) {
					char ch = source.charAt(i);
					int currentInt = Character.digit(ch, 10);
					if (i == index && currentInt == 0) { // 如果是最后一个数字，并且数字为0
						endWithZero = true;
					}
					if (currentInt > 0) { // 如果当前数字不是0
						if (preHasValidValue && lastIsZero) { // 前面存在有效值并且上一个数字是0，则此处追加'零'
							money.append(style.getNumbersText()[0]);
						}
						money.append(style.getNumbersText()[currentInt]); // 添加中文数字
						if (i < index) { // 如果不是个位数，添加单位
							money.append(style.getUnitsText()[4 + i - length]);
						}
						preHasValidValue = true;
						lastIsZero = false;
					} else { // 如果是0
						if (i == 0) { // 如果第一个数为0
							startWithZero = true;
						}
						lastIsZero = true;
					}
				}
				chinese = money.toString();
			}
		}
	}

	/**
	 * 表示中文数字的文本表现形式的枚举类
	 *
	 * @author Ready
	 * @date 2015年8月21日
	 * @since 1.0
	 */
	public static enum FormatStyle {
		/**
		 * 中文数字：'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'<br>
		 * 中文单位：'亿', '万', '千', '百', '十'
		 */
		LOWER_CASE(new char[] { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九' }, new char[] { '千', '百', '十' }),
		/**
		 * 中文数字：'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'<br>
		 * 中文单位：'亿', '万', '仟', '佰', '拾'
		 */
		UPPER_CASE(new char[] { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' }, new char[] { '仟', '佰', '拾' }),
		/**
		 * 中文数字：'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'<br>
		 * 中文单位：'亿', '万', '仟', '佰', '拾'<br>
		 * 特殊处理：整数部分结尾追加字符"元"。如果不包含小数部分或小数部分为0，则再追加上字符"整"，否则追加对应数值的"x角y分"，并且最多精确到单位"分"
		 */
		MONEY(UPPER_CASE.getNumbersText(), UPPER_CASE.getUnitsText());

		final char[] numbersText;
		final char[] unitsText;

		FormatStyle(char[] numbersText, char[] unitsText) {
			this.numbersText = numbersText;
			this.unitsText = unitsText;
		}

		public char[] getNumbersText() {
			return numbersText;
		}

		public char[] getUnitsText() {
			return unitsText;
		}

		public char getNumberChar(char ch) {
			return numbersText[Character.digit(ch, 10)];
		}
	}
}
