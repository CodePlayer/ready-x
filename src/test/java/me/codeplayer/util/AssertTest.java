package me.codeplayer.util;

import java.util.function.Supplier;

import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

@SuppressWarnings("DataFlowIssue")
public class AssertTest {

	static final String errorMsg = "assert failed", nullMsg = null;
	static final Supplier<CharSequence> errorMsgSupplier = () -> errorMsg, nullSupplier = null;

	@Test
	public void isTrue() {
		Assert.isTrue(true);
		Assert.isTrue(true, errorMsg);
		Assert.isTrue(true, nullMsg);
		Assert.isTrue(true, errorMsgSupplier);
		Assert.isTrue(true, nullSupplier);

		assertThrows(AssertException.class, () -> Assert.isTrue(false));
		assertThrows(AssertException.class, () -> Assert.isTrue(false, errorMsg));
		assertThrows(AssertException.class, () -> Assert.isTrue(false, nullMsg));
		assertThrows(AssertException.class, () -> Assert.isTrue(false, errorMsgSupplier));
		assertThrows(AssertException.class, () -> Assert.isTrue(false, nullSupplier));
	}

	@Test
	public void state() {
		Assert.state(true);
		Assert.state(true, errorMsg);
		Assert.state(true, nullMsg);
		Assert.state(true, errorMsgSupplier);
		Assert.state(true, nullSupplier);

		assertThrows(IllegalStateException.class, () -> Assert.state(false));
		assertThrows(IllegalStateException.class, () -> Assert.state(false, nullMsg));
		assertThrows(IllegalStateException.class, () -> Assert.state(false, errorMsg));
		assertThrows(IllegalStateException.class, () -> Assert.state(false, errorMsgSupplier));
		assertThrows(IllegalStateException.class, () -> Assert.state(false, nullSupplier));
	}

	@Test
	public void isFalse() {
		Assert.isFalse(false);
		Assert.isFalse(false, errorMsg);
		Assert.isFalse(false, nullMsg);
		Assert.isFalse(false, errorMsgSupplier);
		Assert.isFalse(false, nullSupplier);

		assertThrows(AssertException.class, () -> Assert.isFalse(true));
		assertThrows(AssertException.class, () -> Assert.isFalse(true, nullMsg));
		assertThrows(AssertException.class, () -> Assert.isFalse(true, errorMsg));
		assertThrows(AssertException.class, () -> Assert.isFalse(true, errorMsgSupplier));
		assertThrows(AssertException.class, () -> Assert.isFalse(true, nullSupplier));
	}

	@Test
	public void notNull() {
		Object obj = new Object();
		assertSame(obj, Assert.notNull(obj));
		assertSame(obj, Assert.notNull(obj, errorMsg));
		assertSame(obj, Assert.notNull(obj, nullMsg));
		assertSame(obj, Assert.notNull(obj, errorMsgSupplier));
		assertSame(obj, Assert.notNull(obj, nullSupplier));

		assertThrows(NullPointerException.class, () -> Assert.notNull(null));
		assertThrows(NullPointerException.class, () -> Assert.notNull(null, nullMsg));
		assertThrows(NullPointerException.class, () -> Assert.notNull(null, errorMsg));
		assertThrows(NullPointerException.class, () -> Assert.notNull(null, errorMsgSupplier));
		assertThrows(NullPointerException.class, () -> Assert.notNull(null, nullSupplier));
	}

	@Test
	public void isEmpty() {
		Assert.isEmpty(null);
		Assert.isEmpty("", errorMsg);
		Assert.isEmpty("", nullMsg);
		Assert.isEmpty("", errorMsgSupplier);
		Assert.isEmpty("", nullSupplier);

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
		assertSame("not empty", Assert.notEmpty("not empty"));
		Assert.notEmpty(" ");
		assertSame("null", Assert.notEmpty("null"));
		assertSame("null", Assert.notEmpty("null", errorMsg));
		assertSame("null", Assert.notEmpty("null", errorMsgSupplier));
		assertSame("null", Assert.notEmpty("null", nullMsg));
		assertSame("null", Assert.notEmpty("null", nullSupplier));

		assertThrows(AssertException.class, () -> Assert.notEmpty(null));
		assertThrows(AssertException.class, () -> Assert.notEmpty(""));
		assertThrows(AssertException.class, () -> Assert.notEmpty("", errorMsg));
		assertThrows(AssertException.class, () -> Assert.notEmpty("", nullMsg));
		assertThrows(AssertException.class, () -> Assert.notEmpty("", errorMsgSupplier));
		assertThrows(AssertException.class, () -> Assert.notEmpty("", nullSupplier));
	}

	@Test
	public void notBlank() {
		assertSame("not empty", Assert.notBlank("not empty"));
		assertSame("null", Assert.notBlank("null", errorMsg));
		assertSame(" not blank", Assert.notBlank(" not blank", errorMsgSupplier));
		assertSame(" not blank", Assert.notBlank(" not blank", nullMsg));
		assertSame(" not blank", Assert.notBlank(" not blank", nullSupplier));

		assertThrows(AssertException.class, () -> Assert.notBlank(null));
		assertThrows(AssertException.class, () -> Assert.notBlank(" "));
		assertThrows(AssertException.class, () -> Assert.notBlank(" ", errorMsg));
		assertThrows(AssertException.class, () -> Assert.notBlank(" ", nullMsg));
		assertThrows(AssertException.class, () -> Assert.notBlank(" ", errorMsgSupplier));
		assertThrows(AssertException.class, () -> Assert.notBlank(" ", nullSupplier));
	}

}