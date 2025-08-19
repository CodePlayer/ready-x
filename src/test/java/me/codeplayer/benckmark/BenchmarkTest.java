package me.codeplayer.benckmark;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

import me.codeplayer.util.NumberX;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * 如果输出的结果是 JSON 文件，可以使用以下站点进行可视化：
 * <li>https://deepoove.com/jmh-visual-chart/</li>
 * <li>https://jmh.morethan.io/</li>
 */
@SuppressWarnings("UnusedReturnValue")
@BenchmarkMode(Mode.Throughput) // 测试类型：吞吐量
@Threads(4) // 每个进程中的测试线程数
@Warmup(iterations = 3) // 预热 5 轮（ 每轮默认执行 10s ）
@Measurement(iterations = 3) // 度量 10 轮（ 每轮默认执行 10s ）
@Fork(1) // 进行 fork 的次数。如果 fork 数是 2 的话，则 JMH 会 fork 出两个进程来进行测试。
// @State(value = Scope.Thread) // 多个线程是否共享实例：Benchmark=所有线程共享一个实例；Thread=每个线程一个实例（默认）；Group=同一个线程在同一个 group 里共享实例
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 统计结果的时间单位
public class BenchmarkTest {

	public static void main(String[] args) throws RunnerException {
		// 启动基准测试
		startBenchmark(BenchmarkTest.class);
	}

	/** 启动性能基准测试 */
	public static void startBenchmark(Class<?> clazz) throws RunnerException {
		// 启动基准测试
		Options opt = new OptionsBuilder()
				.include(clazz.getSimpleName()) // 要导入的测试类
				.resultFormat(ResultFormatType.JSON)
				.result("benchmark-result.json") // 在项目根目录下
				.build();
		new Runner(opt).run(); // 执行测试
	}

	@Benchmark
	public Integer getInteger() {
		String str = "123456";
		return NumberX.getInteger(str, null);
	}

	@Benchmark
	public Integer getInteger0() {
		String str = "123456";
		return getInteger0(str, null);
	}

	/**
	 * 以 Integer 形式返回指定的值
	 *
	 * @param value 指定的对象
	 * @param defaultIfEmpty 如果 <code>value</code> 为 null 或 空字符串，将默认返回该参数
	 * @throws NumberFormatException 如果无法转为 Integer 形式，将报错
	 * @throws IllegalArgumentException 如果 <code>value</code> 非 Number、CharSequence 类型，将报错
	 */
	public static Integer getInteger0(@Nullable Object value, @Nullable Integer defaultIfEmpty) {
		if (value == null) {
			return defaultIfEmpty;
		} else if (value instanceof Integer) {
			return (Integer) value;
		} else if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		if (value instanceof CharSequence) {
			final CharSequence cs = (CharSequence) value;
			if (cs.length() == 0) {
				return defaultIfEmpty;
			}
			return Integer.valueOf(value.toString());
		}
		throw new IllegalArgumentException("Unexpected number value:" + value);
	}

}