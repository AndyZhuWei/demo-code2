package cn.andy.demo.democode2.time;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

/**
 * @Description TODO
 * @Author zhuwei
 * @Date 2021/12/29 17:19
 */
@SpringBootTest
public class TimeTests {

    //1.时间和日期
    @Test
    public void test() {
        LocalTime lt = LocalTime.now();
        System.out.println(lt);


    }
}
