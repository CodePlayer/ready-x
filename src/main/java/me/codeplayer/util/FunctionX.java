package me.codeplayer.util;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionX {

	@SuppressWarnings("rawtypes")
	static final Predicate nonNull = Objects::nonNull;
	@SuppressWarnings("rawtypes")
	static final Function identity = Function.identity();

	@SuppressWarnings("unchecked")
	public static <T> Predicate<T> nonNull() {
		return (Predicate<T>) nonNull;
	}

	@SuppressWarnings("unchecked")
	public static <T> Function<T, T> identity() {
		return identity;
	}

	@SuppressWarnings("unchecked")
	public static <K, T, L extends List<T>> Function<K, L> arrayListBuilder(int size) {
		return k -> (L) CollectionX.newArrayList(size);
	}

	public static <K, T, L extends List<T>> Function<K, L> arrayListBuilder() {
		return arrayListBuilder(10);
	}

}