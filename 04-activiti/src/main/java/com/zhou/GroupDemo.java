package com.zhou;

import org.junit.Test;

import java.util.List;
import org.activiti.engine.*;
import org.activiti.engine.identity.Group;

/**
 * 用户组的增删查改操作
 * 
 * @author zhouweixin
 *
 */
public class GroupDemo {

	/**
	 * 创建组:设置主键
	 */
	@Test
	public void test1() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		// 身份服务
		IdentityService identityService = processEngine.getIdentityService();

		// 创建组
		Group group = identityService.newGroup("1");
		group.setName("管理员");
		group.setType("manager");

		// 保存组
		identityService.saveGroup(group);

		// 查询用户组
		Group g = identityService.createGroupQuery().groupId("1").singleResult();
		System.out.println("test1-{id = " + g.getId() + ", name = " + g.getName() + ", type = " + g.getType() + "}");
	}

	/**
	 * 创建组:自动生成组的主键
	 */
	@Test
	public void test2() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		// 身份服务
		IdentityService identityService = processEngine.getIdentityService();

		// 创建组
		Group group = identityService.newGroup("");
		group.setName("管理员");
		group.setType("manager");
		group.setId(null);

		// 保存组
		identityService.saveGroup(group);

		// 查询用户组
		List<Group> groups = identityService.createGroupQuery().groupNameLike("管理员").list();

		System.out.println("test2");
		groups.stream().forEach(g -> {
			System.out.println("{id = " + g.getId() + ", name = " + g.getName() + ", type = " + g.getType() + "}");
		});
	}

	/**
	 * 更新组: rev_列值为0是新增, 为1是修改
	 */
	@Test
	public void test3() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		// 身份服务
		IdentityService identityService = processEngine.getIdentityService();

		// 查询用户组:此时查询到的g的revision属性>0, 再次保存时认为是更新操作
		Group g = identityService.createGroupQuery().groupId("1").singleResult();

		System.out.println("test3");
		System.out.println("修改前-{id = " + g.getId() + ", name = " + g.getName() + ", type = " + g.getType() + "}");

		// 修改后保存, 每次保存都会使revision加1
		g.setName("管理员-修改后");
		g.setType("manager-修改后");
		identityService.saveGroup(g);

		// 再次查询
		g = identityService.createGroupQuery().groupId("1").singleResult();
		System.out.println("修改后-{id = " + g.getId() + ", name = " + g.getName() + ", type = " + g.getType() + "}");
	}

	/**
	 * 删除用户组:会先删除用户与用户组之间的关系数据,然后删除该用户组
	 */
	@Test
	public void test4() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		// 身份服务
		IdentityService identityService = processEngine.getIdentityService();

		Group g = identityService.createGroupQuery().groupId("1").singleResult();
		System.out.println("test4");
		System.out.println("删除前-{id = " + g.getId() + ", name = " + g.getName() + ", type = " + g.getType() + "}");

		// 执行删除操作:会先删除用户与用户组之间的关系数据,然后删除该用户组(级联删除)
		identityService.deleteGroup("1");

		// 再次查询
		g = identityService.createGroupQuery().groupId("1").singleResult();
		if (g != null) {
			System.out.println("删除后-{id = " + g.getId() + ", name = " + g.getName() + ", type = " + g.getType() + "}");
		} else {
			System.out.println("删除后-查询结果为空");
		}
	}
}
