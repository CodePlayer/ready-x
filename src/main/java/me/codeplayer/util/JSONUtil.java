package me.codeplayer.util;

import java.util.*;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.serializer.*;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * JSON字符串序列化转换工具类
 *
 * @author Ready
 * @since 2014-10-13
 */
public abstract class JSONUtil {

	public static int DEFAULT_SERIALIZER_FEATURES = JSON.DEFAULT_GENERATE_FEATURE | SerializerFeature.DisableCircularReferenceDetect.getMask();

	public static int mergeFeatures(int base, SerializerFeature extra) {
		return base | extra.getMask();
	}

	public static int mergeFeatures(int base, SerializerFeature... extra) {
		for (SerializerFeature feature : extra) {
			base |= feature.getMask();
		}
		return base;
	}

	/**
	 * 将Java对象编码为JSON字符串。<br/>
	 * 如果对象里存在为null的属性，则不包含在字符串中。
	 *
	 * @param obj 指定的任意对象
	 */
	public static String encode(Object obj) {
		return JSON.toJSONString(obj, DEFAULT_SERIALIZER_FEATURES);
	}

	/**
	 * 将Java对象编码为JSON字符串。<br/>
	 * 值为null的属性也会保留并输出。
	 *
	 * @param obj 指定的任意对象
	 */
	public static String encodeKeepNull(Object obj) {
		return JSON.toJSONString(obj, mergeFeatures(DEFAULT_SERIALIZER_FEATURES, SerializerFeature.WriteMapNullValue), EMPTY);
	}

	/**
	 * 将Java对象编码为JSON字符串
	 *
	 * @param obj 指定的任意对象
	 * @param excludeProperties 需要排除的属性数组
	 */
	public static String encodeWithExclude(Object obj, String... excludeProperties) {
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
		Collections.addAll(filter.getExcludes(), excludeProperties);
		return serialize(obj, null, filter);
	}


	public static String serialize(Object obj, String dateFormat, SerializeFilter... filters) {
		return JSON.toJSONString(obj, SerializeConfig.globalInstance, filters, dateFormat, DEFAULT_SERIALIZER_FEATURES, EMPTY);
	}


	/**
	 * 将Java对象编码为JSON字符串
	 *
	 * @param obj 指定的任意对象
	 * @param includeProperties 需要排除的属性数组
	 */
	public static String encodeWithInclude(Object obj, String... includeProperties) {
		return serialize(obj, null, new SimplePropertyPreFilter(includeProperties));
	}

	/**
	 * 将Java对象编码为JSON字符串，并以指定的格式化模式处理日期类型
	 *
	 * @param obj 指定的任意对象
	 * @param pattern 指定的格式化字符串，例如{@code "yyyy-MM-dd"}
	 */
	public static String encodeWithDateFormat(Object obj, String pattern) {
		return serialize(obj, pattern, (SerializeFilter) null);
	}

	/**
	 * 将Java对象编码为JSON字符串，它与encode()方法不同的是，它会进行循环引用检测。如果其中的多个元素或属性指向同一个对象引用，则后者将只输出对前者的引用表示
	 */
	public static String encodeWithReferenceDetect(Object obj) {
		return JSON.toJSONString(obj);
	}

	/**
	 * 将JSON字符串转为对应的JSONObject或JSONArray对象
	 *
	 * @param text 指定的JSON字符串
	 */
	public static Object parse(String text) {
		return JSON.parse(text);
	}

	/**
	 * 将JSON字符串转为指定类型的Java对象
	 *
	 * @param text 指定的JSON字符串
	 * @param clazz 指定的类型
	 */
	public static <T> T parseObject(String text, Class<T> clazz) {
		return JSON.parseObject(text, clazz);
	}

	/**
	 * 将JSON字符串转为JSONObject形式的对象(类似于增强型的 LinkedHashMap)
	 *
	 * @param text 指定的JSON字符串
	 */
	public static JSONObject parseObject(String text) {
		return JSON.parseObject(text, Feature.OrderedField);
	}

	/**
	 * 将JSON字符串转为JSONArray形式的对象(类似于增强型的ArrayList)
	 *
	 * @param text 指定的JSON字符串
	 */
	public static JSONArray parseArray(String text) {
		return JSON.parseArray(text);
	}

	/**
	 * 将JSON字符串转为List形式的指定类型的对象集合
	 *
	 * @param text 指定的JSON字符串
	 * @param clazz 指定的类型
	 */
	public static <T> List<T> parseArray(String text, Class<T> clazz) {
		return JSON.parseArray(text, clazz);
	}

	/**
	 * 将指定的Java对象序列化为JSON字符串
	 *
	 * @param obj 指定的对象
	 * @since 0.1
	 */
	public static String serialize(Object obj) {
		return JSON.toJSONString(obj, mergeFeatures(DEFAULT_SERIALIZER_FEATURES, SerializerFeature.WriteClassName), EMPTY);
	}

	/**
	 * 将指定的JSON字符串反序列化为指定的Java对象
	 *
	 * @param text 指定的JSON字符串
	 * @since 0.1
	 */
	public static <T> T deserialize(String text) {
		return X.castType(JSON.parse(text, Feature.SupportAutoType));
	}
}
