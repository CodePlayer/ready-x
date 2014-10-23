package me.ready.util;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import me.ready.e.LogicException;

/**
 * 通用公共工具类<br>
 * 此类全为静态方法，请使用静态方法的形式调用<br>
 * 由于均为静态方法，所以类名尽可能地短，以免干扰逻辑可读性<br>
 * 并且工具类会经常被调用，类名短，也方便开发人员编写。
 * 
 * @author Ready
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class X {

	/**
	 * 判断指定的字符串是否为空<br>
	 * 如果字符串为null、空字符串,则返回true<br>
	 * <b>注意：</b>本方法不会去除字符串两边的空格，如果想去除字符串两边的空格后再进行判断，可以使用isBlank()方法
	 * 
	 * @param str
	 * @return
	 */
	public static final boolean isEmpty(String str) {
		return str == null || str.length() == 0; // 后面的表达式相当于"".equals(str)，但比其性能稍好
	}

	/**
	 * 判断指定的对象是否为空<br>
	 * 如果对象(或其toSring()返回值)为null、空字符串，则返回true<br>
	 * <b>注意：</b>本方法不会去除字符串两边的空格，如果想去除字符串两边的空格后再进行判断，可以使用isBlank()方法
	 * 
	 * @param obj 指定的对象
	 * @return
	 */
	public static final boolean isEmpty(Object obj) {
		return obj == null || obj.toString().length() == 0; // 后面的表达式相当于"".equals(str)，但比其性能稍好
	}

	/**
	 * 判断指定的字符串是否为null或去空格后为空字符串，如果是则返回true
	 * 
	 * @param str 指定的字符串对象
	 * @return
	 */
	public static final boolean isBlank(String str) {
		return str == null || str.toString().trim().length() == 0;
	}

	/**
	 * 判断指定的对象是否为空<br>
	 * 如果对象(或其toSring()返回值)为null、空字符串、空格字符串，则返回true<br>
	 * <b>注意：</b>本方法会先去除字符串两边的空格，再判断
	 * 
	 * @param obj 指定的对象
	 * @return
	 * @see easymapping.util.StringUtil#isBlank(Object)
	 */
	public static final boolean isBlank(Object obj) {
		return StringUtil.isBlank(obj);
	}

	/**
	 * 返回指定的对象，如果该对象为空，则返回指定的默认值
	 * 
	 * @param <T>
	 * @param t 指定的对象
	 * @param defaultValue 指定的默认值
	 * @return
	 * @see easymapping.util.StringUtil#isEmpty(Object)
	 */
	public static final <T> T getDefault(T t, T defaultValue) {
		return StringUtil.isEmpty(t) ? defaultValue : t;
	}

	/**
	 * 判断Boolean类型是否为null或false，如果是，则返回true
	 * 
	 * @param b 指定的Boolean对象
	 * @return
	 */
	public static final boolean isInvalid(Boolean b) {
		return b == null || !b.booleanValue();
	}

	/**
	 * 判断指定的数值对象是否等于null或0，如果是，则返回true
	 * 
	 * @param number 指定的
	 * @return
	 */
	public static final boolean isInvalid(Number number) {
		return number == null || number.doubleValue() == 0;
	}

	/**
	 * 判断指定的字符串序列是否为null或去空格后为空字符串，如果是则返回true
	 * 
	 * @param sequence 指定的字符串序列对象
	 * @return
	 */
	public static final boolean isInvalid(CharSequence sequence) {
		return isBlank(sequence);
	}

	/**
	 * 判断指定的Map对象是否为null或<code>size() == 0</code>，如果是，则返回true
	 * 
	 * @param map 指定的映射集合对象
	 * @return
	 */
	public static final boolean isInvalid(Map<?, ?> map) {
		return map == null || map.size() == 0;
	}

	/**
	 * 判断指定的Collection对象是否为null或<code>size() == 0</code>，如果是，则返回true
	 * 
	 * @param collection 指定的集合对象
	 * @return
	 */
	public static final boolean isInvalid(Collection<?> collection) {
		return collection == null || collection.size() == 0;
	}

	/**
	 * 判断指定byte数组是否为null或者<code>length == 0</code>，如果是，则返回true
	 * 
	 * @param array 指定的byte数组
	 * @return
	 */
	public static final boolean isInvalid(byte[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 判断指定int数组是否为null或者<code>length == 0</code>，如果是，则返回true
	 * 
	 * @param array 指定的int数组
	 * @return
	 */
	public static final boolean isInvalid(int[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 判断指定long数组是否为null或者<code>length == 0</code>，如果是，则返回true
	 * 
	 * @param array 指定的long数组
	 * @return
	 */
	public static final boolean isInvalid(long[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 判断指定char数组是否为null或者<code>length == 0</code>，如果是，则返回true
	 * 
	 * @param array 指定的char数组
	 * @return
	 */
	public static final boolean isInvalid(char[] intArray) {
		return intArray == null || intArray.length == 0;
	}

	/**
	 * 判断指定float数组是否为null或者<code>length == 0</code>，如果是，则返回true
	 * 
	 * @param array 指定的float数组
	 * @return
	 */
	public static final boolean isInvalid(float[] intArray) {
		return intArray == null || intArray.length == 0;
	}

	/**
	 * 判断指定double数组是否为null或者<code>length == 0</code>，如果是，则返回true
	 * 
	 * @param array 指定的double数组
	 * @return
	 */
	public static final boolean isInvalid(double[] intArray) {
		return intArray == null || intArray.length == 0;
	}

	/**
	 * 判断指定的对象数组是否为null或<code>length == 0</code>，如果是，则返回true
	 * 
	 * @param collection 指定的对象数组
	 * @return
	 */
	public static final boolean isInvalid(Object[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 判断指定对象是否为无效对象，如果是，则返回true<br>
	 * 无效对象obj的定义如下(按照判断顺序排序)： <br>
	 * 1.<code>obj == null</code><br>
	 * 2.如果<code>obj</code>是字符序列对象，去空格后，<code>length() == 0</code><br>
	 * 3.如果<code>obj</code>是数值对象，去空格后，<code>值  == 0</code><br>
	 * 4.如果<code>obj</code>是映射集合(Map)对象，<code>size() == 0</code><br>
	 * 5.如果<code>obj</code>是集合(Collection)对象，<code>size() == 0</code><br>
	 * 6.如果<code>obj</code>是数组(Array)对象，<code>length == 0</code><br>
	 * 7.如果<code>obj</code>是布尔(Boolean)对象，<code>obj 为  false</code><br>
	 * 8.上述情况返回true，其他情况返回false
	 * 
	 * @param obj
	 * @return
	 */
	public static final boolean isInvalid(Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof CharSequence) {
			return ((CharSequence) obj).toString().trim().length() == 0;
		} else if (obj instanceof Number) {
			return ((Number) obj).doubleValue() == 0;
		} else if (obj instanceof Map) {
			return ((Map) obj).size() == 0;
		} else if (obj instanceof Collection) {
			return ((Collection) obj).size() == 0;
		} else if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		} else if (obj instanceof Boolean) {
			return !((Boolean) obj).booleanValue();
		} else {
			return false;
		}
	}

	/**
	 * 如果指定字符串超过限制长度<code>maxLength</code>,则返回限制长度前面的部分字符串<br>
	 * 如果指定字符串==null，则返回空字符串<br>
	 * 如果字符串超出指定长度，则返回maxLength前面的部分，并在末尾加上后缀<code>suffix</code>
	 * 
	 * @param str 指定的字符串
	 * @param maxLength 最大限制长度
	 * @param suffix 超出长度时添加的指定后缀,如果不需要，可以为null
	 * @return
	 */
	public static final String limitChars(String str, int maxLength, String suffix) {
		return StringUtil.limitChars(str, maxLength, suffix);
	}

	/**
	 * 将指定的字段名按照JavaBean规范形式转为对应的getter方法名。例如：<br>
	 * <code>field2GetterName("name")</code> 将返回<code>"getName"</code><br>
	 * <code>field2GetterName("CPU")</code> 将返回<code>"getCPU"</code>
	 * 
	 * @param fieldName 指定的字段名
	 * @return
	 */
	public static final String field2GetterName(String fieldName) {
		return field2MethodName("get", fieldName);
	}

	/**
	 * 将指定的字段名按照JavaBean规范形式转为对应的setter方法名。例如：<br>
	 * <code>field2SetterName("name")</code> 将返回<code>"setName"</code><br>
	 * <code>field2SetterName("CPU")</code> 将返回<code>"setCPU"</code>
	 * 
	 * @param fieldName 指定的字段名
	 * @return
	 */
	public static final String field2SetterName(String fieldName) {
		return field2MethodName("set", fieldName);
	}

	/**
	 * 将指定的字段名按照JavaBean规范形式转为对应的部分方法名。例如：<br>
	 * <code>field2MethodName("get", "name")</code> 将返回<code>"getName"</code><br>
	 * <code>field2MethodName("set", "CPU")</code> 将返回<code>"setCPU"</code>
	 * 
	 * @param prefix 方法名前缀
	 * @param fieldName 指定的字段名
	 * @return
	 */
	public static final String field2MethodName(String prefix, String fieldName) {
		int length;
		if (fieldName == null || (length = fieldName.length()) == 0) {
			throw new LogicException("无效的字段名!");
		}
		if (length == 1) {
			return prefix + fieldName.toUpperCase();
		} else {
			return Character.isUpperCase(fieldName.charAt(1)) ? prefix + fieldName : prefix + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
		}
	}

	/**
	 * 将指定的Map或JavaBean中指定属性的值按照指定的表达式进行解析，并返回对应的值
	 * 
	 * @param bean 指定的Map或JavaBean，如果为null，则返回null；如果不符合Map或JavaBean条件，则抛出异常
	 * @param property指定的属性名或key，如果不存在，将会抛出异常
	 * @param expressions 指定的表达式，例如：<code>"1", "男", "0", "女"</code><br>
	 *            方法将会将指定属性的值(value)，与表达式进行匹配，形如：<br>
	 *            <code>
	 * if(value 等于 "1"){<br>
	 * 		return "男";<br>
	 * }else if(value 等于 "0"){<br>
	 * 		return "女";<br>
	 * }else{<br>
	 * 		return value;<br>
	 * }
	 * </code><br>
	 *            本方法接收的表达式参数个数可以为奇数，例如：<code>6, "星期六", 7, "星期天", "工作日"</code><br>
	 *            相当于：<br>
	 *            if(value 等于 6){<br>
	 *            return "星期六";<br>
	 *            }else if(value 等于 7){<br>
	 *            return "星期天";<br>
	 *            }else{<br>
	 *            return "工作日";<br>
	 *            }
	 * @return
	 */
	public static final Object decode(Object bean, String property, Object... expressions) {
		if (bean == null) return null;
		Object value = null;
		if (bean instanceof Map) {
			Map map = (Map) bean;
			value = map.get(property);
			if (value == null && !map.containsKey(property)) {
				throw new LogicException("Map中没有指定的键：" + property + "!");
			}
		} else {
			Class clazz = bean.getClass();
			try {
				Method method = clazz.getMethod(field2GetterName(property));
				value = method.invoke(bean);
			} catch (Exception e) {
				throw new LogicException("指定的属性名或对应的getter方法不存在!", e);
			}
		}
		return decodeValue(value, expressions);
	}

	/**
	 * 将指定的Collection或数组对象中指定属性的值按照指定的表达式进行解析，并返回对应的值
	 * 
	 * @param collection 指定的Collection或数组对象，如果为null，则返回null；如果不符合Collection或数组对象条件，则抛出异常
	 * @param index 指定的索引，如果不存在，将会抛出异常
	 * @param expressions 指定的表达式，例如：<code>"1", "男", "0", "女"</code><br>
	 *            方法将会将指定属性的值(value)，与表达式进行匹配，形如：<br>
	 * 
	 *            <pre>
	 * if(value 等于 "1"){
	 * 		return "男";
	 * }else if(value 等于 "0"){
	 * 		return "女";
	 * }else{
	 * 		return value;
	 * }
	 * </pre>
	 * 
	 *            本方法接收的表达式参数个数可以为奇数，例如：<code>6, "星期六", 7, "星期天", "工作日"</code><br>
	 *            相当于：
	 * 
	 *            <pre>
	 *            if(value 等于 6){
	 *            	return "星期六";
	 *            }else if(value 等于 7){
	 *            	return "星期天";
	 *            }else{
	 *            	return "工作日";
	 *            }
	 * </pre>
	 * @return
	 */
	public static final Object decode(Object collection, int index, Object... expressions) {
		if (collection == null) return null;
		Object array = null;
		if (collection instanceof Collection) {
			array = ((Collection) collection).toArray();
		} else if (!collection.getClass().isArray()) {
			throw new LogicException("指定的collection不是集合类型或数组类型!");
		}
		return Array.getLength(array) == 0 ? null : decodeValue(Array.get(array, index), expressions);
	}

	/**
	 * 将指定的值根据指定的表达式解析，并返回解析后的结果
	 * 
	 * @param value 指定的值
	 * @param expressions 指定的表达式，例如：<code>"1", "男", "0", "女"</code><br>
	 *            方法将会将指定属性的值(value)，与表达式进行匹配，形如：<br>
	 *            <code>
	 * if(value 等于 "1"){<br>
	 * 		return "男";<br>
	 * }else if(value 等于 "0"){<br>
	 * 		return "女";<br>
	 * }else{<br>
	 * 		return value;<br>
	 * }
	 * </code><br>
	 *            本方法接收的表达式参数个数可以为奇数，例如：<code>6, "星期六", 7, "星期天", "工作日"</code><br>
	 *            相当于：<br>
	 *            if(value 等于 6){<br>
	 *            return "星期六";<br>
	 *            }else if(value 等于 7){<br>
	 *            return "星期天";<br>
	 *            }else{<br>
	 *            return "工作日";<br>
	 *            }
	 * @return
	 */
	public static final <T> T decodeValue(T value, T... expressions) {
		int length;
		if (expressions == null || (length = expressions.length) == 0) {
			throw new LogicException("decode的表达式参数个数不能小于1!");
		}
		int i = 0;
		if ((length & 1) == 1) {// 如果是奇数
			int index = length - 1;
			while (i < index) {
				if (value == expressions[i] || value != null && value.equals(expressions[i])) {
					break;
				}
				i += 2;
			}
			return i < index ? expressions[i + 1] : expressions[index];
		} else {// 如果是偶数
			do {
				if (value == expressions[i] || value != null && value.equals(expressions[i])) {
					break;
				}
			} while ((i += 2) < length);
			return i < length ? expressions[i + 1] : value;
		}
	}

	/**
	 * 如果指定字符串超过限制长度<code>maxLength</code>,则返回限制长度前面的部分字符串<br>
	 * 如果指定字符串==null，则返回空字符串<br>
	 * 如果字符串超出指定长度，则返回maxLength前面的部分，并在末尾加上后缀“...”
	 * 
	 * @param str 指定的字符串
	 * @param maxLength 最大限制长度
	 * @return
	 */
	public static final String limitChars(String str, int maxLength) {
		return limitChars(str, maxLength, "...");
	}

	/**
	 * 去除字符串两端的空格<br>
	 * 如果字符串=null，则返回空字符串""
	 * 
	 * @param str
	 * @return
	 */
	public static final String trim(String str) {
		return str == null ? "" : str.trim();
	}

	/**
	 * 去除指定对象两端的空格，如果对象为null，则返回空字符串""
	 * 
	 * @param obj 指定的对象，内部调用toString类型将其转为字符串
	 * @return
	 */
	public static final String trim(Object obj) {
		return obj == null ? "" : obj.toString().trim();
	}

	/**
	 * 去除字符串两端的空格<br>
	 * 如果字符串==null，这返回HTML的空格符"&amp;nbsp;"
	 * 
	 * @param str
	 * @return
	 */
	public static final String trim4Html(String str) {
		return str == null ? "&nbsp;" : str.trim();
	}

	/**
	 * 判断字符串内容是否为整数形式<br>
	 * 前面带0，例如"0012"仍为整数，返回true<br>
	 * 如果字符串为null，返回false<br>
	 * 如果字符串前后有空格，请先去除空格后再调用此方法，否则返回false<br>
	 * 此方法性能是使用正则表达式验证性能的12-18倍左右
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		return NumberUtil.isNumber(str);
	}

	/**
	 * 判断指定对象是否为整数类型或能够转为整数形式
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNumber(Object obj) {
		return NumberUtil.isNumber(obj);
	}

	/**
	 * 判断字符串内容是否为指定位数的整数形式<br>
	 * 前面带0，例如"0012"属于4位整数<br>
	 * 如果字符串为null，返回false<br>
	 * 如果字符串前后有空格，请先去除空格后再调用此方法，否则返回false
	 * 
	 * @param str
	 * @param length 指定位数大小
	 * @return
	 */
	public static boolean isNumber(String str, int length) {
		return NumberUtil.isNumber(str, length);
	}

	/**
	 * 判断字符串内容是否为整数或小数形式<br>
	 * 前面带0，例如"0012"仍为整数，返回true<br>
	 * 如果字符串为null，返回false<br>
	 * 如果字符串前后有空格，请先去除空格后再调用此方法，否则返回false<br>
	 * 此方法性能是使用正则表达式验证性能的4-9倍
	 * 
	 * @param str
	 * @return
	 */
	public final static boolean isDouble(String str) {
		return NumberUtil.isDouble(str);
	}

	/**
	 * 判断指定对象是否为数组类型
	 * 
	 * @param obj 指定的对象
	 * @return
	 * @see easymapping.util.ArrayUtil#isArray(Object)
	 */
	public final static boolean isArray(Object obj) {
		return obj != null && obj.getClass().isArray();
	}

	/**
	 * 如果字符串不足指定位数，则前面补0，直到指定位数<br>
	 * 如果字符串=null，则返回空字符串""<br>
	 * 如果字符串位数大于指定位数，则返回原字符串
	 * 
	 * @param str 字符串
	 * @param maxLength 指定位数，不能小于1
	 * @return
	 */
	public static final String zeroFill(String str, int maxLength) {
		return StringUtil.zeroFill(str, maxLength);
	}

	/**
	 * 根据需要存储的元素个数确定HashMap等Map接口实现类的初始容量(使用默认的负载因子：0.75)
	 * 
	 * @param capacity 需要存储的元素个数
	 * @return
	 */
	public static final int getCapacity(int capacity) {
		return getCapacity(capacity, 0.75f);
	}

	/**
	 * 根据需要存储的元素个数确定HashMap等Map接口实现类的初始容量
	 * 
	 * @param capacity 需要存储的元素个数
	 * @param loadFactor 负载因子，必须介于0-1之间，如果不在此范围，内部也不检测，后果自负
	 * @return
	 */
	public static final int getCapacity(int capacity, float loadFactor) {
		int initCapacity = 16;
		while (capacity > initCapacity * loadFactor) {
			initCapacity <<= 1;// 如果默认容量小于指定的期望，则扩大一倍
		}
		return initCapacity;
	}
}
