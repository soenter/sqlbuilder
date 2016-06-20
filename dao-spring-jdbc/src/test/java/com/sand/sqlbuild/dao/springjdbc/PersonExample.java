package com.sand.sqlbuild.dao.springjdbc;

import com.sand.sqlbuild.builder.*;
import com.sand.sqlbuild.builder.impl.BuilderFactory;
import com.sand.sqlbuild.builder.impl.PagingBuilderFactory;
import com.sand.sqlbuild.dao.springjdbc.po.PersonPo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.DriverManager;
import java.util.*;

/**
 * @author : sun.mt
 * @create : 2015/11/10 10:43
 * @since : 0.1.2
 */

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback=false)
@ContextConfiguration(locations = {"classpath*:spring-jdbc.xml"})
@Transactional
public class PersonExample {

	@Resource(name="com.sand.sqlbuild.dao.springjdbc.BaseDaoImpl")
	private BaseDao dao;

	@Test
	/**
	 * select * from person
	 * where name = 'tom'
	 * and (age = '23' or email like 'tom@%' or phone <> '13500000000') and rownum <= 1
	 * order by name desc, age asc
	 */
	public void test_select_order_by(){
		Builder builder = BuilderFactory.create()
				.select(PersonPo.all)
				.from(PersonPo.class)
				.where(PersonPo.name.eq("tom"))
				.and()
				.ls(PersonPo.age.eq(23)).or(PersonPo.email.llk("tom@"))
					.or(PersonPo.phone.ne("13500000000"))
				.rs()
				.and().rownum(1)//"rownum <= 1" 只取一条
				.orderBy(
						PersonPo.name.desc(),
						PersonPo.age.asc()
				);

		BuildResult result = builder.build();
		System.out.println(result.getSql());
		PersonPo po = dao.queryForPo(result, new PersonPo());

		if(po.isEmpty()){
			//处理空值，注意：不要判断null，因为传入 dao 的 new PersonPo() 不会为null
		}
		System.out.println("----------------直接取值------------------");
		System.out.println("name:" + po.getValue(PersonPo.name));
		System.out.println("email:" + po.getValue(PersonPo.email));

		System.out.println("----------------遍历值------------------");
		Iterator<Map.Entry<Field<?>, Object>> it = po.iterator();
		while (it.hasNext()){
			Map.Entry<Field<?>, Object> entry = it.next();
			System.out.println(entry.getKey().getName() + ":" + entry.getValue());
		}

	}

	@Test
	/**
	 * select name, count(1) as count, max(age) as age
	 * from person
	 * groug by name
	 */
	public void test_select_group_by(){
		Builder builder = BuilderFactory.create()
				.select(PersonPo.name).comma()
				.func("count(1)").as("count", Integer.class).comma()
				.func("max").ls().args(PersonPo.age).rs().as(PersonPo.age)
				.from(PersonPo.class)
				.groupBy(PersonPo.name);

		BuildResult result = builder.build();
		System.out.println(result.getSql());
		List<PersonPo> pos = dao.queryForPoList(result, PersonPo.class);


		if(pos == null || pos.size() == 0){
			//处理空值，注意：可以判断 null，因为传入的是 PersonPo.class
			System.out.println("没有查询到结果");
			return;
		}
		for(PersonPo po: pos){
			System.out.println("----------------取值------------------");
			System.out.println("name:" + po.getValue(PersonPo.name));
			System.out.println("count:" + po.getValue("count", Integer.class));
			System.out.println("age:" + po.getValue(PersonPo.age));
		}

	}

	@Test
	/**
	 * insert person(name, email, phone, age) values('tom', 'tom@xx.com','13500000000', 22)
	 */
	public void test_insert(){
		Builder builder = BuilderFactory.create()
				.insert(PersonPo.class, new Setter[]{
						PersonPo.name.eq("tom3"),
						PersonPo.email.eq("tom3@xx.com"),
						PersonPo.phone.eq(""),
						PersonPo.age.eq(22)
				});

		BuildResult result = builder.build();
		System.out.println(result.getSql());
		System.out.println(result.getParameters());
		int num = dao.insert(result);
		System.out.println("插入条数：" + num);

	}

	@Test
	/**
	 * update person set phone = '13500000001', age = 23 where name = 'tom'
	 */
	public void test_update(){
		Builder builder = BuilderFactory.create()
				.update(PersonPo.class)
				.set(new Setter[]{
						PersonPo.phone.eq("13500000001"),
						PersonPo.age.eq(PersonPo.age.plus(23))
				})
				.where(PersonPo.name.eq("tom"));

		BuildResult result = builder.build();
		System.out.println(result.getSql());
		int num = dao.insert(result);
		System.out.println("更新条数：" + num);

	}

