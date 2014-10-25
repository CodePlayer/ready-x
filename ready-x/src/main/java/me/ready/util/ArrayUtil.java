package me.ready.util;

import java.lang.reflect.Array;

import me.ready.e.LogicException;

/**
 * 用于对数组类型的数据(字节数组参见NumberUtil类)进行相应处理的工具类
 * @author Ready
 * @date 2012-9-29
 */
public class ArrayUtil {

	// 禁止实例创建
	private ArrayUtil() {
	}

	/**
	 * 长度为0的对象数组
	 */
	public static final Object[] EMPTY_OBJECTS = new Object[0];
	/**
	 * 长度为0的字符串数组
	 */
	public static final String[] EMPTY_STRINGS = new String[0];
	/**
	 * 长度为0的int数组
	 */
	public static final int[] EMPTY_INTS = new int[0];
	/**
	 * 长度为0的Integer数组
	 */
	public static final Integer[] EMPTY_INTEGERS = new Integer[0];

	/**
	 * 判断指定对象是否为数组类型
	 * @param obj 指定的对象
	 * @return
	 */
	public static final boolean isArray(Object obj) {
		return obj != null && obj.getClass().isArray();
	}

	/**
	 * 以指定的分隔符拼接数组元素，并追加到指定的StringBuilder中<br>
	 * 如果数组为空，将会引发异常
	 * @param sb 指定的StringBuilder
	 * @param array 指定的数组
	 * @param delimiter 指定的分隔符
	 */
	public static StringBuilder join(StringBuilder sb, Object array, String delimiter) {
		if (array == null || !array.getClass().isArray()) {
			throw new LogicException("指定的对象不是数组类型的对象!");
		}
		int length;
		if ((length = Array.getLength(array)) == 0) {
			throw new LogicException("指定的数组对象的长度必须大于0!");
		}
		sb.append(Array.get(array, 0));
		for (int i = 1; i < length; i++) {
			sb.append(delimiter);
			sb.append(Array.get(array, i));
		}
		return sb;
	}

	/**
	 * 以指定的分隔符拼接数组元素并返回拼接后的字符串<br>
	 * 如果数组为空，将会引发异常
	 * @param array 指定的数组对象
	 * @param delimiter 指定的分隔符
	 * @return
	 */
	public static String join(Object array, String delimiter) {
		return join(new StringBuilder(), array, delimiter).toString();
	}

	/**
	 * 将指定字符串数组拼接为InSQL子句，方法将会根据元素个数来判断内容为“=”语句还是“IN”语句<br>
	 * 如果数组为空，将会引发异常<br>
	 * 如果数组元素只有一个，拼接内容为“=1”或“='1'”<br>
	 * 如果数组元素有多个，拼接内容为“ IN (1, 2, 5)”或“ IN ('1', '2', '5')”
	 * @param sb 指定的StringBuilder
	 * @param array 指定的任意数组
	 * @param isInclude 指示IN SQL是包含还是排除查询，如果是包含(true)将返回=、IN，如果是排除(false)将返回!=、NOT IN
	 * @param isString 指示元素是否以字符串形式参与InSQL语句。如果为true，将会在每个元素两侧加上单引号"'"
	 */
	public static StringBuilder getInSQL(StringBuilder sb, Object array, boolean isInclude, boolean isString) {
		if (array == null || !array.getClass().isArray()) {
			throw new LogicException("指定的参数对象必须为数组类型!");
		}
		int length = Array.getLength(array);
		if (sb == null && length > 0) {
			sb = StringUtil.getBuilder(length, 3);
		}
		switch (length) {
		case 0:
			throw new LogicException("指定的数组对象的长度必须大于0!");
		case 1:
			if (!isInclude) {
				sb.append('!');
			}
			if (isString) {
				sb.append("='").append(Array.get(array, 0)).append('\'');
			} else {
				sb.append('=').append(Array.get(array, 0));
			}
			break;
		default:
			sb.append(isInclude ? " IN (" : " NOT IN (");
			if (isString) {// 如果是字符串格式
				sb.append('\'');
				join(sb, array, "', '");
				sb.append("')");
			} else {// 如果是数字格式
				join(sb, array, ", ");
				sb.append(')');
			}
			break;
		}
		return sb;
	}

