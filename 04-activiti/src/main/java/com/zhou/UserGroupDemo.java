package com.zhou;

import java.util.List;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.junit.Test;

/**
 * 分配用户到用户组
 * 
 * @author zhouweixin
 *
 */
public class UserGroupDemo {
	/**
	 * 添加组 添加用户 把用户分配到组
	 */
	@Test
	public void test1() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		IdentityService identityService = processEngine.getIdentityService();

		// 新增组
		String groupId = UUID.randomUUID().toString();
		Group group = identityService.newGroup(groupId);
		group.setName("经理");
		group.setType("管理员");
		identityService.saveGroup(group);

		// 新增用户
		String userId = UUID.randomUUID().toString();
		User user = identityService.newUser(userId);
		user.setLastName("王二麻子");
		identityService.saveUser(user);

		// 新增关系
		identityService.createMembership(userId, groupId);

		System.out.println("test1-测试结束!");
	}

	/**
	 * 在有关系的情况下尝试删除用户组
	 */
	public void test2() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		IdentityService identityService = processEngine.getIdentityService();

		// 新增组
		String groupId = UUID.randomUUID().toString();
		Group group = identityService.newGroup(groupId);
		group.setName("经理");
		group.setType("管理员");
		identityService.saveGroup(group);

		// 新增用户
		String userId = UUID.randomUUID().toString();
		User user = identityService.newUser(userId);
		user.setLastName("王二麻子");
		identityService.saveUser(user);

		// 新增关系
		identityService.createMembership(userId, groupId);

		System.out.println("删除group前:");
		Group g = identityService.createGroupQuery().groupId(groupId).singleResult();
		User u = identityService.createUserQuery().userId(userId).singleResult();
		System.out.println(String.format("test2-group.name = %s", g.getName()));
		System.out.println(String.format("test2-user.name = %s", u.getLastName()));

		// 删除用户组
		identityService.deleteGroup(groupId);

		System.out.println("删除group后:");
		g = identityService.createGroupQuery().groupId(groupId).singleResult();
		u = identityService.createUserQuery().userId(userId).singleResult();
		if (g != null) {
			System.out.println(String.format("test2-group.name = %s", g.getName()));
		} else {
			System.out.println("test2-group为空");
		}

		if (u != null) {
			System.out.println(String.format("test2-user.name = %s", u.getLastName()));
		} else {
			System.out.println("test2-user为空");
		}

		System.out.println("test2-测试结束!");
	}

	/**
	 * 在有关系的情况下尝试删除用户
	 */
	public void test3() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		IdentityService identityService = processEngine.getIdentityService();

		// 新增组
		String groupId = UUID.randomUUID().toString();
		Group group = identityService.newGroup(groupId);
		group.setName("经理");
		group.setType("管理员");
		identityService.saveGroup(group);

		// 新增用户
		String userId = UUID.randomUUID().toString();
		User user = identityService.newUser(userId);
		user.setLastName("王二麻子");
		identityService.saveUser(user);

		// 新增关系
		identityService.createMembership(userId, groupId);

		System.out.println("删除user前:");
		Group g = identityService.createGroupQuery().groupId(groupId).singleResult();
		User u = identityService.createUserQuery().userId(userId).singleResult();
		System.out.println(String.format("test3-group.name = %s", g.getName()));
		System.out.println(String.format("test3-user.name = %s", u.getLastName()));

		// 删除用户组
		identityService.deleteUser(userId);

		System.out.println("删除user后:");
		g = identityService.createGroupQuery().groupId(groupId).singleResult();
		u = identityService.createUserQuery().userId(userId).singleResult();
		if (g != null) {
			System.out.println(String.format("test3-group.name = %s", g.getName()));
		} else {
			System.out.println("test3-group为空");
		}

		if (u != null) {
			System.out.println(String.format("test3-user.name = %s", u.getLastName()));
		} else {
			System.out.println("test3-user为空");
		}

		System.out.println("test3-测试结束!");
	}

	/**
	 * 删除用户和用户组的关系
	 */
	public void test4() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		IdentityService identityService = processEngine.getIdentityService();

		// 新增组
		String groupId = UUID.randomUUID().toString();
		Group group = identityService.newGroup(groupId);
		group.setName("经理");
		group.setType("管理员");
		identityService.saveGroup(group);

		// 新增用户
		String userId = UUID.randomUUID().toString();
		User user = identityService.newUser(userId);
		user.setLastName("王二麻子");
		identityService.saveUser(user);

		// 新增关系
		identityService.createMembership(userId, groupId);

		// 删除关系
		identityService.deleteMembership(userId, groupId);

		System.out.println("test4-测试结束!");
	}

	/**
	 * 查询用户组下所有的用户
	 */
	public void test5() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		IdentityService identityService = processEngine.getIdentityService();

		// 新增组1
		String groupId = UUID.randomUUID().toString();
		Group group = identityService.newGroup(groupId);
		group.setName("组1");
		group.setType("group1");
		identityService.saveGroup(group);

		// 新增用户1
		String userId = UUID.randomUUID().toString();
		User user = identityService.newUser(userId);
		user.setLastName("用户1");
		identityService.saveUser(user);

		// 用户1添加到组1
		identityService.createMembership(userId, groupId);

		// 新增用户2
		userId = UUID.randomUUID().toString();
		user = identityService.newUser(userId);
		user.setLastName("用户2");
		identityService.saveUser(user);

		// 用户2添加到组1
		identityService.createMembership(userId, groupId);

		// 新增用户3
		userId = UUID.randomUUID().toString();
		user = identityService.newUser(userId);
		user.setLastName("用户3");
		identityService.saveUser(user);

		// 用户3添加到组1
		identityService.createMembership(userId, groupId);

		// 查询组1下的所有用户
		List<User> list = identityService.createUserQuery().memberOfGroup(groupId).list();
		for (User u : list) {
			System.out.println("test5-" + u.getLastName());
		}

		System.out.println("test5-测试结束!");
	}

	/**
	 * 查询用户所属的用户组
	 */
	public void test6() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		IdentityService identityService = processEngine.getIdentityService();

		// 新增用户1
		String userId = UUID.randomUUID().toString();
		User user = identityService.newUser(userId);
		user.setLastName("用户1");
		identityService.saveUser(user);

		// 新增组1
		String groupId = UUID.randomUUID().toString();
		Group group = identityService.newGroup(groupId);
		group.setName("组1");
		group.setType("group1");
		identityService.saveGroup(group);

		// 用户1添加到组1
		identityService.createMembership(userId, groupId);
		
		groupId = UUID.randomUUID().toString();
		group = identityService.newGroup(groupId);
		group.setName("组2");
		group.setType("group2");
		identityService.saveGroup(group);

		// 用户1添加到组2
		identityService.createMembership(userId, groupId);
		
		groupId = UUID.randomUUID().toString();
		group = identityService.newGroup(groupId);
		group.setName("组3");
		group.setType("group3");
		identityService.saveGroup(group);

		// 用户1添加到组3
		identityService.createMembership(userId, groupId);

		// 查询组1下的所有用户
		List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
		for(Group g:groups) {
			System.out.println("test6-" + g.getName());
		}

		System.out.println("test6-测试结束!");
	}

	public static void main(String[] args) {
		new UserGroupDemo().test6();
	}

}
