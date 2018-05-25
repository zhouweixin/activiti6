# 任务附件管理
---

表名: ACT_HI_ATTACHMENT
接口: Attachment
子接口: AttachmentEntity
实现类: AttachmentEntityImpl

属性:

- id: 主键
- revision: 版本
- name: 名称
- description: 描述
- type: 类型
- taskId: 任务id
- procesInstanceId: 流程实例id
- url: 附件的url
- contentId: 内容的id, 输入流存到ACT_GE_TYTEARRAY表里

## 1.附件的添加与查询

```java
public void test1() {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	// 存储服务
	RepositoryService repositoryService = engine.getRepositoryService();
	// 任务服务
	TaskService taskService = engine.getTaskService();
	// 运行时服务
	RuntimeService runtimeService = engine.getRuntimeService();
	
	// 部署流程
	Deployment deploy = repositoryService.createDeployment().addClasspathResource("MyProcess.bpmn").deploy();
	
	// 流程定义
	ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
	
	// 启动流程
	ProcessInstance processInstance = runtimeService.startProcessInstanceById(pd.getId());
	
	// 查询当前任务
	Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
	
	// (1) 添加文本附件
	taskService.createAttachment("类型: 文本", task.getId(), processInstance.getId(), "名称1", "描述1", "内容1");
	// (2) 添加图片附件
	InputStream is = this.getClass().getClassLoader().getResourceAsStream("MyProcess.png");
	taskService.createAttachment("类型: 图片", task.getId(), processInstance.getId(), "名称2", "描述2", is);
	
	System.out.println("test1测试结束");
}
```