	@Test
	/**
	 * merge into person
	 * using (select 'tom' name, 'tom2@xx.com' email, '13500000002' phone, 24 age from dual) incoming
	 * on (person.name = incoming.name)
	 * when matched then
	 * update set person.email  = incoming.email, person.phone  = incoming.phone, person.age  = incoming.age
	 * when not matched then
	 * insert (person.phone, person.name, person.age, person.email) values (incoming.phone, incoming.name, incoming.age, incoming.email)
	 */
	public void test_upsert(){
		Builder builder = BuilderFactory.create()
				.upsertOracle(PersonPo.class, new Setter[]{
						PersonPo.email.eq("tom2@xx.com"),
						PersonPo.phone.eq("13500000002"),
						PersonPo.age.eq(24)
				}, new Filter[]{
						PersonPo.name.eq("tom")
				});

		BuildResult result = builder.build();
		System.out.println(result.getSql());
		int num = dao.insert(result);
		System.out.println("更新或插入条数：" + num);

	}

	@Test
	/**
	 * delete from person where name = 'tom'
	 */
	public void test_delete(){
		Builder builder = BuilderFactory.create()
				.delete().from(PersonPo.class).where(PersonPo.name.eq("tom"));

		BuildResult result = builder.build();
		System.out.println(result.getSql());
		int num = dao.delete(result);
		System.out.println("删除条数：" + num);
	}

	@Test
	/**
	 * insert person(name, email, phone, age) values(?, ?, ?, ?);
	 * -- params: [["tom1", "tom1@xx.com", "15000000001", 22],["tom2", "tom2@xx.com", "15000000002", 22],...]
	 */
	public void test_insert_batch(){
		Builder builder = BuilderFactory.create()
				.insert(PersonPo.class, new Setter<?>[]{
						PersonPo.name.epv(),// setter 值为空
						PersonPo.email.epv(),
						PersonPo.phone.epv(),
						PersonPo.age.epv()
				});

		BuildResult result = builder.build();
		System.out.println(result.getSql());

		final List<PersonPo> params = new ArrayList<PersonPo>(3);

		PersonPo po1 = new PersonPo();
		po1.setValue(PersonPo.name, "tom1");
		po1.setValue(PersonPo.email, "tom1@xx.com");
		po1.setValue(PersonPo.phone, "15000000001");
		po1.setValue(PersonPo.age, 22);
		params.add(po1);
		PersonPo po2 = new PersonPo();
		po2.setValue(PersonPo.name, "tom2");
		po2.setValue(PersonPo.email, "tom2@xx.com");
		po2.setValue(PersonPo.phone, "15000000002");
		po2.setValue(PersonPo.age, 22);
		params.add(po2);


		System.out.println("----------------方式1------------------");
		int[] nums = dao.batchUpdate(result, params);
		System.out.println("批量返回值：" + Arrays.toString(nums));

		System.out.println("----------------方式2------------------");
		nums = dao.batchUpdate(result, new BatchPoGetter() {
			public PersonPo getPo (int index) {
				return params.get(index);
			}

			public int getBatchSize () {
				return params.size();
			}
		});
		System.out.println("批量返回值：" + Arrays.toString(nums));
	}


	@Test
	/**
	 * update person set email = ?, phone = ?, age = ? where name = ?;
	 * -- params: [["tom1@xx.com", "15000000001", 22, "tom1"],["tom2@xx.com", "15000000002", 22, "tom2"],...]
	 */
	public void test_update_batch(){
		Builder builder = BuilderFactory.create()
				.update(PersonPo.class)
				.set(new Setter<?>[]{
						PersonPo.email.epv(),
						PersonPo.phone.epv(),
						PersonPo.age.epv()
				})
				.where(PersonPo.name.epv());// filter 值为空

		BuildResult result = builder.build();
		System.out.println(result.getSql());

		final List<PersonPo> params = new ArrayList<PersonPo>(3);

		PersonPo po1 = new PersonPo();
		po1.setValue(PersonPo.name, "tom1");
		po1.setValue(PersonPo.email, "tom1@xx.com");
		po1.setValue(PersonPo.phone, "15000000001");
		po1.setValue(PersonPo.age, 22);
		params.add(po1);
		PersonPo po2 = new PersonPo();
		po2.setValue(PersonPo.name, "tom2");
		po2.setValue(PersonPo.email, "tom2@xx.com");
		po2.setValue(PersonPo.phone, "15000000002");
		po2.setValue(PersonPo.age, 22);
		params.add(po2);


		System.out.println("----------------方式1------------------");
		int[] nums = dao.batchUpdate(result, params);
		System.out.println("批量返回值：" + Arrays.toString(nums));

		System.out.println("----------------方式2------------------");
		nums = dao.batchUpdate(result, new BatchPoGetter() {
			public PersonPo getPo (int index) {
				return params.get(index);
			}

			public int getBatchSize () {
				return params.size();
			}
		});
		System.out.println("批量返回值：" + Arrays.toString(nums));
	}

