package me.codeplayer.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumberXTest {

	@Test
	public void getByte_NullValue_ReturnsDefaultValue() {
		byte defaultValue = 10;
		byte result = NumberX.getByte(null, defaultValue);
		assertEquals(defaultValue, result);
	}

	@Test
	public void getByte_NumberValue_ReturnsByteValue() {
		Number number = 123;
		byte result = NumberX.getByte(number);
		assertEquals(number.byteValue(), result);
	}

	@Test
	public void getByte_CharSequenceValue_ReturnsParsedByteValue() {
		String val = "123";
		byte result = NumberX.getByte(val);
		assertEquals(Byte.parseByte(val), result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getByte_InvalidValue_ThrowsException() {
		Object invalidValue = new Object();
		NumberX.getByte(invalidValue);
	}

	@Test
	public void getInt_NullValue_ReturnsDefaultValue() {
		int defaultValue = 100;
		int result = NumberX.getInt(null, defaultValue);
		assertEquals(defaultValue, result);
	}

	@Test
	public void getInt_NumberValue_ReturnsIntValue() {
		Number number = 12345;
		int result = NumberX.getInt(number);
		assertEquals(number.intValue(), result);
	}

	@Test
	public void getInt_CharSequenceValue_ReturnsParsedIntValue() {
		String val = "12345";
		int result = NumberX.getInt(val);
		assertEquals(Integer.parseInt(val), result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getInt_InvalidValue_ThrowsException() {
		Object invalidValue = new Object();
		NumberX.getInt(invalidValue);
	}

	@Test
	public void getBigDecimal_BigDecimalValue_ReturnsSameValue() {
		BigDecimal bigDecimal = new BigDecimal("123.45");
		BigDecimal result = NumberX.getBigDecimal(bigDecimal);
		assertEquals(bigDecimal, result);
	}

	@Test
	public void getBigDecimal_BigIntegerValue_ReturnsConvertedBigDecimal() {
		BigInteger bigInteger = new BigInteger("12345678901234567890");
		BigDecimal result = NumberX.getBigDecimal(bigInteger);
		assertEquals(new BigDecimal(bigInteger), result);
	}

	@Test
	public void getBigDecimal_IntegerValue_ReturnsConvertedBigDecimal() {
		int intValue = 12345;
		BigDecimal result = NumberX.getBigDecimal(intValue);
		assertEquals(BigDecimal.valueOf(intValue), result);
	}

	@Test
	public void getBigDecimal_CharSequenceValue_ReturnsParsedBigDecimal() {
		String val = "123.45";
		BigDecimal result = NumberX.getBigDecimal(val);
		assertEquals(new BigDecimal(val), result);
	}

	@Test
	public void isNumber_NullString_ReturnsFalse() {
		assertFalse(NumberX.isNumber(null));
	}

	@Test
	public void isNumber_EmptyString_ReturnsFalse() {
		assertFalse(NumberX.isNumber(""));
	}

	@Test
	public void isNumber_ValidNumberString_ReturnsTrue() {
		assertTrue(NumberX.isNumber("12345"));
	}

	@Test
	public void isNumber_InvalidNumberString_ReturnsFalse() {
		assertFalse(NumberX.isNumber("123abc"));
	}

	@Test
	public void isInt_NullValue_ReturnsFalse() {
		assertFalse(NumberX.isInt(null));
	}

	@Test
	public void isInt_IntegerValue_ReturnsTrue() {
		assertTrue(NumberX.isInt(12345));
	}

	@Test
	public void isInt_ValidNumberString_ReturnsTrue() {
		assertTrue(NumberX.isInt("12345"));
	}

	@Test
	public void isDouble_NullValue_ReturnsFalse() {
		assertFalse(NumberX.isDouble((Object) null));
	}

	@Test
	public void isDouble_NumberValue_ReturnsTrue() {
		assertTrue(NumberX.isDouble(123.45));
	}

	@Test
	public void isDouble_ValidDoubleString_ReturnsTrue() {
		assertTrue(NumberX.isDouble("123.45"));
	}

	@Test
	public void isDouble_InvalidString_ReturnsFalse() {
		assertFalse(NumberX.isDouble("123abc"));
	}

}