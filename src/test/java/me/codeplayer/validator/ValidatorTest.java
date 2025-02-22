package me.codeplayer.validator;

import java.math.BigDecimal;

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
	public void validatePropertyAccessor_ErrorMsg() {
		assertThrows(IllegalArgumentException.class, () -> {
			Book book = new Book(null, "The Old Man and the Sea", "12345", "978-3-16-148410-0  ", 100, BigDecimal.valueOf(28.8));
			Validator.of(book, id)
					.asserts(Validators.assertNotNull);
		});
	}

	interface Entity {

		Long getId();

		void setId(Long id);

	}

	static class Book implements Entity {

		Long id;
		String name;
		String code;
		String ISBN;
		Integer stock;
		BigDecimal price;

		public Book(Long id, String name, String code, String ISBN, Integer stock, BigDecimal price) {
			this.id = id;
			this.code = code;
			this.name = name;
			this.ISBN = ISBN;
			this.stock = stock;
			this.price = price;
		}

		public Book() {
		}

		@Override
		public Long getId() {
			return id;
		}

		@Override
		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getISBN() {
			return ISBN;
		}

		public void setISBN(String ISBN) {
			this.ISBN = ISBN;
		}

		public Integer getStock() {
			return stock;
		}

		public void setStock(Integer stock) {
			this.stock = stock;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}

	}

}