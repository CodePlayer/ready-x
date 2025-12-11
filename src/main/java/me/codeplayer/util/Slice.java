package me.codeplayer.util;

import java.util.function.Function;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * 对 字符串切片 进行统一转换处理的接口抽象
 */
public interface Slice<E> {

	E sliceAs(String str, int start, int end);

	/**
	 * 将指定的字符串片段转为 String 对象
	 *
	 * @return 如果字符串为 null 则返回 ""
	 */
	@NonNull
	static String parseString(String str, int start, int end) {
		return start == end ? "" : str.substring(start, end);
	}

	/**
	 * 将指定的字符串片段转为 String 对象
	 *
	 * @return 如果字符串为空则返回 null
	 */
	@Nullable
	static String asString(String str, int start, int end) {
		return start == end ? null : str.substring(start, end);
	}

	/**
	 * 将指定的字符串片段转为 int 类型
	 */
	static int parseInt(String str, int start, int end) {
		return JavaHelper.parseInt(str, start, end);
	}

	/**
	 * 将指定的字符串片段转为 Integer 对象
	 *
	 * @return 如果字符串为空则返回 null
	 */
	@Nullable
	static Integer asInteger(String str, int start, int end) {
		return start == end ? null : JavaHelper.parseInt(str, start, end);
	}

	/**
	 * 将指定的字符串片段转为 long 类型
	 */
	static long parseLong(String str, int start, int end) {
		return JavaHelper.parseLong(str, start, end);
	}

	/**
	 * 将指定的字符串片段转为 Long 对象
	 *
	 * @return 如果字符串为空则返回 null
	 */
	@Nullable
	static Long asLong(String str, int start, int end) {
		return start == end ? null : JavaHelper.parseLong(str, start, end);
	}

	static <R> Slice<R> mapIntTo(Function<? super Integer, R> mapper) {
		return (String str, int start, int end) -> mapper.apply(asInteger(str, start, end));
	}

	static <R> Slice<R> mapLongTo(Function<? super Long, R> mapper) {
		return (String str, int start, int end) -> mapper.apply(asLong(str, start, end));
	}

	static <R> Slice<R> mapStringTo(Function<? super String, R> mapper) {
		return (String str, int start, int end) -> mapper.apply(parseString(str, start, end));
	}

	default <R> Slice<R> andThen(Function<? super E, R> after) {
		return (String str, int start, int end) -> after.apply(sliceAs(str, start, end));
	}

}