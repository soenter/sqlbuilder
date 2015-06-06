/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-jdbc-dao
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月6日 上午11:11:34
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月6日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild.impl;

import com.sand.abacus.util.data.sqlbuild.*;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @ClassName ：BuilderImpl
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年5月6日 上午11:11:34
 * @version 1.0.0
 *
 */
public class BuilderImpl implements Builder{

	private StringBuilder builder;
	
	private List<Object> params;
	
	private List<Field<?>> selectFields;
	
	private int fieldCount;
	private int valueCount;
	
	
	boolean isFirstSetFields;
	boolean isFirstSetSetters;
	boolean isFirstSetValues;
	boolean isFirstSetOrders;
	boolean isFirstSetGroups;
	boolean isFirstSetArgs;
	boolean isUnionFlag;
	
	
	//private Type type;
	
	//private Join join;
	
	BuilderImpl(){
		init();
	}
	
	private void init(){
		builder = new StringBuilder();
		params = new ArrayList<Object>();
		selectFields = new ArrayList<Field<?>>();
		fieldCount = 0;
		valueCount = 0;
		isFirstSetFields = true;
		isFirstSetSetters = true;
		isFirstSetValues = true;
		isFirstSetOrders = true;
		isFirstSetGroups = true;
		isFirstSetArgs = true;
		isUnionFlag = false;
	}
	
	
	/*------------------------------------查询（select）------------------------------------*/
	
	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#select()
	 */
	@Override
	public Builder select() {
		//type = Type.select;
		builder.append("select ");
		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#select(com.sand.abacus.util.data.sqlbuild.Field[])
	 */
	@Override
	public Builder select(Field<?>... fields) {
		select();
		fields(fields);
		return this;
	}

	@Override
	public Builder fields(Field<?>... fields) {
		if(fields == null || (fieldCount = fields.length) == 0){
			throw new IllegalArgumentException("fields参数不能为null或长度等于0");
		}
		for (Field<?> field : fields) {
			if(isFirstSetFields){
				isFirstSetFields = false;
			} else {
				dot();
			}
			builder.append(field.getFullName());
			if(!isUnionFlag){
				selectFields.add(field);
			}
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#selectAll()
	 */
	/*@Override
	public Builder all() {
		builder.append(" * ");
		return this;
	}*/

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#distinct()
	 */
	@Override
	public Builder distinct() {
		builder.append(" distinct ");
		return this;
	}

	/*------------------------------------插入（insert）------------------------------------*/


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#insert(com.sand.abacus.util.data.sqlbuild.Table)
	 */
	@Override
	public Builder insert(Class<? extends Table> clazz, Field<?>... fields) {
		//type = Type.insert;
		Table table = newTable(clazz);
		builder.append("insert into ").append(table.getName()).append("(");
		fields(fields);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#values(java.lang.Object[])
	 */
	@Override
	public Builder values(Object... values) {
		if(values == null || (valueCount = values.length) == 0){
			throw new IllegalArgumentException("values参数不能为null或长度等于0");
		}
		
		if(valueCount != fieldCount){
			throw new IllegalArgumentException("values参数长度:" + valueCount + "与fields参数长度:" + fieldCount + "不匹配");
		}
		
		builder.append(") values( ");
		for (Object object : values) {
			if(isFirstSetValues){
				isFirstSetValues =  false;
			} else {
				dot();
			}
			builder.append("?");
			valueCount ++;
			params.add(object);
		}
		builder.append(")");
		
		
		return this;
	}


	/*------------------------------------更新（update）------------------------------------*/


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#update(com.sand.abacus.util.data.sqlbuild.Table)
	 */
	@Override
	public Builder update(Class<? extends Table> clazz) {
		//type = Type.update;
		Table table = newTable(clazz);
		builder.append("update ").append(table.getName());
		return this;
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#set(com.sand.abacus.util.data.sqlbuild.Setter[])
	 */
	@Override
	public Builder set(Setter<?>... setters) {
		if(setters == null || setters.length == 0){
			throw new IllegalArgumentException("values参数不能为null或长度等于0");
		}
		builder.append(" set ");
		for (Setter<?> setter : setters) {
			if(isFirstSetSetters){
				isFirstSetSetters = false;
			} else {
				dot();
			}
			if(setter.isFieldValue()){
				
				Field<?> fieldValue = setter.getFieldValue();
				builder.append(setter.getField().getFullName()).append(" = ").append(fieldValue.getFullName());
				
				if(fieldValue.hasOperator()){
					builder.append(" ").append(fieldValue.getOperator()).append(" ?");
					params.add(fieldValue.getOperValue());
				}
				
			} else {
				builder.append(setter.getField().getFullName()).append(" = ?");
				params.add(setter.getValue());
			}
		}
		return this;
	}

	/*------------------------------------删除（update）------------------------------------*/
	
	
	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#delete()
	 */
	@Override
	public Builder delete() {
		builder.append("delete ");
		return this;
	}

	/*------------------------------------表连接（join）------------------------------------*/

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#join(java.lang.Class)
	 */
	@Override
	public Builder join(Class<? extends Table> clazz) {
		//FIXME 获取表名，改为注解形式
		Table table = newTable(clazz);
		builder.append(" inner join ").append(table.getName());
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#innerJoin(java.lang.Class)
	 */
	@Override
	public Builder innerJoin(Class<? extends Table> clazz) {
		return join(clazz);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#leftJoin(java.lang.Class)
	 */
	@Override
	public Builder leftJoin(Class<? extends Table> clazz) {
		//FIXME 获取表名，改为注解形式
		Table table = newTable(clazz);
		builder.append(" left join ").append(table.getName());
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#rightJoin(java.lang.Class)
	 */
	@Override
	public Builder rightJoin(Class<? extends Table> clazz) {
		//FIXME 获取表名，改为注解形式
		Table table = newTable(clazz);
		builder.append(" right join ").append(table.getName());
		return this;
	}
	
	/*------------------------------------通用（common）------------------------------------*/


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#from(java.lang.Class)
	 */
	@Override
	public Builder from(Class<? extends Table> clazz) {
		//FIXME 获取表名，改为注解形式
		Table table = newTable(clazz);
		builder.append(" from ").append(table.getName());
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#from(java.lang.Class[])
	 */
	@Override
	public Builder from(Class<? extends Table>[] clazzs) {
		builder.append(" from ");
		boolean isFirst = true;
		for (Class<? extends Table> class1 : clazzs) {
			//FIXME 获取表名，改为注解形式
			Table table = newTable(class1);
			if(isFirst){
				isFirst = false;
			} else {
				dot();
			}
			builder.append(table.getName());
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#on(com.sand.abacus.util.data.sqlbuild.Filter)
	 */
	@Override
	public Builder on(Filter<?> filter) {
		builder.append(" on ").append(filter.getField().getFullName()).append(filter.getOperator());
		if(filter.getFieldValue() != null){
			builder.append(filter.getFieldValue().getFullName());
		} else {
			builder.append("?");
			params.add(filter.getValue());
		}
		return this;
	}

	private Table newTable(Class<? extends Table> clazz){
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage());
		}
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#where(com.sand.abacus.util.data.sqlbuild.Filter)
	 */
	@Override
	public Builder where(Filter<?> filter) {
		builder.append(" where ");
		return filter(filter);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#where(com.sand.abacus.util.data.sqlbuild.FilterBuilder)
	 */
	@Override
	public Builder where(FilterBuilder filterBuilder) {
		FilterBuildResult fbr = filterBuilder.build();
		builder.append(" where ").append(fbr.getSql());
		params.addAll(fbr.getParameters());
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#and()
	 */
	@Override
	public Builder and() {
		builder.append(" and ");
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#and(com.sand.abacus.util.data.sqlbuild.Filter)
	 */
	@Override
	public Builder and(Filter<?> filter) {
		and();
		return filter(filter);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#and(com.sand.abacus.util.data.sqlbuild.FilterBuilder)
	 */
	@Override
	public Builder and(FilterBuilder filterBuilder) {
		FilterBuildResult fbr = filterBuilder.build();
		builder.append(" and ").append(fbr.getSql());
		params.addAll(fbr.getParameters());
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#or(com.sand.abacus.util.data.sqlbuild.Filter)
	 */
	@Override
	public Builder or(Filter<?> filter) {
		builder.append(" or ");
		return filter(filter);
	}
	
	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#or(com.sand.abacus.util.data.sqlbuild.FilterBuilder)
	 */
	@Override
	public Builder or(FilterBuilder filterBuilder) {
		FilterBuildResult fbr = filterBuilder.build();
		builder.append(" or ").append(fbr.getSql());
		params.addAll(fbr.getParameters());
		return this;
	}

	private Builder filter(Filter<?> filter){
		builder.append(filter.getField().getFullName()).append(" ");
		String operator = filter.getOperator();
		if(filter.isFieldValue()){
			
			Field<?> fieldValue = filter.getFieldValue();
			builder.append(operator).append(" ").append(fieldValue.getFullName());
			
			if(fieldValue.hasOperator()){
				builder.append(" ").append(fieldValue.getOperator()).append(" ");
			}
		} else {
			if(filter.getType() == com.sand.abacus.util.data.sqlbuild.Filter.Type.ONE){
				if(null != filter.getValue()){
					builder.append(operator).append(" ").append("?");
					params.add(filter.getValue());
				} else {
					builder.append(operator).append(" ");
				}
			} else if(filter.getType() == com.sand.abacus.util.data.sqlbuild.Filter.Type.TWIN){
				if(filter.getValues() == null) 
					throw new IllegalArgumentException("Filter.Type.TWIN时 值对象数组长度必须为2");

				builder.append(operator).append(" ");
				
				params.add(filter.getValues()[0]);
				params.add(filter.getValues()[1]);
				
			} else if(filter.getType() == com.sand.abacus.util.data.sqlbuild.Filter.Type.MULTI){
				if(filter.getValues() == null) 
					throw new IllegalArgumentException("Filter.Type.MULTI时值对象数组不能为null");
				
				boolean isFirst = true;
				builder.append(operator).append(" (");
				for (Object value : filter.getValues()) {
					if(isFirst){
						isFirst = false;
					} else {
						dot();
					}
					builder.append("?");
					params.add(value);
				}
				builder.append(")");
			} else {
				throw new IllegalArgumentException("Filter.Type 非法");
			}
			
		}

		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#orderBy(com.sand.abacus.util.data.sqlbuild.Order[])
	 */
	@Override
	public Builder orderBy(Order... orders) {
		if(orders == null || orders.length == 0){
			throw new IllegalArgumentException("orders参数不能为null或长度等于0");
		}
		builder.append(" order by ");
		for (Order order : orders) {
			if(isFirstSetOrders){
				isFirstSetOrders = false;
			} else {
				dot();
			}
			builder.append(order.getField().getFullName()).append(" ");
			if(order.getType() == Order.Type.asc){
				builder.append("asc");
			} else if(order.getType() == Order.Type.desc){
				builder.append("desc");
			}
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#groupBy(com.sand.abacus.util.data.sqlbuild.Field[])
	 */
	@Override
	public Builder groupBy(Field<?>... fields) {
		if(fields == null || fields.length == 0){
			throw new IllegalArgumentException("fields参数不能为null或长度等于0");
		}
		builder.append(" group by ");
		for (Field<?> field : fields) {
			if(isFirstSetGroups){
				isFirstSetGroups = false;
			} else {
				dot();
			}
			builder.append(field.getFullName());
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#limit(int)
	 */
	@Override
	public Builder limit(int limit) {
		builder.append(" limit = ").append(limit);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#top(int)
	 */
	@Override
	public Builder top(int top) {
		builder.append(" top ").append(top);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#rownum(int)
	 */
	@Override
	public Builder rownum(int rownum) {
		builder.append(" rownum <= ").append(rownum);
		return this;
	}

	
	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#func(java.lang.String)
	 */
	@Override
	public Builder func(String funName) {
		builder.append(" ").append(funName).append(" ");
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#ls()
	 */
	@Override
	public Builder ls() {
		builder.append("(");
		return this;
	}
	

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#args(com.sand.abacus.util.data.sqlbuild.Field[])
	 */
	@Override
	public Builder args(Field<?>... fields) {
		if(fields == null || fields.length == 0){
			throw new IllegalArgumentException("fields参数不能为null或长度等于0");
		}
		for (Field<?> field : fields) {
			if(isFirstSetArgs){
				isFirstSetArgs = false;
			} else {
				dot();
			}
			builder.append(field.getFullName());
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#rs()
	 */
	@Override
	public Builder rs() {
		builder.append(")");
		return this;
	}
	

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#dot()
	 */
	@Override
	public Builder dot() {
		builder.append(",");
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#as(com.sand.abacus.util.data.sqlbuild.Field)
	 */
	@Override
	public Builder as(Field<?> field) {
		builder.append(" as ").append(field.getAsName());
		if(!isUnionFlag){
			selectFields.add(field);
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#as(com.sand.abacus.util.data.sqlbuild.Field, java.lang.Class)
	 */
	@Override
	public Builder as(Field<?> field, Class<?> javaType) {
		Field<?> fd = FieldFactory.as(field, javaType);
		return as(fd);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#forUpdate()
	 */
	@Override
	public Builder forUpdate() {
		builder.append(" for update ");
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#waits(int)
	 */
	@Override
	public Builder waits(int second) {
		builder.append(" wait ").append(second);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#union()
	 */
	@Override
	public Builder union() {
		builder.append(" union ");
		unionInit();
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#unionAll()
	 */
	@Override
	public Builder unionAll() {
		builder.append(" union all ");
		unionInit();
		return this;
	}
	
	private void unionInit(){
		isFirstSetFields = true;
		isFirstSetSetters = true;
		isFirstSetValues = true;
		isFirstSetOrders = true;
		isFirstSetGroups = true;
		isFirstSetArgs = true;
		
		isUnionFlag = true;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Builder#build()
	 */
	@Override
	public BuildResult build() {
		BuildResult result = new BuildResultImpl(builder, params, selectFields);
		//重新初始化
		init();
		return result;
	}
	
	public enum Type{
		select,
		selectAll,
		insert,
		insertAll,
		update,
		delete,
	}
	
	public enum Join{
		innerJoin,
		leftJoin,
		rightJoin,
	}
	
}
