package me.codeplayer.util;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;

import org.jspecify.annotations.Nullable;

/**
 * 通用公共工具类<br>
 * 此类全为静态方法，请使用静态方法的形式调用<br>
 * 由于均为静态方法，所以类名尽可能地短，以免干扰逻辑可读性<br>
 * 并且工具类会经常被调用，类名短，也方便开发人员编写。
 *
 * @author Ready
 */
public abstract class X {

	/**
	 * 判断指定的字符串是否为空<br>
	 * 如果字符串为null、空字符串,则返回true<br>
	 * <b>注意：</b>本方法不会去除字符串两边的空格，如果想去除字符串两边的空格后再进行判断，可以使用 {@link #isBlank(CharSequence)} 方法
	 */
	public static boolean isEmpty(CharSequence str) {
		return StringUtil.isEmpty(str);
	}

	/**
	 * 判断指定的字符串是否为null或去空格后为空字符串，如果是则返回true
	 *
	 * @param str 指定的字符串对象
	 */
	public static boolean isBlank(CharSequence str) {
		return StringUtil.isBlank(str);
	}

	/**
	 * 判断指定的对象是否为空<br>
	 * 如果对象(或其 toString() 返回值)为null、空字符串、空格字符串，则返回true<br>
	 * <b>注意：</b>本方法会先去除字符串两边的空格，再判断
	 *
	 * @param obj 指定的对象
	 * @see StringUtil#isBlank(Object)
	 */
	public static boolean isBlank(Object obj) {
		return StringUtil.isBlank(obj);
	}

	/**
	 * 从指定的多个值依次检测并选取第一个不为null的值
	 */
	public static <T> T expectNotNull(T v1, T v2, T v3, T v4) {
		if (v1 != null) {
			return v1;
		} else if (v2 != null) {
			return v2;
		} else if (v3 != null) {
			return v3;
		} else {
			return v4;
		}
	}

	/**
	 * 从指定的多个值依次检测并选取第一个不为 null 的值
	 *
	 * @see X#expectNotNull(Object, Object, Object, Object)
	 */
	public static <T> T expectNotNull(T v1, T v2, T v3) {
		return v1 != null ? v1 : (v2 != null ? v2 : v3);
	}

	/**
	 * 从指定的多个值依次检测并选取第一个不为 null 的值
	 *
	 * @see #expectNotNull(Object, Object, Object, Object)
	 */
	public static <T> T expectNotNull(T v1, T v2) {
		return v1 != null ? v1 : v2;
	}

	/**
	 * 从指定的多个字符串依次检测并选取第一个不为空字符串的值，否则返回空字符串""
	 */
	public static String expectNotEmpty(String v1, String v2, String v3, String v4) {
		if (v1 != null && !v1.isEmpty()) {
			return v1;
		} else if (v2 != null && !v2.isEmpty()) {
			return v2;
		} else if (v3 != null && !v3.isEmpty()) {
			return v3;
		} else if (v4 != null && !v4.isEmpty()) {
			return v4;
		}
		return "";
	}

	/**
	 * 从指定的多个字符串依次检测并选取第一个不为空字符串的值，否则返回空字符串""
	 */
	public static String expectNotEmpty(String v1, String v2, String v3) {
		return expectNotEmpty(v1, v2, v3, null);
	}

	/**
	 * 从指定的多个字符串依次检测并选取第一个不为空字符串的值，否则返回空字符串""
	 */
	public static String expectNotEmpty(String v1, String v2) {
		return StringUtil.notEmpty(v1) ? v1 : StringUtil.toString(v2);
	}

	/**
	 * 判断指定Boolean值是否有效。<code>true</code> 即为有效。
	 *
	 * @param b 指定的 Boolean 对象
	 */
	public static boolean isValid(Boolean b) {
		return b != null && b;
	}

