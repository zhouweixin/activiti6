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

### (1) 用户的新增删除更新操作和用户组的相似的

### (2) 验证用户密码

```java
public void test2() {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	IdentityService identityService = processEngine.getIdentityService();

	User user = identityService.createUserQuery().userId("1").singleResult();
	System.out
			.println(String.format("test2-user {id = %s, firstName = %s, lastName = %s, email = %s, password = %s}",
					user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword()));

	boolean b1 = identityService.checkPassword("1", "123456");
	boolean b2 = identityService.checkPassword("1", "asdfas");

	System.out.println("123456--验证结果: " + b1);
	System.out.println("asdfas--验证结果: " + b2);
}
```

结果

```
test2-user {id = 1, firstName = 张, lastName = 三, email = *********@163.com, password = 123456}
123456--验证结果: true
asdfas--验证结果: false
```

### (3) 设置用户自定义信息和用户图片

```java
public void test3() {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	IdentityService identityService = processEngine.getIdentityService();

	User user = identityService.newUser("3");
	user.setFirstName("李");
	user.setLastName("四");
	user.setEmail("abc@123.com");
	identityService.saveUser(user);

	// 设置用户信息
	identityService.setUserInfo("2", "age", "18");

	try {
		// 设置用户图片
		FileInputStream fis = new FileInputStream(Resources.getResourceAsFile("ask-for-leave.png"));
		BufferedImage img = ImageIO.read(fis);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(img, "png", output);
		byte[] pics = output.toByteArray();
		Picture picture = new Picture(pics, "ask-for-image");
		identityService.setUserPicture("3", picture);
	} catch (Exception e) {

	}
	
	System.out.println("test3-测试结束!");
}
```

结果

```
test3-测试结束!
```

## 分配用户到用户组

### (1) 添加组 添加用户 把用户分配到组

```java
public void test1() {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	IdentityService identityService = processEngine.getIdentityService();

	// 新增组
	String groupId = UUID.randomUUID().toString();
	Group group = identityService.newGroup(groupId);
	group.setName("经理");
	group.setType("管理员");
	identityService.saveGroup(group);

	// 新增用户
	String userId = UUID.randomUUID().toString();
	User user = identityService.newUser(userId);
	user.setLastName("王二麻子");
	identityService.saveUser(user);

	// 新增关系
	identityService.createMembership(userId, groupId);

	System.out.println("test1-测试结束!");
}
```

结果

```
test1-测试结束!
```

### (2) 删除有用户关系的用户组--级联删除

```java
public void test2() {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	IdentityService identityService = processEngine.getIdentityService();

	// 新增组
	String groupId = UUID.randomUUID().toString();
	Group group = identityService.newGroup(groupId);
	group.setName("经理");
	group.setType("管理员");
	identityService.saveGroup(group);

	// 新增用户
	String userId = UUID.randomUUID().toString();
	User user = identityService.newUser(userId);
	user.setLastName("王二麻子");
	identityService.saveUser(user);

	// 新增关系
	identityService.createMembership(userId, groupId);

	System.out.println("删除group前:");
	Group g = identityService.createGroupQuery().groupId(groupId).singleResult();
	User u = identityService.createUserQuery().userId(userId).singleResult();
	System.out.println(String.format("test2-group.name = %s", g.getName()));
	System.out.println(String.format("test2-user.name = %s", u.getLastName()));

	// 删除用户组
	identityService.deleteGroup(groupId);

	System.out.println("删除group后:");
	g = identityService.createGroupQuery().groupId(groupId).singleResult();
	u = identityService.createUserQuery().userId(userId).singleResult();
	if (g != null) {
		System.out.println(String.format("test2-group.name = %s", g.getName()));
	} else {
		System.out.println("test2-group为空");
	}

	if (u != null) {
		System.out.println(String.format("test2-user.name = %s", u.getLastName()));
	} else {
		System.out.println("test2-user为空");
	}

	System.out.println("test2-测试结束!");
}
```

结果

```
删除group前:
test2-group.name = 经理
test2-user.name = 王二麻子
删除group后:
test2-group为空
test2-user.name = 王二麻子
test2-测试结束!
```

### (3) 删除有用户组关系的用户--级联删除

