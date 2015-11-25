package com.sand.sqlbuild.builder.impl;

import com.sand.sqlbuild.builder.*;

import java.util.*;


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

	private List<Field<?>> emptyValueFields;
	
	private int fieldCount;
	private int valueCount;
	
	
	boolean isFirstSetFields;
	boolean isFirstSetSetters;
	boolean isFirstSetValues;
	boolean isFirstSetOrders;
	boolean isFirstSetGroups;
	boolean isUnionFlag;

	private Type type;
	
	//private Join join;
	
	BuilderImpl(){
		init();
	}
	
	private void init(){
		builder = new StringBuilder();
		fieldCount = 0;
		valueCount = 0;
		isFirstSetFields = true;
		isFirstSetSetters = true;
		isFirstSetValues = true;
		isFirstSetOrders = true;
		isFirstSetGroups = true;
		isUnionFlag = false;

	}

	public Builder reinit(){
		init();
		return this;
	}
	
	/*------------------------------------查询（select）------------------------------------*/
	
	public Builder select() {
		type = Type.select;
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
				getSelectFields().add(field);
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
		Table table = newTable(clazz);
		return insert(table.getName(), fields);
	}

	public Builder insert(Table table, Field<?>... fields) {
		return insert(table.getName(), fields);
	}

	private Builder insert(String name, Field<?>... fields) {
		type = Type.insert;
		builder.append("insert into ").append(name).append("(");
		fields(fields);
		builder.append(")");
		return this;
	}

	public Builder values(Object... values) {
		if(values == null || (valueCount = values.length) == 0){
			throw new IllegalArgumentException("values参数不能为null或长度等于0");
		}
		
		if(valueCount != fieldCount){
			throw new IllegalArgumentException("values参数长度:" + valueCount + "与fields参数长度:" + fieldCount + "不匹配");
		}
		
		builder.append(" values( ");
		for (Object object : values) {
			if(isFirstSetValues){
				isFirstSetValues =  false;
			} else {
				comma();
			}
			builder.append("?");
			getParams().add(object);
		}
		builder.append(")");
		
		
		return this;
	}

	public Builder insert (Class<? extends Table> clazz, Setter<?>... setters) {
		return insert(newTable(clazz).getName(), setters);
	}

	public Builder insert (Table table, Setter<?>... setters) {
		return insert(table.getName(), setters);
	}

	public Builder insert (String tableName, Setter<?>... setters) {

		Field<?>[] fields = new Field<?>[setters.length];
		Object[] values = new Object[setters.length];

		boolean lastEmptyValue = false;
		for (int i = 0; i < setters.length; i++) {
			fields[i] = setters[i].getField();

			if(i == 0){
				lastEmptyValue = setters[i].isEmptyValue();
			} else if(lastEmptyValue != setters[i].isEmptyValue()){
				throw new IllegalArgumentException("setters 的 isEmptyValue 必须保持一致");
			}
			if(!lastEmptyValue){
				values[i] = setters[i].getValue();
			} else {
				getEmptyValueFields().add(setters[i].getField());
			}
		}

		if (lastEmptyValue){
			return insert(tableName, fields);
		} else {
			return insert(tableName, fields).values(values);
		}

	}


	private static final String UPSERT_SERTER_PREFIX = "setter_";
	private static final String UPSERT_FILTER_PREFIX = "filter_";

	public Builder upsertOracle (Class<? extends Table> clazz, Setter<?>[] setters, Filter<?>[] filters) {
		// 使用 oracle merge into 语法实现 upsert
		String tableName = newTable(clazz).getName();

		// 为了去 setters 和 filters 中重复字段
		Map<Field<?>, String> insertFieldMap = new HashMap<Field<?>, String>(setters.length + filters.length);

		StringBuilder incomingSql = new StringBuilder("(select ");
		for(int i = 0; i < filters.length; i ++){
			if(i != 0){
				incomingSql.append(", ");
			}
			Field<?> field = filters[i].getField();
			String incomingFieldName = UPSERT_FILTER_PREFIX + field.getName();
			incomingSql.append("? ").append(incomingFieldName);
			//只有在filter的operator为“=”时才做insert
			if("=".equals(filters[i].getOperator().trim())){
				insertFieldMap.put(field, incomingFieldName);
			}
			if(!filters[i].isEmptyValue()){
				getParams().add(filters[i].getValue());
			}
		}
		for(int i = 0; i < setters.length; i ++){
			Field<?> field = setters[i].getField();
			String incomingFieldName = UPSERT_SERTER_PREFIX + field.getName();
			incomingSql.append(", ? ").append(incomingFieldName);
			insertFieldMap.put(field, incomingFieldName);
			if(!setters[i].isEmptyValue()){
				getParams().add(setters[i].getValue());
			}
		}
		incomingSql.append(" from dual) incoming");

		StringBuilder filterSql = new StringBuilder("(");
		for(int i = 0; i < filters.length; i ++){
			if(i != 0){
				filterSql.append(" and ");
			}
			Field<?> field = filters[i].getField();
			filterSql.append(tableName).append(".").append(field.getName()).append(" ")
					.append(filters[i].getOperator())
					.append(" incoming.").append(UPSERT_FILTER_PREFIX).append(field.getName());
		}
		filterSql.append(")");

		StringBuilder updateSql = new StringBuilder("update set ");
		for(int i = 0; i < setters.length; i ++){
			if(i != 0){
				updateSql.append(", ");
			}
			Field<?> field = setters[i].getField();
			updateSql.append(tableName).append(".").append(field.getName()).append(" ")
					.append(" = incoming.").append(UPSERT_SERTER_PREFIX).append(field.getName());
		}

		StringBuilder insertSql = new StringBuilder("insert (");
		StringBuilder valusSql = new StringBuilder("values (");

		Iterator<Map.Entry<Field<?>, String>> fieldsIt = insertFieldMap.entrySet().iterator();
		boolean isFirst = true;
		while (fieldsIt.hasNext()){
			Map.Entry<Field<?>, String> entryIt = fieldsIt.next();
			Field<?> field = entryIt.getKey();
			if(isFirst){
				isFirst = false;
			} else {
				insertSql.append(", ");
				valusSql.append(", ");
			}
			insertSql.append(tableName).append(".").append(field.getName());
			valusSql.append("incoming.").append(entryIt.getValue());
		}
		insertSql.append(")");
		valusSql.append(")");

		builder.append("merge into ").append(tableName).append(" using ").append(incomingSql)
				.append(" on ").append(filterSql).append(" when matched then ").append(updateSql)
				.append(" when not matched then ").append(insertSql).append(" ").append(valusSql);

		return this;
	}

	private Builder values(int size) {

		builder.append(" values( ");
		for (int i = 0; i <  size; i++) {
			if(i != 0){
				comma();
			}
			builder.append("?");
			valueCount ++;
		}
		builder.append(")");


		return this;
	}
	/*------------------------------------更新（update）------------------------------------*/


	public Builder update(Class<? extends Table> clazz) {
		type = Type.update;
		Table table = newTable(clazz);
		builder.append("update ").append(table.getName());
		return this;
	}


	public Builder set(Setter<?>... setters) {
		if(setters == null || setters.length == 0){
			throw new IllegalArgumentException("values参数不能为null或长度等于0");
		}
		builder.append(" set ");
		boolean lastEmptyValue = false;
		for (Setter<?> setter : setters) {
			if(isFirstSetSetters){
				isFirstSetSetters = false;
				lastEmptyValue = setter.isEmptyValue();
			} else {
				if(lastEmptyValue != setter.isEmptyValue()){
					throw new IllegalArgumentException("setters 的 isEmptyValue 必须保持一致");
				}
				comma();
			}

			if(setter.isFieldValue()){
				
				Field<?> fieldValue = setter.getFieldValue();
				builder.append(setter.getField().getFullName()).append(" = ").append(fieldValue.getFullName());
				
				if(fieldValue.hasOperator()){
					builder.append(" ").append(fieldValue.getOperator()).append(" ?");
					getParams().add(fieldValue.getOperValue());
				}
				
			} else if(setter.isEmptyValue()){
				builder.append(setter.getField().getFullName()).append(" = ?");
				getEmptyValueFields().add(setter.getField());
			} else {
				builder.append(setter.getField().getFullName()).append(" = ?");
				getParams().add(setter.getValue());
			}
		}
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
		Table table = newTable(clazz);
		builder.append(" inner join ").append(table.getName());
		return this;
	}

	public Builder innerJoin(Class<? extends Table> clazz) {
		return join(clazz);
	}

	public Builder leftJoin(Class<? extends Table> clazz) {
		//TODO 获取表名，改为注解形式
		Table table = newTable(clazz);
		builder.append(" left join ").append(table.getName());
		return this;
	}

	public Builder rightJoin(Class<? extends Table> clazz) {
		//TODO 获取表名，改为注解形式
		Table table = newTable(clazz);
		builder.append(" right join ").append(table.getName());
		return this;
	}
	
	/*------------------------------------通用（common）------------------------------------*/


	public Builder from(Class<? extends Table> clazz) {
		//FIXME 获取表名，改为注解形式
		Table table = newTable(clazz);
		builder.append(" from ").append(table.getName());
		return this;
	}

    public Builder from(Class<? extends Table> first, Class<? extends Table>... clazzs) {
        Table table = newTable(first);
        builder.append(" from ").append(table.getName());
        for (Class<? extends Table> class1 : clazzs) {
            //FIXME 获取表名，改为注解形式
            comma();
            table = newTable(class1);
            builder.append(table.getName());
        }
        return this;
    }

	public Builder from(Class<? extends Table>[] clazzs) {
		builder.append(" from ");
		boolean isFirst = true;
		for (Class<? extends Table> class1 : clazzs) {
			//FIXME 获取表名，改为注解形式
			Table table = newTable(class1);
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
			getParams().add(filter.getValue());
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


	public Builder where (Filter<?> filter) {
		builder.append(" where ");
		return filter(filter);
	}

	public Builder where(FilterBuilder filterBuilder) {
		FilterBuildResult fbr = filterBuilder.build();
		builder.append(" where ").append(fbr.getSql());
		getParams().addAll(fbr.getParameters());
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
		getParams().addAll(fbr.getParameters());
		return this;
	}

	public Builder or (Filter<?> filter) {
		builder.append(" or ");
		return filter(filter);
	}
	
	public Builder or(FilterBuilder filterBuilder) {
		FilterBuildResult fbr = filterBuilder.build();
		builder.append(" or ").append(fbr.getSql());
		getParams().addAll(fbr.getParameters());
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
		} else if(filter.isEmptyValue()){
			builder.append(operator).append(" ? ");
			getEmptyValueFields().add(filter.getField());
		} else {
			if(filter.getType() == Filter.Type.ONE){
				if(null != filter.getValue()){
					builder.append(operator).append(" ? ");
					getParams().add(filter.getValue());
				} else {
					builder.append(operator).append(" ");
				}
			} else if(filter.getType() == Filter.Type.TWIN){
				if(filter.getValues() == null) 
					throw new IllegalArgumentException("Filter.Type.TWIN时 值对象数组长度必须为2");

				builder.append(operator).append(" ");
				
				getParams().add(filter.getValues()[0]);
				getParams().add(filter.getValues()[1]);
				
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
					getParams().add(value);
				}
				builder.append(")");
			} else {
				throw new IllegalArgumentException("Filter.Type 非法");
			}
			
		}

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

	public Builder rownum (int rownum) {
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
			getSelectFields().add(field);
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
	
	private void unionInit(){
		isFirstSetFields = true;
		isFirstSetSetters = true;
		isFirstSetValues = true;
		isFirstSetOrders = true;
		isFirstSetGroups = true;

		isUnionFlag = true;

		type = null;
	}

	public BuildResult build() {

		if(type != null){

			if(type == Type.insert && valueCount == 0){
				values(fieldCount);
			}
		}

		BuildResult result = new BuildResultImpl(builder, params, selectFields, emptyValueFields);
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

	private List<Object> getParams(){
		if(params == null){
			params = new ArrayList<Object>();
		}
		return params;
	}

	private List<Field<?>> getEmptyValueFields(){
		if(emptyValueFields == null){
			emptyValueFields = new ArrayList<Field<?>>();
		}
		return emptyValueFields;
	}

	private List<Field<?>> getSelectFields(){
		if(selectFields == null){
			selectFields = new ArrayList<Field<?>>();
		}
		return selectFields;
	}
}
