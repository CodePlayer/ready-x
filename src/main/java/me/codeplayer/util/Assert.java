package me.codeplayer.util;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static me.codeplayer.util.StringX.nullSafeGet;

/**
 * 项目中的通用断言类，用于处理异常，如果断言失败将会抛出异常<br>
 * 断言方法均以 <code>is</code> 开头，相反的方法均以 <code>not</code> 开头（ <code>isFalse()</code> 除外 ）。例如：
 * <pre><code>
 * isTrue 和 isFalse
 * isNull 和 notNull
 * isEmpty 和 notEmpty
 * isBlank 和 notBlank
 * </code></pre>
 *
 * @author Ready
 * @since 2012-4-23
 */
public abstract class Assert {

	/**
	 * 断言布尔表达式结果为true<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param expression boolean表达式
	 * @throws AssertException 如果断言失败
	 */
	public static void isTrue(final boolean expression) {
		if (!expression) {
			throw new AssertException();
		}
	}

	/**
	 * 断言布尔表达式结果为true<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param expression boolean表达式
	 * @param errorMsg 异常消息内容
	 * @throws AssertException 如果断言失败
	 */
	public static void isTrue(final boolean expression, final @Nullable CharSequence errorMsg) {
		if (!expression) {
			throw new AssertException(nullSafeGet(errorMsg));
		}
	}

	/**
	 * 断言布尔表达式结果为true<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param expression boolean表达式
	 * @param msger 异常消息内容
	 * @throws AssertException 如果断言失败
	 */
	public static void isTrue(final boolean expression, final @Nullable Supplier<? extends CharSequence> msger) {
		if (!expression) {
			throw new AssertException(nullSafeGet(msger));
		}
	}

	/**
	 * 断言布尔表达式结果为true<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param expression boolean表达式
	 * @throws IllegalStateException 如果断言失败
	 */
	public static void state(final boolean expression) {
		if (!expression) {
			throw new IllegalStateException();
		}
	}

	/**
	 * 断言布尔表达式结果为true<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param expression boolean表达式
	 * @param errorMsg 异常消息内容
	 * @throws IllegalStateException 如果断言失败
	 */
	public static void state(final boolean expression, final @Nullable CharSequence errorMsg) {
		if (!expression) {
			throw new IllegalStateException(nullSafeGet(errorMsg));
		}
	}

	/**
	 * 断言布尔表达式结果为true<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param expression boolean表达式
	 * @param msger 异常消息内容
	 * @throws IllegalStateException 如果断言失败
	 */
	public static void state(final boolean expression, final @Nullable Supplier<? extends CharSequence> msger) {
		if (!expression) {
			throw new IllegalStateException(nullSafeGet(msger));
		}
	}

	/**
	 * 断言布尔表达式结果为 false
	 * 如果断言失败则抛出异常
	 *
	 * @param expression boolean表达式
	 * @throws AssertException 如果断言失败
	 */
	public static void isFalse(final boolean expression) {
		isTrue(!expression);
	}

	/**
	 * 断言布尔表达式结果为false<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param expression boolean表达式
	 * @param errorMsg 异常消息内容
	 * @throws AssertException 如果断言失败
	 */
	public static void isFalse(final boolean expression, final @Nullable CharSequence errorMsg) {
		isTrue(!expression, errorMsg);
	}

	/**
	 * 断言布尔表达式结果为false<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param expression boolean表达式
	 * @param msger 异常消息内容
	 * @throws AssertException 如果断言失败
	 */
	public static void isFalse(boolean expression, final @Nullable Supplier<? extends CharSequence> msger) {
		isTrue(!expression, msger);
	}

	/**
	 * 断言指定对象为 null<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param object 指定对象
	 * @throws AssertException 如果断言失败
	 */
	public static void isNull(Object object) {
		isTrue(object == null);
	}

	/**
	 * 断言指定对象为 null<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param object 指定对象
	 * @param errorMsg 异常消息内容
	 * @throws AssertException 如果断言失败
	 */
	public static void isNull(@Nullable Object object, @Nullable CharSequence errorMsg) {
		isTrue(object == null, errorMsg);
	}

	/**
	 * 断言指定对象为 null<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param object 指定对象
	 * @param errorMsg 异常消息内容
	 * @throws AssertException 如果断言失败
	 */
	public static void isNull(@Nullable Object object, @Nullable Supplier<? extends CharSequence> errorMsg) {
		isTrue(object == null, errorMsg);
	}

	/**
	 * 断言指定对象不为null<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param object 指定对象
	 * @throws NullPointerException 如果对象为 null
	 */
	@Nonnull
	public static <T> T notNull(@Nullable T object) throws NullPointerException {
		if (object == null) {
			throw new NullPointerException();
		}
		return object;
	}

	/**
	 * 断言指定对象不为null<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param obj 指定对象
	 * @param errorMsg 异常消息内容
	 * @throws NullPointerException 如果对象为 null
	 */
	@Nonnull
	public static <T> T notNull(final @Nullable T obj, final @Nullable CharSequence errorMsg) throws NullPointerException {
		if (obj == null) {
			throw new NullPointerException(nullSafeGet(errorMsg));
		}
		return obj;
	}

