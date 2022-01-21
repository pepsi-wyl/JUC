package Test5;

import lombok.SneakyThrows;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author by pepsi-wyl
 * @date 2022-01-17 21:04
 */

public class T4 {
    public static void main(String[] args) {

        DataLock data = new DataLock();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.increment();
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.decrement();
        }, "B").start();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.increment();
        }, "C").start();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.decrement();
        }, "D").start();

    }
}

/**
 * 等待 业务 通知
 */
// 资源类
class DataLock {
    private Integer number = 0;

    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    /**
     * condition.await();      等待
     * condition.signalAll();  唤醒全部
     */

    // +1
    @SneakyThrows
    public void increment() {
        lock.lock();
        try {
            while (number != 0) {
                //等待
                condition.await();
            }
            //业务
            number++;
            System.out.println(Thread.currentThread().getName() + "==>" + number);
            //通知
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // -1
    @SneakyThrows
    public void decrement() {
        lock.lock();
        try {
            while (number == 0) {
                //等待
                condition.await();
            }
            //业务
            number--;
            System.out.println(Thread.currentThread().getName() + "==>" + number);
            //通知
            condition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}