```java
public void test3() {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	IdentityService identityService = processEngine.getIdentityService();

	// 新增组
	String groupId = UUID.randomUUID().toString();
	Group group = identityService.newGroup(groupId);
	group.setName("经理");
	group.setType("管理员");
	identityService.saveGroup(group);

	// 新增用户
	String userId = UUID.randomUUID().toString();
	User user = identityService.newUser(userId);
	user.setLastName("王二麻子");
	identityService.saveUser(user);

	// 新增关系
	identityService.createMembership(userId, groupId);

	System.out.println("删除user前:");
	Group g = identityService.createGroupQuery().groupId(groupId).singleResult();
	User u = identityService.createUserQuery().userId(userId).singleResult();
	System.out.println(String.format("test3-group.name = %s", g.getName()));
	System.out.println(String.format("test3-user.name = %s", u.getLastName()));

	// 删除用户组
	identityService.deleteUser(userId);

	System.out.println("删除user后:");
	g = identityService.createGroupQuery().groupId(groupId).singleResult();
	u = identityService.createUserQuery().userId(userId).singleResult();
	if (g != null) {
		System.out.println(String.format("test3-group.name = %s", g.getName()));
	} else {
		System.out.println("test3-group为空");
	}

	if (u != null) {
		System.out.println(String.format("test3-user.name = %s", u.getLastName()));
	} else {
		System.out.println("test3-user为空");
	}

	System.out.println("test3-测试结束!");
}
```

结果

```
删除user前:
test3-group.name = 经理
test3-user.name = 王二麻子
删除user后:
test3-group.name = 经理
test3-user为空
test3-测试结束!
```

### (4) 删除用户和用户组的关系

```java
public void test4() {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	IdentityService identityService = processEngine.getIdentityService();

	// 新增组
	String groupId = UUID.randomUUID().toString();
	Group group = identityService.newGroup(groupId);
	group.setName("经理");
	group.setType("管理员");
	identityService.saveGroup(group);

	// 新增用户
	String userId = UUID.randomUUID().toString();
	User user = identityService.newUser(userId);
	user.setLastName("王二麻子");
	identityService.saveUser(user);

	// 新增关系
	identityService.createMembership(userId, groupId);

	// 删除关系
	identityService.deleteMembership(userId, groupId);

	System.out.println("test4-测试结束!");
}
```

结果

```
test4-测试结束!
```

### (5) 查询用户组下所有用户

```java
public void test5() {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	IdentityService identityService = processEngine.getIdentityService();

	// 新增组1
	String groupId = UUID.randomUUID().toString();
	Group group = identityService.newGroup(groupId);
	group.setName("组1");
	group.setType("group1");
	identityService.saveGroup(group);

	// 新增用户1
	String userId = UUID.randomUUID().toString();
	User user = identityService.newUser(userId);
	user.setLastName("用户1");
	identityService.saveUser(user);

	// 用户1添加到组1
	identityService.createMembership(userId, groupId);

	// 新增用户2
	userId = UUID.randomUUID().toString();
	user = identityService.newUser(userId);
	user.setLastName("用户2");
	identityService.saveUser(user);

	// 用户2添加到组1
	identityService.createMembership(userId, groupId);

	// 新增用户3
	userId = UUID.randomUUID().toString();
	user = identityService.newUser(userId);
	user.setLastName("用户3");
	identityService.saveUser(user);

	// 用户3添加到组1
	identityService.createMembership(userId, groupId);

	// 查询组1下的所有用户
	List<User> list = identityService.createUserQuery().memberOfGroup(groupId).list();
	for (User u : list) {
		System.out.println("test5-" + u.getLastName());
	}

	System.out.println("test5-测试结束!");
}
```

结果

```
test5-用户2
test5-用户3
test5-用户1
test5-测试结束!
```

### (6) 查询用户分配的所有用户组

```java
public void test6() {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	IdentityService identityService = processEngine.getIdentityService();

	// 新增用户1
	String userId = UUID.randomUUID().toString();
	User user = identityService.newUser(userId);
	user.setLastName("用户1");
	identityService.saveUser(user);

	// 新增组1
	String groupId = UUID.randomUUID().toString();
	Group group = identityService.newGroup(groupId);
	group.setName("组1");
	group.setType("group1");
	identityService.saveGroup(group);

	// 用户1添加到组1
	identityService.createMembership(userId, groupId);
	
	groupId = UUID.randomUUID().toString();
	group = identityService.newGroup(groupId);
	group.setName("组2");
	group.setType("group2");
	identityService.saveGroup(group);

	// 用户1添加到组2
	identityService.createMembership(userId, groupId);
	
	groupId = UUID.randomUUID().toString();
	group = identityService.newGroup(groupId);
	group.setName("组3");
	group.setType("group3");
	identityService.saveGroup(group);

	// 用户1添加到组3
	identityService.createMembership(userId, groupId);

	// 查询组1下的所有用户
	List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
	for(Group g:groups) {
		System.out.println("test6-" + g.getName());
	}

	System.out.println("test6-测试结束!");
}
```

结果

```
test6-组2
test6-组1
test6-组3
test6-测试结束!
```




