package me.codeplayer.util;

import java.util.function.Supplier;

import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("UnnecessaryBoxing")
public class CacheLoaderTest {

	@Test
	public void lazyCacheLoader() {
		final Integer val = new Integer(12356);
		LazyCacheLoader<Integer> loader = new LazyCacheLoader<>(true, () -> val);
		//noinspection UnnecessaryLocalVariable
		final Supplier<Integer> supplier = loader;
		assertSame(val, supplier.get());
		assertSame(val, loader.get());
		assertSame(loader.get(), new LazyCacheLoader<>(false, () -> val).get());
	}

	@Test
	public void timeBasedCacheLoader() {
		final Supplier<Integer> valueLoader = () -> new Integer(12356);
		TimeBasedCacheLoader<Integer> loader = new TimeBasedCacheLoader<>(20, valueLoader);
		assertTrue(loader.flushRequired());

		Integer prev = loader.get();
		assertFalse(loader.flushRequired());
		assertSame(prev, loader.get());

		assertNull(loader.flush(true));
		assertNotSame(prev, loader.get());

		prev = loader.get();
		assertNotNull(loader.flush(false));
		assertNotSame(prev, loader.get());
		prev = loader.get();

		try {
			Thread.sleep(25);
		} catch (InterruptedException ignored) {
		}
		assertTrue(loader.flushRequired());
		assertNotSame(prev, loader.get());
	}

}