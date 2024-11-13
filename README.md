# 关于 ready-x
ready-x ***3.x*** 是一个基于Java 8（ ***1.x*** 基于 Java 5） 编写的基础工具库，其中包含了常见的文本(字符串)、数组、集合、文件、商业计算、随机数、参数验证、日期等方面的处理工具类。

该工具库的定位与 Apache 的 commons-lang3 相似，但 ready-x 并没有重复实现 commons-lang3 的大部分方法，仅仅作为该工具库的补充，建议在实际开发过程中将 common-lang3 和 ready-x 配合使用（由于历史原因，ready-x 也重复实现了 common-lang3 的部分方法，因为 common-lang3 的部分方法在早期版本中的实现不够理想，例如：使用StringBuffer、没有按照最优逻辑处理）。

由于源代码中的每个方法都已经注明了非常详细的中文注释，因此暂不另行提供API文档。

作为底层基础工具类，ready-x 尽可能在每个细节上保证最高运行性能和最低的内存占用。

如果你对工具库中的方法实现有更好的建议，欢迎及时反馈或自行修改并提交请求。

目前 ready-x 中的工具方法可能并不完备，仅仅基于实际项目的需求驱动。如果其中没有你所需的常用方法，也欢迎提出建议，或自行提交新的方法实现。

本项目自 4.x 起拟采用 《约定式提交 1.0.0》来规范代码提交，详情参见：[https://www.conventionalcommits.org/zh-hans/v1.0.0/](https://www.conventionalcommits.org/zh-hans/v1.0.0/)。

# 使用
本工具类库已发布至 Maven 中央仓库，当前最新版本为：**3.11.4** ，你可以通过如下 Maven 依赖配置引入本库：

```xml
<dependency>
	<groupId>me.codeplayer</groupId>
	<artifactId>ready-x</artifactId>
	<version>3.11.4</version>
</dependency>
```