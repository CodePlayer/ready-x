package me.codeplayer.util;

/**
 * 缓存数据加载器
 * 
 * @date 2017年1月10日
 * @since 0.4.3
 * @param <E>
 */
public interface CacheLoader<E> extends LazyLoader<E> {

	boolean flushRequired();

	E flush(boolean lazy);
}
