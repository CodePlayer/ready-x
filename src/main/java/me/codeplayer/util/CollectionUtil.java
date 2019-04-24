package me.codeplayer.util;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * List、Set、Map等常见集合数据操作的工具类
 * 
 * @author Ready
 * @date 2014-10-21
 * 
 */
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
	 * @param kvPairs 可变参数形式的键值数组，必须是K1, V1, K2, V2, K3, V3...这种形式
	 * @return
	 */
	public static final <K, V> HashMap<K, V> asHashMap(Object... kvPairs) {
		checkPairs(kvPairs);
		int size = kvPairs.length >> 1;
		HashMap<K, V> map = size > 12 ? new HashMap<K, V>(X.getCapacity(size)) : new HashMap<K, V>();
		addToMap(map, kvPairs);
		return map;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个Hashtable集合<br>
	 * 
	 * @param kvPairs 可变参数形式的键值数组，必须是K1, V1, K2, V2, K3, V3...这种形式
	 * @return
	 */
	public static final <K, V> ConcurrentHashMap<K, V> asConcurrentHashMap(Object... kvPairs) {
		checkPairs(kvPairs);
		int size = kvPairs.length >> 1;
		ConcurrentHashMap<K, V> map = size > 12 ? new ConcurrentHashMap<K, V>(X.getCapacity(size)) : new ConcurrentHashMap<K, V>();
		addToMap(map, kvPairs);
		return map;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个LinkedHashMap集合<br>
	 * 
	 * @param kvPairs 可变参数形式的键值数组，必须是K1, V1, K2, V2, K3, V3...这种形式
	 * @return
	 */
	public static final <K, V> LinkedHashMap<K, V> asLinkedHashMap(Object... kvPairs) {
		checkPairs(kvPairs);
		int size = kvPairs.length >> 1;
		LinkedHashMap<K, V> map = size > 12 ? new LinkedHashMap<K, V>(X.getCapacity(size)) : new LinkedHashMap<K, V>();
		addToMap(map, kvPairs);
		return map;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个ConcurrentHashMap集合<br>
	 * 
	 * @param kvPairs 可变参数形式的键值数组，必须是K1, V1, K2, V2, K3, V3...这种形式
	 * @return
	 */
	public static final <K, V> ConcurrentHashMap<K, V> createConcurrentHashMap(Object... kvPairs) {
		checkPairs(kvPairs);
		int size = kvPairs.length >> 1;
		ConcurrentHashMap<K, V> map = size > 12 ? new ConcurrentHashMap<K, V>(X.getCapacity(size)) : new ConcurrentHashMap<K, V>();
		addToMap(map, kvPairs);
		return map;
	}

	protected static final void checkPairs(final Object... pairs) {
		if ((pairs.length & 1) != 0) {
			throw new IllegalArgumentException("The length of the Array must be even:" + pairs.length);
		}
	}

	/**
	 * 根据可变参数形式的键值数组构造一个ArrayList集合<br>
	 * 
	 * @param elements 可变参数形式的元素数组
	 * @return
	 */
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
	public static final <E> LinkedList<E> asLinkedList(E... elements) {
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
	@SuppressWarnings("unchecked")
	public static final <E> Vector<E> asVector(E... elements) {
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
	@SuppressWarnings("unchecked")
	public static final <E> HashSet<E> asHashSet(E... elements) {
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
	@SuppressWarnings("unchecked")
	public static final <E> LinkedHashSet<E> asLinkedHashSet(E... elements) {
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
	public static final <K, V> Map<K, V> addToMap(int ignore, Map<K, V> map, Object... kvPairs) {
		checkPairs(kvPairs);
		Map<Object, Object> m = X.castType(map);
		if (ignore == IGNORE_NONE) {
			for (int i = 0; i < kvPairs.length;) {
				m.put(kvPairs[i++], kvPairs[i++]);
			}
		} else {
			for (int i = 0; i < kvPairs.length;) {
				Object key = kvPairs[i++], value = kvPairs[i++];
				if (notIgnore(value, ignore)) {
					m.put(key, value);
				}
			}
		}
		return map;
	}

	/**
	 * 将可变参数形式的键值数组添加到一个Map集合中
	 * 
	 * @param map 指定的Map集合
	 * @param kvPairs 可变参数形式的键值数组，必须是K1, V1, K2, V2, K3, V3...这种形式
	 */
	public static final <K, V> Map<K, V> addToMap(Map<K, V> map, Object... kvPairs) {
		return addToMap(IGNORE_NONE, map, kvPairs);
	}

	/**
	 * 将可变参数形式的键值数组添加到一个Map集合中
	 * 
	 * @param ignore 指示忽略当前键值的情况：{@link CollectionUtil#IGNORE_NONE }、{@link CollectionUtil#IGNORE_NULL } 、{@link CollectionUtil#IGNORE_EMPTY }、 {@link CollectionUtil#IGNORE_BLANK }
	 * @param collection 指定的Collection集合
	 * @param elements 可变参数形式的元素数组
	 */
	@SuppressWarnings("unchecked")
	public static final <E> Collection<E> addToCollection(int ignore, Collection<? super E> collection, E... elements) {
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
		return X.castType(collection);
	}

	/**
	 * 将可变参数形式的键值数组添加到一个Map集合中
	 * 
	 * @param collection 指定的Collection集合
	 * @param elements 可变参数形式的元素数组
	 */
	@SuppressWarnings("unchecked")
	public static final <E> Collection<E> addToCollection(Collection<? super E> collection, E... elements) {
		return addToCollection(IGNORE_NONE, collection, elements);
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
	@SuppressWarnings("unchecked")
	public static final <K, V> V[] mapValues(Map<K, V> map, Class<V> valueClass, K... keys) {
		V[] results = X.castType(Array.newInstance(valueClass, keys.length));
		for (int i = 0; i < keys.length; i++) {
			results[i] = map.get(keys[i]);
		}
		return results;
	}

	/**
	 * 移除集合里重复的元素
	 *
	 * @param list 待处理的集合
	 * @param <E>
	 * @return
	 */
	public static <E> List<E> removeDuplicate(List<E> list) {
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		ArrayList<E> singleList = new ArrayList<E>(list.size());
		for (E e : list) {
			if (!singleList.contains(e)) {
				singleList.add(e);
			}
		}
		return singleList;
	}
}
