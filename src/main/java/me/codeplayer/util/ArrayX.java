package me.codeplayer.util;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 用于对数组类型的数据(字节数组参见NumberUtil类)进行相应处理的工具类
 *
 * @author Ready
 * @since 2012-9-29
 */
public abstract class ArrayX {

	/**
	 * 判断指定对象是否为数组类型
	 *
	 * @param obj 指定的对象
	 */
	public static boolean isArray(Object obj) {
		return obj != null && obj.getClass().isArray();
	}

	/**
	 * 以指定的分隔符拼接数组元素，并追加到指定的StringBuilder中
	 *
	 * @param sb 指定的StringBuilder（如果为null，则内部自动创建）
	 * @param array 指定的数组
	 * @param delimiter 指定的分隔符
	 */
	public static StringBuilder join(@Nullable StringBuilder sb, Object array, String delimiter) {
		if (array == null) {
			throw new NullPointerException();
		}
		final int length = Array.getLength(array);
		if (length == 0) {
			return sb;
		}
		if (sb == null) {
			sb = new StringBuilder(((length + delimiter.length()) << 3) + 2);
		}
		// 如果是 int、long 及其包装类型时，需调用特定的 append 方法，避免循环装箱及调用 String.valueOf 的开销
		final Class<?> type = array.getClass().getComponentType();
		appendElement(sb, type, array, 0);
		for (int i = 1; i < length; i++) {
			appendElement(sb.append(delimiter), type, array, i);
		}
		return sb;
	}

	static void appendElement(final StringBuilder sb, final Class<?> elementType, Object array, int i) {
		if (elementType == int.class) {
			sb.append(Array.getInt(array, i));
		} else if (elementType == long.class) {
			sb.append(Array.getLong(array, i));
		} else {
			Object val = Array.get(array, i);
			if (val != null) {
				if (elementType == Integer.class) {
					sb.append((int) val);
					return;
				} else if (elementType == Long.class) {
					sb.append((long) val);
					return;
				}
			}
			sb.append(val);
		}
	}

	/**
	 * 以指定的分隔符拼接数组元素并返回拼接后的字符串<br>
	 * 如果数组为空，将会引发异常
	 *
	 * @param array 指定的数组对象
	 * @param delimiter 指定的分隔符
	 */
	public static String join(Object array, String delimiter) {
		StringBuilder sb = join(null, array, delimiter);
		return sb == null ? "" : sb.toString();
	}

	/**
	 * 将指定字符串数组拼接为 IN SQL子句 <br>
	 * 如果数组为空，将会引发异常 <br>
	 * 如果存在数组元素，则拼接内容形如 " IN (1, 2, 5)" 或 " IN ('1', '2', '5')"
	 *
	 * @param sb 指定的StringBuilder
	 * @param array 指定的任意数组
	 * @param isInclude 指示IN SQL是包含还是排除查询，如果是包含(true)将返回 IN，如果是排除(false)将返回 NOT IN
	 * @param isString 指示元素是否以字符串形式参与in SQL语句。如果为true，将会在每个元素两侧加上单引号"'"
	 */
	public static StringBuilder getInSQL(StringBuilder sb, Object array, boolean isInclude, boolean isString) {
		final int length = Array.getLength(array);
		if (length == 0) {
			throw new IllegalArgumentException("Array can not be empty:" + array);
		}
		if (sb == null) {
			int factor = 8;
			if (isString) {
				factor += 2;
			}
			sb = new StringBuilder(length * factor + (isInclude ? 4 : 8));
		}
		sb.append(isInclude ? " IN (" : " NOT IN (");
		if (isString) {// 如果是字符串格式
			sb.append('\'');
			join(sb, array, "', '");
			sb.append("')");
		} else {// 如果是数字格式
			join(sb, array, ", ");
			sb.append(')');
		}
		return sb;
	}

