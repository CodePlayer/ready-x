package me.codeplayer.util;

public interface NumBuffer {

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

	static NumBuffer of(char[] template) {
		return new CharNumBuffer(template);
	}

	static NumBuffer of(String template, boolean latin1) {
		if (JavaUtil.isJava9OrHigher) {
			return latin1 ? new Latin1ByteNumBuffer(template) : new StringBuilderNumBuffer(template);
		}
		return new CharNumBuffer(template);
	}

	static NumBuffer of(String template) {
		return of(template, JavaUtil.STRING_CODER.applyAsInt(template) == JavaUtil.LATIN1);
	}

	static NumBuffer of(StringBuilder template) {
		return new StringBuilderNumBuffer(template);
	}

	static NumBuffer ofBuilder(String template) {
		return new StringBuilderNumBuffer(template);
	}

}