	/**
	 * 判断指定的数值对象是否有效。如果参数为 <code>null</code> 或 数值等于0，则为无效；其他均为有效。
	 *
	 * @param number 指定的
	 */
	public static boolean isValid(Number number) {
		return number != null && number.doubleValue() != 0;
	}

	/**
	 * 判断指定的数值对象是否有效。如果参数为 <code>null</code> 或 数值等于0，则为无效；其他均为有效。
	 *
	 * @param number 指定的
	 */
	public static boolean isValid(Integer number) {
		return number != null && number != 0;
	}

	/**
	 * 判断指定的数值对象是否有效。如果参数为 <code>null</code> 或 数值等于0，则为无效；其他均为有效。
	 *
	 * @param number 指定的
	 */
	public static boolean isValid(Long number) {
		return number != null && number != 0;
	}

	/**
	 * 判断指定的字符串序列是否有效。如果参数为 <code>null</code> 或空字符串 <code>""</code> ，则为无效；其他均为有效。
	 *
	 * @param sequence 指定的字符串序列对象
	 */
	public static boolean isValid(CharSequence sequence) {
		return StringUtil.notEmpty(sequence);
	}

	/**
	 * 判断指定的Map对象是否有效。如果参数为 <code>null</code> 或 <code>map.size() == 0</code>，则为无效，其他均为有效。
	 *
	 * @param map 指定的映射集合对象
	 */
	public static boolean isValid(Map<?, ?> map) {
		return map != null && !map.isEmpty();
	}

	/**
	 * 判断指定的Collection对象是否有效。如果参数为 <code>null</code> 或 <code>collection.size() == 0</code>，则为无效，其他均为有效。
	 *
	 * @param collection 指定的集合对象
	 */
	public static boolean isValid(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}

	/**
	 * 判断指定byte数组是否有效。如果参数为 <code>null</code> 或 <code>array.length == 0</code>，则为无效，其他均为有效。
	 *
	 * @param array 指定的 byte 数组
	 */
	public static boolean isValid(byte[] array) {
		return array != null && array.length > 0;
	}

	/**
	 * 判断指定int数组是否有效。如果参数为 <code>null</code> 或 <code>array.length == 0</code>，则为无效，其他均为有效。
	 *
	 * @param array 指定的 int 数组
	 */
	public static boolean isValid(int[] array) {
		return array != null && array.length > 0;
	}

	/**
	 * 判断指定long数组是否有效。如果参数为 <code>null</code> 或 <code>array.length == 0</code>，则为无效，其他均为有效。
	 *
	 * @param array 指定的 long 数组
	 */
	public static boolean isValid(long[] array) {
		return array != null && array.length > 0;
	}

	/**
	 * 判断指定char数组是否有效。如果参数为 <code>null</code> 或 <code>array.length == 0</code>，则为无效，其他均为有效。
	 *
	 * @param array 指定的 char 数组
	 */
	public static boolean isValid(char[] array) {
		return array != null && array.length > 0;
	}

	/**
	 * 判断指定float数组是否有效。如果参数为 <code>null</code> 或 <code>array.length == 0</code>，则为无效，其他均为有效。
	 *
	 * @param array 指定的 float 数组
	 */
	public static boolean isValid(float[] array) {
		return array != null && array.length > 0;
	}

	/**
	 * 判断指定double数组是否有效。如果参数为 <code>null</code> 或 <code>array.length == 0</code>，则为无效，其他均为有效。
	 *
	 * @param array 指定的 double 数组
	 */
	public static boolean isValid(double[] array) {
		return array != null && array.length > 0;
	}

	/**
	 * 判断指定的对象数组是否有效。如果参数为 <code>null</code> 或 <code>array.length == 0</code>，则为无效，其他均为有效。
	 *
	 * @param array 指定的对象数组
	 */
	public static boolean isValid(Object[] array) {
		return array != null && array.length > 0;
	}

