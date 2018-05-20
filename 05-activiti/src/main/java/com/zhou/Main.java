package com.zhou;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.junit.Test;

public class Main {

	/**
	 * 添加20个组
	 */
	@Test
	public void test0() {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 加载身份服务
		IdentityService identityService = engine.getIdentityService();

		// 添加5个组
		for (int i = 1; i <= 20; i++) {
			Group group = identityService.newGroup(String.format("%02d", i));
			group.setName(String.format("组%02d", i));
			group.setType(String.format("类型%02d", i));
			identityService.saveGroup(group);
		}

		if (engine != null) {
			engine.close();
		}
	}

	/**
	 * list方法
	 */
	@Test
	public void test1() {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 加载身份服务
		IdentityService identityService = engine.getIdentityService();

		// 查询
		List<Group> groups = identityService.createGroupQuery().list();
		System.out.println("test1:");
		for (Group g : groups) {
			System.out.println(String.format("id = %s, name = %s, type = %s", g.getId(), g.getName(), g.getType()));
		}
	}

	/**
	 * listPage方法
	 */
	@Test
	public void test2() {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 加载身份服务
		IdentityService identityService = engine.getIdentityService();

		// 查询
		List<Group> groups = identityService.createGroupQuery().listPage(2, 6);
		System.out.println("test2:");
		for (Group g : groups) {
			System.out.println(String.format("id = %s, name = %s, type = %s", g.getId(), g.getName(), g.getType()));
		}
	}

	/**
	 * count方法
	 */
	@Test
	public void test3() {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 加载身份服务
		IdentityService identityService = engine.getIdentityService();

		// 查询
		long count = identityService.createGroupQuery().count();
		System.out.println("test3: count = " + count);
	}

	/**
	 * asc / desc 方法
	 */
	@Test
	public void test4() {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 加载身份服务
		IdentityService identityService = engine.getIdentityService();

		// 查询
		System.out.println("test4:");
		List<Group> groups = identityService.createGroupQuery().orderByGroupName().asc().list();
		System.out.println("名称增序:");
		for (Group g : groups) {
			System.out.println(String.format("id = %s, name = %s, type = %s", g.getId(), g.getName(), g.getType()));
		}

		groups = identityService.createGroupQuery().orderByGroupType().desc().listPage(4, 5);
		System.out.println("类型分页减序:");
		for (Group g : groups) {
			System.out.println(String.format("id = %s, name = %s, type = %s", g.getId(), g.getName(), g.getType()));
		}
	}

	/**
	 * 原生Sql语句
	 */
	@Test
	public void test5() {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 加载身份服务
		IdentityService identityService = engine.getIdentityService();

		// 查询
		List<Group> groups = identityService.createNativeGroupQuery()
				.sql("select * from act_id_group where name_=#{name}").parameter("name", "组01").list();

		System.out.println("test5: ");
		for (Group g : groups) {
			System.out.println(String.format("id = %s, name = %s, type = %s", g.getId(), g.getName(), g.getType()));
		}
	}
}
