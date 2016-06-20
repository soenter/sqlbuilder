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
public class BuilderImpl <T extends Builder> implements Builder<T> {

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

	public T reinit(){
		init();
		return (T)this;
	}
	
	/*------------------------------------查询（select）------------------------------------*/
	
	public T select() {
		type = Type.select;
		builder.append("select ");
		return (T)this;
	}
	
	public T select(Fieldable... fields) {
		select();
		fields(fields);
		return (T)this;
	}

	public T fields(Fieldable... fields) {
		if(fields == null || (fieldCount = fields.length) == 0){
			throw new IllegalArgumentException("fields参数不能为null或长度等于0");
		}
		for (Fieldable field : fields) {
			if(isFirstSetFields){
				isFirstSetFields = false;
			} else {
				comma();
			}

			Field realField;
			if(field instanceof Field){
				realField = (Field)field;
				field(realField);
				if(realField.getAlias() != null && realField.getAliasJavaType() != null){
					as(realField.getAlias(), realField.getAliasJavaType());
				} else if(!isUnionFlag) {
					getSelectFields().add(realField);
				}
			} else {
				Jointer jointer = (Jointer)field;
				field(jointer);
				realField = recursionGetLastField(jointer);
				if(!isUnionFlag) {
					getSelectFields().add(realField);
				}
			}
		}
		return (T)this;
	}

	private Field<?> recursionGetLastField(Jointer jointer){
		return FieldUtils.recursionGetLastField(jointer);

	}

	private T field(Fieldable field) {
		if(field instanceof Field){
			Field realField = (Field)field;
			field(realField);
		} else if(field instanceof Jointer){
			Jointer jointer = (Jointer)field;
			field(jointer);
		}
		return (T)this;
	}

	private T field(Field<?> field) {
		builder.append(field.getFullName());
		return (T)this;
	}

	private T field(Jointer jointer) {
		recursionJointer(jointer);
		return (T)this;
	}

	public T distinct() {
		builder.append(" distinct ");
		return (T)this;
	}

	/*------------------------------------插入（insert）------------------------------------*/

	public T insert(Class<? extends Table> clazz, Field<?>... fields) {
		Table table = newTable(clazz);
		return insert(table.getName(), fields);
	}

	public T insert(Table table, Field<?>... fields) {
		return insert(table.getName(), fields);
	}

	private T insert(String name, Field<?>... fields) {
		type = Type.insert;
		builder.append("insert into ").append(name).append("(");
		fields(fields);
		builder.append(")");
		return (T)this;
	}

	public T values(Object... values) {
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


		return (T)this;
	}

	public T insert (Class<? extends Table> clazz, Setter<?>... setters) {
		return insert(newTable(clazz).getName(), setters);
	}

	public T insert (Table table, Setter<?>... setters) {
		return insert(table.getName(), setters);
	}

