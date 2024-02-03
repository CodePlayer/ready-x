package me.codeplayer.util;

import java.util.function.*;

/**
 * 缓存数据加载器
 *
 * @since 2017-1-10
 * @since 0.4.3
 */
public interface CacheLoader<E> extends Supplier<E> {

	boolean flushRequired();

	E flush(boolean lazy);

}