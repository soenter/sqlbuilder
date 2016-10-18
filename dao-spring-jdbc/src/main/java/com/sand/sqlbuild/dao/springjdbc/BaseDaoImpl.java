package com.sand.sqlbuild.dao.springjdbc;

import com.sand.sqlbuild.builder.*;
import com.sand.sqlbuild.builder.impl.BuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

/**
 * @author : sun.mt
 * @create : 2015/9/22 17:50
 * @since : 0.1.1
 */
@Repository("com.sand.sqlbuild.dao.springjdbc.BaseDaoImpl")
public class BaseDaoImpl implements BaseDao, InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseDaoImpl.class);

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	private DatabaseType databaseType;

	public JdbcTemplate getJdbcTemplate () {
		return jdbcTemplate;
	}

	public int insert(BuildResult buildResult) {
		String sql = buildResult.getSql();
		Object[] params = BuilderUtils.paramsToArray(buildResult);
		try {
			return this.getJdbcTemplate().update(sql, params);
		} catch (DataAccessException e){
			LOGGER.error("data access exceptions parameters : {}", Arrays.toString(params));
			throw e;
		}
	}

	public int insert (AbstractPo po) {

		if(po == null || po.isEmpty()){
			throw new IllegalArgumentException("po 不能为 null 或空");
		}

		Iterator<Map.Entry<Field<?>, Object>> iterator = po.iterator();
		Setter<?>[] setters = new Setter[po.size()];
		int index = 0;
		while(iterator.hasNext()){
			Map.Entry<Field<?>, Object> entry = iterator.next();
			Field field = entry.getKey();
			setters[index ++] = field.eq(entry.getValue());
		}

		Builder builder = BuilderFactory.create()
				.insert(po, setters);


		return insert(builder.build());
	}

	public int update(BuildResult buildResult) {
		String sql = buildResult.getSql();
		Object[] params = BuilderUtils.paramsToArray(buildResult);
		try {
			return this.getJdbcTemplate().update(sql, params);
		} catch (DataAccessException e){
			LOGGER.error("data access exceptions parameters : {}", Arrays.toString(params));
			throw e;
		}
	}

	public <R extends AbstractPo> int[] batchUpdate (BuildResult result, List<R> params) {

		checkBatch(result);
		List<Object[]> batchArgs = new ArrayList<Object[]>(params.size());

		List<Field<?>> emptyFiels = result.getEmptyValuesFields();

		for(AbstractPo po: params){
			Object[] objs = new Object[po.size()];

			int index = 0;
			for(Field<?> field: emptyFiels){
				objs[index++] = po.getValue(field);
			}

			batchArgs.add(objs);
		}

		return this.getJdbcTemplate().batchUpdate(result.getSql(), batchArgs);
	}

	public <R extends AbstractPo> int[] batchUpdate (BuildResult result, final BatchPoGetter poGetter) {

		//checkBatch(result);
		final List<Field<?>> emptyFiels = result.getEmptyValuesFields();

		return this.getJdbcTemplate().batchUpdate(result.getSql(), new BatchPreparedStatementSetter(){

			public void setValues (PreparedStatement ps, int i) throws SQLException {
				int index = 1;
				for(Field<?> field: emptyFiels){
					ps.setObject(index++, poGetter.getPo(i).getValue(field));
				}
			}

			public int getBatchSize () {
				return poGetter.getBatchSize();
			}
		});
	}

	private void checkBatch(BuildResult result){
		List<Field<?>> emptyFiels = result.getEmptyValuesFields();

		if(emptyFiels == null || emptyFiels.isEmpty()){
			throw new IllegalArgumentException("批量执行 EmptyValuesFields 不能为null或空");
		}
	}

	public int delete(BuildResult buildResult) {
		String sql = buildResult.getSql();
		Object[] params = BuilderUtils.paramsToArray(buildResult);
		try {
			return this.getJdbcTemplate().update(sql, params);
		} catch (DataAccessException e){
			LOGGER.error("data access exceptions parameters : {}", Arrays.toString(params));
			throw e;
		}
	}

	public <R extends AbstractPo> R queryForPo (final BuildResult buildResult, final R po) {
		String sql = buildResult.getSql();
		Object[] params = BuilderUtils.paramsToArray(buildResult);

		try {
			return this.getJdbcTemplate().queryForObject(sql, params, new RowMapper<R>() {

				public R mapRow (ResultSet rs, int rowNum) throws SQLException {
					jdbcTypeToJavaType(rs, buildResult.getSelectFields(), po);
					po.setEmptyResult(false);
					return po;
				}

			});
		} catch (EmptyResultDataAccessException e) {
			po.setEmptyResult(true);
			return po;
		} catch (DataAccessException e){
			LOGGER.error("data access exceptions parameters : {}", Arrays.toString(params));
			throw e;
		}
	}

	public <R> R queryFirstColumnForObject (BuildResult buildResult, final Class<R> clazz) {
		String sql = buildResult.getSql();
		Object[] params = BuilderUtils.paramsToArray(buildResult);

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
		Object[] params = BuilderUtils.paramsToArray(buildResult);

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

	@Override
	public Map<String,Object> queryForMap(final BuildResult buildResult){
		String sql = buildResult.getSql();
		Object[] params = BuilderUtils.paramsToArray(buildResult);
		try {
			return this.getJdbcTemplate().queryForMap(sql,params);
		} catch (DataAccessException e) {
			LOGGER.error("data access exceptions parameters : {}", Arrays.toString(params));
			throw  e;
		}

	}

	private <R extends AbstractPo> void jdbcTypeToJavaType(ResultSet rs, List<Field<?>> fields, R po) throws SQLException{

		int index = 1;
		for (Field<?> field : fields) {
			//FIXME 此语法仅在Java 1.7和ojdbc7及以上版本支持,mysql-connector-java版本大于等于5.1.21
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

		Builder builder = BuilderFactory.create()
				.upsertOracle(clazz, setters, filters);
		BuildResult result = builder.build();
		return getJdbcTemplate().update(result.getSql(), BuilderUtils.paramsToArray(result));
	}

	public <R extends AbstractPo> int upsert (Setter<?>[] setters, Filter<?>[] filters, Class<R> clazz) {
		if(DatabaseType.ORACLE.equals(databaseType)){
			return upsertOracle(setters, filters, clazz);
		} else {
			//TODO 增加更多数据库upsert方式
			throw new IllegalArgumentException("不支持数据库: [" + databaseType.getProductName() + " ] 做 upsert 操作");
		}
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

	public long queryForPagingCount (PagingBuilder builder) {
		BuildResult result = builder.buildPagingCount();
		return queryFirstColumnForObject(result, Long.class);
	}

	public <R extends AbstractPo> List<R> queryForPagingPoList (PagingBuilder builder, Class<R> clazz, int pagingStart, int pagingEnd, int pagingLimit) {
		BuildResult result = null;
		if(databaseType == DatabaseType.ORACLE){
			result = builder.buildPagingList(pagingStart, pagingEnd, pagingLimit);
		} else if(databaseType == DatabaseType.MYSQL){
			result = builder.buildMySqlPagingList(pagingStart, pagingEnd, pagingLimit);
		} else {
			throw new IllegalArgumentException("Query paging list not support database type [" + databaseType + "]");
		}

		return queryForPoList(result, clazz);
	}
	
	public <R extends AbstractPo> Page<R> queryForPage(PagingBuilder builder, Class<R> clazz,Page<R> page){
		Long count = queryForPagingCount(builder);
		BuildResult result = null;
		if(databaseType == DatabaseType.ORACLE){
			result = builder.buildPagingList(page.getPageStart(), page.getPageEnd(), page.getPageLimit());
		} else if(databaseType == DatabaseType.MYSQL){
			result = builder.buildMySqlPagingList(page.getPageStart(), page.getPageEnd(), page.getPageLimit());
		} else {
			throw new IllegalArgumentException("Query paging list not support database type [" + databaseType + "]");
		}
		page.setTotalCount(count);
		page.setResult(queryForPoList(result, clazz));
		return page;
	}

	public void afterPropertiesSet () throws Exception {

		Connection connection = jdbcTemplate.getDataSource().getConnection();
		try {
			DatabaseMetaData metaData = connection.getMetaData();
			databaseType = DatabaseType.fromProductName(metaData.getDatabaseProductName());
		} finally {
			DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource());
		}
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
