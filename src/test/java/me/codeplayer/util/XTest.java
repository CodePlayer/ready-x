package me.codeplayer.util;

import static org.junit.Assert.*;

import java.util.*;
import java.util.function.*;

import org.assertj.core.api.*;
import org.junit.*;
import org.mockito.*;

import me.codeplayer.util.JsonXTest.*;

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
}