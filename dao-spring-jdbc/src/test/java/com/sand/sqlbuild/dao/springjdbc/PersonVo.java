package com.sand.sqlbuild.dao.springjdbc;

/**
 * @author : sun.mt
 * @create : 16-6-24 下午9:16
 * @since : ${VERSION}
 */
public class PersonVo {
	String name;

	String email;

	int age;

	String phone;

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public String getEmail () {
		return email;
	}

	public void setEmail (String email) {
		this.email = email;
	}

	public int getAge () {
		return age;
	}

	public void setAge (int age) {
		this.age = age;
	}

	public String getPhone () {
		return phone;
	}

	public void setPhone (String phone) {
		this.phone = phone;
	}
}
