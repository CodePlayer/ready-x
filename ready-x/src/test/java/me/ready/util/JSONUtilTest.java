package me.ready.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;

@SuppressWarnings("unchecked")
public class JSONUtilTest {

	//	@Test
	public void encode() {
		// 编码HashMap
		Map<Object, Object> map = CollectionUtil.createHashMap("name", "张三", "age", 18);
		System.out.println(JSONUtil.encode(map)); // {"age":18,"name":"张三"}
		// 编码User对象
		User user = new User(1, "张三", "123456", true);
		System.out.println(JSONUtil.encode(user)); // {"gender":true,"id":1,"name":"张三","password":"123456"}
		// 编码数组
		Object[] objects = new Object[] { "Hello", "云创", true, user };
		System.out.println(JSONUtil.encode(objects)); // ["Hello","云创",true,{"gender":true,"id":1,"name":"张三","password":"123456"}]
		// 编码List集合
		List<User> users = new ArrayList<User>();
		users.add(user);
		users.add(user); // 由于集合中存在重复的引用，请注意比较它与下面的encodeWithReferenceDetect()的输出区别
		System.out.println(JSONUtil.encode(users)); // [{"gender":true,"id":1,"name":"张三","password":"123456"},{"gender":false,"id":2,"name":"李四","password":"12345"}]
	}

	//	@Test
	public void encodeWithReferenceDetect() {
		// 编码HashMap
		Map<Object, Object> map = CollectionUtil.createHashMap("name", "张三", "age", 18);
		System.out.println(JSONUtil.encodeWithReferenceDetect(map)); // {"age":18,"name":"张三"}
		// 编码User对象
		User user = new User(1, "张三", "123456", true);
		System.out.println(JSONUtil.encodeWithReferenceDetect(user)); // {"gender":true,"id":1,"name":"张三","password":"123456"}
		// 编码数组
		Object[] objects = new Object[] { "Hello", "云创", true, user };
		System.out.println(JSONUtil.encodeWithReferenceDetect(objects)); // ["Hello","云创",true,{"gender":true,"id":1,"name":"张三","password":"123456"}]
		// 编码List集合
		List<User> users = new ArrayList<User>();
		users.add(user);
		users.add(user);
		System.out.println(JSONUtil.encodeWithReferenceDetect(users)); // [{"gender":true,"id":1,"name":"张三","password":"123456"},{"$ref":"$[0]"}]
	}

//	@Test
	public void parse() {
		Object json = JSONUtil.parse("['大家好']");
		System.out.println(json.getClass());
	}

	//	@Test
	public void parseObject() {
		String text = "{\"gender\":true,\"id\":1,\"name\":\"张三\",\"password\":\"123456\"}";
		User user = JSONUtil.parseObject(text, User.class);
		System.out.println(user.getName()); // 张三
		System.out.println(user.getPassword()); // 123456
		System.out.println(user.getId()); // 1
	}

		@Test
	public void parseArray() {
		JSONArray jsonArray = JSONUtil.parseArray("['大家好']");
		System.out.println(jsonArray.get(0)); // 大家好
	}
}

class User {

	private int id;
	private String name;
	private String password;
	private boolean gender;

	public User() {
	}

	public User(int id, String name, String password, boolean gender) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.gender = gender;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}
}
