package me.codeplayer.util;

public interface CharReplacer {

	/**
	 * 将指定的字符数组中的指定索引处的字符替换为指定的字符
	 */
	void setCharAt(int index, char ch);

	/**
	 * 将指定的数字设置到指定的字符数组中的指定索引处，并填充指定的长度，如果数字的长度不够，则在前面填充0
	 *
	 * @param number 指定的数字
	 * @param start 指定的起始索引
	 * @param end 结束索引（不包含）
	 */
	void pickChars(long number, int start, int end);

	/**
	 * 将指定的数字设置到指定的字符数组中的指定索引处，从右向左依次填充，并最多填充指定的长度
	 *
	 * @param number 指定的数值，如果 {@code number == 0}，则不填充
	 * @param start 指定的起始索引
	 * @param end 结束索引（不包含）
	 */
	void pickValidChars(long number, int start, int end);

	static CharReplacer of(char[] template) {
		return new UTF16CharReplacer(template);
	}

	static CharReplacer of(String template, boolean targetLatin1) {
		return of(template, JavaX.STRING_CODER.applyAsInt(template) == JavaX.LATIN1, targetLatin1);
	}

	static CharReplacer of(String template, boolean sourceLatin1, boolean targetLatin1) {
		if (JavaX.isJava9OrHigher) {
			return sourceLatin1 && targetLatin1 ? new Latin1CharReplacer(template) : new StringBuilderCharReplacer(template);
		}
		return new UTF16CharReplacer(template);
	}

	static CharReplacer of(String template, char targetChar) {
		return of(template, targetChar >>> 8 == 0);
	}

	static CharReplacer of(StringBuilder template) {
		return new StringBuilderCharReplacer(template);
	}

	static CharReplacer ofBuilder(String template) {
		return new StringBuilderCharReplacer(template);
	}

}