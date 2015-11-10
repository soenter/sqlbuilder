package com.sand.sqlbuild.builder;

import java.util.List;

/**
 * @author : sun.mt
 * @create : 2015/11/9 16:43
 * @since : 0.1.3
 */
public interface SqlResult {

	String getSql ();

	List<Object> getParameters ();
}
