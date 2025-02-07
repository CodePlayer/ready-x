package me.codeplayer.validator;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class ValidatorsTest {

	@Test
	public void validateTrue_ReturnBoolean() {
		Book book = new Book(123L, "The Old Man and the Sea", "12345", "978-3-16-148410-0  ", 100, BigDecimal.valueOf(28.8));
		boolean result = Validators.of(book)
				.begin(Book::getId)
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
		Assert.assertTrue(result);
		Assert.assertEquals("978-3-16-148410-0", book.getISBN());
	}

	@Test(expected = IllegalArgumentException.class)
	public void validate_ThrowException() {
		Book book = new Book(123L, "The Old Man and the Sea", "12345", "978-3-16-148410-0  ", 88, BigDecimal.ZERO);
		Pipeline<Book, ?> validator = Validators.of(book)
				.begin(Book::getId)
				.asserts(Validators.assertPositive)

				.begin(Book::getName, Book::setName)
				.asserts(Validators.assertNotEmpty)
				.apply(Validators.upper)

				.begin(Book::getStock)
				.asserts(Validators.assertNotNull)
				.asserts(Validators.assertNonNegative, "请输入有效的库存数量")
				.asserts(val -> val % 10 == 0, () -> "库存数量必须是 10 的整倍数");

		Assert.assertEquals("THE OLD MAN AND THE SEA", book.getName());
		Assert.assertEquals("库存数量必须是 10 的整倍数", validator.getResult());

		validator.tryThrow();
	}

	@Test
	public void validate_ErrorMsg() {
		Book book = new Book(null, "The Old Man and the Sea", "12345", "978-3-16-148410-0  ", 100, BigDecimal.valueOf(28.8));
		final String errorMsg = "书籍名称必须是1~20个字符";
		Pipeline<Book, ?> validator = Validators.of(book)
				.silent()
				.begin(Book::getId)
				.asserts(Validators.assertIsNull)

				.begin(Book::getName)
				.asserts(Validators.assertLength(1, 20), errorMsg);

		Assert.assertFalse(validator.isOK());
		Assert.assertEquals(errorMsg, validator.getResult());
		Assert.assertEquals(errorMsg, validator.getResult(String.class));
	}

	static class Book {

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

		public Long getId() {
			return id;
		}

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