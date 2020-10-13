package me.codeplayer.util;

import java.util.function.*;

import javax.annotation.*;

/**
 * 项目中的通用断言类，用于处理异常，如果断言失败将会抛出异常<br>
 * 断言方法均以is开头，相反的方法均以not开头<br>
 * 例如：isTrue和notTrue、isNull和notNull、isEmpty和notEmpty、isBlank和notBlank
 *
 * @author Ready
 * @date 2012-4-23
 */
public abstract class Assert {

	/**
	 * 断言布尔表达式结果为true<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param expression boolean表达式
	 */
	public static final void isTrue(final boolean expression) {
		if (!expression) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 断言布尔表达式结果为true<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param expression boolean表达式
	 * @param errorMsg   异常消息内容
	 */
	public static final void isTrue(final boolean expression, final @Nullable CharSequence errorMsg) {
		if (!expression) {
			throw new IllegalArgumentException(X.map(errorMsg, CharSequence::toString));
		}
	}

	/**
	 * 断言布尔表达式结果为true<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param expression boolean表达式
	 * @param msger      异常消息内容
	 */
	public static final void isTrue(final boolean expression, final @Nullable Supplier<CharSequence> msger) {
		if (!expression) {
			if (msger != null) {
				throw new IllegalArgumentException(X.map(msger.get(), CharSequence::toString));
			}
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 断言布尔表达式结果为false<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param expression boolean表达式
	 * @param errorMsg   异常消息内容
	 */
	public static final void notTrue(final boolean expression, final @Nullable CharSequence errorMsg) {
		isTrue(!expression, errorMsg);
	}

	/**
	 * 断言布尔表达式结果为false<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param expression boolean表达式
	 * @param msger      异常消息内容
	 */
	public static final void notTrue(boolean expression, final @Nullable Supplier<CharSequence> msger) {
		isTrue(!expression, msger);
	}

	/**
	 * 断言指定对象为null<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param object 指定对象
	 */
	public static final void isNull(Object object) {
		isTrue(object == null);
	}

	/**
	 * 断言指定对象为null<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param object   指定对象
	 * @param errorMsg 异常消息内容
	 */
	public static final void isNull(Object object, @Nullable CharSequence errorMsg) {
		isTrue(object == null, errorMsg);
	}

	/**
	 * 断言指定对象不为null<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param object 指定对象
	 */
	public static final <T> T notNull(T object) {
		if (object == null) {
			throw new NullPointerException();
		}
		return object;
	}

	/**
	 * 断言指定对象不为null<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param obj      指定对象
	 * @param errorMsg 异常消息内容
	 */
	public static final <T> T notNull(final T obj, final @Nullable CharSequence errorMsg) {
		if (obj == null) {
			throw new NullPointerException(X.map(errorMsg, CharSequence::toString));
		}
		return obj;
	}

	/**
	 * 断言指定对象不为null<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param obj   指定对象
	 * @param msger 异常消息内容
	 */
	public static final <T> T notNull(final T obj, final @Nullable Supplier<CharSequence> msger) {
		if (obj == null) {
			if (msger != null) {
				throw new NullPointerException(X.map(msger.get(), CharSequence::toString));
			}
			throw new NullPointerException();
		}
		return obj;
	}

	/**
	 * 断言指定对象的字符串形式为空(若为null、空字符串均属断言成功)<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param str      指定字符串
	 * @param errorMsg 异常消息内容
	 */
	public static final <T> T isEmpty(final T str, final @Nullable CharSequence errorMsg) {
		isTrue(StringUtil.isEmpty(str), errorMsg);
		return str;
	}

	/**
	 * 断言指定对象的字符串形式为空(若为null、空字符串均属断言成功)<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param str 指定字符串
	 */
	public static final <T> T isEmpty(T str) {
		isTrue(StringUtil.isEmpty(str));
		return str;
	}

	/**
	 * 断言指定字符串不为空(若为null、空字符串均属断言失败)<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param str 指定字符串
	 */
	public static final <T> T notEmpty(T str) {
		isTrue(StringUtil.notEmpty(str));
		return str;
	}

	/**
	 * 断言指定字符串不为空(若为null、空字符串均属断言失败)<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param str      指定字符串
	 * @param errorMsg 异常消息内容
	 * @see StringUtil#notEmpty(Object)
	 */
	public static final void notEmpty(Object str, final @Nullable CharSequence errorMsg) {
		isTrue(StringUtil.notEmpty(str), errorMsg);
	}

	/**
	 * 断言指定字符串不为空(若为null、空字符串均属断言失败)<br>
	 * 如果断言失败则抛出异常
	 *
	 * @param str      指定字符串
	 * @param errorMsg 异常消息内容
	 * @see StringUtil#notEmpty(Object)
	 */
	public static final void notEmpty(Object str, final @Nullable Supplier<CharSequence> errorMsg) {
		isTrue(StringUtil.notEmpty(str), errorMsg);
	}

	/**
	 * 断言指定对象为空对象，如果断言失败则抛出异常<br>
	 * 空对象的定义如下： <br>
	 * 1.字符串对象 == null或者去空格后==空字符串<br>
	 * 2.其他对象==null
	 *
	 * @param obj      指定对象
	 * @param errorMsg 异常消息内容
	 * @see StringUtil#isBlank(Object)
	 */
	public static final <T> T isBlank(T obj, String errorMsg) {
		isTrue(StringUtil.isBlank(obj), errorMsg);
		return obj;
	}

	/**
	 * 断言指定对象不为空对象，如果断言失败则抛出异常<br>
	 * 空对象的定义如下： <br>
	 * 1.字符串对象 == null或者去空格后==空字符串<br>
	 * 2.其他对象==null
	 *
	 * @param obj 指定对象
	 * @see StringUtil#notBlank(Object)
	 */
	public static final <T> T notBlank(T obj) {
		isTrue(StringUtil.notBlank(obj));
		return obj;
	}

	/**
	 * 断言指定对象不为空对象，如果断言失败则抛出异常<br>
	 * 空对象的定义如下： <br>
	 * 1.字符串对象 == null或者去空格后==空字符串<br>
	 * 2.其他对象==null
	 *
	 * @param obj      指定对象
	 * @param errorMsg 异常消息内容
	 * @see StringUtil#notBlank(Object)
	 */
	public static final <T> T notBlank(T obj, String errorMsg) {
		isTrue(StringUtil.notBlank(obj), errorMsg);
		return obj;
	}

	/**
	 * 断言两个对象相等(equals)，如果断言失败则抛出异常<br>
	 *
	 * @param obj      指定的对象
	 * @param another  另一个对象
	 * @param errorMsg 异常消息内容
	 */
	public static final void equals(Object obj, Object another, String errorMsg) {
		isTrue(obj == another || obj != null && obj.equals(another), errorMsg);
	}

	/**
	 * 断言两个对象不相等(equals)，如果断言失败则抛出异常<br>
	 *
	 * @param obj      指定的对象
	 * @param another  另一个对象
	 * @param errorMsg 异常消息内容
	 */
	public static final void notEquals(Object obj, Object another, String errorMsg) {
		notTrue(obj == another || obj != null && obj.equals(another), errorMsg);
	}

	/**
	 * 断言两个对象相等(==)，如果断言失败则抛出异常<br>
	 *
	 * @param obj      指定的对象
	 * @param another  另一个对象
	 * @param errorMsg 异常消息内容
	 */
	public static final void isSame(Object obj, Object another, String errorMsg) {
		isTrue(obj == another, errorMsg);
	}

	/**
	 * 断言两个对象不相等(==)，如果断言失败则抛出异常<br>
	 *
	 * @param obj      指定的对象
	 * @param another  另一个对象
	 * @param errorMsg 异常消息内容
	 */
	public static final void notSame(Object obj, Object another, String errorMsg) {
		notTrue(obj == another, errorMsg);
	}
}
