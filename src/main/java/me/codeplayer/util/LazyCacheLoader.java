package me.codeplayer.util;

/**
 * 缓存数据加载器
 * 
 * @date 2017年1月10日
 * @since 0.4.3
 * @param <E>
 */
public class LazyCacheLoader<E> implements CacheLoader<E> {

	protected final LazyLoader<E> loader;
	protected long internal;
	protected transient volatile E data;
	protected transient volatile long nextUpdateTime;

	public LazyCacheLoader(long internal, LazyLoader<E> loader) {
		this.internal = internal;
		this.loader = loader;
	}

	@Override
	public boolean flushRequired() {
		return flushRequired(System.currentTimeMillis());
	}

	protected boolean flushRequired(long baseTime) {
		return nextUpdateTime == 0 && internal > 0 && nextUpdateTime < baseTime;
	}

	@Override
	public E flush(boolean lazy) {
		E val;
		if (lazy) {
			nextUpdateTime = 0;
			val = null;
		} else {
			data = val = load(System.currentTimeMillis());
		}
		return val;
	}

	@Override
	public E load() {
		final long now = System.currentTimeMillis();
		if (flushRequired(now)) {
			return load(now);
		} else {
			return data;
		}
	}

	protected E load(final long now) {
		final E val = loader.load();
		data = val;
		nextUpdateTime = now + internal;
		return val;
	}
}
