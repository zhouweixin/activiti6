# 流程权限

用户或用户组分配流程权限

表名: ACT_RU_IDENTITYLINK
接口: IdentityLink
子接口: IdentityLinkEntity
实现类: IdentityLinkEntityImpl

- id: 主键
- type: 数据类型: assignee, candidate, starter, participant, owner
- groupId: 用户组id
- userId: 用户id
- taskId: 任务id
- processDefId: 流程定义id

## (1) 流程分配用户及查询(用户组与用户类似)

```java
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
```

结果

```
流程定义1: id = process1:11:57507, name = process1
流程定义2: id = process2:9:57511, name = process2
user1 : process1
user2 : process1
user2 : process2
user3 : process2
procDef1 : user1
procDef1 : user2
procDef2 : user2
procDef2 : user3
```
