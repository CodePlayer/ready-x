package me.codeplayer.util;

import java.math.BigDecimal;

public class Book implements Entity {

	Long id;
	String name;
	String code;
	String ISBN;
	Integer stock;
	BigDecimal price;

	public Book() {
	}

	public Book(Long id) {
		this.id = id;
	}

	public Book(Long id, String name, String code, String ISBN, Integer stock, BigDecimal price) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.ISBN = ISBN;
		this.stock = stock;
		this.price = price;
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