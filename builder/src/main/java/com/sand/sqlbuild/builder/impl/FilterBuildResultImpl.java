package com.sand.sqlbuild.builder.impl;

import com.sand.sqlbuild.builder.FilterBuildResult;

import java.util.List;

/**
 *
 * @author : sun.mt
 * @date : 2015年5月8日 下午1:01:12
 * @since 1.0.0
 *
 */
public class FilterBuildResultImpl implements FilterBuildResult {
	
	private final StringBuilder builder;
	
	private final List<Object> params;

	FilterBuildResultImpl(StringBuilder builder, List<Object> params) {
		super();
		this.builder = builder;
		this.params = params;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilderResult#getSql()
	 */
	public String getSql() {
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilderResult#getParameters()
	 */
	public List<Object> getParameters() {
		return params;
	}

}
