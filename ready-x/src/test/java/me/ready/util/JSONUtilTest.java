package me.ready.util;

import java.util.Map;

import org.json.JSONObject;
import org.junit.Test;

public class JSONUtilTest {

	@Test
	public void testObject() {
		// User user = new User(1, "张三", "123456", true);
		// System.out.println(new JSONObject(user).toString());
		// System.err.println("====" + JSONUtil.encode(user));
	}

	// @Test
	@SuppressWarnings("unchecked")
	public void createHashMap() {
		Map<Object, Object> map = CollectionUtil.createHashMap("name", "张三", "age", 18);
		System.out.println(new JSONObject(map).toString());
	}

	// @Test
	public void testArray() {
		// Object[] array = new Object[] { "张三", true, 15, new User(2, "李四",
		// "45646", false) };
		// System.out.println(new JSONArray(array).toString());
	}

	@Test
	public void testVar() {
		// String str = "张三";
		// System.out.println(new JSONObject(str).toString());
	}
}
