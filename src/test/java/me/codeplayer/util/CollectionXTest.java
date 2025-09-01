package me.codeplayer.util;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.*;

import org.assertj.core.api.WithAssertions;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionXTest implements WithAssertions {

	@Test
	public void ofArrayList() {
		ArrayList<Integer> list = CollectionX.asArrayList(1, 2, 3, 4);
		assertThat(list)
				.isInstanceOf(ArrayList.class)
				.hasSize(4)
				.contains(3, Index.atIndex(2));
	}

	@Test
	public void checkPairs() {
		assertThatThrownBy(() -> CollectionX.checkPairs("A", "B", "C")).isInstanceOf(IllegalArgumentException.class);

		assertThatCode(() -> CollectionX.checkPairs("A", "B", "C", "D")).doesNotThrowAnyException();
	}

	@Test
	public void asHashMap() {
		HashMap<Object, Object> map = CollectionX.asHashMap("name", "Tom", "age", 18);
		assertThat(map)
				.isInstanceOf(HashMap.class)
				.hasSize(2)
				.containsEntry("name", "Tom")
				.containsEntry("age", 18);
	}

	@Test
	public void putAll() {
		HashMap<String, Object> map = CollectionX.asHashMap("name", "Tom", "age", 18);
		HashMap<String, Object> result = CollectionX.putAll(map, "k1", "v1", "k2", "v2", "k3", "v3", "k4", "v4", "k5", "v5", "k6", "v6", "k7", "v7");

		assertThat(result)
				.isSameAs(map)
				.hasSize(9)
				.containsEntry("name", "Tom")
				.containsEntry("age", 18)
				.containsEntry("k1", "v1")
				.containsEntry("k2", "v2")
				.containsEntry("k3", "v3")
				.containsEntry("k4", "v4")
				.containsEntry("k5", "v5")
				.containsEntry("k6", "v6")
				.containsEntry("k7", "v7")
		;
	}

	@Test
	public void putAll_EnumMap() {
		EnumMap<TestEnum, Object> map = new EnumMap<>(TestEnum.class);
		EnumMap<TestEnum, Object> result = CollectionX.putAll(map, TestEnum.VALUE1, "Tom", TestEnum.VALUE2, 18);

		assertThat(result)
				.isSameAs(map)
				.hasSize(2)
				.containsEntry(TestEnum.VALUE1, "Tom")
				.containsEntry(TestEnum.VALUE2, 18);

		EnumMap<TestEnum, String> stringMap = CollectionX.replaceValues(map, (k, v) -> v.toString());
		assertThat(stringMap)
				.isSameAs(map)
				.hasSize(2)
				.containsEntry(TestEnum.VALUE1, "Tom")
				.containsEntry(TestEnum.VALUE2, "18");
	}

	@Test
	public void mapValues() {
		HashMap<Object, Object> map = CollectionX.asHashMap("name", "Tom", "age", 18, "color", "Yellow");
		Object[] array = CollectionX.mapValues(map, Object.class, "name", "age", "haha");
		assertThat(array).hasSize(3).contains("Tom", Index.atIndex(0)).contains(null, Index.atIndex(2));
	}

	@Test
	public void findFirst() {
		ArrayList<Integer> ids = CollectionX.asArrayList(2, 1, 3, 4);
		Integer first = CollectionX.findFirst(ids, i -> i > 3);
		assertThat(first).isEqualTo(4);

		assertThat(CollectionX.getAny(ids)).isEqualTo(2);
	}

	@Test
	public void ofHashSet() {
		HashSet<Integer> set = CollectionX.asHashSet(1, 2, 3, 4, 3, 5);
		assertThat(set).hasSize(5).contains(1, 2, 3, 4, 5);
	}

	@Test
	public void get_ListIsNull_ReturnsDefaultValue() {
		assertThat(CollectionX.get(null, 0, "default")).isEqualTo("default");
	}

	@Test
	public void get_IndexOutOfBounds_ReturnsDefaultValue() {
		List<String> list = Arrays.asList("a", "b");
		assertThat(CollectionX.get(list, 2, "default")).isEqualTo("default");
		assertThat(CollectionX.get(list, -1, "default")).isEqualTo("default");
	}

	@Test
	public void get_ValidIndex_ReturnsElement() {
		List<String> list = Arrays.asList("a", "b", "c");
		assertThat(CollectionX.get(list, 1)).isEqualTo("b");
	}

	@Test
	public void get_IndexEqualsSize_ReturnsDefaultValue() {
		List<String> list = Arrays.asList("a", "b");
		assertThat(CollectionX.get(list, 2, "default")).isEqualTo("default");
	}

	@Test
	public void mapInitialCapacity_WithLoadFactor_CalculatesCorrectly() {
		assertThat(CollectionX.mapInitialCapacity(10, 0.75f)).isEqualTo(14);
	}

	@Test
	public void mapInitialCapacity_WithDefaultLoadFactor_CalculatesCorrectly() {
		assertThat(CollectionX.mapInitialCapacity(10)).isEqualTo(14);
	}

	@Test
	public void addAll_WithFilter_AddsFilteredElements() {
		List<String> target = CollectionX.asArrayList("1", "2", "3", "4", "5");
		Predicate<String> filter = s -> s.length() > 1;
		CollectionX.addAll(target, filter, "a", "abc", "de");
		assertThat(target).containsExactly("1", "2", "3", "4", "5", "abc", "de");

		target = CollectionX.asArrayList("1");
		List<String> result = CollectionX.addAll(target, "2", "3", "4", "5", "6", "7", "8");
		assertThat(result).isSameAs(target).containsExactly("1", "2", "3", "4", "5", "6", "7", "8");
	}

	@Test
	public void addAll_WithFilter_AddsFilteredElements2() {
		List<String> target = CollectionX.asArrayList("1", "2", "3", "4", "5", null);
		Predicate<String> filter = s -> s.length() > 1;
		CollectionX.addAll(target, filter, "a", "abc", "de");
		assertThat(target).containsExactly("1", "2", "3", "4", "5", null, "abc", "de");
	}

	@Test
	public void addAll_WithoutFilter_AddsAllElements() {
		List<String> target = CollectionX.asArrayList("a", "b", "c", "d", "e", "f", "g");
		List<String> result = CollectionX.addAll(target, "a", "abc", "de");
		assertSame(result, target);
		assertThat(target).containsExactly("a", "b", "c", "d", "e", "f", "g", "a", "abc", "de");
	}

	@Test
	public void putAll_MapWithKeyValuePairs_AddsCorrectly() {
		HashMap<String, Integer> map = new HashMap<>();
		HashMap<String, Integer> result = CollectionX.putAll(map, "key1", 1, "key2", 2);
		assertSame(result, map);
		HashMap<String, Object> typeMap = CollectionX.putAllRaw(X.castType(map), "key3", "3");
		assertThat(typeMap)
				.isSameAs(map)
				.containsEntry("key1", 1)
				.containsEntry("key2", 2)
				.containsEntry("key3", "3");
	}

	@Test
	public void putAllRaw_MapWithOddNumberOfKeyValuePairs_ThrowsException() {
		Map<String, Integer> map = new HashMap<>();
		assertThatThrownBy(() -> CollectionX.putAllRaw(map, "key1", 1, "key2")).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void asList_EmptyInput_ReturnsEmptyList() {
		List<Integer> list = CollectionX.asList(ArrayList::new);
		assertThat(list).isExactlyInstanceOf(ArrayList.class).isEmpty();
	}

	@Test
	public void asList_SingleElement_ReturnsListWithElement() {
		List<Integer> list = CollectionX.asList(ArrayList::new, 1);
		assertThat(list).containsExactly(1);
	}

	@Test
	public void asList_MultipleElements_ReturnsListWithElements() {
		List<Integer> list = CollectionX.asList(ArrayList::new, 1, 2, 3);
		assertThat(list).containsExactly(1, 2, 3);
	}

	@Test
	public void asList_DuplicateElements_ReturnsListWithDuplicates() {
		List<Integer> list = CollectionX.asList(ArrayList::new, 1, 2, 2);
		assertThat(list).containsExactly(1, 2, 2);
	}

	@Test
	public void asSet_EmptyInput_ReturnsEmptySet() {
		Set<Integer> set = CollectionX.asSet(HashSet::new);
		assertThat(set).isEmpty();
	}

	@Test
	public void asSet_SingleElement_ReturnsSetWithElement() {
		Set<Integer> set = CollectionX.asSet(HashSet::new, 1);
		assertThat(set).containsExactly(1);
	}

	@Test
	public void asSet_MultipleElements_ReturnsSetWithElements() {
		Set<Integer> set = CollectionX.asSet(HashSet::new, 1, 2, 3);
		assertThat(set).containsExactly(1, 2, 3);
	}

	@Test
	public void asSet_DuplicateElements_ReturnsSetWithoutDuplicates() {
		Set<Integer> set = CollectionX.asSet(HashSet::new, 1, 2, 2);
		assertThat(set).containsExactly(1, 2);
	}

	@Test
	public void filter_EmptyCollection_ReturnsEmptyList() {
		List<Integer> filtered = CollectionX.filter(Collections.emptyList(), i -> i > 0);
		assertThat(filtered).isEmpty();
	}

	@Test
	public void filter_AllElementsMatch_ReturnsAllElements() {
		List<Integer> filtered = CollectionX.filter(Arrays.asList(1, 2, 3), i -> i > 0);
		assertThat(filtered).containsExactly(1, 2, 3);
	}

	@Test
	public void filter_NoElementsMatch_ReturnsEmptyList() {
		List<Integer> filtered = CollectionX.filter(Arrays.asList(1, 2, 3), i -> i > 5);
		assertThat(filtered).isEmpty();
	}

	@Test
	public void filter_SomeElementsMatch_ReturnsMatchingElements() {
		List<Integer> filtered = CollectionX.filter(Arrays.asList(1, 2, 3), i -> i > 1);
		assertThat(filtered).containsExactly(2, 3);
	}

	@Test
	public void toMap_EmptyInput_ReturnsEmptyMap() {
		Map<String, Integer> map = CollectionX.toMap(HashMap::new);
		assertThat(map).isEmpty();
	}

	@Test
	public void toMap_SingleKeyValuePair_ReturnsMapWithPair() {
		Map<String, Integer> map = CollectionX.toMap(HashMap::new, "key", 1);
		assertThat(map).containsEntry("key", 1);
	}

	@Test
	public void toMap_MultipleKeyValuePairs_ReturnsMapWithPairs() {
		Map<String, Integer> map = CollectionX.toMap(HashMap::new, "key1", 1, "key2", 2);
		assertThat(map).containsEntry("key1", 1).containsEntry("key2", 2);
	}

	@Test
	public void toMap_DuplicateKeys_ReturnsMapWithLastValue() {
		Map<String, Integer> map = CollectionX.toMap(HashMap::new, "key", 1, "key", 2);
		assertThat(map).containsEntry("key", 2);
	}

	@Test
	public void asRawHashMap_EmptyInput_ReturnsEmptyMap() {
		Map<String, Integer> map = CollectionX.asRawHashMap();
		assertThat(map).isEmpty();
	}

	@Test
	public void asRawHashMap_MultipleKeyValuePairs_ReturnsMapWithPairs() {
		Map<String, Integer> map = CollectionX.asRawHashMap("key1", 1, "key2", 2);
		assertThat(map).containsEntry("key1", 1).containsEntry("key2", 2);
	}

	@Test
	public void asRawHashMap_MultipleKeyValuePairs_throwsException() {
		assertThrows(IllegalArgumentException.class, () -> CollectionX.asRawHashMap("key1", 1, "key2", 2, "key3"));
	}

	@Test
	public void asHashMap_SingleKeyValuePair_ReturnsMapWithPair() {
		Map<String, Integer> map = CollectionX.asHashMap("key", 1);
		assertThat(map).containsEntry("key", 1);
	}

	@Test
	public void asHashMap_MultipleKeyValuePairs_ReturnsMapWithPairs() {
		Map<String, Integer> map = CollectionX.asHashMap("key1", 1, "key2", 2);
		assertThat(map).containsEntry("key1", 1).containsEntry("key2", 2);
	}

	@Test
	public void asHashMap_DuplicateKeys_ReturnsMapWithLastValue() {
		Map<String, Integer> map = CollectionX.asHashMap("key", 1, "key", 2);
		assertThat(map).containsEntry("key", 2);
	}

	@Test
	public void toMap_WithIterable_ReturnsMapWithElements() {
		List<String> items = Arrays.asList("a", "bb", "ccc");
		Map<String, Integer> map = CollectionX.toMap(HashMap::new, items, Function.identity(), String::length);
		assertThat(map).containsEntry("a", 1).containsEntry("bb", 2).containsEntry("ccc", 3);
	}

	@Test
	public void toHashMap_WithIterable_ReturnsMapWithElements() {
		List<String> items = Arrays.asList("a", "bb", "ccc");
		Map<String, Integer> map = CollectionX.toHashMap(items, Function.identity(), String::length);
		assertThat(map).containsEntry("a", 1).containsEntry("bb", 2).containsEntry("ccc", 3);
	}

	@Test
	public void newHashMap_SizeZero_ReturnsEmptyMap() {
		Map<String, Integer> map = CollectionX.newHashMap(0);
		assertThat(map).isEmpty();
	}

	@Test
	public void newHashMap_SizeBetween6And12_ReturnsEmptyMap() {
		Map<String, Integer> map = CollectionX.newHashMap(10);
		assertThat(map).isEmpty();
	}

	@Test
	public void newHashMap_SizeGreaterThan128_ReturnsMapWithCalculatedCapacity() {
		Map<String, Integer> map = CollectionX.newHashMap(130);
		assertThat(map).isEmpty();
	}

	@Test
	public void newLinkedHashMap_SizeZero_ReturnsEmptyMap() {
		Map<String, Integer> map = CollectionX.newLinkedHashMap(0);
		assertThat(map).isEmpty();
	}

	@Test
	public void newLinkedHashMap_SizeBetween6And12_ReturnsEmptyMap() {
		Map<String, Integer> map = CollectionX.newLinkedHashMap(10);
		assertThat(map).isEmpty();
	}

	@Test
	public void newLinkedHashMap_SizeGreaterThan128_ReturnsMapWithCalculatedCapacity() {
		Map<String, Integer> map = CollectionX.newLinkedHashMap(130);
		assertThat(map).isEmpty();
	}

	@Test
	public void newHashSet_SizeZero_ReturnsEmptySet() {
		Set<String> set = CollectionX.newHashSet(0);
		assertThat(set).isEmpty();
	}

	@Test
	public void newHashSet_SizeBetween6And12_ReturnsEmptySet() {
		Set<String> set = CollectionX.newHashSet(10);
		assertThat(set).isEmpty();
	}

	@Test
	public void newHashSet_SizeGreaterThan128_ReturnsSetWithCalculatedCapacity() {
		Set<String> set = CollectionX.newHashSet(130);
		assertThat(set).isEmpty();
	}

	@Test
	public void newLinkedHashSet_SizeZero_ReturnsEmptySet() {
		Set<String> set = CollectionX.newLinkedHashSet(0);
		assertThat(set).isEmpty();
	}

	@Test
	public void newLinkedHashSet_SizeBetween6And12_ReturnsEmptySet() {
		Set<String> set = CollectionX.newLinkedHashSet(10);
		assertThat(set).isEmpty();
	}

	@Test
	public void newLinkedHashSet_SizeGreaterThan128_ReturnsSetWithCalculatedCapacity() {
		Set<String> set = CollectionX.newLinkedHashSet(130);
		assertThat(set).isEmpty();
	}

	@Test
	public void newArrayList_SizeZero_ReturnsEmptyList() {
		List<String> list = CollectionX.newArrayList(0);
		assertThat(list).isEmpty();
	}

	@Test
	public void newArrayList_SizeTen_ReturnsEmptyList() {
		List<String> list = CollectionX.newArrayList(10);
		assertThat(list).isEmpty();
	}

	@Test
	public void newArrayList_SizeOther_ReturnsListWithCapacity() {
		List<String> list = CollectionX.newArrayList(5);
		assertThat(list).isEmpty();
	}

	@Test
	public void asLinkedHashSet_EmptyInput_ReturnsEmptySet() {
		LinkedHashSet<Object> set = CollectionX.asLinkedHashSet();
		assertThat(set).isExactlyInstanceOf(LinkedHashSet.class).isEmpty();
	}

	@Test
	public void asLinkedHashSet_SingleElement_ReturnsSetWithOneElement() {
		LinkedHashSet<Object> set = CollectionX.asLinkedHashSet("A");
		assertThat(set).hasSize(1).contains("A");
	}

	@Test
	public void asLinkedHashSet_MultipleElements_ReturnsSetWithUniqueElements() {
		LinkedHashSet<Object> set = CollectionX.asLinkedHashSet("A", "B", "C");
		assertThat(set).hasSize(3).contains("A", "B", "C");
	}

	@Test
	public void asLinkedHashSet_DuplicateElements_ReturnsSetWithoutDuplicates() {
		LinkedHashSet<Object> set = CollectionX.asLinkedHashSet("A", "B", "A");
		assertThat(set).hasSize(2).contains("A", "B");
	}

	@Test
	public void asLinkedHashSet_PreservesInsertionOrder() {
		LinkedHashSet<Object> set = CollectionX.asLinkedHashSet("A", "B", "C");
		assertThat(set).containsSequence("A", "B", "C");
	}

	@Test
	public void asLinkedHashMap_EvenNumberOfElements_CreatesMapCorrectly() {
		Map<String, Object> map = CollectionX.asLinkedHashMap("name", "Tom", "age", 18);
		assertThat(map).isInstanceOf(LinkedHashMap.class).hasSize(2).containsEntry("name", "Tom").containsEntry("age", 18);
	}

	@Test
	public void asRawLinkedHashMap_MultipleKeyValuePairs_ReturnsMapWithPairs() {
		LinkedHashMap<String, Integer> map = CollectionX.asLinkedHashMap("key1", 1, "key2", 2, "key3", 3, "key4", 4);
		assertThat(map).containsEntry("key1", 1).containsEntry("key2", 2).containsEntry("key3", 3).containsEntry("key4", 4);

		assertArrayEquals(new Object[] { "key1", "key2", "key3", "key4" }, map.keySet().toArray());
	}

	@Test
	public void asRawLinkedHashMap_OddNumberOfElements_IgnoresLastElement() {
		assertThrows(IllegalArgumentException.class, () -> CollectionX.asRawLinkedHashMap("name"));
		assertThrows(IllegalArgumentException.class, () -> CollectionX.asRawLinkedHashMap("name", "Tom", "age"));
	}

	@Test
	public void asRawLinkedHashMap_EmptyInput_ReturnsEmptyMap() {
		Map<String, Object> map = CollectionX.asRawLinkedHashMap();
		assertThat(map).isInstanceOf(LinkedHashMap.class).isEmpty();
	}

	@Test
	public void asLinkedHashMap_DuplicateKeys_KeepsLastValue() {
		Map<String, Object> map = CollectionX.asLinkedHashMap("name", "Tom", "name", "Jerry");
		assertThat(map).isInstanceOf(LinkedHashMap.class).hasSize(1).containsEntry("name", "Jerry");
	}

	@Test
	public void toMap_NullItems_ReturnsEmptyMap() {
		List<String> items = null;
		Map<String, String> map = CollectionX.toMap(HashMap::new, items, FunctionX.identity());
		assertThat(map).isEmpty();
	}

	@Test
	public void toMap_EmptyItems_ReturnsEmptyMap() {
		Map<String, String> map = CollectionX.toMap(HashMap::new, Collections.emptyList(), Function.identity());
		assertThat(map).isEmpty();
	}

	@Test
	public void toMap_NonEmptyItems_ReturnsMapWithCorrectValues() {
		List<String> items = Arrays.asList("a", "b");
		Map<String, String> map = CollectionX.toMap(HashMap::new, items, Function.identity());
		assertThat(map).hasSize(2).containsEntry("a", "a").containsEntry("b", "b");
	}

	@Test
	public void toMap_DuplicateKeys_LastValueWins() {
		List<String> items = Arrays.asList("a", "b", "a");
		Map<String, Integer> map = CollectionX.toMap(HashMap::new, items, Function.identity(), String::length);
		assertThat(map).hasSize(2).containsEntry("a", 1).containsEntry("b", 1);
	}

	@Test
	public void toMap_KeyMapperReturnsNull_KeyIsNull() {
		List<String> items = Arrays.asList("a", "b");
		Map<String, Integer> map = CollectionX.toMap(HashMap::new, items, s -> null, String::length);
		assertThat(map).hasSize(1).containsEntry(null, 1);
	}

	@Test
	public void toHashMap_NullItems_ReturnsEmptyMap() {
		HashMap<String, Integer> map = CollectionX.toHashMap(null, Object::toString);
		assertTrue(map.isEmpty());
	}

	@Test
	public void toHashMap_EmptyIterable_ReturnsEmptyMap() {
		Iterable<Integer> items = Collections.emptyList();
		HashMap<String, Integer> map = CollectionX.toHashMap(items, Object::toString);
		assertTrue(map.isEmpty());
	}

	@Test
	public void toHashMap_SingleElement_CorrectlyMapsToMap() {
		Iterable<Integer> items = Collections.singletonList(1);
		HashMap<String, Integer> map = CollectionX.toHashMap(items, Object::toString);
		assertEquals(1, map.size());
		assertEquals(Integer.valueOf(1), map.get("1"));
	}

	@Test
	public void toHashMap_MultipleElements_CorrectlyMapsToMap() {
		Iterable<Integer> items = Arrays.asList(1, 2, 3);
		HashMap<String, Integer> map = CollectionX.toHashMap(items, Object::toString);
		assertEquals(3, map.size());
		assertEquals(Integer.valueOf(1), map.get("1"));
		assertEquals(Integer.valueOf(2), map.get("2"));
		assertEquals(Integer.valueOf(3), map.get("3"));
	}

	@Test
	public void toHashMap_DuplicateKeys_LastValueOverwrites() {
		Iterable<Integer> items = Arrays.asList(1, 2, 1);
		HashMap<String, Integer> map = CollectionX.toHashMap(items, Object::toString);
		assertEquals(2, map.size());
		assertEquals(Integer.valueOf(2), map.get("2"));
		assertEquals(Integer.valueOf(1), map.get("1"));
	}

	@Test
	public void mapValues_NullMap_ReturnsNullArray() {
		Object[] values = CollectionX.mapValues(null, Object.class, "k1", "k2");
		assertArrayEquals(new Object[2], values);
	}

	@Test
	public void mapValues_EmptyMap_ReturnsNullArray() {
		Map<String, Object> map = new HashMap<>();
		Object[] values = CollectionX.mapValues(map, Object.class, "k1", "k2");
		assertArrayEquals(new Object[2], values);
	}

	@Test
	public void mapValues_KeysNotExist_ReturnsNullArray() {
		Map<String, Object> map = CollectionX.asHashMap("k3", "v3");
		Object[] values = CollectionX.mapValues(map, Object.class, "k1", "k2");
		assertArrayEquals(new Object[2], values);
	}

	@Test
	public void mapValues_KeysExist_ReturnsArray() {
		Map<String, String> map = CollectionX.asHashMap("k1", "v1", "k2", "v2", "k3", "v3");
		String[] values = CollectionX.mapValues(map, String.class, "k1", "k3", "k4");
		assertArrayEquals(new String[] { "v1", "v3", null }, values);
	}

	@Test
	public void findFirst_NullRange_ReturnsNull() {
		Integer result = CollectionX.findFirst(null, i -> i > 0);
		assertThat(result).isNull();
	}

	@Test
	public void findFirst_EmptyRange_ReturnsNull() {
		Integer result = CollectionX.findFirst(Collections.emptyList(), i -> i > 0);
		assertThat(result).isNull();
	}

	@Test
	public void findFirst_NoMatch_ReturnsNull() {
		List<Integer> range = Arrays.asList(1, 2, 3);
		Integer result = CollectionX.findFirst(range, i -> i > 10);
		assertThat(result).isNull();
	}

	@Test
	public void anyMatch_NullRange_ReturnsFalse() {
		Predicate<Integer> filter = i -> i > 0;
		boolean result = CollectionX.anyMatch(null, filter);
		assertThat(result).isFalse();
	}

	@Test
	public void anyMatch_EmptyRange_ReturnsFalse() {
		Predicate<Integer> filter = i -> i > 0;
		boolean result = CollectionX.anyMatch(Collections.emptyList(), filter);
		assertThat(result).isFalse();
	}

	@Test
	public void anyMatch_NoMatch_ReturnsFalse() {
		List<Integer> range = Arrays.asList(1, 2, 3);
		boolean result = CollectionX.anyMatch(range, i -> i > 10);
		assertThat(result).isFalse();
	}

	@Test
	public void countMatches_NullRange_ReturnsZero() {
		Predicate<Integer> filter = i -> i > 0;
		int result = CollectionX.countMatches(null, filter, 1);
		assertThat(result).isZero();
	}

	@Test
	public void countMatches_EmptyRange_ReturnsZero() {
		Predicate<Integer> filter = i -> i > 0;
		int result = CollectionX.countMatches(Collections.emptyList(), filter, 1);
		assertThat(result).isZero();
	}

	@Test
	public void countMatches_NoMatch_ReturnsZero() {
		List<Integer> range = Arrays.asList(1, 2, 3);
		int result = CollectionX.countMatches(range, i -> i > 10, 1);
		assertThat(result).isZero();
	}

	@Test
	public void getAny_NullRange_ReturnsNull() {
		Integer result = CollectionX.getAny(null);
		assertThat(result).isNull();
	}

	@Test
	public void getAny_EmptyRange_ReturnsNull() {
		Integer result = CollectionX.getAny(Collections.emptyList());
		assertThat(result).isNull();
	}

	@Test
	public void toList_NullCollection_ReturnsNull() {
		List<String> result = CollectionX.toList(null, Function.identity(), true);
		assertThat(result).isNull();
	}

	@Test
	public void toList_ConverterReturnsNull_AllowsNull() {
		List<String> items = Arrays.asList("a", "b");
		List<String> result = CollectionX.toList(items, s -> null, true);
		assertThat(result).hasSize(2).containsNull().containsNull();
	}

	@Test
	public void toSet_ConverterReturnsNull_AllowsNull() {
		List<String> items = Arrays.asList("a", "b");
		Set<String> result = CollectionX.toSet(items, s -> null, true);
		assertThat(result).hasSize(1).containsNull();
	}

	@Test
	public void toCollection_NullCollection_ReturnsNull() {
		Collection<String> result = CollectionX.toCollection(null, FunctionX.identity(), true, CollectionX::newArrayList);
		assertThat(result).isNull();
	}

	@Test
	public void toCollection_ConverterReturnsNull_AllowsNull() {
		List<String> items = Arrays.asList("a", "b");
		Collection<String> result = CollectionX.toCollection(items, s -> null, true, CollectionX::newArrayList);
		assertThat(result).hasSize(2).containsNull().containsNull();
	}

	@Test
	public void toSet_NullCollection_ReturnsNull() {
		assertThat(CollectionX.toSet(null, Function.identity())).isNull();

		Set<String> result = CollectionX.toSet(null, FunctionX.identity(), true);
		assertThat(result).isNull();
	}

	@Test
	public void toSet_EmptyCollection_ReturnsEmptySet() {
		assertThat(CollectionX.toSet(Collections.emptyList(), e -> e)).isEmpty();
	}

	@Test
	public void toSet_NonEmptyCollection_ReturnsConvertedSet() {
		List<String> list = Arrays.asList("a", "b", "c");
		HashSet<String> result = CollectionX.toSet(list, String::toUpperCase);
		assertThat(result).containsExactlyInAnyOrder("A", "B", "C");
	}

	@Test
	public void toSet_ConverterReturnsNull_AllowsNulls() {
		List<String> list = Arrays.asList("a", "b", "c");
		HashSet<String> result = CollectionX.toSet(list, e -> null);
		assertThat(result).containsNull().hasSize(1);
	}

	@Test
	public void toSet_ConverterReturnsNonNull_ContainsNonNullValues() {
		List<String> list = Arrays.asList("a", "b", "c");
		HashSet<String> result = CollectionX.toSet(list, e -> e);
		assertThat(result).containsExactlyInAnyOrder("a", "b", "c");
	}

	@Test
	public void toList_CollectionIsNull_ReturnsNull() {
		ArrayList<String> result = CollectionX.toList(null, Object::toString);
		assertThat(result).isNull();
	}

	@Test
	public void toList_EmptyCollection_ReturnsEmptyList() {
		ArrayList<String> result = CollectionX.toList(Collections.emptyList(), Object::toString);
		assertThat(result).isEmpty();
	}

	@Test
	public void toList_NonEmptyCollection_ReturnsConvertedArrayList() {
		ArrayList<String> result = CollectionX.toList(Arrays.asList(1, 2, 3), Object::toString);
		assertThat(result).containsExactly("1", "2", "3");
	}

	@Test
	public void toList_ConverterReturnsNull_ContainsNullValues() {
		ArrayList<String> result = CollectionX.toList(Arrays.asList(1, 2, 3), i -> i == 2 ? null : i.toString());
		assertThat(result).containsExactly("1", null, "3");
	}

	@Test
	public void groupBy_CollectionIsNull_ReturnsEmptyMap() {
		Map<String, List<Integer>> result = CollectionX.groupBy(null, Object::toString);
		assertThat(result).isEmpty();
	}

	@Test
	public void groupBy_EmptyCollection_ReturnsEmptyMap() {
		Map<String, List<Integer>> result = CollectionX.groupBy(Collections.emptyList(), Object::toString);
		assertThat(result).isEmpty();
	}

	@Test
	public void groupBy_NonEmptyCollection_ReturnsGroupedMap() {
		Map<String, List<Integer>> result = CollectionX.groupBy(Arrays.asList(1, 2, 3, 4), i -> i % 2 == 0 ? "even" : "odd");
		assertThat(result).hasSize(2).containsEntry("even", Arrays.asList(2, 4)).containsEntry("odd", Arrays.asList(1, 3));
	}

	@Test
	public void groupBy_KeyMapperReturnsNull_HandlesNullKeys() {
		Map<String, List<Integer>> result = CollectionX.groupBy(Arrays.asList(1, 2, 3), i -> i == 2 ? null : "key");
		assertThat(result).hasSize(2).containsEntry("key", Arrays.asList(1, 3)).containsEntry(null, Collections.singletonList(2));
	}

	@Test
	public void filterAndMap_CollectionIsNull_ReturnsEmptyList() {
		List<String> result = CollectionX.filterAndMap(null, i -> true, Object::toString);
		assertThat(result).isEmpty();
	}

	@Test
	public void filterAndMap_EmptyCollection_ReturnsEmptyList() {
		List<String> result = CollectionX.filterAndMap(Collections.emptyList(), i -> true, Object::toString);
		assertThat(result).isEmpty();
	}

	@Test
	public void filterAndMap_NonEmptyCollection_ReturnsFilteredAndMappedList() {
		List<String> result = CollectionX.filterAndMap(Arrays.asList(1, 2, 3, 4), i -> i % 2 == 0, Object::toString);
		assertThat(result).containsExactly("2", "4");
	}

	@Test
	public void filterAndMap_NoElementsMatchPredicate_ReturnsEmptyList() {
		List<String> result = CollectionX.filterAndMap(Arrays.asList(1, 2, 3), i -> i > 3, Object::toString);
		assertThat(result).isEmpty();
	}

	@Test
	public void mapToParams_NullParams_ReturnsNull() {
		StringBuilder sb = CollectionX.mapToParams(null, null, null);
		assertNull(sb);
	}

	@Test
	public void mapToParams_EmptyParams_ReturnsNull() {
		StringBuilder sb = CollectionX.mapToParams(null, null, new HashMap<>());
		assertNull(sb);
	}

	@Test
	public void mapToParams_NullStringBuilder_CreatesNew() {
		Map<String, Object> params = CollectionX.asLinkedHashMap("key", "value", "age", 18);
		StringBuilder sb = CollectionX.mapToParams(null, null, params, false, null);
		assertEquals("key=value&age=18", sb.toString());

		assertEquals("key=value&age=18", CollectionX.mapToParams(params));
		params.put("name", "中文");
		params.put("price", BigDecimal.valueOf(12345678.91));
		assertEquals("key=value&age=18&name=%E4%B8%AD%E6%96%87&price=12345678.91", CollectionX.mapToParams(params));
	}

	@Test
	public void mapToParams_HasParamTrue_AppendsAmpersand() {
		Map<String, Object> params = CollectionX.asHashMap("key", "value");
		StringBuilder sb = StringX.getBuilder(10, "k1=v1");
		sb = CollectionX.mapToParams(sb, null, params);
		assertEquals("k1=v1&key=value", sb.toString());
	}

	@Test
	public void mapToParams_ConverterReturnsNull_SkipsEntry() {
		Map<String, Object> params = CollectionX.asHashMap("k1", "v1", "k2", "v2");
		Function<Map.Entry<String, Object>, Map.Entry<String, Object>> converter = entry -> null;
		StringBuilder sb = CollectionX.mapToParams(null, null, params, false, converter);
		assertEquals(0, sb.length());
	}

	@Test
	public void mapToParams_ConverterModifiesEntry_UsesModifiedEntry() {
		Map<String, Object> params = CollectionX.asHashMap("key", "value");
		Function<Map.Entry<String, Object>, Map.Entry<String, Object>> converter = entry -> new HashMap.SimpleEntry<>("newKey", "newValue");
		StringBuilder sb = CollectionX.mapToParams(null, null, params, false, converter);
		assertEquals("newKey=newValue", sb.toString());
	}

	@Test
	public void mapToParams_UrlSafeRequired_EncodesValue() {
		Map<String, Object> params = CollectionX.asLinkedHashMap("key", "value with spaces", "name", "中文");
		StringBuilder sb = CollectionX.mapToParams(null, null, true, params);
		assertEquals("key=value+with+spaces&name=%E4%B8%AD%E6%96%87", sb.toString());
	}

	@Test
	public void mapToParams_UrlSafeDisabled_PlainValue() {
		Map<String, Object> params = CollectionX.asLinkedHashMap("key", "value with spaces", "name", "中文");
		StringBuilder sb = CollectionX.mapToParams(null, null, params, false, null);
		assertEquals("key=value with spaces&name=中文", sb.toString());
	}

	/**
	 * 验证 asHashMap(K k1, V v1) 方法能正确创建包含一个键值对的 HashMap
	 */
	@Test
	public void asHashMap_OnePair() {
		HashMap<String, Integer> map = CollectionX.asHashMap("key1", 1);
		assertThat(map).isInstanceOf(HashMap.class).hasSize(1).containsEntry("key1", 1);

		HashMap<String, String> replaced = CollectionX.replaceValues(map, (k, v) -> v.toString());
		assertThat(replaced).isSameAs(map).isInstanceOf(HashMap.class).hasSize(1).containsEntry("key1", "1");
	}

	/**
	 * 验证 asHashMap(K k1, V v1, K k2, V v2) 方法能正确创建包含两个键值对的 HashMap
	 */
	@Test
	public void asHashMap_TwoPairs() {
		HashMap<String, Integer> map = CollectionX.asHashMap("key1", 1, "key2", 2);
		assertThat(map).isInstanceOf(HashMap.class).hasSize(2).containsEntry("key1", 1).containsEntry("key2", 2);

		HashMap<String, Integer> replaced = CollectionX.replaceValues(map, (k, v) -> v * 2);
		assertThat(replaced).isSameAs(map).isInstanceOf(HashMap.class).hasSize(2).containsEntry("key1", 2).containsEntry("key2", 4);

		map = CollectionX.asHashMap("key1", 1, "key1", 2);
		assertThat(map).isInstanceOf(HashMap.class).hasSize(1).containsEntry("key1", 2);

		map = CollectionX.asHashMap(null, 1, "key1", null);
		assertThat(map).isInstanceOf(HashMap.class).hasSize(2).containsEntry(null, 1).containsEntry("key1", null);
	}

	/**
	 * 验证 asHashMap(K k1, V v1, K k2, V v2, K k3, V v3) 方法能正确创建包含三个键值对的 HashMap
	 */
	@Test
	public void asHashMap_ThreePairs() {
		HashMap<String, Integer> map = CollectionX.asHashMap("key1", 1, "key2", 1, "key3", 1);
		assertThat(map).isInstanceOf(HashMap.class).hasSize(3).containsEntry("key1", 1).containsEntry("key2", 1).containsEntry("key3", 1);
	}

	/**
	 * 验证 asHashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) 方法能正确创建包含四个键值对的 HashMap
	 */
	@Test
	public void asHashMap_FourPairs() {
		HashMap<String, Integer> map = CollectionX.asHashMap("key1", 1, "key2", 2, "key3", 3, "key4", 4);
		assertThat(map).isInstanceOf(HashMap.class).hasSize(4).containsEntry("key1", 1).containsEntry("key2", 2).containsEntry("key3", 3).containsEntry("key4", 4);
	}

	/**
	 * 验证 asHashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) 方法能正确创建包含五个键值对的 HashMap
	 */
	@Test
	public void asHashMap_FivePairs() {
		HashMap<String, Integer> map = CollectionX.asHashMap("key1", 1, "key2", 2, "key3", 3, "key4", 4, "key5", 5);
		assertThat(map).isInstanceOf(HashMap.class).hasSize(5).containsEntry("key1", 1).containsEntry("key2", 2).containsEntry("key3", 3).containsEntry("key4", 4).containsEntry("key5", 5);
	}

	/**
	 * 验证 asHashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) 方法能正确创建包含六个键值对的 HashMap
	 */
	@Test
	public void asHashMap_SixPairs() {
		HashMap<String, Integer> map = CollectionX.asHashMap("key1", 1, "key2", 2, "key3", 3, "key4", 4, "key5", 5, "key6", 6);
		assertThat(map).isInstanceOf(HashMap.class).hasSize(6).containsEntry("key1", 1).containsEntry("key2", 2).containsEntry("key3", 3).containsEntry("key4", 4).containsEntry("key5", 5).containsEntry("key6", 6);
	}

	/**
	 * 验证 asHashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, Object... kvPairs) 方法能正确创建包含可变参数键值对的 HashMap
	 */
	@Test
	public void asHashMap_VariablePairs() {
		HashMap<String, Integer> map = CollectionX.asHashMap("key1", 1, "key2", 2, "key3", 3, "key4", 4, "key5", 5, "key6", 6, "key7", 7, "key8", 8);
		assertThat(map).isInstanceOf(HashMap.class).hasSize(8).containsEntry("key1", 1).containsEntry("key2", 2).containsEntry("key3", 3).containsEntry("key4", 4).containsEntry("key5", 5).containsEntry("key6", 6).containsEntry("key7", 7)
				.containsEntry("key8", 8);
	}

	/**
	 * 测试 asHashSet(E e1) 方法
	 */
	@Test
	public void asHashSet_OneElement_ReturnsHashSetWithOneElement() {
		HashSet<String> set = CollectionX.asHashSet("A");
		assertThat(set).isInstanceOf(HashSet.class).hasSize(1).contains("A");
	}

	/**
	 * 测试 asHashSet(E e1, E e2) 方法
	 */
	@Test
	public void asHashSet_TwoElements_ReturnsHashSetWithTwoElements() {
		HashSet<String> set = CollectionX.asHashSet("A", "B");
		assertThat(set).isInstanceOf(HashSet.class).hasSize(2).contains("A", "B");
	}

	/**
	 * 测试 asHashSet(E e1, E e2, E e3) 方法
	 */
	@Test
	public void asHashSet_ThreeElements_ReturnsHashSetWithThreeElements() {
		HashSet<String> set = CollectionX.asHashSet("A", "B", "C");
		assertThat(set).isInstanceOf(HashSet.class).hasSize(3).contains("A", "B", "C");
	}

	/**
	 * 测试 asHashSet(E e1, E e2, E e3, E e4) 方法
	 */
	@Test
	public void asHashSet_FourElements_ReturnsHashSetWithFourElements() {
		HashSet<String> set = CollectionX.asHashSet("A", "B", "C", "D");
		assertThat(set).isInstanceOf(HashSet.class).hasSize(4).contains("A", "B", "C", "D");
	}

	/**
	 * 测试 asHashSet(E e1, E e2, E e3, E e4, E e5) 方法
	 */
	@Test
	public void asHashSet_FiveElements_ReturnsHashSetWithFiveElements() {
		HashSet<String> set = CollectionX.asHashSet("A", "B", "C", "D", "E");
		assertThat(set).isInstanceOf(HashSet.class).hasSize(5).contains("A", "B", "C", "D", "E");
	}

	/**
	 * 测试 asHashSet(E e1, E e2, E e3, E e4, E e5, E e6) 方法
	 */
	@Test
	public void asHashSet_SixElements_ReturnsHashSetWithSixElements() {
		HashSet<String> set = CollectionX.asHashSet("A", "B", "C", "D", "E", "F");
		assertThat(set).isInstanceOf(HashSet.class).hasSize(6).contains("A", "B", "C", "D", "E", "F");
	}

	/**
	 * 测试 asHashSet(E e1, E e2, E e3, E e4, E e5, E e6) 方法
	 */
	@Test
	public void asHashSet_SevenElements_ReturnsHashSetWithSixElements() {
		HashSet<String> set = CollectionX.asHashSet("A", "B", "C", "D", "E", "F", "G");
		assertThat(set).isInstanceOf(HashSet.class).hasSize(7).contains("A", "B", "C", "D", "E", "F", "G");
	}

	/**
	 * 测试重复元素是否会自动去重（HashSet 特性）
	 */
	@Test
	public void asHashSet_DuplicateElements_ReturnsHashSetWithoutDuplicates() {
		HashSet<String> set = CollectionX.asHashSet("A", "B", "A", "C", "B", "D");
		assertThat(set).isInstanceOf(HashSet.class).hasSize(4).contains("A", "B", "C", "D");
	}

	/**
	 * 测试 asLinkedHashSet(E e1)
	 */
	@Test
	public void asLinkedHashSet_OneElement_ReturnsCorrectSet() {
		LinkedHashSet<String> set = CollectionX.asLinkedHashSet("A");
		assertThat(set).isInstanceOf(LinkedHashSet.class).hasSize(1).containsExactly("A");
	}

	/**
	 * 测试 asLinkedHashSet(E e1, E e2)
	 */
	@Test
	public void asLinkedHashSet_TwoElements_ReturnsCorrectSet() {
		LinkedHashSet<String> set = CollectionX.asLinkedHashSet("A", "B");
		assertThat(set).isInstanceOf(LinkedHashSet.class).hasSize(2).containsExactly("A", "B");
	}

	/**
	 * 测试 asLinkedHashSet(E e1, E e2, E e3)
	 */
	@Test
	public void asLinkedHashSet_ThreeElements_ReturnsCorrectSet() {
		LinkedHashSet<String> set = CollectionX.asLinkedHashSet("A", "B", "C");
		assertThat(set).isInstanceOf(LinkedHashSet.class).hasSize(3).containsExactly("A", "B", "C");
	}

	/**
	 * 测试 asLinkedHashSet(E e1, E e2, E e3, E e4)
	 */
	@Test
	public void asLinkedHashSet_FourElements_ReturnsCorrectSet() {
		LinkedHashSet<String> set = CollectionX.asLinkedHashSet("A", "B", "C", "D");
		assertThat(set).isInstanceOf(LinkedHashSet.class).hasSize(4).containsExactly("A", "B", "C", "D");
	}

	/**
	 * 测试 asLinkedHashSet(E e1, E e2, E e3, E e4, E e5)
	 */
	@Test
	public void asLinkedHashSet_FiveElements_ReturnsCorrectSet() {
		LinkedHashSet<String> set = CollectionX.asLinkedHashSet("A", "B", "C", "D", "E");
		assertThat(set).isInstanceOf(LinkedHashSet.class).hasSize(5).containsExactly("A", "B", "C", "D", "E");
	}

	/**
	 * 测试 asLinkedHashSet(E e1, E e2, E e3, E e4, E e5, E e6)
	 */
	@Test
	public void asLinkedHashSet_SixElements_ReturnsCorrectSet() {
		LinkedHashSet<String> set = CollectionX.asLinkedHashSet("A", "B", "C", "D", "E", "F");
		assertThat(set).isInstanceOf(LinkedHashSet.class).hasSize(6).containsExactly("A", "B", "C", "D", "E", "F");
	}

	/**
	 * 测试重复元素是否被正确去重并保持插入顺序
	 */
	@Test
	public void asLinkedHashSet_WithDuplicates_ReturnsDeduplicatedSet() {
		LinkedHashSet<String> set = CollectionX.asLinkedHashSet("A", "B", "A", "C", "B");
		assertThat(set).isInstanceOf(LinkedHashSet.class).hasSize(3).containsExactly("A", "B", "C");
	}

	@Test
	public void asLinkedHashMap_1Pair() {
		LinkedHashMap<String, Integer> map = CollectionX.asLinkedHashMap("a", 1);
		assertThat(map).isInstanceOf(LinkedHashMap.class).hasSize(1).containsEntry("a", 1);
	}

	@Test
	public void asLinkedHashMap_2Pairs() {
		LinkedHashMap<String, Integer> map = CollectionX.asLinkedHashMap("a", 1, "b", 2);
		assertThat(map).isInstanceOf(LinkedHashMap.class).hasSize(2).containsEntry("a", 1).containsEntry("b", 2);
	}

	@Test
	public void asLinkedHashMap_3Pairs() {
		LinkedHashMap<String, Integer> map = CollectionX.asLinkedHashMap("a", 1, "b", 2, "c", 3);
		assertThat(map).isInstanceOf(LinkedHashMap.class).hasSize(3).containsEntry("a", 1).containsEntry("b", 2).containsEntry("c", 3);
	}

	@Test
	public void asLinkedHashMap_4Pairs() {
		LinkedHashMap<String, Integer> map = CollectionX.asLinkedHashMap("a", 1, "b", 2, "c", 3, "d", 4);
		assertThat(map).isInstanceOf(LinkedHashMap.class).hasSize(4).containsEntry("a", 1).containsEntry("b", 2).containsEntry("c", 3).containsEntry("d", 4);
	}

	@Test
	public void asLinkedHashMap_5Pairs() {
		LinkedHashMap<String, Integer> map = CollectionX.asLinkedHashMap("a", 1, "b", 2, "c", 3, "d", 4, "e", 5);
		assertThat(map).isInstanceOf(LinkedHashMap.class).hasSize(5).containsEntry("a", 1).containsEntry("b", 2).containsEntry("c", 3).containsEntry("d", 4).containsEntry("e", 5);
	}

	@Test
	public void asLinkedHashMap_6Pairs() {
		LinkedHashMap<String, Integer> map = CollectionX.asLinkedHashMap("a", 1, "b", 2, "c", 3, "d", 4, "e", 5, "f", 6);
		assertThat(map).isInstanceOf(LinkedHashMap.class).hasSize(6).containsEntry("a", 1).containsEntry("b", 2).containsEntry("c", 3).containsEntry("d", 4).containsEntry("e", 5).containsEntry("f", 6);
	}

	@Test
	public void asLinkedHashMap_WithVarargs() {
		LinkedHashMap<String, Integer> map = CollectionX.asLinkedHashMap("a", 1, "b", 2, "c", 3, "d", 4, "e", 5, "f", 6, "g", 7, "h", 8);
		assertThat(map).isInstanceOf(LinkedHashMap.class).hasSize(8).containsEntry("a", 1).containsEntry("b", 2).containsEntry("c", 3).containsEntry("d", 4).containsEntry("e", 5).containsEntry("f", 6).containsEntry("g", 7)
				.containsEntry("h", 8);
	}

	@Test
	public void asLinkedHashMap_WithNullKeyAndValue() {
		LinkedHashMap<String, String> map = CollectionX.asLinkedHashMap(null, "value", "key", null);
		assertThat(map).isInstanceOf(LinkedHashMap.class).hasSize(2).containsEntry(null, "value").containsEntry("key", null);
	}

	@Test
	public void asLinkedHashMap_InsertionOrderPreserved() {
		LinkedHashMap<String, Integer> map = CollectionX.asLinkedHashMap("z", 26, "a", 1, "m", 13);
		assertThat(map.keySet()).containsExactly("z", "a", "m");
	}

	@Test
	public void asLinkedHashMap_VarargsOddNumber_ThrowsException() {
		assertThatThrownBy(() -> CollectionX.asRawLinkedHashMap("a", 1, "b", 2, "c")) // 奇数个参数
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void get_MapIsNull_ReturnsNull() {
		Map<String, String> map = null;
		assertNull(CollectionX.get(map, "key"));
	}

	@Test
	public void get_MapIsEmpty_ReturnsNull() {
		Map<String, String> map = new HashMap<>();
		assertThat(CollectionX.get(map, "key")).isNull();
		Object val = CollectionX.get(Collections.emptyMap(), "key");
		assertNull(val);
	}

	@Test
	public void get_KeyNotExists_ReturnsNull() {
		Map<String, String> map = CollectionX.asHashMap("name", "Tom");
		assertNull(CollectionX.get(map, "age"));
	}

	@Test
	public void get_KeyExists_ReturnsValue() {
		Map<String, String> map = CollectionX.asHashMap("name", "Tom");
		assertThat(CollectionX.get(map, "name")).isEqualTo("Tom");
	}

	@Test
	public void getOrDefault_MapIsNull_ReturnsDefaultValue() {
		assertThat(CollectionX.getOrDefault(null, "key", "default")).isEqualTo("default");
	}

	@Test
	public void getOrDefault_MapIsEmpty_ReturnsDefaultValue() {
		Map<String, String> map = new HashMap<>();
		assertThat(CollectionX.getOrDefault(map, "key", "default")).isEqualTo("default");

		assertThat(CollectionX.getOrDefault(Collections.emptyMap(), "key", "default")).isEqualTo("default");
	}

	@Test
	public void getOrDefault_KeyNotExists_ReturnsDefaultValue() {
		Map<String, String> map = CollectionX.asHashMap("name", "Tom");
		assertThat(CollectionX.getOrDefault(map, "age", "default")).isEqualTo("default");
	}

	@Test
	public void getOrDefault_KeyExists_ReturnsValue() {
		Map<String, String> map = CollectionX.asHashMap("name", "Tom");
		assertThat(CollectionX.getOrDefault(map, "name", "default")).isEqualTo("Tom");
	}

	@Test
	public void getNonNull_MapIsNull_ReturnsNull() {
		Map<String, String> map = null;
		assertThat(CollectionX.getNonNull(map, "key1", "key2")).isNull();

		String val = CollectionX.getNonNull(Collections.emptyMap(), "key1", "key2");
		assertNull(val);
	}

	@Test
	public void getNonNull_Key1NotExistsButKey2Exists_ReturnsKey2Value() {
		Map<String, String> map = CollectionX.asHashMap("key2", "value2");
		assertThat(CollectionX.getNonNull(map, "key1", "key2")).isEqualTo("value2");
	}

	@Test
	public void getNonNull_Key1Exists_ReturnsKey1Value() {
		Map<String, String> map = CollectionX.asHashMap("key1", "value1", "key2", "value2");
		assertThat(CollectionX.getNonNull(map, "key1", "key2")).isEqualTo("value1");
	}

	@Test
	public void getOrDefault_TwoKeys_MapIsNull_ReturnsDefaultValue() {
		assertThat(CollectionX.getOrDefault(null, "key1", "key2", "default")).isEqualTo("default");
	}

	@Test
	public void getOrDefault_TwoKeys_Key1NotExistsButKey2Exists_ReturnsKey2Value() {
		Map<String, String> map = CollectionX.asHashMap("key2", "value2");
		assertThat(CollectionX.getOrDefault(map, "key1", "key2", "default")).isEqualTo("value2");
	}

	@Test
	public void getOrDefault_TwoKeys_Key1Exists_ReturnsKey1Value() {
		Map<String, String> map = CollectionX.asHashMap("key1", "value1", "key2", "value2");
		assertThat(CollectionX.getOrDefault(map, "key1", "key2", "default")).isEqualTo("value1");
	}

	@Test
	public void getOrDefault_TwoKeys_BothKeysNotExists_ReturnsDefaultValue() {
		Map<String, String> map = new HashMap<>();
		assertThat(CollectionX.getOrDefault(map, "key1", "key2", "default")).isEqualTo("default");
	}

	@Test
	public void getOrDefault_TwoMaps_Map1IsNull_ReturnsMap2Value() {
		Map<String, String> map2 = CollectionX.asHashMap("key", "value2");
		assertThat(CollectionX.getOrDefault(null, "key1", map2, "key", "default")).isEqualTo("value2");
	}

	@Test
	public void getOrDefault_TwoMaps_Map1KeyNotExistsButMap2KeyExists_ReturnsMap2Value() {
		Map<String, String> map1 = new HashMap<>();
		Map<String, String> map2 = CollectionX.asHashMap("key", "value2");
		assertThat(CollectionX.getOrDefault(map1, "key1", map2, "key", "default")).isEqualTo("value2");
	}

	@Test
	public void getOrDefault_TwoMaps_Map1HasKey_ReturnsMap1Value() {
		Map<String, String> map1 = CollectionX.asHashMap("key", "value1");
		Map<String, String> map2 = CollectionX.asHashMap("key", "value2");
		assertThat(CollectionX.getOrDefault(map1, "key", map2, "key", "default")).isEqualTo("value1");
	}

	@Test
	public void getOrDefault_TwoMaps_BothMapsNull_ReturnsDefaultValue() {
		assertThat(CollectionX.getOrDefault(null, "key1", null, "key2", "default")).isEqualTo("default");
	}

	@Test
	public void getOrDefault_TwoMapsSameKey_Map1HasKey_ReturnsMap1Value() {
		Map<String, String> map1 = CollectionX.asHashMap("key", "value1");
		Map<String, String> map2 = CollectionX.asHashMap("key", "value2");
		assertThat(CollectionX.getOrDefault(map1, map2, "key", "default")).isEqualTo("value1");
	}

	@Test
	public void getOrDefault_TwoMapsSameKey_Map1KeyNullButMap2HasKey_ReturnsMap2Value() {
		Map<String, String> map1 = CollectionX.asHashMap("key", null);
		Map<String, String> map2 = CollectionX.asHashMap("key", "value2");
		assertThat(CollectionX.getOrDefault(map1, map2, "key", "default")).isEqualTo("value2");
	}

	@Test
	public void getNonNull_TwoMaps_Map1HasKey_ReturnsMap1Value() {
		Map<String, String> map1 = CollectionX.asHashMap("key", "value1");
		Map<String, String> map2 = CollectionX.asHashMap("key", "value2");
		assertThat(CollectionX.getNonNull(map1, map2, "key")).isEqualTo("value1");
	}

	@Test
	public void getNonNull_TwoMaps_Map1KeyNullButMap2HasKey_ReturnsMap2Value() {
		Map<String, String> map1 = CollectionX.asHashMap("key", null);
		Map<String, String> map2 = CollectionX.asHashMap("key", "value2");
		assertThat(CollectionX.getNonNull(map1, map2, "key")).isEqualTo("value2");
	}

	@Test
	public void getNonNull_TwoMaps_BothMapsNull_ReturnsNull() {
		Map<String, Object> map1 = null, map2 = null;
		assertNull(CollectionX.getNonNull(map1, map2, "key"));
	}

	// ========================
	// List 版本 replaceValues 测试
	// ========================

	@Test
	public void replaceValues_ListIsNull_ReturnsNull() {
		Function<String, Integer> converter = String::length;
		List<Integer> result = CollectionX.replaceValues(null, converter);
		assertThat(result).isNull();
	}

	@Test
	public void replaceValues_ListIsEmpty_ReturnsSameList() {
		List<String> list = new ArrayList<>();
		Function<String, Integer> converter = String::length;
		ArrayList<Integer> result = CollectionX.replaceValues(list, converter);
		assertThat(result).isSameAs(list).isEmpty();

		list = Collections.emptyList();
		List<Integer> result2 = CollectionX.replaceValues(list, converter);
		assertThat(result2).isSameAs(list).isEmpty();
	}

	@Test
	public void replaceValues_ListIsNotEmpty_ReplacesAllElements() {
		List<String> list = CollectionX.asArrayList("a", "bb", "ccc");
		Function<String, Integer> converter = String::length;
		List<Integer> result = CollectionX.replaceValues(list, converter);

		assertThat(result).isSameAs(list);
		assertThat(result).containsExactly(1, 2, 3);
	}

	@Test
	public void replaceValues_ConverterThrowsException_ThrowsException() {
		List<String> list = new ArrayList<>(Arrays.asList("a", "bb"));
		Function<String, Integer> converter = s -> {
			if ("bb".equals(s)) {
				throw new RuntimeException("模拟异常");
			}
			return s.length();
		};

		assertThatThrownBy(() -> CollectionX.replaceValues(list, converter)).isInstanceOf(RuntimeException.class).hasMessage("模拟异常");
	}

	@Test
	public void replaceValues_UnmodifiableList_ThrowsException() {
		List<String> list = Collections.unmodifiableList(CollectionX.asArrayList("1", "2", "3"));
		assertThatThrownBy(() -> CollectionX.replaceValues(list, String::length)).isInstanceOf(UnsupportedOperationException.class);
	}

	// ========================
	// Map 版本 replaceValues 测试
	// ========================

	@Test
	public void replaceValues_MapIsNull_ReturnsNull() {
		BiFunction<String, Object, String> converter = (k, v) -> k + "=" + v;
		Map<String, String> result = CollectionX.replaceValues(null, converter);
		assertThat(result).isNull();
	}

	@Test
	public void replaceValues_MapIsEmpty_ReturnsSameMap() {
		Map<String, Object> map = new HashMap<>();
		BiFunction<String, Object, String> converter = (k, v) -> k + "=" + v;
		Map<String, String> result = CollectionX.replaceValues(map, converter);
		assertThat(result).isSameAs(map).isEmpty();

		map = Collections.emptyMap();
		result = CollectionX.replaceValues(map, converter);
		assertThat(result).isSameAs(map).isEmpty();
	}

	@Test
	public void replaceValues_MapIsNotEmpty_ReplacesAllValues() {
		HashMap<String, Object> map = CollectionX.asHashMap("name", "Tom", "age", 18);

		BiFunction<String, Object, String> converter = (k, v) -> k + "=" + v;
		HashMap<String, String> result = CollectionX.replaceValues(map, converter);

		assertThat(result).isSameAs(map);
		assertThat(map).containsEntry("name", "name=Tom").containsEntry("age", "age=18");
	}

	@Test
	public void replaceValues_MapConverterThrowsException_ThrowsException() {
		Map<String, Object> map = CollectionX.asHashMap("name", "Tom");

		BiFunction<String, Object, String> converter = (k, v) -> {
			if ("name".equals(k)) {
				throw new RuntimeException("转换异常");
			}
			return k + "=" + v;
		};
		assertThatThrownBy(() -> CollectionX.replaceValues(map, converter)).isInstanceOf(RuntimeException.class).hasMessage("转换异常");
	}

	@Test
	public void replaceValues_UnmodifiableMap_ThrowsException() {
		Map<String, Object> map = Collections.unmodifiableMap(CollectionX.asHashMap("name", "Tom"));

		BiFunction<String, Object, String> converter = (k, v) -> {
			if ("name".equals(k)) {
				throw new RuntimeException("转换异常");
			}
			return k + "=" + v;
		};
		assertThatThrownBy(() -> CollectionX.replaceValues(map, converter)).isInstanceOf(UnsupportedOperationException.class);
	}

}