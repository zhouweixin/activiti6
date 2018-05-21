package com.zhou;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;

public class Main {
	
	public static void main(String[] args) {
		new Main().test2();
	}

	/**
	 * 流程权限设置与查询
	 */
	@Test
	public void test1() {
		// 加载默认配置文件
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

		// 身份服务
		IdentityService identityService = engine.getIdentityService();
		// 存储服务
		RepositoryService repositoryService = engine.getRepositoryService();

		// 创建用户1
		User user1 = identityService.newUser("");
		user1.setId(null);
		user1.setLastName("user1");
		identityService.saveUser(user1);

		// 创建用户2
		User user2 = identityService.newUser("");
		user2.setId(null);
		user2.setLastName("user2");
		identityService.saveUser(user2);

		// 创建用户3
		User user3 = identityService.newUser("");
		user3.setId(null);
		user3.setLastName("user3");
		identityService.saveUser(user3);

		// 部署流程1
		DeploymentBuilder builder1 = repositoryService.createDeployment();
		builder1.addClasspathResource("process1.bpmn");
		Deployment deploy1 = builder1.name("deploy1").deploy();

		// 部署流程2
		DeploymentBuilder builder2 = repositoryService.createDeployment();
		builder2.addClasspathResource("process2.bpmn");
		Deployment deploy2 = builder2.name("deploy2").deploy();

		ProcessDefinition processDefinition1 = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deploy1.getId()).singleResult();
		ProcessDefinition processDefinition2 = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deploy2.getId()).singleResult();

		System.out.println(
				String.format("流程定义1: id = %s, name = %s", processDefinition1.getId(), processDefinition1.getName()));
		System.out.println(
				String.format("流程定义2: id = %s, name = %s", processDefinition2.getId(), processDefinition2.getName()));

		// 把流程1权限分配给用户1和用户2
		repositoryService.addCandidateStarterUser(processDefinition1.getId(), user1.getId());
		repositoryService.addCandidateStarterUser(processDefinition1.getId(), user2.getId());

		// 把流程2权限分配给用户2和用户3
		repositoryService.addCandidateStarterUser(processDefinition2.getId(), user2.getId());
		repositoryService.addCandidateStarterUser(processDefinition2.getId(), user3.getId());

		// 分别查询用户1,2,3拥有权限的流程
		List<ProcessDefinition> user1processDefinitions = repositoryService.createProcessDefinitionQuery()
				.startableByUser(user1.getId()).list();
		List<ProcessDefinition> user2processDefinitions = repositoryService.createProcessDefinitionQuery()
				.startableByUser(user2.getId()).list();
		List<ProcessDefinition> user3processDefinitions = repositoryService.createProcessDefinitionQuery()
				.startableByUser(user3.getId()).list();

		for (ProcessDefinition pd : user1processDefinitions) {
			System.out.println(String.format("user1 : %s", pd.getName()));
		}
		for (ProcessDefinition pd : user2processDefinitions) {
			System.out.println(String.format("user2 : %s", pd.getName()));
		}
		for (ProcessDefinition pd : user3processDefinitions) {
			System.out.println(String.format("user3 : %s", pd.getName()));
		}
		
		// 分别查询流程1, 2分配的用户
		List<User> processDefinition1Users = identityService.createUserQuery().potentialStarter(processDefinition1.getId()).list();
		List<User> processDefinition2Users = identityService.createUserQuery().potentialStarter(processDefinition2.getId()).list();
		for(User user:processDefinition1Users) {
			System.out.println(String.format("procDef1 : %s", user.getLastName()));			
		}
		for(User user:processDefinition2Users) {
			System.out.println(String.format("procDef2 : %s", user.getLastName()));			
		}
	}

	@Test
	public void test2() {
		// 通过默认配置文件加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 身份服务
		IdentityService identityService = engine.getIdentityService();
		
		// 添加用户组1
		Group group1 = identityService.newGroup("");
		group1.setId(null);
		group1.setName("group1");
		identityService.saveGroup(group1);
		
		// 添加用户组2
		Group group2 = identityService.newGroup("");
		group2.setId(null);
		group2.setName("group2");
		identityService.saveGroup(group2);
		
		System.out.println("用户组:");
		System.out.println(String.format("group1.id = %s, group1.name = %s", group1.getId(), group1.getName()));
		System.out.println(String.format("group2.id = %s, group2.name = %s", group2.getId(), group2.getName()));
	}
}
