package me.codeplayer.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({ "ConstantValue", "BoxingBoxedValue" })
public class CmpTest {

	static final int _null = 3;
	static final Integer[] integers = { 0, 1, 2, null };
	static final Long[] longs = { 0L, 1L, 2L, null };
	static final Float[] floats = { 0F, 1F, 2F, null };
	static final Double[] doubles = { 0D, 1D, 2D, null };
	static final BigInteger[] bigIntegers = { BigInteger.ZERO, BigInteger.ONE, BigInteger.valueOf(2), null };
	static final BigDecimal[] bigDecimals = { BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.valueOf(2), null };

	@Test
	public void eqVal_Integer() {
		Integer NULL = integers[_null], _0 = 0, _1 = 1, _2 = 2;
		assertTrue(Cmp.eqVal(_1, _1)); // equals
		assertTrue(Cmp.eqVal(_1, new Integer(_1))); // equals but !=
		assertFalse(Cmp.eqVal(_0, _2)); // not equals
		assertFalse(Cmp.eqVal(NULL, _1)); // argument 1 is null
		assertFalse(Cmp.eqVal(_1, NULL)); // argument 2 is null
		assertFalse(Cmp.eqVal(NULL, NULL)); // both are null
	}

	@Test
	public void eqVal_Long() {
		Long NULL = longs[_null], _0 = 0L, _1 = 1L, _2 = 2L;
		assertTrue(Cmp.eqVal(_1, _1)); // equals
		assertTrue(Cmp.eqVal(_1, new Long(_1))); // equals but !=
		assertFalse(Cmp.eqVal(_0, _2)); // not equals
		assertFalse(Cmp.eqVal(NULL, _1)); // argument 1 is null
		assertFalse(Cmp.eqVal(_1, NULL)); // argument 2 is null
		assertFalse(Cmp.eqVal(NULL, NULL)); // both are null
	}

	@Test
	public void eq_Integer() {
		assertTrue(Cmp.eq(integers[1], 1)); // equals
		assertFalse(Cmp.eq(integers[_null], 1)); // argument 1 is null
		assertFalse(Cmp.eq(integers[1], 2)); // not equals
	}

	@Test
	public void eq_Long() {
		assertTrue(Cmp.eq(longs[1], 1L)); // equals
		assertFalse(Cmp.eq(longs[_null], 1L)); // argument 1 is null
		assertFalse(Cmp.eq(longs[1], 2L)); // not equals
	}

	@Test
	public void eq_Object() {
		assertTrue(Cmp.eq("test", "test"));
		assertFalse(Cmp.eq("test", "other"));
		assertFalse(Cmp.eq("test", "other"));
		assertFalse(Cmp.eq(1L, "test"));
		assertFalse(Cmp.eq("", null));
		assertFalse(Cmp.eq(null, ""));
		assertFalse(Cmp.eq(null, null));
		assertFalse(Cmp.eq("abc", null));
		assertFalse(Cmp.eq("A", "a"));
		assertFalse(Cmp.eq(0, Boolean.FALSE));
	}

	@Test
	public void zeroToNull() {
		// null
		int i = _null;
		assertNull(Cmp.zeroToNull(integers[i]));
		assertNull(Cmp.zeroToNull(longs[i]));
		assertNull(Cmp.zeroToNull(floats[i]));
		assertNull(Cmp.zeroToNull(doubles[i]));
		assertNull(Cmp.zeroToNull(bigIntegers[i]));
		assertNull(Cmp.zeroToNull(bigDecimals[i]));
		// zero
		i = 0;
		assertNull(Cmp.zeroToNull(integers[i]));
		assertNull(Cmp.zeroToNull(longs[i]));
		assertNull(Cmp.zeroToNull(floats[i]));
		assertNull(Cmp.zeroToNull(doubles[i]));
		assertNull(Cmp.zeroToNull(bigIntegers[i]));
		assertNull(Cmp.zeroToNull(bigDecimals[i]));
		// non zero
		i = 1;
		assertSame(integers[i], Cmp.zeroToNull(integers[i]));
		assertSame(longs[i], Cmp.zeroToNull(longs[i]));
		assertSame(floats[i], Cmp.zeroToNull(floats[i]));
		assertSame(doubles[i], Cmp.zeroToNull(doubles[i]));
		assertSame(bigIntegers[i], Cmp.zeroToNull(bigIntegers[i]));
		assertSame(bigDecimals[i], Cmp.zeroToNull(bigDecimals[i]));
		// special
		BigInteger largeValue = new BigInteger("12345678901234567890");
		assertSame(largeValue, Cmp.zeroToNull(largeValue));
		assertNull(Cmp.zeroToNull(BigDecimal.valueOf(0.0F)));
		assertNull(Cmp.zeroToNull(BigDecimal.valueOf(0.0D)));
		assertNull(Cmp.zeroToNull(new BigDecimal("0.00")));
		assertNotNull(Cmp.zeroToNull(new BigDecimal("0.0000000000000000001")));
		assertNotNull(Cmp.zeroToNull(BigDecimal.valueOf(-1)));

	}

