package me.codeplayer.util;

import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EnumXTest {

	private enum TestEnum {
		VALUE1,
		VALUE2,
		VALUE3
	}

	@Test
	public void of_ValidName_ReturnsEnumValue() {
		TestEnum result = EnumX.of(TestEnum.class, "VALUE1", TestEnum.VALUE3);
		assertEquals(TestEnum.VALUE1, result);
	}

	@Test
	public void of_InvalidName_ReturnsDefaultValue() {
		TestEnum result = EnumX.of(TestEnum.class, "INVALID", TestEnum.VALUE3);
		assertEquals(TestEnum.VALUE3, result);
	}

	@Test
	public void of_NullName_ReturnsDefaultValue() {
		TestEnum result = EnumX.of(TestEnum.class, null, TestEnum.VALUE3);
		assertEquals(TestEnum.VALUE3, result);
	}

	@Test
	public void of_ValidName_ReturnsEnumValue_NullDefault() {
		TestEnum result = EnumX.of(TestEnum.class, "VALUE2");
		assertEquals(TestEnum.VALUE2, result);
	}

	@Test
	public void of_InvalidName_ReturnsNull() {
		TestEnum result = EnumX.of(TestEnum.class, "INVALID");
		assertNull(result);
	}

	@Test
	public void getMatched_AllValuesMatch_ReturnsAll() {
		Predicate<TestEnum> matcher = e -> true;
		TestEnum[] result = EnumX.getMatched(TestEnum.class, null, matcher);
		assertEquals(3, result.length);
		assertEquals(TestEnum.VALUE1, result[0]);
		assertEquals(TestEnum.VALUE2, result[1]);
		assertEquals(TestEnum.VALUE3, result[2]);
	}

	@Test
	public void getMatched_SomeValuesMatch_ReturnsMatched() {
		Predicate<TestEnum> matcher = e -> e.name().contains("1");
		TestEnum[] result = EnumX.getMatched(TestEnum.class, null, matcher);
		assertEquals(1, result.length);
		assertEquals(TestEnum.VALUE1, result[0]);
	}

	@Test
	public void getMatched_NoValuesMatch_ReturnsEmpty() {
		Predicate<TestEnum> matcher = e -> false;
		TestEnum[] result = EnumX.getMatched(TestEnum.class, null, matcher);
		assertEquals(0, result.length);
	}

	@Test
	public void getMatched_SpecificValuesProvided_ReturnsMatched() {
		Predicate<TestEnum> matcher = e -> e.name().contains("2");
		TestEnum[] values = { TestEnum.VALUE1, TestEnum.VALUE2, TestEnum.VALUE3 };
		TestEnum[] result = EnumX.getMatched(TestEnum.class, values, matcher);
		assertEquals(1, result.length);
		assertEquals(TestEnum.VALUE2, result[0]);
	}

}