	/**
	 * 将指定的数组拼接为InSQL子句并返回，内部将会根据元素个数来判断返回“=”语句还是“IN”语句<br>
	 * 如果数组为空，将会引发异常<br>
	 * 如果数组元素只有一个，将会返回“=1”或“='1'”<br>
	 * 如果数组元素有多个，将会返回“ IN (1, 2, 5)”或“ IN ('1', '2', '5')”
	 *
	 * @param array 指定的数组
	 * @param isInclude 指示IN SQL是包含还是排除查询，如果是包含(true)将返回=、IN，如果是排除(false)将返回!=、NOT IN
	 * @param isString 指示元素是否以字符串形式参与InSQL语句。如果为true，将会在每个元素两侧加上单引号"'"
	 */
	public static String getInSQL(Object array, boolean isInclude, boolean isString) {
		return getInSQL(null, array, isInclude, isString).toString();
	}

	/**
	 * 将指定的数组拼接为InSQL子句并返回，内部将会根据元素个数来判断返回“=”语句还是“IN”语句<br>
	 * 如果数组为空，将会引发异常<br>
	 * 如果数组元素只有一个，将会返回“=1”或“='1'”<br>
	 * 如果数组元素有多个，将会返回“ IN (1, 2, 5)”或“ IN ('1', '2', '5')”
	 *
	 * @param array 指定的数组
	 * @param isString 指示元素是否以字符串形式参与InSQL语句。如果为true，将会在每个元素两侧加上单引号"'"
	 */
	public static String getInSQL(Object array, boolean isString) {
		return getInSQL(null, array, true, isString).toString();
	}

	/**
	 * 返回指定数组对象的字符串形式<br>
	 * 如果<code>array</code>是一个数组，则迭代其元素返回字符串，如“[e1, e2, e3, e4]”<br>
	 * 如果<code>array</code>不是一个数组，则直接调用String.valueOf()方法返回
	 */
	public static String toString(Object array) {
		if (array == null || !array.getClass().isArray()) {
			return String.valueOf(array);
		} else {
			int length = Array.getLength(array);
			if (length == 0) {
				return "[]";
			} else {
				int capacity = length << 3;
				if (capacity < 16) {
					capacity = 16;
				}
				StringBuilder sb = new StringBuilder(capacity);
				sb.append('[').append(Array.get(array, 0));
				for (int i = 1; i < length; i++) {
					sb.append(", ").append(Array.get(array, i));
				}
				sb.append(']');
				return sb.toString();
			}
		}
	}

	/**
	 * 迭代数组元素并将迭代字符串追加至StringBuilder中,追加字符串形如：“[e1, e2, e3, [e4_1, e4_2, e4_3, e4_4]]<br>
	 * 本方法可以迭代多维数组，内部采用递归算法
	 *
	 * @param sb 指定的StringBuilder
	 * @param array 指定的数组对象
	 */
	public static StringBuilder toFinalString(StringBuilder sb, Object array) {
		if (array == null || !array.getClass().isArray()) {
			if (sb == null) {
				sb = new StringBuilder();
			}
			sb.append(array);
		} else {
			int length = Array.getLength(array);
			if (length == 0) {
				if (sb == null) {
					sb = new StringBuilder();
				}
				sb.append("[]");
			} else {
				if (sb == null) {
					sb = new StringBuilder(length << 3);
				}
				sb.append('[');
				toFinalString(sb, Array.get(array, 0));
				for (int i = 1; i < length; i++) {
					sb.append(", ");
					toFinalString(sb, Array.get(array, i));
				}
				sb.append(']');
			}
		}
		return sb;
	}

	/**
	 * 迭代数组元素并返回迭代字符串，例如：“[e1, e2, e3, [e4_1, e4_2, e4_3, e4_4]]”<br>
	 * 本方法可以迭代多维数组，内部采用递归算法
	 *
	 * @param array 指定的数组对象
	 */
	public static String toFinalString(Object array) {
		return toFinalString(null, array).toString();
	}

