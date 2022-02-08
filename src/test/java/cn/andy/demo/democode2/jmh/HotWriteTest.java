package cn.andy.demo.democode2.jmh;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Description 写热点测试
 * @Author zhuwei
 * @Date 2022/2/8 9:40
 */
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(time = 1,iterations = 3)
@Fork(1)
@Threads(10)
@State(value = Scope.Benchmark)
@SpringBootTest
public class HotWriteTest {

    private final LongAdder longAdder = new LongAdder();

    private final AtomicLong atomicLong = new AtomicLong();

    @Test
    public void test() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(HotWriteTest.class.getSimpleName())
                .result("HotWriteTest_result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

    @Benchmark
    public void longAdder() {
        longAdder.increment();
    }

    @Benchmark
    public void atomicLong() {
        atomicLong.incrementAndGet();
    }
}
