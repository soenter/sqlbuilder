package com.sand.sqlbuild.dao.springjdbc;

import com.sand.sqlbuild.builder.Field;
import com.sand.sqlbuild.builder.Table;
import com.sand.sqlbuild.builder.impl.FieldFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author : sun.mt
 * @create : 2015/9/22 17:36
 * @since : 0.1.1
 */
public abstract class AbstractPo implements Table, Iterable<Map.Entry<Field<?>, Object>> {
	private final Map<Field<?>, Object> values;

	private boolean isEmptyResult;

	public AbstractPo(){
		values = new HashMap<Field<?>, Object>();
	}

	public <T> void setValue(Field<T> field, T object){
		values.put(field, object);
	}
	/**
	 * 不强制类型检查
	 *
	 * @param field
	 * @param object
	 */
	public <T> void setValueWithoutTypecheck(Field<?> field, T object){
		values.put(field, object);
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(Field<T> key){
		return (T)values.get(key);
	}

	public <T> T getValue(String aliasName, Class<T> clazz){
		return (T)values.get(FieldFactory.create(aliasName, clazz));
	}

	public Iterator<Map.Entry<Field<?>, Object>> iterator() {
		return values.entrySet().iterator();
	}

	public int size(){
		return values.size();
	}

	public boolean isEmpty(){
		return values.isEmpty();
	}

	public boolean containsField(Field<?> field){
		return values.containsKey(field);
	}

	public boolean isEmptyResult () {
		return isEmptyResult;
	}

	public void setEmptyResult (boolean emptyResult) {
		isEmptyResult = emptyResult;
	}
}
