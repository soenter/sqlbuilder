/**
 * Copyright : http://www.sandpay.com.cn/ , 2007-2015
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年6月2日 下午3:53:10
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年6月2日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild.impl;

import java.util.ArrayList;
import java.util.List;

import com.sand.abacus.util.data.sqlbuild.BuildResult;
import com.sand.abacus.util.data.sqlbuild.Builder;
import com.sand.abacus.util.data.sqlbuild.BuilderUtils;
import com.sand.abacus.util.data.sqlbuild.Field;
import com.sand.abacus.util.data.sqlbuild.Filter;
import com.sand.abacus.util.data.sqlbuild.FilterBuilder;
import com.sand.abacus.util.data.sqlbuild.Order;
import com.sand.abacus.util.data.sqlbuild.PagingBuilder;
import com.sand.abacus.util.data.sqlbuild.Table;

/**
 *
 * @ClassName ：PagingBuilderImpl
 * @Description : 
 * @author : sun.mt
 * @date : 2015年6月2日 下午3:53:10
 * @since 1.0.0
 *
 */
public class PagingBuilderImpl implements PagingBuilder {
	
	private Builder selectBuilder;
	private Builder fromBuilder;
	private Builder whereBuilder;
	private Builder orderBuilder;
	private StringBuilder builder;
	
	private BuildResult selectBr;
	private BuildResult fromBr;
	private BuildResult whereBr;
	private BuildResult orderBr;
	
	private Field<?> fromPk;
	
	private List<Object> params;
	
	PagingBuilderImpl(){
		init();
	}
	
	private void init(){
		selectBuilder = BuilderFactory.create();
		fromBuilder = BuilderFactory.create();
		whereBuilder = BuilderFactory.create();
		orderBuilder = BuilderFactory.create();
		
		selectBr = null;
		fromBr = null;
		whereBr = null;
		orderBr = null;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.PagingBuilder#select(com.sand.abacus.util.data.sqlbuild.Field[])
	 */
	public PagingBuilder select(Field<?>... fields) {
		selectBuilder.select(fields);
		
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.PagingBuilder#from(com.sand.abacus.util.data.sqlbuild.Table)
	 */
	public PagingBuilder from(Class<? extends Table> clazz, Field<?> pk) {
		fromBuilder.from(clazz);
		fromPk = pk;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.PagingBuilder#innerJoin(com.sand.abacus.util.data.sqlbuild.Table)
	 */
	public PagingBuilder innerJoin(Class<? extends Table> clazz) {
		fromBuilder.innerJoin(clazz);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.PagingBuilder#leftJoin(com.sand.abacus.util.data.sqlbuild.Table)
	 */
	public PagingBuilder leftJoin(Class<? extends Table> clazz) {
		fromBuilder.leftJoin(clazz);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.PagingBuilder#rightJoin(com.sand.abacus.util.data.sqlbuild.Table)
	 */
	public PagingBuilder rightJoin(Class<? extends Table> clazz) {
		fromBuilder.rightJoin(clazz);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.PagingBuilder#on(com.sand.abacus.util.data.sqlbuild.Field)
	 */
	public PagingBuilder on(Filter<?> filter) {
		fromBuilder.on(filter);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.PagingBuilder#where(com.sand.abacus.util.data.sqlbuild.Filter[])
	 */
	public PagingBuilder where(Filter<?>... filters) {
		BuilderUtils.joinFiltersByAnd(whereBuilder, filters);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.PagingBuilder#where(com.sand.abacus.util.data.sqlbuild.FilterBuilder)
	 */
	public PagingBuilder where(FilterBuilder filterBuilder) {
		whereBuilder.where(filterBuilder);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.PagingBuilder#and(com.sand.abacus.util.data.sqlbuild.Filter)
	 */
	public PagingBuilder and(Filter<?> filter) {
		whereBuilder.and(filter);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.PagingBuilder#or(com.sand.abacus.util.data.sqlbuild.Filter)
	 */
	public PagingBuilder or(Filter<?> filter) {
		whereBuilder.or(filter);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.PagingBuilder#orderBy(com.sand.abacus.util.data.sqlbuild.Order)
	 */
	public PagingBuilder orderBy(Order... orders) {
		orderBuilder.orderBy(orders);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.PagingBuilder#buildPagingCount()
	 */
	public BuildResult buildPagingCount() {
		//构建结果
		buildResult();
		builder = new StringBuilder();
		params = new ArrayList<Object>();
		
		builder.append("select count(*) ");
		
		//from builder
		builder.append(fromBr.getSql());

		//where builder
		builder.append(whereBr.getSql());
		params.addAll(whereBr.getParameters());
		
		return new BuildResultImpl(builder, params, null);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.PagingBuilder#buildPagingList()
	 */
	public BuildResult buildPagingList(int pageStart, int pageEnd, int pageLimit) {
		if(pageStart < 1){
			throw new IllegalArgumentException("分页开始数不能小于1");
		}
		if(pageEnd <= pageStart){
			throw new IllegalArgumentException("分页结束数不能小于等于分页开始数");
		}
		if(pageLimit < 1){
			throw new IllegalArgumentException("分页限制数不能小于1");
		}
		pageStart = pageStart - 1;
		pageEnd = pageEnd - 1;
		//构建结果
		buildResult();
		builder = new StringBuilder();
		params = new ArrayList<Object>();
		
		builder.append("with rownum_table as ( select row_number() over( ");
		
		//order builder
		builder.append(orderBr.getSql());
		builder.append(") rownum_table_rn , ");
		
		if(fromPk == null){
			throw new IllegalArgumentException("from 方法中传入的pk字段不能为null");
		}
		
		builder.append(fromPk.getFullName()).append(" ");
		
		//from builder
		builder.append(fromBr.getSql()).append(" ");
		params.addAll(fromBr.getParameters());

		//where builder
		builder.append(whereBr.getSql()).append(" ");
		params.addAll(whereBr.getParameters());
		
		builder.append(") ");
		
		//select builder
		builder.append(selectBr.getSql()).append(" ");
		params.addAll(selectBr.getParameters());
		
		//from
		builder.append(fromBr.getSql()).append(" ");
		params.addAll(fromBr.getParameters());
		
		//inner join rownum_table
		builder.append("inner join rownum_table on ").append(fromPk.getFullName())
		.append(" = ").append("rownum_table.").append(fromPk.getName())
		.append(" and rownum_table.rownum_table_rn between ? and ? ");
		params.add(pageStart * pageLimit);
		params.add(pageEnd * pageLimit);
		
		//where
		builder.append(whereBr.getSql()).append(" ");
		params.addAll(whereBr.getParameters());
		
		//order 
		builder.append(orderBr.getSql()).append(" ");
		
		return new BuildResultImpl(builder, params, selectBr.getSelectFields());
	}
	
	private void buildResult(){
		if(selectBr == null){
			selectBr = selectBuilder.build();
		}
		if(fromBr == null){
			fromBr = fromBuilder.build();
		}
		if(whereBr == null){
			whereBr = whereBuilder.build();
		}
		if(orderBr == null){
			orderBr = orderBuilder.build();
		}
	}

}