	/**
	 * 判断指定对象是否无效。只有以下情况视为无效，其他均为有效。<br>
	 * 无效的参数对象arg的定义如下(按照判断顺序排序)： <br>
	 * 1. <code>arg == null</code><br>
	 * 2. 如果<code>arg</code>是字符序列对象，去空格后，<code>arg.length() == 0</code><br>
	 * 3. 如果<code>arg</code>是数值对象，去空格后，<code>值  == 0</code><br>
	 * 4. 如果<code>arg</code>是映射集合(Map)对象，<code>arg.size() == 0</code><br>
	 * 5. 如果<code>arg</code>是集合(Collection)对象，<code>arg.size() == 0</code><br>
	 * 6. 如果<code>arg</code>是数组(Array)对象，<code>arg.length == 0</code><br>
	 * 7. 如果<code>arg</code>是布尔(Boolean)对象，<code>arg 等价于  false</code><br>
	 *
	 * @return 上述无效的情况返回 <code>false</code>，其他情况均返回 <code>true</code>
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isValid(Object arg) {
		if (arg == null) {
			return false;
		} else if (arg instanceof CharSequence) {
			return ((CharSequence) arg).length() > 0;
		} else if (arg instanceof Number) {
			return ((Number) arg).doubleValue() != 0;
		} else if (arg instanceof Map) {
			return !((Map) arg).isEmpty();
		} else if (arg instanceof Collection) {
			return !((Collection) arg).isEmpty();
		} else if (arg.getClass().isArray()) {
			return Array.getLength(arg) > 0;
		} else if (arg instanceof Boolean) {
			return (Boolean) arg;
		}
		return true;
	}

	/**
	 * 将指定的值根据指定的表达式解析，并返回解析后的结果
	 *
	 * @param input 指定的值
	 * @param expressions 指定的表达式，例如：<code>("1", "男", "0", "女")</code>方法将会将指定属性的值(value)，与表达式进行匹配，形如：
	 *
	 * <pre>
	 * <code>
	 * if(value 等于 "1"){
	 * 	return "男";
	 * }else if(value 等于 "0"){
	 * 	return "女";
	 * }else{
	 * 	return value;
	 * }
	 * </code>
	 * </pre>
	 * <p>
	 * 本方法接收的表达式参数个数可以为奇数，例如：<code>(6, "星期六", 7, "星期天", "工作日")</code>，相当于：
	 * <pre>
	 * if(value 等于 6){
	 * 	return "星期六";
	 * }else if(value 等于 7){
	 * 	return "星期天";
	 * }else{
	 * 	return "工作日";
	 * }
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public static <T> T decode(Object input, Object... expressions) {
		int length;
		if (expressions == null || (length = expressions.length) == 0) {
			throw new IllegalArgumentException("decode的表达式参数个数不能小于1!");
		}
		Object val = null;
		int endIndex = length - 1;
		for (int i = 0; i < length; i++) {
			if (i == endIndex) { // 奇数
				val = expressions[i];
				break;
			}
			Object key = expressions[i++];
			if (Objects.equals(input, key)) {
				val = expressions[i];
				break;
			}
		}
		return (T) val;
	}

	/**
	 * 将指定泛型对象进行泛型擦除，并转换为对应的泛型声明
	 */
	@SuppressWarnings("unchecked")
	public static <T> T castType(Object obj) {
		return (T) obj;
	}

	/**
	 * 使用 {@code obj} 执行指定的调用
	 *
	 * @param obj 指定的对象，可以为 null
	 * @param consumer 指定的调用，如果 {@code obj} 为 null，则不执行该调用
	 */
	public static <T> void use(@Nullable T obj, Consumer<? super T> consumer) {
		if (obj != null) {
			consumer.accept(obj);
		}
	}

