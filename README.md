[![Java CI](https://img.shields.io/github/actions/workflow/status/CodePlayer/ready-x/ci.yaml?branch=master&logo=github&logoColor=white)](https://github.com/CodePlayer/ready-x/actions/workflows/ci.yaml)
[![Codecov](https://img.shields.io/codecov/c/github/CodePlayer/ready-x/master?logo=codecov&logoColor=white)](https://codecov.io/gh/CodePlayer/ready-x/branch/master)
[![Maven Central](https://img.shields.io/maven-central/v/me.codeplayer/ready-x?logo=apache-maven&logoColor=white)](https://mvnrepository.com/artifact/me.codeplayer/ready-x)
[![GitHub release](https://img.shields.io/github/v/release/CodePlayer/ready-x)](https://github.com/CodePlayer/ready-x/releases)
[![Java support](https://img.shields.io/badge/Java-8+-green?logo=java&logoColor=white)](https://openjdk.java.net/)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

 English | [中文](README_zh.md)

# About Ready-X

> Ready-X, a high-performance Java utility library, strives to be the Swiss Army knife of Java utility libraries!

Ready-X is a foundational utility library based on Java 8 (***1.x*** is based on Java 5), which includes common utilities for handling text (strings), arrays, collections, files, business calculations, random numbers, parameter validation, dates, and more.

This library is positioned similarly to Apache's commons-lang3, but Ready-X does not reimplement most of the methods in commons-lang3. **It serves merely as a supplement** to that library. We recommend using commons-lang3 and ready-x together in actual development.
> Due to historical reasons, Ready-X has also reimplemented some methods from commons-lang3, as some methods in early versions of commons-lang3 were not ideally implemented, such as using StringBuffer or not following optimal logic.

## Design Philosophy

### 1. Naming mostly ends with "X"

As we all know, in Java, most utility classes are typically named with "s" or "Util(s)" as a suffix.

To avoid class name confusion, we prefer to use "X" as the naming suffix for utility classes, such as: `StringX`, `NumberX`, `CollectionX`, `ArrayX`, `EnumX`, `FileX`, `JsonX`, `X`, etc.

> "X" is a magical variable full of infinite possibilities in mathematics. It's short and easy to type, has a three-dimensional look, and itself resembles a Swiss Army knife.

### 2. Ultimate Performance

Tech enthusiasts have an endless pursuit of performance, and we are committed to providing more performant utility methods for daily development.

As frequently used underlying foundational utility classes, Ready-X strives to ensure the highest runtime performance and lowest memory footprint in every detail.

You can refer to the following examples:

**Example 1: Join multiple user IDs into a string, separated by ","**
```java
// 1. Standard version
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

// 2. Upgraded version 1
public static String joinIds1(List<User> list) {
  final StringBuilder sb = new StringBuilder(list.size() * 8); // Initialize capacity
  for (User user : list) {
    if (sb.length() > 0) {
      sb.append(",");
    }
    sb.append(user.getId());
  }
  return sb.toString();
}

// 3. Upgraded version 2
public static String joinIds2(List<User> list) {
  final StringBuilder sb = new StringBuilder(list.size() * 8); // Initialize capacity
  for (User user : list) {
    if (sb.length() > 0) {
      sb.append(",");
    }
    sb.append(user.getId().intValue()); // Avoid the overhead of Integer.toString() generating intermediate strings in each loop
  }
  return sb.toString();
}

// 4. Directly use the encapsulated utility method (and it's null-safe)
public static String joinIds(List<User> list) {
  return StringX.joinIntValue(list, User::getId, ",");
}
```

**Example 2: Convert List<User> to Map<Integer, User>** (key is user ID)

```java
// 1. Standard version
public static Map<Integer, User> toMap0(List<User> list) {
  if (list == null) {
    return new HashMap<>();
  }
  return list.stream().collect(Collectors.toMap(User::getId, Function.identity()));
}

// 2. Upgraded version 1
public static Map<Integer, User> toMap1(List<User> list) {
  Map<Integer, User> map = new HashMap<>();
  if (list != null) {
    for (User user : list) {
      map.put(user.getId(), user);
    }
  }
  return map;
}

// 3. Upgraded version 2
public static Map<Integer, User> toMap2(List<User> list) {
  if (list == null) {
    return new HashMap<>();
  }
  Map<Integer, User> map = new HashMap<>(list.size(), 1F); // Pre-initialize capacity to avoid expansion overhead mid-loop
  for (User user : list) {
    map.put(user.getId(), user);
  }
  return map;
}

// 4. Directly use the encapsulated utility method (and it's null-safe)
public static Map<Integer, User> toMap(List<User> list) {
  return CollectionX.toHashMap(list, User::getId);
}

```

**Example 3: Detect if segment "a" exists in the string "a,b,c,d" (multiple segments separated by ",")**
```java
// 1. Standard version
public boolean contains0(String segments, String part) {
  String[] parts = segments.split(",");
  return Arrays.asList(parts).contains(part);
}

// 2. Upgraded version 1
public boolean contains1(String segments, String part) {
  String[] parts = segments.split(",");
  return org.apache.commons.lang3.ArrayUtils.contains(parts, part); // Avoid new ArrayList
}

// 3. Directly use the encapsulated utility method (and it's null-safe)
// No need to split first, directly through one-time indexOf + boundary character judgment, avoiding multiple iterations and generation of multiple intermediate string/collection objects
public boolean contains(String segments, String part) {
  return StringX.containsWord(segments, part, ",");
}
```

In addition, Ready-X **now supports the Java 9+ Multi-Release JAR Files mechanism** (see [JEP 238: Multi-Release JAR Files](https://openjdk.org/jeps/238) for details). If using JDK 9+, Ready-X will automatically leverage some newer APIs to improve performance.

For example, when you write code `List<Integer> ids = StringX.splitAsIntList("123,450,781", ',');`:

- In Java 8 scenarios, you typically need to `String.substring()` first, then `Integer.valueOf()`.
- In Java 9+ scenarios, Ready-X will automatically leverage the [new API](https://docs.oracle.com/javase/9/docs/api/java/lang/Integer.html#parseInt-java.lang.CharSequence-int-int-int-) `java.lang.Integer.parseInt(java.lang.CharSequence, int, int, int)`, to avoid the overhead of `String.substring()` generating new intermediate strings each time.

### 3. Rational and Restrained Use of Lambda

While Lambda (or Stream API) is useful, we should maintain **appropriate** rationality and restraint.

Because Lambda is essentially an anonymous class, each Lambda invocation essentially creates (`new`) an anonymous class instance. For example, if you write two `User::getId`, they are actually not equal (`==`).

Using Lambda (or Stream) incurs some additional performance overhead.

Although the JVM has made many optimizations in escape analysis, stack allocation, etc., **under equal development efficiency**, if we can avoid adding burden to bytecode, virtual machines, and GC, we shouldn't trouble them.

Of course, if it can indeed reduce code volume, **it should still be used**, and there's no need to worry about this performance overhead. Development efficiency is always the priority.

It's just that if the code volume is similar, there's no need to use Lambda for the sake of Lambda. We need to reasonably balance development efficiency and performance.

In daily development, using pre-encapsulated utility methods to replace most Lambdas, we can achieve the same or even less code volume, and also get relatively better performance.

```java
public Map<Long, User> toHashMap1(List<User> userList) {
    return userList.stream().collect(Collectors.toMap(User::getId, Function.identity()));
}

// Using the encapsulated utility method, not only is the code shorter, but it also achieves better performance in regular use
public Map<Long, User> toHashMap2(List<User> userList) {
    return CollectionX.toHashMap(userList, User::getId);
}


public List<Long> toUserIdList1(List<User> userList) {
	return userList.stream().map(User::getId).collect(Collectors.toList());
}

// Using the encapsulated utility method, not only is the code shorter, but it also achieves better performance in regular use
public List<Long> toUserIdList2(List<User> userList) {
	return CollectionX.toList(userList, User::getId);
}

public List<User> filterVIP1(List<User> userList) {
	return userList.stream().filter(User::isVIP).collect(Collectors.toList());
}

// Using the encapsulated utility method, not only is the code shorter, but it also achieves better performance in regular use
public List<User> filterVIP2(List<User> userList) {
    return CollectionX.filter(userList, User::isVIP);
}
		
public User findFirstVIP1(List<User> userList) {
	return userList.stream().filter(User::isVIP).findFirst().orElse(null);
}

// Using the encapsulated utility method, not only is the code shorter, but it also achieves better performance in regular use
public User findFirstVIP2(List<User> userList) {
	return CollectionX.findFirst(userList, User::isVIP);
}
```

### 4. Pursuing 80+% Test Coverage

1. Continuously improve test cases, striving to cover all branch code;
2. Each commit automatically performs **multi-system** (Windows, Ubuntu, Mac) x **multi-version** (JDK 8, 11, 17, 21, 25) cross unit testing, striving to cover more test scenarios.

### 5. Strict Backward Compatibility Within the Same Major Version

Follow semantic versioning conventions.

Taking `x.y.z` as an example:
- `x` changes indicate a major version update (**not necessarily backward compatible**);
- `y` changes indicate a minor version update, generally adding new features or characteristics, or undergoing major refactoring and optimization, but trying to ensure backward compatibility (if backward compatibility cannot be maintained due to bug fixes or adjustments to a few obscure APIs, explanations will be provided in the update);
- `z` changes indicate a patch update, generally bug fixes or internal code improvements.

> 【Note】
> - SNAPSHOT, ALPHA, BETA, RC, and other unofficial versions are not guaranteed to be backward compatible.
> - `3.x` and earlier versions did not strictly follow this specification.

### 6. Complete Code Comments

Since each method in the source code is already annotated with very detailed Chinese comments, separate API documentation is not currently provided.

If you have better suggestions for method implementations in the utility library, please feel free to provide feedback or modify and submit a pull request yourself.

Currently, the utility methods in Ready-X may not be complete, and are only driven by the needs of actual projects. If you need common methods that are not included, you are welcome to make suggestions or submit new method implementations yourself.

Starting from 4.x, this project intends to adopt "Conventional Commits 1.0.0" to standardize code commits. For details, see: [https://www.conventionalcommits.org/en/v1.0.0/](https://www.conventionalcommits.org/en/v1.0.0/).

## Usage
This utility library has been published to the Maven Central Repository. The current latest version is: **4.3.2**. You can include this library through the following Maven dependency configuration:

```xml
<dependency>
  <groupId>me.codeplayer</groupId>
  <artifactId>ready-x</artifactId>
  <version>4.3.2</version>
</dependency>
```

The latest version of the **3.x** branch is **3.18.2**. You can include this library through the following Maven dependency configuration:

```xml
<dependency>
  <groupId>me.codeplayer</groupId>
  <artifactId>ready-x</artifactId>
  <version>3.18.2</version>
</dependency>
```