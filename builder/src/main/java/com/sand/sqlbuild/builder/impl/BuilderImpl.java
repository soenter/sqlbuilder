package com.sand.sqlbuild.builder.impl;

import com.sand.sqlbuild.builder.*;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author : sun.mt
 * @date : 2015年5月6日 上午11:11:34
 * @since 1.0.0
 *
 */
public class BuilderImpl implements Builder {

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
	boolean isUnionFlag;
	boolean hasSetKey;
	
	
	//private Type type;
	
	//private Join join;
	
	BuilderImpl(){
		init();
	}

	public Builder reinit(){
		init();
		return this;
	}
	
	/*------------------------------------查询（select）------------------------------------*/
	
	public Builder select() {
		//type = Type.select;
		builder.append("select ");
		return this;
	}
	
	public Builder select(Field<?>... fields) {
		select();
		fields(fields);
		return this;
	}

	public Builder fields(Field<?>... fields) {
		if(fields == null || (fieldCount = fields.length) == 0){
			throw new IllegalArgumentException("fields参数不能为null或长度等于0");
		}
		for (Field<?> field : fields) {
			if(isFirstSetFields){
				isFirstSetFields = false;
			} else {
				comma();
			}
			builder.append(field.getFullName());
			if(!isUnionFlag){
				selectFields.add(field);
			}
		}
		return this;
	}

	public Builder distinct() {
		builder.append(" distinct ");
		return this;
	}

	/*------------------------------------插入（insert）------------------------------------*/


