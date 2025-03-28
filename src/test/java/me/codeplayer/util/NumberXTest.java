package me.codeplayer.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NumberXTest {

	@Test
	public void getByte_NullValue_ReturnsDefaultValue() {
		Byte defaultValue = 10;
		assertEquals(defaultValue, NumberX.getByte(null, defaultValue));
	}

	@Test
	public void getByte_NumberValue_ReturnsByteValue() {
		//noinspection WrapperTypeMayBePrimitive
		Integer number = 123;
		assertEquals(number.byteValue(), NumberX.getByte(number));

		number = 300;
		assertEquals(44, NumberX.getByte(number));
	}

	@Test
	public void getByte_CharSequenceValue_ReturnsParsedByteValue() {
		String val = "123";
		byte result = NumberX.getByte(val);
		assertEquals(Byte.parseByte(val), result);
	}

	@Test
	public void getByte_InvalidValue_ThrowsException() {
		Object invalidValue = new Object();
		assertThrows(IllegalArgumentException.class, () -> NumberX.getByte(invalidValue));
	}

	@Test
	public void getByte_NumberValueWithDefaultValue_ReturnsByteValue() {
		Number number = 123;
		byte result = NumberX.getByte(number, (byte) 0);
		assertEquals(number.byteValue(), result);
	}

	@Test
	public void getByte_CharSequenceValueWithDefaultValue_ReturnsParsedByteValue() {
		String val = "123";
		byte result = NumberX.getByte(val, (byte) 0);
		assertEquals(Byte.parseByte(val), result);
	}

	@Test
	public void getByte_EmptyCharSequenceValue_ReturnsDefaultValue() {
		byte defaultValue = 10;
		byte result = NumberX.getByte("", defaultValue);
		assertEquals(defaultValue, result);
	}

	@Test
	public void getByte_InvalidValueWithDefaultValue_ThrowsException() {
		Object invalidValue = new Object();
		assertThrows(IllegalArgumentException.class, () -> NumberX.getByte(invalidValue, (byte) 0));
	}

	@Test
	public void getShort_NumberValue_ReturnsShortValue() {
		// 测试用例：value 是 Short
		Short shortValue = 123;
		assertEquals(shortValue.shortValue(), NumberX.getShort(shortValue));

		// 测试用例：value 是其他 Number 类型
		Integer intValue = 12345;
		assertEquals(intValue.shortValue(), NumberX.getShort(intValue));

		Long longValue = 1234567890L;
		assertEquals(longValue.shortValue(), NumberX.getShort(longValue));
	}

	@Test
	public void getShort_CharSequenceValue_ReturnsParsedShortValue() {
		String val = "12345";
		short result = NumberX.getShort(val);
		assertEquals(Short.parseShort(val), result);
	}

	@Test
	public void getShort_InvalidValue_ThrowsException() {
		Object invalidValue = new Object();
		assertThrows(IllegalArgumentException.class, () -> NumberX.getShort(invalidValue));
	}

	@Test
	public void getShort_NullValue_ReturnsDefaultValue() {
		Short defaultValue = 10;
		assertEquals(defaultValue, NumberX.getShort(null, defaultValue));
	}

	@Test
	public void getShort_EmptyCharSequenceValue_ReturnsDefaultValue() {
		Short defaultValue = 10;
		assertEquals(defaultValue, NumberX.getShort("", defaultValue));
	}

	@Test
	public void getShort_NonEmptyCharSequenceValue_ReturnsParsedShortValue() {
		String val = "12345";
		Short result = NumberX.getShort(val, (short) 0);
		assertEquals(Short.valueOf(val), result);
	}

	@Test
	public void getShort_InvalidValueWithDefaultValue_ThrowsException() {
		Object invalidValue = new Object();
		assertThrows(IllegalArgumentException.class, () -> NumberX.getShort(invalidValue, (short) 0));
	}

	@Test
	public void getInt_NullValue_ReturnsDefaultValue() {
		Integer _null = null;
		int defaultValue = 100;
		int result = NumberX.getInt(_null, defaultValue);
		assertEquals(defaultValue, result);

		Integer result2 = NumberX.getInteger(_null, _null);
		assertNull(result2);
	}

	@Test
	public void getInteger() {
		Integer number = 1234567;
		Integer result = NumberX.getInteger(number, 0);
		assertSame(number, result);
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

	@Test
	public void getInt_InvalidValue_ThrowsException() {
		Object invalidValue = new Object();
		assertThrows(IllegalArgumentException.class, () -> NumberX.getInt(invalidValue, 0));
	}

	@Test
	public void getInt_InvalidString_ThrowsException() {
		assertThrows(NumberFormatException.class, () -> NumberX.getInteger("abc", null));
	}

	@Test
	public void getLong_NumberValue_ReturnsLongValue() {
		Number number = 1234567890L;
		long result = NumberX.getLong(number);
		assertEquals(number.longValue(), result);
	}

	@Test
	public void getLong_CharSequenceValue_ReturnsParsedLongValue() {
		String val = "1234567890";
		long result = NumberX.getLong(val);
		assertEquals(Long.parseLong(val), result);
	}

	@Test
	public void getLong_InvalidCharSequenceValue_ThrowsException() {
		String invalidVal = "abc";
		assertThrows(NumberFormatException.class, () -> NumberX.getLong(invalidVal, 0));
	}

	@Test
	public void getLong_NullValue_ThrowsException() {
		assertThrows(NullPointerException.class, () -> NumberX.getLong(null));
	}

	@Test
	public void getLong_NullValueWithDefaultValue_ReturnsDefaultValue() {
		long defaultValue = 1000L;
		long result = NumberX.getLong(null, defaultValue);
		assertEquals(defaultValue, result);
	}

	@Test
	public void getLong_InvalidCharSequenceValueWithDefaultValue_ThrowException() {
		String invalidVal = "abc";
		long defaultValue = 1000L;
		assertThrows(NumberFormatException.class, () -> NumberX.getLong(invalidVal, defaultValue));
	}

	@Test
	public void getLong_NumberValueWithLongDefaultValue_ReturnsLongValue() {
		Long number = 1234567890L;
		Long defaultValue = 1000L;
		Long result = NumberX.getLong(number, defaultValue);
		assertSame(number, result);
	}

	@Test
	public void getLong_CharSequenceValueWithLongDefaultValue_ReturnsParsedLongValue() {
		String val = "1234567890";
		Long defaultValue = 1000L;
		Long result = NumberX.getLong(val, defaultValue);
		assertEquals(Long.parseLong(val), result.longValue());
	}

	@Test
	public void getLong_NullValueWithLongDefaultValue_ReturnsDefaultValue() {
		Long defaultValue = 1000L;
		Long result = NumberX.getLong(null, defaultValue);
		assertEquals(defaultValue, result);
	}

	@Test
	public void getLong_InvalidCharSequenceValueWithLongDefaultValue_ThrowException() {
		String invalidVal = "abc";
		Long defaultValue = 1000L;
		assertThrows(NumberFormatException.class, () -> NumberX.getLong(invalidVal, defaultValue));
	}

	@Test
	public void getFloat_NumberValue_ReturnsFloatValue() {
		Number number = 123.45f;
		float result = NumberX.getFloat(number);
		assertEquals(number.floatValue(), result, 0.001f);
	}

	@Test
	public void getFloat_CharSequenceValue_ReturnsParsedFloatValue() {
		String val = "123.45";
		float result = NumberX.getFloat(val);
		assertEquals(Float.parseFloat(val), result, 0.001f);
	}

	@Test
	public void getFloat_InvalidValue_ThrowsException() {
		Object invalidValue = "abc";
		assertThrows(NumberFormatException.class, () -> NumberX.getFloat(invalidValue));
	}

	@Test
	public void getFloat_NullValue_ReturnsDefaultValue() {
		float defaultValue = 10.0f;
		float result = NumberX.getFloat(null, defaultValue);
		assertEquals(defaultValue, result, 0.001f);
	}

	@Test
	public void getFloat_EmptyCharSequenceValue_ReturnsDefaultValue() {
		float defaultValue = 10.0f;
		float result = NumberX.getFloat("", defaultValue);
		assertEquals(defaultValue, result, 0.001f);
	}

	@Test
	public void getDouble_NumberValue_ReturnsDoubleValue() {
		Number number = 123.45;
		double result = NumberX.getDouble(number);
		assertEquals(number.doubleValue(), result, 0.001);
	}

	@Test
	public void getDouble_CharSequenceValue_ReturnsParsedDoubleValue() {
		String val = "123.45";
		double result = NumberX.getDouble(val);
		assertEquals(Double.parseDouble(val), result, 0.001);
	}

	@Test
	public void getDouble_InvalidValue_ThrowsException() {
		Object invalidValue = "abc";
		assertThrows(NumberFormatException.class, () -> NumberX.getDouble(invalidValue));
	}

	@Test
	public void getDouble_NullValue_ReturnsDefaultValue() {
		double defaultValue = 10.0;
		double result = NumberX.getDouble(null, defaultValue);
		assertEquals(defaultValue, result, 0.001);
	}

	@Test
	public void getDouble_EmptyCharSequenceValue_ReturnsDefaultValue() {
		double defaultValue = 10.0;
		double result = NumberX.getDouble("", defaultValue);
		assertEquals(defaultValue, result, 0.001);
	}

	@Test
	public void getBigDecimal_NumberValue_ReturnsBigDecimalValue() {
		Number number = 1234567890L;
		BigDecimal result = NumberX.getBigDecimal(number);
		assertEquals(new BigDecimal(number.toString()), result);
	}

	@Test
	public void getBigDecimal_CharSequenceValue_ReturnsParsedBigDecimalValue() {
		String val = "1234567890.12345";
		BigDecimal result = NumberX.getBigDecimal(val);
		assertEquals(new BigDecimal(val), result);
	}

	@Test
	public void getBigDecimal_InvalidValue_ThrowsException() {
		Object invalidValue = "abc";
		assertThrows(NumberFormatException.class, () -> NumberX.getBigDecimal(invalidValue, null));
	}

	@Test
	public void getBigDecimal_NullValue_ReturnsDefaultValue() {
		BigDecimal defaultValue = new BigDecimal("10.0");
		BigDecimal result = NumberX.getBigDecimal(null, defaultValue);
		assertEquals(defaultValue, result);
	}

	@Test
	public void getBigDecimal_EmptyCharSequenceValue_ReturnsDefaultValue() {
		BigDecimal defaultValue = new BigDecimal("10.0");
		BigDecimal result = NumberX.getBigDecimal("", defaultValue);
		assertEquals(defaultValue, result);

		result = NumberX.getBigDecimal("", 10);
		assertEquals(0, result.compareTo(BigDecimal.TEN));
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
	public void isNumber() {
		// true
		assertTrue(NumberX.isNumber("123"));
		assertTrue(NumberX.isNumber("12345"));
		assertTrue(NumberX.isNumber("00123"));
		assertTrue(NumberX.isNumber(123));
		assertTrue(NumberX.isNumber(-123));
		assertTrue(NumberX.isNumber(123.00));
		assertTrue(NumberX.isNumber(123.0F));
		assertTrue(NumberX.isNumber(BigInteger.TEN));
		assertTrue(NumberX.isNumber(new BigDecimal("123.00")));

		// false
		assertFalse(NumberX.isNumber(null));
		assertFalse(NumberX.isNumber(""));
		assertFalse(NumberX.isNumber("   "));
		assertFalse(NumberX.isNumber("-12345"));
		assertFalse(NumberX.isNumber("12.3"));
		assertFalse(NumberX.isNumber(" 123"));
		assertFalse(NumberX.isNumber("12a"));
		assertFalse(NumberX.isNumber("abc"));
		assertFalse(NumberX.isNumber("0xff"));
		assertFalse(NumberX.isNumber(123.1));

		// true
		assertTrue(NumberX.isNumber("123", 3));
		assertTrue(NumberX.isNumber("00123", 5));
		// false
		assertFalse(NumberX.isNumber(null, 3));
		assertFalse(NumberX.isNumber("123", 4));
		assertFalse(NumberX.isNumber("-123", 3));
		assertFalse(NumberX.isNumber("-123", 4));
		assertFalse(NumberX.isNumber("123.0", 3));
		assertFalse(NumberX.isNumber("123.0", 4));
		assertFalse(NumberX.isNumber(" 123", 3));
		assertFalse(NumberX.isNumber("12a", 3));
	}

	@Test
	public void isNumber_NegativeLength_ThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> NumberX.isNumber("123", -1));
	}

	@Test
	public void isNumeric() {
		// true
		assertTrue(NumberX.isNumeric("0"));
		assertTrue(NumberX.isNumeric("001"));
		assertTrue(NumberX.isNumeric("12345"));
		// false
		assertFalse(NumberX.isNumeric(null));
		assertFalse(NumberX.isNumeric(""));
		assertFalse(NumberX.isNumeric("  "));
		assertFalse(NumberX.isNumeric("1.23"));
		assertFalse(NumberX.isNumeric("0.0"));
		assertFalse(NumberX.isNumeric("-12345"));
		assertFalse(NumberX.isNumeric(" 123"));
		assertFalse(NumberX.isNumeric(" 0xff"));
		// true
		assertTrue(NumberX.isNumeric("12345", 0, 5));
		assertTrue(NumberX.isNumeric("abc123def", 3, 6));
		// false
		assertFalse(NumberX.isNumeric(null, 0, 1));
		assertFalse(NumberX.isNumeric("", 0, 1));
		assertFalse(NumberX.isNumeric("123", 1, 1));
		assertFalse(NumberX.isNumeric("123", 2, 1));
		assertFalse(NumberX.isNumeric("123abc", 0, 6));
		assertFalse(NumberX.isNumeric("123abc456", 0, 9));
		assertFalse(NumberX.isNumeric("abc123", 0, 6));
		assertFalse(NumberX.isNumeric("123abc", 0, 6));
	}

	@Test
	public void isDecimal() {
		assertTrue(NumberX.isDecimal(123.45));
		assertTrue(NumberX.isDecimal(123));
		assertTrue(NumberX.isDecimal(1235678L));
		assertTrue(NumberX.isDecimal(123.45F));
		assertTrue(NumberX.isDecimal("123.45"));
		assertTrue(NumberX.isDecimal("0"));
		assertTrue(NumberX.isDecimal("0.0"));
		assertTrue(NumberX.isDecimal(BigDecimal.valueOf(456.78)));
		assertTrue(NumberX.isDecimal(-123));
		assertTrue(NumberX.isDecimal(-123.45));
		assertTrue(NumberX.isDecimal(new StringBuilder("123.45")));
		assertTrue(NumberX.isDecimal(StringX.getBuilder(0, "123", ".", "45")));
		assertTrue(NumberX.isDecimal("-123", true));
		assertTrue(NumberX.isDecimal("-123.45", true));

		assertFalse(NumberX.isDecimal("-123"));
		assertFalse(NumberX.isDecimal("-123.45"));
		assertFalse(NumberX.isDecimal(""));
		assertFalse(NumberX.isDecimal(" 123"));
		assertFalse(NumberX.isDecimal("123 "));
		assertFalse(NumberX.isDecimal("12E3"));
		assertFalse(NumberX.isDecimal("-12E3"));
		assertFalse(NumberX.isDecimal((Object) null));
		assertFalse(NumberX.isDecimal("123abc"));
	}

	@Test
	public void formatNormalDateTime() {
		StringBuilder chars = new StringBuilder("yyyy-MM-dd HH:mm:ss");
		NumberX.pickChars(2023, chars, 0, 4);
		NumberX.pickChars(10, chars, 5, 7);
		NumberX.pickChars(5, chars, 8, 10);
		NumberX.pickChars(14, chars, 11, 13);
		NumberX.pickChars(30, chars, 14, 16);
		NumberX.pickChars(45, chars, 17, 19);
		assertEquals("2023-10-05 14:30:45", chars.toString());
	}

}