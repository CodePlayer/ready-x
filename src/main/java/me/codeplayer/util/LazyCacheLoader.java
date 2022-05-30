package me.codeplayer.util;

import java.util.function.*;

/**
 * 支持懒加载的缓存数据加载器，一般情况下，数据只会加载一次
 *
 * @author Ready
 * @date 2019年3月20日
 * @since 2.0
 */
public class LazyCacheLoader<E> implements CacheLoader<E> {

	public static final Object uninitialized = new Object();
	//
	protected transient volatile Object value = uninitialized;
	protected final Supplier<E> loader;

	public LazyCacheLoader(Supplier<E> loader) {
		Assert.notNull(loader);
		this.loader = loader;
	}

	public LazyCacheLoader(final boolean initialize, Supplier<E> loader) {
		this(loader);
		if (initialize) {
			this.value = loader.get();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get() {
		Object val = value;
		if (isUninitialized(val)) {
			value = val = loader.get();
		}
		return (E) val;
	}

	@Override
	public boolean flushRequired() {
		return isUninitialized(value);
	}

	protected boolean isUninitialized(final Object val) {
		return val == uninitialized;
	}

	@Override
	public E flush(boolean lazy) {
		if (lazy) {
			value = uninitialized;
			return null;
		} else {
			final E val = loader.get();
			value = val;
			return val;
		}
	}

}