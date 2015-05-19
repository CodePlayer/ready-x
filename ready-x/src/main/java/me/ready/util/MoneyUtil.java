package me.ready.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 将阿拉伯数字形式的整数转换为中文大写形式的字符串的工具类
 * 
 * @author Ready
 * @date 2013-4-20
 */
public abstract class MoneyUtil {

	/**
	 * 中文数字 0-9： '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'
	 */
	private static final char[] CHINESE_NUMBERS = new char[] { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' };
	/**
	 * 中文小单位：'仟', '佰', '拾'
	 */
	private static final char[] CHINESE_UNITS = new char[] { '仟', '佰', '拾' };
	/**
	 * 中文大单位：'亿', '万'
	 */
	private static final char[] CHINESE_BIG_UNITS = new char[] { '亿', '万' };

	/**
	 * 格式化阿拉伯数字为中文大写形式
	 * 
	 * @param numberStr 不能传入非阿拉伯形式的数字字符串
	 * @return
	 */
	public static String parseNumber(long number) {
		StringBuilder chineseMoney = new StringBuilder();
		if (number == 0) {
			chineseMoney.append(CHINESE_NUMBERS[0]);
			return chineseMoney.toString();
		}
		String numberStr = Long.toString(number);
		int length = numberStr.length();
		// 计算cell的个数
		int size = length / 4;
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
			Cell cell = new Cell(numberStr.substring(startIndex, length));
			cells.add(cell);
		} while ((length -= 4) > 0);
		// 反向迭代cell，从高位到低位取出
		int index = size;
		boolean leftEndWithZero = false; // 指示当前计算单元左侧的单元是否以0结尾
		while (index-- > 0) {
			Cell cell = cells.get(index);
			if (cell.chinese.length() > 0) {
				if (leftEndWithZero || cell.startWithZero) {
					chineseMoney.append(CHINESE_NUMBERS[0]);
				}
				chineseMoney.append(cell.chinese);
				if (index > 0) { // 如果不是最后一个单元，并且当前单位不全是0，则追加单位'亿'或'万'
					chineseMoney.append(CHINESE_BIG_UNITS[index % 2]);
				}
			}
			leftEndWithZero = cell.endWithZero; // 传递给循环外的变量保存，便于下一个计算单元进行判断
		}
		return chineseMoney.toString();
	}

	/**
	 * 计算单元，接收一个4位以内的阿拉伯数字字符串，并将其转换为对应的中文大写形式
	 * 
	 * @author Ready
	 * @date 2013-4-19
	 */
	public static class Cell {

		private String chinese = ""; // 中文形式的字符串
		private String source; // 原阿拉伯数字形式的字符串
		private boolean startWithZero; // 当前计算单元是否以0开头
		private boolean endWithZero; // 当前计算单元是否以0结尾

		/**
		 * 构造函数
		 * 
		 * @param moneyCell
		 */
		public Cell(String moneyCell) {
			this.source = moneyCell;
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
							money.append(CHINESE_NUMBERS[0]);
						}
						money.append(CHINESE_NUMBERS[currentInt]); // 添加中文数字
						if (i < index) { // 如果不是个位数，添加单位
							money.append(CHINESE_UNITS[4 + i - length]);
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
}
