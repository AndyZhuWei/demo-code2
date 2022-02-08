package cn.andy.demo.democode2.queue;

import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Description TODO
 * @Author zhuwei
 * @Date 2022/2/8 10:12
 */
@Component("arrayQueue")
public class ArrayQueue implements IQueue {

    private static final ArrayBlockingQueue<Object> QUEUE = new ArrayBlockingQueue<>(100000);


    @Override
    public void put(Object o) throws InterruptedException {
        QUEUE.put(o);
    }

    @Override
    public Object take() throws InterruptedException {
        return QUEUE.take();
    }
}
