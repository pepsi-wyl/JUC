package Test2;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 11:18
 */

import lombok.*;

// 线程的停止
public class T1 {
    @SneakyThrows
    public static void main(String[] args) {
        ThreadStop threadStop = new ThreadStop();
        Thread thread = new Thread(threadStop);
        thread.start();
        Thread.sleep(100);
        threadStop.stop();
    }
}

class ThreadStop implements Runnable {
    // 设置线程标志位  volatile修饰符用来保证其它线程读取的总是该变量的最新的值
    private volatile Boolean flag = true;

    // 编写该线程的stop方法
    public void stop() {
        flag = false;
    }

    @Override
    public void run() {
        while (flag) {
            System.out.println(Thread.currentThread().getName());
        }
    }
}