package me.codeplayer.util;

import java.util.*;

import org.assertj.core.api.*;
import org.assertj.core.data.*;
import org.junit.*;

public class CollectionUtilTest implements WithAssertions {

	@Test
	public void ofArrayList() {
		ArrayList<Integer> list = CollectionUtil.asArrayList(1, 2, 3, 4);
		assertThat(list)
				.isInstanceOf(ArrayList.class)
				.hasSize(4)
				.contains(3, Index.atIndex(2));
	}

	@Test
	public void checkPairs() {
		assertThatThrownBy(() -> CollectionUtil.checkPairs("A", "B", "C")).isInstanceOf(IllegalArgumentException.class);

		assertThatCode(() -> CollectionUtil.checkPairs("A", "B", "C", "D")).doesNotThrowAnyException();
	}

	@Test
	public void ofHashMap() {
		HashMap<Object, Object> map = CollectionUtil.asHashMap("name", "Tom", "age", 18);
		assertThat(map)
				.isInstanceOf(HashMap.class)
				.hasSize(2)
				.containsEntry("name", "Tom")
				.containsEntry("age", 18);
	}

	@Test
	public void mapValues() {
		HashMap<Object, Object> map = CollectionUtil.asHashMap("name", "Tom", "age", 18, "color", "Yellow");
		Object[] array = CollectionUtil.mapValues(map, Object.class, "name", "age", "haha");
		assertThat(array)
				.hasSize(3)
				.contains("Tom", Index.atIndex(0))
				.contains(null, Index.atIndex(2));
	}

	@Test
	public void findFirst() {
		ArrayList<Integer> ids = CollectionUtil.asArrayList(2, 1, 3, 4);
		Integer first = CollectionUtil.findFirst(ids, i -> i > 3);
		assertThat(first).isEqualTo(4);

		assertThat(CollectionUtil.getAny(ids)).isEqualTo(2);
	}

	@Test
	public void ofHashSet() {
		HashSet<Integer> set = CollectionUtil.asHashSet(1, 2, 3, 4, 3, 5);
		assertThat(set).hasSize(5)
				.contains(1, 2, 3, 4, 5);
	}
}