package com.sand.sqlbuild.builder;

import com.sand.sqlbuild.builder.impl.BuilderFactory;

/**
 * @author : sun.mt
 * @date : 2015/11/29 17:03
 * @since : 0.2.0
 */
@SuppressWarnings("unchecked")
public class BSQL {

	public static Builder select (){
		return BuilderFactory.create().select();
	}

	public static Builder select (Fieldable... fields){
		return BuilderFactory.create().select(fields);
	}

	public static Builder insert (Class<? extends Table> clazz, Field<?>... fields){
		return BuilderFactory.create().insert(clazz, fields);
	}
	public static Builder insert (Table table, Field<?>... fields){
		return BuilderFactory.create().insert(table, fields);
	}

	public static Builder insert (Class<? extends Table> clazz, Setter<?>... setters){
		return BuilderFactory.create().insert(clazz, setters);
	}

	public static Builder insert (Table table, Setter<?>... setters){
		return BuilderFactory.create().insert(table, setters);
	}

	public static Builder update (Class<? extends Table> clazz){
		return BuilderFactory.create().update(clazz);
	}

	public static Builder delete (){
		return BuilderFactory.create().delete();
	}

}
