package com.sand.sqlbuild.builder.impl;

import com.sand.sqlbuild.builder.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author : sun.mt
 * @date : 2015年6月2日 下午3:53:10
 * @since 1.0.0
 *
 */
public class PagingBuilderImpl extends BuilderImpl<PagingBuilder> implements PagingBuilder {
	
	private Builder<PagingBuilder> selectBuilder;
	private Builder<PagingBuilder> fromBuilder;
	private Builder<PagingBuilder> whereBuilder;
	private Builder<PagingBuilder> orderBuilder;
	private Builder<PagingBuilder> groupbyBuilder;

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
		groupbyBuilder = BuilderFactory.create();

		selectBr = null;
		fromBr = null;
		whereBr = null;
		orderBr = null;
	}

	@Override
	public PagingBuilder select () {
		selectBuilder.select();
		return this;
	}

	@Override
	public PagingBuilder select (Fieldable... fields) {
		selectBuilder.select(fields);
		return this;
	}

	public PagingBuilder from(Class<? extends Table> clazz, Field<?> pk) {
		fromBuilder.from(clazz);
		fromPk = pk;
		return this;
	}

	@Override
	public PagingBuilder from(Class<? extends Table> clazz) {
		fromBuilder.from(clazz);
		return this;
	}

	@Override
	public PagingBuilder from (Class<? extends Table> first, Class<? extends Table>... clazzs) {
		fromBuilder.from(first, clazzs);
		return this;
	}

	@Override
	public PagingBuilder where () {
		whereBuilder.where();
		return this;
	}

	@Override
	public PagingBuilder where (Filter<?> filter) {
		whereBuilder.where(filter);
		return this;
	}

	@Deprecated
	public PagingBuilder where(FilterBuilder filterBuilder) {
		whereBuilder.where(filterBuilder);
		return this;
	}

	@Override
	public PagingBuilder where (Jointer jointer) {
		whereBuilder.where(jointer);
		return this;
	}

	@Override
	public PagingBuilder orderBy(Order... orders) {
		orderBuilder.orderBy(orders);
		return this;
	}

	@Override
	public PagingBuilder groupBy (Fieldable... fields) {
		groupbyBuilder.groupBy(fields);
		return this;
	}

	public BuildResult buildPagingCount() {
		//构建结果
		buildResult();
		StringBuilder builder = new StringBuilder();
		params = new ArrayList<Object>();
		
		builder.append("select count(*) ");
		
		//from builder
		builder.append(fromBr.getSql());

		//where builder
		builder.append(whereBr.getSql());
		if(whereBr.getParameters() != null){
			params.addAll(whereBr.getParameters());
		}

		return new BuildResultImpl(builder, params, null, null);
	}

	private void checkPageArgs(int pageStart, int pageEnd, int pageLimit){
		if(pageStart < 1){
			throw new IllegalArgumentException("分页开始数不能小于1");
		}
		if(pageEnd <= pageStart){
			throw new IllegalArgumentException("分页结束数不能小于等于分页开始数");
		}
		if(pageLimit < 1){
			throw new IllegalArgumentException("分页限制数不能小于1");
		}
	}

	public BuildResult buildPagingList(int pageStart, int pageEnd, int pageLimit) {
		checkPageArgs(pageStart, pageEnd, pageLimit);
		pageStart = pageStart - 1;
		pageEnd = pageEnd - 1;
		//构建结果
		buildResult();
		StringBuilder builder = new StringBuilder();
		params = new ArrayList<Object>();
		
		builder.append("with rownum_table as ( select row_number() over( ");
		
		//order builder
		builder.append(orderBr.getSql());
		builder.append(") rownum_table_rn , ");
		
		if(fromPk == null){
			throw new IllegalArgumentException("oracle 分页查询 from 方法中传入的pk字段不能为null");
		}
		
		builder.append(fromPk.getFullName()).append(" ");
		
		//from builder
		builder.append(fromBr.getSql()).append(" ");
		if(fromBr.getParameters() != null){
			params.addAll(fromBr.getParameters());
		}

		//where builder
		builder.append(whereBr.getSql()).append(" ");
		if(whereBr.getParameters() != null){
			params.addAll(whereBr.getParameters());
		}
		
		builder.append(") ");
		
		//select builder
		builder.append(selectBr.getSql()).append(" ");
		if(selectBr.getParameters() != null){
			params.addAll(selectBr.getParameters());
		}
		
		//from
		builder.append(fromBr.getSql()).append(" ");
		if(fromBr.getParameters() != null){
			params.addAll(fromBr.getParameters());
		}
		
		//inner join rownum_table
		builder.append("inner join rownum_table on ").append(fromPk.getFullName())
		.append(" = ").append("rownum_table.").append(fromPk.getName())
		.append(" and rownum_table.rownum_table_rn between ? and ? ");
		params.add(pageStart * pageLimit + 1);
		params.add(pageEnd * pageLimit);
		
		//where
		builder.append(whereBr.getSql()).append(" ");
		if(whereBr.getParameters() != null){
			params.addAll(whereBr.getParameters());
		}
		
		//order 
		builder.append(orderBr.getSql()).append(" ");

		return new BuildResultImpl(builder, params, selectBr.getSelectFields(), selectBr.getEmptyValuesFields());
	}

	public BuildResult buildMySqlPagingList (int pageStart, int pageEnd, int pageLimit) {
		checkPageArgs(pageStart, pageEnd, pageLimit);
		pageStart = pageStart - 1;
		pageEnd = pageEnd - 1;
		//构建结果
		buildResult();
		StringBuilder builder = new StringBuilder();
		params = new ArrayList<Object>();

		//select builder
		builder.append(selectBr.getSql()).append(" ");
		if(selectBr.getParameters() != null){
			params.addAll(selectBr.getParameters());
		}

		//from
		builder.append(fromBr.getSql()).append(" ");
		if(fromBr.getParameters() != null){
			params.addAll(fromBr.getParameters());
		}

		//where
		builder.append(whereBr.getSql()).append(" ");
		if(whereBr.getParameters() != null){
			params.addAll(whereBr.getParameters());
		}

		//order
		builder.append(orderBr.getSql()).append(" ");

		// limit
		builder.append("limit ").append(pageStart * pageLimit).append(", ").append(pageEnd * pageLimit);
		System.out.println("mysql sql:" + builder.toString());
		return new BuildResultImpl(builder, params, selectBr.getSelectFields(), selectBr.getEmptyValuesFields());
	}

	@Override
	public BuildResult build () {
		throw new UnsupportedOperationException("PagingBuilder 不支持 build() 方法");
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