	/**
	 * 判断指定数组是否不为null并且数组长度<code>length > 0</code>，如果是，则返回true<br>
	 * 如果指定参数不为null，也不是数组类型，则引发异常
	 *
	 * @param array 指定的数组
	 */
	public static boolean hasLength(Object array) {
		return array != null && Array.getLength(array) > 0;
	}

	/**
	 * 获取指定数组元素的长度，如果数组为null将返回0，如果不是数组类型，将引发异常
	 *
	 * @param array 指定的数组
	 * @param assertNotEmpty 当数组为null或数组长度为0时，是否触发异常，如果为true，则触发异常
	 */
	public static int getLength(Object array, boolean assertNotEmpty) {
		int length = getLength(array);
		if (length == 0 && assertNotEmpty) {
			throw new IllegalArgumentException("Array can not be empty:" + array);
		}
		return length;
	}

	/**
	 * 获取指定数组元素的长度，如果指定的参数为null或长度为0，则返回0<br>
	 * 如果指定参数不是数组类型，将引发异常
	 *
	 * @param array 指定的数组对象
	 */
	public static int getLength(@Nullable Object array) {
		return array == null ? 0 : Array.getLength(array);
	}

	/**
	 * 获取指定数组元素的长度，如果指定的参数为null或长度为0，则返回0
	 *
	 * @param array 指定的数组对象
	 */
	public static int getLength(@Nullable Object[] array) {
		return array == null ? 0 : array.length;
	}

	/**
	 * 获取指定数组元素的"维度"，如果数组为普通的一维数组，则返回1；如果为二维数组，则返回2；以此类推...<br>
	 * 如果不是数组，则返回-1<br>
	 * <b>注意：</b>Java没有真正意义上的多维数组，只有嵌套数组
	 *
	 * @param array 指定的数组对象
	 * @author Ready
	 * @since 0.3.1
	 */
	public static int getDimension(Object array) {
		if (array != null) {
			Class<?> clazz = array.getClass();
			if (clazz.isArray()) {
				String className = clazz.getName();
				int len = className.length();
				for (int i = 0; i < len; i++) {
					if (className.charAt(i) != '[') {
						return i;
					}
				}
			}
		}
		return -1;
	}

	/**
	 * 判断指定对象是否是一个原始类型的数组(例如：int[]、float[]、char[]、double[]等)
	 *
	 * @param array 指定的对象
	 * @since 0.3.1
	 */
	public static boolean isPrimitiveArray(Object array) {
		if (array != null) {
			final Class<?> clazz = array.getClass();
			if (clazz.isArray()) {
				// 获取数组的元素类型
				Class<?> componentType = clazz.getComponentType();
				// 判断元素类型是否为基本数据类型
				return componentType.isPrimitive();
			}
		}
		return false;
	}

	/**
	 * 检测指定的整数数组中是否存在指定的整数值
	 *
	 * @since 0.3.5
	 */
	public static boolean in(int value, int... array) {
		return ArrayUtils.indexOf(array, value) != ArrayUtils.INDEX_NOT_FOUND;
	}

	/**
	 * 检测指定的数组中是否存在指定的值
	 *
	 * @since 0.3.5
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean ins(T value, T... array) {
		if (value == null) {
			for (T t : array) {
				if (t == null) {
					return true;
				}
			}
		} else {
			for (T t : array) {
				if (value.equals(t)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 检测指定的数组中是否存在指定的值
	 *
	 * @since 4.0.0
	 */
	public static <T> boolean ins(T value, T v1, T v2) {
		return value == null
				? v1 == null || v2 == null
				: value.equals(v1) || value.equals(v2);
	}

	/**
	 * 检测指定的数组中是否存在指定的值
	 *
	 * @since 4.0.0
	 */
	public static <T> boolean ins(T value, T v1, T v2, T v3) {
		return value == null
				? v1 == null || v2 == null || v3 == null
				: value.equals(v1) || value.equals(v2) || value.equals(v3);
	}

