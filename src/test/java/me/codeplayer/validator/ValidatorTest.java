package me.codeplayer.validator;

import java.math.BigDecimal;
import java.util.function.Predicate;

import me.codeplayer.util.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

	static PropertyAccessor<Entity, Long> id = PropertyAccessor.of(Entity::getId, Entity::setId);

	@Test
	public void misc() {
		assertNotNull(id.getGetter());
		assertNotNull(id.getSetter());
	}

	@Test
	public void validateTrue_ReturnBoolean() {
		Book book = new Book(123L, "The Old Man and the Sea", "12345", "978-3-16-148410-0  ", 100, BigDecimal.valueOf(28.8));
		boolean result = Validator.of(book, id)
				.asserts(Validators.assertPositive)

				.begin(Book::getName)
				.asserts(Validators.assertLength(1, 50))

				.begin(Book::getCode)
				.asserts(Validators.assertNotBlank)
				.asserts(Validators.assertIsNumber)

				.begin(Book::getISBN, Book::setISBN)
				.apply(Validators.trim)
				.asserts(Validators.assertLength(17, 17), "请输入正确的ISBN号")
				.apply(Validators.lower)

				.begin(Book::getStock)
				.asserts(Validators.assertRange(0, 100000), () -> "请输入有效的库存数量")

				.begin(Book::getPrice)
				.asserts(Validators.assertRange(BigDecimal.ZERO, null))
				.isOK();
		assertTrue(result);
		assertEquals("978-3-16-148410-0", book.getISBN());
	}

	@Test
	public void validate_ThrowException() {
		assertThrows(IllegalArgumentException.class, () -> {
			Book book = new Book(123L, "The Old Man and the Sea", "12345", "978-3-16-148410-0  ", 88, BigDecimal.ZERO);
			Validator<Book, ?> validator = Validator.of(book, Book::getName, Book::setName)
					.asserts(Validators.assertNotEmpty)
					.apply(Validators.upper)

					.begin(Book::getId)
					.asserts(Validators.assertPositive)

					.begin(Book::getCode)
					.asserts(Validators.assertNotBlank)
					.asserts(Validators.assertIsNonNegative)

					.begin(Book::getStock)
					.asserts(Validators.assertNotNull)
					.asserts(Validators.assertNonNegative, "请输入有效的库存数量")
					.asserts(val -> val % 10 == 0, () -> "库存数量必须是 10 的整倍数");

			assertEquals("THE OLD MAN AND THE SEA", book.getName());
			assertEquals("库存数量必须是 10 的整倍数", validator.getResult());

			validator.tryThrow();
		});
	}

	@Test
	public void validate_ErrorMsg() {
		Book book = new Book(null, "The Old Man and the Sea", "12345", "978-3-16-148410-0  ", 100, BigDecimal.valueOf(28.8));
		final String errorMsg = "书籍名称必须是1~20个字符";
		Validator<Book, ?> validator = Validator.of(book)
				.silent()
				.begin(Book::getId)
				.asserts(Validators.assertIsNull)

				.begin(Book::getName)
				.asserts(Validators.assertLength(1, 20), errorMsg);

		assertFalse(validator.isOK());
		assertEquals(errorMsg, validator.getResult());
		assertEquals(errorMsg, validator.getResult(String.class));
	}

	@Test
	public void validateValue_ErrorMsg() {
		final String name = "The Old Man and the Sea";
		Book book = new Book(123L, name, "12345", "978-3-16-148410-0  ", 100, BigDecimal.valueOf(100));
		final String errorMsg = "书籍名称必须是1~20个字符";
		Validator<Book, String> validator = Validator.valueOf(name).
				asserts(Validators.assertLength(1, 50), errorMsg)

				.silent()
				.begin(book).begin(Book::getId)
				.asserts(Validators.assertPositive)

				.begin(Book::getName)
				.asserts(Validators.assertLength(1, 20), errorMsg);

		assertFalse(validator.isOK());
		assertEquals(errorMsg, validator.getResult());
		assertEquals(errorMsg, validator.getResult(String.class));
	}

	@Test
	void assertLength() {
		{
			Predicate<CharSequence> matcher = Validators.assertLength(1, 2);
			assertFalse(matcher.test(null));
			assertFalse(matcher.test(""));
			assertTrue(matcher.test("a"));
			assertTrue(matcher.test("ab"));
			assertFalse(matcher.test("abc"));
		}
		{
			Predicate<CharSequence> matcher = Validators.assertLength(1, -1);
			assertFalse(matcher.test(null));
			assertFalse(matcher.test(""));
			assertTrue(matcher.test("a"));
			assertTrue(matcher.test("ab"));
			assertTrue(matcher.test("HelloWorld"));
		}

	}

	@Test
	void assertRange() {
		{
			Predicate<Integer> matcher = Validators.assertRange(1, 2);
			assertFalse(matcher.test(null));
			assertFalse(matcher.test(0));
			assertTrue(matcher.test(1));
			assertTrue(matcher.test(2));
			assertFalse(matcher.test(3));
		}
		{
			Predicate<Integer> matcher = Validators.assertRange(1, -1);
			assertFalse(matcher.test(null));
			assertFalse(matcher.test(-1));
			assertFalse(matcher.test(0));
			assertFalse(matcher.test(1));
			assertFalse(matcher.test(2));
			assertFalse(matcher.test(3));
		}
		{
			Predicate<Long> matcher = Validators.assertRange(1L, 2L);
			assertFalse(matcher.test(null));
			assertFalse(matcher.test(0L));
			assertTrue(matcher.test(1L));
			assertTrue(matcher.test(2L));
			assertFalse(matcher.test(3L));
		}
		{
			Predicate<Long> matcher = Validators.assertRange(1L, -1L);
			assertFalse(matcher.test(null));
			assertFalse(matcher.test(-1L));
			assertFalse(matcher.test(0L));
			assertFalse(matcher.test(1L));
			assertFalse(matcher.test(2L));
			assertFalse(matcher.test(3L));
		}
		{
			Predicate<Double> matcher = Validators.assertRange(1D, 2D);
			assertFalse(matcher.test(null));
			assertFalse(matcher.test(0D));
			assertTrue(matcher.test(1D));
			assertTrue(matcher.test(2D));
			assertFalse(matcher.test(3D));
		}
		{
			Predicate<Double> matcher = Validators.assertRange(1D, -1D);
			assertFalse(matcher.test(null));
			assertFalse(matcher.test(-1D));
			assertFalse(matcher.test(0D));
			assertFalse(matcher.test(1D));
			assertFalse(matcher.test(2D));
			assertFalse(matcher.test(3D));
		}
		{
			Predicate<BigDecimal> matcher = Validators.assertRange(Arith.ONE, Arith.TEN);
			assertFalse(matcher.test(null));
			assertFalse(matcher.test(Arith.toBigDecimal(0)));
			assertTrue(matcher.test(Arith.toBigDecimal(1)));
			assertTrue(matcher.test(Arith.toBigDecimal(2)));
			assertTrue(matcher.test(Arith.toBigDecimal(3)));
			assertTrue(matcher.test(Arith.toBigDecimal(10)));
			assertFalse(matcher.test(Arith.toBigDecimal(11)));
		}
		{
			Predicate<BigDecimal> matcher = Validators.assertRange(null, null);
			assertFalse(matcher.test(null));
			assertTrue(matcher.test(Arith.toBigDecimal(0)));
			assertTrue(matcher.test(Arith.toBigDecimal(1)));
			assertTrue(matcher.test(Arith.toBigDecimal(2)));
			assertTrue(matcher.test(Arith.toBigDecimal(3)));
			assertTrue(matcher.test(Arith.toBigDecimal(10)));
			assertTrue(matcher.test(Arith.toBigDecimal(11)));
		}
		{
			Predicate<BigDecimal> matcher = Validators.assertRange(Arith.ONE, null);
			assertFalse(matcher.test(null));
			assertFalse(matcher.test(Arith.toBigDecimal(0)));
			assertTrue(matcher.test(Arith.toBigDecimal(1)));
			assertTrue(matcher.test(Arith.toBigDecimal(2)));
			assertTrue(matcher.test(Arith.toBigDecimal(3)));
			assertTrue(matcher.test(Arith.toBigDecimal(10)));
			assertTrue(matcher.test(Arith.toBigDecimal(11)));
		}
		{
			Predicate<BigDecimal> matcher = Validators.assertRange(null, Arith.TEN);
			assertFalse(matcher.test(null));
			assertTrue(matcher.test(Arith.toBigDecimal(0)));
			assertTrue(matcher.test(Arith.toBigDecimal(1)));
			assertTrue(matcher.test(Arith.toBigDecimal(2)));
			assertTrue(matcher.test(Arith.toBigDecimal(3)));
			assertTrue(matcher.test(Arith.toBigDecimal(10)));
			assertFalse(matcher.test(Arith.toBigDecimal(11)));
		}
	}

	@Test
	public void validatePropertyAccessor_ErrorMsg() {
		assertThrows(IllegalArgumentException.class, () -> {
			Book book = new Book(null, "The Old Man and the Sea", "12345", "978-3-16-148410-0  ", 100, BigDecimal.valueOf(28.8));
			Validator.of(book, id)
					.asserts(Validators.assertNotNull);
		});
	}

}