# Activiti 数据查询
---

服务组件与查询方法的映射

- XXXService----------createXXXQuery-------------Query

Query的方法

- asc
- desc
- count
- list
- listPage
- singleResult

## (1) 添加20条数据

```java
public void test0() {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	// 加载身份服务
	IdentityService identityService = engine.getIdentityService();

	// 添加5个组
	for (int i = 1; i <= 20; i++) {
		Group group = identityService.newGroup(String.format("%02d", i));
		group.setName(String.format("组%02d", i));
		group.setType(String.format("类型%02d", i));
		identityService.saveGroup(group);
	}

	if (engine != null) {
		engine.close();
	}
}
```

## (2) list方法

```java
public void test1() {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	// 加载身份服务
	IdentityService identityService = engine.getIdentityService();

	// 查询
	List<Group> groups = identityService.createGroupQuery().list();
	System.out.println("test1:");
	for (Group g : groups) {
		System.out.println(String.format("id = %s, name = %s, type = %s", g.getId(), g.getName(), g.getType()));
	}
}
```

结果

```
test1:
id = 01, name = 组01, type = 类型01
id = 02, name = 组02, type = 类型02
id = 03, name = 组03, type = 类型03
id = 04, name = 组04, type = 类型04
id = 05, name = 组05, type = 类型05
id = 06, name = 组06, type = 类型06
id = 07, name = 组07, type = 类型07
id = 08, name = 组08, type = 类型08
id = 09, name = 组09, type = 类型09
id = 10, name = 组10, type = 类型10
id = 11, name = 组11, type = 类型11
id = 12, name = 组12, type = 类型12
id = 13, name = 组13, type = 类型13
id = 14, name = 组14, type = 类型14
id = 15, name = 组15, type = 类型15
id = 16, name = 组16, type = 类型16
id = 17, name = 组17, type = 类型17
id = 18, name = 组18, type = 类型18
id = 19, name = 组19, type = 类型19
id = 20, name = 组20, type = 类型20
```

## (3) listPage方法

```java
public void test2() {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	// 加载身份服务
	IdentityService identityService = engine.getIdentityService();

	// 查询
	List<Group> groups = identityService.createGroupQuery().listPage(2, 6);
	System.out.println("test2:");
	for (Group g : groups) {
		System.out.println(String.format("id = %s, name = %s, type = %s", g.getId(), g.getName(), g.getType()));
	}
}
```

结果

```
test2:
id = 03, name = 组03, type = 类型03
id = 04, name = 组04, type = 类型04
id = 05, name = 组05, type = 类型05
id = 06, name = 组06, type = 类型06
id = 07, name = 组07, type = 类型07
id = 08, name = 组08, type = 类型08
```

## (4) count方法

```java
public void test3() {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	// 加载身份服务
	IdentityService identityService = engine.getIdentityService();

	// 查询
	long count = identityService.createGroupQuery().count();
	System.out.println("test3: count = " + count);		
}
```

结果

```
test3: count = 20
```

## (5) orderByXXX, asc, desc方法

```java
public void test4() {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	// 加载身份服务
	IdentityService identityService = engine.getIdentityService();

	// 查询
	System.out.println("test4:");
	List<Group> groups = identityService.createGroupQuery().orderByGroupName().asc().list();
	System.out.println("名称增序:");
	for (Group g : groups) {
		System.out.println(String.format("id = %s, name = %s, type = %s", g.getId(), g.getName(), g.getType()));
	}
	
	groups = identityService.createGroupQuery().orderByGroupType().desc().listPage(4, 5);
	System.out.println("类型分页减序:");
	for (Group g : groups) {
		System.out.println(String.format("id = %s, name = %s, type = %s", g.getId(), g.getName(), g.getType()));
	}
}
```

结果

```
test4:
名称增序:
id = 01, name = 组01, type = 类型01
id = 02, name = 组02, type = 类型02
id = 03, name = 组03, type = 类型03
id = 04, name = 组04, type = 类型04
id = 05, name = 组05, type = 类型05
id = 06, name = 组06, type = 类型06
id = 07, name = 组07, type = 类型07
id = 08, name = 组08, type = 类型08
id = 09, name = 组09, type = 类型09
id = 10, name = 组10, type = 类型10
id = 11, name = 组11, type = 类型11
id = 12, name = 组12, type = 类型12
id = 13, name = 组13, type = 类型13
id = 14, name = 组14, type = 类型14
id = 15, name = 组15, type = 类型15
id = 16, name = 组16, type = 类型16
id = 17, name = 组17, type = 类型17
id = 18, name = 组18, type = 类型18
id = 19, name = 组19, type = 类型19
id = 20, name = 组20, type = 类型20
类型分页减序:
id = 16, name = 组16, type = 类型16
id = 15, name = 组15, type = 类型15
id = 14, name = 组14, type = 类型14
id = 13, name = 组13, type = 类型13
id = 12, name = 组12, type = 类型12
```

## (6) 原生SQL语句

```java
public void test5() {
	// 加载引擎
	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	// 加载身份服务
	IdentityService identityService = engine.getIdentityService();

	// 查询
	List<Group> groups = identityService.createNativeGroupQuery()
			.sql("select * from act_id_group where name_=#{name}")
			.parameter("name", "组01").list();
	
	System.out.println("test5: ");
	for (Group g : groups) {
		System.out.println(String.format("id = %s, name = %s, type = %s", g.getId(), g.getName(), g.getType()));
	}
}
```

结果

```
test5: 
id = 01, name = 组01, type = 类型01
```