package me.codeplayer.util;

import java.nio.charset.StandardCharsets;

class Latin1ByteCharReplacer implements CharReplacer {

	final byte[] chars;

	public Latin1ByteCharReplacer(byte[] chars) {
		this.chars = chars;
	}

	public Latin1ByteCharReplacer(String chars) {
		this.chars = chars.getBytes(StandardCharsets.US_ASCII);
	}

	@Override
	public void setCharAt(int index, char ch) {
		if (ch >>> 8 == 0) { // see java.lang.StringLatin1#canEncode
			throw new IllegalArgumentException("Invalid char: " + ch);
		}
		chars[index] = (byte) ch;
	}

	@Override
	public void pickChars(long number, int start, int end) {
		while (end-- > start) {
			chars[end] = (byte) ('0' + (number % 10));
			number /= 10;
		}
	}

	@Override
	public void pickValidChars(long number, int start, int end) {
		while (number > 0 && end-- > start) {
			chars[end] = (byte) ('0' + (number % 10));
			number /= 10;
		}
	}

	@Override
	public String toString() {
		return JavaUtil.STRING_CREATOR_JDK11.apply(chars, JavaUtil.LATIN1);
	}

}