package cn.andy.demo.democode2.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @Description
 * @Author zhuwei
 * @Date 2022/1/25 16:20
 */
@Slf4j
public class FutureAndCallableExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        //使用Callable,可以获取返回值
        Callable<String> callable = () -> {
            log.info("进入 Callable 的call方法");
            //模拟子线程任务，在此睡眠2s
            //小细节：由于call方法会抛出Exception，这里不用像Runnable的run方法那样try/catch了
            TimeUnit.SECONDS.sleep(5);
            return "Hello from Callable";
        };

        log.info("提交Callable到线程池");
        long startTime = System.nanoTime();
        Future<String> future = executorService.submit(callable);

        log.info("主线程继续执行");

        //1.演示future.get()阻塞的效果
//        log.info("主线程等待获取Future结果");
//        //Future.get() blocks until the result is available
//        String result = future.get();
//        log.info("主线程获取到Future结果:{}",result);

        //2.演示isDone()方法
        //如果子线程没有结束，则睡眠1s重新检查
//        while(!future.isDone()) {
//            log.info("Task is still not done...");
//            TimeUnit.SECONDS.sleep(1);
//        }
//        //此时会直接获取到
//        String result = future.get();
//        log.info("主线程获取到Future结果:{}",result);


//        //3.演示cancel()方法
//        while(!future.isDone()) {
//            log.info("子线程任务还没有结束...");
//            TimeUnit.SECONDS.sleep(1);
//
//            double elapsedTimeInSec = (System.nanoTime() - startTime)/ 1000000000.0;
//            //如果程序运行时间大于1s,则取消子线程得运行
//            if(elapsedTimeInSec > 1) {
//                future.cancel(true);
//            }
//        }
//        //此时调用时，如果取消了，就会抛出CancellationException
//        String result = future.get();



        //4.演示isCancelled()方法
        while(!future.isDone()) {
            log.info("子线程任务还没有结束...");
            TimeUnit.SECONDS.sleep(1);

            double elapsedTimeInSec = (System.nanoTime() - startTime)/ 1000000000.0;
            //如果程序运行时间大于1s,则取消子线程得运行
            if(elapsedTimeInSec > 1) {
                future.cancel(true);
            }
        }
        //通过isCancelled方法判断程序是否被取消，如果被取消，则打印日志，如果没有被取消，则正常调用get()方法
        if(!future.isCancelled()) {
            log.info("子线程任务已完成");
            String result = future.get();
            log.info("主线程获取到Future结果:{}",result);
        } else {
            log.warn("子线程任务被取消");
        }



        executorService.shutdown();

    }

}