	/**
	 * 使用 {@code obj} 执行指定的调用
	 *
	 * @param obj 指定的对象，可以为 null
	 * @param filter 过滤器，只有 {code obj} 满足该条件，才会执行 {@code consumer}
	 * @param consumer 指定的调用，如果 {@code obj} 为 null，则不执行该调用
	 */
	public static <T> void use(@Nullable T obj, Predicate<T> filter, Consumer<? super T> consumer) {
		if (obj != null && filter.test(obj)) {
			consumer.accept(obj);
		}
	}

	/**
	 * 对指定的对象执行指定的 {@code mapper } 转换，安全地获得期望的转换结果
	 *
	 * @param obj 指定的对象，可以为 null
	 * @param mapper 转换器
	 * @return 如果 {@code obj == null } 则返回null，否则返回转换后的结果
	 * @since 2.3.0
	 */
	public static <T, R> R map(@Nullable T obj, Function<? super T, R> mapper) {
		if (obj == null || mapper == null) {
			return null;
		}
		return mapper.apply(obj);
	}

	/**
	 * 对指定的对象执行 {@code mapper }、{@code nestedMapper} 双重转换，安全地获得期望的转换结果
	 *
	 * @param obj 指定的对象，可以为 null
	 * @param mapper 转换器
	 * @param nestedMapper 嵌套的二次转换器
	 * @return 如果 {@code obj == null } 则返回 null，否则返回转换后的结果
	 * @since 2.6
	 */
	public static <T, R, E> E map(@Nullable T obj, Function<? super T, R> mapper, Function<R, E> nestedMapper) {
		return map(map(obj, mapper), nestedMapper);
	}

	/**
	 * 对指定的对象执行执行的 {@code mapper } 转换，安全地获得期望的转换结果
	 *
	 * @param obj 指定的对象，可以为 null
	 * @param mapper 转换器
	 * @param defaultIfNull 如果转换后的值为null，则返回该参数值
	 * @return 如果 {@code obj == null } 则返回null，否则返回转换后的结果
	 * @since 2.6
	 */
	public static <T, R> R mapElse(@Nullable T obj, Function<? super T, R> mapper, R defaultIfNull) {
		R val = map(obj, mapper);
		return val == null ? defaultIfNull : val;
	}

	/**
	 * 对指定的对象执行执行的 {@code mapper } 转换，安全地获得期望的转换结果
	 *
	 * @param obj 指定的对象，可以为 null
	 * @param mapper 转换器
	 * @param defaultIfNull 如果转换后的值为null，则返回该备用对象的返回值
	 * @return 如果 {@code obj == null } 则返回null，否则返回转换后的结果
	 * @since 2.6
	 */
	public static <T, R> R mapElseGet(@Nullable T obj, Function<? super T, R> mapper, Supplier<R> defaultIfNull) {
		R val = map(obj, mapper);
		return val == null ? defaultIfNull.get() : val;
	}

	/**
	 * 尝试拆箱可能由 {@link Supplier } 接口包装的实体对象
	 *
	 * @return 如果指定参数实现了 {@link Supplier } 接口，则调用 get() 方法 并返回其值；否则直接返回 该对象本身
	 * @since 2.3.0
	 */
	@SuppressWarnings("unchecked")
	public static <E> E tryUnwrap(@Nullable Object supplier) {
		if (supplier instanceof Supplier) {
			return ((Supplier<E>) supplier).get();
		}
		return (E) supplier;
	}

	/**
	 * 当指定 val 为 null 时，则返回替补 {@code other} 中的返回值，否则返回 val 本身
	 *
	 * @since 2.6
	 */
	public static <T> T getElse(@Nullable T val, Supplier<T> other) {
		return val == null ? other.get() : val;
	}

	/**
	 * 检测指定的对象在经过指定的转换后，是否符合指定的条件
	 *
	 * @param bean 指定的对象
	 * @param mapper 转换器
	 * @param matcher 条件判断器
	 * @since 2.3.0
	 */
	public static <T, R> boolean isMatch(@Nullable T bean, final Function<? super T, R> mapper, final Predicate<? super R> matcher) {
		final R val = map(bean, mapper);
		return matcher.test(val);
	}

