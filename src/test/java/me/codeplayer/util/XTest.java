package me.codeplayer.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import me.codeplayer.util.JsonXTest.User;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class XTest implements WithAssertions {

	@SuppressWarnings("ConstantValue")
	@Test
	public void isValid() {
		// Boolean
		assertTrue(X.isValid(true));

		assertFalse(X.isValid((Boolean) null));
		assertFalse(X.isValid(false));

		// Number
		int i = 0;
		assertTrue(X.isValid(-5));
		assertTrue(X.isValid(100.12));
		assertTrue(X.isValid(123L));
		assertTrue(X.isValid(-123L));
		assertTrue(X.isValid(123.45));
		assertTrue(X.isValid(-123.45));
		assertTrue(X.isValid(BigInteger.TEN));
		assertTrue(X.isValid(BigDecimal.TEN));

		assertFalse(X.isValid(i));
		assertFalse(X.isValid(0));
		assertFalse(X.isValid(0L));
		assertFalse(X.isValid(0.0));
		assertFalse(X.isValid(0.0F));
		assertFalse(X.isValid(BigDecimal.ZERO));
		assertFalse(X.isValid(BigInteger.ZERO));

		// CharSequence
		assertTrue(X.isValid("abc"));
		assertTrue(X.isValid("Code Player"));
		assertTrue(X.isValid(" "));

		assertFalse(X.isValid(""));

		//  Array & Collection & Map
		assertTrue(X.isValid(new int[1]));
		assertTrue(X.isValid(new byte[] { 1, 2, 3 }));
		Object[] array = new Integer[] { 1, 2, 3 };
		assertTrue((X.isValid(array)));
		assertTrue(X.isValid(new int[] { 1, 2, 3 }));
		assertTrue(X.isValid(new long[] { 1L, 2L, 3L }));
		assertTrue(X.isValid(new char[] { 'a', 'b', 'c' }));
		assertTrue(X.isValid(new float[] { 1.0f, 2.0f, 3.0f }));
		assertTrue(X.isValid(new double[] { 1.0, 2.0, 3.0 }));
		assertTrue(X.isValid(new Object[] { JavaX.javaVersion, 2, 3 }));

		List<Integer> list = Arrays.asList(1, 2, 3);
		assertTrue(X.isValid(list));
		HashMap<Object, Object> map = CollectionX.asHashMap("name", "James", "age", 18);
		assertTrue(X.isValid(map));
		assertTrue(X.isValid(new int[] { 1, 2, 3 }));
		Map<String, Integer> map1 = CollectionX.asHashMap("key", 1);
		assertTrue(X.isValid(map1));
		List<String> list1 = Arrays.asList("a", "b", "c");
		assertTrue(X.isValid(list1));

		assertFalse(X.isValid(new int[0]));
		assertFalse(X.isValid(Collections.emptyList()));
		assertFalse(X.isValid(Collections.emptySet()));
		assertFalse(X.isValid(Arrays.asList()));
		assertFalse(X.isValid((Collection<?>) null));

		assertFalse(X.isValid(Collections.emptyMap()));
		assertFalse(X.isValid((Map<?, ?>) null));

		assertFalse(X.isValid((byte[]) null));
		assertFalse(X.isValid(new byte[0]));
		assertFalse(X.isValid((int[]) null));
		assertFalse(X.isValid(new int[0]));
		assertFalse(X.isValid((long[]) null));
		assertFalse(X.isValid(new long[0]));
		assertFalse(X.isValid((char[]) null));
		assertFalse(X.isValid(new char[0]));
		assertFalse(X.isValid((float[]) null));
		assertFalse(X.isValid(new float[0]));
		assertFalse(X.isValid((double[]) null));
		assertFalse(X.isValid(new double[0]));
		assertFalse(X.isValid(ArrayX.ofNullable(null)));
		assertFalse(X.isValid(ArrayX.of()));
		assertFalse(X.isValid(new Object[0]));

		assertTrue(X.isValid(new Object()));
		assertTrue(X.isValid(new AssertException("errorMsg", null)));
		assertTrue(X.isValid(new AssertException((Throwable) null)));
		assertTrue(X.isValid(new AssertException((Throwable) null)));
		assertTrue(X.isValid(new AssertException("errorMsg", null, false, false)));
		assertFalse(X.isValid((Object) null));
	}

	@Test
	public void test() {
		User nullUser = null;

		User user = new User();
		user.setName("James");

		assertThat(X.expectNotNull(nullUser, user)).isSameAs(user);

		assertThat(X.getElse(nullUser, User::new)).isNotNull();

		assertThat(X.map(user, User::getName)).isEqualTo("James");
		assertThat(X.map(nullUser, User::getName)).isNull();

		assertThat(X.map(user, User::getName, String::length)).isEqualTo(5);

		assertThat(X.isMatch(user, User::getName, StringX::notEmpty)).isTrue();

		assertThat(X.eqLax(nullUser, user)).isEqualTo(0);

		assertThat(X.eqLax(nullUser, user, User::getName)).isEqualTo(0);

		assertThat(X.size("acsd中国")).isEqualTo(6);

		assertThat(X.sizeOfArray(new int[3])).isEqualTo(3);

		assertThat(X.mapElse(user, User::getPassword, "Hello")).isEqualTo("Hello");

		assertThat(X.mapElseGet(user, User::getPassword, () -> StringX.replaceChars("18000001234", '*', 3, -4))).isEqualTo("180****1234");

		assertThat((Object) X.decode(1, 0, "PENDING", 1, "YES", -1, "NO")).isEqualTo("YES");

		assertThat((String) X.tryUnwrap((Supplier<String>) () -> StringX.replaceSubstring("Hello", "+++", 1, -1))).isEqualTo("H+++o");

		user = Mockito.spy(user);

		X.with(user, User::getName);

		Mockito.verify(user).getName();
	}

	@Test
	public void expectNotEmpty() {
		assertThat(X.expectNotEmpty(null, "123")).isEqualTo("123");
		assertThat(X.expectNotEmpty("James", "Hello")).isEqualTo("James");
		assertEquals("", X.expectNotEmpty(null, null));
		assertEquals("Code", X.expectNotEmpty("Code", null));
		assertEquals(" ", X.expectNotEmpty(" ", null));
		assertEquals("Player", X.expectNotEmpty("", "Player"));
		assertEquals(" ", X.expectNotEmpty("", " "));

		assertEquals("", X.expectNotEmpty(null, null, null));
		assertEquals("", X.expectNotEmpty("", "", ""));
		assertEquals(" ", X.expectNotEmpty("", "", " "));
		assertEquals("v1", X.expectNotEmpty("v1", null, null));
		assertEquals("v2", X.expectNotEmpty(null, "v2", null));
		assertEquals("v3", X.expectNotEmpty(null, null, "v3"));
		assertEquals("v2", X.expectNotEmpty("", "v2", null));
		assertEquals("v3", X.expectNotEmpty("", "", "v3"));
		assertEquals("", X.expectNotEmpty("", null, ""));

		assertEquals("", X.expectNotEmpty(null, null, null, null));
		assertEquals("v4", X.expectNotEmpty(null, null, null, "v4"));
	}

	@Test
	public void size_Collection_Null_ReturnsZero() {
		assertEquals(0, X.size((Collection<?>) null));
	}

	@Test
	public void size_Collection_Empty_ReturnsZero() {
		assertEquals(0, X.size(Collections.emptyList()));
	}

	@Test
	public void size_Collection_NonEmpty_ReturnsSize() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		assertEquals(3, X.size(list));
	}

	@Test
	public void size_Map_Null_ReturnsZero() {
		assertEquals(0, X.size((Map<?, ?>) null));
	}

	@Test
	public void size_Map_Empty_ReturnsZero() {
		assertEquals(0, X.size(Collections.emptyMap()));
	}

	@Test
	public void size_Map_NonEmpty_ReturnsSize() {
		Map<String, Integer> map = new HashMap<>();
		map.put("key1", 1);
		map.put("key2", 2);
		assertEquals(2, X.size(map));
	}

	@Test
	public void size_ObjectArray_Null_ReturnsZero() {
		assertEquals(0, X.size((Object[]) null));
	}

	@Test
	public void size_ObjectArray_Empty_ReturnsZero() {
		assertEquals(0, X.size(new Object[0]));
	}

	@Test
	public void size_ObjectArray_NonEmpty_ReturnsLength() {
		Object[] array = new Object[] { 1, 2, 3 };
		assertEquals(3, X.size(array));
	}

	@Test
	public void sizeOfArray_Null_ReturnsZero() {
		assertEquals(0, X.sizeOfArray(null));
	}

	@Test
	public void sizeOfArray_Empty_ReturnsZero() {
		assertEquals(0, X.sizeOfArray(new int[0]));
	}

	@Test
	public void sizeOfArray_NonEmpty_ReturnsLength() {
		int[] array = new int[] { 1, 2, 3 };
		assertEquals(3, X.sizeOfArray(array));
	}

	@Test(expected = IllegalArgumentException.class)
	public void sizeOfArray_NotAnArray_ThrowsException() {
		X.sizeOfArray("not an array");
	}

	@Test
	public void size() {
		assertEquals(0, X.size((CharSequence) null));
		assertEquals(0, X.size(""));
		assertEquals(1, X.size(" "));
		StringBuilder sb = new StringBuilder("CodePlayer").append(" ");
		assertEquals(11, X.size(sb));
		assertEquals(6, X.size("acsd中国"));
	}

	@Test
	public void expectNotNull() {
		assertNull(X.expectNotNull(null, null, null, null));

		assertEquals("first", X.expectNotNull("first", null, null, null));
		assertEquals("second", X.expectNotNull(null, "second", null, null));
		assertEquals("third", X.expectNotNull(null, null, "third", null));
		assertEquals("fourth", X.expectNotNull(null, null, null, "fourth"));

		assertNull(X.expectNotNull(null, null, null));
		assertEquals("first", X.expectNotNull("first", null, null));
		assertEquals("second", X.expectNotNull(null, "second", null));
		assertEquals("third", X.expectNotNull(null, null, "third"));

		assertNull(X.expectNotNull(null, null));
		assertEquals("first", X.expectNotNull("first", null));
		assertEquals("second", X.expectNotNull(null, "second"));
	}

	@Test
	public void emptyToNull() {
		Integer[] array = new Integer[0];
		assertNull(X.emptyToNull(array));
	}

	@Test
	public void emptyToNull_Array_NonEmpty_ReturnsArray() {
		Integer[] nonEmptyArray = { 1, 2, 3 };
		assertSame(nonEmptyArray, X.emptyToNull(nonEmptyArray));
	}

	@Test
	public void emptyToNull_Collection_Empty_ReturnsNull() {
		List<Integer> emptyList = Collections.emptyList();
		assertNull(X.emptyToNull(emptyList));
	}

	@Test
	public void emptyToNull_Collection_NonEmpty_ReturnsCollection() {
		List<Integer> nonEmptyList = Arrays.asList(1, 2, 3);
		assertSame(nonEmptyList, X.emptyToNull(nonEmptyList));
	}

	@Test
	public void emptyToNull_Map_Empty_ReturnsNull() {
		assertNull(X.emptyToNull(Collections.emptyMap()));
	}

	@Test
	public void emptyToNull_Map_NonEmpty_ReturnsMap() {
		Map<String, Integer> nonEmptyMap = CollectionX.asHashMap("key", 1);
		assertSame(nonEmptyMap, X.emptyToNull(nonEmptyMap));
	}

	@Test
	public void mapAny_EmptyArgs_ReturnsNull() {
		assertNull(X.mapAny(t -> t, r -> true));
	}

	@Test
	public void mapAny_NoElementMatches_ReturnsNull() {
		assertNull(X.mapAny(t -> t, r -> false, 1, 2, 3));
	}

	@Test
	public void mapAny_FirstElementMatches_ReturnsFirstMatch() {
		Integer expected = 1;
		Integer val = X.mapAny(t -> t, r -> r == 1, expected, 2, 3);
		assertSame(expected, val);
	}

	@Test
	public void mapAny_MiddleElementMatches_ReturnsFirstMatch() {
		Integer expected = 2;
		Integer val = X.mapAny(t -> t, r -> r == 2, 1, expected, 3);
		assertSame(expected, val);
	}

	@Test
	public void mapAny_LastElementMatches_ReturnsFirstMatch() {
		Integer expected = 3;
		Integer val = X.mapAny(t -> t, r -> r == 3, 1, 2, expected);
		assertSame(expected, val);
	}

	@Test
	public void mapAny_MapperAndMatcherComplexLogic_ReturnsCorrectResult() {
		Integer val = X.mapAny(String::length, len -> len > 3, "hi", "hello", "world");
		assertEquals((Integer) 5, val);
	}

	@Test
	public void with() {
		final User user = new User();

		X.with("CodePlayer", StringX::notEmpty, user::setName);
		assertEquals("CodePlayer", user.getName());

		final Set<Object> values = new HashSet<>();
		final Consumer<Object> add = values::add;
		X.with(user.getId(), add);
		X.with(user.getName(), add);
		X.with(user.getPassword(), add);

		assertEquals(1, values.size());
		assertEquals("CodePlayer", CollectionX.getAny(values));

		X.with(null, NumberX::isPositive, user::setId);
		assertNull(user.getId());

		user.setAge(18);
		user.setPassword("");

		final User copy = new User();
		X.with(user.getId(), copy::setId);
		X.with(user.getName(), StringX::notEmpty, copy::setName);
		X.with(user.getPassword(), StringX::notEmpty, copy::setPassword);
		X.with(user.getAge(), NumberX::isPositive, copy::setAge);
		assertNull(copy.getId());
		assertEquals("CodePlayer", copy.getName());
		assertNull(copy.getPassword());
		assertEquals((Integer) 18, copy.getAge());
	}

	@Test
	public void with_NonNullObjectAndFalseFilter_ConsumerNotExecuted() {
		StringBuilder sb = new StringBuilder();
		X.with("test", obj -> false, obj -> sb.append("executed"));
		assertEquals("", sb.toString());
	}

}