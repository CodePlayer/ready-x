package me.codeplayer.util;

import java.util.*;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayXTest {

	@Test
	public void isArray_NullObject_ReturnsFalse() {
		assertFalse(ArrayX.isArray(null));
	}

	@Test
	public void isArray_ArrayObject_ReturnsTrue() {
		int[] array = { 1, 2, 3 };
		assertTrue(ArrayX.isArray(array));
	}

	@Test
	public void isArray_NonArrayObject_ReturnsFalse() {
		String str = "Hello";
		assertFalse(ArrayX.isArray(str));
	}

	@Test
	public void isArray_EmptyArray_ReturnsTrue() {
		int[] emptyArray = {};
		assertTrue(ArrayX.isArray(emptyArray));
	}

	@Test
	public void isArray_ObjectArray_ReturnsTrue() {
		Object[] objArray = { 1, "string", 3.14 };
		assertTrue(ArrayX.isArray(objArray));
	}

	@Test
	public void join_NullArray_ThrowsException() {
		assertThrows(NullPointerException.class, () -> ArrayX.join(null, null, ","));
	}

	@Test
	public void join_NullStringBuilder_CreatesNewStringBuilder() {
		int[] array = { 1, 2, 3 };
		String result = ArrayX.join(null, array, ",").toString();
		assertEquals("1,2,3", result);
	}

	@Test
	public void join_EmptyArray_ReturnsEmptyStringBuilder() {
		int[] array = {};
		StringBuilder sb = new StringBuilder();
		StringBuilder result = ArrayX.join(sb, array, ",");
		assertEquals("", result.toString());
	}

	@Test
	public void join_IntArray_CorrectJoin() {
		int[] array = { 1, 2, 3 };
		String result = ArrayX.join(array, ",");
		assertEquals("1,2,3", result);
	}

	@Test
	public void join_LongArray_CorrectJoin() {
		long[] array = { 1L, 2L, 3L };
		String result = ArrayX.join(array, ",");
		assertEquals("1,2,3", result);
	}

	@Test
	public void join_IntegerArray_CorrectJoin() {
		Integer[] array = { 1, 2, 3 };
		String result = ArrayX.join(array, ",");
		assertEquals("1,2,3", result);
	}

	@Test
	public void join_LongObjectArray_CorrectJoin() {
		Long[] array = { 1L, 2L, 3L };
		String result = ArrayX.join(array, ",");
		assertEquals("1,2,3", result);
	}

	@Test
	public void join_MixedArray_CorrectJoin() {
		Object[] array = { 1, 2L, "3" };
		String result = ArrayX.join(array, ",");
		assertEquals("1,2,3", result);
	}

	@Test
	public void join_ArrayWithNulls_CorrectJoin() {
		Object[] array = { 1, null, "3" };
		String result = ArrayX.join(array, ",");
		assertEquals("1,null,3", result);
	}

	@Test
	public void getInSQL_EmptyArray_ThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> ArrayX.getInSQL(new int[] {}, true, true));
	}

	@Test
	public void getInSQL_SingleElementIncludeString_ReturnsEquals() {
		String result = ArrayX.getInSQL(new String[] { "1" }, true, true);
		assertEquals(" IN ('1')", result);
	}

	@Test
	public void getInSQL_SingleElementIncludeNumeric_ReturnsEquals() {
		String result = ArrayX.getInSQL(new int[] { 1 }, true, false);
		assertEquals(" IN (1)", result);
	}

	@Test
	public void getInSQL_SingleElementExcludeString_ReturnsNotEquals() {
		String result = ArrayX.getInSQL(new String[] { "1" }, false, true);
		assertEquals(" NOT IN ('1')", result);
	}

	@Test
	public void getInSQL_SingleElementExcludeNumeric_ReturnsNotEquals() {
		String result = ArrayX.getInSQL(new int[] { 1 }, false, false);
		assertEquals(" NOT IN (1)", result);
	}

	@Test
	public void getInSQL_MultipleElementsIncludeString_ReturnsInClause() {
		String result = ArrayX.getInSQL(new String[] { "1", "2", "3" }, true, true);
		assertEquals(" IN ('1', '2', '3')", result);
	}

	@Test
	public void getInSQL_MultipleElementsIncludeNumeric_ReturnsInClause() {
		String result = ArrayX.getInSQL(new int[] { 1, 2, 3 }, true, false);
		assertEquals(" IN (1, 2, 3)", result);
	}

	@Test
	public void getInSQL_MultipleElementsExcludeString_ReturnsNotInClause() {
		String result = ArrayX.getInSQL(new String[] { "1", "2", "3" }, false, true);
		assertEquals(" NOT IN ('1', '2', '3')", result);
	}

	@Test
	public void getInSQL_MultipleElementsExcludeNumeric_ReturnsNotInClause() {
		String result = ArrayX.getInSQL(new int[] { 1, 2, 3 }, false, false);
		assertEquals(" NOT IN (1, 2, 3)", result);
	}

	@Test
	public void getInSQL2_EmptyArray_ThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> ArrayX.getInSQL(new int[] {}, true));
	}

	@Test
	public void getInSQL2_SingleElementIncludeString_ReturnsEquals() {
		String result = ArrayX.getInSQL(new String[] { "1" }, true);
		assertEquals(" IN ('1')", result);
	}

	@Test
	public void getInSQL2_SingleElementIncludeNumeric_ReturnsEquals() {
		String result = ArrayX.getInSQL(new int[] { 1 }, false);
		assertEquals(" IN (1)", result);
	}

	@Test
	public void getInSQL2_MultipleElementsIncludeString_ReturnsInClause() {
		String result = ArrayX.getInSQL(new String[] { "1", "2", "3" }, true);
		assertEquals(" IN ('1', '2', '3')", result);
	}

	@Test
	public void getInSQL2_MultipleElementsIncludeNumeric_ReturnsInClause() {
		String result = ArrayX.getInSQL(new int[] { 1, 2, 3 }, false);
		assertEquals(" IN (1, 2, 3)", result);
	}

	@Test
	public void toString_NullInput_ReturnsNullString() {
		assertEquals("null", ArrayX.toString(null));
	}

	@Test
	public void toString_NonArrayInput_ReturnsStringRepresentation() {
		String str = "Hello";
		assertEquals("Hello", ArrayX.toString(str));
	}

	@Test
	public void toString_EmptyArray_ReturnsEmptyBrackets() {
		int[] emptyArray = {};
		assertEquals("[]", ArrayX.toString(emptyArray));
	}

	@Test
	public void toString_NonEmptyIntArray_ReturnsStringRepresentation() {
		int[] array = { 1, 2, 3 };
		assertEquals("[1, 2, 3]", ArrayX.toString(array));
	}

	@Test
	public void toString_NonEmptyObjectArray_ReturnsStringRepresentation() {
		Object[] objArray = { 1, "string", 3.14 };
		assertEquals("[1, string, 3.14]", ArrayX.toString(objArray));
	}

	@Test
	public void toString_ArrayWithNullElements_ReturnsStringRepresentation() {
		Object[] arrayWithNulls = { 1, null, "3" };
		assertEquals("[1, null, 3]", ArrayX.toString(arrayWithNulls));
	}

	@Test
	public void toFinalString_NullArray_ReturnsNullString() {
		StringBuilder sb = new StringBuilder();
		StringBuilder result = ArrayX.toFinalString(sb, null);
		assertEquals("null", result.toString());
	}

	@Test
	public void toFinalString_NonArrayObject_ReturnsObjectString() {
		StringBuilder sb = new StringBuilder();
		StringBuilder result = ArrayX.toFinalString(sb, "Hello");
		assertEquals("Hello", result.toString());
	}

	@Test
	public void toFinalString_EmptyArray_ReturnsEmptyBrackets() {
		StringBuilder sb = new StringBuilder();
		StringBuilder result = ArrayX.toFinalString(sb, new int[] {});
		assertEquals("[]", result.toString());
	}

	@Test
	public void toFinalString_NonEmptyArray_ReturnsArrayString() {
		StringBuilder sb = new StringBuilder();
		final int[] array = { 1, 2, 3 };
		StringBuilder result = ArrayX.toFinalString(sb, array);
		assertEquals("[1, 2, 3]", result.toString());
	}

	@Test
	public void toFinalString_MultiDimensionalArray_ReturnsNestedArrayString() {
		Object[] array = { 1, new int[] { 2, 3 }, new Object[] { 4, new int[] { 5, 6 } } };
		String result = ArrayX.toFinalString(array);
		assertEquals("[1, [2, 3], [4, [5, 6]]]", result);
	}

	@Test
	public void toFinalString_MixedTypeArray_ReturnsMixedArrayString() {
		Object[] array = { 1, "string", 3.14 };
		String result = ArrayX.toFinalString(array);
		assertEquals("[1, string, 3.14]", result);
	}

	@Test
	public void hasLength_NullArray_ReturnsFalse() {
		assertFalse(ArrayX.hasLength(null));
	}

	@Test
	public void hasLength_EmptyArray_ReturnsFalse() {
		int[] emptyArray = {};
		assertFalse(ArrayX.hasLength(emptyArray));
	}

	@Test
	public void hasLength_NonEmptyArray_ReturnsTrue() {
		int[] array = { 1, 2, 3 };
		assertTrue(ArrayX.hasLength(array));
	}

	@Test
	public void hasLength_NonArrayType_ThrowsException() {
		String str = "Hello";
		assertThrows(IllegalArgumentException.class, () -> ArrayX.hasLength(str));
	}

	@Test
	public void getLength_AssertNotEmpty_NullArray_ThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> ArrayX.getLength(null, true));
	}

	@Test
	public void getLength_AssertNotEmpty_EmptyArray_ThrowsException() {
		int[] emptyArray = {};
		assertThrows(IllegalArgumentException.class, () -> ArrayX.getLength(emptyArray, true));
	}

	@Test
	public void getLength_AssertNotEmpty_NonEmptyArray_ReturnsLength() {
		int[] array = { 1, 2, 3 };
		assertEquals(3, ArrayX.getLength(array, true));
	}

	@Test
	public void getLength_AssertNotNotEmpty_EmptyArray_ReturnsZero() {
		int[] emptyArray = {};
		assertEquals(0, ArrayX.getLength(emptyArray, false));
	}

	@Test
	public void getLength_NullArray_ReturnsZero() {
		assertEquals(0, ArrayX.getLength((Object) null));
	}

	@Test
	public void getLength_EmptyArray_ReturnsZero() {
		int[] emptyArray = {};
		assertEquals(0, ArrayX.getLength(emptyArray));
	}

	@Test
	public void getLength_NonEmptyArray_ReturnsLength() {
		int[] array = { 1, 2, 3 };
		assertEquals(3, ArrayX.getLength(array));
	}

	@Test
	public void getLength_NonArrayType_ThrowsException() {
		String str = "Hello";
		assertThrows(IllegalArgumentException.class, () -> ArrayX.getLength(str));
	}

	@Test
	public void getLength_ObjectArray_NullArray_ReturnsZero() {
		assertEquals(0, ArrayX.getLength((Object[]) null));
	}

	@Test
	public void getLength_ObjectArray_EmptyArray_ReturnsZero() {
		Object[] emptyArray = {};
		assertEquals(0, ArrayX.getLength(emptyArray));
	}

	@Test
	public void getLength_ObjectArray_NonEmptyArray_ReturnsLength() {
		Object[] array = { 1, 2, 3 };
		assertEquals(3, ArrayX.getLength(array));
	}

	@Test
	public void getDimension_NullObject_ReturnsMinusOne() {
		assertEquals(-1, ArrayX.getDimension(null));
	}

	@Test
	public void getDimension_Array_ReturnsDimension() {
		int[][] array = { { 1, 2 }, { 3, 4 } };
		assertEquals(2, ArrayX.getDimension(array));
	}

	@Test
	public void getDimension_NonArray_ReturnsMinusOne() {
		String str = "Hello";
		assertEquals(-1, ArrayX.getDimension(str));
	}

	@Test
	public void isPrimitiveArray_NullObject_ReturnsFalse() {
		assertFalse(ArrayX.isPrimitiveArray(null));
	}

	@Test
	public void isPrimitiveArray_NonArrayObject_ReturnsFalse() {
		String str = "Hello";
		assertFalse(ArrayX.isPrimitiveArray(str));
	}

	@Test
	public void isPrimitiveArray_PrimitiveArray_ReturnsTrue() {
		int[] intArray = { 1, 2, 3 };
		assertTrue(ArrayX.isPrimitiveArray(intArray));
	}

	@Test
	public void isPrimitiveArray_NonPrimitiveArray_ReturnsFalse() {
		Integer[] integerArray = { 1, 2, 3 };
		assertFalse(ArrayX.isPrimitiveArray(integerArray));
	}

	@Test
	public void in_EmptyArray_ReturnsFalse() {
		assertFalse(ArrayX.in(1, new int[] {}));
	}

	@Test
	public void in_ValueInArray_ReturnsTrue() {
		assertTrue(ArrayX.in(2, 1, 2, 3));
	}

	@Test
	public void in_ValueNotInArray_ReturnsFalse() {
		assertFalse(ArrayX.in(4, 1, 2, 3));
	}

	@Test
	public void ins_EmptyArray_ReturnsFalse() {
		assertFalse(ArrayX.ins(1, new Integer[] {}));
	}

	@Test
	public void ins_ValueInArray_ReturnsTrue() {
		assertTrue(ArrayX.ins(2, 1, 2, 3));
	}

	@Test
	public void ins_ValueNotInArray_ReturnsFalse() {
		assertFalse(ArrayX.ins(4, 1, 2, 3));
	}

	@Test
	public void ins_NullValueInArray_ReturnsTrue() {
		assertTrue(ArrayX.ins(null, 1, null, 3));
	}

	@Test
	public void ins_NullValueNotInArray_ReturnsFalse() {
		assertFalse(ArrayX.ins(null, 1, 2, 3));
	}

	@Test
	public void ins_ValueIsNull_AllNulls_ReturnsTrue() {
		assertTrue(ArrayX.ins(null, null, null));
		assertTrue(ArrayX.ins(null, null, null, null));
		assertTrue(ArrayX.ins(null, null, null, null, null));
	}

	@Test
	public void ins_ValueIsNull_SomeNulls_ReturnsTrue() {
		assertTrue(ArrayX.ins(null, null, "a"));
		assertTrue(ArrayX.ins(null, "a", null));
		assertTrue(ArrayX.ins(null, null, "a", "b"));
		assertTrue(ArrayX.ins(null, "a", null, "b"));
		assertTrue(ArrayX.ins(null, "a", "b", null));
		assertTrue(ArrayX.ins(null, null, "a", "b", "c"));
		assertTrue(ArrayX.ins(null, "a", null, "b", "c"));
		assertTrue(ArrayX.ins(null, "a", "b", null, "c"));
		assertTrue(ArrayX.ins(null, "a", "b", "c", null));
	}

	@Test
	public void ins_ValueIsNull_NoNulls_ReturnsFalse() {
		assertFalse(ArrayX.ins(null, "a", "b"));
		assertFalse(ArrayX.ins(null, "a", "b", "c"));
		assertFalse(ArrayX.ins(null, "a", "b", "c", "d"));
	}

	@Test
	public void ins_ValueIsNotNull_ValueEqualsOne_ReturnsTrue() {
		assertTrue(ArrayX.ins("a", "a", "b"));
		assertTrue(ArrayX.ins("a", "b", "a"));
		assertTrue(ArrayX.ins("a", "a", "b", "c"));
		assertTrue(ArrayX.ins("a", "b", "a", "c"));
		assertTrue(ArrayX.ins("a", "b", "c", "a"));
		assertTrue(ArrayX.ins("a", "a", "b", "c", "d"));
		assertTrue(ArrayX.ins("a", "b", "a", "c", "d"));
		assertTrue(ArrayX.ins("a", "b", "c", "a", "d"));
		assertTrue(ArrayX.ins("a", "b", "c", "d", "a"));
	}

	@Test
	public void ins_ValueIsNotNull_ValueDoesNotEqualAny_ReturnsFalse() {
		assertFalse(ArrayX.ins("a", "b", "c"));
		assertFalse(ArrayX.ins("a", "b", "c", "d"));
		assertFalse(ArrayX.ins("a", "b", "c", "d", "e"));
	}

	@Test
	public void indexOfInterval_NullOrEmptyArray_ReturnsMinusOne() {
		assertEquals(-1, ArrayX.indexOfInterval(null, 5, true));
		assertEquals(-1, ArrayX.indexOfInterval(new Integer[] {}, 5, true));
	}

	@Test
	public void indexOfInterval_UnableToDetermineOrder_ThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> ArrayX.indexOfInterval(new Integer[] { 5 }, 5, null));
	}

	@Test
	public void indexOfInterval_UnableToDetermineOrder_ThrowsExceptionForEqualElements() {
		assertThrows(IllegalArgumentException.class, () -> ArrayX.indexOfInterval(new Integer[] { 5, 5 }, 5, null));
	}

	@Test
	public void indexOfInterval_AscendingOrder_ReturnsCorrectIndex() {
		Integer[] array = { 5, 10, 20, 50, 100 };
		assertEquals(0, ArrayX.indexOfInterval(array, 5, true));
		assertEquals(1, ArrayX.indexOfInterval(array, 12, true));
		assertEquals(4, ArrayX.indexOfInterval(array, 100, true));
		assertEquals(-1, ArrayX.indexOfInterval(array, 4, true));
	}

	@Test
	public void indexOfInterval_DescendingOrder_ReturnsCorrectIndex() {
		Integer[] array = { 100, 50, 20, 10, 5 };
		assertEquals(0, ArrayX.indexOfInterval(array, 100, false));
		assertEquals(0, ArrayX.indexOfInterval(array, 55, false));
		assertEquals(4, ArrayX.indexOfInterval(array, 5, false));
		assertEquals(-1, ArrayX.indexOfInterval(array, 101, false));
	}

	@Test
	public void unique_ForceNewCopy_ReturnsNewArray() {
		Integer[] array = { 1, 2, 2, 3 };
		Integer[] result = (Integer[]) ArrayX.unique(array);
		assertNotSame(array, result);
		assertArrayEquals(new Integer[] { 1, 2, 3 }, result);
	}

	@Test
	public void unique_NoForceNewCopy_ReturnsSameArrayIfNoDuplicates() {
		Integer[] array = { 1, 2, 3 };
		Integer[] result = (Integer[]) ArrayX.unique(array, false);
		assertSame(array, result);
	}

	@Test
	public void toArray_NullCollection_ReturnsNull() {
		assertNull(ArrayX.toArray(null, Integer.class));
	}

	@Test
	public void toArray_Collection_ReturnsArray() {
		Collection<Integer> collection = Arrays.asList(1, 2, 3);
		Integer[] result = ArrayX.toArray(collection, Integer.class);
		assertArrayEquals(new Integer[] { 1, 2, 3 }, result);
	}

	@Test
	public void of_NullElements_ReturnsNull() {
		Object[] values = ArrayX.ofNull();
		assertNull(values);
	}

	@Test
	public void filter_EmptyArray_ReturnsEmptyArray() {
		Integer[] result = ArrayX.filter(new Integer[] {}, x -> x > 0);
		assertArrayEquals(new Integer[] {}, result);
	}

	@Test
	public void filter_NonEmptyArray_ReturnsFilteredArray() {
		Integer[] result = ArrayX.filter(new Integer[] { 1, 2, 3, 4 }, x -> x % 2 == 0);
		assertArrayEquals(new Integer[] { 2, 4 }, result);
	}

	@Test
	public void matchAny_EmptyArray_ReturnsFalse() {
		final Integer[] values = {};
		assertFalse(ArrayX.matchAny(x -> x > 0, values));
	}

	@Test
	public void matchAny_NonEmptyArray_ReturnsTrueIfAnyMatch() {
		assertTrue(ArrayX.matchAny(x -> x > 0, 1, 2, 3));
	}

	@Test
	public void matchAll_EmptyArray_ReturnsTrue() {
		final Integer[] values = {};
		assertFalse(ArrayX.matchAll(x -> x > 0, values));
	}

	@Test
	public void matchAll_NonEmptyArray_ReturnsFalseIfNotAllMatch() {
		assertFalse(ArrayX.matchAll(x -> x > 0, 1, -2, 3));
	}

	@Test
	public void ofNullable_NullElement_ReturnsNull() {
		//noinspection ConstantValue
		Object[] array = ArrayX.ofNullable(null);
		assertNull(array);
	}

	@Test
	public void ofNullable_IntegerElement() {
		Integer[] array = ArrayX.ofNullable(1);
		assertNotNull(array);
		assertEquals(1, array.length);
		assertEquals((Integer) 1, array[0]);
	}

	@Test
	public void ofNullable_NonNullElement_ReturnsArrayWithElement() {
		Integer element = 42;
		Integer[] result = ArrayX.ofNullable(element);
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals(element, result[0]);
	}

	@Test
	public void ofNullable_NonNullStringElement_ReturnsArrayWithElement() {
		String element = "test";
		String[] result = ArrayX.ofNullable(element);
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals(element, result[0]);
	}

	@Test
	public void ofNullable_NonNullObjectElement_ReturnsArrayWithElement() {
		Object element = new Object();
		Object[] result = ArrayX.ofNullable(element);
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals(element, result[0]);
	}

	@Test
	public void toArray_NullIterable_ReturnsNull() {
		String[] result = ArrayX.toArray(null, String.class, Object::toString);
		assertNull(result);
	}

	@Test
	public void toArray_NonEmptyCollection_ReturnsArray() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		String[] result = ArrayX.toArray(list, String.class, Object::toString);
		assertArrayEquals(new String[] { "1", "2", "3" }, result);
	}

	@Test
	public void toArray_EmptyCollection_ReturnsEmptyArray() {
		List<Integer> list = Collections.emptyList();
		String[] result = ArrayX.toArray(list, String.class, Object::toString);
		assertArrayEquals(new String[] {}, result);
	}

	@Test
	public void toArray_NonEmptyIterable_ReturnsArray() {
		Iterable<Integer> iterable = Arrays.asList(1, 2, 3);
		String[] result = ArrayX.toArray(iterable, String.class, Object::toString);
		assertArrayEquals(new String[] { "1", "2", "3" }, result);
	}

	@Test
	public void toArray_EmptyIterable_ReturnsEmptyArray() {
		Iterable<Integer> iterable = Collections.emptyList();
		String[] result = ArrayX.toArray(iterable, String.class, Object::toString);
		assertArrayEquals(new String[] {}, result);
	}

	/**
	 * 测试 toArray 方法 - 当 items 为 null 时，应返回 null
	 */
	@Test
	public void toArrayWhenItemsIsNull() {
		String[] result = ArrayX.toArray(String.class, Object::toString, (String[]) null);
		assertNull(result);
	}

	/**
	 * 测试 toArray 方法 - 当 items 为空数组时，应返回空的目标类型数组
	 */
	@Test
	public void toArrayWhenItemsIsEmpty() {
		String[] result = ArrayX.toArray(String.class, Object::toString);
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
		String[] result = ArrayX.toArray(String.class, mapper, input);

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
		String result = ArrayX.get((String[]) null, 0);
		assertNull(result);
	}

	/**
	 * 测试 get(E[] array, int index) 方法 - 当 index 为负数时，应返回 null
	 */
	@Test
	public void getWithTypedArrayWhenIndexIsNegative() {
		String[] array = { "a", "b", "c" };
		String result = ArrayX.get(array, -1);
		assertNull(result);
	}

	/**
	 * 测试 get(E[] array, int index) 方法 - 当 index 超出数组范围时，应返回 null
	 */
	@Test
	public void getWithTypedArrayWhenIndexIsOutOfRange() {
		String[] array = { "a", "b", "c" };
		String result = ArrayX.get(array, 3);
		assertNull(result);
	}

	/**
	 * 测试 get(E[] array, int index) 方法 - 当 index 在有效范围内时，应返回对应元素
	 */
	@Test
	public void getWithTypedArrayWhenIndexIsValid() {
		String[] array = { "a", "b", "c" };
		String result = ArrayX.get(array, 1);
		assertEquals("b", result);
	}

	/**
	 * 测试 get(Object array, int index) 方法 - 当 array 为 null 时，应返回 null
	 */
	@Test
	public void getWithObjectArrayWhenArrayIsNull() {
		String result = ArrayX.get((Object) null, 0);
		assertNull(result);
	}

	/**
	 * 测试 get(Object array, int index) 方法 - 当 index 为负数时，应返回 null
	 */
	@Test
	public void getWithObjectArrayWhenIndexIsNegative() {
		String[] array = { "a", "b", "c" };
		String result = ArrayX.get(array, -1);
		assertNull(result);
	}

	/**
	 * 测试 get(Object array, int index) 方法 - 当 index 超出数组范围时，应返回 null
	 */
	@Test
	public void getWithObjectArrayWhenIndexIsOutOfRange() {
		String[] array = { "a", "b", "c" };
		String result = ArrayX.get(array, 3);
		assertNull(result);
	}

	/**
	 * 测试 get(Object array, int index) 方法 - 当 index 在有效范围内时，应返回对应元素
	 */
	@Test
	public void getWithObjectArrayWhenIndexIsValid() {
		String[] array = { "a", "b", "c" };
		String result = ArrayX.get((Object) array, 1);
		assertEquals("b", result);
	}

	/**
	 * 测试 get(Object array, int index) 方法 - 处理基本类型数组
	 */
	@Test
	public void getWithObjectArrayWhenArrayIsPrimitive() {
		int[] array = { 10, 20, 30 };
		Integer result = ArrayX.get(array, 1);
		assertEquals(Integer.valueOf(20), result);
	}

	/**
	 * 测试 toArray 方法 - 处理字符串到整数的转换
	 */
	@Test
	public void toArrayWithStringToIntegerConversion() {
		String[] input = { "1", "2", "3" };
		Function<String, Integer> mapper = Integer::parseInt;
		Integer[] result = ArrayX.toArray(Integer.class, mapper, input);

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
		assertThrows(NullPointerException.class, () -> ArrayX.filter(x -> true, (Integer[]) null));
	}

	/**
	 * 测试 filter 方法 - 当 array 为空数组时，应返回空数组
	 */
	@Test
	public void filterWhenArrayIsEmpty() {
		Integer[] input = {};
		Integer[] result = ArrayX.filter(x -> true, input);
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * 测试 filter 方法 - 当所有元素都满足条件时，应返回原数组副本
	 */
	@Test
	public void filterWhenAllElementsMatch() {
		Integer[] input = { 1, 2, 3 };
		Integer[] result = ArrayX.filter(x -> true, input);
		assertArrayEquals(input, result);
	}

	/**
	 * 测试 filter 方法 - 当部分元素满足条件时，应返回满足条件的元素组成的新数组
	 */
	@Test
	public void filterWhenSomeElementsMatch() {
		Integer[] input = { 1, 2, 3, 4, 5 };
		Integer[] result = ArrayX.filter(x -> x > 2, input);
		assertArrayEquals(new Integer[] { 3, 4, 5 }, result);
	}

	/**
	 * 测试 filter 方法 - 当没有元素满足条件时，应返回空数组
	 */
	@Test
	public void filterWhenNoElementsMatch() {
		Integer[] input = { 1, 2, 3 };
		Integer[] result = ArrayX.filter(x -> false, input);
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * 测试 toCollection 方法 - 当 items 为 null 时，应返回 null
	 */
	@Test
	public void toCollectionWhenItemsIsNull() {
		ArrayList<String> result = ArrayX.toCollection(ArrayList::new, Object::toString, true, (String[]) null);
		assertNull(result);
	}

	/**
	 * 测试 toCollection 方法 - 当 allowNull 为 true 时，应包含 null 值
	 */
	@Test
	public void toCollectionWhenAllowNullIsTrue() {
		String[] input = { "a", null, "b" };
		ArrayList<String> result = ArrayX.toCollection(ArrayList::new, Function.identity(), true, input);
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
		ArrayList<String> result = ArrayX.toCollection(ArrayList::new, Function.identity(), false, input);
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
		ArrayList<String> result = ArrayX.toList(Function.identity(), true, input);
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
		ArrayList<String> result = ArrayX.toList(Function.identity(), false, input);
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
		ArrayList<String> result = ArrayX.toList(Function.identity(), input);
		assertEquals(3, result.size()); // 默认 allowNull = true
	}

	/**
	 * 测试 toList 方法 - 当 items 为 null 时，应返回 null
	 */
	@Test
	public void toListWhenItemsIsNull() {
		ArrayList<String> result = ArrayX.toList(Function.identity(), (String[]) null);
		assertNull(result);
	}

	/**
	 * 测试 toSet 方法 - 当 allowNull 为 true 时，应包含 null 值
	 */
	@Test
	public void toSetWhenAllowNullIsTrue() {
		String[] input = { "a", null, "b", "a" };
		HashSet<String> result = ArrayX.toSet(Function.identity(), true, input);
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
		HashSet<String> result = ArrayX.toSet(Function.identity(), false, input);
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
		HashSet<String> result = ArrayX.toSet(Function.identity(), input);
		assertEquals(3, result.size()); // 默认 allowNull = true
	}

	/**
	 * 测试 toSet 方法 - 当 items 为 null 时，应返回 null
	 */
	@Test
	public void toSetWhenItemsIsNull() {
		HashSet<String> result = ArrayX.toSet(Function.identity(), (String[]) null);
		assertNull(result);
	}

	/**
	 * 测试 filter 方法 - 验证返回数组的正确性
	 */
	@Test
	public void filterReturnsCorrectArray() {
		Integer[] input = { 1, 2, 3, 4, 5, 6 };
		Integer[] result = ArrayX.filter(x -> x % 2 == 0, input);
		assertArrayEquals(new Integer[] { 2, 4, 6 }, result);
	}

}