package me.codeplayer.util;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

import javax.annotation.*;

/**
 * List、Set、Map等常见集合数据操作的工具类
 *
 * @author Ready
 * @date 2014-10-21
 */
public abstract class CollectionUtil {

	protected static void checkPairs(final Object... pairs) {
		if ((pairs.length & 1) != 0) {
			throw new IllegalArgumentException("The length of the Array must be even:" + pairs.length);
		}
	}

	/**
	 * 根据真实的元素个数和负载因子，计算 Map 集合合理的容量初始值
	 *
	 * @param realCapacity 真实的元素个数
	 * @param loadFactor 负载因子
	 */
	public static int mapInitialCapacity(int realCapacity, float loadFactor) {
		float size = realCapacity / loadFactor;
		return size > (int) size ? (int) size + 1 : (int) size;
	}

	/**
	 * 根据真实的元素个数和默认的负载因子，计算 Map 集合合理的容量初始值
	 *
	 * @param realCapacity 真实的元素个数
	 */
	public static int mapInitialCapacity(int realCapacity) {
		return realCapacity * 4 / 3 + (realCapacity % 3 == 0 ? 0 : 1);
	}

	/**
	 * 将可变参数形式的键值数组添加到一个 Map 集合中
	 *
	 * @param target 指定的 Collection 集合
	 * @param filter 如果指定了该数组元素过滤器，则只有过滤结果为 true 的才会被添加
	 * @param elements 可变参数形式的元素数组
	 */
	@SuppressWarnings("unchecked")
	public static <E> Collection<E> addAll(Collection<E> target, @Nullable Predicate<? super E> filter, final E... elements) {
		for (E e : elements) {
			if (filter == null || filter.test(e)) {
				target.add(e);
			}
		}
		return target;
	}

	/**
	 * 将可变参数形式的键值数组添加到一个Map集合中
	 *
	 * @param target 指定的 Collection 集合
	 * @param elements 可变参数形式的元素数组
	 */
	@SuppressWarnings("unchecked")
	public static <E> Collection<E> addAll(Collection<E> target, E... elements) {
		return addAll(target, null, elements);
	}

