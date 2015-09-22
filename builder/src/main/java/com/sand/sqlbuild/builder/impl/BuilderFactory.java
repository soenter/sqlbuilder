package com.sand.sqlbuild.builder.impl;

import com.sand.sqlbuild.builder.Builder;
import com.sand.sqlbuild.builder.FilterBuilder;

/**
 *
 * @author : sun.mt
 * @date : 2015年5月7日 下午2:59:05
 * @since 1.0.0
 *
 */
public class BuilderFactory {

	private BuilderFactory(){}
	
	public static Builder create(){
		return new BuilderImpl();
	}
	
	public static FilterBuilder createFilterBuilder(){
		return new FilterBuilderImpl();
	}
	
}
