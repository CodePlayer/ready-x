package me.codeplayer.util;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

	/**
	 * 测试 isNull(Object object) 方法 - 正常情况
	 */
	@Test
	public void isNull() {
		// 测试用的错误信息
		final String errorMsg = "object should be null";
		final Supplier<CharSequence> errorMsgSupplier = () -> errorMsg;
		// 传入 null，不应抛出异常
		assertDoesNotThrow(() -> Assert.isNull(null));
		// 传入非 null 对象，应抛出 AssertException
		assertThrows(AssertException.class, () -> Assert.isNull(new Object()));
		// 传入 null 和错误信息，不应抛出异常
		assertDoesNotThrow(() -> Assert.isNull(null, errorMsg));
		// 传入 null 和 null 错误信息，不应抛出异常
		assertDoesNotThrow(() -> Assert.isNull(null, (CharSequence) null));
		{
			// 传入非 null 对象和错误信息，应抛出 AssertException
			AssertException exception = assertThrows(AssertException.class, () -> Assert.isNull(new Object(), errorMsg));
			// 验证异常消息
			assert exception.getMessage() != null && exception.getMessage().contains(errorMsg);
		}

		// 传入非 null 对象和 null 错误信息，应抛出 AssertException
		assertThrows(AssertException.class, () -> Assert.isNull(new Object(), (CharSequence) null));
		// 传入 null 和错误信息 Supplier，不应抛出异常
		assertDoesNotThrow(() -> Assert.isNull(null, errorMsgSupplier));
		// 传入 null 和 null Supplier，不应抛出异常
		assertDoesNotThrow(() -> Assert.isNull(null, (Supplier<CharSequence>) null));
		{
			// 传入非 null 对象和错误信息 Supplier，应抛出 AssertException
			AssertException exception = assertThrows(AssertException.class, () -> Assert.isNull(new Object(), errorMsgSupplier));
			// 验证异常消息
			assert exception.getMessage() != null && exception.getMessage().contains(errorMsg);
		}
		// 传入非 null 对象和 null Supplier，应抛出 AssertException
		assertThrows(AssertException.class, () -> Assert.isNull(new Object(), (Supplier<CharSequence>) null));
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