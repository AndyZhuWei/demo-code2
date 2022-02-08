package cn.andy.demo.democode2.queue;

import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description TODO
 * @Author zhuwei
 * @Date 2022/2/8 10:14
 */
@Component("linkedQueue")
public class LinkedQueue implements IQueue {

    private static final LinkedBlockingQueue<Object> QUEUE = new LinkedBlockingQueue<>(100000);

    @Override
    public void put(Object o) throws InterruptedException {
        QUEUE.put(o);
    }

    @Override
    public Object take() throws InterruptedException {
        return QUEUE.take();
    }
}
