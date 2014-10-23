package me.ready.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * List、Set、Map等常见集合数据操作的工具类 <<<<<<< HEAD
 * 
 * @package me.ready.util
 * @author Ready
 * @date 2014-10-21
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CollectionUtil {

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
	 * @param map 指定的Map集合
	 * @param KeysAndValues 可变参数形式的键值数组，必须是K1, V1, K2, V2, K3, V3...这种形式
	 */
	public static final void addToMap(Map map, Object... KeysAndValues) {
		Assert.isTrue((KeysAndValues.length & 1) == 0, "指定键值的参数个数必须为偶数!");
		for (int i = 0; i < KeysAndValues.length;) {
			map.put(KeysAndValues[i++], KeysAndValues[i++]);
		}
	}

	/**
	 * 将可变参数形式的键值数组添加到一个Map集合中
	 * 
	 * @param collection 指定的Collection集合
	 * @param elements 可变参数形式的元素数组
	 */
	public static final <E> void addToCollection(Collection<? super E> collection, E... elements) {
		for (E e : elements) {
			collection.add(e);
		}
	}
}
