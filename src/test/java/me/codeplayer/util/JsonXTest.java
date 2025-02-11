package me.codeplayer.util;

import java.util.*;

import com.alibaba.fastjson2.*;
import org.assertj.core.api.WithAssertions;
import org.assertj.core.data.Index;
import org.junit.Assert;
import org.junit.Test;

public class JsonXTest implements WithAssertions {

	User user = new User(1, "张三", "123456", true);

	Map<Object, Object> map = CollectionX.toMap(LinkedHashMap::new, "name", "张三", "age", 18, "gender", null);

	Object[] array = new Object[] { "Hello", "CodePlayer", true, user };

	List<User> users = CollectionX.asArrayList(user, user);

	@Test
	public void encode() {
		// map
		String jsonStr = JsonX.encode(map);
		assertThat(jsonStr).isEqualTo("{\"name\":\"张三\",\"age\":18}");
		// keep null fields
		assertThat(JsonX.encodeKeepNull(map))
				.isEqualTo("{\"name\":\"张三\",\"age\":18,\"gender\":null}");

		// POJO
		JSONObject json = JsonX.parseObject(jsonStr);
		assertThat(json)
				.containsEntry("name", "张三")
				.containsEntry("age", 18);

		jsonStr = JsonX.encode(user);
		User parsed = JsonX.parseObject(jsonStr, User.class);
		Assert.assertEquals(parsed, user);
		// array
		// ["Hello","CodePlayer",true,{"gender":true,"id":1,"name":"张三","password":"123456"}]
		jsonStr = JsonX.encode(array);
		JSONArray jsonArray = JsonX.parseArray(jsonStr);
		assertThat(jsonArray)
				.hasSize(4)
				.contains("Hello", Index.atIndex(0))
				.contains("CodePlayer", Index.atIndex(1))
				.contains(true, Index.atIndex(2));
		assertThat(jsonArray.getJSONObject(3))
				.hasSize(5)
				.containsEntry("id", 1)
				.containsEntry("name", "张三")
				.containsEntry("password", "123456")
				.containsEntry("gender", true)
				.containsEntry("admin", true);
		// ArrayList
		// 由于集合中存在重复的引用，请注意比较它与下面的encodeWithReferenceDetect()的输出区别
		jsonStr = JsonX.encode(users);
		List<User> list = JsonX.parseArray(jsonStr, User.class);
		Assert.assertEquals(users, list);

	}

	@Test
	public void encodeWithExclude() {
		// map
		String jsonStr = JsonX.encodeWithExclude(map, "age");
		Assert.assertEquals("{\"name\":\"张三\"}", jsonStr);

		jsonStr = JsonX.encodeWithExclude(user, "id", "password");
		// POJO
		LinkedHashMap<String, Object> json = JsonX.parseObject(jsonStr, new TypeReference<LinkedHashMap<String, Object>>() {
		});
		assertThat(json)
				.hasSize(3)
				.containsEntry("name", "张三")
				.containsEntry("admin", true)
				.containsEntry("gender", true);
	}

	@Test
	public void encodeWithInclude() {
		// map
		String jsonStr = JsonX.encodeWithInclude(map, "name");
		Assert.assertEquals("{\"name\":\"张三\"}", jsonStr);

		jsonStr = JsonX.encodeWithInclude(user, "id", "name");
		// POJO
		JSONObject json = (JSONObject) JsonX.parse(jsonStr);
		assertThat(json)
				.hasSize(2)
				.containsEntry("name", "张三")
				.containsEntry("id", 1);
	}

	@Test
	public void encodeRaw() {
		// map
		String jsonStr = JsonX.encodeRaw(map);
		assertThat(jsonStr).isEqualTo("{\"name\":\"张三\",\"age\":18}");

		jsonStr = JsonX.encodeRaw(user);
		User actual = JsonX.parseObject(jsonStr, User.class);
		Assert.assertEquals(user, actual);
		// array
		// ["Hello","CodePlayer",true,{"gender":true,"id":1,"name":"张三","password":"123456"}]
		jsonStr = JsonX.encodeRaw(array);
		JSONArray jsonArray = JsonX.parseArray(jsonStr);
		assertThat(jsonArray)
				.hasSize(4)
				.contains("Hello", Index.atIndex(0))
				.contains("CodePlayer", Index.atIndex(1))
				.contains(true, Index.atIndex(2));
		assertThat(jsonArray.getJSONObject(3))
				.hasSize(5)
				.containsEntry("id", 1)
				.containsEntry("name", "张三")
				.containsEntry("password", "123456")
				.containsEntry("gender", true)
				.containsEntry("admin", true);
		// ArrayList
		// 由于集合中存在重复的引用，请注意比较它与下面的encodeWithReferenceDetect()的输出区别
		jsonStr = JsonX.encodeRaw(users);
		List<User> list = JsonX.parseArray(jsonStr, User.class);
		Assert.assertEquals(users, list);

	}

	@Test
	public void parseObject() {
		JSONObject jsonObj = JsonX.parseObject("{\"name\":\"张三\",\"age\":18}");
		assertThat(jsonObj).isInstanceOf(Map.class)
				.hasSize(2)
				.containsEntry("name", "张三")
				.containsEntry("age", 18);

		String text = "{\"gender\":true,\"id\":1,\"name\":\"张三\",\"password\":\"123456\"}";
		User user = JsonX.parseObject(text, User.class);
		assertThat(user)
				.extracting("name", "password", "id", "gender", "admin")
				.containsExactly("张三", "123456", 1, true, true);

		text = "{\"name\":\"张三\",\"password\":\"123456\"}";
		user = JsonX.parseObject(text, User.class);
		assertThat(user)
				.extracting("name", "password", "id", "gender")
				.containsExactly("张三", "123456", null, false);
	}

	@Test
	public void parseArray() {
		List<Object> list = JsonX.parseArray("['大家好',1,true,null]");
		assertThat(list)
				.hasSize(4)
				.contains("大家好", 1, true, null);
	}

	static class User {

		private Integer id;
		private String name;
		private String password;
		private Integer age;
		private boolean gender;

		public User() {
		}

		public User(int id, String name, String password, boolean gender) {
			this.id = id;
			this.name = name;
			this.password = password;
			this.gender = gender;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
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

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public boolean isGender() {
			return gender;
		}

		public void setGender(boolean gender) {
			this.gender = gender;
		}

		public boolean isAdmin() {
			return Cmp.eq(id, 1);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			User user = (User) o;
			return gender == user.gender && Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(password, user.password);
		}

		@Override
		public int hashCode() {
			return Objects.hash(id, name, password, gender);
		}

	}

}