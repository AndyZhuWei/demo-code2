//标识JMH进行Benchmark时所使用的模式
//Throughput：吞吐量。比如“1秒内可以执行多少次调用”，单位是ops/time
//AverageTime：每次调用的平均耗时。比如“每次调用平均耗时xxx毫秒”，单位是time/ops
//SampleTime：随机取样，最后输出取样结果的分布。比如“99%的调用在xxx毫秒内，99.99%的调用在xxx毫秒以内”
//SingleShotTime：只运行一次，往往同时设置warmup=0，一般用于测试冷启动的性能。
//上面的这些模式并不是只能使用某一个，这些模式是可以被组合使用的，比如
@BenchmarkMode({Mode.AverageTime})
//输出结果的时间单位
@OutputTimeUnit(TimeUnit.NANOSECONDS)
//程序预热所需要的一些参数，可以用在类或者方法上。由于JVM存在JIT机制，所以一般前几次的效率都可能会不太理想，所以需要让程序先预热一下再跑。
// 这样可以保证测试结果的准确性。参数如下：
//iterations：预热的次数，默认值是org.openjdk.jmh.runner.Defaults#WARMUP_ITERATIONS=5
//time：每次预热执行的时长，默认值是org.openjdk.jmh.runner.Defaults#WARMUP_TIME=10秒
//timeUnit：上面那个时长对应的单位类型，默认是秒
//batchSize：每个操作的基准方法调用次数，默认值是org.openjdk.jmh.runner.Defaults#WARMUP_BATCHSIZE=1。1就代表一次一次的调用，如果是2那就代表2次2次的调用。
@Warmup(time = 1,iterations =5)
//这个参数与@Warmup中的参数完全一样，只是@Warmup是用在预热上，预热结果不算入最终的结果中。而@Measurement是指实际测试时的参数。
@Measurement(iterations = 5, time = 1)
//默认值是org.openjdk.jmh.runner.Defaults#MEASUREMENT_FORKS=5，可以手动指定。@Fork
//中设置是多少，那jmh执行测试时就会创建多少个独立的进程来进行测试。但是需要注意的是，不管有多少个进程进行测试，他们都是串行的。当fork为0时，表示不需要进行fork。官方解释是这样的：
//
//JVMs are notoriously good at profile-guided optimizations. This is bad for benchmarks, because different tests can mix their profiles together, and then render the “uniformly bad” code for every test. Forking (running in a separate process) each test can help to evade this issue.
//JMH will fork the tests by default.
//
//翻译成人话就是说，首先因为JVM存在profile-guided optimizations
//的特性，但是这样的特性是不利于进行基准测试的。因为不同的测试方法会混在一起，最后会导致结果出现偏差。为了避免这样的偏差才有了@Fork的存在。关于这个偏差的问题可以参考官方的这个例子：code-tools/jmh: 2be2df7dbaf8 jmh-samples/src/main/java/org/openjdk/jmh/samples/JMHSample_12_Forking.java
//
//所以为了避免这样的问题，我们可以设置@Fork(1)这样每一个测试的方法都会跑在不同的jvm进程中，也就避免了相互影响。
@Fork(1)
//通过State可以指定一个对象的作用范围，jmh通过scope来进行实例化和共享操作。@State可以被继承使用，如果父类定义了该注解，子类则无需定义。
// 由于jmh可以进行多线程测试，所以不同的scope的隔离级别如下：
//Scope.Benchmark：全局共享，所有的测试线程共享同一个实例对象。可以用来测试有状态的实例在多线程下的性能。
//Scope.Group：同一个线程组内部的线程共享一个实例对象。
//Scope.Thread：每个线程获取到都是不一样的实例对象。
@State(value = Scope.Benchmark)
//每一个测试进程（JVM）中的线程数。
//@Threads(1)


与JUnit的区别
俗话说条条大路通罗马。JUnit解决是测试一条道路能不能通往罗马，而JMH是测试哪条通往罗马的道路最快。在我看来，JUnit更多的是功能测试，
而JMH是性能测试。这两个测试的不是一个方面。