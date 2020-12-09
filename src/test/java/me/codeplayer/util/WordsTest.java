package me.codeplayer.util;

import org.assertj.core.api.*;
import org.junit.*;

public class WordsTest implements WithAssertions {

	@Test
	public void test() {
		Words words = Words.of("java-print-hello-world".toLowerCase());
		// 实现了 stream 接口，可以自行定制
		words.stream().forEach(System.out::println);

		assertThat(words.to(Words.SNAKE_CASE)).isEqualTo("java_print_hello_world");
		assertThat(words.to(Words.CAMEL_CASE)).isEqualTo("javaPrintHelloWorld");
		assertThat(words.to(Words.PASCAL_CASE)).isEqualTo("JavaPrintHelloWorld");
		assertThat(words.to(Words.KEBAB_CASE)).isEqualTo("java-print-hello-world");

		assertThat(Words.of("javaPrintHelloWorld").join("+"))
				.isEqualTo("java+Print+Hello+World");

		assertThat(Words.of("java_print_hello_world").join("||"))
				.isEqualTo("java||print||hello||world");

		assertThat(Words.of("JavaPrintCPUWorld").join("->"))
				.isEqualTo("Java->Print->CPU->World");

		System.out.println(Words.of("JavaPrintCPUWorld").to(Words.CAMEL_CASE));
	}
}