	@Test
	public void nullToZero() {
		// null
		int i = _null, expected = 0;
		assertEquals(integers[expected], Cmp.nullToZero(integers[i]));
		assertEquals(longs[expected], Cmp.nullToZero(longs[i]));
		assertEquals(floats[expected], Cmp.nullToZero(floats[i]));
		assertEquals(doubles[expected], Cmp.nullToZero(doubles[i]));
		assertEquals(bigIntegers[expected], Cmp.nullToZero(bigIntegers[i]));
		assertEquals(bigDecimals[expected], Cmp.nullToZero(bigDecimals[i]));
		// zero
		i = 0;
		assertEquals(integers[expected], Cmp.nullToZero(integers[i]));
		assertEquals(longs[expected], Cmp.nullToZero(longs[i]));
		assertEquals(floats[expected], Cmp.nullToZero(floats[i]));
		assertEquals(doubles[expected], Cmp.nullToZero(doubles[i]));
		assertEquals(bigIntegers[expected], Cmp.nullToZero(bigIntegers[i]));
		assertEquals(bigDecimals[expected], Cmp.nullToZero(bigDecimals[i]));
		// non zero
		i = 1;
		expected = 1;
		assertSame(integers[expected], Cmp.nullToZero(integers[i]));
		assertSame(longs[expected], Cmp.nullToZero(longs[i]));
		assertSame(floats[expected], Cmp.nullToZero(floats[i]));
		assertSame(doubles[expected], Cmp.nullToZero(doubles[i]));
		assertSame(bigIntegers[expected], Cmp.nullToZero(bigIntegers[i]));
		assertSame(bigDecimals[expected], Cmp.nullToZero(bigDecimals[i]));
	}

	@Test
	public void geOrNull() {
		// null
		int i = _null;
		final Date now = new Date(), before = new Date(now.getTime() - 1);
		assertTrue(Cmp.geOrNull(integers[i], 1));
		assertTrue(Cmp.geOrNull(longs[i], 1L));
		assertTrue(Cmp.geOrNull(bigIntegers[i], BigInteger.ONE));
		assertTrue(Cmp.geOrNull(bigDecimals[i], BigDecimal.ONE));
		assertTrue(Cmp.geOrNull(null, now));
		// equals
		i = 1;
		assertTrue(Cmp.geOrNull(integers[i], 1));
		assertTrue(Cmp.geOrNull(longs[i], 1L));
		assertTrue(Cmp.geOrNull(bigIntegers[i], BigInteger.ONE));
		assertTrue(Cmp.geOrNull(bigDecimals[i], BigDecimal.ONE));
		assertTrue(Cmp.geOrNull(now, new Date(now.getTime())));
		// >
		i = 2;
		assertTrue(Cmp.geOrNull(integers[i], -1));
		assertTrue(Cmp.geOrNull(longs[i], 1L));
		assertTrue(Cmp.geOrNull(bigIntegers[i], BigInteger.ONE));
		assertTrue(Cmp.geOrNull(bigDecimals[i], BigDecimal.ONE));
		assertTrue(Cmp.geOrNull(now, before));
		// <
		i = 0;
		assertFalse(Cmp.geOrNull(integers[i], 1));
		assertFalse(Cmp.geOrNull(longs[i], 1L));
		assertFalse(Cmp.geOrNull(bigIntegers[i], BigInteger.ONE));
		assertFalse(Cmp.geOrNull(bigDecimals[i], BigDecimal.ONE));
		assertFalse(Cmp.geOrNull(before, now));
		// special
		assertTrue(Cmp.geOrNull(bigDecimals[0], new BigDecimal("0.00")));
		assertTrue(Cmp.geOrNull(bigDecimals[0], new BigDecimal("-0.000000000001")));
		assertTrue(Cmp.geOrNull(new BigDecimal("-0.000000000001"), new BigDecimal("-0.000000000002")));
		assertFalse(Cmp.geOrNull(bigDecimals[1], new BigDecimal("1.0000000001")));
		assertTrue(Cmp.geOrNull(bigDecimals[1], BigDecimal.valueOf(-2)));
	}

