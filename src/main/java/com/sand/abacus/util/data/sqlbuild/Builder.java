/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-dao-base
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月6日 下午12:28:52
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月6日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild;



/**
 *
 * @ClassName ：Builder
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年5月6日 下午12:28:52
 * @version 1.0.0
 *
 */
public interface Builder {
	
	/*------------------------------------查询（select）------------------------------------*/
	
	public Builder select();
	
	public Builder select(Field<?>... fields);
	
	public Builder distinct();

	public Builder fields(Field<?>... fields);
	
	//public Builder all();

	/*------------------------------------插入（insert）------------------------------------*/

	public Builder insert(Class<? extends Table> clazz, Field<?>... fields);
	
	public Builder values(Object... values);

	/*------------------------------------更新（update）------------------------------------*/

	public Builder update(Class<? extends Table> clazz);

	public Builder set(Setter<?>... setters);

	/*------------------------------------删除（update）------------------------------------*/

	public Builder delete();

	/*------------------------------------表连接（join）------------------------------------*/

	/**
	 * 内连接（inner join）
	 * 
	 * @param clazz
	 * @return
	 */
	public Builder join(Class<? extends Table> clazz);
	
	/**
	 * 内连接（inner join）
	 * 
	 * @param clazz
	 * @return
	 */
	public Builder innerJoin(Class<? extends Table> clazz);
	public Builder leftJoin(Class<? extends Table> clazz);
	public Builder rightJoin(Class<? extends Table> clazz);
	public Builder on(Filter<?> filter);

	/*------------------------------------通用（common）------------------------------------*/
	
	public Builder from(Class<? extends Table> clazz);

	/**
	 * 多表查询（select * from table_1, table_2）
	 * 
	 * @param clazz
	 * @return
	 */
	public Builder from(Class<? extends Table>[] clazzs);
	
	public Builder where(Filter<?> filter);
	
	public Builder where(FilterBuilder filterBuilder);
	
	public Builder and();
	
	public Builder and(Filter<?> filter);
	
	public Builder and(FilterBuilder filterBuilder);
	
	public Builder or(Filter<?> filter);
	
	public Builder or(FilterBuilder filterBuilder);
	
	public Builder orderBy(Order... orders);
	
	public Builder groupBy(Field<?>... fields);

	/**
	 * Mysql 语法
	 * 
	 * @param limit
	 * @return
	 */
	public Builder limit(int limit);
	
	/**
	 * SQL Server 语法
	 * 
	 * @param top
	 * @return
	 */
	public Builder top(int top);
	
	/**
	 * Oracle 语法
	 * 
	 * @param rownum
	 * @return
	 */
	public Builder rownum(int rownum);
	

	/**
	 * 函数
	 * 
	 * @param funName
	 * @return
	 */
	public Builder func(String funName);

	/**
	 * 左边括号"("
	 * 
	 * <br>ls = left sign
	 * @return
	 */
	public Builder ls();
	
	/**
	 * 函数参数
	 * 
	 * @param fields
	 * @return
	 */
	public Builder args(Field<?>... fields);
	
	/**
	 * 右边括号")"
	 * 
	 * <br>rs = left sign
	 * 
	 * @return
	 */
	public Builder rs();
	
	/**
	 * 逗号 “,”
	 * 
	 * @return
	 */
	public Builder dot();
	
	/**
	 * 别名
	 * 
	 * @param field
	 * @return
	 */
	public Builder as(Field<?> field);
	
	public Builder as(Field<?> field, Class<?> javaType);

	public Builder forUpdate();
	
	/**
	 * wait 子句指定等待其他用户释放锁的秒数，防止无限期的等待
	 * 
	 * <br>为了避免与Object.wait重名后边加一个s后缀
	 * 
	 * @param second
	 * @return
	 */
	public Builder waits(int second);
	
	public Builder union();

	public Builder unionAll();

	/**
	 * 构建返回结果并重新初始化builder
	 * 
	 * @return
	 */
	public BuildResult build();
	
}
