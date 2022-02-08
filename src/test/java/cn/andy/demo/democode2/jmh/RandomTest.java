package cn.andy.demo.democode2.jmh;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @Description 我们都知道获取随机数可以使用Random，同时在官方文档中也强调Random虽然是线程安全的，
 * 但是如果在多线程的情况下，最好还是使用ThreadLocalRandom
 * 。那么，Random与ThreadLocalRandom在效率上相差多少呢？我们在实际使用过程中该如何选择呢？
 * @Author zhuwei
 * @Date 2022/2/7 18:17
 *
 * 从结果上看ThreadLocalRandom.current().nextInt()
 * 完胜，而且效率差别非常大。同时我们也没必要自己搞ThreadLocal来封装Random。
 * 因为JDK提供的ThreadLocalRandom.current()就已经是天花板了。
 *
 */
@BenchmarkMode({Mode.AverageTime,Mode.Throughput})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3,time = 1)
@Measurement(time = 1,iterations = 5)
@Fork(1)
@Threads(5)
@State(value=Scope.Benchmark)
@SpringBootTest
public class RandomTest {

    private final Random random = new Random();

    private final ThreadLocal<Random> randomThreadLocalHolder = ThreadLocal.withInitial(Random::new);

    @Test
    public void test() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(RandomTest.class.getSimpleName())
                .result("RandomTest_result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();

    }

    @Benchmark
    public int random() {
        return random.nextInt();
    }

    @Benchmark
    public int randomThreadLocalHolder() {
        return randomThreadLocalHolder.get().nextInt();
    }

    @Benchmark
    public int threadLocalRandom() {
        return ThreadLocalRandom.current().nextInt();
    }





}
