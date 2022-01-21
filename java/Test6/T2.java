package Test6;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author by pepsi-wyl
 * @date 2022-01-19 19:53
 */
public class T2 {
    public static void main(String[] args) {
    }
}

/**
 * 自定义缓存
 */
class Cache {

    // map集合
    private volatile Map<String, Object> map = new HashMap<>();

    // 写
    public void put(String key, Object value) {
        System.out.println(Thread.currentThread().getName() + "开始写");
        map.put(key, value);
        System.out.println(Thread.currentThread().getName() + "写完毕");
    }

    // 写
    public void get(String key) {
        System.out.println(Thread.currentThread().getName() + "开始读");
        map.get(key);
        System.out.println(Thread.currentThread().getName() + "写完写");
    }

    public static void main(String[] args) {
        Cache cache = new Cache();

        for (int i = 0; i < 10; i++) {
            final int temp = i;   // lambda 表达式访问不到i 需要final充当中间变量
            new Thread(() -> {
                cache.put(String.valueOf(temp + 1), temp + 1); // 写操作
            }, String.valueOf(i + 1)).start();
        }

        for (int i = 0; i < 10; i++) {
            final int temp = i;
            new Thread(() -> {
                cache.get(String.valueOf(temp + 1));           // 读操作
            }, String.valueOf(i + 1)).start();
        }
    }
}

class CacheLock {
    private volatile Map<String, Object> map = new HashMap<>();

    // 读写锁 更加细粒度的控制
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    // 写
    public void put(String key, Object value) {
        readWriteLock.writeLock().lock();       // 加写锁
        try {
            System.out.println(Thread.currentThread().getName() + "开始写");
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + "写完毕");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readWriteLock.writeLock().unlock(); // 写锁释放
        }
    }

    // 读
    public void get(String key) {
        readWriteLock.readLock().lock();        // 加读锁
        try {
            System.out.println(Thread.currentThread().getName() + "开始读");
            map.get(key);
            System.out.println(Thread.currentThread().getName() + "读完毕");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readLock().unlock();   // 读锁释放
        }
    }

    public static void main(String[] args) {
        CacheLock cacheLock = new CacheLock();

        // 十个线程 写
        for (int i = 0; i < 10; i++) {
            // lambda 表达式访问不到i 需要final充当中间变量
            final int temp = i;
            new Thread(() -> {
                cacheLock.put(String.valueOf(temp + 1), temp + 1);    // 写操作
            }, String.valueOf(i + 1)).start();
        }

        // 十个线程 读
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            new Thread(() -> {
                cacheLock.get(String.valueOf(temp + 1));              // 读操作
            }, String.valueOf(i + 1)).start();
        }
    }

}




