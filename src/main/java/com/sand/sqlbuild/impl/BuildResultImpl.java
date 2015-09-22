package com.sand.sqlbuild.impl;

import java.util.List;

import com.sand.sqlbuild.BuildResult;
import com.sand.sqlbuild.Field;

/**
 *
 * @author : sun.mt
 * @date : 2015年5月7日 上午9:52:10
 * @since 1.0.0
 *
 */
public class BuildResultImpl implements BuildResult {

	private final StringBuilder builder;
	
	private final List<Object> params;
	
	private List<Field<?>> selectFields;
	

	public BuildResultImpl(StringBuilder builder, List<Object> params, List<Field<?>> selectFields) {
		super();
		this.builder = builder;
		this.params = params;
		this.selectFields = selectFields;
	}

	public String getSql() {
		//System.out.println(builder.toString());
		return builder.toString();
	}

	public List<Object> getParameters() {
		return params;
	}

	public List<Field<?>> getSelectFields() {
		if(selectFields == null || selectFields.size() == 0){
			throw new IllegalArgumentException("查询字段不能为空");
		}
		return selectFields;
	}

}
