package com.sand.sqlbuild.builder;

import com.sand.sqlbuild.builder.impl.JointerImpl;

/**
 *
 * @author : sun.mt@sand.com.cn
 * @date : 2015/6/8 14:34
 * @since 1.0.0
 *
 */
public class FieldUtils {

	/**
	 * 根据别名获取全名
	 * @param aliasName
	 * @return
	 */
	public static String getFullNameByAliasName(String aliasName){
		return Table.innerAsTableName + "." + aliasName;
	}

	public static Field<?> recursionGetLastField(Jointer jointer){
		if(jointer.getPartner() instanceof Field){
			return (Field<?>)jointer.getPartner();
		} else {
			if(jointer.hasPrev()){
				return recursionGetLastField(jointer.getPrev());
			}
			return null;
		}

	}

	public static Jointer recursionGetFirstJointer(Jointer jointer){
		if(jointer != null && jointer.hasPrev()){
			return recursionGetFirstJointer(jointer.getPrev());
		}
		return jointer;
	}

	/**
	 * 双链表首尾相接
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public static Jointer joinJointer(Jointer arg1, String operator, Jointer arg2){

		// 双链表首尾相接
		Jointer firstJointer = FieldUtils.recursionGetFirstJointer(arg2);
		((JointerImpl)arg1).setNext(firstJointer);

		((JointerImpl)firstJointer).setOperator(operator);

		((JointerImpl)firstJointer).setPrev(arg1);

		return arg2;
	}

	public static String underlineToHumpStyle(String field){

		if(field == null) return null;

		if(field.indexOf("_") == -1){
			return field.toLowerCase();
		}

		StringBuilder sb = new StringBuilder(field.toLowerCase());

		int index = 0;
		while((index = sb.indexOf("_")) != -1){
			char c = sb.charAt(index + 1);
			sb.replace(index, index + 2, String.valueOf(c).toUpperCase());
		}

		return sb.toString();
	}

	public static String createAsName(String tableTame, String fieldName){
		String name = (tableTame + fieldName).replace("_", "");
		//oracle 列名称长度不能大于30 FIXME 汉字未处理
		if (name.length() > 30) {
			return name.substring(name.length() - 30);
		}
		return name;
	}
}