	public Builder insert(Class<? extends Table> clazz, Field<?>... fields) {
		//type = Type.insert;
		Table table = BuilderUtils.newTable(clazz);
		builder.append("insert into ").append(table.getName()).append("(");
		fields(fields);
		return this;
	}

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
				comma();
			}
			builder.append("?");
			valueCount ++;
			params.add(object);
		}
		builder.append(")");
		
		
		return this;
	}


	/*------------------------------------更新（update）------------------------------------*/


	public Builder update(Class<? extends Table> clazz) {
		//type = Type.update;
		Table table = BuilderUtils.newTable(clazz);
		builder.append("update ").append(table.getName());
		return this;
	}


	public Builder set(Setter<?>... setters) {
		if(setters == null || setters.length == 0){
			throw new IllegalArgumentException("values参数不能为null或长度等于0");
		}

		appendSetKey();
		for (Setter<?> setter : setters) {
			if(isFirstSetSetters){
				isFirstSetSetters = false;
			} else {
				comma();
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

	public Builder set (Field<?> field) {
		appendSetKey();
		builder.append(field.getFullName());
		return this;
	}

	/*------------------------------------删除（update）------------------------------------*/
	
	
	public Builder delete() {
		builder.append("delete ");
		return this;
	}

	/*------------------------------------表连接（join）------------------------------------*/

	public Builder join(Class<? extends Table> clazz) {
		//FIXME 获取表名，改为注解形式
		Table table = BuilderUtils.newTable(clazz);
		builder.append(" inner join ").append(table.getName());
		return this;
	}

	public Builder innerJoin(Class<? extends Table> clazz) {
		return join(clazz);
	}

	public Builder leftJoin(Class<? extends Table> clazz) {
		//TODO 获取表名，改为注解形式
		Table table = BuilderUtils.newTable(clazz);
		builder.append(" left join ").append(table.getName());
		return this;
	}

	public Builder rightJoin(Class<? extends Table> clazz) {
		//TODO 获取表名，改为注解形式
		Table table = BuilderUtils.newTable(clazz);
		builder.append(" right join ").append(table.getName());
		return this;
	}
	
	/*------------------------------------通用（common）------------------------------------*/


	public Builder from(Class<? extends Table> clazz) {
		//FIXME 获取表名，改为注解形式
		Table table = BuilderUtils.newTable(clazz);
		builder.append(" from ").append(table.getName());
		return this;
	}

    public Builder from(Class<? extends Table> first, Class<? extends Table>... clazzs) {
        Table table = BuilderUtils.newTable(first);
        builder.append(" from ").append(table.getName());
        for (Class<? extends Table> class1 : clazzs) {
            //FIXME 获取表名，改为注解形式
            comma();
            table = BuilderUtils.newTable(class1);
            builder.append(table.getName());
        }
        return this;
    }

	public Builder from(Class<? extends Table>[] clazzs) {
		builder.append(" from ");
		boolean isFirst = true;
		for (Class<? extends Table> class1 : clazzs) {
			//FIXME 获取表名，改为注解形式
			Table table = BuilderUtils.newTable(class1);
			if(isFirst){
				isFirst = false;
			} else {
				comma();
			}
			builder.append(table.getName());
		}
		return this;
	}

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


	public Builder where(Filter<?> filter) {
		builder.append(" where ");
		return filter(filter);
	}

	public Builder where(FilterBuilder filterBuilder) {
		FilterBuildResult fbr = filterBuilder.build();
		builder.append(" where ").append(fbr.getSql());
		params.addAll(fbr.getParameters());
		return this;
	}

	public Builder and() {
		builder.append(" and ");
		return this;
	}

	public Builder and(Filter<?> filter) {
		and();
		return filter(filter);
	}

	public Builder and(FilterBuilder filterBuilder) {
		FilterBuildResult fbr = filterBuilder.build();
		builder.append(" and ").append(fbr.getSql());
		params.addAll(fbr.getParameters());
		return this;
	}

	public Builder or(Filter<?> filter) {
		builder.append(" or ");
		return filter(filter);
	}
	
	public Builder or(FilterBuilder filterBuilder) {
		FilterBuildResult fbr = filterBuilder.build();
		builder.append(" or ").append(fbr.getSql());
		params.addAll(fbr.getParameters());
		return this;
	}

	public Builder orderBy(Order... orders) {
		if(orders == null || orders.length == 0){
			throw new IllegalArgumentException("orders参数不能为null或长度等于0");
		}
		builder.append(" order by ");
		for (Order order : orders) {
			if(isFirstSetOrders){
				isFirstSetOrders = false;
			} else {
				comma();
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

	public Builder groupBy(Field<?>... fields) {
		if(fields == null || fields.length == 0){
			throw new IllegalArgumentException("fields参数不能为null或长度等于0");
		}
		builder.append(" group by ");
		for (Field<?> field : fields) {
			if(isFirstSetGroups){
				isFirstSetGroups = false;
			} else {
				comma();
			}
			builder.append(field.getFullName());
		}
		return this;
	}

	public Builder limit(int limit) {
		builder.append(" limit = ").append(limit);
		return this;
	}

	public Builder top(int top) {
		builder.append(" top ").append(top);
		return this;
	}

	public Builder rownum(int rownum) {
		builder.append(" rownum <= ").append(rownum);
		return this;
	}

	
	public Builder func(String funName) {
		builder.append(" ").append(funName).append(" ");
		return this;
	}

	public Builder ls() {
		builder.append("(");
		return this;
	}
	

	public Builder args(Field<?>... fields) {
		if(fields == null || fields.length == 0){
			throw new IllegalArgumentException("fields参数不能为null或长度等于0");
		}
		boolean isFirstSetArgs = true;
		for (Field<?> field : fields) {
			if(isFirstSetArgs){
				isFirstSetArgs = false;
			} else {
				comma();
			}
			builder.append(field.getFullName());
		}
		return this;
	}

	public Builder rs() {
		builder.append(")");
		return this;
	}

	public Builder dot () {
		builder.append(".");
		return this;
	}

	public Builder comma () {
		builder.append(",");
		return this;
	}

	public Builder as(Field<?> field) {
		builder.append(" as ").append(field.getAsName());
		if(!isUnionFlag){
			selectFields.add(field);
		}
		return this;
	}


    public Builder as(String aliasName, Class<?> javaType) {
		Field<?> asField = FieldFactory.create(aliasName, javaType);
        return as(asField);
    }

	public Builder as(Field<?> field, Class<?> javaType) {
		Field<?> fd = FieldFactory.as(field, javaType);
		return as(fd);
	}

	public Builder forUpdate() {
		builder.append(" for update ");
		return this;
	}

	public Builder waits(int second) {
		builder.append(" wait ").append(second);
		return this;
	}

	public Builder union() {
		builder.append(" union ");
		unionInit();
		return this;
	}

	public Builder unionAll() {
		builder.append(" union all ");
		unionInit();
		return this;
	}

	public Builder cases () {
		builder.append(" case ");
		return this;
	}

	public Builder cases (Field<?> field) {
		cases();
		builder.append(field.getFullName());
		return this;
	}

	public Builder when (Object value) {
		builder.append(" when ").append("? ");
		params.add(value);
		return this;
	}

	public Builder when (Filter<?> filter) {
		builder.append(" when ");
		return filter(filter);
	}

	public Builder then (Object value) {
		builder.append(" then ?");
		params.add(value);
		return this;
	}

	public Builder then (Field<?> field) {
		builder.append(" then ").append(field.getFullName());
		return this;
	}

	public Builder elses (Object value) {
		builder.append(" else ?");
		params.add(value);
		return this;
	}

	public Builder elses (Field<?> field) {
		builder.append(" else ").append(field.getFullName());
		return this;
	}

	public Builder end () {
		builder.append(" end ");
		return this;
	}

	public Builder eq () {
		builder.append(" = ");
		return this;
	}

	public BuildResult build() {
		BuildResult result = new BuildResultImpl(builder, params, selectFields);
		//重新初始化
		//init();
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

	// ========================================= private =========================================

	private void
	init(){
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
		isUnionFlag = false;
		hasSetKey = false;
	}

	private void unionInit(){
		isFirstSetFields = true;
		isFirstSetSetters = true;
		isFirstSetValues = true;
		isFirstSetOrders = true;
		isFirstSetGroups = true;

		isUnionFlag = true;

		hasSetKey = false;
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
			if(filter.getType() == Filter.Type.ONE){
				if(null != filter.getValue()){
					builder.append(operator).append(" ").append("?");
					params.add(filter.getValue());
				} else {
					builder.append(operator).append(" ");
				}
			} else if(filter.getType() == Filter.Type.TWIN){
				if(filter.getValues() == null)
					throw new IllegalArgumentException("Filter.Type.TWIN时 值对象数组长度必须为2");

				builder.append(operator).append(" ");

				params.add(filter.getValues()[0]);
				params.add(filter.getValues()[1]);

			} else if(filter.getType() == Filter.Type.MULTI){
				if(filter.getValues() == null)
					throw new IllegalArgumentException("Filter.Type.MULTI时值对象数组不能为null");

				boolean isFirst = true;
				builder.append(operator).append(" (");
				for (Object value : filter.getValues()) {
					if(isFirst){
						isFirst = false;
					} else {
						comma();
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

	private void appendSetKey(){
		if(!hasSetKey){
			hasSetKey = true;
			builder.append(" set ");
		}
	}
}
