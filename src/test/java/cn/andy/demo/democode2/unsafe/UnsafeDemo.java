package cn.andy.demo.democode2.unsafe;

import lombok.Data;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @Description 使用sun.misc.Unsafe
 * @Author zhuwei
 * @Date 2022/1/28 15:15
 *
 *
 * 如果你尝试创建Unsafe类的实例，基于以下两种原因是不被允许的。
 *
 *     1）、Unsafe类的构造函数是私有的；
 *
 *     2）、虽然它有静态的getUnsafe()方法，但是如果你尝试调用Unsafe.getUnsafe()，会得到一个SecutiryException。这个类只有被JDK信任的类实例化。
 *
 *
 * sun.misc.Unsafe提供了可以随意查看及修改JVM中运行时的数据结构，尽管这些功能在JAVA开发本身是不适用的，
 * Unsafe是一个用于研究学习HotSpot虚拟机非常棒的工具，因为它不需要调用C++代码，或者需要创建即时分析的工具。
 *
 *
 *
 *
 */
public class UnsafeDemo {


    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        //1.突破限制创建实例
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);

        //This creats an instance of player class without any initialization
        Player p = (Player) unsafe.allocateInstance(Player.class);
        System.out.println(p.getAge()); //Print 0

        p.setAge(45);
        System.out.println(p.getAge());//Print 45


    }
}

@Data
class Player {
    private int age = 12;

    private Player() {
        this.age =  50;
    }
}