	@Test
	public void ge() {
		// null
		int i = _null;
		final Date now = new Date(), before = new Date(now.getTime() - 1);
		assertFalse(Cmp.ge(integers[i], 1));
		assertFalse(Cmp.ge(longs[i], 1L));
		assertFalse(Cmp.ge(bigIntegers[i], BigInteger.ONE));
		assertFalse(Cmp.ge(bigDecimals[i], BigDecimal.ONE));
		assertFalse(Cmp.ge(null, now));
		// equals
		i = 1;
		assertTrue(Cmp.ge(integers[i], 1));
		assertTrue(Cmp.ge(longs[i], 1L));
		assertTrue(Cmp.ge(bigIntegers[i], BigInteger.ONE));
		assertTrue(Cmp.ge(bigDecimals[i], BigDecimal.ONE));
		assertTrue(Cmp.ge(now, new Date(now.getTime())));
		// >
		i = 2;
		assertTrue(Cmp.ge(integers[i], -1));
		assertTrue(Cmp.ge(longs[i], 1L));
		assertTrue(Cmp.ge(bigIntegers[i], BigInteger.ONE));
		assertTrue(Cmp.ge(bigDecimals[i], BigDecimal.ONE));
		assertTrue(Cmp.ge(now, before));
		// <
		i = 0;
		assertFalse(Cmp.ge(integers[i], 1));
		assertFalse(Cmp.ge(longs[i], 1L));
		assertFalse(Cmp.ge(bigIntegers[i], BigInteger.ONE));
		assertFalse(Cmp.ge(bigDecimals[i], BigDecimal.ONE));
		assertFalse(Cmp.ge(before, now));
		// special
		assertTrue(Cmp.ge(bigDecimals[0], new BigDecimal("0.00")));
		assertTrue(Cmp.ge(bigDecimals[0], new BigDecimal("-0.000000000001")));
		assertTrue(Cmp.ge(new BigDecimal("-0.000000000001"), new BigDecimal("-0.000000000002")));
		assertFalse(Cmp.ge(bigDecimals[1], new BigDecimal("1.0000000001")));
		assertTrue(Cmp.ge(bigDecimals[1], BigDecimal.valueOf(-2)));
	}

