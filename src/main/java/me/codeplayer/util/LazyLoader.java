package me.codeplayer.util;

/**
 * 延迟加载器
 * 
 * @date 2017年1月10日
 * @since 0.4.3
 * @param <E>
 */
public interface LazyLoader<E> {

	E load();
}
