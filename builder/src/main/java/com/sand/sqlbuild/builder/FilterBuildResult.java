package com.sand.sqlbuild.builder;

import java.util.List;

/**
 *
 * @author : sun.mt
 * @date : 2015年5月8日 下午12:52:14
 * @since 1.0.0
 *
 */
public interface FilterBuildResult {

	String getSql ();
	
	List<Object> getParameters ();
}