	@Test
	public void gtOrNull() {
		// null
		int i = _null;
		final Date now = new Date(), before = new Date(now.getTime() - 1);
		assertTrue(Cmp.gtOrNull(integers[i], 1));
		assertTrue(Cmp.gtOrNull(longs[i], 1L));
		assertTrue(Cmp.gtOrNull(bigIntegers[i], BigInteger.ONE));
		assertTrue(Cmp.gtOrNull(bigDecimals[i], BigDecimal.ONE));
		assertTrue(Cmp.gtOrNull(null, now));
		// equals
		i = 1;
		assertFalse(Cmp.gtOrNull(integers[i], 1));
		assertFalse(Cmp.gtOrNull(longs[i], 1L));
		assertFalse(Cmp.gtOrNull(bigIntegers[i], BigInteger.ONE));
		assertFalse(Cmp.gtOrNull(bigDecimals[i], BigDecimal.ONE));
		assertFalse(Cmp.gtOrNull(now, new Date(now.getTime())));
		// >
		i = 2;
		assertTrue(Cmp.gtOrNull(integers[i], -1));
		assertTrue(Cmp.gtOrNull(longs[i], 1L));
		assertTrue(Cmp.gtOrNull(bigIntegers[i], BigInteger.ONE));
		assertTrue(Cmp.gtOrNull(bigDecimals[i], BigDecimal.ONE));
		assertTrue(Cmp.gtOrNull(now, before));
		// <
		i = 0;
		assertFalse(Cmp.gtOrNull(integers[i], 1));
		assertFalse(Cmp.gtOrNull(longs[i], 1L));
		assertFalse(Cmp.gtOrNull(bigIntegers[i], BigInteger.ONE));
		assertFalse(Cmp.gtOrNull(bigDecimals[i], BigDecimal.ONE));
		assertFalse(Cmp.gtOrNull(before, now));
		// special
		assertFalse(Cmp.gtOrNull(bigDecimals[0], new BigDecimal("0.00")));
		assertTrue(Cmp.gtOrNull(bigDecimals[0], new BigDecimal("-0.000000000001")));
		assertTrue(Cmp.gtOrNull(new BigDecimal("-0.000000000001"), new BigDecimal("-0.000000000002")));
		assertFalse(Cmp.gtOrNull(bigDecimals[1], new BigDecimal("1.0000000001")));
		assertTrue(Cmp.gtOrNull(bigDecimals[1], BigDecimal.valueOf(-2)));
	}

	@Test
	public void gt() {
		// null
		int i = _null;
		final Date now = new Date(), before = new Date(now.getTime() - 1);
		assertFalse(Cmp.gt(integers[i], 1));
		assertFalse(Cmp.gt(longs[i], 1L));
		assertFalse(Cmp.gt(bigIntegers[i], BigInteger.ONE));
		assertFalse(Cmp.gt(bigDecimals[i], BigDecimal.ONE));
		assertFalse(Cmp.gt(null, now));
		// equals
		i = 1;
		assertFalse(Cmp.gt(integers[i], 1));
		assertFalse(Cmp.gt(longs[i], 1L));
		assertFalse(Cmp.gt(bigIntegers[i], BigInteger.ONE));
		assertFalse(Cmp.gt(bigDecimals[i], BigDecimal.ONE));
		assertFalse(Cmp.gt(now, new Date(now.getTime())));
		// >
		i = 2;
		assertTrue(Cmp.gt(integers[i], -1));
		assertTrue(Cmp.gt(longs[i], 1L));
		assertTrue(Cmp.gt(bigIntegers[i], BigInteger.ONE));
		assertTrue(Cmp.gt(bigDecimals[i], BigDecimal.ONE));
		assertTrue(Cmp.gt(now, before));
		// <
		i = 0;
		assertFalse(Cmp.gt(integers[i], 1));
		assertFalse(Cmp.gt(longs[i], 1L));
		assertFalse(Cmp.gt(bigIntegers[i], BigInteger.ONE));
		assertFalse(Cmp.gt(bigDecimals[i], BigDecimal.ONE));
		assertFalse(Cmp.gt(before, now));
		// special
		assertFalse(Cmp.gt(bigDecimals[0], new BigDecimal("0.00")));
		assertTrue(Cmp.gt(bigDecimals[0], new BigDecimal("-0.000000000001")));
		assertTrue(Cmp.gt(new BigDecimal("-0.000000000001"), new BigDecimal("-0.000000000002")));
		assertFalse(Cmp.gt(bigDecimals[1], new BigDecimal("1.0000000001")));
		assertTrue(Cmp.gt(bigDecimals[1], BigDecimal.valueOf(-2)));
	}

