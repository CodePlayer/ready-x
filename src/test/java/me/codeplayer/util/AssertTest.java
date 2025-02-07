package me.codeplayer.util;

import org.junit.Test;

public class AssertTest {

	@Test
	public void isTrue_ExpressionTrue_NoException() {
		Assert.isTrue(true);
	}

	@Test(expected = AssertException.class)
	public void isTrue_ExpressionFalse_ExceptionThrown() {
		Assert.isTrue(false);
	}

	@Test
	public void state_ExpressionTrue_NoException() {
		Assert.state(true);
	}

	@Test(expected = IllegalStateException.class)
	public void state_ExpressionFalse_ExceptionThrown() {
		Assert.state(false);
	}

	@Test
	public void isFalse_ExpressionFalse_NoException() {
		Assert.isFalse(false);
	}

	@Test(expected = AssertException.class)
	public void isFalse_ExpressionTrue_ExceptionThrown() {
		Assert.isFalse(true);
	}

	@Test
	public void notNull_ObjectNotNull_NoException() {
		Object obj = new Object();
		Assert.notNull(obj);
	}

	@Test(expected = NullPointerException.class)
	public void notNull_ObjectNull_ExceptionThrown() {
		Assert.notNull(null);
	}

	@Test
	public void isEmpty_StringNullOrEmpty_NoException() {
		Assert.isEmpty(null);
		Assert.isEmpty("");
	}

	@Test(expected = AssertException.class)
	public void isEmpty_StringNotEmpty_ExceptionThrown() {
		Assert.isEmpty("not empty");
	}

	@Test
	public void notEmpty_StringNotNullOrEmpty_NoException() {
		Assert.notEmpty("not empty");
	}

	@Test
	public void notBlank_StringNotNullOrBlank_NoException() {
		Assert.notBlank("not blank");
	}

	@Test(expected = AssertException.class)
	public void notEmpty_StringNull_ExceptionThrown() {
		Assert.notEmpty(null);
	}

	@Test(expected = AssertException.class)
	public void notEmpty_StringEmpty_ExceptionThrown() {
		Assert.notEmpty("");
	}

	@Test(expected = AssertException.class)
	public void notBlank_StringNull_ExceptionThrown() {
		Assert.notBlank(null);
	}

	@Test(expected = AssertException.class)
	public void notBlank_StringEmpty_ExceptionThrown() {
		Assert.notBlank("");
	}

	@Test(expected = AssertException.class)
	public void notBlank_StringBlank_ExceptionThrown() {
		Assert.notBlank("   ");
	}

}