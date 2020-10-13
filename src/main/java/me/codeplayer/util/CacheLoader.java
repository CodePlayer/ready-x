package me.codeplayer.util;

import java.util.function.*;

/**
 * 缓存数据加载器
 *
 * @date 2017年1月10日
 * @since 0.4.3
 */
public interface CacheLoader<E> extends Supplier<E> {

	boolean flushRequired();

	E flush(boolean lazy);
}
