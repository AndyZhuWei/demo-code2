package cn.andy.demo.democode2.jmh;

import cn.andy.demo.democode2.DemoCode2Application;
import cn.andy.demo.democode2.queue.IQueue;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @Description 同步队列性能测试+SpringBoot集成
 * @Author zhuwei
 * @Date 2022/2/8 10:17
 */
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement( iterations = 3, time = 1)
@Fork(1)
@State(Scope.Group)
@SpringBootTest
public class SpringBootTestTest {

    private ConfigurableApplicationContext applicationContext;
    private IQueue arrayQueue;
    private IQueue linkedQueue;

    @Test
    public void test() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(SpringBootTestTest.class.getSimpleName())
                .result("SpringBootTestTest_result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

    @Setup
    public void init() {
        applicationContext = SpringApplication.run(DemoCode2Application.class);
        arrayQueue = applicationContext.getBean("arrayQueue",IQueue.class);
        linkedQueue = applicationContext.getBean("linkedQueue",IQueue.class);

    }

    @TearDown
    public void down() {
        applicationContext.close();
    }

    @Group("arrayQueue")
    @GroupThreads(2)
    @Benchmark
    public void arrayQueuePut() throws InterruptedException {
        arrayQueue.put(new Object());
    }

    @Group("arrayQueue")
    @GroupThreads(10)
    @Benchmark
    public Object arrayQueueGet() throws InterruptedException {
        return arrayQueue.take();
    }

    @Group("linkedQueue")
    @GroupThreads(2)
    @Benchmark
    public void linkedQueuePut() throws InterruptedException {
        linkedQueue.put(new Object());
    }

    @Group("linkedQueue")
    @GroupThreads(10)
    @Benchmark
    public Object linkedQueueGet() throws InterruptedException {
        return linkedQueue.take();
    }
}
