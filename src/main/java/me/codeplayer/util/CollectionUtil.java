package me.codeplayer.util;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * List、Set、Map等常见集合数据操作的工具类
 *
 * @author Ready
 * @since 2014-10-21
 */
public abstract class CollectionUtil {

	protected static void checkPairs(final Object... pairs) {
		if ((pairs.length & 1) != 0) {
			throw new IllegalArgumentException("The length of the Array must be even:" + pairs.length);
		}
	}

	public static <T> T get(@Nullable List<T> list, int index, T defaultValue) {
		return list != null && list.size() > index ? list.get(index) : defaultValue;
	}

	public static <T> T get(@Nullable List<T> list, int index) {
		return get(list, index, null);
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
		if (filter == null) {
			Collections.addAll(target, elements);
			return target;
		}
		for (E e : elements) {
			if (filter.test(e)) {
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
	public static <E, S extends Collection<E>> S asList(IntFunction<S> newList, E... elements) {
		final S list = newList.apply(elements.length);
		Collections.addAll(list, elements);
		return list;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个 Set 集合
	 *
	 * @param newSet Set 构造器，int 类型的参数为 Set 构造方法的 initialCapacity 参数
	 * @param elements 可变参数形式的元素数组
	 */
	@SuppressWarnings("unchecked")
	public static <E, S extends Set<E>> S asSet(IntFunction<S> newSet, E... elements) {
		final S set = newSet.apply(elements.length);
		Collections.addAll(set, elements);
		return set;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个 HashSet 集合
	 *
	 * @param elements 可变参数形式的元素数组
	 */
	@SuppressWarnings("unchecked")
	public static <E> HashSet<E> asHashSet(E... elements) {
		HashSet<E> set = newHashSet(elements.length);
		Collections.addAll(set, elements);
		return set;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个 HashSet 集合
	 *
	 * @param elements 可变参数形式的元素数组
	 */
	@SuppressWarnings("unchecked")
	public static <E> LinkedHashSet<E> asLinkedHashSet(E... elements) {
		LinkedHashSet<E> set = newLinkedHashSet(elements.length);
		Collections.addAll(set, elements);
		return set;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个 ArrayList 集合
	 *
	 * @param elements 可变参数形式的元素数组
	 */
	@SuppressWarnings("unchecked")
	public static <E> ArrayList<E> asArrayList(E... elements) {
		final ArrayList<E> list = newArrayList(elements.length);
		Collections.addAll(list, elements);
		return list;
	}

	/**
	 * 过滤指定的集合，并返回获得符合条件的新集合
	 *
	 * @param c 指定的集合
	 * @param matcher 只有经过该过滤器后返回 true 的元素才符合条件
	 * @since 3.10.0
	 */
	public static <E> List<E> filter(final Collection<E> c, final Predicate<? super E> matcher) {
		int size = c.size();
		final List<E> list = new ArrayList<>();
		if (size > 0) {
			for (E e : c) {
				if (matcher.test(e)) {
					list.add(e);
				}
			}
		}
		return list;
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
	public static <K, V> HashMap<K, V> asHashMap(Object... elements) {
		final HashMap<K, V> map = newHashMap(elements.length);
		addAll(map, elements);
		return map;
	}

	/**
	 * 根据可变参数形式的键值数组构造一个 Map 集合<br/>
	 *
	 * @param elements 可变参数形式的元素数组
	 */
	public static <K, V> LinkedHashMap<K, V> asLinkedHashMap(Object... elements) {
		final LinkedHashMap<K, V> map = newLinkedHashMap(elements.length);
		addAll(map, elements);
		return map;
	}

	/**
	 * 将指定的数据集合转为 Map 集合
	 *
	 * @param newMap Map 构造器，int 参数为 Map 构造方法的 initialCapacity 参数
	 * @param items 需要放入 Map 集合的数据集合
	 * @param keyMapper Map 的 {@code Entry.key} 转换器
	 * @param valueMapper Map 的 {@code Entry.value} 转换器
	 */
	public static <E, K, V, M extends Map<K, V>> M toMap(final IntFunction<M> newMap, @Nullable final Iterable<E> items, final Function<? super E, K> keyMapper,
	                                                     final Function<? super E, V> valueMapper) {
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
	public static <E, K, V> HashMap<K, V> toHashMap(@Nullable final Iterable<E> items, final Function<? super E, K> keyMapper, final Function<? super E, V> valueMapper) {
		return toMap(CollectionUtil::newHashMap, items, keyMapper, valueMapper);
	}

	/**
	 * 构造一个可以实际存储指定容量的 HashMap
	 *
	 * @param size 预期的实际存储容量
	 */
	public static <K, V> HashMap<K, V> newHashMap(final int size) {
		if (size > 6 && size <= 12 || size == 0) {
			return new HashMap<>();
		} else if (size > 128) {
			return new HashMap<>(mapInitialCapacity(size));
		}
		return new HashMap<>(size, 1F);
	}

	/**
	 * 构造一个可以实际存储指定容量的 LinkedHashMap
	 *
	 * @param size 预期的实际存储容量
	 */
	public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(final int size) {
		if (size > 6 && size <= 12 || size == 0) {
			return new LinkedHashMap<>();
		} else if (size > 128) {
			return new LinkedHashMap<>(mapInitialCapacity(size));
		}
		return new LinkedHashMap<>(size, 1F);
	}

	/**
	 * 构造一个可以实际存储指定容量的 HashSet
	 *
	 * @param size 预期的实际存储容量
	 */
	public static <K> HashSet<K> newHashSet(int size) {
		if (size > 6 && size <= 12 || size == 0) {
			return new HashSet<>();
		} else if (size > 128) {
			return new HashSet<>(mapInitialCapacity(size));
		}
		return new HashSet<>(size, 1F);
	}

	/**
	 * 构造一个可以实际存储指定容量的 HashSet
	 *
	 * @param size 预期的实际存储容量
	 */
	public static <K> LinkedHashSet<K> newLinkedHashSet(int size) {
		if (size > 6 && size <= 12 || size == 0) {
			return new LinkedHashSet<>();
		} else if (size > 128) {
			return new LinkedHashSet<>(mapInitialCapacity(size));
		}
		return new LinkedHashSet<>(size, 1F);
	}

	/**
	 * 构造一个可以实际存储指定容量的 HashSet
	 *
	 * @param size 预期的实际存储容量
	 */
	public static <E> ArrayList<E> newArrayList(int size) {
		return size == 10 ? new ArrayList<>() : new ArrayList<>(size);
	}

	/**
	 * 将指定的数据集合转为 Map 集合
	 *
	 * @param newMap Map 构造器，int 参数为 Map 构造方法的 initialCapacity 参数
	 * @param items 需要放入 Map 集合的数据集合
	 * @param keyMapper Map 的 {@code Entry.key} 转换器
	 */
	public static <K, V, M extends Map<K, V>> M toMap(final IntFunction<M> newMap, @Nullable final Iterable<V> items, final Function<? super V, K> keyMapper) {
		return toMap(newMap, items, keyMapper, Function.identity());
	}

	/**
	 * 将指定的数据集合转为 HashMap 集合
	 *
	 * @param items 需要放入 Map 集合的数据集合
	 * @param keyMapper Map 的 {@code Entry.key} 转换器
	 */
	public static <K, V> HashMap<K, V> toHashMap(@Nullable final Iterable<V> items, final Function<? super V, K> keyMapper) {
		return toMap(CollectionUtil::newHashMap, items, keyMapper);
	}

	/**
	 * 获取Map集合中指定的多个键的值，并以数组的形式依次返回。如果集合中没有指定的键，则数组对应位置的值为null
	 *
	 * @param map 指定的Map集合
	 * @param valueClass 数组的组件类型
	 * @param keys 指定的键数组
	 * @since 0.3.1
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> V[] mapValues(@Nullable Map<K, V> map, Class<V> valueClass, K... keys) {
		final V[] results = (V[]) Array.newInstance(valueClass, keys.length);
		final boolean empty = map == null || map.isEmpty();
		for (int i = 0; i < keys.length; i++) {
			results[i] = empty ? null : map.get(keys[i]);
		}
		return results;
	}

	/**
	 * 遍历集合，并使用指定的过滤器进行检测，返回遍历到的第一个符合条件的元素。这相当于
	 * <pre><code>
	 * range.stream().filter(filter).findFirst().orElse(null);
	 * </code></pre>
	 * 但性能更优
	 *
	 * @param filter 如果为 null，则默认为 true
	 * @since 2.8.0
	 */
	public static <T> T findFirst(@Nullable final Collection<T> range, final Predicate<? super T> filter) {
		if (range != null && !range.isEmpty()) {
			for (T t : range) {
				if (filter.test(t)) {
					return t;
				}
			}
		}
		return null;
	}

	/**
	 * 检查集合中是否存在至少一个符合指定条件的元素。这相当于
	 * <pre><code>
	 *  range.stream().anyMatch(filter);
	 *  </code></pre>
	 * 但性能更优
	 *
	 * @param filter 条件过滤器
	 */
	public static <T> boolean anyMatch(@Nullable final Collection<T> range, final Predicate<? super T> filter) {
		return countMatches(range, filter, 1) > 0;
	}

	/**
	 * 检索集合中符合条件的元素个数。这相当于
	 * <pre><code>
	 *  long count = range.stream().filter(matcher).count();
	 *  return max <= 0 ? count : Math.min(count, max);
	 *  </code></pre>
	 * 但性能更优
	 *
	 * @param matcher 条件过滤器
	 * @param max 限制返回的最大计数。如果为 0 或负数 表示不限制
	 */
	public static <T> int countMatches(@Nullable final Collection<T> range, final Predicate<? super T> matcher, final int max) {
		int count = 0;
		if (range != null && !range.isEmpty()) {
			for (T t : range) {
				if (matcher.test(t) && ++count == max) {
					break;
				}
			}
		}
		return count;
	}

	/**
	 * 遍历集合，并使用指定的过滤器进行检测，返回遍历到的第一个元素。这相当于
	 * <pre><code>
	 * range.stream().findFirst().orElse(null);
	 * </code></pre>
	 * 但性能更优
	 *
	 * @since 3.0.0
	 */
	public static <T> T getAny(@Nullable final Collection<T> range) {
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
	 * 对指定集合进行进行转换，并返回转换后的 ArrayList 集合，这相当于
	 * <pre><code>
	 *  // allowNull = true
	 *  c.stream().map(converter).collect(Collectors.toList());
	 *
	 *  // allowNull = false
	 *  c.stream().map(converter).filter(Objects::nonNull).collect(Collectors.toList());
	 *  </code></pre>
	 * 但性能更优
	 *
	 * @return 只有 {@code c == null}，才会返回 {@code null}
	 */
	public static <E, R> ArrayList<R> toList(@Nullable Collection<E> c, Function<? super E, R> converter, boolean allowNull) {
		return toCollection(c, converter, allowNull, CollectionUtil::newArrayList);
	}

	/**
	 * 对指定集合进行进行转换，并返回转换后的 HashSet 集合，这相当于
	 * <pre><code>
	 *  // allowNull = true
	 *  c.stream().map(converter).collect(Collectors.toSet());
	 *
	 *  // allowNull = false
	 *  c.stream().map(converter).filter(Objects::nonNull).collect(Collectors.toSet());
	 *  </code></pre>
	 * 但性能更优
	 *
	 * @return 只有 {@code c == null}，才会返回 {@code null}
	 */
	public static <E, R> HashSet<R> toSet(Collection<E> c, Function<? super E, R> converter, boolean allowNull) {
		return toCollection(c, converter, allowNull, CollectionUtil::newHashSet);
	}

	/**
	 * 对指定集合进行进行转换，并返回转换后的 HashSet 集合，这相当于
	 * <pre><code>
	 *  c.stream().map(converter).collect(Collectors.toSet());
	 *  </code></pre>
	 * 但性能更优
	 *
	 * @return 只有 {@code c == null}，才会返回 {@code null}
	 */
	public static <E, R> HashSet<R> toSet(Collection<E> c, Function<? super E, R> converter) {
		return toSet(c, converter, true);
	}

	/**
	 * 对指定集合进行进行转换，并返回转换后的集合，这相当于
	 * <pre><code>
	 *  // allowNull = true
	 *  c.stream().map(converter).collect(Collectors.toList/toSet());
	 *
	 *  // allowNull = false
	 *  c.stream().map(converter).filter(Objects::nonNull).collect(Collectors.toList/toSet());
	 *  </code></pre>
	 * 但性能更优
	 *
	 * @return 如果 {@code c == null}，则返回 {@code null}
	 */
	public static <E, R, C extends Collection<R>> C toCollection(@Nullable Collection<E> c, Function<? super E, R> converter, boolean allowNull, IntFunction<? extends C> creator) {
		if (c == null) {
			return null;
		}
		final C values = creator.apply(c.size());
		for (E t : c) {
			R val = converter.apply(t);
			if (allowNull || val != null) {
				values.add(val);
			}
		}
		return values;
	}

	/**
	 * 对指定集合进行进行转换，并返回转换后的 ArrayList 集合，这相当于
	 * <pre><code>
	 *  c.stream().map(converter).collect(Collectors.toList());
	 *  </code></pre>
	 * 但性能更优
	 *
	 * @return 只有 {@code c == null}，才会返回 {@code null}
	 */
	public static <E, R> ArrayList<R> toList(@Nullable Collection<E> c, Function<? super E, R> converter) {
		return toList(c, converter, true);
	}

	/**
	 * 对指定集合进行分组，这相当于
	 * <pre><code>
	 *  c.stream().collect(Collectors.groupingBy(keyMapper));
	 *  </code></pre>
	 * 但性能更优
	 */
	@Nonnull
	public static <K, V> Map<K, List<V>> groupBy(@Nullable final Collection<V> c, final Function<? super V, ? extends K> keyMapper) {
		final int size = c == null ? 0 : c.size();
		if (size == 0) {
			return new HashMap<>();
		}
		final Map<K, List<V>> map = size <= 16 ? new HashMap<>(size, 1F) : new HashMap<>();
		final Function<K, List<V>> listBuilder = k -> CollectionUtil.newArrayList(size);
		for (V t : c) {
			map.computeIfAbsent(keyMapper.apply(t), listBuilder).add(t);
		}
		return map;
	}

	/**
	 * 返回从指定集合过滤并映射后的新集合，这相当于
	 * <pre><code>
	 *  c.stream().filter(filter).map(mapper).collect(Collectors.toList());
	 *  </code></pre>
	 * 但性能更优
	 */
	@Nonnull
	public static <T, R> List<R> filterAndMap(@Nullable Collection<T> c, final Predicate<? super T> filter, final Function<? super T, R> mapper) {
		final int size = c == null ? 0 : c.size();
		if (size > 0) {
			List<R> result = null;
			for (T t : c) {
				if (filter.test(t)) {
					if (result == null) {
						result = size > 10 ? new ArrayList<>() : new ArrayList<>(size);
					}
					result.add(mapper.apply(t));
				}
			}
			if (result != null) {
				return result;
			}
		}
		return new ArrayList<>();
	}

	/**
	 * 将 Map 集合中的键值对转换为 URL 参数格式的字符串。
	 * 【注意】如果 {@link Map.Entry#getValue()} 为 {@code null}，则表示该键值对不会被拼接
	 *
	 * @param sb 用于拼接参数的 StringBuilder。如果为 {@code null}，则内部会在必要时创建一个新的 StringBuilder 对象
	 * @param hasParam 之前是否已拼接了参数，如果为 {@code true}，则会在后续拼接的第一个参数前追加 '&' 符号。如果为 {@code null} 表示内部自动根据 {@code sb} 中的内容进行识别
	 * @param urlSafeRequired 是否需要对参数值进行 URL 预编码（只有明确不会出现非安全字符时，才建议为  {@code false} ）
	 * @param converter 对集合元素进行预处理的转换器，如果为 null 则表示无需预处理。如果转换后的结果为 {@code null}，则表示跳过该参数（不对其进行参数拼接）
	 * @return 如果传入的 sb 为 {@code null} 且 {@code params} 为空，就会返回 {@code null}
	 */
	public static StringBuilder mapToParams(@Nullable StringBuilder sb, @Nullable Boolean hasParam, final @Nullable Map<String, ?> params, final boolean urlSafeRequired, final @Nullable Function<Map.Entry<String, Object>, Map.Entry<String, Object>> converter) {
		if (params == null || params.isEmpty()) {
			return sb;
		}
		boolean notFirst;
		if (sb == null) {
			sb = new StringBuilder(params.size() * 16);
			notFirst = hasParam != null && hasParam;
		} else {
			notFirst = hasParam != null ? hasParam : sb.lastIndexOf("=") > -1;
			sb.ensureCapacity(sb.length() + params.size() * 16);
		}
		for (Map.Entry<String, ?> entry : params.entrySet()) {
			if (converter != null) {
				entry = converter.apply(X.castType(entry));
				if (entry == null) {
					continue;
				}
			}
			Object val = entry.getValue();
			if (val != null) {
				if (notFirst) {
					sb.append('&');
				} else {
					notFirst = true;
				}
				sb.append(entry.getKey()).append('=');
				if (val instanceof Number) {
					if (val instanceof Integer) {
						sb.append((int) val);
					} else if (val instanceof Long) {
						sb.append((long) val);
					} else if (val instanceof BigDecimal) {
						sb.append(((BigDecimal) val).toPlainString());
					} else {
						sb.append(val);
					}
					continue;
				}
				if (urlSafeRequired) { // 如果需要对 URL 编码安全，就需要对参数进行 URL 预编码
					try {
						sb.append(URLEncoder.encode(val.toString(), "UTF-8"));
					} catch (java.io.UnsupportedEncodingException e) {
						throw new IllegalArgumentException(e);
					}
				} else {
					sb.append(val);
				}
			}
		}
		return sb;
	}

	/**
	 * 将 Map 集合中的键值对转换为 URL 参数格式的字符串，如果参数值中存在不安全的字符，也会预先进行 URL 编码。
	 * 【注意】如果 {@link Map.Entry#getValue()} 为 {@code null}，则表示该键值对不会被拼接
	 *
	 * @param sb 用于拼接参数的 StringBuilder。如果为 {@code null}，则内部会在必要时创建一个新的 StringBuilder 对象
	 * @param hasParam 之前是否已拼接了参数，如果为 {@code true}，则会在后续拼接的第一个参数前追加 '&' 符号。如果为 {@code null} 表示内部自动根据 {@code sb} 中的内容进行识别
	 * @param converter 对集合元素进行预处理的转换器，如果为 null 则表示无需预处理。如果转换后的结果为 {@code null}，则表示跳过该参数（不对其进行参数拼接）
	 * @return 如果传入的 sb 为 {@code null} 且 {@code params} 为空，就会返回 {@code null}
	 */
	public static StringBuilder mapToParams(@Nullable StringBuilder sb, @Nullable Boolean hasParam, final @Nullable Map<String, ?> params, final @Nullable Function<Map.Entry<String, Object>, Map.Entry<String, Object>> converter) {
		return mapToParams(sb, hasParam, params, true, converter);
	}

	/**
	 * 将 Map 集合中的键值对转换为 URL 参数格式的字符串，如果参数值中存在不安全的字符，也会预先进行 URL 编码。
	 * 【注意】如果参数值为 {@code null}，则表示该键值对不会被拼接
	 *
	 * @param sb 用于拼接参数的 StringBuilder。如果为 {@code null}，则内部会在必要时创建一个新的 StringBuilder 对象
	 * @param hasParam 之前是否已拼接了参数，如果为 {@code true}，则会在后续拼接的第一个参数前追加 '&' 符号。如果为 {@code null} 表示内部自动根据 {@code sb} 中的内容进行识别
	 * @return 如果传入的 sb 为 {@code null} 且 {@code params} 为空，就会返回 {@code null}
	 */
	public static StringBuilder mapToParams(@Nullable StringBuilder sb, @Nullable Boolean hasParam, final boolean urlSafeRequired, final @Nullable Map<String, ?> params) {
		return mapToParams(sb, hasParam, params, urlSafeRequired, null);
	}

	/**
	 * 将 Map 集合中的键值对转换为 URL 参数格式的字符串，如果参数值中存在不安全的字符，也会预先进行 URL 编码。
	 * 【注意】如果参数值为 {@code null}，则表示该键值对不会被拼接
	 *
	 * @param sb 用于拼接参数的 StringBuilder。如果为 {@code null}，则内部会在必要时创建一个新的 StringBuilder 对象
	 * @param hasParam 之前是否已拼接了参数，如果为 {@code true}，则会在后续拼接的第一个参数前追加 '&' 符号。如果为 {@code null} 表示内部自动根据 {@code sb} 中的内容进行识别
	 * @return 如果传入的 sb 为 {@code null} 且 {@code params} 为空，就会返回 {@code null}
	 */
	public static StringBuilder mapToParams(@Nullable StringBuilder sb, @Nullable Boolean hasParam, final @Nullable Map<String, ?> params) {
		return mapToParams(sb, hasParam, params, null);
	}

	/**
	 * 将 Map 集合中的键值对转换为 URL 参数格式的字符串，如果参数值中存在不安全的字符，也会预先进行 URL 编码。
	 * 【注意】如果参数值为 {@code null}，则表示该键值对不会被拼接
	 *
	 * @return 如果传入的 {@code params} 为空，则返回 ""
	 */
	@Nonnull
	public static String mapToParams(final @Nullable Map<String, ?> params, final boolean urlSafeRequired) {
		StringBuilder sb = mapToParams(null, Boolean.FALSE, params, urlSafeRequired, null);
		return sb == null ? "" : sb.toString();
	}

	/**
	 * 将 Map 集合中的键值对转换为 URL 参数格式的字符串，如果参数值中存在不安全的字符，也会预先进行 URL 编码。
	 * 【注意】如果参数值为 {@code null}，则表示该键值对不会被拼接
	 *
	 * @return 如果传入的 {@code params} 为空，则返回 ""
	 */
	@Nonnull
	public static String mapToParams(final @Nullable Map<String, ?> params) {
		return mapToParams(params, true);
	}

}