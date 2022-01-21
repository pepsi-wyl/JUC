package Test3;

import lombok.SneakyThrows;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 20:57
 */

// lock锁的使用
public class T5 {
    public static void main(String[] args) {
        BuyTicketLock station = new BuyTicketLock();
        Thread thread1 = new Thread(station, "A");
        Thread thread2 = new Thread(station, "B");
        Thread thread3 = new Thread(station, "C");
        thread1.start();
        thread2.start();
        thread3.start();
    }
}

class BuyTicketLock implements Runnable {

    private int ticketNums = 10;  // 票数
    private Boolean flag = true;  // 标志位

    private ReentrantLock lock=new ReentrantLock();  // 定义可重入锁

    @SneakyThrows
    @Override
    public void run() {
        while (flag) {   // 线程标志位为true
            try {
                lock.lock();
                // 无票处理
                if (ticketNums <= 0) {
                    flag = false;
                    return;
                }
                //模拟买票延时
                Thread.sleep(100);
                //买票
                System.out.println(Thread.currentThread().getName() + " " + ticketNums--);
            }finally {
                lock.unlock();
            }
        }
    }

}
