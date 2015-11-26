package com.sand.sqlbuild.builder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : sun.mt
 * @create : 2015/11/26 14:34
 * @since : 0.2.0
 */
public enum DatabaseType {

	//DERBY("Apache Derby"),
	//DB2("DB2"),
	//DB2ZOS("DB2ZOS"),
	//HSQL("HSQL Database Engine"),
	//SQLSERVER("Microsoft SQL Server"),
	//MYSQL("MySQL"),
	ORACLE("Oracle");
	//POSTGRES("PostgreSQL"),
	//SYBASE("Sybase"),
	//H2("H2");

	private final String productName;

	private DatabaseType(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return productName;
	}

	private static final Map<String, DatabaseType> nameMap;

	static{
		nameMap = new HashMap<String, DatabaseType>(values().length);
		for(DatabaseType type: values()){
			nameMap.put(type.getProductName(), type);
		}
	}


	public static DatabaseType fromProductName(String productName){
		productName = commonDatabaseName(productName);
		if(!nameMap.containsKey(productName)){
			throw new IllegalArgumentException("DatabaseType not found for product name: [" +
					productName + "]");
		}
		else{
			return nameMap.get(productName);
		}
	}

	private static String commonDatabaseName(String source) {
		String name = source;
		if (source != null && source.startsWith("DB2")) {
			name = "DB2";
		}
		else if ("Sybase SQL Server".equals(source) ||
				"Adaptive Server Enterprise".equals(source) ||
				"ASE".equals(source) ||
				"sql server".equalsIgnoreCase(source) ) {
			name = "Sybase";
		}
		return name;
	}
}