	/**
	 * 断言指定对象不为null<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param obj 指定对象
	 * @param msger 异常消息内容
	 * @throws NullPointerException 如果对象为 null
	 */
	@Nonnull
	public static <T> T notNull(final @Nullable T obj, final @Nullable Supplier<? extends CharSequence> msger) throws NullPointerException {
		if (obj == null) {
			throw new NullPointerException(nullSafeGet(msger));
		}
		return obj;
	}

	/**
	 * 断言指定对象的字符串形式为空(若为null、空字符串均属断言成功)<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param str 指定字符串
	 * @throws AssertException 如果断言失败
	 */
	public static void isEmpty(final @Nullable CharSequence str) {
		isTrue(StringX.isEmpty(str));
	}

	/**
	 * 断言指定对象的字符串形式为空(若为null、空字符串均属断言成功)<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param str 指定字符串
	 * @param errorMsg 异常消息内容
	 * @throws AssertException 如果断言失败
	 */
	public static void isEmpty(final @Nullable CharSequence str, final @Nullable CharSequence errorMsg) {
		isTrue(StringX.isEmpty(str), errorMsg);
	}

	/**
	 * 断言指定对象的字符串形式为空(若为null、空字符串均属断言成功)<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param str 指定字符串
	 * @param errorMsger 异常消息内容
	 * @throws AssertException 如果断言失败
	 */
	public static void isEmpty(final @Nullable CharSequence str, final @Nullable Supplier<? extends CharSequence> errorMsger) {
		isTrue(StringX.isEmpty(str), errorMsger);
	}

	/**
	 * 断言指定字符串不为空(若为null、空字符串均属断言失败)<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param str 指定字符串
	 * @throws AssertException 如果断言失败
	 * @see StringX#notEmpty(CharSequence)
	 */
	@Nonnull
	public static <T extends CharSequence> T notEmpty(@Nullable T str) {
		isTrue(StringX.notEmpty(str));
		return str;
	}

	/**
	 * 断言指定字符串不为空(若为null、空字符串均属断言失败)<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param str 指定字符串
	 * @param errorMsg 异常消息内容
	 * @throws AssertException 如果断言失败
	 * @see StringX#notEmpty(CharSequence)
	 */
	public static <T extends CharSequence> T notEmpty(@Nullable T str, final @Nullable CharSequence errorMsg) {
		isTrue(StringX.notEmpty(str), errorMsg);
		return str;
	}

	/**
	 * 断言指定字符串不为空(若为null、空字符串均属断言失败)<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param str 指定字符串
	 * @param errorMsg 异常消息内容
	 * @throws AssertException 如果断言失败
	 * @see StringX#notEmpty(CharSequence)
	 */
	public static <T extends CharSequence> T notEmpty(@Nullable T str, final @Nullable Supplier<? extends CharSequence> errorMsg) {
		isTrue(StringX.notEmpty(str), errorMsg);
		return str;
	}

	/**
	 * 断言指定对象不为空对象，如果断言失败则抛出异常<br>
	 * 空对象的定义如下： <br>
	 * 1.字符串对象 == null或者去空格后==空字符串<br>
	 * 2.其他对象==null
	 *
	 * @param obj 指定对象
	 * @throws AssertException 如果断言失败
	 * @see StringX#notBlank(CharSequence)
	 */
	@Nonnull
	public static <T extends CharSequence> T notBlank(@Nullable T obj) {
		isTrue(StringX.notBlank(obj));
		return obj;
	}

	/**
	 * 断言指定对象不为空对象，如果断言失败则抛出异常<br>
	 * 空对象的定义如下： <br>
	 * 1.字符串对象 == null或者去空格后==空字符串<br>
	 * 2.其他对象==null
	 *
	 * @param cs 指定对象
	 * @param errorMsg 异常消息内容
	 * @throws AssertException 如果断言失败
	 * @see StringX#notBlank(CharSequence)
	 */
	@Nonnull
	public static <T extends CharSequence> T notBlank(@Nullable T cs, @Nullable CharSequence errorMsg) {
		isTrue(StringX.notBlank(cs), errorMsg);
		return cs;
	}

	/**
	 * 断言指定对象不为空对象，如果断言失败则抛出异常<br>
	 * 空对象的定义如下： <br>
	 * 1.字符串对象 == null或者去空格后==空字符串<br>
	 * 2.其他对象==null
	 *
	 * @param cs 指定对象
	 * @param errorMsger 异常消息内容
	 * @throws AssertException 如果断言失败
	 * @see StringX#notBlank(CharSequence)
	 */
	@Nonnull
	public static <T extends CharSequence> T notBlank(@Nullable T cs, final @Nullable Supplier<? extends CharSequence> errorMsger) {
		isTrue(StringX.notBlank(cs), errorMsger);
		return cs;
	}

}