	/**
	 * 检测指定的数组中是否存在指定的值
	 *
	 * @since 4.0.0
	 */
	public static <T> boolean ins(T value, T v1, T v2, T v3, T v4) {
		return value == null
				? v1 == null || v2 == null || v3 == null || v4 == null
				: value.equals(v1) || value.equals(v2) || value.equals(v3) || value.equals(v4);
	}

	/**
	 * 查找指定对象在已经排序好的区间临界值数组中的区间索引。
	 * <p>
	 * 数组{@code array }必须预先排序好，可以是升序或降序。例如：{@code [5, 10, 20, 50, 100] }。 此时，如果 {@code toCompare = 4 }，则返回 -1；如果 {@code toCompare = 5 }，则返回 0；如果 {@code toCompare = 12 }，则返回 1。
	 * <p>
	 * 如果数组中存在相等的值，则返回最靠近最大值的区间索引。
	 *
	 * @param sortedArray 区间临界值数组
	 * @param toCompare 指定的对象
	 * @param ascOrDesc 指定区间临界值数组的排序方式： true 表示升序， false 表示 降序；null 则自动根据数组中前两个元素的比较结果智能判断排序方式
	 * @return 返回对应的区间索引。如果不满足最小的区间临界值，则返回 -1
	 * @throws IllegalArgumentException 如果数组前两个元素的大小相等，则抛出该异常
	 * @since 1.0.4
	 */
	public static <T extends Comparable<T>> int indexOfInterval(@Nullable T[] sortedArray, T toCompare, @Nullable Boolean ascOrDesc) throws IllegalArgumentException {
		if (sortedArray == null || sortedArray.length == 0) {
			return -1;
		}
		boolean orderByAsc;
		if (ascOrDesc == null) {
			int result;
			if (sortedArray.length == 1 || (result = sortedArray[0].compareTo(sortedArray[1])) == 0) {
				throw new IllegalArgumentException("Unable to determine the sort order");
			}
			orderByAsc = result < 0;
		} else {
			orderByAsc = ascOrDesc;
		}
		int index = -1;
		// fn( [ 1, 3, 5 ], 0, true ) = -1
		// fn( [ 1, 3, 5 ], 1, true ) = 0
		// fn( [ 1, 3, 5 ], 2, true ) = 0
		// fn( [ 1, 3, 5 ], 3, true ) = 1
		// fn( [ 1, 3, 5 ], 5, true ) = 2
		// fn( [ 1, 3, 5 ], 6, true ) = 2
		if (orderByAsc) {
			for (int i = 0; i < sortedArray.length && toCompare.compareTo(sortedArray[i]) >= 0; i++) {
				index = i;
			}
		} else {
			// fn( [ 5, 3, 1 ], 6, false ) = -1
			// fn( [ 5, 3, 1 ], 5, false ) = 0
			// fn( [ 5, 3, 1 ], 4, false ) = 0
			// fn( [ 5, 3, 1 ], 3, false ) = 1
			// fn( [ 5, 3, 1 ], 1, false ) = 2
			// fn( [ 5, 3, 1 ], 0, false ) = 2
			for (int i = 0; i < sortedArray.length && toCompare.compareTo(sortedArray[i]) <= 0; i++) {
				index = i;
			}
		}
		return index;
	}

	/**
	 * 移除数组里的重复元素，使其唯一化。如果存在重复的元素，则只保留排序最靠前(索引较小)的那个元素
	 *
	 * @param array 指定的数组对象
	 * @param newArrayRequired 是否必须返回新的数组副本。如果为 {@code false}，在没有找到重复元素时，将会直接返回原数组 {@code array}
	 */
	public static Object unique(Object array, final boolean newArrayRequired) {
		final int length = Array.getLength(array);
		if (length > 1) {
			final Map<Object, Boolean> map = CollectionX.newLinkedHashMap(length);
			for (int i = 0; i < length; i++) {
				map.putIfAbsent(Array.get(array, i), Boolean.TRUE);
			}
			final int size = map.size();
			if (size < length) {
				final Class<?> componentType = array.getClass().getComponentType();
				final Object copy = Array.newInstance(componentType, size);
				int i = 0;
				for (Object ele : map.keySet()) {
					Array.set(copy, i++, ele);
				}
				return copy;
			}
		}
		if (newArrayRequired) {
			final Class<?> componentType = array.getClass().getComponentType();
			Object copy = Array.newInstance(componentType, length);
			System.arraycopy(array, 0, copy, 0, length);
			return copy;
		}
		return array;
	}

