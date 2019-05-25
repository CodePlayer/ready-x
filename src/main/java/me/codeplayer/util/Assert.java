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
	 * @param errorMsg 异常消息内容
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
	 * @param msger 异常消息内容
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
	 * @param message 异常消息内容
	 */
	public static final void notTrue(final boolean expression, final @Nullable CharSequence message) {
		isTrue(!expression, message);
	}

	/**
	 * 断言布尔表达式结果为false<br>
	 * 如果断言失败则抛出异常
	 * 
	 * @param expression boolean表达式
	 * @param msger 异常消息内容
	 */
	public static final void notTrue(boolean expression, final @Nullable Supplier<CharSequence> msger) {
		isTrue(!expression, msger);
	}

	/**
	 * 断言指定对象为null<br>
	 * 如果断言失败则抛出异常
	 * 
	 * @param object 指定对象
	 * @param message 异常消息内容
	 */
	public static final void isNull(Object object) {
		isTrue(object == null);
	}

	/**
	 * 断言指定对象为null<br>
	 * 如果断言失败则抛出异常
	 * 
	 * @param object 指定对象
	 * @param message 异常消息内容
	 */
	public static final void isNull(Object object, @Nullable CharSequence message) {
		isTrue(object == null, message);
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
	 * @param object 指定对象
	 * @param message 异常消息内容
	 */
	public static final void notNull(Object object, final @Nullable CharSequence errorMsg) {
		if (object == null) {
			throw new NullPointerException(X.map(errorMsg, CharSequence::toString));
		}
	}

	/**
	 * 断言指定对象不为null<br>
	 * 如果断言失败则抛出异常
	 * 
	 * @param object 指定对象
	 * @param msger 异常消息内容
	 */
	public static final void notNull(Object object, final @Nullable Supplier<CharSequence> msger) {
		if (object == null) {
			if (msger != null) {
				throw new NullPointerException(X.map(msger.get(), CharSequence::toString));
			}
			throw new NullPointerException();
		}
	}

	/**
	 * 断言指定对象的字符串形式为空(若为null、空字符串均属断言成功)<br>
	 * 如果断言失败则抛出异常
	 * 
	 * @param str 指定字符串
	 * @param message 异常消息内容
	 * @see #isEmpty(Object)
	 */
	public static final void isEmpty(Object str, final @Nullable CharSequence message) {
		isTrue(StringUtil.isEmpty(str), message);
	}

	/**
	 * 断言指定字符串不为空(若为null、空字符串均属断言失败)<br>
	 * 如果断言失败则抛出异常
	 * 
	 * @param str 指定字符串
	 * @param message 异常消息内容
	 * @see #isEmpty(Object)
	 */
	public static final <T> T notEmpty(T str) {
		isTrue(StringUtil.notEmpty(str));
		return str;
	}

	/**
	 * 断言指定字符串不为空(若为null、空字符串均属断言失败)<br>
	 * 如果断言失败则抛出异常
	 * 
	 * @param str 指定字符串
	 * @param message 异常消息内容
	 * @see #isEmpty(Object)
	 */
	public static final void notEmpty(Object str, final @Nullable CharSequence message) {
		isTrue(StringUtil.notEmpty(str), message);
	}

	/**
	 * 断言指定字符串不为空(若为null、空字符串均属断言失败)<br>
	 * 如果断言失败则抛出异常
	 * 
	 * @param str 指定字符串
	 * @param message 异常消息内容
	 * @see #isEmpty(Object)
	 */
	public static final void notEmpty(Object str, final @Nullable Supplier<CharSequence> message) {
		isTrue(StringUtil.notEmpty(str), message);
	}

	/**
	 * 断言指定对象为空对象，如果断言失败则抛出异常<br>
	 * 空对象的定义如下： <br>
	 * 1.字符串对象 == null或者去空格后==空字符串<br>
	 * 2.其他对象==null
	 * 
	 * @param obj 指定对象
	 * @param message 异常消息内容
	 * @see #isBlank(Object)
	 */
	public static final void isBlank(Object obj, String message) {
		isTrue(StringUtil.isBlank(obj), message);
	}

	/**
	 * 断言指定对象不为空对象，如果断言失败则抛出异常<br>
	 * 空对象的定义如下： <br>
	 * 1.字符串对象 == null或者去空格后==空字符串<br>
	 * 2.其他对象==null
	 * 
	 * @param obj 指定对象
	 * @param message 异常消息内容
	 * @see #isBlank(Object)
	 */
	public static final void notBlank(Object obj, String message) {
		isTrue(!StringUtil.isBlank(obj), message);
	}

	/**
	 * 断言两个对象相等(equals)，如果断言失败则抛出异常<br>
	 * 
	 * @param obj 指定的对象
	 * @param another 另一个对象
	 * @param message 异常消息内容
	 */
	public static final void equals(Object obj, Object another, String message) {
		isTrue((obj == null && another == null) || (obj != null && obj.equals(another)), message);
	}

	/**
	 * 断言两个对象不相等(equals)，如果断言失败则抛出异常<br>
	 * 
	 * @param obj 指定的对象
	 * @param another 另一个对象
	 * @param message 异常消息内容
	 */
	public static final void notEquals(Object obj, Object another, String message) {
		isTrue((obj == null && another != null) || (obj != null && obj.equals(another)), message);
	}

	/**
	 * 断言两个对象相等(==)，如果断言失败则抛出异常<br>
	 * 
	 * @param obj 指定的对象
	 * @param another 另一个对象
	 * @param message 异常消息内容
	 */
	public static final void isSame(Object obj, Object another, String message) {
		isTrue(obj == another, message);
	}

	/**
	 * 断言两个对象不相等(==)，如果断言失败则抛出异常<br>
	 * 
	 * @param obj 指定的对象
	 * @param another 另一个对象
	 * @param message 异常消息内容
	 */
	public static final void notSame(Object obj, Object another, String message) {
		isTrue(obj != another, message);
	}
}
