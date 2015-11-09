package com.sand.sqlbuild.dao.springjdbc;

import com.sand.sqlbuild.builder.*;
import com.sand.sqlbuild.builder.impl.BuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author : sun.mt
 * @create : 2015/9/22 17:50
 * @since : 0.1.1
 */
public class BaseDaoImpl implements BaseDao{

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseDaoImpl.class);

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate () {
		return jdbcTemplate;
	}

	public int insert(BuildResult buildResult) {
		String sql = buildResult.getSql();
		Object[] params = buildResult.getParameters().toArray();
		try {
			return this.getJdbcTemplate().update(sql, params);
		} catch (DataAccessException e){
			LOGGER.error("data access exceptions parameters : {}", Arrays.toString(params));
			throw e;
		}
	}

	public int update(BuildResult buildResult) {
		String sql = buildResult.getSql();
		Object[] params = buildResult.getParameters().toArray();
		try {
			return this.getJdbcTemplate().update(sql, params);
		} catch (DataAccessException e){
			LOGGER.error("data access exceptions parameters : {}", Arrays.toString(params));
			throw e;
		}
	}

	public int delete(BuildResult buildResult) {
		String sql = buildResult.getSql();
		Object[] params = buildResult.getParameters().toArray();
		try {
			return this.getJdbcTemplate().update(sql, params);
		} catch (DataAccessException e){
			LOGGER.error("data access exceptions parameters : {}", Arrays.toString(params));
			throw e;
		}
	}

	public <R extends AbstractPo> R queryForPo (final BuildResult buildResult, final R po) {
		String sql = buildResult.getSql();
		Object[] params = buildResult.getParameters().toArray();

		try {
			return this.getJdbcTemplate().queryForObject(sql, params, new RowMapper<R>() {

				public R mapRow (ResultSet rs, int rowNum) throws SQLException {
					jdbcTypeToJavaType(rs, buildResult.getSelectFields(), po);
					return po;
				}

			});
		} catch (EmptyResultDataAccessException e) {
			return po;
		} catch (DataAccessException e){
			LOGGER.error("data access exceptions parameters : {}", Arrays.toString(params));
			throw e;
		}
	}

	public <R> R queryFirstColumnForObject (BuildResult buildResult, final Class<R> clazz) {
		String sql = buildResult.getSql();
		Object[] params = buildResult.getParameters().toArray();

		try {
			return this.getJdbcTemplate().queryForObject(sql, params, new RowMapper<R>() {

				public R mapRow (ResultSet rs, int rowNum) throws SQLException {
					return rs.getObject(1, clazz);
				}

			});

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (DataAccessException e){
			LOGGER.error("data access exceptions parameters : {}", Arrays.toString(params));
			throw e;
		}
	}

	public <R extends AbstractPo> List<R> queryForPoList (final BuildResult buildResult, final Class<R> clazz) {
		String sql = buildResult.getSql();
		Object[] params = buildResult.getParameters().toArray();

		try {
			return this.getJdbcTemplate().query(sql, params, new RowMapper<R>() {

				public R mapRow (ResultSet rs, int rowNum) throws SQLException {
					try {
						R po = clazz.newInstance();

						jdbcTypeToJavaType(rs, buildResult.getSelectFields(), po);
						return po;
					} catch (InstantiationException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				}

			});
		} catch (DataAccessException e) {
			LOGGER.error("data access exceptions parameters : {}", Arrays.toString(params));
			throw  e;
		}

	}

	private <R extends AbstractPo> void jdbcTypeToJavaType(ResultSet rs, List<Field<?>> fields, R po) throws SQLException{

		int index = 1;
		for (Field<?> field : fields) {
			//FIXME 此语法仅在Java 1.7和ojdbc7及以上版本支持
			po.setValueWithoutTypecheck(field, rs.getObject(index, field.getJavaType()));
			index ++;
			/*if(field.getJavaType().equals(Integer.class)){
				po.setValue(field, rs.getBigDecimal(field.getName()).intValue());
			} else if(field.getJavaType().equals(String.class)) {
				po.setValue(field, rs.getString(field.getName()));
			}*/
		}
	}

	public <R extends AbstractPo> int insert(Setter<?>[] setters, Class<R> clazz) {

		Field<?>[] fields = new Field<?>[setters.length];
		Object[] values = new Object[setters.length];

		for (int i = 0; i < setters.length; i++) {
			fields[i] = setters[i].getField();
			values[i] = setters[i].getValue();
		}

		Builder builder = BuilderFactory.create();

		builder.insert(clazz, fields)
				.values(values);

		return insert(builder.build());
	}

	public <R extends AbstractPo> int update(Setter<?>[] setters, Filter<?>[] filters, Class<R> clazz) {

		Builder builder = BuilderFactory.create();

		builder.update(clazz).set(setters);

		BuilderUtils.joinFiltersByAnd(builder, filters);

		return update(builder.build());
	}

	public <R extends AbstractPo> int update(Setter<?>[] setters, FilterBuilder filterBuilder, Class<R> clazz) {

		Builder builder = BuilderFactory.create();

		builder.update(clazz).set(setters).where(filterBuilder);


		return update(builder.build());
	}

	public <R extends AbstractPo> int upsertOracle (Setter<?>[] setters, Filter<?>[] filters, Class<R> clazz) {

		// 使用 oracle merge into 语法实现 upsert
		String tableName = newInstance(clazz).getName();

		// 为了去 setters 和 filters 中重复字段
		Map<String, Field<?>> insertFieldMap = new HashMap<String, Field<?>>(setters.length + filters.length);
		List<Object> params = new ArrayList<Object>(setters.length + filters.length);

		StringBuilder incomingSql = new StringBuilder("(select ");
		for(int i = 0; i < filters.length; i ++){
			if(i != 0){
				incomingSql.append(", ");
			}
			Field<?> field = filters[i].getField();
			incomingSql.append("? ").append(field.getName());
			insertFieldMap.put(field.getName(), field);
			params.add(filters[i].getValue());
		}
		for(int i = 0; i < setters.length; i ++){
			Field<?> field = setters[i].getField();
			incomingSql.append(", ? ").append(field.getName());
			insertFieldMap.put(field.getName(), field);
			params.add(setters[i].getValue());
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
					.append(" incoming.").append(field.getName());
		}
		filterSql.append(")");

		StringBuilder updateSql = new StringBuilder("update set ");
		for(int i = 0; i < setters.length; i ++){
			if(i != 0){
				updateSql.append(", ");
			}
			Field<?> field = setters[i].getField();
			updateSql.append(tableName).append(".").append(field.getName()).append(" ")
					.append(" = incoming.").append(field.getName());
		}

		StringBuilder insertSql = new StringBuilder("insert (");
		StringBuilder valusSql = new StringBuilder("values (");

		Iterator<Field<?>> fieldsIt = insertFieldMap.values().iterator();
		boolean isFirst = true;
		while (fieldsIt.hasNext()){
			Field<?> field = fieldsIt.next();
			if(isFirst){
				isFirst = false;
			} else {
				insertSql.append(", ");
				valusSql.append(", ");
			}
			insertSql.append(tableName).append(".").append(field.getName());
			valusSql.append("incoming.").append(field.getName());
		}
		insertSql.append(")");
		valusSql.append(")");

		StringBuilder mergeSql = new StringBuilder("merge into ").append(tableName).append(" using ").append(incomingSql)
				.append(" on ").append(filterSql).append(" when matched then ").append(updateSql)
				.append(" when not matched then ").append(insertSql).append(" ").append(valusSql);

		return getJdbcTemplate().update(mergeSql.toString(), params.toArray());
	}

	public <R extends AbstractPo> int delete(Filter<?>[] filters, Class<R> clazz) {
		Builder builder = BuilderFactory.create();

		builder.delete().from(clazz);

		joinFiltersByAnd(builder, filters);

		return delete(builder.build());
	}

	public <R extends AbstractPo> int delete(FilterBuilder filterBuilder, Class<R> clazz) {
		Builder builder = BuilderFactory.create();

		builder.delete().from(clazz).where(filterBuilder);

		return delete(builder.build());
	}

	public <R extends AbstractPo> R queryForPo (Field<?>[] fields, Filter<?>[] filters, Class<R> clazz) {
		Builder builder = BuilderFactory.create()

				.select(fields)
				.from(clazz);

		joinFiltersByAnd(builder, filters);

		return queryForPo(builder.build(), newInstance(clazz));
	}

	public <R extends AbstractPo> R queryForPo (Field<?>[] fields, FilterBuilder filterBuilder, Class<R> clazz) {
		Builder builder = BuilderFactory.create()

				.select(fields)
				.from(clazz)
				.where(filterBuilder);

		return queryForPo(builder.build(), newInstance(clazz));
	}

	public <R extends AbstractPo> List<R> queryForPoList (Field<?>[] fields, Filter<?>[] filters, Class<R> clazz) {
		Builder builder = BuilderFactory.create()

				.select(fields)
				.from(clazz);

		joinFiltersByAnd(builder, filters);

		return queryForPoList(builder.build(), clazz);
	}

	public <R extends AbstractPo> List<R> queryForPoList (Field<?>[] fields, FilterBuilder filterBuilder, Class<R> clazz) {
		Builder builder = BuilderFactory.create()

				.select(fields)
				.from(clazz)
				.where(filterBuilder);

		return queryForPoList(builder.build(), clazz);
	}

	private void joinFiltersByAnd(Builder builder, Filter<?>[] filters){
		for(int i = 0; i < filters.length; i++){
			if(i == 0) {
				builder.where(filters[i]);
			} else {
				builder.and(filters[i]);
			}
		}
	}

	private <R extends AbstractPo> R newInstance(Class<R> clazz){
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
