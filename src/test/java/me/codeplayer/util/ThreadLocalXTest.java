package me.codeplayer.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ThreadLocalXTest {

	@BeforeEach
	public void setUp() {
		// 在每个测试之前清除缓存
		ThreadLocalX.clear();
	}

	@Test
	public void getMap_CacheInitialized_ReturnsEmptyMap() {
		Map<Object, Object> map = ThreadLocalX.getMap();
		assertNotNull(map, "Cache should not be null");
		assertTrue(map.isEmpty(), "Cache should be empty");
	}

	@Test
	public void put_NewKey_ReturnsNull() {
		assertNull(ThreadLocalX.put("key", "value"), "Should return null for new key");
	}

	@Test
	public void put_ExistingKey_ReturnsOldValue() {
		ThreadLocalX.put("key", "oldValue");
		assertEquals("oldValue", ThreadLocalX.put("key", "newValue"), "Should return old value");
	}

	@Test
	public void get_ExistingKey_ReturnsValue() {
		ThreadLocalX.put("key", "value");
		assertEquals("value", ThreadLocalX.get("key"), "Should return value for existing key");
	}

	@Test
	public void get_NonExistingKey_ReturnsNull() {
		assertNull(ThreadLocalX.get("nonExistingKey"), "Should return null for non-existing key");
	}

	@Test
	public void clear_CacheEmptied() {
		ThreadLocalX.put("key", "value");
		ThreadLocalX.clear();
		assertTrue(ThreadLocalX.isEmpty(), "Cache should be empty after clear");
	}

	@Test
	public void size_CacheSizeCorrect() {
		ThreadLocalX.put("key1", "value1");
		ThreadLocalX.put("key2", "value2");
		assertEquals(2, ThreadLocalX.size(), "Cache size should be 2");
	}

	@Test
	public void isEmpty_CacheEmpty_ReturnsTrue() {
		assertTrue(ThreadLocalX.isEmpty(), "Cache should be empty");
	}

	@Test
	public void isEmpty_CacheNotEmpty_ReturnsFalse() {
		ThreadLocalX.put("key", "value");
		assertFalse(ThreadLocalX.isEmpty(), "Cache should not be empty");
	}

	@Test
	public void containsKey_KeyExists_ReturnsTrue() {
		ThreadLocalX.put("key", "value");
		assertTrue(ThreadLocalX.containsKey("key"), "Cache should contain key");
	}

	@Test
	public void containsKey_KeyDoesNotExist_ReturnsFalse() {
		assertFalse(ThreadLocalX.containsKey("nonExistingKey"), "Cache should not contain key");
	}

	@Test
	public void containsValue_ValueExists_ReturnsTrue() {
		ThreadLocalX.put("key", "value");
		assertTrue(ThreadLocalX.containsValue("value"), "Cache should contain value");
	}

	@Test
	public void containsValue_ValueDoesNotExist_ReturnsFalse() {
		assertFalse(ThreadLocalX.containsValue("nonExistingValue"), "Cache should not contain value");
	}

	@Test
	public void putAll_AddsMultipleEntries() {
		Map<Object, Object> map = CollectionX.asHashMap("key1", "value1", "key2", "value2");
		ThreadLocalX.putAll(map);
		assertEquals(2, ThreadLocalX.size(), "Cache size should be 2");
	}

	@Test
	public void reset_OverwritesCache() {
		ThreadLocalX.put("key1", "value1");
		Map<Object, Object> newMap = new HashMap<>();
		newMap.put("key2", "value2");
		ThreadLocalX.reset(newMap);
		assertEquals(1, ThreadLocalX.size(), "Cache size should be 1");
		assertEquals("value2", ThreadLocalX.get("key2"), "Should contain new value");
	}

	@Test
	public void remove_ExistingKey_ReturnsOldValue() {
		ThreadLocalX.put("key", "value");
		assertEquals("value", ThreadLocalX.remove("key"), "Should return old value");
	}

	@Test
	public void remove_NonExistingKey_ReturnsNull() {
		assertNull(ThreadLocalX.remove("nonExistingKey"), "Should return null for non-existing key");
	}

	@Test
	public void destroy_RemovesCache() {
		ThreadLocalX.put("key", "value");
		ThreadLocalX.destroy();
		assertEquals(0, ThreadLocalX.size());
	}

}