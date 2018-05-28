# 流程控制
---

执行流

接口：Execution
子接口：ExecutionEntity
实现类：ExecutionEntityImpl

- id: 主键
- revision： 版本号
- businessKey： 流程实例的业务主键
- parentId：父执行流的ID
- processDefinitionId：流程定义的ID
- superExecutionId：父执行流的ID
- activityId：当前执行流的动作
- isActive：活跃
- isConcurrent：并行
- isScope：执行流的范围
- isEventScope：事件范围
- suspensionState：流程中断状态，1活跃；2中断

## 1.启动流程的三种主要方法

### (1) 通过流程定义的id启动流程:startProcessInstanceById

```java
public void test1() {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	// 存储服务
	RepositoryService repositoryService = engine.getRepositoryService();
	// 运行时服务
	RuntimeService runtimeService = engine.getRuntimeService();
	
	// 部署流程
	Deployment deploy = repositoryService.createDeployment().addClasspathResource("MyProcess.bpmn").deploy();
	// 查询流程定义对象
	ProcessDefinition processDef = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
	
	// 定义参数
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("name", "张三");
	map.put("age", 18);
	
	// 通过流程定义的id启动流程实例:param1和param2为业务主键
	runtimeService.startProcessInstanceById(processDef.getId());// 流程定义的id
	runtimeService.startProcessInstanceById(processDef.getId(), "param1");
	runtimeService.startProcessInstanceById(processDef.getId(), map);
	runtimeService.startProcessInstanceById(processDef.getId(), "param2", map);
	
	// 查询流程实例的个数
	long count = runtimeService.createProcessInstanceQuery().count();
	System.out.println("test1: 流程实例的个数"+count);
}
```

结果

```
test1: 流程实例的个数4
```

### (2) 

```java
public void test2() {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	// 存储服务
	RepositoryService repositoryService = engine.getRepositoryService();
	// 运行时服务
	RuntimeService runtimeService = engine.getRuntimeService();
	
	// 部署流程
	repositoryService.createDeployment().addClasspathResource("MyProcess.bpmn").deploy();
	
	// 定义参数
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("name", "张三");
	map.put("age", 18);
	
	// 通过流程定义的id启动流程实例:param1和param2为业务主键
	runtimeService.startProcessInstanceByKey("myProcess");// 流程文件里的id
	runtimeService.startProcessInstanceByKey("myProcess", "param1");
	runtimeService.startProcessInstanceByKey("myProcess", map);
	runtimeService.startProcessInstanceByKey("myProcess", "param2", map);
	
	// 查询流程实例的个数
	long count = runtimeService.createProcessInstanceQuery().count();
	System.out.println("test2: 流程实例的个数"+count);
}
```

结果

```
test2: 流程实例的个数8
```

### (3) 

```java
public void test3() {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	// 存储服务
	RepositoryService repositoryService = engine.getRepositoryService();
	// 运行时服务
	RuntimeService runtimeService = engine.getRuntimeService();
	
	// 部署流程
	repositoryService.createDeployment().addClasspathResource("MyProcess.bpmn").deploy();
	
	// 定义参数
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("name", "张三");
	map.put("age", 18);
	
	// 通过流程定义的id启动流程实例:param1和param2为业务主键
	runtimeService.startProcessInstanceByMessage("startMsgName");// 消息的name
	runtimeService.startProcessInstanceByMessage("startMsgName", "param1");
	runtimeService.startProcessInstanceByMessage("startMsgName", map);
	runtimeService.startProcessInstanceByMessage("startMsgName", "param2", map);
	
	// 查询流程实例的个数
	long count = runtimeService.createProcessInstanceQuery().count();
	System.out.println("test3: 流程实例的个数"+count);
}
```

结果

```
test3: 流程实例的个数12
```