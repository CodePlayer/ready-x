package me.codeplayer.util;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import javax.annotation.*;

import org.apache.commons.lang3.*;

import me.codeplayer.util.CharConverter.*;

/**
 * 一组分词（单词）的抽象表示。 其来源于将指定字符串按照特定的断词法进行分词处理。
 */
public class Words {

	/** [ { startIndex, endIndex ( exclude ) ]... } */
	protected final List<Segment> segments;

	Words(List<Segment> segments) {
		this.segments = segments;
	}

	public static Words from(String source, @Nullable FromWordCase fromCase) {
		final int len = source.length();
		// { startIndex, endIndex ( exclude ) }
		final List<Segment> segments = new ArrayList<>();
		final WordSplitter splitter = new WordSplitter();
		int wordIndex = 0;
		for (int i = 0; i < len; i++) {
			char ch = source.charAt(i);
			Segment seg;
			if (fromCase == null) {
				if (ch == '_' /* snake */ || ch == '-' /* kebab */ || Character.isSpaceChar(ch) /* space */) {
					seg = splitter.splitAtSep(i, 1, splitter.prevUpperCount > 0);
				} else {
					seg = splitter.trySplitSpecial(ch, source, i);
				}
			} else {
				seg = fromCase.trySplit(ch, source, i, splitter);
			}
			if (seg != null) {
				if (seg != Segment.EMPTY) {
					segments.add(seg.attach(source, wordIndex++));
				}
				splitter.begin = splitter.nextBegin;
			}
		}
		if (splitter.begin < len) {
			segments.add(new Segment(splitter.begin, len, splitter.prevUpperCount > 0).attach(source, wordIndex));
		}
		return new Words(segments);
	}

	public static Words from(Iterable<String> words) {
		List<Segment> segments = words instanceof Collection ? new ArrayList<>(((Collection<String>) words).size()) : new ArrayList<>();
		int i = 0;
		for (String word : words) {
			if (StringUtil.isEmpty(word)) {
				continue;
			}
			segments.add(new Segment(0, word.length(), StringUtils.isAllUpperCase(word)).attach(word, i++));
		}
		return new Words(segments);
	}

	public static Words from(String... words) {
		return from(Arrays.asList(words));
	}

	public static Words from(String source) {
		return from(source, null);
	}

	public int count() {
		return segments.size();
	}

	public Stream<String> stream() {
		return segments.stream().map(Segment::toString);
	}

	public StringBuilder forEachAppend(@Nullable StringBuilder sb, BiConsumer<StringBuilder, Segment> appender) {
		final int size = segments.size();
		if (size == 0) {
			return new StringBuilder(0);
		}
		if (sb == null) {
			sb = new StringBuilder(segments.get(0).source.length() + size);
		}
		for (Segment seg : segments) {
			appender.accept(sb, seg);
		}
		return sb;
	}

	public StringBuilder join(@Nullable StringBuilder builder, final CharSequence delimiter) {
		return forEachAppend(builder, (sb, seg) -> {
			if (seg.wordIndex > 0) {
				sb.append(delimiter);
			}
			sb.append(seg.source, seg.begin, seg.end);
		});
	}

	public String join(CharSequence delimiter) {
		return join(null, delimiter).toString();
	}

	public StringBuilder convertCase(@Nullable WordSeparator ws, WordCaseDescriptor descriptor, @Nullable CharCase preprocessor) {
		return forEachAppend(null, (sb, seg) -> {
			if (ws != null) {
				ws.appendSeparator(sb, seg);
			}
			descriptor.formatWord(sb, seg, preprocessor);
		});
	}

	public StringBuilder convertCaseWithSep(char sep, WordCaseDescriptor descriptor, @Nullable CharCase preprocessor) {
		return forEachAppend(null, (sb, seg) -> {
			if (seg.wordIndex > 0) {
				sb.append(sep);
			}
			descriptor.formatWord(sb, seg, preprocessor);
		});
	}

