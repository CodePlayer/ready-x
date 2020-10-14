# 3.0.1

* 修复 当目标文件存在，且`FileUtil.move*()`系列方法传入参数`ovveride = true`时， 无法正确移动并覆盖文件的 bug。
* 新增 `CollectionUtil.mapInitialCapacity(int, float)` 及其重载方法。
* 新增 `CollectionUtil.ofSet(IntFunction, Object[])` 和 `CollectionUtil.ofHashSet(Object[])` 方法。
* 新增 `CollectionUtil.of(Supplier, Object[])` 方法。
* 重命名 `CollectionUtil.of(IntFunction, Object[])` 为 `CollectionUtil.ofSize`。