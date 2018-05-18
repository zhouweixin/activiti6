package com.zhou;

import java.util.UUID;

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
		
		// 生成一个随机的主键
		String id = UUID.randomUUID().toString();
		
		User user = identityService.newUser(id);
		user.setFirstName("张");
		user.setLastName("三");
		user.setEmail("*********@163.com");
		user.setPassword("123456");
		identityService.saveUser(user);
		
		User u = identityService.createUserQuery().userId(id).singleResult();
		System.out.println(String.format("test1-{id = %s, firstName = %s, lastName = %s, email = %s, password = %s}", 
					u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getPassword()));
	}
}
