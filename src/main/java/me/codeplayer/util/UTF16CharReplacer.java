package me.codeplayer.util;

class UTF16CharReplacer implements CharReplacer {

	final char[] chars;

	public UTF16CharReplacer(char[] chars) {
		this.chars = chars;
	}

	public UTF16CharReplacer(String chars) {
		this.chars = chars.toCharArray();
	}

	@Override
	public void setCharAt(int index, char ch) {
		chars[index] = ch;
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