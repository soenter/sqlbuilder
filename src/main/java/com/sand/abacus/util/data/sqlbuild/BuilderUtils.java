/**
 * Copyright : http://www.sandpay.com.cn/ , 2007-2015
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年6月2日 下午4:41:13
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年6月2日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild;

/**
 *
 * @ClassName ：BuilderUtils
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年6月2日 下午4:41:13
 * @version 1.0.0
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
