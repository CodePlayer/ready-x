package me.codeplayer.util;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * List、Set、Map等常见集合数据操作的工具类
 * 
 * @author Ready
 * @date 2014-10-21
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class CollectionUtil {

	/** 不做任何忽略处理 */
	public static final int IGNORE_NONE = 0;
	/** 忽略null值 */
	public static final int IGNORE_NULL = 1;
	/** 忽略null值和空字符串 */
	public static final int IGNORE_EMPTY = 2;
	/** 忽略null值、空字符串""和空白字符串 */
	public static final int IGNORE_BLANK = 3;

	/**
	 * 根据可变参数形式的键值数组构造一个HashMap集合<br>
	 * 
	 * @param KeysAndValues 可变参数形式的键值数组，必须是K1, V1, K2, V2, K3, V3...这种形式
	 * @return
	 */
	public static final HashMap createHashMap(Object... KeysAndValues) {
		Assert.isTrue((KeysAndValues.length & 1) == 0, "指定键值的参数个数必须为偶数!");
		int size = KeysAndValues.length >> 1;
		HashMap map = size > 12 ? new HashMap(X.getCapacity(size)) : new HashMap();
		addToMap(map, KeysAndValues);
		return map;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个Hashtable集合<br>
	 * 
	 * @param KeysAndValues 可变参数形式的键值数组，必须是K1, V1, K2, V2, K3, V3...这种形式
	 * @return
	 */
	public static final Hashtable createHashtable(Object... KeysAndValues) {
		Assert.isTrue((KeysAndValues.length & 1) == 0, "指定键值的参数个数必须为偶数!");
		int size = KeysAndValues.length >> 1;
		Hashtable table = size > 12 ? new Hashtable(X.getCapacity(size)) : new Hashtable();
		addToMap(table, KeysAndValues);
		return table;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个LinkedHashMap集合<br>
	 * 
	 * @param KeysAndValues 可变参数形式的键值数组，必须是K1, V1, K2, V2, K3, V3...这种形式
	 * @return
	 */
	public static final LinkedHashMap createLinkedHashMap(Object... KeysAndValues) {
		Assert.isTrue((KeysAndValues.length & 1) == 0, "指定键值的参数个数必须为偶数!");
		int size = KeysAndValues.length >> 1;
		LinkedHashMap map = size > 12 ? new LinkedHashMap(X.getCapacity(size)) : new LinkedHashMap();
		addToMap(map, KeysAndValues);
		return map;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个ConcurrentHashMap集合<br>
	 * 
	 * @param KeysAndValues 可变参数形式的键值数组，必须是K1, V1, K2, V2, K3, V3...这种形式
	 * @return
	 */
	public static final ConcurrentHashMap createConcurrentHashMap(Object... KeysAndValues) {
		Assert.isTrue((KeysAndValues.length & 1) == 0, "指定键值的参数个数必须为偶数!");
		int size = KeysAndValues.length >> 1;
		ConcurrentHashMap map = size > 12 ? new ConcurrentHashMap(X.getCapacity(size)) : new ConcurrentHashMap();
		addToMap(map, KeysAndValues);
		return map;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个ArrayList集合<br>
	 * 
	 * @param elements 可变参数形式的元素数组
	 * @return
	 */
	public static final <E> ArrayList<E> createArrayList(E... elements) {
		ArrayList<E> list = new ArrayList<E>(elements.length);
		addToCollection(list, elements);
		return list;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个LinkedList集合<br>
	 * 
	 * @param elements 可变参数形式的元素数组
	 * @return
	 */
	public static final <E> LinkedList<E> createLinkedList(E... elements) {
		LinkedList<E> list = new LinkedList<E>();
		addToCollection(list, elements);
		return list;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个Vector集合<br>
	 * 
	 * @param elements 可变参数形式的元素数组
	 * @return
	 */
	public static final <E> Vector<E> createVector(E... elements) {
		Vector<E> list = new Vector<E>();
		addToCollection(list, elements);
		return list;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个HashSet集合<br>
	 * 
	 * @param elements 可变参数形式的元素数组
	 * @return
	 */
	public static final <E> HashSet<E> createHashSet(E... elements) {
		HashSet<E> list = new HashSet<E>();
		addToCollection(list, elements);
		return list;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个LinkedHashSet集合<br>
	 * 
	 * @param elements 可变参数形式的元素数组
	 * @return
	 */
	public static final <E> LinkedHashSet<E> createLinkedHashSet(E... elements) {
		LinkedHashSet<E> list = new LinkedHashSet<E>();
		addToCollection(list, elements);
		return list;
	}

	/**
	 * 将可变参数形式的键值数组添加到一个Map集合中
	 * 
	 * @param ignore 指示忽略当前键值的情况：{@link CollectionUtil#IGNORE_NONE }、{@link CollectionUtil#IGNORE_NULL } 、{@link CollectionUtil#IGNORE_EMPTY }、 {@link CollectionUtil#IGNORE_BLANK }
	 * @param map 指定的Map集合
	 * @param kvPairs 可变参数形式的键值数组，必须是K1, V1, K2, V2, K3, V3...这种形式
	 */
	public static final void addToMap(int ignore, Map map, Object... kvPairs) {
		Assert.isTrue((kvPairs.length & 1) == 0, "指定键值的参数个数必须为偶数!");
		if (ignore == IGNORE_NONE) {
			for (int i = 0; i < kvPairs.length;) {
				map.put(kvPairs[i++], kvPairs[i++]);
			}
		} else {
			for (int i = 0; i < kvPairs.length;) {
				Object key = kvPairs[i++], value = kvPairs[i++];
				if (notIgnore(value, ignore)) {
					map.put(key, value);
				}
			}
		}
	}

	/**
	 * 将可变参数形式的键值数组添加到一个Map集合中
	 * 
	 * @param map 指定的Map集合
	 * @param kvPairs 可变参数形式的键值数组，必须是K1, V1, K2, V2, K3, V3...这种形式
	 */
	public static final void addToMap(Map map, Object... kvPairs) {
		addToMap(IGNORE_NONE, map, kvPairs);
	}

	/**
	 * 将可变参数形式的键值数组添加到一个Map集合中
	 * 
	 * @param ignore 指示忽略当前键值的情况：{@link CollectionUtil#IGNORE_NONE }、{@link CollectionUtil#IGNORE_NULL } 、{@link CollectionUtil#IGNORE_EMPTY }、 {@link CollectionUtil#IGNORE_BLANK }
	 * @param collection 指定的Collection集合
	 * @param elements 可变参数形式的元素数组
	 */
	public static final <E> void addToCollection(int ignore, Collection<? super E> collection, E... elements) {
		if (ignore == IGNORE_NONE) {
			for (int i = 0; i < elements.length; i++) {
				collection.add(elements[i]);
			}
		} else {
			for (int i = 0; i < elements.length; i++) {
				if (notIgnore(elements[i], ignore)) {
					collection.add(elements[i]);
				}
			}
		}
	}

	/**
	 * 将可变参数形式的键值数组添加到一个Map集合中
	 * 
	 * @param collection 指定的Collection集合
	 * @param elements 可变参数形式的元素数组
	 */
	public static final <E> void addToCollection(Collection<? super E> collection, E... elements) {
		addToCollection(IGNORE_NONE, collection, elements);
	}

	/**
	 * 判断是否不忽略指定值
	 * 
	 * @param value
	 * @param ignore
	 * @return
	 */
	protected static final boolean notIgnore(Object value, int ignore) {
		if (ignore > IGNORE_NONE) {
			switch (ignore) {
			case IGNORE_NULL:
				return value != null;
			case IGNORE_EMPTY:
				return value != null && value.toString().length() > 0;
			case IGNORE_BLANK:
				return value != null && StringUtil.notEmpty(value.toString());
			}
		}
		return true;
	}

	/**
	 * 获取Map集合中指定的多个键的值，并以数组的形式依次返回。如果集合中没有指定的键，则数组对应位置的值为null
	 * 
	 * @param map 指定的Map集合
	 * @param valueClass Map中存储的值的类型
	 * @param keys 指定的键数组
	 * @return
	 * @author Ready
	 * @since 0.3.1
	 */
	public static final <K, V> V[] mapValues(Map<K, V> map, Class<V> valueClass, K... keys) {
		V[] results = (V[]) Array.newInstance(valueClass, keys.length);
		for (int i = 0; i < keys.length; i++) {
			results[i] = map.get(keys[i]);
		}
		return results;
	}
}
