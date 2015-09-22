package com.sand.sqlbuild;

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
}