	public StringBuilder convertCaseWithSep(char sep, WordCaseDescriptor descriptor) {
		return convertCaseWithSep(sep, descriptor, null);
	}

	public StringBuilder convertCaseWithSep(String sep, WordCaseDescriptor descriptor, @Nullable CharCase preprocessor) {
		return forEachAppend(null, (sb, seg) -> {
			if (seg.wordIndex > 0) {
				sb.append(sep);
			}
			descriptor.formatWord(sb, seg, preprocessor);
		});
	}

	public StringBuilder convertCaseWithSep(String sep, WordCaseDescriptor descriptor) {
		return convertCaseWithSep(sep, descriptor, null);
	}

	public String to(@Nullable WordSeparator ws, WordCaseDescriptor descriptor, @Nullable CharCase preprocessor) {
		return convertCase(ws, descriptor, preprocessor).toString();
	}

	public String to(ToWordCase wordCase, @Nullable CharCase preprocessor) {
		return wordCase.toString(convertCase(wordCase, wordCase.getDescriptor(), preprocessor));
	}

	public String to(ToWordCase wordCase) {
		return to(wordCase, null);
	}

	public static class Segment {

		public transient String source;
		public transient int wordIndex;
		//
		public final int begin;
		public final int end;
		/** 全部大写的缩略词 */
		public final boolean abbr;

		public Segment(int begin, int end, boolean abbr) {
			this.begin = begin;
			this.end = end;
			this.abbr = abbr;
		}

		static final Segment EMPTY = new Segment(-1, -1, false);

		public Segment attach(String source, int wordIndex) {
			this.source = source;
			this.wordIndex = wordIndex;
			return this;
		}

		public static Segment of(int begin, int end, boolean abbr) {
			return begin < end ? new Segment(begin, end, abbr) : EMPTY;
		}

		@Override
		public String toString() {
			return source.substring(begin, end);
		}
	}

	public static interface FromWordCase {
		Segment trySplit(char ch, String source, int currentIndex, WordSplitter ref);
	}

	public static interface WordSeparator {
		void appendSeparator(StringBuilder sb, Segment seg);
	}

	public static interface WordCaseDescriptor {
		/**
		 * @param continueFlagRef 只包含一个 boolean 值的数组，该接口方法的实现可以通过该 boolean 值来传达下个字符是否还需要调用该方法来获取 {@code CharCase}。
		 * @return 如果返回 null，则后续字符不再需要大小写转换处理，直接跳出处理循环。 其他情况下：如果 {@code continueFlagRef[0]} 为 true，则将返回对象仅应用于当前字符的转换处理；如果为 false，则后续字符都将应用该 {@code CharCase} 进行转换处理
		 */
		CharCase getCharCase(Segment seg, int charIndex, boolean[] continueFlagRef);

		default void formatWord(final StringBuilder sb, final Segment seg, final CharCase preprocessor) {
			CharCase charCase = null;
			final int end = seg.end;
			final boolean[] continueFlagRef = new boolean[] { true };
			int begin = seg.begin;
			final String source = seg.source;
			for (int i = 0; begin < end;) {
				charCase = getCharCase(seg, i++, continueFlagRef);
				if (charCase == null) {
					charCase = preprocessor;
					break;
				}
				if (charCase == CharCase.NONE && preprocessor != null) {
					charCase = preprocessor;
				}
				if (continueFlagRef[0]) {
					sb.append(charCase.apply(source.charAt(begin++)));
				} else {
					break;
				}
			}
			if (begin < end) {
				if (charCase == null || charCase == CharCase.NONE) {
					sb.append(source, begin, end);
				} else {
					for (int i = begin; i < end; i++) {
						sb.append(charCase.apply(source.charAt(i)));
					}
				}
			}
		}

	}

	public static interface ToWordCase extends WordSeparator {

		WordCaseDescriptor getDescriptor();

		default String toString(StringBuilder sb) {
			return sb.toString();
		}
	}

