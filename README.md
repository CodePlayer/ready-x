[![Java CI](https://img.shields.io/github/actions/workflow/status/CodePlayer/ready-x/ci.yaml?branch=master&logo=github&logoColor=white)](https://github.com/CodePlayer/ready-x/actions/workflows/ci.yaml)
[![Codecov](https://img.shields.io/codecov/c/github/CodePlayer/ready-x/master?logo=codecov&logoColor=white)](https://codecov.io/gh/CodePlayer/ready-x/branch/master)
[![Maven Central](https://img.shields.io/maven-central/v/me.codeplayer/ready-x?logo=apache-maven&logoColor=white)](https://mvnrepository.com/artifact/me.codeplayer/ready-x)
[![GitHub release](https://img.shields.io/github/v/release/CodePlayer/ready-x)](https://github.com/CodePlayer/ready-x/releases)
[![Java support](https://img.shields.io/badge/Java-8+-green?logo=java&logoColor=white)](https://openjdk.java.net/)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# 关于 Ready-X

> Ready-X，一个高性能的Java工具类库，致力于成为 Java 工具类库中的瑞士军刀 ！

Ready-X 是一个基于Java 8（ ***1.x*** 基于 Java 5） 编写的基础工具库，其中包含了常见的文本(字符串)、数组、集合、文件、商业计算、随机数、参数验证、日期等方面的处理工具类。

本工具库的定位与 Apache 的 commons-lang3 相似，但 Ready-X 并没有重复实现 commons-lang3 的大部分方法，**仅仅作为该工具库的补充**，建议在实际开发过程中将 common-lang3 和 ready-x 配合使用
> 由于历史原因，Ready-X 也重复实现了 common-lang3 的部分方法，因为 common-lang3 的部分方法在早期版本中的实现不够理想，例如：使用StringBuffer、没有按照更优的逻辑处理。

## 设计理念

### 1、命名多以 "X" 结尾

众所周知，在 Java 中，大多数工具类的命名一般以 "s" 或 "Util(s)" 结尾。

为了避免类名混淆，我们优先以 "X" 作为工具类的命名后缀。 "X" 是数学中充分无限可能的魔法变量，看起来也像是一把瑞士军刀。

### 2、极致性能

技术极客对于性能的追求是无止境的，我们致力于为日常开发提供性能更优的工具方法。

作为高频使用的底层基础工具类，Ready-X 尽可能在每个细节上保证最高运行性能和最低的内存占用。

可以参考如下示例：

**示例一：将多个用户的ID拼接为一个字符串，以","分隔**
```java
// ① 常规版本
public static String joinIds0(List<User> list) {
  final StringBuilder sb = new StringBuilder();
  for (User user : list) {
    if (sb.length() > 0) {
      sb.append(",");
    }
    sb.append(user.getId());
  }
  return sb.toString();
}

// ② 升级版本 1
public static String joinIds1(List<User> list) {
  final StringBuilder sb = new StringBuilder(list.size() * 8); // 初始化容量
  for (User user : list) {
    if (sb.length() > 0) {
      sb.append(",");
    }
    sb.append(user.getId());
  }
  return sb.toString();
}

// ③ 升级版本 2
public static String joinIds2(List<User> list) {
  final StringBuilder sb = new StringBuilder(list.size() * 8); // 初始化容量
  for (User user : list) {
    if (sb.length() > 0) {
      sb.append(",");
    }
    sb.append(user.getId().intValue()); // 避免循环中每次 Integer.toString() 生成中间字符串的开销
  }
  return sb.toString();
}

// ④ 直接使用封装好的工具方法（且是 null 安全的）
public static String joinIds(List<User> list) {
  return StringX.joinIntValue(list, User::getId, ",");
}
```

**示例二：将 List<User> 转为 Map<Integer, User>**（key是用户ID）

```java
// ① 常规版本
public static Map<Integer, User> toMap0(List<User> list) {
  if (list == null) {
    return new HashMap<>();
  }
  return list.stream().collect(Collectors.toMap(User::getId, Function.identity()));
}

// ② 升级版本 1
public static Map<Integer, User> toMap1(List<User> list) {
  Map<Integer, User> map = new HashMap<>();
  if (list != null) {
    for (User user : list) {
      map.put(user.getId(), user);
    }
  }
  return map;
}

// ③ 升级版本 2
public static Map<Integer, User> toMap2(List<User> list) {
  if (list == null) {
    return new HashMap<>();
  }
  Map<Integer, User> map = new HashMap<>(list.size(), 1F); // 预初始化容量，避免循环中途的扩容开销
  for (User user : list) {
    map.put(user.getId(), user);
  }
  return map;
}

// ④ 直接使用封装好的工具方法（并且是 null 安全的）
public static Map<Integer, User> toMap(List<User> list) {
  return CollectionX.toHashMap(list, User::getId);
}

```

**示例三：从缓存中获取指定用户的ID（没有就返回 null ）**
```java
// ① 常规版本
public static Integer mapUserId(String cacheKey) {
  return Optional.ofNullable(cache.get(cacheKey)).map(User::getId).orElse(null);
}

// ② 直接使用封装好的工具方法
public static Integer mapUserId(String cacheKey) {
  return X.map(cache.get(cacheKey), User::getId);
  // 如果没有就返回 0，可以使用如下代码，此外还有支持 else 懒加载的工具方法
  // return X.mapElse(cache.get(cacheKey), User::getId, 0);
}
```

### 3、对 Lambda 的使用保持理性和克制

Lambda（或 Stream API） 虽然好用，但是也要保持**适当**的理性和克制。

因为 Lambda 本质是一个匿名类，每调用一次 Lambda，本质上都是 `new` 一个匿名类实例。 比如，你写两个 `User::getId`，它们实际上是不相等（`==`）的。

使用 Lambda（或 Stream ），会带来一定的额外性能损耗。

虽然 JVM 在 逃逸分析、栈上分配 等方面为此做了不少优化，但是**在同等开发效率下**，能不增加 字节码、虚拟机、GC 负担的，就不必给它们添麻烦。

当然，如果确能减少代码量的，**该用还是得用**,，也不必纠结这一点性能开销，开发效率始终是优先的。

只是，如果代码量差不多的话，那就不必为了 Lambda 而 Lambda，要合理权衡开发效率和性能。

在日常开发中，使用预先封装的工具方法来替代大部分 Lambda，我们也可以实现相同甚至更少的代码量，并且还能获得相对更好的性能。

### 4、追求 99% 的测试覆盖率


### 5、同一大版本内严格向后兼容


### 6、代码注释完备

由于源代码中的每个方法都已经注明了非常详细的中文注释，因此暂不另行提供API文档。

如果你对工具库中的方法实现有更好的建议，欢迎及时反馈或自行修改并提交请求。

目前 Ready-X 中的工具方法可能并不完备，仅仅基于实际项目的需求驱动。如果其中没有你所需的常用方法，也欢迎提出建议，或自行提交新的方法实现。

本项目自 4.x 起拟采用 《约定式提交 1.0.0》来规范代码提交，详情参见：[https://www.conventionalcommits.org/zh-hans/v1.0.0/](https://www.conventionalcommits.org/zh-hans/v1.0.0/)。

## 使用
本工具类库已发布至 Maven 中央仓库，当前最新版本为：**3.11.4** ，你可以通过如下 Maven 依赖配置引入本库：

```xml
<dependency>
  <groupId>me.codeplayer</groupId>
  <artifactId>ready-x</artifactId>
  <version>3.11.4</version>
</dependency>
```