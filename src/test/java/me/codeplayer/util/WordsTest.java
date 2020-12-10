package me.codeplayer.util;

import org.assertj.core.api.*;
import org.junit.*;

import me.codeplayer.util.Words.*;

import static org.junit.Assert.*;

public class WordsTest implements WithAssertions {

	@Test
	public void test() {
		Words words = Words.of("Java-Print-hello-world");
		// 实现了 stream 接口，可以自行定制
		words.stream().forEach(System.out::println);

		assertEquals("java_print_hello_world", words.to(Words.SNAKE_CASE));
		assertEquals("javaPrintHelloWorld", words.to(Words.CAMEL_CASE));
		assertEquals("JavaPrintHelloWorld", words.to(Words.PASCAL_CASE));
		assertEquals("java-print-hello-world", words.to(Words.KEBAB_CASE));

		assertEquals("java_print_hello_world", words.to(Words.SNAKE_CASE, CharCase.UPPER));

		assertThat(Words.of("javaPrintHelloWorld").join("+"))
				.isEqualTo("java+Print+Hello+World");

		assertThat(Words.of("java_print_hello_world").join("||"))
				.isEqualTo("java||print||hello||world");

		words = Words.of("JavaPrintCPUWorld");
		assertEquals("Java->Print->CPU->World", words.join("->"));
		assertEquals("java_print_cpu_world", words.to(Words.SNAKE_CASE));

		words = Words.of("JAVA IS THE BEST LANGUAGE");

		assertEquals("java_is_the_best_language", words.to(Words.SNAKE_CASE));
		assertEquals("JAVAISTHEBESTLANGUAGE", words.to(Words.PASCAL_CASE));
		assertEquals("JavaIsTheBestLanguage", words.to(Words.PASCAL_CASE, CharCase.LOWER));
		assertEquals("JAVAISTHEBESTLANGUAGE", words.to(Words.CAMEL_CASE));
		assertEquals("javaIsTheBestLanguage", words.to(Words.CAMEL_CASE, CharCase.LOWER));
		assertEquals("java-is-the-best-language", words.to(Words.KEBAB_CASE));
	}
}
