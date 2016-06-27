package com.sand.sqlbuild.dao.springjdbc.po;

import com.sand.sqlbuild.builder.Field;
import com.sand.sqlbuild.builder.impl.FieldFactory;
import com.sand.sqlbuild.dao.springjdbc.AbstractPo;

/**
 * @author : sun.mt
 * @create : 2015/11/10 10:38
 * @since : 0.1.3
 */
public class PersonPo extends AbstractPo{

	private static final String TABLE_NAME = "person";


	public static final Field<String> name = FieldFactory.create(TABLE_NAME, "name", String.class);
	public static final Field<String> email = FieldFactory.create(TABLE_NAME, "email", String.class);
	public static final Field<String> phone = FieldFactory.create(TABLE_NAME, "phone", String.class);
	public static final Field<Integer> age = FieldFactory.create(TABLE_NAME, "age", Integer.class);


	public static final Field<?>[] all = new Field[]{
			name,
			email,
			phone,
			age
	};


	public String getName () {
		return TABLE_NAME;
	}

	public static Field<String> getEmail () {
		return email;
	}

	public static Field<String> getPhone () {
		return phone;
	}

	public static Field<Integer> getAge () {
		return age;
	}
}
