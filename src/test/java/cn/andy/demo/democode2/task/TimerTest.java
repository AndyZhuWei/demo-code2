package cn.andy.demo.democode2.task;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @Description Java 原生 Timer
 * @Author zhuwei
 * @Date 2022/2/25 14:37
 */
@SpringBootTest
public class TimerTest {


    @Test
    public void test() throws InterruptedException {
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println("task1");
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println("task2");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(task1, 0,1000);
        timer.schedule(task2, 0,1000);
        TimeUnit.SECONDS.sleep(10);
    }



}
