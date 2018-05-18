# 用户组和用户
---

## 1.用户组

表名: **ACT_ID_GROUP**

接口: **Group**

子接口: **GroupEntity**

实现类: **GroupEntityImpl**

属性: 

- id : 主键
- name : 名称
- type : 类型
- revision : 版本, 用户不可修改, 每执行一次saveGroup加1

### (1) 用户组的新增---指定用户组的主键id

```java
public void test1() {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	// 身份服务
	IdentityService identityService = processEngine.getIdentityService();

	// 创建组
	Group group = identityService.newGroup("1");
	group.setName("管理员");
	group.setType("manager");

	// 保存组
	identityService.saveGroup(group);

	// 查询用户组
	Group g = identityService.createGroupQuery().groupId("1").singleResult();
	System.out.println("test1-{id = " + g.getId() + ", name = " + g.getName() + ", type = " + g.getType() + "}");
}
```

结果

```
test1-{id = 1, name = 管理员, type = manager}
```

### (2) 用户组的新增---自动生成主键id

```java
public void test2() {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	// 身份服务
	IdentityService identityService = processEngine.getIdentityService();

	// 创建组
	Group group = identityService.newGroup("");
	group.setName("管理员");
	group.setType("manager");
	group.setId(null);

	// 保存组
	identityService.saveGroup(group);

	// 查询用户组
	List<Group> groups = identityService.createGroupQuery().groupNameLike("管理员").list();

	System.out.println("test2");
	groups.stream().forEach(g -> {
		System.out.println("{id = " + g.getId() + ", name = " + g.getName() + ", type = " + g.getType() + "}");
	});
}
```

结果

```
test2
{id = 1, name = 管理员, type = manager}
{id = 20001, name = 管理员, type = manager}
```

### (3) 更新组---revision值为0是新增, >0是修改

```java
public void test3() {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	// 身份服务
	IdentityService identityService = processEngine.getIdentityService();

	// 查询用户组:此时查询到的g的revision属性>0, 再次保存时认为是更新操作
	Group g = identityService.createGroupQuery().groupId("1").singleResult();

	System.out.println("test3");
	System.out.println("修改前-{id = " + g.getId() + ", name = " + g.getName() + ", type = " + g.getType() + "}");

	// 修改后保存, 每次保存都会使revision加1
	g.setName("管理员-修改后");
	g.setType("manager-修改后");
	identityService.saveGroup(g);

	// 再次查询
	g = identityService.createGroupQuery().groupId("1").singleResult();
	System.out.println("修改后-{id = " + g.getId() + ", name = " + g.getName() + ", type = " + g.getType() + "}");
}
```

结果

```
test3
修改前-{id = 1, name = 管理员, type = manager}
修改后-{id = 1, name = 管理员-修改后, type = manager-修改后}
```

### (4) 删除用户组---级联删除

```java
public void test4() {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	// 身份服务
	IdentityService identityService = processEngine.getIdentityService();

	Group g = identityService.createGroupQuery().groupId("1").singleResult();
	System.out.println("test4");
	System.out.println("删除前-{id = " + g.getId() + ", name = " + g.getName() + ", type = " + g.getType() + "}");

	// 执行删除操作:会先删除用户与用户组之间的关系数据,然后删除该用户组(级联删除)
	identityService.deleteGroup("1");

	// 再次查询
	g = identityService.createGroupQuery().groupId("1").singleResult();
	if (g != null) {
		System.out.println("删除后-{id = " + g.getId() + ", name = " + g.getName() + ", type = " + g.getType() + "}");
	} else {
		System.out.println("删除后-查询结果为空");
	}
}
```

结果

```
test4
删除前-{id = 1, name = 管理员-修改后, type = manager-修改后}
删除后-查询结果为空
```

## 2.用户

表名: **ACT_ID_USER**, **ACT_ID_INFO**

接口: **User**

子接口: **UserEntity**

实现类: **UserEntityImpl**

属性: 

- id : 主键
- firstName : 姓
- lastName : 名
- email : 邮箱
- password : 密码
- pictureByteArrayId : 图片**PICTURE_GE_BTEARRAY**表的外键
- revision : 版本

### 用户的新增删除更新操作和用户组的相似的

## Activiti的数据查询