	/**
	 * 将指定的异常伪装成运行时异常（实际还是抛出指定的异常，只不过底层会通过泛型擦除避免编译时检查）
	 *
	 * @param t 指定的异常
	 */
	public static RuntimeException sneakyThrow(Throwable t) {
		if (t == null) {
			throw new NullPointerException("t");
		}
		return sneakyThrow0(t);
	}

	@SuppressWarnings("unchecked")
	private static <T extends Throwable> T sneakyThrow0(Throwable t) throws T {
		throw (T) t;
	}

	/**
	 * 递归地调用对象的指定方法，直到返回的对象满足指定条件（如果 {@code root} 对象本身符合条件就返回其本身）
	 *
	 * @since 2.8
	 */
	public static <T> T getRecursively(T root, Function<T, T> recursion, Predicate<T> untilMatch) {
		T t = root;
		while (t != null) {
			if (untilMatch.test(t)) {
				return t;
			}
			t = recursion.apply(t);
		}
		return null;
	}

	/**
	 * 宽松地比较两个实体的指定属性值是否相等，并返回比较结果
	 *
	 * @return 相等 -> 1；如果有一方为 null -> 0；否则返回 -1
	 * @since 2.8
	 */
	public static <T> int eqLax(@Nullable T inputVal, @Nullable T currentVal) {
		if (inputVal == currentVal) {
			return 1;
		} else if (inputVal == null || currentVal == null) {
			return 0;
		} else {
			return inputVal.equals(currentVal) ? 1 : -1;
		}
	}

	/**
	 * 宽松地比较两个实体的指定属性值是否相等
	 *
	 * @return 相等 -> 1；如果有一方为 null -> 0；否则返回 -1
	 * @since 2.8
	 */
	public static <T, R> int eqLax(@Nullable T input, @Nullable T current, Function<T, R> getter) {
		R inputVal = X.map(input, getter), currentVal = X.map(current, getter);
		return eqLax(inputVal, currentVal);
	}

	/**
	 * 期望两个实体的指定属性值是相等的，并返回该值。 本方法允许其中一个属性值为 null，并会自动使用另一方的值进行填补。如果两者都不为 null 且不相等，则直接抛出非法参数异常
	 *
	 * @return 返回非空的属性值。如果两个属性值相等，优先返回 {@code current} 的属性值
	 * @since 2.8
	 */
	public static <T, R> R expectEquals(@Nullable T input, @Nullable T current, Function<T, R> getter, BiConsumer<T, R> setIfNull, @Nullable Object error) throws IllegalArgumentException {
		final R inputVal = X.map(input, getter), currentVal = X.map(current, getter);
		if (inputVal == currentVal) {
			return currentVal;
		} else if (inputVal == null) {
			setIfNull.accept(input, currentVal);
			return currentVal;
		} else if (currentVal == null) {
			setIfNull.accept(current, inputVal);
			return inputVal;
		} else if (inputVal.equals(currentVal)) {
			return currentVal;
		}
		String errorMsg = tryUnwrap(error);
		throw new IllegalArgumentException(errorMsg);
	}

	/**
	 * 期望两个实体的指定属性值是相等的，并返回该值。 本方法允许其中一个属性值为 null，并会自动使用另一方的值进行填补。如果两者都不为 null 且不相等，则直接抛出非法参数异常
	 *
	 * @return 返回非空的属性值。如果两个属性值相等，优先返回 {@code current} 的属性值
	 * @since 2.8
	 */
	public static <T, R> R expectEquals(@Nullable T input, @Nullable T current, Function<T, R> getter, BiConsumer<T, R> setIfNull) throws IllegalArgumentException {
		return expectEquals(input, current, getter, setIfNull, null);
	}

