# 任务变量
---

表名: ACT_RU_VARIABLE

Activiti支持的数据类型

1. Boolean
2. Date
3. Double
4. Integer
5. Long 
6. Null
7. Short
8. String


注: 

- setVariable设置的是全局变量, 在整个流程内的所有任务都可以访问到
- setVariableLocal设置的是局部变量, 只有本任务可以访问, 并且任务被删除或被完成后删除也会删除


## (1) 为任务添加属性变量: 基本类型

```java
public void test1() {
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	TaskService taskService = engine.getTaskService();
	Task task = taskService.newTask();
	taskService.saveTask(task);

	taskService.setVariable(task.getId(), "var_boolean", false);
	taskService.setVariable(task.getId(), "var_date", new Date());
	taskService.setVariable(task.getId(), "var_double", 2.3);
	taskService.setVariable(task.getId(), "var_int", 10);
	taskService.setVariable(task.getId(), "var_long", 200L);
	taskService.setVariable(task.getId(), "var_null", null);
	taskService.setVariable(task.getId(), "var_short", (short) 1);
	taskService.setVariable(task.getId(), "var_String", "hello");

	System.out.println("var_boolean = " + taskService.getVariable(task.getId(), "var_boolean"));
	System.out.println("var_date = " + taskService.getVariable(task.getId(), "var_date"));
	System.out.println("var_double = " + taskService.getVariable(task.getId(), "var_double"));
	System.out.println("var_int = " + taskService.getVariable(task.getId(), "var_int"));
	System.out.println("var_long = " + taskService.getVariable(task.getId(), "var_long"));
	System.out.println("var_null = " + taskService.getVariable(task.getId(), "var_null"));
	System.out.println("var_short = " + taskService.getVariable(task.getId(), "var_short"));
	System.out.println("var_String = " + taskService.getVariable(task.getId(), "var_String"));
	System.out.println("test1测试结束");
}
```

结果

```
var_boolean = false
var_date = Fri May 25 12:53:05 CST 2018
var_double = 2.3
var_int = 10
var_long = 200
var_null = null
var_short = 1
var_String = hello
test1测试结束
```

## (2) 为任务添加属性变量: 自定义类型

```java
public void test2() {
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	TaskService taskService = engine.getTaskService();
	Task task = taskService.newTask();
	taskService.saveTask(task);

	TestObjectVariable testObjectVariable = new TestObjectVariable(1, "张三", 18);
	taskService.setVariable(task.getId(), "testObjectVariable", testObjectVariable);
	System.out.println("testObjectVariable = " + taskService.getVariable(task.getId(), "testObjectVariable"));

	System.out.println("test2测试结束");
}
```

结果

```
testObjectVariable = TestObjectVariable [id=1, name=张三, age=18]
test2测试结束
```

## (3) 为任务添加属性变量: Map类型

```java
public void test3() {
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	TaskService taskService = engine.getTaskService();
	Task task = taskService.newTask();
	taskService.saveTask(task);

	Map<String, Object> map = new HashMap<String, Object>();
	map.put("id", 2);
	map.put("name", "李四");
	map.put("age", 20);
	taskService.setVariable(task.getId(), "map", map);
	System.out.println("map = " + taskService.getVariable(task.getId(), "map"));

	System.out.println("test3测试结束");
}
```

结果

```
map = {name=李四, id=2, age=20}
test3测试结束
```

## (4) 为任务添加属性变量: 数据对象(dataObject)

```java
public void test4() {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	// 存储服务
	RepositoryService repositoryService = engine.getRepositoryService();
	// 运行时服务
	RuntimeService runtimeService = engine.getRuntimeService();
	TaskService taskService = engine.getTaskService();

	// 部署流程
	Deployment deploy = repositoryService.createDeployment().addClasspathResource("data-object-process.bpmn")
			.deploy();
	// 流程定义
	ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId())
			.singleResult();

	// 启动流程
	ProcessInstance process = runtimeService.startProcessInstanceById(pd.getId());

	// 查询流程任务
	Task task = taskService.createTaskQuery().processInstanceId(process.getId()).singleResult();

	Map<String, DataObject> dataObjects = taskService.getDataObjects(task.getId());

	dataObjects.forEach((key, value) -> {
		System.out.println(String.format("%s-%s", key, value.getValue()));
	});
}
```

结果

```
name-王五
age-24
id-3
test4测试结束
```
