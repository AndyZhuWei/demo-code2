package cn.andy.demo.democode2.thread;

/**
 * @Description Thread类中interrupt（）、interrupted（）和isInterrupted（）方法详解
 * @Author zhuwei
 * @Date 2022/1/28 15:51
 *
 *
 *最后总结，关于这三个方法，interrupt（）是给线程设置中断标志；interrupted（）是检测中断并清除中断状态；isInterrupted（）只检测中断。
 * 还有重要的一点就是interrupted（）作用于当前线程，interrupt（）和isInterrupted（）作用于此线程，即代码中调用此方法的实例所代表的线程。
 *
 *
 */
public class ThreadDemo {
    public static void main(String[] args) {
        MyThread thread = new MyThread();
        thread.start();

        //调用后仅仅是设置了中断标志，线程还会继续执行
        thread.interrupt();

        //isInterrupted()得到是否中断，不会清除中断标志。两次都会输出true
        System.out.println("第一次调用thread.isInterrupted():"+thread.isInterrupted());
        System.out.println("第二次调用thread.isInterrupted():"+thread.isInterrupted());
        //System.out.println("thread是否存活:"+thread.isAlive());


        //两次调用都会输出false,并不会是一个true,一个false.因为thread.interrupted()表示得当前线程，此处得当前线程是main,虽然是thread调用的形式
        System.out.println("第一次调用thread.interrupted():"+thread.interrupted());
        System.out.println("第二次调用thread.interrupted():"+thread.interrupted());
        System.out.println("thread是否存活:"+thread.isAlive());
    }

}


class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i <10 ;i++) {
            System.out.println("i="+(i+1));
        }
    }
}