	/**
	 * 期望两个实体的指定属性值是相等的，并返回该值。 如果两者不相等且 {@code input} 为 null，则会自动使用 {@code old} 进行赋值操作。如果两者都不为 null 且不相等，则直接抛出非法参数异常
	 *
	 * @return 返回 {@code old} 的属性值
	 * @since 2.8
	 */
	public static <T, R> R expectEqualsBasedOld(@Nullable T input, @Nullable T old, Function<T, R> getter, Consumer<R> setOldIfInputNull, @Nullable Object error) throws IllegalArgumentException {
		final R currentVal = X.map(old, getter);
		if (input == old) {
			return currentVal;
		}
		final R inputVal = X.map(input, getter);
		if (inputVal == currentVal) {
			return currentVal;
		} else if (inputVal == null) {
			setOldIfInputNull.accept(currentVal);
			return currentVal;
		} else if (inputVal.equals(currentVal)) {
			return currentVal;
		}
		String errorMsg = tryUnwrap(error);
		throw new IllegalArgumentException(errorMsg);
	}

	/**
	 * 期望两个实体的指定属性值是相等的，并返回该值。 如果两者不相等且 {@code input} 为 null，则会自动使用 {@code old} 进行赋值操作。如果两者都不为 null 且不相等，则直接抛出非法参数异常
	 *
	 * @return 返回 {@code old} 的属性值
	 * @since 2.8
	 */
	public static <T, R> R expectEqualsBasedOld(@Nullable T input, @Nullable T old, Function<T, R> getter, Consumer<R> setOldIfInputNul) throws IllegalArgumentException {
		return expectEqualsBasedOld(input, old, getter, setOldIfInputNul, null);
	}

	/**
	 * 返回集合的长度，如果参数为null，则返回 0
	 *
	 * @since 2.8
	 */
	public static int size(final Collection<?> c) {
		return c == null ? 0 : c.size();
	}

	/**
	 * 返回 Map 的长度，如果参数为null，则返回 0
	 *
	 * @since 2.8
	 */
	public static int size(final Map<?, ?> map) {
		return map == null ? 0 : map.size();
	}

	/**
	 * 返回数组的长度，如果参数为null，则返回 0
	 *
	 * @since 2.8
	 */
	public static int size(final Object[] array) {
		return array == null ? 0 : array.length;
	}

	/**
	 * 返回数组的长度，如果参数为 {@code null}，则返回 0
	 *
	 * @throws IllegalArgumentException 如果 {@code array} 不是数组（可以为 {@code null}）
	 * @since 2.8
	 */
	public static int sizeOfArray(final Object array) throws IllegalArgumentException {
		return array == null ? 0 : Array.getLength(array);
	}

	/**
	 * 返回字符序列的长度，如果参数为null，则返回 0
	 *
	 * @since 2.8
	 */
	public static int size(final CharSequence cs) {
		return cs == null ? 0 : cs.length();
	}

	/**
	 * 尝试将空数组转为 null
	 */
	public static <T> T[] emptyToNull(T[] array) {
		return X.isValid(array) ? array : null;
	}

	/**
	 * 尝试将空集合转为 null
	 */
	public static <T extends Collection<?>> T emptyToNull(T c) {
		return X.isValid(c) ? c : null;
	}

	/**
	 * 尝试将空 Map 集合转为 null
	 */
	public static <T extends Map<?, ?>> T emptyToNull(T map) {
		return X.isValid(map) ? map : null;
	}

	/**
	 * 尝试将数组 args 中的元素依次应用到 mapper 中，并返回第一个符合 {@code matcher } 条件的返回值
	 */
	@SuppressWarnings("unchecked")
	public static <T, R> R mapAny(Function<? super T, ? extends R> mapper, Predicate<R> matcher, T... args) {
		for (T t : args) {
			R val = mapper.apply(t);
			if (matcher.test(val)) {
				return val;
			}
		}
		return null;
	}

}