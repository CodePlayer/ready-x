package me.codeplayer.util;

import java.util.Objects;

public class User {

	Integer id;
	String name;
	String password;
	Integer age;
	boolean gender;

	public User() {
	}

	public User(Integer id) {
		this.id = id;
	}

	public User(Integer id, String name, String password, boolean gender) {
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