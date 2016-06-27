package com.sand.sqlbuild.dao.springjdbc;

import com.sand.sqlbuild.dao.springjdbc.po.PersonPo;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.sand.sqlbuild.dao.springjdbc.po.PersonPo.*;



/**
 * @author : sun.mt
 * @create : 16-6-24 下午9:03
 * @since : ${VERSION}
 */
public class PerformanceTest {


	@Test
	public void test_new_po_performance(){


		long start = System.currentTimeMillis();

		for (int i = 0; i < 10000 * 100; i++){
			PersonPo po = new PersonPo();
			po.setValue(name, "tom-"+ i);
			po.setValue(PersonPo.age, 1);
			po.setValue(PersonPo.email, "tom-"+ i + "@x.xx");
			po.setValue(PersonPo.phone, "12500000000");
//
//			Map map = new HashMap();
//			map.put("name", "tom-"+ i);
		}

		long end = System.currentTimeMillis();

		System.out.println("用时：" + (end - start));

	}
	@Test
	public void test_new_hashmap_performance(){


		long start = System.currentTimeMillis();

		for (int i = 0; i < 10000 * 100; i++){
			Map map = new HashMap();
			map.put(name, "tom-"+ i);
			map.put(PersonPo.age, 1);
			map.put(PersonPo.email, "tom-"+ i + "@x.xx");
			map.put(PersonPo.phone, "12500000000");
		}

		long end = System.currentTimeMillis();

		System.out.println("用时：" + (end - start));

	}
	@Test
	public void test_new_vo_performance(){


		long start = System.currentTimeMillis();

		for (int i = 0; i < 10000 * 100; i++){
			PersonVo vo = new PersonVo();

			vo.setName("tom" + i);
			vo.setAge(i);
			vo.setEmail("tom" + i + "@x.xx");
			vo.setPhone("12500000000");

		}

		long end = System.currentTimeMillis();

		System.out.println("用时：" + (end - start));

	}


	@Test
	public void test_reflact_vo_performance() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {


		long start = System.currentTimeMillis();

		Method setName = PersonVo.class.getMethod("setName", String.class);
		Method setAge = PersonVo.class.getMethod("setAge", int.class);
		Method setEmail = PersonVo.class.getMethod("setEmail", String.class);
		Method setPhone = PersonVo.class.getMethod("setPhone", String.class);

		for (int i = 0; i < 10000 * 100; i++){
			PersonVo vo = new PersonVo();

			setName.invoke(vo, "tom" + i);
			setAge.invoke(vo, i);
			setEmail.invoke(vo, "tom" + i + "@x.xx");
			setPhone.invoke(vo, "12500000000");
		}

		long end = System.currentTimeMillis();

		System.out.println("用时：" + (end - start));

	}

}
