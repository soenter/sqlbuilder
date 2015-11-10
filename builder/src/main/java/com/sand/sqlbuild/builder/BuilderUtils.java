package com.sand.sqlbuild.builder;

/**
 *
 * @author : sun.mt
 * @date : 2015年6月2日 下午4:41:13
 * @since 1.0.0
 *
 */
public class BuilderUtils {

	public static void joinFiltersByAnd(Builder builder, Filter<?>[] filters){
		for(int i = 0; i < filters.length; i++){
			if(i == 0) {
				builder.where(filters[i]);
			} else {
				builder.and(filters[i]);
			}
		}
	}



	public static Table newTable(Class<? extends Table> clazz){
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