	/**
	 * 用于存储拆分单词中途所需的关键标记信息（循环拆分处理时，务必复用同一对象）
	 */
	public static class WordSplitter {

		private int begin;
		private int end;
		private int nextBegin;
		private int prevUpperCount;

		public int getBegin() {
			return begin;
		}

		public int getEnd() {
			return end;
		}

		public int getNextBegin() {
			return nextBegin;
		}

		public int getPrevUpperCount() {
			return prevUpperCount;
		}

		public void setBegin(int begin) {
			this.begin = begin;
		}

		protected Segment splitAt(int endIndex, int nextBegin, int prevUpperCount, boolean abbr) {
			this.end = endIndex;
			this.nextBegin = nextBegin;
			this.prevUpperCount = prevUpperCount;
			return toSegment(abbr);
		}

		public Segment splitAt(int endIndex, int prevUpperCount, boolean abbr) {
			return splitAt(endIndex, endIndex, prevUpperCount, abbr);
		}

		public Segment splitAtSep(int charIndex, int sepCharLength, boolean abbr) {
			int nextBegin = charIndex + 1;
			return splitAt(nextBegin - sepCharLength, nextBegin, 0, abbr);
		}

		public Segment toSegment(boolean abbr) {
			return Segment.of(begin, end, abbr);
		}

		public Segment trySplitSpecial(char ch, String source, int charIndex) {
			if (Character.isUpperCase(ch)) { // upper
				if (prevUpperCount == 0) {
					return splitAt(charIndex, 1, false);
				}
				prevUpperCount++;
			} else { // lower
				if (prevUpperCount > 1) {
					return splitAt(charIndex - 1, 0, true);
				}
				prevUpperCount = 0;
			}
			return null;
		}

		public Segment trySplit(final char sep, final char ch, final String source, final int charIndex) {
			if (sep == ch || Character.isSpaceChar(ch)) {
				return splitAtSep(charIndex, 1, false);
			}
			return trySplitSpecial(ch, source, charIndex);
		}

	}

	/**
	 * 单词组的连词风格
	 */
	public static class WordCase implements FromWordCase, ToWordCase {

		public final char sep;
		public final boolean hasSep;
		public final WordCaseDescriptor descriptor;

		public WordCase(char sep, boolean hasSep, WordCaseDescriptor descriptor) {
			this.sep = sep;
			this.hasSep = hasSep;
			this.descriptor = descriptor;
		}

		public WordCase(char sep, WordCaseDescriptor descriptor) {
			this(sep, true, descriptor);
		}

		public WordCase(WordCaseDescriptor descriptor) {
			this(' ', false, descriptor);
		}

		@Override
		public Segment trySplit(final char ch, final String source, final int charIndex, WordSplitter ref) {
			return ref.trySplit(sep, ch, source, charIndex);
		}

		@Override
		public WordCaseDescriptor getDescriptor() {
			return descriptor;
		}

		@Override
		public void appendSeparator(final StringBuilder sb, final Segment seg) {
			if (hasSep && seg.wordIndex > 0) {
				sb.append(sep);
			}
		}

	}

	public static final WordCase SNAKE_CASE = new WordCase('_', (seg, i, ref) -> {
		ref[0] = false;
		return CharCase.LOWER;
	});

	public static final WordCase CAMEL_CASE = new WordCase((seg, i, ref) -> {
		if (seg.wordIndex > 0 && i == 0) {
			return CharCase.UPPER;
		}
		if (seg.abbr) {
			return CharCase.NONE;
		}
		return CharCase.LOWER;
	});

	public static final WordCase PASCAL_CASE = new WordCase((seg, i, ref) -> {
		if (i == 0) {
			return CharCase.UPPER;
		}
		if (seg.abbr) {
			return CharCase.NONE;
		}
		return CharCase.LOWER;
	});

	public static final WordCase KEBAB_CASE = new WordCase('-', (seg, i, ref) -> {
		ref[0] = false;
		return CharCase.LOWER;
	});

}