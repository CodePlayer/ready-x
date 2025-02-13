package me.codeplayer.util;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import org.assertj.core.api.WithAssertions;
import org.assertj.core.data.Index;
import org.junit.Assert;
import org.junit.Test;

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
	public void mapValues() {
		HashMap<Object, Object> map = CollectionX.asHashMap("name", "Tom", "age", 18, "color", "Yellow");
		Object[] array = CollectionX.mapValues(map, Object.class, "name", "age", "haha");
		assertThat(array)
				.hasSize(3)
				.contains("Tom", Index.atIndex(0))
				.contains(null, Index.atIndex(2));
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
		assertThat(set).hasSize(5)
				.contains(1, 2, 3, 4, 5);
	}

	@Test
	public void get_ListIsNull_ReturnsDefaultValue() {
		assertThat(CollectionX.get(null, 0, "default")).isEqualTo("default");
	}

	@Test
	public void get_IndexOutOfBounds_ReturnsDefaultValue() {
		List<String> list = Arrays.asList("a", "b");
		assertThat(CollectionX.get(list, 2, "default")).isEqualTo("default");
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
		List<String> target = new ArrayList<>();
		Predicate<String> filter = s -> s.length() > 1;
		CollectionX.addAll(target, filter, "a", "abc", "de");
		assertThat(target).containsExactly("abc", "de");
	}

	@Test
	public void addAll_WithoutFilter_AddsAllElements() {
		List<String> target = CollectionX.asArrayList("a");
		Collection<String> result = CollectionX.addAll(target, "a", "abc", "de");
		Assert.assertSame(result, target);
		assertThat(target).containsExactly("a", "a", "abc", "de");
	}

	@Test
	public void addAll_MapWithKeyValuePairs_AddsCorrectly() {
		Map<String, Integer> map = new HashMap<>();
		Map<String, Integer> result = CollectionX.addAll(map, "key1", 1, "key2", 2);
		Assert.assertSame(result, map);
		assertThat(map).containsEntry("key1", 1).containsEntry("key2", 2);
	}

	@Test
	public void addAll_MapWithOddNumberOfKeyValuePairs_ThrowsException() {
		Map<String, Integer> map = new HashMap<>();
		assertThatThrownBy(() -> CollectionX.addAll(map, "key1", 1, "key2"))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void asList_EmptyInput_ReturnsEmptyList() {
		List<Integer> list = CollectionX.asList(ArrayList::new);
		assertThat(list)
				.isExactlyInstanceOf(ArrayList.class)
				.isEmpty();
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
	public void asHashMap_EmptyInput_ReturnsEmptyMap() {
		Map<String, Integer> map = CollectionX.asHashMap();
		assertThat(map).isEmpty();
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
		assertThat(set)
				.isExactlyInstanceOf(LinkedHashSet.class)
				.isEmpty();
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
		assertThat(map)
				.isInstanceOf(LinkedHashMap.class)
				.hasSize(2)
				.containsEntry("name", "Tom")
				.containsEntry("age", 18);
	}

	@Test(expected = IllegalArgumentException.class)
	public void asLinkedHashMap_OddNumberOfElements_IgnoresLastElement() {
		Map<String, Object> map = CollectionX.asLinkedHashMap("name", "Tom", "age");
		assertThat(map)
				.isInstanceOf(LinkedHashMap.class)
				.hasSize(2)
				.containsEntry("name", "Tom")
				.containsEntry("age", 18)
		;
	}

	@Test
	public void asLinkedHashMap_EmptyInput_ReturnsEmptyMap() {
		Map<String, Object> map = CollectionX.asLinkedHashMap();
		assertThat(map)
				.isInstanceOf(LinkedHashMap.class)
				.isEmpty();
	}

	@Test(expected = IllegalArgumentException.class)
	public void asLinkedHashMap_SingleElement_ReturnsEmptyMap() {
		Map<String, Object> map = CollectionX.asLinkedHashMap("name");
	}

	@Test
	public void asLinkedHashMap_DuplicateKeys_KeepsLastValue() {
		Map<String, Object> map = CollectionX.asLinkedHashMap("name", "Tom", "name", "Jerry");
		assertThat(map)
				.isInstanceOf(LinkedHashMap.class)
				.hasSize(1)
				.containsEntry("name", "Jerry");
	}

	@Test
	public void toMap_NullItems_ReturnsEmptyMap() {
		List<String> items = null;
		Map<String, String> map = CollectionX.toMap(HashMap::new, items, Function.identity());
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
		Assert.assertTrue(map.isEmpty());
	}

	@Test
	public void toHashMap_EmptyIterable_ReturnsEmptyMap() {
		Iterable<Integer> items = Collections.emptyList();
		HashMap<String, Integer> map = CollectionX.toHashMap(items, Object::toString);
		Assert.assertTrue(map.isEmpty());
	}

	@Test
	public void toHashMap_SingleElement_CorrectlyMapsToMap() {
		Iterable<Integer> items = Collections.singletonList(1);
		HashMap<String, Integer> map = CollectionX.toHashMap(items, Object::toString);
		Assert.assertEquals(1, map.size());
		Assert.assertEquals(Integer.valueOf(1), map.get("1"));
	}

	@Test
	public void toHashMap_MultipleElements_CorrectlyMapsToMap() {
		Iterable<Integer> items = Arrays.asList(1, 2, 3);
		HashMap<String, Integer> map = CollectionX.toHashMap(items, Object::toString);
		Assert.assertEquals(3, map.size());
		Assert.assertEquals(Integer.valueOf(1), map.get("1"));
		Assert.assertEquals(Integer.valueOf(2), map.get("2"));
		Assert.assertEquals(Integer.valueOf(3), map.get("3"));
	}

	@Test
	public void toHashMap_DuplicateKeys_LastValueOverwrites() {
		Iterable<Integer> items = Arrays.asList(1, 2, 1);
		HashMap<String, Integer> map = CollectionX.toHashMap(items, Object::toString);
		Assert.assertEquals(2, map.size());
		Assert.assertEquals(Integer.valueOf(2), map.get("2"));
		Assert.assertEquals(Integer.valueOf(1), map.get("1"));
	}

	@Test
	public void mapValues_NullMap_ReturnsNullArray() {
		Object[] values = CollectionX.mapValues(null, Object.class, "k1", "k2");
		Assert.assertArrayEquals(new Object[2], values);
	}

	@Test
	public void mapValues_EmptyMap_ReturnsNullArray() {
		Map<String, Object> map = new HashMap<>();
		Object[] values = CollectionX.mapValues(map, Object.class, "k1", "k2");
		Assert.assertArrayEquals(new Object[2], values);
	}

	@Test
	public void mapValues_KeysNotExist_ReturnsNullArray() {
		Map<String, Object> map = CollectionX.asHashMap("k3", "v3");
		Object[] values = CollectionX.mapValues(map, Object.class, "k1", "k2");
		Assert.assertArrayEquals(new Object[2], values);
	}

	@Test
	public void mapValues_KeysExist_ReturnsArray() {
		Map<String, String> map = CollectionX.asHashMap("k1", "v1", "k2", "v2", "k3", "v3");
		String[] values = CollectionX.mapValues(map, String.class, "k1", "k3", "k4");
		Assert.assertArrayEquals(new String[] { "v1", "v3", null }, values);
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
		assertThat(result).hasSize(2)
				.containsEntry("even", Arrays.asList(2, 4))
				.containsEntry("odd", Arrays.asList(1, 3));
	}

	@Test
	public void groupBy_KeyMapperReturnsNull_HandlesNullKeys() {
		Map<String, List<Integer>> result = CollectionX.groupBy(Arrays.asList(1, 2, 3), i -> i == 2 ? null : "key");
		assertThat(result).hasSize(2)
				.containsEntry("key", Arrays.asList(1, 3))
				.containsEntry(null, Collections.singletonList(2));
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
		Assert.assertNull(sb);
	}

	@Test
	public void mapToParams_EmptyParams_ReturnsNull() {
		StringBuilder sb = CollectionX.mapToParams(null, null, new HashMap<>());
		Assert.assertNull(sb);
	}

	@Test
	public void mapToParams_NullStringBuilder_CreatesNew() {
		Map<String, Object> params = CollectionX.asLinkedHashMap("key", "value", "age", 18);
		StringBuilder sb = CollectionX.mapToParams(null, null, params, false, null);
		Assert.assertEquals("key=value&age=18", sb.toString());

		Assert.assertEquals("key=value&age=18", CollectionX.mapToParams(params));
		params.put("name", "中文");
		params.put("price", BigDecimal.valueOf(12345678.91));
		Assert.assertEquals("key=value&age=18&name=%E4%B8%AD%E6%96%87&price=12345678.91", CollectionX.mapToParams(params));
	}

	@Test
	public void mapToParams_HasParamTrue_AppendsAmpersand() {
		Map<String, Object> params = CollectionX.asHashMap("key", "value");
		StringBuilder sb = new StringBuilder("k1=v1");
		sb = CollectionX.mapToParams(sb, null, params);
		Assert.assertEquals("k1=v1&key=value", sb.toString());
	}

	@Test
	public void mapToParams_ConverterReturnsNull_SkipsEntry() {
		Map<String, Object> params = CollectionX.asHashMap("k1", "v1", "k2", "v2");
		Function<Map.Entry<String, Object>, Map.Entry<String, Object>> converter = entry -> null;
		StringBuilder sb = CollectionX.mapToParams(null, null, params, false, converter);
		Assert.assertEquals(0, sb.length());
	}

	@Test
	public void mapToParams_ConverterModifiesEntry_UsesModifiedEntry() {
		Map<String, Object> params = CollectionX.asHashMap("key", "value");
		Function<Map.Entry<String, Object>, Map.Entry<String, Object>> converter = entry -> new HashMap.SimpleEntry<>("newKey", "newValue");
		StringBuilder sb = CollectionX.mapToParams(null, null, params, false, converter);
		Assert.assertEquals("newKey=newValue", sb.toString());
	}

	@Test
	public void mapToParams_UrlSafeRequired_EncodesValue() {
		Map<String, Object> params = CollectionX.asLinkedHashMap("key", "value with spaces", "name", "中文");
		StringBuilder sb = CollectionX.mapToParams(null, null, true, params);
		Assert.assertEquals("key=value+with+spaces&name=%E4%B8%AD%E6%96%87", sb.toString());
	}

	@Test
	public void mapToParams_UrlSafeDisabled_PlainValue() {
		Map<String, Object> params = CollectionX.asLinkedHashMap("key", "value with spaces", "name", "中文");
		StringBuilder sb = CollectionX.mapToParams(null, null, params, false, null);
		Assert.assertEquals("key=value with spaces&name=中文", sb.toString());
	}

}