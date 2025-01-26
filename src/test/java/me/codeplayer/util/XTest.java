package me.codeplayer.util;

import java.util.*;
import java.util.function.Supplier;

import me.codeplayer.util.JsonXTest.User;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class XTest implements WithAssertions {

	@Test
	public void isValid() {
		assertFalse(X.isValid((Boolean) null));
		assertTrue(X.isValid(true));
		assertFalse(X.isValid(false));

		int i = 0;
		assertFalse(X.isValid(i));
		assertTrue(X.isValid(-5));
		assertTrue(X.isValid(100.12));

		assertTrue(X.isValid("xxx"));
		assertFalse(X.isValid(""));

		assertTrue(X.isValid(new int[1]));
		assertFalse(X.isValid(new int[0]));

		Object[] array = new Integer[] { 1, 2, 3 };
		assertTrue((X.isValid(array)));

		List<Integer> list = Arrays.asList(1, 2, 3);
		assertTrue(X.isValid(list));
		assertFalse(X.isValid(Collections.emptyList()));

		HashMap<Object, Object> map = CollectionX.asHashMap("name", "James", "age", 18);
		assertTrue(X.isValid(map));
		assertFalse(X.isValid(Collections.emptyMap()));
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

		assertThat(X.mapElseGet(user, User::getPassword, () -> StringX.replaceChars("18000001234", '*', 3, -4)))
				.isEqualTo("180****1234");

		assertThat((Object) X.decode(1, 0, "PENDING", 1, "YES", -1, "NO")).isEqualTo("YES");

		assertThat(X.expectNotEmpty(user.getPassword(), "123")).isEqualTo("123");

		assertThat(X.expectNotEmpty(user.getName(), "Hello")).isEqualTo("James");

		assertThat((String) X.tryUnwrap((Supplier<String>) () -> StringX.replaceSubstring("Hello", "+++", 1, -1)))
				.isEqualTo("H+++o");

		user = Mockito.spy(user);

		X.with(user, User::getName);

		Mockito.verify(user).getName();
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
	public void size_CharSequence_Null_ReturnsZero() {
		assertEquals(0, X.size((CharSequence) null));
	}

	@Test
	public void size_CharSequence_Empty_ReturnsZero() {
		assertEquals(0, X.size(""));
	}

	@Test
	public void size_CharSequence_Blank_ReturnsLength() {
		assertEquals(1, X.size(" "));
	}

	@Test
	public void size_StringBuilder_NonEmpty_ReturnsLength() {
		StringBuilder sb = new StringBuilder("CodePlayer").append(" ");
		assertEquals(11, X.size(sb));
	}

	@Test
	public void size_CharSequence_NonEmpty_ReturnsLength() {
		assertEquals(6, X.size("acsd中国"));
	}

}