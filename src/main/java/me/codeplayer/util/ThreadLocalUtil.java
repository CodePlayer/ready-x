package me.codeplayer.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 提供基于线程的局部缓存工具类，内部采用HashMap实现
 *
 * @author Ready
 * @since 2014-10-23
 */
public abstract class ThreadLocalUtil {

	private static final ThreadLocal<Map<Object, Object>> LOCAL_CACHE = new ThreadLocal<>();

	/**
	 * 获取线程局部缓存中存储的Map集合<br>
	 * 如果线程局部缓存中不存在任何键值映射，则返回一个空的HashMap。<br>
	 * <strong>注意：<strong>该方法返回的并不是一个缓存副本，而是缓存的引用，因此你对返回集合进行的更改会反映到线程局部缓存中
	 */
	public static Map<Object, Object> getMap() {
		Map<Object, Object> cache = LOCAL_CACHE.get();
		if (cache == null) {
			LOCAL_CACHE.set(cache = new HashMap<>());
		}
		return cache;
	}

	/**
	 * 向当前线程的局部缓存中添加指定的键值映射。如果该映射以前包含了一个该键的映射关系，则旧值被替换。
	 *
	 * @param key 指定值将要关联的键
	 * @param value 指定键将要关联的值
	 * @return 与 key 关联的旧值；如果 key 没有任何映射关系，则返回 null。（返回 null 还可能表示该映射之前将 null 与 key 关联。）
	 */
	public static Object put(Object key, Object value) {
		return getMap().put(key, value);
	}

	/**
	 * 返回线程局部缓存中指定键所映射的值；如果对于该键来说，此映射不包含任何映射关系，则返回 null。
	 */
	public static Object get(Object key) {
		Map<Object, Object> cache = LOCAL_CACHE.get();
		return cache == null ? null : cache.get(key);
	}

	/**
	 * 清空当前线程的局部缓存<br>
	 * 该方法会清空缓存的HashMap，并从线程局部缓存中移除HashMap
	 */
	public static void clear() {
		Map<Object, Object> cache = LOCAL_CACHE.get();
		if (cache != null) {
			cache.clear();
			LOCAL_CACHE.remove();
		}
	}

	/**
	 * 返回线程局部缓存中的键-值映射关系数。
	 */
	public static int size() {
		Map<Object, Object> cache = LOCAL_CACHE.get();
		return cache == null ? 0 : cache.size();
	}

	/**
	 * 如果线程局部缓存中不存在任何键值映射，则返回 true。
	 */
	public static boolean isEmpty() {
		Map<Object, Object> cache = LOCAL_CACHE.get();
		return cache == null || cache.isEmpty();
	}

	/**
	 * 如果线程局部缓存中包含对于指定键的映射关系，则返回 true。
	 */
	public static boolean containsKey(Object key) {
		Map<Object, Object> cache = LOCAL_CACHE.get();
		return cache != null && cache.containsKey(key);
	}

	/**
	 * 如果线程局部缓存中将一个或多个键映射到指定值，则返回 true。
	 */
	public static boolean containsValue(Object key) {
		Map<Object, Object> cache = LOCAL_CACHE.get();
		return cache != null && cache.containsValue(key);
	}

	/**
	 * 将指定映射的所有映射关系复制到线程局部缓存中，这些映射关系将替换线程局部缓存目前针对指定映射中所有键的所有映射关系。
	 */
	public static void putAll(Map<Object, Object> map) {
		getMap().putAll(map);
	}

	/**
	 * 使用指定的映射集合重置线程局部缓存<br>
	 * 之前的所有缓存数据将被丢弃，并使用新的映射集合作为缓存实现
	 */
	public static void reset(Map<Object, Object> map) {
		LOCAL_CACHE.set(map);
	}

	/**
	 * 从此映射中移除指定键的映射关系（如果存在）。
	 *
	 * @return 与 key 关联的旧值；如果 key 没有任何映射关系，则返回 null。（返回 null 还可能表示该映射之前将 null 与 key 关联。）
	 */
	public static Object remove(Object key) {
		Map<Object, Object> cache = LOCAL_CACHE.get();
		return cache == null ? null : cache.remove(key);
	}

	/**
	 * 移除 ThreadLocal 中当前线程绑定的数据（与线程解绑）
	 */
	public static void destroy() {
		LOCAL_CACHE.remove();
	}

}