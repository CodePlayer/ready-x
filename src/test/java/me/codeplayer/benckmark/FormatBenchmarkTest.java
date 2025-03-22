package me.codeplayer.benckmark;

import java.util.concurrent.TimeUnit;

import me.codeplayer.util.EasyDate;
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
public class FormatBenchmarkTest {

	public static void main(String[] args) throws RunnerException {
		// 启动基准测试
		startBenchmark(FormatBenchmarkTest.class);
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

	/**
	 * 使用 StringBuilder 主要是在 JDK9+ 能够节省接近一半的内存
	 * 【JDK 8】 50755.632 ops/ms VS 61089.169 ops/ms => 性能降低了 ≈17%
	 * 【JDK 17】 79165.469 ops/ms VS 68021.803 ops/ms => 性能提高了 ≈16%
	 * 【JDK 21】 61123.802 ops/ms VS 69271.851 ops/ms => 性能降低了 ≈12%
	 * 【JDK 24】 64356.656 ops/ms VS 69623.470 ops/ms => 性能降低了 ≈8%
	 */
	@Benchmark
	public String toDateTimeString1() {
		return EasyDate.toDateTimeString(2025, 3, 21, 10, 29, 13);
	}

	@Benchmark
	public String toDateTimeString2() {
		return toDateTimeString(2025, 3, 21, 10, 29, 13);
	}

	/**
	 * 返回"yyyy-MM-dd HH:mm:ss"格式的字符串
	 */
	public static String toDateTimeString(int year, int month, int day, int hour, int minute, int second) {
		final char[] chars = "0000-00-00 00:00:00".toCharArray();
		formatNormalDateTime(chars, year, month, day, hour, minute, second);
		return new String(chars);
	}

	protected static void formatNormalDateTime(char[] chars, int year, int month, int day, int hour, int minute, int second) {
		formatNormalDate(chars, 0, year, month, day);
		formatNormalTime(chars, 11, hour, minute, second);
	}

	public static void formatNormalDate(char[] chars, final int offset, final int year, final int month, final int day) {
		fillNumberToChars(chars, year, offset, 4);
		fillNumberToChars(chars, month, offset + 5, 2);
		fillNumberToChars(chars, day, offset + 8, 2);
	}

	public static void formatNormalTime(char[] chars, final int offset, final int hour, final int minute, final int second) {
		fillNumberToChars(chars, hour, offset, 2);
		fillNumberToChars(chars, minute, offset + 3, 2);
		fillNumberToChars(chars, second, offset + 6, 2);
	}

	/**
	 * 将指定的数字设置到指定的字符数组中的指定索引处，从右向左依次填充，并最多填充指定的长度
	 *
	 * @param chars 指定的字符数组
	 * @param number 指定的数字
	 * @param start 指定的起始索引
	 * @param length 指定的长度
	 */
	public static void fillNumberToChars(char[] chars, int number, int start, int length) {
		int end = start + length;
		while (number > 0 && length-- > 0) {
			chars[--end] = (char) ('0' + (number % 10));
			number /= 10;
		}
	}

}