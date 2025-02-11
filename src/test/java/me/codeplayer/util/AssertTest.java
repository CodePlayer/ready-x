package me.codeplayer.util;

import java.util.function.Supplier;

import org.junit.Test;

import static org.junit.Assert.assertThrows;

@SuppressWarnings("DataFlowIssue")
public class AssertTest {

	@Test
	public void isTrue() {
		Assert.isTrue(true);
		Assert.isTrue(true, "error message");
		Assert.isTrue(true, () -> "assert failed");

		assertThrows(AssertException.class, () -> Assert.isTrue(false));
		assertThrows(AssertException.class, () -> Assert.isTrue(false, "error message"));
		assertThrows(AssertException.class, () -> Assert.isTrue(false, (CharSequence) null));
		assertThrows(AssertException.class, () -> Assert.isTrue(false, () -> "assert failed"));
		assertThrows(AssertException.class, () -> Assert.isTrue(false, (Supplier<? extends CharSequence>) null));
	}

	@Test
	public void state() {
		Assert.state(true);
		assertThrows(IllegalStateException.class, () -> Assert.state(false));
		assertThrows(IllegalStateException.class, () -> Assert.state(false, "state error message"));
		assertThrows(IllegalStateException.class, () -> Assert.state(false, (CharSequence) null));
	}

	@Test
	public void isFalse() {
		Assert.isFalse(false);
		assertThrows(AssertException.class, () -> Assert.isFalse(true));
		assertThrows(AssertException.class, () -> Assert.isFalse(true, (CharSequence) null));
		assertThrows(AssertException.class, () -> Assert.isFalse(true, "assert failed"));
		assertThrows(AssertException.class, () -> Assert.isFalse(true, () -> "assert failed"));
		assertThrows(AssertException.class, () -> Assert.isFalse(true, (Supplier<? extends CharSequence>) null));
	}

	@Test
	public void notNull() {
		Object obj = new Object();
		Assert.notNull(obj);

		assertThrows(NullPointerException.class, () -> Assert.notNull(null));
		assertThrows(NullPointerException.class, () -> Assert.notNull(null, (CharSequence) null));
		assertThrows(NullPointerException.class, () -> Assert.notNull(null, "assert failed"));
		assertThrows(NullPointerException.class, () -> Assert.notNull(null, () -> "assert failed"));
		assertThrows(NullPointerException.class, () -> Assert.notNull(null, (Supplier<? extends CharSequence>) null));
	}

	@Test
	public void isEmpty() {
		Assert.isEmpty(null);
		Assert.isEmpty("");

		assertThrows(AssertException.class, () -> Assert.isEmpty("null"));
		assertThrows(AssertException.class, () -> Assert.isEmpty(" "));
		assertThrows(AssertException.class, () -> Assert.isEmpty(" "));
		assertThrows(AssertException.class, () -> Assert.isEmpty(" ", (CharSequence) null));
		assertThrows(AssertException.class, () -> Assert.isEmpty(" ", "assert failed"));
		assertThrows(AssertException.class, () -> Assert.isEmpty(" ", () -> "assert failed"));
		assertThrows(AssertException.class, () -> Assert.isEmpty(" ", (Supplier<? extends CharSequence>) null));
	}

	@Test
	public void notEmpty() {
		Assert.notEmpty("not empty");
		Assert.notEmpty(" ");
		Assert.notEmpty("null");
		assertThrows(AssertException.class, () -> Assert.notEmpty(null));
		assertThrows(AssertException.class, () -> Assert.notEmpty(""));
		assertThrows(AssertException.class, () -> Assert.notEmpty("", (CharSequence) null));
		assertThrows(AssertException.class, () -> Assert.notEmpty("", "assert failed"));
		assertThrows(AssertException.class, () -> Assert.notEmpty("", () -> "assert failed"));
		assertThrows(AssertException.class, () -> Assert.notEmpty("", (Supplier<? extends CharSequence>) null));
	}

	@Test
	public void notBlank() {
		Assert.notBlank("not empty");
		Assert.notBlank("null");
		assertThrows(AssertException.class, () -> Assert.notBlank(null));
		assertThrows(AssertException.class, () -> Assert.notBlank(" "));
		assertThrows(AssertException.class, () -> Assert.notBlank(" ", (CharSequence) null));
		assertThrows(AssertException.class, () -> Assert.notBlank(" ", "assert failed"));
		assertThrows(AssertException.class, () -> Assert.notBlank(" ", () -> "assert failed"));
		assertThrows(AssertException.class, () -> Assert.notBlank(" ", (Supplier<? extends CharSequence>) null));
	}

}