	@Test
	public void leOrNull() {
		// null
		int i = _null;
		final Date now = new Date(), before = new Date(now.getTime() - 1);
		assertTrue(Cmp.leOrNull(integers[i], 1));
		assertTrue(Cmp.leOrNull(longs[i], 1L));
		assertTrue(Cmp.leOrNull(bigIntegers[i], BigInteger.ONE));
		assertTrue(Cmp.leOrNull(bigDecimals[i], BigDecimal.ONE));
		assertTrue(Cmp.leOrNull(null, now));
		// equals
		i = 1;
		assertTrue(Cmp.leOrNull(integers[i], 1));
		assertTrue(Cmp.leOrNull(longs[i], 1L));
		assertTrue(Cmp.leOrNull(bigIntegers[i], BigInteger.ONE));
		assertTrue(Cmp.leOrNull(bigDecimals[i], BigDecimal.ONE));
		assertTrue(Cmp.leOrNull(now, new Date(now.getTime())));
		// >
		i = 2;
		assertFalse(Cmp.leOrNull(integers[i], -1));
		assertFalse(Cmp.leOrNull(longs[i], 1L));
		assertFalse(Cmp.leOrNull(bigIntegers[i], BigInteger.ONE));
		assertFalse(Cmp.leOrNull(bigDecimals[i], BigDecimal.ONE));
		assertFalse(Cmp.leOrNull(now, before));
		// <
		i = 0;
		assertTrue(Cmp.leOrNull(integers[i], 1));
		assertTrue(Cmp.leOrNull(longs[i], 1L));
		assertTrue(Cmp.leOrNull(bigIntegers[i], BigInteger.ONE));
		assertTrue(Cmp.leOrNull(bigDecimals[i], BigDecimal.ONE));
		assertTrue(Cmp.leOrNull(before, now));
		// special
		assertTrue(Cmp.leOrNull(bigDecimals[0], new BigDecimal("0.00")));
		assertFalse(Cmp.leOrNull(bigDecimals[0], new BigDecimal("-0.000000000001")));
		assertFalse(Cmp.leOrNull(new BigDecimal("-0.000000000001"), new BigDecimal("-0.000000000002")));
		assertTrue(Cmp.leOrNull(bigDecimals[1], new BigDecimal("1.0000000001")));
		assertFalse(Cmp.leOrNull(bigDecimals[1], BigDecimal.valueOf(-2)));
	}

	@Test
	public void le() {
		// null
		int i = _null;
		final Date now = new Date(), before = new Date(now.getTime() - 1);
		assertFalse(Cmp.le(integers[i], 1));
		assertFalse(Cmp.le(longs[i], 1L));
		assertFalse(Cmp.le(bigIntegers[i], BigInteger.ONE));
		assertFalse(Cmp.le(bigDecimals[i], BigDecimal.ONE));
		assertFalse(Cmp.le(null, now));
		// equals
		i = 1;
		assertTrue(Cmp.le(integers[i], 1));
		assertTrue(Cmp.le(longs[i], 1L));
		assertTrue(Cmp.le(bigIntegers[i], BigInteger.ONE));
		assertTrue(Cmp.le(bigDecimals[i], BigDecimal.ONE));
		assertTrue(Cmp.le(now, new Date(now.getTime())));
		// >
		i = 2;
		assertFalse(Cmp.le(integers[i], -1));
		assertFalse(Cmp.le(longs[i], 1L));
		assertFalse(Cmp.le(bigIntegers[i], BigInteger.ONE));
		assertFalse(Cmp.le(bigDecimals[i], BigDecimal.ONE));
		assertFalse(Cmp.le(now, before));
		// <
		i = 0;
		assertTrue(Cmp.le(integers[i], 1));
		assertTrue(Cmp.le(longs[i], 1L));
		assertTrue(Cmp.le(bigIntegers[i], BigInteger.ONE));
		assertTrue(Cmp.le(bigDecimals[i], BigDecimal.ONE));
		assertTrue(Cmp.le(before, now));
		// special
		assertTrue(Cmp.le(bigDecimals[0], new BigDecimal("0.00")));
		assertFalse(Cmp.le(bigDecimals[0], new BigDecimal("-0.000000000001")));
		assertFalse(Cmp.le(new BigDecimal("-0.000000000001"), new BigDecimal("-0.000000000002")));
		assertTrue(Cmp.le(bigDecimals[1], new BigDecimal("1.0000000001")));
		assertFalse(Cmp.le(bigDecimals[1], BigDecimal.valueOf(-2)));
	}

