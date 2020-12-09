package me.codeplayer.util;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import javax.annotation.*;

/**
 * 一组分词（单词）的抽象表示。
 * 其来源于将指定字符串按照特定的断词法进行分词处理。
 */
public class Words {

	protected final String source;
	/** [ { startIndex, endIndex ( exclude ) ]... } */
	protected final List<int[]> segments;

	Words(String source, List<int[]> segments) {
		this.source = source;
		this.segments = segments;
	}

	public int count() {
		return segments.size();
	}

	public Stream<String> stream() {
		return StreamSupport.stream(new Spliterator<String>() {
			int index = 0;

			@Override
			public boolean tryAdvance(Consumer<? super String> action) {
				if (index < segments.size()) {
					int[] seg = segments.get(index++);
					action.accept(source.substring(seg[0], seg[1]));
					return true;
				}
				return false;
			}

			@Override
			public Spliterator<String> trySplit() {
				return null;
			}

			@Override
			public long estimateSize() {
				return segments.size();
			}

			@Override
			public int characteristics() {
				return Spliterator.ORDERED | Spliterator.IMMUTABLE;
			}
		}, false);
	}

	public StringBuilder join(@Nullable StringBuilder sb, CharSequence delimiter) {
		final int size = segments.size();
		if (sb == null) {
			sb = new StringBuilder(source.length() + size);
		}
		for (int i = 0; i < size; i++) {
			int[] seg = segments.get(i);
			if (i > 0) {
				sb.append(delimiter);
			}
			sb.append(source, seg[0], seg[1]);
		}
		return sb;
	}

	public String join(CharSequence delimiter) {
		return join(null, delimiter).toString();
	}

	public String to(ToWordCase wordCase) {
		final StringBuilder sb = new StringBuilder(source.length() + segments.size());
		for (int i = 0; i < segments.size(); i++) {
			int[] seg = segments.get(i);
			wordCase.formatWord(sb, i, source, seg[0], seg[1]);
		}
		return sb.toString();
	}

	public static Words of(String source, @Nullable FromWordCase fromCase) {
		final int len = source.length();
		// { startIndex, endIndex ( exclude ) }
		final List<int[]> segments = new ArrayList<>();
		WordSplitter ref = new WordSplitter();
		for (int i = 0; i < len; i++) {
			char ch = source.charAt(i);
			WordSplitter splitter;
			if (fromCase == null) {
				if (ch == '_' /* snake */ || ch == '-' /* kebab */ || Character.isSpaceChar(ch) /* space */) {
					splitter = ref.splitAtSep(ref, i);
				} else {
					splitter = CAMEL_CASE.trySplitSpecial(ch, source, i, ref);
				}
			} else {
				splitter = fromCase.trySplit(ch, source, i, ref);
			}
			if (splitter != null) {
				if (splitter.beginIndex < splitter.endIndex) {
					segments.add(new int[] { splitter.beginIndex, splitter.endIndex });
				}
				splitter.beginIndex = splitter.nextBeginIndex;
			}
		}
		if (ref.beginIndex < len) {
			segments.add(new int[] { ref.beginIndex, len });
		}
		return new Words(source, segments);
	}

	public static Words of(String source) {
		return of(source, null);
	}

	public static final WordCase SNAKE_CASE = new WordCase('_', -1);
	public static final WordCase CAMEL_CASE = new WordCase(' ', 1) {
		@Override
		protected WordSplitter trySplitSpecial(char ch, String source, int charIndex, WordSplitter ref) {
			return super.trySplitSpecial(ch, source, charIndex, ref);
		}
	};
	public static final WordCase PASCAL_CASE = new WordCase(' ', 0);
	public static final WordCase KEBAB_CASE = new WordCase('-', -1);

	public static interface FromWordCase {

		WordSplitter trySplit(char ch, String source, int currentIndex, WordSplitter ref);

	}

	public static interface ToWordCase {

		void formatWord(StringBuilder sb, int wordIndex, String source, int beginIndex, int endIndex);
	}

	/**
	 * 用于存储拆分单词中途所需的关键标记信息（每次处理时，复用同一对象）
	 */
	public static class WordSplitter {

		private int beginIndex;
		private int endIndex;
		private int nextBeginIndex;
		private int prevUpperCount;

		public int getBeginIndex() {
			return beginIndex;
		}

		public int getEndIndex() {
			return endIndex;
		}

		public int getNextBeginIndex() {
			return nextBeginIndex;
		}

		public int getPrevUpperCount() {
			return prevUpperCount;
		}

		public void setBeginIndex(int beginIndex) {
			this.beginIndex = beginIndex;
		}

		public WordSplitter splitAt(int end, int nextBegin, int prevUpperCount) {
			this.endIndex = end;
			this.nextBeginIndex = nextBegin;
			this.prevUpperCount = prevUpperCount;
			return this;
		}

		public WordSplitter splitAtSep(WordSplitter prev, int charIndex) {
			return splitAt(charIndex, charIndex + 1, 0);
		}

	}

	/**
	 * 单词组的连词风格
	 */
	public static class WordCase implements FromWordCase, ToWordCase {

		public final char sep;
		public final boolean hasSep;
		public final int beginIndexOfWordsToUpperFirstChar;

		public WordCase(char sep, boolean hasSep, int beginIndexOfWordsToUpperFirstChar) {
			this.sep = sep;
			this.hasSep = hasSep;
			this.beginIndexOfWordsToUpperFirstChar = beginIndexOfWordsToUpperFirstChar;
		}

		public WordCase(char sep, int beginIndexOfWordsToUpperFirstChar) {
			this(sep, sep != ' ', beginIndexOfWordsToUpperFirstChar);
		}

		protected WordSplitter trySplitSpecial(char ch, String source, int charIndex, WordSplitter ref) {
			if (Character.isUpperCase(ch)) { // upper
				if (ref.prevUpperCount == 0) {
					return ref.splitAt(charIndex, charIndex, 1);
				}
				ref.prevUpperCount++;
			} else { // lower
				if (ref.prevUpperCount > 1) {
					return ref.splitAt(charIndex - 1, charIndex - 1, 0);
				}
				ref.prevUpperCount = 0;
			}
			return null;
		}

		public WordSplitter trySplit(final char ch, final String source, final int charIndex, WordSplitter ref) {
			if (sep == ch || Character.isSpaceChar(ch)) {
				return ref.splitAtSep(ref, charIndex);
			}
			return trySplitSpecial(ch, source, charIndex, ref);
		}

		public void formatWord(final StringBuilder sb, final int wordIndex, final String source, int beginIndex, int endIndex) {
			if (hasSep && wordIndex > 0) {
				sb.append(sep);
			}
			if (beginIndexOfWordsToUpperFirstChar != -1) {
				char first = source.charAt(beginIndex++);
				sb.append(wordIndex >= beginIndexOfWordsToUpperFirstChar
						? Character.toUpperCase(first)
						: Character.toLowerCase(first));
			}
			sb.append(source, beginIndex, endIndex);
		}
	}
}