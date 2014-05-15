package com.deutchall.identification;

public class User {
	
	private String name;
	private String email;
	
	public User(String name, String email) {
		this.name = name;
		this.email = email;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof User) {
			User aux = (User) o;
			return aux.getName().equals(this.getName());
		} else {
			return false;
		}
	}
}
