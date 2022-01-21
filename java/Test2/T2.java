package Test2;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 11:40
 */

import lombok.*;

// 线程的停止
public class T2 {
    @SneakyThrows
    public static void main(String[] args) {
        ThreadInterrupt threadInterrupt = new ThreadInterrupt();
        Thread thread = new Thread(threadInterrupt);
        thread.start();
        Thread.sleep(100);
        thread.interrupt();
    }
}


class ThreadInterrupt implements Runnable {
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println(Thread.currentThread().getName());
        }
    }
}
