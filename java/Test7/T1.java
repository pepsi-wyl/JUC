package Test7;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author by pepsi-wyl
 * @date 2022-01-20 18:28
 */

// volatile_T 保证可见性
public class T1 {
    private static volatile int number = 0;



    @SneakyThrows
    public static void main(String[] args) {
        new Thread(() -> {
            while (number == 0) {

            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        number++;
        System.out.println(number);
    }
}

// 保证原子性
class volatile_T {


    // 原子类 保证原子性
    private volatile static AtomicInteger number = new AtomicInteger();

    // 加操作
    public static void add() {
        number.getAndIncrement();   // CAS
    }

    public static void main(String[] args) {

        for (int i = 1; i <= 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    add();
                }
            }).start();
        }

        while (Thread.activeCount() > 2) {
            Thread.yield();
        }

        System.out.println(number);

    }
}

// 原子类

