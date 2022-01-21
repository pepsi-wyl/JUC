package Test5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author by pepsi-wyl
 * @date 2022-01-18 11:28
 */

public class T5 {
    public static void main(String[] args) {
        DataConcurrent data = new DataConcurrent();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) data.printA();
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) data.printB();
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) data.printC();
        }, "C").start();

    }
}


// 资源类 Concurrent精准通知线程唤醒
class DataConcurrent {

    private int number = 1;

    private Lock lock = new ReentrantLock();

    // 三个线程标记
    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();
    private Condition conditionC = lock.newCondition();

    public void printA() {
        lock.lock();
        try {
            // 等待
            while (number != 1) {
                conditionA.await();   // A 线程阻塞
            }
            // 业务
            number = 2;
            System.out.println(Thread.currentThread().getName());
            // 通知
            conditionB.signal();      // B 线程唤醒
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printB() {
        lock.lock();
        try {
            // 等待
            while (number != 2) {
                conditionB.await();   // B 线程阻塞
            }
            // 业务
            number = 3;
            System.out.println(Thread.currentThread().getName());
            // 通知
            conditionC.signal();     // C 线程唤醒
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printC() {
        lock.lock();
        try {
            // 等待
            while (number != 3) {
                conditionC.await();   // C 线程阻塞
            }
            // 业务
            number = 1;
            System.out.println(Thread.currentThread().getName());
            // 通知
            conditionA.signal();     // A 线程唤醒
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}