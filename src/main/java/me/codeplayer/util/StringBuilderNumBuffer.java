package me.codeplayer.util;

class StringBuilderNumBuffer implements NumBuffer {

	final StringBuilder chars;

	public StringBuilderNumBuffer(StringBuilder template) {
		this.chars = template;
	}

	public StringBuilderNumBuffer(String template) {
		this.chars = new StringBuilder(template.length()).append(template);
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