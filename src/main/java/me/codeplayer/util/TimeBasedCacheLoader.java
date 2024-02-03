package me.codeplayer.util;

import java.util.function.Supplier;

/**
 * 基于时间间隔进行数据更新的缓存数据加载器
 *
 * @since 2017-1-10
 * @since 2.0
 */
public class TimeBasedCacheLoader<E> extends LazyCacheLoader<E> {

	protected long internal;
	protected transient volatile long nextUpdateTime;

	public TimeBasedCacheLoader(final long internal, final Supplier<E> loader) {
		super(loader);
		this.internal = internal;
	}

	@Override
	public boolean flushRequired() {
		return flushRequired(value, System.currentTimeMillis());
	}

	protected boolean flushRequired(Object val, long baseTime) {
		return isUninitialized(val) || nextUpdateTime == 0 || internal > 0 && nextUpdateTime < baseTime;
	}

	public E flush(boolean lazy) {
		final E val;
		if (lazy) {
			nextUpdateTime = 0;
			val = null;
		} else {
			value = val = load(System.currentTimeMillis());
		}
		return val;
	}

	@SuppressWarnings("unchecked")
	public E get() {
		final long now = System.currentTimeMillis();
		Object val = value;
		if (flushRequired(val, now)) {
			return load(now);
		} else {
			return (E) val;
		}
	}

	protected E load(final long now) {
		final E val = loader.get();
		value = val;
		nextUpdateTime = now + internal;
		return val;
	}

}