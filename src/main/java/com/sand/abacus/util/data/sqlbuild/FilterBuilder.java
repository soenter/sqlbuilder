/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月8日 上午11:21:59
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月8日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild;

/**
 *
 * @ClassName ：FilterGroup
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年5月8日 上午11:21:59
 * @version 1.0.0
 *
 */
public interface FilterBuilder {

	/**
	 * 左边括号"("
	 * 
	 * <br>ls = left sign
	 * @return
	 */
	public FilterBuilder ls();
	
	/**
	 * 右边括号")"
	 * 
	 * <br>rs = left sign
	 * 
	 * @return
	 */
	public FilterBuilder rs();
	
	/**
	 * 左右边括号 "("  ")"
	 * 
	 * <br>lrs = left right sign
	 * 
	 * @return
	 */
	public FilterBuilder lrs(FilterBuilder filterBuilder);
	
	/**
	 * 分组里边嵌套分组
	 * 
	 * @param filterBuilder 嵌套分组
	 * @return
	 */
	public FilterBuilder add(FilterBuilder filterBuilder);

	/**
	 * 分组里边套条件
	 * 
	 * @param filter 条件
	 * @return
	 */
	public FilterBuilder add(Filter<?> filter);
	
	/**
	 * 逻辑连接“且”
	 * 
	 * @return
	 */
	public FilterBuilder and();
	
	/**
	 * 逻辑连接“或”
	 * 
	 * @return
	 */
	public FilterBuilder or();
	
	/**
	 * 限制查询记录数, Mysql 语法
	 * 
	 * @param limit
	 * @return
	 */
	public FilterBuilder limit(int limit);
	
	/**
	 * 限制查询记录数, Oracle 语法
	 * 
	 * @param rownum
	 * @return
	 */
	public FilterBuilder rownum(int rownum);
	

	/**
	 * 构建返回结果并重新初始化builder
	 * 
	 * @return
	 */
	public FilterBuildResult build();
}
