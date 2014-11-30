package me.ready.util;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * JSON字符串序列化转换工具类
 * 
 * @package me.ready.util
 * @author Ready
 * @date 2014-10-13
 * 
 */
@SuppressWarnings("unchecked")
public class JSONUtil {

	// 禁止实例创建
	private JSONUtil() {
	}

	/**
	 * 将Java对象编码为JSON字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static final String encode(Object obj) {
		return JSON.toJSONString(obj);
	}

	/**
	 * 将Java对象编码为JSON字符串，该方法不会对对象的属性进行循环引用检测，因此对象的属性只能是基本数据类型才能使用此方法。<br>
	 * 它可以提高编码JSON字符串的性能
	 * 
	 * @param obj
	 * @return
	 */
	public static final String encodeSimple(Object obj) {
		return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
	}

	/**
	 * 将JSON字符串转为指定类型的Java对象
	 * 
	 * @param text 指定的JSON字符串
	 * @param clazz 指定的类型
	 * @return
	 */
	public static final <T> T parseObject(String text, Class<T> clazz) {
		return JSON.parseObject(text, clazz);
	}

	/**
	 * 将JSON字符串转为JSONObject形式的对象(类似于增强型的HashMap)
	 * 
	 * @param text 指定的JSON字符串
	 * @return
	 */
	public static final JSONObject parseObject(String text) {
		return JSON.parseObject(text);
	}

	/**
	 * 将JSON字符串转为JSONObject形式的对象(类似于增强型的HashMap)
	 * 
	 * @param text 指定的JSON字符串
	 * @return
	 */
	public static final JSONArray parseArray(String text) {
		return JSON.parseArray(text);
	}

	/**
	 * 将JSON字符串转为List形式的指定类型的对象集合
	 * 
	 * @param text 指定的JSON字符串
	 * @param clazz 指定的类型
	 * @return
	 */
	public static final <T> List<T> parseArray(String text, Class<T> clazz) {
		return JSON.parseArray(text, clazz);
	}

	/**
	 * 将指定的Java对象序列化为JSON字符串
	 * 
	 * @since 0.1
	 * @param obj 指定的对象
	 * @return
	 */
	public static final String serialize(Object obj) {
		return JSON.toJSONString(obj, SerializerFeature.WriteClassName);
	}

	/**
	 * 
	 * 将指定的JSON字符串反序列化为指定的Java对象
	 * 
	 * @since 0.1
	 * @param text 指定的JSON字符串
	 * @return
	 */
	public static final <T> T deserialize(String text) {
		return (T) JSON.parse(text);
	}
}
