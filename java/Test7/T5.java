package Test7;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author by pepsi-wyl
 * @date 2022-01-21 14:33
 */
public class T5 {

}

class SpinLock {

    private AtomicReference<Thread> atomicReference = new AtomicReference<Thread>();

    // 加锁
    @SneakyThrows
    public void myLock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "======>" + "myLock");
        // 自旋锁
        while (!atomicReference.compareAndSet(null, thread)) {  // 为true是
            System.out.println(thread.getName() + "自旋......");  //
            TimeUnit.SECONDS.sleep(1);
        }
    }

    // 解锁
    public void myUnLock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "======>" + "myUnLock");
        // 解自旋锁
        atomicReference.compareAndSet(thread, null);
    }

    @SneakyThrows
    public static void main(String[] args) {

        SpinLock lock = new SpinLock();

        new Thread(() -> {
            lock.myLock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.myUnLock();
            }
        }, "A").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            lock.myLock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.myUnLock();
            }
        }, "B").start();

    }
}
