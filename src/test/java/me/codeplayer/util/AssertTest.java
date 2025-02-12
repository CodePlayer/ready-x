package me.codeplayer.util;

import java.util.function.Supplier;

import org.junit.Test;

import static org.junit.Assert.assertThrows;

@SuppressWarnings("DataFlowIssue")
public class AssertTest {

	static final String errorMsg = "assert failed", nullMsg = null;
	static final Supplier<CharSequence> errorMsgSupplier = () -> errorMsg, nullSupplier = null;

	@Test
	public void isTrue() {
		Assert.isTrue(true);
		Assert.isTrue(true, "error message");
		Assert.isTrue(true, () -> "assert failed");

		assertThrows(AssertException.class, () -> Assert.isTrue(false));
		assertThrows(AssertException.class, () -> Assert.isTrue(false, errorMsg));
		assertThrows(AssertException.class, () -> Assert.isTrue(false, nullMsg));
		assertThrows(AssertException.class, () -> Assert.isTrue(false, errorMsgSupplier));
		assertThrows(AssertException.class, () -> Assert.isTrue(false, nullSupplier));
	}

	@Test
	public void state() {
		Assert.state(true);
		assertThrows(IllegalStateException.class, () -> Assert.state(false));
		assertThrows(IllegalStateException.class, () -> Assert.state(false, nullMsg));
		assertThrows(IllegalStateException.class, () -> Assert.state(false, errorMsg));
		assertThrows(IllegalStateException.class, () -> Assert.state(false, errorMsgSupplier));
		assertThrows(IllegalStateException.class, () -> Assert.state(false, nullSupplier));
	}

	@Test
	public void isFalse() {
		Assert.isFalse(false);
		assertThrows(AssertException.class, () -> Assert.isFalse(true));
		assertThrows(AssertException.class, () -> Assert.isFalse(true, nullMsg));
		assertThrows(AssertException.class, () -> Assert.isFalse(true, errorMsg));
		assertThrows(AssertException.class, () -> Assert.isFalse(true, errorMsgSupplier));
		assertThrows(AssertException.class, () -> Assert.isFalse(true, nullSupplier));
	}

	@Test
	public void notNull() {
		Object obj = new Object();
		Assert.notNull(obj);

		assertThrows(NullPointerException.class, () -> Assert.notNull(null));
		assertThrows(NullPointerException.class, () -> Assert.notNull(null, nullMsg));
		assertThrows(NullPointerException.class, () -> Assert.notNull(null, errorMsg));
		assertThrows(NullPointerException.class, () -> Assert.notNull(null, errorMsgSupplier));
		assertThrows(NullPointerException.class, () -> Assert.notNull(null, nullSupplier));
	}

	@Test
	public void isEmpty() {
		Assert.isEmpty(null);
		Assert.isEmpty("");

		assertThrows(AssertException.class, () -> Assert.isEmpty("null"));
		assertThrows(AssertException.class, () -> Assert.isEmpty(" "));
		assertThrows(AssertException.class, () -> Assert.isEmpty(" "));
		assertThrows(AssertException.class, () -> Assert.isEmpty(" ", errorMsg));
		assertThrows(AssertException.class, () -> Assert.isEmpty(" ", nullMsg));
		assertThrows(AssertException.class, () -> Assert.isEmpty(" ", errorMsgSupplier));
		assertThrows(AssertException.class, () -> Assert.isEmpty(" ", nullSupplier));
	}

	@Test
	public void notEmpty() {
		Assert.notEmpty("not empty");
		Assert.notEmpty(" ");
		Assert.notEmpty("null");
		assertThrows(AssertException.class, () -> Assert.notEmpty(null));
		assertThrows(AssertException.class, () -> Assert.notEmpty(""));
		assertThrows(AssertException.class, () -> Assert.notEmpty("", errorMsg));
		assertThrows(AssertException.class, () -> Assert.notEmpty("", nullMsg));
		assertThrows(AssertException.class, () -> Assert.notEmpty("", errorMsgSupplier));
		assertThrows(AssertException.class, () -> Assert.notEmpty("", nullSupplier));
	}

	@Test
	public void notBlank() {
		Assert.notBlank("not empty");
		Assert.notBlank("null");
		assertThrows(AssertException.class, () -> Assert.notBlank(null));
		assertThrows(AssertException.class, () -> Assert.notBlank(" "));
		assertThrows(AssertException.class, () -> Assert.notBlank(" ", errorMsg));
		assertThrows(AssertException.class, () -> Assert.notBlank(" ", nullMsg));
		assertThrows(AssertException.class, () -> Assert.notBlank(" ", errorMsgSupplier));
		assertThrows(AssertException.class, () -> Assert.notBlank(" ", nullSupplier));
	}

}