package com.sand.sqlbuild.builder;

import com.sand.sqlbuild.builder.impl.BuilderFactory;
import com.sand.sqlbuild.builder.po.PersonPo;
import org.junit.Test;

/**
 * @author : sun.mt
 * @create : 2015/11/10 10:43
 * @since : 0.1.2
 */
public class PersonTest {


	@Test
	public void test_batch(){

		Builder builder = BuilderFactory.create()
				.insert(PersonPo.class,
						PersonPo.name.eq("121"),
						PersonPo.age.eq(11)
				);

		System.out.println(builder.build().getSql());


		Builder builder2 = BuilderFactory.create()
				.update(PersonPo.class)
				.set(
						PersonPo.name.epv()
				)
				.where(PersonPo.age.epv());

		System.out.println(builder2.build().getSql());


		Builder builder3 = BuilderFactory.create()
				.upsertOracle(PersonPo.class,
						new Setter[]{
								PersonPo.name.epv()
						},
						new Filter[]{
								PersonPo.name.epv()
						}
				);

		System.out.println(builder3.build().getSql());


	}

}
