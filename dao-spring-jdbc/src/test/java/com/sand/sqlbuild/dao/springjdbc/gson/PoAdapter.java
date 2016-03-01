package com.sand.sqlbuild.dao.springjdbc.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sand.sqlbuild.builder.Field;
import com.sand.sqlbuild.dao.springjdbc.AbstractPo;
import com.sand.sqlbuild.dao.springjdbc.po.PersonPo;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author : sun.mt
 * @create : 15-12-15 下午4:29
 * @since : 0.2.0
 */
public class PoAdapter extends TypeAdapter<AbstractPo> {

	@Override
	public void write (JsonWriter jsonWriter, AbstractPo o) throws IOException {
		jsonWriter.beginObject();

		Iterator<Map.Entry<Field<?>, Object>> it =  o.iterator();
		while (it.hasNext()){
			Map.Entry<Field<?>, Object> entry = it.next();
			Field<?> field = entry.getKey();
			Object value = entry.getValue();

			jsonWriter.name(underlineStypeToHumpStyle(field.getName())).value(value == null?null:value.toString());
		}

		jsonWriter.endObject();
	}

	@Override
	public PersonPo read (JsonReader jsonReader) throws IOException {

		throw new RuntimeException("不支持解析 Json 字符串到 PO 对象");
	}

	public static String underlineStypeToHumpStyle(String field){

		if(field == null || field.indexOf("_") == -1){
			return field;
		}

		StringBuilder sb = new StringBuilder(field.toLowerCase());

		int index = 0;
		while((index = sb.indexOf("_")) != -1){
			char c = sb.charAt(index + 1);
			sb.replace(index, index + 2, String.valueOf(c).toUpperCase());
		}

		return sb.toString();
	}

	public static void  main(String[] args){

		System.out.println(underlineStypeToHumpStyle("INSERT_TIME_uu_yy"));
	}
}
