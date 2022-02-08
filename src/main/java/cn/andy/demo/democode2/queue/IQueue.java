package cn.andy.demo.democode2.queue;

public interface IQueue {
    void put(Object o) throws InterruptedException;

    Object take() throws InterruptedException;
}
