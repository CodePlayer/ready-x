package me.codeplayer.util;

class StringBuilderCharReplacer implements CharReplacer {

	final StringBuilder chars;

	public StringBuilderCharReplacer(StringBuilder template) {
		this.chars = template;
	}

	public StringBuilderCharReplacer(String template) {
		this.chars = new StringBuilder(template.length()).append(template);
	}

	@Override
	public void setCharAt(int index, char ch) {
		chars.setCharAt(index, ch);
	}

	@Override
	public void pickChars(long number, int start, int end) {
		while (end-- > start) {
			chars.setCharAt(end, (char) ('0' + (number % 10)));
			number /= 10;
		}
	}

	@Override
	public void pickValidChars(long number, int start, int end) {
		while (number > 0 && end-- > start) {
			chars.setCharAt(end, (char) ('0' + (number % 10)));
			number /= 10;
		}
	}

	@Override
	public String toString() {
		return chars.toString();
	}

}