package com.sand.sqlbuild.builder.impl;

import com.sand.sqlbuild.builder.BuildResult;
import com.sand.sqlbuild.builder.Field;

import java.util.List;

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
	
	private final List<Field<?>> selectFields;

	private List<Field<?>> emptyValueFields;
	

	public BuildResultImpl(StringBuilder builder, List<Object> params, List<Field<?>> selectFields, List<Field<?>> emptyValueFields) {
		super();
		this.builder = builder;
		this.params = params;
		this.selectFields = selectFields;
		this.emptyValueFields = emptyValueFields;
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

	public List<Field<?>> getEmptyValuesFields () {
		return emptyValueFields;
	}
}