	@Test
	/**
	 * insert person(name, email, phone, age) values('tom3', 'tom3@xx.com','13500000003', 22)
	 */
	public void test_insert_po(){

		PersonPo po = new PersonPo();
		po.setValue(PersonPo.name, "tom3");
		po.setValue(PersonPo.email, "tom3@xx.com");
		po.setValue(PersonPo.phone, "15000000003");
		po.setValue(PersonPo.age, 22);

		dao.insert(po);
	}


	@Test
	/**
	 * select case name when 'tom1' then '汤姆1' else name end as chineseName,
	 * case when age < 18 then '未成年'
	 *      when age between 18 and 45 then '青年'
	 *      when age > 45 and age < 59 then '中年'
	 * else '老年' end as ageGroup
	 * from person
	 */
	public void test_case_when(){

		Builder builder = BuilderFactory.create()
				.select()
				.cases(PersonPo.name)
					.when("tom1").then("汤姆1")
				.elses(PersonPo.name).end().as("chineseName", String.class).comma()
				.cases()
					.when(PersonPo.age.lt(18)).then("未成年")
					.when(PersonPo.age.bta(18, 45)).then("青年")
					.when(PersonPo.age.gt(45)).and(PersonPo.age.lt(59)).then("中年")
				.elses("老年").end().as("ageGroup", String.class)
				.from(PersonPo.class);

		List<PersonPo> pos = dao.queryForPoList(builder.build(), PersonPo.class);

		if(pos == null || pos.size() == 0){
			//处理空值，注意：可以判断 null，因为传入的是 PersonPo.class
			System.out.println("没有查询到结果");
			return;
		}
		System.out.println("----------------取值------------------");
		for(PersonPo po: pos){
			System.out.println("chineseName:" + po.getValue("chineseName", Integer.class));
			System.out.println("ageGroup:" + po.getValue("ageGroup", Integer.class));
		}

	}


	@Test
	/**
	 * select name, age
	 * from person
	 * where name is not null and email is null
	 * groug by name
	 */
	public void test_select_where_is_null_or_not_null(){
		Builder builder = BSQL
				.select(PersonPo.name, PersonPo.age)
				.from(PersonPo.class)
				.where(PersonPo.name.isNotNull())
				.and(PersonPo.email.isNull());

		BuildResult result = builder.build();
		System.out.println(result.getSql());
		List<PersonPo> pos = dao.queryForPoList(result, PersonPo.class);


		if(pos == null || pos.size() == 0){
			//处理空值，注意：可以判断 null，因为传入的是 PersonPo.class
			System.out.println("没有查询到结果");
			return;
		}
		for(PersonPo po: pos){
			System.out.println("----------------取值------------------");
			System.out.println("name:" + po.getValue(PersonPo.name));
			System.out.println("count:" + po.getValue("count", Integer.class));
			System.out.println("age:" + po.getValue(PersonPo.age));
		}

	}

