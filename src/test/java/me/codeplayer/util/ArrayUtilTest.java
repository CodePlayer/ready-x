package me.codeplayer.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayUtilTest {

	/**
	 * 测试 toArray 方法 - 当 items 为 null 时，应返回 null
	 */
	@Test
	public void toArrayWhenItemsIsNull() {
		String[] result = ArrayUtil.toArray(String.class, Object::toString, (String[]) null);
		assertNull(result);
	}

	/**
	 * 测试 toArray 方法 - 当 items 为空数组时，应返回空的目标类型数组
	 */
	@Test
	public void toArrayWhenItemsIsEmpty() {
		String[] result = ArrayUtil.toArray(String.class, Object::toString);
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * 测试 toArray 方法 - 当 items 包含多个元素时，应正确映射并返回目标类型数组
	 */
	@Test
	public void toArrayWhenItemsHasElements() {
		Integer[] input = { 1, 2, 3 };
		Function<Integer, String> mapper = Object::toString;
		String[] result = ArrayUtil.toArray(String.class, mapper, input);

		assertNotNull(result);
		assertEquals(3, result.length);
		assertEquals("1", result[0]);
		assertEquals("2", result[1]);
		assertEquals("3", result[2]);
	}

	/**
	 * 测试 get(E[] array, int index) 方法 - 当 array 为 null 时，应返回 null
	 */
	@Test
	public void getWithTypedArrayWhenArrayIsNull() {
		String result = ArrayUtil.get((String[]) null, 0);
		assertNull(result);
	}

	/**
	 * 测试 get(E[] array, int index) 方法 - 当 index 为负数时，应返回 null
	 */
	@Test
	public void getWithTypedArrayWhenIndexIsNegative() {
		String[] array = { "a", "b", "c" };
		String result = ArrayUtil.get(array, -1);
		assertNull(result);
	}

	/**
	 * 测试 get(E[] array, int index) 方法 - 当 index 超出数组范围时，应返回 null
	 */
	@Test
	public void getWithTypedArrayWhenIndexIsOutOfRange() {
		String[] array = { "a", "b", "c" };
		String result = ArrayUtil.get(array, 3);
		assertNull(result);
	}

	/**
	 * 测试 get(E[] array, int index) 方法 - 当 index 在有效范围内时，应返回对应元素
	 */
	@Test
	public void getWithTypedArrayWhenIndexIsValid() {
		String[] array = { "a", "b", "c" };
		String result = ArrayUtil.get(array, 1);
		assertEquals("b", result);
	}

	/**
	 * 测试 get(Object array, int index) 方法 - 当 array 为 null 时，应返回 null
	 */
	@Test
	public void getWithObjectArrayWhenArrayIsNull() {
		String result = ArrayUtil.get((Object) null, 0);
		assertNull(result);
	}

	/**
	 * 测试 get(Object array, int index) 方法 - 当 index 为负数时，应返回 null
	 */
	@Test
	public void getWithObjectArrayWhenIndexIsNegative() {
		String[] array = { "a", "b", "c" };
		String result = ArrayUtil.get(array, -1);
		assertNull(result);
	}

	/**
	 * 测试 get(Object array, int index) 方法 - 当 index 超出数组范围时，应返回 null
	 */
	@Test
	public void getWithObjectArrayWhenIndexIsOutOfRange() {
		String[] array = { "a", "b", "c" };
		String result = ArrayUtil.get(array, 3);
		assertNull(result);
	}

	/**
	 * 测试 get(Object array, int index) 方法 - 当 index 在有效范围内时，应返回对应元素
	 */
	@Test
	public void getWithObjectArrayWhenIndexIsValid() {
		String[] array = { "a", "b", "c" };
		String result = ArrayUtil.get((Object) array, 1);
		assertEquals("b", result);
	}

	/**
	 * 测试 get(Object array, int index) 方法 - 处理基本类型数组
	 */
	@Test
	public void getWithObjectArrayWhenArrayIsPrimitive() {
		int[] array = { 10, 20, 30 };
		Integer result = ArrayUtil.get(array, 1);
		assertEquals(Integer.valueOf(20), result);
	}

	/**
	 * 测试 toArray 方法 - 处理字符串到整数的转换
	 */
	@Test
	public void toArrayWithStringToIntegerConversion() {
		String[] input = { "1", "2", "3" };
		Function<String, Integer> mapper = Integer::parseInt;
		Integer[] result = ArrayUtil.toArray(Integer.class, mapper, input);

		assertNotNull(result);
		assertEquals(3, result.length);
		assertEquals(Integer.valueOf(1), result[0]);
		assertEquals(Integer.valueOf(2), result[1]);
		assertEquals(Integer.valueOf(3), result[2]);
	}

	/**
	 * 测试 filter 方法 - 当 array 为 null 时，应返回 null
	 */
	@Test
	public void filterWhenArrayIsNull() {
		assertThrows(NullPointerException.class, () -> ArrayUtil.filter(x -> true, (Integer[]) null));
	}

	/**
	 * 测试 filter 方法 - 当 array 为空数组时，应返回空数组
	 */
	@Test
	public void filterWhenArrayIsEmpty() {
		Integer[] input = {};
		Integer[] result = ArrayUtil.filter(x -> true, input);
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * 测试 filter 方法 - 当所有元素都满足条件时，应返回原数组副本
	 */
	@Test
	public void filterWhenAllElementsMatch() {
		Integer[] input = { 1, 2, 3 };
		Integer[] result = ArrayUtil.filter(x -> true, input);
		assertArrayEquals(input, result);
	}

	/**
	 * 测试 filter 方法 - 当部分元素满足条件时，应返回满足条件的元素组成的新数组
	 */
	@Test
	public void filterWhenSomeElementsMatch() {
		Integer[] input = { 1, 2, 3, 4, 5 };
		Integer[] result = ArrayUtil.filter(x -> x > 2, input);
		assertArrayEquals(new Integer[] { 3, 4, 5 }, result);
	}

	/**
	 * 测试 filter 方法 - 当没有元素满足条件时，应返回空数组
	 */
	@Test
	public void filterWhenNoElementsMatch() {
		Integer[] input = { 1, 2, 3 };
		Integer[] result = ArrayUtil.filter(x -> false, input);
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * 测试 toCollection 方法 - 当 items 为 null 时，应返回 null
	 */
	@Test
	public void toCollectionWhenItemsIsNull() {
		ArrayList<String> result = ArrayUtil.toCollection(ArrayList::new, Object::toString, true, (String[]) null);
		assertNull(result);
	}

	/**
	 * 测试 toCollection 方法 - 当 allowNull 为 true 时，应包含 null 值
	 */
	@Test
	public void toCollectionWhenAllowNullIsTrue() {
		String[] input = { "a", null, "b" };
		ArrayList<String> result = ArrayUtil.toCollection(ArrayList::new, Function.identity(), true, input);
		assertEquals(3, result.size());
		assertEquals("a", result.get(0));
		assertNull(result.get(1));
		assertEquals("b", result.get(2));
	}

	/**
	 * 测试 toCollection 方法 - 当 allowNull 为 false 时，应过滤掉 null 值
	 */
	@Test
	public void toCollectionWhenAllowNullIsFalse() {
		String[] input = { "a", null, "b" };
		ArrayList<String> result = ArrayUtil.toCollection(ArrayList::new, Function.identity(), false, input);
		assertEquals(2, result.size());
		assertEquals("a", result.get(0));
		assertEquals("b", result.get(1));
	}

	/**
	 * 测试 toList 方法 - 当 allowNull 为 true 时，应包含 null 值
	 */
	@Test
	public void toListWhenAllowNullIsTrue() {
		String[] input = { "a", null, "b" };
		ArrayList<String> result = ArrayUtil.toList(Function.identity(), true, input);
		assertEquals(3, result.size());
		assertEquals("a", result.get(0));
		assertNull(result.get(1));
		assertEquals("b", result.get(2));
	}

	/**
	 * 测试 toList 方法 - 当 allowNull 为 false 时，应过滤掉 null 值
	 */
	@Test
	public void toListWhenAllowNullIsFalse() {
		String[] input = { "a", null, "b" };
		ArrayList<String> result = ArrayUtil.toList(Function.identity(), false, input);
		assertEquals(2, result.size());
		assertEquals("a", result.get(0));
		assertEquals("b", result.get(1));
	}

	/**
	 * 测试 toList 方法 - 使用默认的 allowNull 值（true）
	 */
	@Test
	public void toListWithDefaultAllowNull() {
		String[] input = { "a", null, "b" };
		ArrayList<String> result = ArrayUtil.toList(Function.identity(), input);
		assertEquals(3, result.size()); // 默认 allowNull = true
	}

	/**
	 * 测试 toList 方法 - 当 items 为 null 时，应返回 null
	 */
	@Test
	public void toListWhenItemsIsNull() {
		ArrayList<String> result = ArrayUtil.toList(Function.identity(), (String[]) null);
		assertNull(result);
	}

	/**
	 * 测试 toSet 方法 - 当 allowNull 为 true 时，应包含 null 值
	 */
	@Test
	public void toSetWhenAllowNullIsTrue() {
		String[] input = { "a", null, "b", "a" };
		HashSet<String> result = ArrayUtil.toSet(Function.identity(), true, input);
		assertEquals(3, result.size()); // a, null, b
		assertTrue(result.contains("a"));
		assertTrue(result.contains(null));
		assertTrue(result.contains("b"));
	}

	/**
	 * 测试 toSet 方法 - 当 allowNull 为 false 时，应过滤掉 null 值
	 */
	@Test
	public void toSetWhenAllowNullIsFalse() {
		String[] input = { "a", null, "b", "a" };
		HashSet<String> result = ArrayUtil.toSet(Function.identity(), false, input);
		assertEquals(2, result.size()); // a, b
		assertTrue(result.contains("a"));
		assertTrue(result.contains("b"));
	}

	/**
	 * 测试 toSet 方法 - 使用默认的 allowNull 值（true）
	 */
	@Test
	public void toSetWithDefaultAllowNull() {
		String[] input = { "a", null, "b" };
		HashSet<String> result = ArrayUtil.toSet(Function.identity(), input);
		assertEquals(3, result.size()); // 默认 allowNull = true
	}

	/**
	 * 测试 toSet 方法 - 当 items 为 null 时，应返回 null
	 */
	@Test
	public void toSetWhenItemsIsNull() {
		HashSet<String> result = ArrayUtil.toSet(Function.identity(), (String[]) null);
		assertNull(result);
	}

	/**
	 * 测试 filter 方法 - 验证返回数组的正确性
	 */
	@Test
	public void filterReturnsCorrectArray() {
		Integer[] input = { 1, 2, 3, 4, 5, 6 };
		Integer[] result = ArrayUtil.filter(x -> x % 2 == 0, input);
		assertArrayEquals(new Integer[] { 2, 4, 6 }, result);
	}

}