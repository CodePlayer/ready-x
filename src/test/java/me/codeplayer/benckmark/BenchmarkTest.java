package me.codeplayer.benckmark;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

import me.codeplayer.util.NumberX;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.Throughput) // 测试类型：吞吐量
// @Threads(16)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchmarkTest {

	public static void main(String[] args) throws RunnerException {
		// 启动基准测试
		Options opt = new OptionsBuilder()
				.include(BenchmarkTest.class.getSimpleName()) // 要导入的测试类
				.warmupIterations(5) // 预热 5 轮
				.measurementIterations(10) // 度量10轮
				.forks(1)
				.build();
		new Runner(opt).run(); // 执行测试
	}

	@Benchmark
	public void getInteger() {
		String str = "123456";
		Integer val = NumberX.getInteger(str, null);
	}

	@Benchmark
	public void getInteger0() {
		String str = "123456";
		Integer val = getInteger0(str, null);
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