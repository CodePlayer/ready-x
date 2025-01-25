package me.codeplayer.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class CmpTest {

	@Test
	public void eqVal_IntegerEqual_ReturnsTrue() {
		assertTrue(Cmp.eqVal(1, 1));
	}

	@Test
	public void eqVal_IntegerNotEqual_ReturnsFalse() {
		assertFalse(Cmp.eqVal(1, 2));
	}

	@Test
	public void eqVal_OneIntegerIsNull_ReturnsFalse() {
		assertFalse(Cmp.eqVal(null, 1));
	}

	@Test
	public void eqVal_BothIntegersAreNull_ReturnsFalse() {
		assertFalse(Cmp.eqVal((Integer) null, null));
	}

	@Test
	public void eqVal_LongEqual_ReturnsTrue() {
		assertTrue(Cmp.eqVal(1L, 1L));
	}

	@Test
	public void eqVal_LongNotEqual_ReturnsFalse() {
		assertFalse(Cmp.eqVal(1L, 2L));
	}

	@Test
	public void eqVal_OneLongIsNull_ReturnsFalse() {
		assertFalse(Cmp.eqVal(null, 1L));
	}

	@Test
	public void eqVal_BothLongsAreNull_ReturnsFalse() {
		assertFalse(Cmp.eqVal((Long) null, null));
	}

	@Test
	public void eq_IntegerEqualsInt_ReturnsTrue() {
		assertTrue(Cmp.eq((Integer) 1, 1));
	}

	@Test
	public void eq_IntegerIsNull_ReturnsFalse() {
		assertFalse(Cmp.eq((Integer) null, 1));
	}

	@Test
	public void eq_LongEqualsLong_ReturnsTrue() {
		assertTrue(Cmp.eq((Long) 1L, 1L));
	}

	@Test
	public void eq_LongIsNull_ReturnsFalse() {
		assertFalse(Cmp.eq(null, 1L));
	}

	@Test
	public void eq_ObjectsEqual_ReturnsTrue() {
		assertTrue(Cmp.eq("test", "test"));
	}

	@Test
	public void eq_ObjectsNotEqual_ReturnsFalse() {
		assertFalse(Cmp.eq("test", "other"));
	}

	@Test
	public void eq_OneObjectIsNull_ReturnsFalse() {
		assertFalse(Cmp.eq(null, "test"));
	}

	@Test
	public void zeroToNull_IntegerZero_ReturnsNull() {
		assertNull(Cmp.zeroToNull(0));
	}

	@Test
	public void zeroToNull_IntegerNotZero_ReturnsValue() {
		assertEquals(Integer.valueOf(1), Cmp.zeroToNull(1));
	}

	@Test
	public void zeroToNull_IntegerIsNull_ReturnsNull() {
		assertNull(Cmp.zeroToNull((Integer) null));
	}

	@Test
	public void nullToZero_IntegerIsNull_ReturnsZero() {
		Integer zero = Cmp.nullToZero((Integer) null);
		assertEquals((Integer) 0, zero);
	}

	@Test
	public void nullToZero_IntegerNotNull_ReturnsValue() {
		assertEquals(Integer.valueOf(1), Cmp.nullToZero(1));
	}

	@Test
	public void geOrNull_IntegerIsNull_ReturnsTrue() {
		assertTrue(Cmp.geOrNull((Integer) null, 1));
	}

	@Test
	public void geOrNull_IntegerGreaterOrEqual_ReturnsTrue() {
		assertTrue(Cmp.geOrNull(2, 1));
	}

	@Test
	public void geOrNull_IntegerLess_ReturnsFalse() {
		assertFalse(Cmp.geOrNull(1, 2));
	}

	@Test
	public void gtOrNull_IntegerIsNull_ReturnsTrue() {
		assertTrue(Cmp.gtOrNull((Integer) null, 1));
	}

	@Test
	public void gtOrNull_IntegerGreaterThan_ReturnsTrue() {
		assertTrue(Cmp.gtOrNull(2, 1));
	}

	@Test
	public void gtOrNull_IntegerLessOrEqual_ReturnsFalse() {
		assertFalse(Cmp.gtOrNull(1, 1));
	}

	@Test
	public void ge_IntegerIsNull_ReturnsFalse() {
		assertFalse(Cmp.ge((Integer) null, 1));
	}

	@Test
	public void ge_IntegerGreaterOrEqual_ReturnsTrue() {
		assertTrue(Cmp.ge(2, 1));
	}

	@Test
	public void ge_IntegerLess_ReturnsFalse() {
		assertFalse(Cmp.ge(1, 2));
	}

	@Test
	public void gt_IntegerIsNull_ReturnsFalse() {
		assertFalse(Cmp.gt((Integer) null, 1));
	}

	@Test
	public void gt_IntegerGreaterThan_ReturnsTrue() {
		assertTrue(Cmp.gt(2, 1));
	}

	@Test
	public void gt_IntegerLessOrEqual_ReturnsFalse() {
		assertFalse(Cmp.gt(1, 1));
	}

	@Test
	public void leOrNull_IntegerIsNull_ReturnsTrue() {
		assertTrue(Cmp.leOrNull((Integer) null, 1));
	}

	@Test
	public void leOrNull_IntegerLessOrEqual_ReturnsTrue() {
		assertTrue(Cmp.leOrNull(1, 2));
	}

	@Test
	public void leOrNull_IntegerGreater_ReturnsFalse() {
		assertFalse(Cmp.leOrNull(2, 1));
	}

	@Test
	public void ltOrNull_IntegerIsNull_ReturnsTrue() {
		assertTrue(Cmp.ltOrNull((Integer) null, 1));
	}

	@Test
	public void ltOrNull_IntegerLessThan_ReturnsTrue() {
		assertTrue(Cmp.ltOrNull(1, 2));
	}

	@Test
	public void ltOrNull_IntegerGreaterOrEqual_ReturnsFalse() {
		assertFalse(Cmp.ltOrNull(2, 1));
	}

	@Test
	public void le_IntegerIsNull_ReturnsFalse() {
		assertFalse(Cmp.le((Integer) null, 1));
	}

	@Test
	public void le_IntegerLessOrEqual_ReturnsTrue() {
		assertTrue(Cmp.le(1, 2));
	}

	@Test
	public void le_IntegerGreater_ReturnsFalse() {
		assertFalse(Cmp.le(2, 1));
	}

	@Test
	public void lt_IntegerIsNull_ReturnsFalse() {
		assertFalse(Cmp.lt((Integer) null, 1));
	}

	@Test
	public void lt_IntegerLessThan_ReturnsTrue() {
		assertTrue(Cmp.lt(1, 2));
	}

	@Test
	public void lt_IntegerGreaterOrEqual_ReturnsFalse() {
		assertFalse(Cmp.lt(2, 1));
	}

	@Test
	public void between_IntegerInRange_ReturnsTrue() {
		assertTrue(Cmp.between((Integer) 1, 0, 2));
	}

	@Test
	public void between_IntegerNotInRange_ReturnsFalse() {
		assertFalse(Cmp.between((Integer) 3, 0, 2));
	}

	@Test
	public void between_IntegerIsNull_ReturnsFalse() {
		assertFalse(Cmp.between((Integer) null, 0, 2));
	}

	@Test
	public void between_MinIsNull_ReturnsFalse() {
		assertFalse(Cmp.between(1, null, 2));
	}

	@Test
	public void between_MaxIsNull_ReturnsFalse() {
		assertFalse(Cmp.between(1, 0, null));
	}

}