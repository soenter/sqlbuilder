package com.sand.sqlbuild.dao.springjdbc.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sand.sqlbuild.dao.springjdbc.AbstractPo;
import com.sand.sqlbuild.dao.springjdbc.po.PersonPo;
import org.junit.Test;

/**
 * @author : sun.mt
 * @create : 15-12-15 下午4:30
 * @since : 0.2.0
 */
public class GsonTest {

	@Test
	public void test_gson_adapter(){
		final GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeHierarchyAdapter(AbstractPo.class, new PoAdapter());
		final Gson gson = gsonBuilder.create();


		PersonPo po = new PersonPo();

		po.setValue(PersonPo.name, "tom");
		po.setValue(PersonPo.email, "13800000000@xx.com");
		po.setValue(PersonPo.phone, "13800000000");
		po.setValue(PersonPo.age, 22);


		System.out.println(gson.toJson(po));


	}
}
