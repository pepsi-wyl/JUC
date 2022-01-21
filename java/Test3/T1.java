package Test3;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 18:20
 */

import lombok.SneakyThrows;

// 不安全案例  买票
public class T1 {
    public static void main(String[] args) {
        BuyTicket station = new BuyTicket();
        Thread thread1 = new Thread(station, "A");
        Thread thread2 = new Thread(station, "B");
        Thread thread3 = new Thread(station, "C");
        thread1.start();
        thread2.start();
        thread3.start();
    }
}

class BuyTicket implements Runnable {

    private int ticketNums = 10;  // 票数
    private Boolean flag = true;  // 标志位

    /**
     * synchronized 同步方法 this对象(当前对象)
     */
    //买票
    @SneakyThrows
    public synchronized void buy() {

        // 无票处理
        if (ticketNums <= 0) {
            flag = false;
            return;
        }

        //模拟买票延时
        Thread.sleep(100);

        //买票
        System.out.println(Thread.currentThread().getName() + " " + ticketNums--);

    }

    @Override
    public void run() {
        while (flag) {   // 线程标志位为true
            buy();
        }
    }

}
