package Test5;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author by pepsi-wyl
 * @date 2022-01-17 20:19
 */

// 企业 开发  减小耦合度
public class T2 {
    public static void main(String[] args) {
        TicketLock ticket = new TicketLock();

        new Thread(() -> {
            for (int i = 0; i < 20; i++) ticket.sale();
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 20; i++) ticket.sale();
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 20; i++) ticket.sale();
        }, "C").start();

    }
}

// 资源类
class TicketLock {
    //属性
    private int number = 50;

    // 公平锁true  非公平锁false 默认
    ReentrantLock lock = new ReentrantLock();

    //方法
    public void sale() {
        lock.lock(); //加锁
        try {
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + "买" + number-- + "  " + "剩余" + number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}

