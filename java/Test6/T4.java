package Test6;

import java.util.concurrent.*;

/**
 * @author by pepsi-wyl
 * @date 2022-01-19 21:58
 */
public class T4 {
}

/**
 * 三大方法
 */

// 单个线程
class SingleThreadExecutor_T {
    public static void main(String[] args) {
        // 单个线程  只有一个线程同时执行
        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " OK");
            });
        }
        pool.shutdown(); // 关闭线程池
    }
}

// 固定线程大小的线程池
class FixedThreadPool_T {
    public static void main(String[] args) {
        // 固定线程大小的线程池  最大支持同时执行的线程数
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " OK");
            });
        }
        pool.shutdown();  // 关闭线程池
    }
}

// 可伸缩
class CachedThreadPool_T {
    public static void main(String[] args) {
        // 可伸缩 随着线程的数量自由伸缩
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " OK");
            });
        }
        pool.shutdown();  // 关闭线程池
    }
}

/**
 * 七大参数
 */
// ThreadPoolExecutor 原生方法创建线程池
class ThreadPoolExecutor_T {
    public static void main(String[] args) {

        ExecutorService pool = new ThreadPoolExecutor(
                2, // 核心线程池大小                      有2个窗口办理
                5, // 最大核心线程池大小               一共有5个窗口 3个窗口没有开
                3,  // 保持连接时间                      办理业务的最大时间
                TimeUnit.SECONDS,  // 保持连接时间单位                 时间单位
                new LinkedBlockingDeque<>(3), // 阻塞队列     侯客厅
                Executors.defaultThreadFactory(),     // 线程工厂,创建线程
                new ThreadPoolExecutor.DiscardOldestPolicy()  // 拒绝策略     候车厅满 继续来人怎么办
        );

        // 最大承载  队列+最大核心线程池大小
        for (int i = 1; i <= 10; i++) {
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " OK");
            });
        }

        pool.shutdown();  // 关闭线程池

    }
}

/**
 * 四大竞争策略
 * new ThreadPoolExecutor.AbortPolicy()         丢掉任务 抛出异常
 * new ThreadPoolExecutor.CallerRunsPolicy()    哪里来的去哪里
 * new ThreadPoolExecutor.DiscardPolicy()       丢掉任务 不抛出异常
 * new ThreadPoolExecutor.DiscardOldestPolicy() 尝试和第一个竞争，失败则丢掉异常  不抛出异常
 */


// CPU 密集型  多核
// IO  密集型


