package me.ready.util;

import org.json.JSONObject;

/**
 * JSON字符串转换工具类
 * @package me.ready.util
 * @author Ready
 * @date 2014-10-13
 *
 */
public class JSONUtil {

	// 禁止实例创建
	private JSONUtil() {

	}

	/**
	 * 将Java对象编码为JSON字符串
	 * @param obj
	 * @return
	 */
	public static final String encode(Object obj) {
		return JSONObject.valueToString(obj);
	}

}
