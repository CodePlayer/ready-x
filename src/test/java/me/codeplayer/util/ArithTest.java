package me.codeplayer.util;

import java.math.*;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArithTest implements WithAssertions {

	static final double delta = 0.00000000000001;

	@Test
	public void round() {
		assertThat(Arith.round(12.65656465, 3)).isEqualTo(12.657);

		assertThat(Arith.round(12.45123, 2)).isEqualTo(12.45);

		assertThat(Arith.round(-123.641, 0)).isEqualTo(-124);

		assertThat(new Arith(123.456).round(2).toBigDecimal()).isEqualByComparingTo(new BigDecimal("123.46"));

		assertThat(new Arith(123.456).round(0).toBigDecimal()).isEqualByComparingTo(BigDecimal.valueOf(123));

		assertThat(new Arith(123.456).round(-1).toBigDecimal()).isEqualByComparingTo(BigDecimal.valueOf(120));
	}

	@Test
	public void ceil() {
		assertThat(Arith.ceilToLong(12.65656465)).isEqualTo(13);
		assertThat(Arith.ceilToLong(154.125656465)).isEqualTo(155);
		assertThat(Arith.ceilToLong(1442.65656465)).isEqualTo(1443);
		assertThat(Arith.ceilToLong(-1442.65656465)).isEqualTo(-1442);
		assertThat(Arith.ceilToLong(123)).isEqualTo(123);

		assertThat(new Arith(12.345).ceil(2).toBigDecimal()).isEqualByComparingTo(new BigDecimal("12.35"));
		assertThat(new Arith(-12.345).ceil(2).toBigDecimal()).isEqualByComparingTo(new BigDecimal("-12.34"));
		assertThat(new Arith(0).ceil(2).toBigDecimal()).isEqualByComparingTo(BigDecimal.ZERO);
		assertThat(new Arith(12.345).ceil().toBigDecimal()).isEqualByComparingTo(BigDecimal.valueOf(13));
		assertThat(new Arith(-12.345).ceil().toBigDecimal()).isEqualByComparingTo(BigDecimal.valueOf(-12));
	}

	@Test
	public void floor() {
		assertThat(Arith.floorToLong(12.65656465)).isEqualTo(12);
		assertThat(Arith.floorToLong(154.125656465)).isEqualTo(154);
		assertThat(Arith.floorToLong(1442.65656465)).isEqualTo(1442);
		assertThat(Arith.floorToLong(-1442.65656465)).isEqualTo(-1443);
		assertThat(Arith.floorToLong(123)).isEqualTo(123);

		assertThat(new Arith(12.345).floor(2).toBigDecimal()).isEqualByComparingTo(new BigDecimal("12.34"));
		assertThat(new Arith(-12.345).floor(2).toBigDecimal()).isEqualByComparingTo(new BigDecimal("-12.35"));
		assertThat(new Arith(0).floor(2).toBigDecimal()).isEqualByComparingTo(BigDecimal.ZERO);
		assertThat(new Arith(12.345).floor().toBigDecimal()).isEqualByComparingTo(BigDecimal.valueOf(12));
		assertThat(new Arith(-12.345).floor().toBigDecimal()).isEqualByComparingTo(BigDecimal.valueOf(-13));
	}

	@Test
	public void arith() {
		assertEquals(0, new Arith().value().compareTo(BigDecimal.ZERO));

		final Arith arith = new Arith(BigInteger.TEN);
		arith.add(0);
		assertEquals(0, arith.toBigInteger().compareTo(BigInteger.TEN));
		assertEquals(0, arith.add("90").value().compareTo(Arith.HUNDRED));

		assertEquals(0, new Arith(123).add(12).add(15.12).toBigDecimal().compareTo(new BigDecimal("150.12")));

		assertEquals(0, new Arith("12.4").minus(12).minus(0.4).toBigDecimal().compareTo(BigDecimal.ZERO));

		assertEquals(0, new Arith(BigDecimal.TEN).minus("9.9").multiply(10).divide("1").toBigDecimal().compareTo(BigDecimal.ONE));

		assertEquals(0, new Arith(true).add(10.3).multiply("10").divide("11.3").toBigDecimal().compareTo(BigDecimal.TEN));

		assertThat(new Arith(false).add(212.454).add(1245.23).multiply(12.45).value).isEqualByComparingTo(BigDecimal.valueOf(18148.1658));
	}

	@Test
	public void divide_ZeroDivisor_ThrowsArithmeticException() {
		assertThrows(ArithmeticException.class, () -> new Arith(10).divide(BigDecimal.ZERO, 2, RoundingMode.HALF_UP));
	}

	@Test
	public void divide0_ZeroDivisor_ThrowsArithmeticException() {
		assertThrows(ArithmeticException.class, () -> Arith.divide(10.0, 0.0, 2));
	}

	static final BigDecimal d3_33 = new BigDecimal("3.33"), d3 = BigDecimal.valueOf(3);

	@Test
	public void divide_DifferentRoundingModes_ProducesExpectedResults() {
		BigDecimal result = new Arith(10).divide(d3, 2, RoundingMode.UP).toBigDecimal();
		assertEquals(new BigDecimal("3.34"), result);

		result = new Arith(10).divide(d3, 2, RoundingMode.DOWN).toBigDecimal();

		assertEquals(d3_33, result);

		result = new Arith(10).divide(d3, 2, RoundingMode.HALF_UP).toBigDecimal();
		assertEquals(d3_33, result);
	}

	@Test
	public void divide_DifferentScales_ProducesExpectedResults() {
		BigDecimal result = new Arith(10).divide(d3, 0, RoundingMode.HALF_UP).toBigDecimal();
		assertEquals(d3, result);

		result = new Arith(10).divide(d3, 1, RoundingMode.HALF_UP).toBigDecimal();
		assertEquals(new BigDecimal("3.3"), result);

		result = new Arith(10).divide(d3, 2, RoundingMode.HALF_UP).toBigDecimal();
		assertEquals(d3_33, result);
	}

	@Test
	public void divide_DifferentInputTypes_ProducesExpectedResults() {
		BigDecimal result = new Arith(10).divide("3", 2, RoundingMode.HALF_UP).toBigDecimal();
		assertEquals(d3_33, result);

		result = new Arith(10).divide(3.0, 2, RoundingMode.HALF_UP).toBigDecimal();
		assertEquals(d3_33, result);

		result = new Arith(10).divide(3L, 2, RoundingMode.HALF_UP).toBigDecimal();
		assertEquals(d3_33, result);
	}

	@Test
	public void divideRound_ZeroDivisor_ThrowsArithmeticException() {
		assertThrows(ArithmeticException.class, () -> new Arith(10).divideRound(BigDecimal.ZERO, 2));
	}

	@Test
	public void divideRound_DifferentScales_ProducesExpectedResults() {
		BigDecimal result = new Arith(10).divideRound(d3, 0).toBigDecimal();
		assertEquals(d3, result);

		result = new Arith(10).divideRound(d3, 1).toBigDecimal();
		assertEquals(new BigDecimal("3.3"), result);

		result = new Arith(10).divideRound(d3, 2).toBigDecimal();
		assertEquals(d3_33, result);
	}

	@Test
	public void divideRound_StringInput_ProducesExpectedResults() {
		BigDecimal result = new Arith(10).divideRound("3", 2).toBigDecimal();
		assertEquals(d3_33, result);
	}

	@Test
	public void divideRound_DoubleInput_ProducesExpectedResults() {
		BigDecimal result = new Arith(10).divideRound(3.0, 2).toBigDecimal();
		assertEquals(d3_33, result);
	}

	@Test
	public void divideRound_LongInput_ProducesExpectedResults() {
		BigDecimal result = new Arith(10).divideRound(3L, 2).toBigDecimal();
		assertEquals(d3_33, result);
	}

	@Test
	public void setScale_ValidScaleAndRoundingMode_SetsCorrectScale() {
		Arith arith = new Arith(123.456);
		arith.setScale(2, RoundingMode.HALF_UP);
		assertThat(arith.toBigDecimal()).isEqualByComparingTo(new BigDecimal("123.46"));
	}

	@Test
	public void setScale_ZeroScale_SetsCorrectScale() {
		Arith arith = new Arith(123.456);
		arith.setScale(0, RoundingMode.HALF_UP);
		assertThat(arith.toBigDecimal()).isEqualByComparingTo(BigDecimal.valueOf(123));
	}

	@Test
	public void setScale_NegativeScale_SetsCorrectScale() {
		Arith arith = new Arith(123.456).setScale(-1, RoundingMode.HALF_UP);
		assertThat(arith.toBigDecimal()).isEqualByComparingTo(BigDecimal.valueOf(120));
	}

	@Test
	public void setScale_InvalidScale_ThrowsArithmeticException() {
		Arith arith = new Arith(123.456);
		assertThrows(ArithmeticException.class, () -> arith.setScale(Integer.MAX_VALUE, RoundingMode.HALF_UP));
	}

	@Test
	public void round_InvalidScale_ThrowsArithmeticException() {
		Arith arith = new Arith(123.456);
		assertThrows(ArithmeticException.class, () -> arith.round(Integer.MAX_VALUE));
	}

	@Test
	public void compareTo_BigDecimalSameReference_ReturnsZero() {
		BigDecimal a = BigDecimal.valueOf(10);
		int result = Arith.compareTo(a, a);
		assertEquals(0, result);
	}

	@Test
	public void compareTo_BigDecimalDifferentValues_ReturnsCorrectResult() {
		BigDecimal a = BigDecimal.valueOf(10);
		BigDecimal b = BigDecimal.valueOf(20);
		int result = Arith.compareTo(a, b);
		assertEquals(-1, result);

		result = Arith.compareTo(b, a);
		assertEquals(1, result);

		result = Arith.compareTo(a, a);
		assertEquals(0, result);
	}

	@Test
	public void compareTo_BigDecimalAndDouble_ReturnsCorrectResult() {
		BigDecimal a = BigDecimal.valueOf(10);
		double b = 20.0;
		int result = Arith.compareTo(a, b);
		assertEquals(-1, result);

		b = 5.0;
		result = Arith.compareTo(a, b);
		assertEquals(1, result);

		b = 10.0;
		result = Arith.compareTo(a, b);
		assertEquals(0, result);
	}

	@Test
	public void compareTo_BigDecimalAndLong_ReturnsCorrectResult() {
		BigDecimal a = BigDecimal.valueOf(10);
		long b = 20;
		int result = Arith.compareTo(a, b);
		assertEquals(-1, result);

		b = 5;
		result = Arith.compareTo(a, b);
		assertEquals(1, result);

		b = 10;
		result = Arith.compareTo(a, b);
		assertEquals(0, result);
	}

	@Test
	public void compareTo_BigDecimalAndString_ReturnsCorrectResult() {
		BigDecimal a = BigDecimal.valueOf(10);
		String b = "20";
		int result = Arith.compareTo(a, b);
		assertEquals(-1, result);

		b = "5";
		result = Arith.compareTo(a, b);
		assertEquals(1, result);

		b = "10";
		result = Arith.compareTo(a, b);
		assertEquals(0, result);
	}

	@Test
	public void roundFast_NormalCase_CorrectRounding() {
		assertThat(Arith.roundFast(12.65656465, 3)).isEqualTo(12.657);
		assertThat(Arith.roundFast(12.45123, 2)).isEqualTo(12.45);
		assertThat(Arith.roundFast(-123.641, 0)).isEqualTo(-124);

		assertThat(Arith.roundFast(12.65656465, 10)).isEqualTo(12.65656465);

		assertThat(Arith.roundFast(12.65656465, 11)).isEqualTo(Arith.round(12.65656465, 11));

		assertThat(Arith.roundFast(-12.65656465, 3)).isEqualTo(-12.657);
		assertThat(Arith.roundFast(-12.45123, 2)).isEqualTo(-12.45);
		assertThat(Arith.roundFast(-123.641, 0)).isEqualTo(-124);

		assertThat(Arith.roundFast(0.0, 3)).isEqualTo(0.0);
	}

	@Test
	public void doubleValue_ShouldReturnCorrectDoubleValue() {
		Arith arith = new Arith(123.456);
		assertEquals(123.456, arith.doubleValue(), delta);
	}

	@Test
	public void doubleValueWithScale_ShouldReturnRoundedDoubleValue() {
		Arith arith = new Arith(123.456);
		assertEquals(123.46, arith.doubleValue(2), delta);
	}

	@Test
	public void intValue_ShouldReturnCorrectIntValue() {
		Arith arith = new Arith(123.456);
		assertEquals(123, arith.intValue());
	}

	@Test
	public void longValue_ShouldReturnCorrectLongValue() {
		Arith arith = new Arith(123456789.123);
		assertEquals(123456789L, arith.longValue());
	}

	@Test
	public void floatValue_ShouldReturnCorrectFloatValue() {
		Arith arith = new Arith(123.456);
		assertEquals(123.456f, arith.floatValue(), 0.001f);
	}

	@Test
	public void byteValue_ShouldReturnCorrectByteValue() {
		Arith arith = new Arith(123);
		assertEquals((byte) 123, arith.byteValue());
	}

	@Test
	public void shortValue_ShouldReturnCorrectShortValue() {
		Arith arith = new Arith(12345);
		assertEquals((short) 12345, arith.shortValue());
	}

	@Test
	public void doubleValueWithScale_ShouldHandleNegativeValues() {
		Arith arith = new Arith(-123.456);
		assertEquals(-123.46, arith.doubleValue(2), delta);
	}

	@Test
	public void intValue_ShouldHandleNegativeValues() {
		Arith arith = new Arith(-123.456);
		assertEquals(-123, arith.intValue());
	}

	@Test
	public void longValue_ShouldHandleNegativeValues() {
		Arith arith = new Arith(-123456789.123);
		assertEquals(-123456789L, arith.longValue());
	}

	@Test
	public void floatValue_ShouldHandleNegativeValues() {
		Arith arith = new Arith(-123.456);
		assertEquals(-123.456f, arith.floatValue(), 0.001f);
	}

	@Test
	public void byteValue_ShouldHandleNegativeValues() {
		Arith arith = new Arith(-123);
		assertEquals((byte) -123, arith.byteValue());
	}

	@Test
	public void shortValue_ShouldHandleNegativeValues() {
		Arith arith = new Arith(-12345);
		assertEquals((short) -12345, arith.shortValue());
	}

	@Test
	public void doubleValue_ShouldHandleZero() {
		Arith arith = new Arith(0);
		assertEquals(0.0, arith.doubleValue(), delta);
	}

	@Test
	public void intValue_ShouldHandleZero() {
		Arith arith = new Arith(0);
		assertEquals(0, arith.intValue());
	}

	@Test
	public void longValue_ShouldHandleZero() {
		Arith arith = new Arith(0);
		assertEquals(0L, arith.longValue());
	}

	@Test
	public void floatValue_ShouldHandleZero() {
		Arith arith = new Arith(0);
		assertEquals(0.0f, arith.floatValue(), 0.00001f);
	}

	@Test
	public void byteValue_ShouldHandleZero() {
		Arith arith = new Arith(0);
		assertEquals((byte) 0, arith.byteValue());
	}

	@Test
	public void shortValue_ShouldHandleZero() {
		Arith arith = new Arith(0);
		assertEquals((short) 0, arith.shortValue());
	}

	@Test
	public void add_PositiveNumbers_ReturnsCorrectSum() {
		double result = Arith.add(12.65656465, 3.34343535);
		assertEquals(16.0, result, delta);
	}

	@Test
	public void add_NegativeNumbers_ReturnsCorrectSum() {
		double result = Arith.add(-12.65656465, -3.34343535);
		assertEquals(-16.0, result, delta);
	}

	@Test
	public void add_MixedNumbers_ReturnsCorrectSum() {
		double result = Arith.add(12.65656465, -3.34343535);
		assertEquals(9.3131293, result, delta);
	}

	@Test
	public void add_ZeroValue_ReturnsCorrectSum() {
		double result = Arith.add(0.0, 12.65656465);
		assertEquals(12.65656465, result, delta);

		result = Arith.add(12.65656465, 0.0);
		assertEquals(12.65656465, result, delta);

		result = Arith.add(0.0, 0.0);
		assertEquals(0.0, result, delta);
	}

	@Test
	public void minus() {
		assertThat(Arith.minus(10.0, 5.0)).isEqualTo(5.0);
		assertThat(Arith.minus(-10.0, -5.0)).isEqualTo(-5.0);
		assertThat(Arith.minus(0.0, 5.0)).isEqualTo(-5.0);
		assertThat(Arith.multiply(10.0, 5.0)).isEqualTo(50.0);
	}

	@Test
	public void multiply_NegativeNumbers_CorrectResult() {
		assertThat(Arith.multiply(-10.0, -5.0)).isEqualTo(50.0);

		// -39.7617704786898225
		// -39.76
		double result = Arith.multiply(-12.65656465, 3.14159265, 2);
		assertEquals(-39.76, result, delta);
	}

	@Test
	public void multiply_ZeroAndPositive_CorrectResult() {
		assertThat(Arith.multiply(0.0, 5.0)).isEqualTo(0.0);
	}

	@Test
	public void multiply_WithScaleAndRoundingMode_CorrectResult() {
		assertThat(Arith.multiply(10.0, 3.33333, 2, RoundingMode.HALF_UP)).isEqualTo(33.33);
	}

	@Test
	public void multiply_WithScale_CorrectResult() {
		assertThat(Arith.multiply(10.0, 3.33333, 2)).isEqualTo(33.33);
	}

	@Test
	public void multiply_WithPrecision_CorrectResult() {
		assertThat(Arith.multiply(10.0, 3.33333, 4)).isEqualTo(33.3333);
	}

	@Test
	public void divide_PositiveNumbers_CorrectResult() {
		assertThat(Arith.divide(10.0, 2.0, 2)).isEqualTo(5.0);
	}

	@Test
	public void multiply_PositiveNumbersWithPositivePrecision_CorrectResult() {
		// 39.7617704786898225 => 39.762
		double result = Arith.multiply(12.65656465, 3.14159265, 3);
		assertEquals(39.762, result, delta);
	}

	@Test
	public void multiply_PositiveNumbersWithZeroPrecision_CorrectResult() {
		double result = Arith.multiply(12.65656465, 3.14159265, 0);
		assertEquals(40, result, delta);
	}

	@Test
	public void multiply_PositiveNumbersWithNegativePrecision_CorrectResult() {
		double result = Arith.multiply(12.65656465, 3.14159265, -1);
		assertEquals(40, result, delta);
	}

	@Test
	public void multiply_ZeroProduct_CorrectResult() {
		double result = Arith.multiply(0, 3.14159265, 2);
		assertEquals(0, result, delta);
	}

	@Test
	public void multiply_NegativePrecision_CorrectResult() {
		// 123.456789 => 100
		double result = Arith.multiply(12345.6789, 0.01, -2);
		assertEquals(100, result, delta);
	}

	@Test
	public void multiplyInContext_NormalCase_ShouldReturnCorrectResult() {
		// 83.810205 => 83.8
		double result = Arith.multiplyInContext(12.345, 6.789, 3);
		assertEquals(83.8, result, delta);
	}

	@Test
	public void multiplyInContext_ZeroPrecision_ShouldReturnIntegerResult() {
		double result = Arith.multiplyInContext(12.345, 6.789, 0);
		assertEquals(83.810205, result, delta);
	}

	@Test
	public void multiplyInContext_HighPrecision_ShouldReturnExactResult() {
		double result = Arith.multiplyInContext(12.345, 6.789, 10);
		assertEquals(83.810205, result, delta);
	}

	@Test
	public void multiplyInContext_LowPrecision_ShouldRoundCorrectly() {
		double result = Arith.multiplyInContext(12.345, 6.789, 1);
		assertEquals(80.0, result, delta);
	}

	@Test
	public void multiplyInContext_NegativeResult_ShouldHandleCorrectly() {
		// -83.810205 => -83.8
		double result = Arith.multiplyInContext(-12.345, 6.789, 3);
		assertEquals(-83.8, result, delta);
	}

	@Test
	public void multiplyInContext_ZeroProduct_ShouldReturnZero() {
		double result = Arith.multiplyInContext(0.0, 6.789, 3);
		assertEquals(0.0, result, delta);
	}

	@Test
	public void even_PositiveNumber_RoundsCorrectly() {
		assertThat(Arith.even(12.65656465, 3)).isEqualTo(12.657);
		assertThat(Arith.even(12.45123, 2)).isEqualTo(12.45);
	}

	@Test
	public void even_NegativeNumber_RoundsCorrectly() {
		assertThat(Arith.even(-123.641, 0)).isEqualTo(-124);
	}

	@Test
	public void even_Zero_RoundsCorrectly() {
		assertThat(Arith.even(0, 2)).isEqualTo(0);
	}

	@Test
	public void scale() {
		assertThat(Arith.scale(10.55, 1, RoundingMode.UP)).isEqualTo(10.6);
		assertThat(Arith.scale(10.55, 1, RoundingMode.DOWN)).isEqualTo(10.5);
		assertThat(Arith.scale(10.55, 1, RoundingMode.HALF_UP)).isEqualTo(10.6);
		assertThat(Arith.scale(10.55, 1, RoundingMode.HALF_DOWN)).isEqualTo(10.5);
		assertThat(Arith.scale(10.55, 1, RoundingMode.HALF_EVEN)).isEqualTo(10.6);

		assertThat(Arith.scale(-12.345, 2, RoundingMode.HALF_UP)).isEqualTo(-12.35);
		assertThat(Arith.scale(-12.345, 2, RoundingMode.HALF_DOWN)).isEqualTo(-12.34);

		assertThat(Arith.scale(0, 2, RoundingMode.HALF_UP)).isEqualTo(0);
	}

	@Test
	public void fastScale_SmallScale_ReturnsSameValue() {
		assertThat(Arith.fastScale(12.345, 3, RoundingMode.HALF_UP)).isEqualByComparingTo(new BigDecimal("12.345"));
	}

	@Test
	public void fastScale_LargerScale_ScalesCorrectly() {
		assertThat(Arith.fastScale(12.345, 2, RoundingMode.HALF_UP)).isEqualByComparingTo(new BigDecimal("12.35"));
	}

	@Test
	public void fastScale_Zero_RoundsCorrectly() {
		assertThat(Arith.fastScale(0, 2, RoundingMode.HALF_UP)).isEqualByComparingTo(BigDecimal.ZERO);
	}

	@Test
	public void isIntegral() {
		assertFalse(Arith.isIntegral(null));
		assertTrue(Arith.isIntegral(BigDecimal.valueOf(100)));
		assertTrue(Arith.isIntegral(new BigDecimal("100.00")));
		assertFalse(Arith.isIntegral(new BigDecimal("100.5")));
		assertFalse(Arith.isIntegral(new BigDecimal("100.001")));
	}

	@Test
	public void canDivideExactly_NullDivisor_ThrowsException() {
		assertThrows(NullPointerException.class, () -> Arith.canDivideExactly(BigDecimal.TEN, null));
	}

	@Test
	public void canDivideExactly() {
		assertFalse(Arith.canDivideExactly(BigDecimal.TEN, BigDecimal.ZERO));
		assertTrue(Arith.canDivideExactly(new BigDecimal("100"), BigDecimal.valueOf(10)));
		assertFalse(Arith.canDivideExactly(new BigDecimal("100"), BigDecimal.valueOf(3)));
	}

	@Test
	public void toString_CorrectStringRepresentation() {
		assertEquals("100", new Arith(100).toString());
	}

	@Test
	public void toRawString_CorrectStringRepresentation() {
		assertEquals("100", new Arith(100).toRawString());
	}

	@Test
	public void toStringWithScale_CorrectRoundedStringRepresentation() {
		assertEquals("100.00", new Arith(100).toString(2));
	}

	@Test
	public void valueOf_BigDecimalInput_ReturnsArith() {
		assertEquals(BigDecimal.valueOf(100), Arith.valueOf(BigDecimal.valueOf(100)).value());
	}

	@Test
	public void valueOf_BigIntegerInput_ReturnsArith() {
		assertEquals(BigDecimal.valueOf(100), Arith.valueOf(BigInteger.valueOf(100)).value());
	}

	@Test
	public void valueOf_NumberInput_ReturnsArith() {
		assertEquals(BigDecimal.valueOf(100), Arith.valueOf(100).value());
	}

	@Test
	public void valueOf_BooleanInput_ReturnsArith() {
		assertEquals(BigDecimal.ONE, Arith.valueOf(true).value());
	}

	@Test
	public void valueOf_StringInput_ReturnsArith() {
		assertEquals(BigDecimal.valueOf(100), Arith.valueOf("100").value());
	}

	@Test
	public void valueOfOrZero_NullInput_ReturnsZeroArith() {
		assertEquals(BigDecimal.ZERO, Arith.valueOfOrZero(null).value());
	}

	@Test
	public void valueOfOrZero_NonNullInput_ReturnsArith() {
		assertEquals(BigDecimal.valueOf(100), Arith.valueOfOrZero("100").value());
	}

	@Test
	public void toBigDecimal() {
		assertEquals(new BigDecimal("100.5"), Arith.toBigDecimal(100.5));
		assertSame(BigDecimal.ZERO, Arith.toBigDecimal(0.0));
		assertSame(BigDecimal.ONE, Arith.toBigDecimal(1.0));
		assertSame(BigDecimal.TEN, Arith.toBigDecimal(10.0));
		assertSame(Arith.HUNDRED, Arith.toBigDecimal(100));
		assertSame(Arith.HUNDRED, Arith.toBigDecimal(100.0));

		assertSame(Arith.THOUSAND, Arith.toBigDecimal(1000.0));
		assertSame(Arith.MYRIAD, Arith.toBigDecimal(10000.0));
		assertSame(Arith.HANDRED_MILLION, Arith.toBigDecimal(10000_0000.0));
	}

}