# 流程存储
---

*注: eclipse创建bpmn自动保存图片设置: window -> preferences -> activiti -> save actions -> 勾选 create process definition image when saving the diagram*

## 1.流程文件部署

表名: ACT_RE_DEPLOYMENT
接口: Deployment
子接口: DeploymentEntity
实现类: DeploymentEntityImpl

属性:

- id: 主键
- name: 部署名称
- deploymentTime: 部署时间
- category: 部署的类别
- tenantId: 预留字段
- key: 键

## 2.添加输入流资源

```java
public void test1() throws Exception {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

	// 存储服务
	RepositoryService repositoryService = engine.getRepositoryService();

	// 加载图片
	InputStream is1 = this.getClass().getClassLoader().getResourceAsStream("process1.png");
	InputStream is2 = this.getClass().getClassLoader().getResourceAsStream("process2.png");

	// 部署对象
	DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

	// 添加资源输入流
	deploymentBuilder.addInputStream("is1", is1);
	deploymentBuilder.addInputStream("is2", is2);

	// 部署
	deploymentBuilder.deploy();

	if (engine != null) {
		engine.close();
	}

	System.out.println("test1-测试结束!");
}
```

## 3.添加Classpath资源: 等同于test1, 程序内部实现了test1

```java
public void test2() throws Exception {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

	// 存储服务
	RepositoryService repositoryService = engine.getRepositoryService();

	// 部署对象
	DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

	// 添加资源输入流
	deploymentBuilder.addClasspathResource("process1.png");
	deploymentBuilder.addClasspathResource("process2.png");

	// 部署
	deploymentBuilder.deploy();

	if (engine != null) {
		engine.close();
	}

	System.out.println("test2-测试结束!");
}
```

## 4.添加String资源

```java
public void test3() throws Exception {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

	// 存储服务
	RepositoryService repositoryService = engine.getRepositoryService();

	// 部署对象
	DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

	// 添加资源输入流
	deploymentBuilder.addString("process1", "流程1");
	deploymentBuilder.addString("process2", "流程2");

	// 部署
	deploymentBuilder.deploy();

	if (engine != null) {
		engine.close();
	}

	System.out.println("test3-测试结束!");
}
```

## 5.添加zip资源

```java
public void test4() throws Exception {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

	// 存储服务
	RepositoryService repositoryService = engine.getRepositoryService();

	// 部署对象
	DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

	InputStream is = this.getClass().getClassLoader().getResourceAsStream("processes.zip");

	// 添加资源输入流
	deploymentBuilder.addZipInputStream(new ZipInputStream(is));

	// 部署
	deploymentBuilder.deploy();

	if (engine != null) {
		engine.close();
	}

	System.out.println("test4-测试结束!");
}
```

## 6.添加BPMN模型资源

```java
public void test5() {
	// 加载默认引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	// 存储服务
	RepositoryService repositoryService = engine.getRepositoryService();

	DeploymentBuilder builder = repositoryService.createDeployment();
	builder.addBpmnModel("MyCreateProcess", this.createBpmnModel()).name("MyCreateDeploy").deploy();

	if (engine != null) {
		engine.close();
	}

	System.out.println("test5-测试结束");
}
```

代码创建流程

```java
public BpmnModel createBpmnModel() {
	// 创建模型
	BpmnModel bpmnModel = new BpmnModel();

	// 创建流程
	Process process = new Process();
	process.setId("myCreateProcess");
	process.setName("MyCreateProcess");

	// 开始事件
	StartEvent startEvent = new StartEvent();
	startEvent.setId("myStartEvent");
	startEvent.setName("开始");
	process.addFlowElement(startEvent);

	// 用户任务1
	UserTask userTask1 = new UserTask();
	userTask1.setId("askForLeaveTask");
	userTask1.setName("请假");
	process.addFlowElement(userTask1);

	// 用户任务2
	UserTask userTask2 = new UserTask();
	userTask2.setId("auditTask");
	userTask2.setName("审批");
	process.addFlowElement(userTask2);

	// 结束事件
	EndEvent endEvent = new EndEvent();
	endEvent.setId("myEndEvent");
	endEvent.setName("结束");
	process.addFlowElement(endEvent);

	// 添加流程顺序
	process.addFlowElement(new SequenceFlow("myStartEvent", "askForLeaveTask"));
	process.addFlowElement(new SequenceFlow("askForLeaveTask", "auditTask"));
	process.addFlowElement(new SequenceFlow("auditTask", "myEndEvent"));

	// 把流程加进模型
	bpmnModel.addProcess(process);

	return bpmnModel;
}
```

## 7.设置部署信息

```java
public void test6() {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

	// 存储服务
	RepositoryService repositoryService = engine.getRepositoryService();

	// 部署
	DeploymentBuilder builder = repositoryService.createDeployment();

	// 设置属性
	builder.name("myDeploy").category("myCategory").key("myKey").tenantId("myTenantId").deploy();

	System.out.println("test6: ");
	List<Deployment> deploys = repositoryService.createDeploymentQuery().deploymentName("myDeploy").list();
	for (Deployment deploy : deploys) {
		System.out.println(String.format("部署后: id = %s, name = %s", deploy.getId(), deploy.getName()));
	}

	if (engine != null) {
		engine.close();
	}

	System.out.println("test6-测试结束!");
}
```

## 8.过滤重复部署

```java
public void test7() {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

	// 存储服务
	RepositoryService repositoryService = engine.getRepositoryService();

	// 部署
	DeploymentBuilder builder1 = repositoryService.createDeployment();

	// 启动过滤
	builder1.enableDuplicateFiltering();

	// 设置属性
	builder1.addClasspathResource("duplicateProcess.bpmn").name("DuplicateFilterA").deploy();

	// 部署
	DeploymentBuilder builder2 = repositoryService.createDeployment();

	// 启动过滤
	// builder2.enableDuplicateFiltering();

	// 设置属性
	builder2.addClasspathResource("duplicateProcess.bpmn").name("DuplicateFilterA").deploy();

	// 部署
	DeploymentBuilder builder3 = repositoryService.createDeployment();

	// 启动过滤
	builder3.enableDuplicateFiltering();

	// 设置属性
	builder3.addClasspathResource("duplicateProcess.bpmn").name("DuplicateFilterA").deploy();

	if (engine != null) {
		engine.close();
	}

	System.out.println("test7-测试结束!");
}
```