	@Test
	public void ltOrNull() {
		// null
		int i = _null;
		final Date now = new Date(), before = new Date(now.getTime() - 1);
		assertTrue(Cmp.ltOrNull(integers[i], 1));
		assertTrue(Cmp.ltOrNull(longs[i], 1L));
		assertTrue(Cmp.ltOrNull(bigIntegers[i], BigInteger.ONE));
		assertTrue(Cmp.ltOrNull(bigDecimals[i], BigDecimal.ONE));
		assertTrue(Cmp.ltOrNull(null, now));
		// equals
		i = 1;
		assertFalse(Cmp.ltOrNull(integers[i], 1));
		assertFalse(Cmp.ltOrNull(longs[i], 1L));
		assertFalse(Cmp.ltOrNull(bigIntegers[i], BigInteger.ONE));
		assertFalse(Cmp.ltOrNull(bigDecimals[i], BigDecimal.ONE));
		assertFalse(Cmp.ltOrNull(now, new Date(now.getTime())));
		// >
		i = 2;
		assertFalse(Cmp.ltOrNull(integers[i], -1));
		assertFalse(Cmp.ltOrNull(longs[i], 1L));
		assertFalse(Cmp.ltOrNull(bigIntegers[i], BigInteger.ONE));
		assertFalse(Cmp.ltOrNull(bigDecimals[i], BigDecimal.ONE));
		assertFalse(Cmp.ltOrNull(now, before));
		// <
		i = 0;
		assertTrue(Cmp.ltOrNull(integers[i], 1));
		assertTrue(Cmp.ltOrNull(longs[i], 1L));
		assertTrue(Cmp.ltOrNull(bigIntegers[i], BigInteger.ONE));
		assertTrue(Cmp.ltOrNull(bigDecimals[i], BigDecimal.ONE));
		assertTrue(Cmp.ltOrNull(before, now));
		// special
		assertFalse(Cmp.ltOrNull(bigDecimals[0], new BigDecimal("0.00")));
		assertFalse(Cmp.ltOrNull(bigDecimals[0], new BigDecimal("-0.000000000001")));
		assertFalse(Cmp.ltOrNull(new BigDecimal("-0.000000000001"), new BigDecimal("-0.000000000002")));
		assertTrue(Cmp.ltOrNull(bigDecimals[1], new BigDecimal("1.0000000001")));
		assertFalse(Cmp.ltOrNull(bigDecimals[1], BigDecimal.valueOf(-2)));
	}

	@Test
	public void lt() {
		// null
		int i = _null;
		final Date now = new Date(), before = new Date(now.getTime() - 1);
		assertFalse(Cmp.lt(integers[i], 1));
		assertFalse(Cmp.lt(longs[i], 1L));
		assertFalse(Cmp.lt(bigIntegers[i], BigInteger.ONE));
		assertFalse(Cmp.lt(bigDecimals[i], BigDecimal.ONE));
		assertFalse(Cmp.lt(null, now));
		// equals
		i = 1;
		assertFalse(Cmp.lt(integers[i], 1));
		assertFalse(Cmp.lt(longs[i], 1L));
		assertFalse(Cmp.lt(bigIntegers[i], BigInteger.ONE));
		assertFalse(Cmp.lt(bigDecimals[i], BigDecimal.ONE));
		assertFalse(Cmp.lt(now, new Date(now.getTime())));
		// >
		i = 2;
		assertFalse(Cmp.lt(integers[i], -1));
		assertFalse(Cmp.lt(longs[i], 1L));
		assertFalse(Cmp.lt(bigIntegers[i], BigInteger.ONE));
		assertFalse(Cmp.lt(bigDecimals[i], BigDecimal.ONE));
		assertFalse(Cmp.lt(now, before));
		// <
		i = 0;
		assertTrue(Cmp.lt(integers[i], 1));
		assertTrue(Cmp.lt(longs[i], 1L));
		assertTrue(Cmp.lt(bigIntegers[i], BigInteger.ONE));
		assertTrue(Cmp.lt(bigDecimals[i], BigDecimal.ONE));
		assertTrue(Cmp.lt(before, now));
		// special
		assertFalse(Cmp.lt(bigDecimals[0], new BigDecimal("0.00")));
		assertFalse(Cmp.lt(bigDecimals[0], new BigDecimal("-0.000000000001")));
		assertFalse(Cmp.lt(new BigDecimal("-0.000000000001"), new BigDecimal("-0.000000000002")));
		assertTrue(Cmp.lt(bigDecimals[1], new BigDecimal("1.0000000001")));
		assertFalse(Cmp.lt(bigDecimals[1], BigDecimal.valueOf(-2)));
	}

