package me.codeplayer.util;

class CharNumBuffer implements NumBuffer {

	final char[] chars;

	public CharNumBuffer(char[] chars) {
		this.chars = chars;
	}

	public CharNumBuffer(String chars) {
		this.chars = chars.toCharArray();
	}

	@Override
	public void pickChars(long number, int start, int end) {
		while (end-- > start) {
			chars[end] = (char) ('0' + (number % 10));
			number /= 10;
		}
	}

	@Override
	public void pickValidChars(long number, int start, int end) {
		while (number > 0 && end-- > start) {
			chars[end] = (char) ('0' + (number % 10));
			number /= 10;
		}
	}

	@Override
	public String toString() {
		return JavaUtil.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
	}

}