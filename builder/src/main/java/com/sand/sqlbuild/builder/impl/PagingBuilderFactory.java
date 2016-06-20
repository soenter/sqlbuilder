package com.sand.sqlbuild.builder.impl;

import com.sand.sqlbuild.builder.PagingBuilder;

/**
 *
 * @author : sun.mt
 * @date : 2015年6月2日 下午5:20:21
 * @since 1.0.0
 *
 */
public class PagingBuilderFactory {

	public static PagingBuilder create(){
		return new PagingBuilderImpl(true).initBuilders();
	}
}
