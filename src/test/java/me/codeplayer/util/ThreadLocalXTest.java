package me.codeplayer.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ThreadLocalXTest {

	@Before
	public void setUp() {
		// 在每个测试之前清除缓存
		ThreadLocalX.clear();
	}

	@Test
	public void getMap_CacheInitialized_ReturnsEmptyMap() {
		Map<Object, Object> map = ThreadLocalX.getMap();
		assertNotNull("Cache should not be null", map);
		assertTrue("Cache should be empty", map.isEmpty());
	}

	@Test
	public void put_NewKey_ReturnsNull() {
		assertNull("Should return null for new key", ThreadLocalX.put("key", "value"));
	}

	@Test
	public void put_ExistingKey_ReturnsOldValue() {
		ThreadLocalX.put("key", "oldValue");
		assertEquals("Should return old value", "oldValue", ThreadLocalX.put("key", "newValue"));
	}

	@Test
	public void get_ExistingKey_ReturnsValue() {
		ThreadLocalX.put("key", "value");
		assertEquals("Should return value for existing key", "value", ThreadLocalX.get("key"));
	}

	@Test
	public void get_NonExistingKey_ReturnsNull() {
		assertNull("Should return null for non-existing key", ThreadLocalX.get("nonExistingKey"));
	}

	@Test
	public void clear_CacheEmptied() {
		ThreadLocalX.put("key", "value");
		ThreadLocalX.clear();
		assertTrue("Cache should be empty after clear", ThreadLocalX.isEmpty());
	}

	@Test
	public void size_CacheSizeCorrect() {
		ThreadLocalX.put("key1", "value1");
		ThreadLocalX.put("key2", "value2");
		assertEquals("Cache size should be 2", 2, ThreadLocalX.size());
	}

	@Test
	public void isEmpty_CacheEmpty_ReturnsTrue() {
		assertTrue("Cache should be empty", ThreadLocalX.isEmpty());
	}

	@Test
	public void isEmpty_CacheNotEmpty_ReturnsFalse() {
		ThreadLocalX.put("key", "value");
		assertFalse("Cache should not be empty", ThreadLocalX.isEmpty());
	}

	@Test
	public void containsKey_KeyExists_ReturnsTrue() {
		ThreadLocalX.put("key", "value");
		assertTrue("Cache should contain key", ThreadLocalX.containsKey("key"));
	}

	@Test
	public void containsKey_KeyDoesNotExist_ReturnsFalse() {
		assertFalse("Cache should not contain key", ThreadLocalX.containsKey("nonExistingKey"));
	}

	@Test
	public void containsValue_ValueExists_ReturnsTrue() {
		ThreadLocalX.put("key", "value");
		assertTrue("Cache should contain value", ThreadLocalX.containsValue("value"));
	}

	@Test
	public void containsValue_ValueDoesNotExist_ReturnsFalse() {
		assertFalse("Cache should not contain value", ThreadLocalX.containsValue("nonExistingValue"));
	}

	@Test
	public void putAll_AddsMultipleEntries() {
		Map<Object, Object> map = CollectionX.asHashMap("key1", "value1", "key2", "value2");
		ThreadLocalX.putAll(map);
		assertEquals("Cache size should be 2", 2, ThreadLocalX.size());
	}

	@Test
	public void reset_OverwritesCache() {
		ThreadLocalX.put("key1", "value1");
		Map<Object, Object> newMap = new HashMap<>();
		newMap.put("key2", "value2");
		ThreadLocalX.reset(newMap);
		assertEquals("Cache size should be 1", 1, ThreadLocalX.size());
		assertEquals("Should contain new value", "value2", ThreadLocalX.get("key2"));
	}

	@Test
	public void remove_ExistingKey_ReturnsOldValue() {
		ThreadLocalX.put("key", "value");
		assertEquals("Should return old value", "value", ThreadLocalX.remove("key"));
	}

	@Test
	public void remove_NonExistingKey_ReturnsNull() {
		assertNull("Should return null for non-existing key", ThreadLocalX.remove("nonExistingKey"));
	}

	@Test
	public void destroy_RemovesCache() {
		ThreadLocalX.put("key", "value");
		ThreadLocalX.destroy();
		assertEquals(0, ThreadLocalX.size());
	}

}