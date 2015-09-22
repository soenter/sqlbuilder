package com.sand.sqlbuild;

import java.util.List;

/**
 *
 * @author : sun.mt
 * @date : 2015年5月7日 上午9:47:39
 * @since 1.0.0
 *
 */
public interface BuildResult {

	public String getSql();
	
	public List<Object> getParameters();
	
	public List<Field<?>> getSelectFields();
}
