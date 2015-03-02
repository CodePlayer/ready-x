package me.ready.util;

import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

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
	 * @param obj 指定的任意对象
	 * @return
	 */
	public static final String encode(Object obj) {
		return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
	}

	/**
	 * 将Java对象编码为JSON字符串
	 * 
	 * @param obj 指定的任意对象
	 * @param excludeProperties 需要排除的属性数组
	 * @return
	 */
	public static final String encodeWithExclude(Object obj, String... excludeProperties) {
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
		Collections.addAll(filter.getExcludes(), excludeProperties);
		return JSON.toJSONString(obj, filter, SerializerFeature.DisableCircularReferenceDetect);
	}

	/**
	 * 将Java对象编码为JSON字符串
	 * 
	 * @param obj 指定的任意对象
	 * @param excludeProperties 需要排除的属性数组
	 * @return
	 */
	public static final String encodeWithInclude(Object obj, String... includeProperties) {
		return JSON.toJSONString(obj, new SimplePropertyPreFilter(includeProperties), SerializerFeature.DisableCircularReferenceDetect);
	}

	/**
	 * 将Java对象编码为JSON字符串，并以指定的格式化模式处理日期类型
	 * 
	 * @param obj 指定的任意对象
	 * @param pattern 指定的格式化字符串，例如{@code "yyyy-MM-dd"}
	 * @return
	 */
	public static final String encodeWithDateFormat(Object obj, String pattern) {
		return JSON.toJSONStringWithDateFormat(obj, pattern, SerializerFeature.DisableCircularReferenceDetect);
	}

	/**
	 * 将Java对象编码为JSON字符串，并使用指定的属性过滤器进行过滤(只有符合条件的属性才可以序列化为JSON字符串)
	 * 
	 * @param obj 指定的任意对象
	 * @param dateFormat 日期格式化字符串(如果没有可以为null)
	 * @return
	 */
	public static final String encodeWithJSONFilter(Object object, final String dateFormat) {
		SerializeWriter out = new SerializeWriter();
		try {
			JSONSerializer serializer = new JSONSerializer(out);
			serializer.config(SerializerFeature.DisableCircularReferenceDetect, true);
			serializer.getPropertyPreFilters().add(JSONPropertyPreFilter.getInstance());
			if (dateFormat != null) {
				serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
				serializer.setDateFormat(dateFormat);
			}
			serializer.write(object);
			return out.toString();
		} finally {
			out.close();
		}
	}

	/**
	 * 将Java对象编码为JSON字符串，它与encode()方法不同的是，它会进行循环引用检测。如果其中的多个元素或属性指向同一个对象引用，则后者将只输出对前者的引用表示
	 * 
	 * @param obj
	 * @return
	 */
	public static final String encodeWithReferenceDetect(Object obj) {
		return JSON.toJSONString(obj);
	}

	/**
	 * 将JSON字符串转为对应的JSONObject或JSONArray对象
	 * 
	 * @param text 指定的JSON字符串
	 * @return
	 */
	public static final Object parse(String text) {
		return JSON.parse(text);
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
	 * 将JSON字符串转为JSONArray形式的对象(类似于增强型的ArrayList)
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
