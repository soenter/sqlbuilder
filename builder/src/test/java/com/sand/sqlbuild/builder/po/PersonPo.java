package com.sand.sqlbuild.builder.po;

import com.sand.sqlbuild.builder.Field;
import com.sand.sqlbuild.builder.Table;
import com.sand.sqlbuild.builder.impl.FieldFactory;

/**
 * @author : sun.mt
 * @create : 2015/11/10 10:38
 * @since : 0.1.3
 */
public class PersonPo implements Table{

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
}
