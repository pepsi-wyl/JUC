package Test7;

import lombok.SneakyThrows;

import java.io.Serializable;
import java.lang.reflect.Constructor;

/**
 * @author by pepsi-wyl
 * @date 2022-01-20 20:57
 */

public class T2 {

}

// 饿汉式 单例模式
class Hungry_T1 {

    // 耗费空间的资源  饿汉式 可能会浪费空间
    private byte[] data = new byte[1024 * 1024 * 4];

    private Hungry_T1() {
    }

    private final static Hungry_T1 HUNGRY_T1 = new Hungry_T1();

    public static Hungry_T1 getInstance() {  // 得到实例
        return HUNGRY_T1;
    }
}

// ---->耗费空间的资源  饿汉式 可能会浪费空间 ------>懒汉式

// 懒汉式 单例模式
class Hungry_T2 {

    private Hungry_T2() {
        System.out.println(Thread.currentThread().getName() + " OK");
    }

    private static Hungry_T2 HUNGRY_T2;

    public static Hungry_T2 getInstance() {   // 得到实例
        if (HUNGRY_T2 == null) {
            HUNGRY_T2 = new Hungry_T2();      // 在用的时候进行创建
        }
        return HUNGRY_T2;
    }

    // 并发下存在问题
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> getInstance(), String.valueOf(i + 1)).start();
        }
    }
}

// ------>getInstance并发下存在问题 可能产生多个对象(非单例)

// 懒汉式 单例模式
class Hungry_T3 {

    private Hungry_T3() {
        System.out.println(Thread.currentThread().getName() + " OK");
    }

    private static Hungry_T3 HUNGRY_T3;

    public static Hungry_T3 getInstance() {           // 得到实例
        // DCL 双重检测锁模式  懒汉式单例
        if (HUNGRY_T3 == null) {
            synchronized (Hungry_T3.class) {
                if (HUNGRY_T3 == null) {
                    HUNGRY_T3 = new Hungry_T3();      // 在用的时候进行创建
                }
            }
        }
        return HUNGRY_T3;
    }

    //DCL
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> getInstance(), String.valueOf(i + 1)).start();
        }
    }
}

/**
 * new对象 非原子性操作  指令重排
 * 1.分配内存空间
 * 2.执行构造方法，初始化对象
 * 3.把对象指向空间
 * 如果一个线程先指向空间 其他线程可能放回null
 */

// 懒汉式 单例模式
class Hungry_T4 implements Serializable {

    private volatile static Boolean pepsi_wyl = true;  // 加密变量------<<<<<<<<<<<<<<<
    private volatile static Hungry_T4 HUNGRY_T4;       // 防止指令重排

    /**
     * 防止反射操作 破坏单例模式
     */
    private Hungry_T4() {
        // 这里双重校验，也是防止两个线程拿到的都是true，而创建了两个实例
        if (pepsi_wyl) {
            synchronized (Hungry_T4.class) {
                if (pepsi_wyl) {
                    // 为第一次创建，将pepsi_wyl设置为true
                    pepsi_wyl = false;
                } else {
                    // pepsi_wyl为true，表示之前创建过了，需要抛出异常
                    throw new RuntimeException("单例对象创建重复异常");
                }
            }
        }
    }

    //DCL + volatile
    public static Hungry_T4 getInstance() {           // 得到实例
        // DCL 双重检测锁模式  懒汉式单例
        if (HUNGRY_T4 == null) {
            synchronized (Hungry_T4.class) {
                if (HUNGRY_T4 == null) {
                    HUNGRY_T4 = new Hungry_T4();      // 在用的时候进行创建
                }
            }
        }
        return HUNGRY_T4;
    }

    /** 单例对象实现了Serializable接口，通过重写readResolve()禁止程序通过深拷贝创建多个实例，达到破坏单例对象的目的 */
    private Object readResolve(){
        return Hungry_T4.class;
    }
}

// 懒汉式 单例模式
enum Hungry {

    HUNGRY;

    public static Hungry getInstance() {           // 得到实例
        return HUNGRY;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> System.out.println(getInstance().hashCode()), String.valueOf(i + 1)).start();
        }
    }

}