	/**
	 * 移除数组里的元素唯一化。如果存在重复的元素，则只保留排序最靠前(索引较小)的那个元素
	 *
	 * @param array 指定的数组对象
	 * @see #unique(Object, boolean)
	 */
	public static Object unique(Object array) {
		return unique(array, true);
	}

	/**
	 * 将指定的集合转为对应的数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(@Nullable final Collection<T> c, Class<T> type) {
		if (c == null) {
			return null;
		}
		final int size = c.size();
		final T[] array = (T[]) Array.newInstance(type, size);
		if (size > 0) {
			c.toArray(array);
		}
		return array;
	}

	/**
	 * 将指定的集合中的元素经过 {@code mapper} 转换后，转为对应类型的数组
	 *
	 * @return 只有 {@code items} 是 {@code null} 时，才返回 {@code null}
	 * @since 3.0.0
	 */
	public static <T, R> R[] toArray(@Nullable Iterable<T> items, Class<R> type, Function<? super T, R> mapper) {
		if (items == null) {
			return null;
		}
		if (items instanceof Collection) {
			final int size = ((Collection<T>) items).size();
			final R[] array = X.castType(Array.newInstance(type, size));
			int i = 0;
			for (T item : items) {
				array[i++] = mapper.apply(item);
			}
			return array;
		} else {
			List<R> list = new ArrayList<>();
			for (T item : items) {
				list.add(mapper.apply(item));
			}
			return toArray(list, type);
		}
	}

	/**
	 * 快捷创建 Object 数组
	 */
	public static Object[] of(Object... elements) {
		return elements;
	}

	/**
	 * 过滤指定的数组，获得符合条件的元素数组
	 *
	 * @param array 指定的数组
	 * @param matcher 只有经过该过滤器后返回 true 的元素才符合条件
	 * @since 2.0.0
	 */
	public static <E> E[] filter(final E[] array, final Predicate<? super E> matcher) {
		final E[] newAarray = array.clone();
		int count = 0;
		for (E e : newAarray) {
			if (matcher.test(e)) {
				newAarray[count++] = e;
			}
		}
		return count == newAarray.length ? newAarray : Arrays.copyOf(newAarray, count);
	}

	/**
	 * 将单个元素构造成一个仅包含该元素的数组
	 *
	 * @param element 指定的元素值
	 * @return 如果 val == null，则返回 null；否则返回对应类型的数组
	 * @since 2.1.0
	 */
	@SuppressWarnings("unchecked")
	public static <E> E[] ofNullable(E element) {
		if (element == null) {
			return null;
		}
		Object array = Array.newInstance(element.getClass(), 1);
		Array.set(array, 0, element);
		return (E[]) array;
	}

	/**
	 * 将单个元素构造成一个仅包含该元素的数组
	 *
	 * @return null
	 * @since 3.5.0
	 */
	public static <E> E[] ofNull() {
		return null;
	}

	/**
	 * 指示指定的参数是否至少有一个符合指定的 {@code matcher} 条件
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean matchAny(Predicate<T> matcher, T... values) {
		for (T val : values) {
			if (matcher.test(val)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 指示指定的参数是否都符合指定的 {@code matcher} 条件
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean matchAll(Predicate<T> matcher, T... values) {
		for (T val : values) {
			if (!matcher.test(val)) {
				return false;
			}
		}
		return values.length > 0;
	}

}