package Test6;

import lombok.SneakyThrows;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author by pepsi-wyl
 * @date 2022-01-18 20:32
 */
public class T1 {
}

// CountDownLatch  减法计数器  -1
class CountDownLatch_T {
    @SneakyThrows
    public static void main(String[] args) {

        // 参数为要计数的数
        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 0; i < 3; i++) {
            final int temp = i + 1;  // 解决线程内部访问不到 i 变量的问题
            new Thread(() -> {
                System.out.println(temp + " Go Out");
                countDownLatch.countDown();   // 数量减一
            }, String.valueOf(i)).start();
        }

        countDownLatch.await();               // 等待计数器归零(await被唤醒)所有受其影响而等待的线程将会被激活
        // 该处阻塞----------------------------------------------------------------------------<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        System.out.println("关闭");   // 在计数器归零前等待
    }
}

// CyclicBarrier  栅栏
class CyclicBarrier_T {
    public static void main(String[] args) throws Exception {

        // 参数 线程的个数 和 Runnable参数(最后一个到达线程要做的任务)
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> System.out.println("全部进入"));

        for (int i = 1; i <= 3; i++) {   // 3个线程
            new Thread(() -> {
                try {

                    //当线程到达栅栏位置时将调用await方法
                    System.out.println(Thread.currentThread().getName() + "开始等待其他线程");
                    cyclicBarrier.await();                   // 开始等待

                    /**
                     *  调用await方法的线程告诉CyclicBarrier自己已经到达同步点，然后当前线程被阻塞。
                     *  直到parties个参与线程调用了await方法，CyclicBarrier同样提供带超时时间的await和不带超时时间的await方法：
                     */

                    System.out.println(Thread.currentThread().getName() + "开始执行");
                    Thread.sleep(1000);// 工作线程开始处理，这里用Thread.sleep()来模拟业务处理
                    System.out.println(Thread.currentThread().getName() + "执行完毕");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();
        }

        // 该处不阻塞----------------------------------------------------------------------------<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        System.out.println("关闭");

    }
}

/**
 * 并发限制 控制最大线程数
 */
// Semaphore 信号量
class Semaphore_T {
    public static void main(String[] args) {

        // 线程数量   模拟一共有几个车位
        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                try {

                    /**
                     * 获得 如果已经满 则等待
                     */
                    semaphore.acquire();  // 得到

                    System.out.println(Thread.currentThread().getName()+"抢到车位!!!");
                    TimeUnit.SECONDS.sleep(1);   // 模拟真实业务
                    System.out.println(Thread.currentThread().getName()+"离开车位!!!");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {

                    /**
                     * 释放，唤醒等待的线程
                     */
                    semaphore.release();  // 释放

                }
            },String.valueOf(i+1)).start();
        }

    }
}