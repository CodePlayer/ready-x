package me.codeplayer.util;

import java.util.*;

import com.alibaba.fastjson2.*;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

public class JSONUtilTest implements WithAssertions {

	User user = new User(1, "张三", "123456", true);

	Map<Object, Object> map = CollectionUtil.toMap(LinkedHashMap::new, "name", "张三", "age", 18, "gender", null);

	Object[] array = new Object[] { "Hello", "CodePlayer", true, user };

	List<User> users = CollectionUtil.asArrayList(user, user);

	@Test
	public void encode() {
		// map
		String jsonStr = JSONUtil.encode(map);
		assertThat(jsonStr).isEqualTo("{\"name\":\"张三\",\"age\":18}");
		// keep null fields
		assertThat(JSONUtil.encodeKeepNull(map))
				.isEqualTo("{\"name\":\"张三\",\"age\":18,\"gender\":null}");

		// POJO
		JSONObject json = JSONUtil.parseObject(jsonStr);
		assertThat(json)
				.containsEntry("name", "张三")
				.containsEntry("age", 18);

		assertThat(JSONUtil.encode(user)).isEqualTo("{\"gender\":true,\"id\":1,\"name\":\"张三\",\"password\":\"123456\"}");
		// array
		// ["Hello","CodePlayer",true,{"gender":true,"id":1,"name":"张三","password":"123456"}]
		assertThat(JSONUtil.encode(array))
				.isEqualTo("[\"Hello\",\"CodePlayer\",true,{\"gender\":true,\"id\":1,\"name\":\"张三\",\"password\":\"123456\"}]");
		// ArrayList
		// 由于集合中存在重复的引用，请注意比较它与下面的encodeWithReferenceDetect()的输出区别
		assertThat(JSONUtil.encode(users)) // [{"gender":true,"id":1,"name":"张三","password":"123456"},{"gender":true,"id":1,"name":"张三","password":"123456"}]
				.isEqualTo("[{\"gender\":true,\"id\":1,\"name\":\"张三\",\"password\":\"123456\"},{\"gender\":true,\"id\":1,\"name\":\"张三\",\"password\":\"123456\"}]");

	}

	@Test
	public void encodeWithReferenceDetect() {
		// ArrayList
		// [{"gender":true,"id":1,"name":"张三","password":"123456"},{"$ref":"$[0]"}]
		assertThat(JSON.toJSONString(users, JSONWriter.Feature.ReferenceDetection))
				.isEqualTo("[{\"gender\":true,\"id\":1,\"name\":\"张三\",\"password\":\"123456\"},{\"$ref\":\"$[0]\"}]");
	}

	@Test
	public void parseObject() {
		JSONObject jsonObj = JSONUtil.parseObject("{\"name\":\"张三\",\"age\":18}");
		assertThat(jsonObj).isInstanceOf(Map.class)
				.hasSize(2)
				.containsEntry("name", "张三")
				.containsEntry("age", 18);

		String text = "{\"gender\":true,\"id\":1,\"name\":\"张三\",\"password\":\"123456\"}";
		User user = JSONUtil.parseObject(text, User.class);
		assertThat(user)
				.extracting("name", "password", "id", "gender")
				.containsExactly("张三", "123456", 1, true);

		text = "{\"name\":\"张三\",\"password\":\"123456\"}";
		user = JSONUtil.parseObject(text, User.class);
		assertThat(user)
				.extracting("name", "password", "id", "gender")
				.containsExactly("张三", "123456", 0, false);
	}

	@Test
	public void parseArray() {
		List<Object> list = JSONUtil.parseArray("['大家好',1,true,null]");
		assertThat(list)
				.hasSize(4)
				.contains("大家好", 1, true, null);
	}

	static class User {

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

}