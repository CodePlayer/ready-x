package me.ready.util;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * JSON字符串转换工具类
 * 
 * @package me.ready.util
 * @author Ready
 * @date 2014-10-13
 * 
 */
public class JSONUtil {

	// 禁止实例创建
	private JSONUtil() {}

	/**
	 * 将Java对象编码为JSON字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static final String encode(Object obj) {
		if (obj.getClass().isArray()) {
			return new JSONArray(obj).toString();
		}
		return new JSONObject(obj).toString();
	}

	/**
	 * 将JSON字符串转为JSONArray形式的数组(类似于增强型的ArrayList)
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static final JSONArray decodeForArray(String jsonStr) {
		return new JSONArray(jsonStr);
	}

	/**
	 * 将JSON字符串转为JSONObject形式的对象(类似于增强型的HashMap)
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static final JSONObject decodeForMap(String jsonStr) {
		return new JSONObject(jsonStr);
	}
}