	/**
	 * 将可变参数形式的键值数组添加到一个 Map 集合中
	 *
	 * @param map 指定的Map集合
	 * @param kvPairs 可变参数形式的键值数组，必须是K1, V1, K2, V2, K3, V3...这种形式
	 */
	public static <K, V> Map<K, V> addAll(final Map<K, V> map, final Object... kvPairs) {
		checkPairs(kvPairs);
		Map<Object, Object> m = X.castType(map);
		for (int i = 0; i < kvPairs.length; ) {
			m.put(kvPairs[i++], kvPairs[i++]);
		}
		return map;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个集合
	 *
	 * @param newList Collection 构造器，int 类型的参数为 Collection 构造方法的 initialCapacity 参数
	 * @param elements 可变参数形式的元素数组
	 */
	@SuppressWarnings("unchecked")
	public static <E, S extends Collection<E>> S ofSize(IntFunction<S> newList, E... elements) {
		S list = newList.apply(elements.length);
		addAll(list, elements);
		return list;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个集合
	 *
	 * @param newList Collection 构造器
	 * @param elements 可变参数形式的元素数组
	 */
	@SuppressWarnings("unchecked")
	public static <E, S extends Collection<E>> S of(Supplier<S> newList, E... elements) {
		S list = newList.get();
		addAll(list, elements);
		return list;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个 Set 集合
	 *
	 * @param newSet Set 构造器，int 类型的参数为 Set 构造方法的 initialCapacity 参数
	 * @param elements 可变参数形式的元素数组
	 */
	@SuppressWarnings("unchecked")
	public static <E, S extends Set<E>> S ofSet(IntFunction<S> newSet, E... elements) {
		S list = newSet.apply(mapInitialCapacity(elements.length));
		addAll(list, elements);
		return list;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个 HashSet 集合
	 *
	 * @param elements 可变参数形式的元素数组
	 */
	@SuppressWarnings("unchecked")
	public static <E> HashSet<E> ofHashSet(E... elements) {
		return ofSet(HashSet::new, elements);
	}

	/**
	 * 根据可变参数形式的键值数组构造一个 ArrayList 集合
	 *
	 * @param elements 可变参数形式的元素数组
	 */
	@SuppressWarnings("unchecked")
	public static <E> ArrayList<E> ofArrayList(E... elements) {
		return ofSize(ArrayList::new, elements);
	}

	/**
	 * 根据可变参数形式的键值数组构造一个 Map 集合<br/>
	 *
	 * @param newMap Map 构造器，int 参数为 Map 构造方法的 initialCapacity 参数
	 * @param elements 可变参数形式的元素数组
	 */
	public static <K, V, M extends Map<K, V>> M toMap(IntFunction<M> newMap, Object... elements) {
		M map = newMap.apply(mapInitialCapacity(elements.length));
		addAll(map, elements);
		return map;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个 Map 集合<br/>
	 *
	 * @param elements 可变参数形式的元素数组
	 */
	public static <K, V> HashMap<K, V> ofHashMap(Object... elements) {
		return toMap(HashMap::new, elements);
	}

	/**
	 * 将指定的数据集合转为 Map 集合
	 *
	 * @param newMap Map 构造器，int 参数为 Map 构造方法的 initialCapacity 参数
	 * @param items 需要放入 Map 集合的数据集合
	 * @param keyMapper Map 的 {@code Entry.key} 转换器
	 * @param valueMapper Map 的 {@code Entry.value} 转换器
	 */
	public static <E, K, V, M extends Map<K, V>> M toMap(final IntFunction<M> newMap, final Iterable<E> items, final Function<? super E, K> keyMapper, final Function<? super E, V> valueMapper) {
		int size = items instanceof Collection ? ((Collection<E>) items).size() : 0;
		M map = newMap.apply(size > 0 ? mapInitialCapacity(size) : 16);
		if (items != null) {
			for (E e : items) {
				map.put(keyMapper.apply(e), valueMapper.apply(e));
			}
		}
		return map;
	}

	/**
	 * 将指定的数据集合转为 HashMap 集合
	 *
	 * @param items 需要放入 Map 集合的数据集合
	 * @param keyMapper Map 的 {@code Entry.key} 转换器
	 * @param valueMapper Map 的 {@code Entry.value} 转换器
	 */
	public static <E, K, V> HashMap<K, V> toHashMap(final Iterable<E> items, final Function<? super E, K> keyMapper, final Function<? super E, V> valueMapper) {
		return toMap(HashMap::new, items, keyMapper, valueMapper);
	}

	/**
	 * 将指定的数据集合转为 Map 集合
	 *
	 * @param newMap Map 构造器，int 参数为 Map 构造方法的 initialCapacity 参数
	 * @param items 需要放入 Map 集合的数据集合
	 * @param keyMapper Map 的 {@code Entry.key} 转换器
	 */
	public static <K, V, M extends Map<K, V>> M toMap(final IntFunction<M> newMap, final Iterable<V> items, final Function<? super V, K> keyMapper) {
		return toMap(newMap, items, keyMapper, Function.identity());
	}

	/**
	 * 将指定的数据集合转为 HashMap 集合
	 *
	 * @param items 需要放入 Map 集合的数据集合
	 * @param keyMapper Map 的 {@code Entry.key} 转换器
	 */
	public static <K, V> HashMap<K, V> toHashMap(final Iterable<V> items, final Function<? super V, K> keyMapper) {
		return toMap(HashMap::new, items, keyMapper);
	}

	/**
	 * 获取Map集合中指定的多个键的值，并以数组的形式依次返回。如果集合中没有指定的键，则数组对应位置的值为null
	 *
	 * @param map 指定的Map集合
	 * @param valueClass 数组的组件类型
	 * @param keys 指定的键数组
	 * @author Ready
	 * @since 0.3.1
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> V[] mapValues(Map<K, V> map, Class<V> valueClass, K... keys) {
		V[] results = X.castType(Array.newInstance(valueClass, keys.length));
		for (int i = 0; i < keys.length; i++) {
			results[i] = map.get(keys[i]);
		}
		return results;
	}

	/**
	 * 遍历集合，并使用指定的过滤器进行检测，返回遍历到的第一个符合条件的元素
	 *
	 * @param filter 如果为 null，则默认为 true
	 * @since 2.8.0
	 */
	public static <T> T findFirst(final Collection<T> range, @Nullable final Predicate<? super T> filter) {
		if (range != null && !range.isEmpty()) {
			for (T t : range) {
				if (filter == null || filter.test(t)) {
					return t;
				}
			}
		}
		return null;
	}

	/**
	 * 遍历集合，并使用指定的过滤器进行检测，返回遍历到的第一个元素
	 *
	 * @since 3.0.0
	 */
	public static <T> T getAny(final Collection<T> range) {
		if (range != null && !range.isEmpty()) {
			if (range instanceof List) {
				return ((List<T>) range).get(0);
			}
			for (T t : range) {
				return t;
			}
		}
		return null;
	}

	/**
	 * 依次将集合中每个元素进行指定的映射，。并返回映射后的数组
	 *
	 * @param list 集合
	 * @param fieldType 映射后的字段类型
	 * @param mapper 映射转换器
	 */
	@SuppressWarnings("unchecked")
	public static <T, R> R[] mapField(Collection<T> list, Class<R> fieldType, Function<? super T, ? extends R> mapper) {
		if (list == null) {
			return null;
		}
		int size = list.size();
		final R[] arrays = (R[]) Array.newInstance(fieldType, size);
		if (size > 0) {
			int index = 0;
			for (T t : list) {
				arrays[index++] = mapper.apply(t);
			}
		}
		return arrays;
	}

}