	/**
	 * 将指定的数组拼接为InSQL子句并返回，内部将会根据元素个数来判断返回“=”语句还是“IN”语句<br>
	 * 如果数组为空，将会引发异常<br>
	 * 如果数组元素只有一个，将会返回“=1”或“='1'”<br>
	 * 如果数组元素有多个，将会返回“ IN (1, 2, 5)”或“ IN ('1', '2', '5')”
	 * @param array 指定的数组
	 * @param isInclude 指示IN SQL是包含还是排除查询，如果是包含(true)将返回=、IN，如果是排除(false)将返回!=、NOT IN
	 * @param isString 指示元素是否以字符串形式参与InSQL语句。如果为true，将会在每个元素两侧加上单引号"'"
	 * @return
	 */
	public static String getInSQL(Object array, boolean isInclude, boolean isString) {
		return getInSQL(null, array, isInclude, isString).toString();
	}

	/**
	 * 将指定的数组拼接为InSQL子句并返回，内部将会根据元素个数来判断返回“=”语句还是“IN”语句<br>
	 * 如果数组为空，将会引发异常<br>
	 * 如果数组元素只有一个，将会返回“=1”或“='1'”<br>
	 * 如果数组元素有多个，将会返回“ IN (1, 2, 5)”或“ IN ('1', '2', '5')”
	 * @param array 指定的数组
	 * @param isString 指示元素是否以字符串形式参与InSQL语句。如果为true，将会在每个元素两侧加上单引号"'"
	 * @return
	 */
	public static String getInSQL(Object array, boolean isString) {
		return getInSQL(null, array, true, isString).toString();
	}

	/**
	 * 返回指定数组对象的字符串形式<br>
	 * 如果<code>array</code>是一个数组，则迭代其元素返回字符串，如“[e1, e2, e3, e4]”<br>
	 * 如果<code>array</code>不是一个数组，则直接调用String.valueOf()方法返回
	 * @param array
	 * @return
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
	 * @param sb 指定的StringBuilder
	 * @param array 指定的数组对象
	 * @return
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
					sb = StringUtil.getBuilder(length, 3);
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
	 * @param array 指定的数组对象
	 * @return
	 */
	public static String toFinalString(Object array) {
		return toFinalString(null, array).toString();
	}

	/**
	 * 判断指定数组是否不为null并且数组长度<code>length > 0</code>，如果是，则返回true<br>
	 * 如果指定参数不为null，也不是数组类型，则引发异常
	 * @param array 指定的数组
	 * @return
	 */
	public static final boolean hasLength(Object array) {
		return array != null && Array.getLength(array) > 0;
	}

	/**
	 * 获取指定数组元素的长度，如果数组为null将返回0，如果不是数组类型，将引发异常
	 * @param array 指定的数组
	 * @param triggerError 当数组为null或数组长度为0时，是否触发异常，如果为true，则触发异常
	 * @return
	 */
	public static final int getLength(Object array, boolean triggerError) {
		int length = getLength(array);
		if (length == 0 && triggerError) {
			throw new LogicException("指定的参数必须是数组类型，并且长度不能为0!");
		}
		return length;
	}

	/**
	 * 获取指定数组元素的长度，如果指定的参数为null或长度为0，则返回0<br>
	 * 如果指定参数不是数组类型，将引发异常
	 * @param array 指定的对象数组
	 * @return
	 */
	public static final int getLength(Object array) {
		if (array == null) {
			return 0;
		}
		return Array.getLength(array);
	}
}