	@Test
	public void between() {
		final Date now = new Date(), before = new Date(now.getTime() - 1), after = new Date(now.getTime() + 1);
		// null
		assertFalse(Cmp.between(integers[_null], 0, 1));
		assertFalse(Cmp.between(longs[_null], 0, 1));
		assertFalse(Cmp.between(bigIntegers[_null], bigIntegers[0], bigIntegers[1]));
		assertFalse(Cmp.between(bigIntegers[0], null, bigIntegers[1]));
		assertFalse(Cmp.between(bigIntegers[0], bigIntegers[0], null));
		assertFalse(Cmp.between(bigIntegers[0], null, null));
		assertFalse(Cmp.between(bigDecimals[_null], bigDecimals[0], bigDecimals[1]));
		assertFalse(Cmp.between(null, before, after));
		assertFalse(Cmp.between(now, before, null));
		assertFalse(Cmp.between(now, null, after));
		assertFalse(Cmp.between(now, null, null));
		// $val < min
		int i = 0;
		assertFalse(Cmp.between(integers[i], 1, 2));
		assertFalse(Cmp.between(longs[i], 1L, 2L));
		assertFalse(Cmp.between(bigIntegers[i], bigIntegers[1], bigIntegers[2]));
		assertFalse(Cmp.between(bigDecimals[i], bigDecimals[1], bigDecimals[2]));
		assertFalse(Cmp.between(before, now, after));
		// $val = min
		assertTrue(Cmp.between(integers[i], 0, 2));
		assertTrue(Cmp.between(longs[i], 0L, 2L));
		assertTrue(Cmp.between(bigIntegers[i], bigIntegers[0], bigIntegers[2]));
		assertTrue(Cmp.between(bigDecimals[i], bigDecimals[0], bigDecimals[2]));
		assertTrue(Cmp.between(before, before, after));
		// min < $val < max
		i = 1;
		assertTrue(Cmp.between(integers[i], 0, 2));
		assertTrue(Cmp.between(longs[i], 0L, 2L));
		assertTrue(Cmp.between(bigIntegers[i], bigIntegers[0], bigIntegers[2]));
		assertTrue(Cmp.between(bigDecimals[i], bigDecimals[0], bigDecimals[2]));
		assertTrue(Cmp.between(now, before, after));
		// $val = max
		i = 2;
		assertTrue(Cmp.between(integers[i], 0, 2));
		assertTrue(Cmp.between(longs[i], 0L, 2L));
		assertTrue(Cmp.between(bigIntegers[i], bigIntegers[0], bigIntegers[2]));
		assertTrue(Cmp.between(bigDecimals[i], bigDecimals[0], bigDecimals[2]));
		assertTrue(Cmp.between(after, before, after));
		// $val > max
		assertFalse(Cmp.between(integers[i], 0, 1));
		assertFalse(Cmp.between(longs[i], 0L, 1L));
		assertFalse(Cmp.between(bigIntegers[i], bigIntegers[0], bigIntegers[1]));
		assertFalse(Cmp.between(bigDecimals[i], bigDecimals[0], bigDecimals[1]));
		assertFalse(Cmp.between(after, before, now));
		// special
		assertTrue(Cmp.between(bigDecimals[0], new BigDecimal("0.00"), bigDecimals[1]));
		assertTrue(Cmp.between(BigDecimal.valueOf(-1), BigDecimal.valueOf(-2), bigDecimals[1]));
	}

}