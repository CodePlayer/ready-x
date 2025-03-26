package me.codeplayer.util;

import java.util.stream.Collectors;

import me.codeplayer.util.CharConverter.CharCase;
import me.codeplayer.util.Words.WordCaseDescriptor;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordsTest implements WithAssertions {

	@Test
	public void test() {
		Words words = Words.from("Java-Print-hello-world");
		// 实现了 stream 接口，可以自行定制
		// words.stream().forEach(System.out::println);

		assertEquals("java_print_hello_world", words.to(Words.SNAKE_CASE));
		assertEquals("javaPrintHelloWorld", words.to(Words.CAMEL_CASE));
		assertEquals("JavaPrintHelloWorld", words.to(Words.PASCAL_CASE));
		assertEquals("java-print-hello-world", words.to(Words.KEBAB_CASE));

		assertEquals("java_print_hello_world", words.to(Words.SNAKE_CASE, CharCase.UPPER));

		Words words1 = Words.from("javaPrintHelloWorld");
		assertEquals(4, words1.count());
		assertThat(words1.join("+"))
				.isEqualTo("java+Print+Hello+World");

		assertThat(words1.stream().collect(Collectors.joining("+"))).isEqualTo("java+Print+Hello+World");

		assertThat(Words.from("java_print_hello_world").join("||"))
				.isEqualTo("java||print||hello||world");

		words = Words.from("JavaPrintCPUWorld");
		assertEquals("Java->Print->CPU->World", words.join("->"));
		assertEquals("java_print_cpu_world", words.to(Words.SNAKE_CASE));

		words = Words.from("JAVA IS THE BEST LANGUAGE");

		assertEquals("java_is_the_best_language", words.to(Words.SNAKE_CASE));
		assertEquals("JAVAISTHEBESTLANGUAGE", words.to(Words.PASCAL_CASE));
		assertEquals("JavaIsTheBestLanguage", words.to(Words.PASCAL_CASE, CharCase.LOWER));
		assertEquals("JAVAISTHEBESTLANGUAGE", words.to(Words.CAMEL_CASE));
		assertEquals("javaIsTheBestLanguage", words.to(Words.CAMEL_CASE, CharCase.LOWER));
		assertEquals("java-is-the-best-language", words.to(Words.KEBAB_CASE));

		WordCaseDescriptor wcd = (seg, i, continueFlagRef) -> i % 2 == 0 ? CharCase.UPPER : CharCase.LOWER;

		assertEquals("JaVa_Is_ThE_BeSt_LaNgUaGe", words.convertCaseWithSep('_', wcd).toString());

		assertEquals("PhP~~~Is~~~ThE~~~BeSt~~~LaNgUaGe", Words.from("PHP IS THE BEST LANGUAGE").convertCaseWithSep("~~~", wcd).toString());

		assertEquals("hello_world_not_word", Words.from("hello", "World", null, "NOT", "worD").to(Words.SNAKE_CASE));
	}

}