package me.ready;

public class User {

	private int id;
	private String username;
	private String password;
	private boolean gender;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public User(int id, String username, String password, boolean gender) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.gender = gender;
	}

}