	@Test
	/**
	 * select name || '_' || email, age + age as age, age + age as age1
	 * from person
	 */
	public void test_select_field_join_field(){
		Builder builder = BSQL
				.select(
						PersonPo.name.join(" || '_' || ", PersonPo.email).join(" || '_' || ", PersonPo.age).as(PersonPo.name),
						PersonPo.age.join("+", PersonPo.age),
						PersonPo.age.plus(PersonPo.age).as("age1", Integer.class)
				)
				.from(PersonPo.class);

		BuildResult result = builder.build();
		System.out.println(result.getSql());
		List<PersonPo> pos = dao.queryForPoList(result, PersonPo.class);

		if(pos == null || pos.size() == 0){
			//处理空值，注意：可以判断 null，因为传入的是 PersonPo.class
			System.out.println("没有查询到结果");
			return;
		}
		for(PersonPo po: pos){
			System.out.println("----------------取值------------------");
			System.out.println("name:" + po.getValue(PersonPo.name));
			System.out.println("age1:" + po.getValue(PersonPo.age));
			System.out.println("age2:" + po.getValue("age1", Integer.class));
		}

	}
	@Test
	/**
	 * select name || '_' || email, age + age as age, age + age as age1
	 * from person
	 * where name != email
	 */
	public void test_filter_with_field(){
		Builder builder = BSQL
				.select(
						PersonPo.name.join(" || '_' || ", PersonPo.email).join(" || '_' || ", PersonPo.age).as(PersonPo.name),
						PersonPo.age.join("+", PersonPo.age),
						PersonPo.age.plus(PersonPo.age).as("age1", Integer.class)
				)
				.from(PersonPo.class)
				.where(PersonPo.name.ne(PersonPo.email));

		BuildResult result = builder.build();
		System.out.println(result.getSql());
		List<PersonPo> pos = dao.queryForPoList(result, PersonPo.class);

		if(pos == null || pos.size() == 0){
			//处理空值，注意：可以判断 null，因为传入的是 PersonPo.class
			System.out.println("没有查询到结果");
			return;
		}
		for(PersonPo po: pos){
			System.out.println("----------------取值------------------");
			System.out.println("name:" + po.getValue(PersonPo.name));
			System.out.println("age1:" + po.getValue(PersonPo.age));
			System.out.println("age2:" + po.getValue("age1", Integer.class));
		}

	}
	@Test
	/**
	 * select name || '_' || email, age + age as age, age + age as age1
	 * from person
	 * where age + 1 > 20
	 * or age + age > 40
	 */
	public void test_filter_with_fields(){
		Builder builder = BSQL
				.select(
//						PersonPo.name,
						PersonPo.name.join(" || '_' || ", PersonPo.email).join(" || '_' || ", PersonPo.age).as(PersonPo.name),
						PersonPo.age.join("+", PersonPo.age),
						PersonPo.age.plus(PersonPo.age).as("age1", Integer.class)
				)
				.from(PersonPo.class)
				.where(PersonPo.age.plus(1).gt(20))
				.and(PersonPo.age.plus(PersonPo.age).gt(40));

		BuildResult result = builder.build();
		System.out.println(result.getSql());
		List<PersonPo> pos = dao.queryForPoList(result, PersonPo.class);

		if(pos == null || pos.size() == 0){
			//处理空值，注意：可以判断 null，因为传入的是 PersonPo.class
			System.out.println("没有查询到结果");
			return;
		}
		for(PersonPo po: pos){
			System.out.println("----------------取值------------------");
			System.out.println("name:" + po.getValue(PersonPo.name));
			System.out.println("age1:" + po.getValue(PersonPo.age));
			System.out.println("age2:" + po.getValue("age1", Integer.class));
		}

	}

	@Test
	public void test_mysql_paging(){
		PagingBuilder builder = PagingBuilderFactory.create()
				.select(PersonPo.all)
				.from(PersonPo.class, PersonPo.name)
				.where(PersonPo.age.gt(1))
				.and(PersonPo.email.lk("tom"))
				.and(PersonPo.name.lk("tom"))
				.orderBy(PersonPo.name.asc());


		long count = dao.queryForPagingCount(builder);

		System.out.println("分页总条数：" + count);

		List<PersonPo> pos = dao.queryForPagingPoList(builder, PersonPo.class, 1, 2, 1);
		for(PersonPo po: pos){
			System.out.println("----------------取值------------------");
			System.out.println("name:" + po.getValue(PersonPo.name));
			System.out.println("age1:" + po.getValue(PersonPo.age));
			System.out.println("age2:" + po.getValue("age1", Integer.class));
		}
	}

	@Test
	/**
	 * select name
	 * from person
	 * where like '%om%'
	 */
	public void test_jointer_with_like(){
		Builder builder = BSQL
				.select(
						PersonPo.name
				)
				.from(PersonPo.class)
				.where(PersonPo.name.lk("om"));

		BuildResult result = builder.build();
		System.out.println(result.getSql());
		List<PersonPo> pos = dao.queryForPoList(result, PersonPo.class);

		if(pos == null || pos.size() == 0){
			//处理空值，注意：可以判断 null，因为传入的是 PersonPo.class
			System.out.println("没有查询到结果");
			return;
		}
		for(PersonPo po: pos){
			System.out.println("----------------取值------------------");
			System.out.println("name:" + po.getValue(PersonPo.name));
		}

	}

	@Test
	/**
	 * select name
	 * from person
	 * where like '%om%'
	 */
	public void test_jointer_(){
		Builder builder = BSQL
				.select(
						PersonPo.age.subtract(PersonPo.age.plus(PersonPo.age)),
						PersonPo.age.subtract(PersonPo.age).plus(PersonPo.age).as("age2", Integer.class)
				)
				.from(PersonPo.class)
				.where(PersonPo.age.bta(1, 22));

		BuildResult result = builder.build();
		System.out.println(result.getSql());
		List<PersonPo> pos = dao.queryForPoList(result, PersonPo.class);

		if(pos == null || pos.size() == 0){
			//处理空值，注意：可以判断 null，因为传入的是 PersonPo.class
			System.out.println("没有查询到结果");
			return;
		}
		for(PersonPo po: pos){
			System.out.println("----------------取值------------------");
			System.out.println("age:" + po.getValue(PersonPo.age));
			System.out.println("age2:" + po.getValue("age2", Integer.class));
		}

	}
}
