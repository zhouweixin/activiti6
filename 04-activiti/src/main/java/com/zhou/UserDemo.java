package com.zhou;

import org.activiti.engine.*;
import org.activiti.engine.identity.User;
import org.junit.Test;

public class UserDemo {

	/**
	 * 添加
	 */
	@Test
	public void test1() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		IdentityService identityService = processEngine.getIdentityService();

		User user = identityService.newUser("1");
		user.setFirstName("张");
		user.setLastName("三");
		user.setEmail("*********@163.com");
		user.setPassword("123456");
		identityService.saveUser(user);

		User u = identityService.createUserQuery().userId("1").singleResult();
		System.out.println(String.format("test1-{id = %s, firstName = %s, lastName = %s, email = %s, password = %s}",
				u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getPassword()));
	}

	/**
	 * 验证用户密码
	 */
	@Test
	public void test2() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		IdentityService identityService = processEngine.getIdentityService();

		User user = identityService.createUserQuery().userId("1").singleResult();
		System.out.println(String.format("test2-user {id = %s, firstName = %s, lastName = %s, email = %s, password = %s}",
				user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword()));

		boolean b1 = identityService.checkPassword("1", "123456");
		boolean b2 = identityService.checkPassword("1", "asdfas");

		System.out.println("123456--验证结果: " + b1);
		System.out.println("asdfas--验证结果: " + b2);
	}
}
