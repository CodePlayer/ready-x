package me.codeplayer.util;

import java.nio.charset.StandardCharsets;

class Latin1ByteNumBuffer implements NumBuffer {

	final byte[] chars;

	public Latin1ByteNumBuffer(byte[] chars) {
		this.chars = chars;
	}

	public Latin1ByteNumBuffer(String chars) {
		this.chars = chars.getBytes(StandardCharsets.US_ASCII);
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
		return JavaX.STRING_CREATOR_JDK11.apply(chars, JavaX.LATIN1);
	}

}