	public T insert (String tableName, Setter<?>... setters) {

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
			return (T)insert(tableName, fields).values(values);
		}

	}


	private static final String UPSERT_SERTER_PREFIX = "setter_";
	private static final String UPSERT_FILTER_PREFIX = "filter_";

	public T upsertOracle (Class<? extends Table> clazz, Setter<?>[] setters, Filter<?>[] filters) {
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

		return (T)this;
	}

	private T values(int size) {

		builder.append(" values( ");
		for (int i = 0; i <  size; i++) {
			if(i != 0){
				comma();
			}
			builder.append("?");
			valueCount ++;
		}
		builder.append(")");


		return (T)this;
	}
	/*------------------------------------更新（update）------------------------------------*/


	public T update(Class<? extends Table> clazz) {
		type = Type.update;
		Table table = newTable(clazz);
		builder.append("update ").append(table.getName());
		return (T)this;
	}


	public T set(Setter<?>... setters) {
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

				Fieldable fieldValue = setter.getFieldable();
				builder.append(setter.getField().getFullName()).append(" = ");
				precessFieldable(fieldValue);

			} else if(setter.isEmptyValue()){
				builder.append(setter.getField().getFullName()).append(" = ?");
				getEmptyValueFields().add(setter.getField());
			} else {
				builder.append(setter.getField().getFullName()).append(" = ?");
				getParams().add(setter.getValue());
			}
		}
		return (T)this;
	}

	private void recursionJointer (Jointer jointer){

		if(jointer.hasPrev()) {
			recursionJointer(jointer.getPrev());
		}
		if(jointer.hasOperator()){
			builder.append(" ").append(jointer.getOperator());
		}
		Object partner = jointer.getPartner();
		if(partner == null) return;
		if (partner instanceof Field) {
			if(jointer.hasOperator() && "as".equalsIgnoreCase(jointer.getOperator().trim())){
				builder.append(" ").append(((Field) partner).getAsName());
			} else {
				builder.append(" ").append(((Field) partner).getFullName());
			}
		} else if (partner instanceof Filter) {
			filter((Filter) partner);
		} else {
			builder.append(" ?");
			getParams().add(partner);
		}
	}

	private void precessFieldable(Fieldable fieldable){
		if(fieldable instanceof Field){
			builder.append(((Field)fieldable).getFullName());
		} else {
			recursionJointer((Jointer) fieldable);
		}
	}

	/*------------------------------------删除（update）------------------------------------*/


	public T delete() {
		builder.append("delete ");
		return (T)this;
	}

	/*------------------------------------表连接（join）------------------------------------*/

	public T join(Class<? extends Table> clazz) {
		//FIXME 获取表名，改为注解形式
		Table table = newTable(clazz);
		builder.append(" inner join ").append(table.getName());
		return (T)this;
	}

	public T innerJoin(Class<? extends Table> clazz) {
		return join(clazz);
	}

	public T leftJoin(Class<? extends Table> clazz) {
		//TODO 获取表名，改为注解形式
		Table table = newTable(clazz);
		builder.append(" left join ").append(table.getName());
		return (T)this;
	}

	public T rightJoin(Class<? extends Table> clazz) {
		//TODO 获取表名，改为注解形式
		Table table = newTable(clazz);
		builder.append(" right join ").append(table.getName());
		return (T)this;
	}

	/*------------------------------------通用（common）------------------------------------*/


	public T from(Class<? extends Table> clazz) {
		//FIXME 获取表名，改为注解形式
		Table table = newTable(clazz);
		builder.append(" from ").append(table.getName());
		return (T)this;
	}

    public T from(Class<? extends Table> first, Class<? extends Table>... clazzs) {
        Table table = newTable(first);
        builder.append(" from ").append(table.getName());
        for (Class<? extends Table> class1 : clazzs) {
            //FIXME 获取表名，改为注解形式
            comma();
            table = newTable(class1);
            builder.append(table.getName());
        }
        return (T)this;
    }

	public T from(Class<? extends Table>[] clazzs) {
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
		return (T)this;
	}

	public T on(Filter<?> filter) {
		builder.append(" on ").append(filter.getField().getFullName()).append(filter.getOperator());
		if(filter.getFieldable() != null){
			precessFieldable(filter.getFieldable());
		} else {
			builder.append("?");
			getParams().add(filter.getValue());
		}
		return (T)this;
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

	public T where () {
		builder.append(" where ");
		return (T)this;
	}

	public T where (Filter<?> filter) {
		where();
		return filter(filter);
	}

	public T where(FilterBuilder filterBuilder) {
		FilterBuildResult fbr = filterBuilder.build();
		builder.append(" where ").append(fbr.getSql());
		if(fbr.getParameters() != null){
			getParams().addAll(fbr.getParameters());
		}
		return (T)this;
	}

	public T and() {
		builder.append(" and ");
		return (T)this;
	}

	public T and(Filter<?> filter) {
		and();
		return filter(filter);
	}

	public T and(FilterBuilder filterBuilder) {
		FilterBuildResult fbr = filterBuilder.build();
		builder.append(" and ").append(fbr.getSql());
		if(fbr.getParameters() != null){
			getParams().addAll(fbr.getParameters());
		}
		return (T)this;
	}

	public T or (Filter<?> filter) {
		builder.append(" or ");
		return filter(filter);
	}

	public T or(FilterBuilder filterBuilder) {
		FilterBuildResult fbr = filterBuilder.build();
		builder.append(" or ").append(fbr.getSql());
		if(fbr.getParameters() != null){
			getParams().addAll(fbr.getParameters());
		}
		return (T)this;
	}

	private T filter(Filter<?> filter){
		builder.append(filter.getField().getFullName()).append(" ");
		String operator = filter.getOperator();
		if(filter.isFieldValue()){

			builder.append(operator).append(" ");

			precessFieldable(filter.getFieldable());
			//FIXME why ?
//			if(fieldValue.hasOperator()){
//				builder.append(" ").append(fieldValue.getOperator()).append(" ");
//			}
		} else {
			if(filter.getValueType() == ValueType.ONE_VALUE || filter.isEmptyValue()){
				builder.append(operator).append(" ? ");
				if(filter.isEmptyValue()){
					getEmptyValueFields().add(filter.getField());
				} else {
					getParams().add(filter.getValue());
				}
			} else if(filter.getValueType() == ValueType.TWIN_VALUES){
				if(filter.getValues() == null)
					throw new IllegalArgumentException("ValueType.TWIN时 值对象数组长度必须为2");

				builder.append(operator).append(" ");

				getParams().add(filter.getValues()[0]);
				getParams().add(filter.getValues()[1]);

			} else if(filter.getValueType() == ValueType.MULTI_VALUES){
				if(filter.getValues() == null)
					throw new IllegalArgumentException("ValueType.MULTI时值对象数组不能为null");

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
			} else if(filter.getValueType() == ValueType.NO_VALUE){
				builder.append(operator);
			} else {
				throw new IllegalArgumentException("ValueType 非法");
			}

		}

		return (T)this;
	}

	public T orderBy(Order... orders) {
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
		return (T)this;
	}

	public T groupBy(Fieldable... fields) {
		if(fields == null || fields.length == 0){
			throw new IllegalArgumentException("fields参数不能为null或长度等于0");
		}
		builder.append(" group by ");
		for (Fieldable field : fields) {
			if(isFirstSetGroups){
				isFirstSetGroups = false;
			} else {
				comma();
			}
			precessFieldable(field);
		}
		return (T)this;
	}

	public T limit(int limit) {
		builder.append(" limit = ").append(limit);
		return (T)this;
	}

	public T limit (int limitStart, int limitEnd) {
		builder.append(" limit ").append(limitStart).append(", ").append(limitEnd).append(" ");
		return (T)this;
	}

	public T top(int top) {
		builder.append(" top ").append(top);
		return (T)this;
	}

	public T rownum (int rownum) {
		builder.append(" rownum <= ").append(rownum);
		return (T)this;
	}


	public T func(String funName) {
		builder.append(" ").append(funName).append(" ");
		return (T)this;
	}

	public T ls() {
		builder.append("(");
		return (T)this;
	}


	public T args(Fieldable... fields) {
		if(fields == null || fields.length == 0){
			throw new IllegalArgumentException("fields参数不能为null或长度等于0");
		}
		boolean isFirstSetArgs = true;
		for (Fieldable field : fields) {
			if(isFirstSetArgs){
				isFirstSetArgs = false;
			} else {
				comma();
			}
			field(field);
		}
		return (T)this;
	}

	public T rs() {
		builder.append(")");
		return (T)this;
	}

	public T dot () {
		builder.append(".");
		return (T)this;
	}

	public T comma () {
		builder.append(",");
		return (T)this;
	}

	public T as(Field<?> field) {
		builder.append(" as ").append(field.getAsName());
		if(!isUnionFlag){
			getSelectFields().add(field);
		}
		return (T)this;
	}


    public T as(String aliasName, Class<?> javaType) {
	    Field<?> asField = FieldFactory.create(aliasName, javaType);
        return as(asField);
    }

	public T as(Field<?> field, Class<?> javaType) {
		Field<?> fd = FieldFactory.as(field, javaType);
		return as(fd);
	}

	public T forUpdate() {
		builder.append(" for update ");
		return (T)this;
	}

	public T waits(int second) {
		builder.append(" wait ").append(second);
		return (T)this;
	}

	public T union() {
		builder.append(" union ");
		unionInit();
		return (T)this;
	}

	public T unionAll() {
		builder.append(" union all ");
		unionInit();
		return (T)this;
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

	public T ls (Filter<?> filter) {
		ls();
		filter(filter);
		return (T)this;
	}

	public T rs (Filter<?> filter) {
		rs();
		filter(filter);
		return (T)this;
	}

	public T or () {
		builder.append(" or ");
		return (T)this;
	}

	public T cases () {
		builder.append(" case ");
		return (T)this;
	}

	public T cases (Field<?> field) {
		cases();
		return field(field);
	}

	public T when () {
		builder.append(" when ");
		return (T)this;
	}

	public T when (Field<?> field) {
		when();
		return field(field);
	}

	public T when (Object value) {
		when();
		builder.append(" ? ");
		getParams().add(value);
		return (T)this;
	}

	public T when (Filter<?> filter) {
		when();
		return filter(filter);
	}

	public T then () {
		builder.append(" then ");
		return (T)this;
	}

	public T then (Field<?> field) {
		then();
		return field(field);
	}

	public T then (Object value) {
		then();
		builder.append(" ? ");
		getParams().add(value);
		return (T)this;
	}

	public T elses () {
		builder.append(" else ");
		return (T)this;
	}

	public T elses (Field<?> field) {
		elses();
		return field(field);
	}

	public T elses (Object value) {
		elses();
		builder.append(" ? ");
		getParams().add(value);
		return (T)this;
	}

	public T end () {
		builder.append(" end ");
		return (T)this;
	}

	public T count (Field<?> field) {
		builder.append("count(").append(field.getFullName()).append(")");
		return (T)this;
	}

	public T countDistinct (Field<?> field) {
		builder.append("count(distinct ").append(field.getFullName()).append(")");
		return (T)this;
	}

	public T sum (Fieldable field) {
		builder.append("sum(");
		field(field);
		builder.append(")");
		return (T)this;
	}

	public T max (Fieldable field) {
		builder.append("max(");
		field(field);
		builder.append(")");
		return (T)this;
	}

	public T min (Fieldable field) {
		builder.append("min(");
		field(field);
		builder.append(")");
		return (T)this;
	}

	public T avg (Fieldable field) {
		builder.append("avg(");
		field(field);
		builder.append(")");
		return (T)this;
	}

	public T len (Fieldable field) {
		builder.append("len(");
		field(field);
		builder.append(")");
		return (T)this;
	}

	public T mid (Fieldable field, int start, int length) {
		builder.append("min(");
		field(field);
		builder.append(",")
				.append(start).append(",")
				.append(length)
				.append(")");
		return (T)this;
	}

	public T ucase (Fieldable field) {
		builder.append("ucase(");
		field(field);
		builder.append(")");
		return (T)this;
	}

	public T lcase (Fieldable field) {
		builder.append("lcase(");
		field(field);
		builder.append(")");
		return (T)this;
	}

	public T first (Fieldable field) {
		builder.append("first(");
		field(field);
		builder.append(")");
		return (T)this;
	}

	public T last (Fieldable field) {
		builder.append("last(");
		field(field);
		builder.append(")");
		return (T)this;
	}

	public T round (Fieldable field, int decimals) {
		builder.append("round(");
		field(field);
		builder.append(")");
		return (T)this;
	}

	public T format (Fieldable field, String format) {
		builder.append("format(");
		field(field);
		builder.append(",").append(format).append(")");
		return (T)this;
	}

	public T where (Jointer jointer) {
		where();
		recursionJointer(jointer);
		return (T)this;
	}

	public T and (Jointer jointer) {
		and();
		recursionJointer(jointer);
		return (T)this;
	}

	public T or (Jointer jointer) {
		or();
		recursionJointer(jointer);
		return (T)this;
	}

	public T rs (Jointer jointer) {
		rs();
		recursionJointer(jointer);
		return (T)this;
	}

	public T ls (Jointer jointer) {
		ls();
		recursionJointer(jointer);
		return (T)this;
	}

	public T cases (Jointer jointer) {
		cases();
		recursionJointer(jointer);
		return (T)this;
	}

	public T when (Jointer jointer) {
		when();
		recursionJointer(jointer);
		return (T)this;
	}

	public T then (Jointer jointer) {
		then();
		recursionJointer(jointer);
		return (T)this;
	}

	public T elses (Jointer jointer) {
		elses();
		recursionJointer(jointer);
		return (T)this;
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
