# 流程引擎的创建
---

### (1)通过默认方式创建流程引擎对象

```java
public void test1() {
	// 默认加载resource下的activiti.cfg.xml配置文件
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	System.out.println("test1-引擎名称: " + processEngine.getName());
}

public void test2() {
	// 下面是test1的执行细节, 当执行init函数时, 所有流程引擎对象都会由ProcessEngines的Map来管理
	ProcessEngines.init();
	Map<String, ProcessEngine> processEngines = ProcessEngines.getProcessEngines();
	
	if(processEngines.containsKey("default")) {
		ProcessEngine processEngine = processEngines.get("default");
		System.out.println("test2-引擎名称: " + processEngine.getName());
	}
}
```

输出

```
test1-引擎名称: default
test2-引擎名称: default
```

### (2)通过配置文件创建流程引擎对象

```java
public void test3() {
	// 加载配置文件
	ProcessEngineConfiguration config = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("my.cfg.xml", "myConfig");
	
	// 修改名称
	config.setProcessEngineName("myProcessEngine");
	ProcessEngine processEngine = config.buildProcessEngine();
	System.out.println("test3-流程引擎\n"+ProcessEngines.getProcessEngines());
	processEngine.close();
}
```

输出

```
test3-流程引擎
{myProcessEngine=org.activiti.engine.impl.ProcessEngineImpl@324a0017}
```

### (3)引擎对象的注册和注销

```java
public void test4() {
	// 加载的同时会注册流程引擎
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	System.out.println("test4-加载后-流程引擎对象数量 : " + ProcessEngines.getProcessEngines().size());
	System.out.println(ProcessEngines.getProcessEngines());
	
	// 注销
	ProcessEngines.unregister(processEngine);
	System.out.println("test4-注销后-流程引擎对象数量 : " + ProcessEngines.getProcessEngines().size());
	System.out.println(ProcessEngines.getProcessEngines());
	processEngine.close();
	
	// 设置名称重新创建
	ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
	processEngineConfiguration.setProcessEngineName("newName");
	processEngine = processEngineConfiguration.buildProcessEngine();
	
	// 注册		
	ProcessEngines.registerProcessEngine(processEngine);
	System.out.println("test4-注册后-流程引擎对象数量 : " + ProcessEngines.getProcessEngines().size());
	System.out.println(ProcessEngines.getProcessEngines());
	processEngine.close();
}
```

输出

```
test4-加载后-流程引擎对象数量 : 1
{default=org.activiti.engine.impl.ProcessEngineImpl@17aad511}
test4-注销后-流程引擎对象数量 : 0
{}
test4-注册后-流程引擎对象数量 : 1
{newName=org.activiti.engine.impl.ProcessEngineImpl@43f82e78}
```