package Test2;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 16:53
 */

// 观察测试线程的状态
public class T6 {
    @SneakyThrows
    public static void main(String[] args) {
        ThreadState threadState = new ThreadState();

        Thread thread = new Thread(threadState);
        System.out.println(thread.getState());  // NEW

        thread.start();
        System.out.println(thread.getState());  // RUNNABLE

        while (thread.getState()!=Thread.State.TERMINATED){  // TIMED_WAITING
            System.out.println(thread.getState());
        }

        System.out.println(thread.getState());  // TERMINATED

        thread.start();  // 线程结束之后不能再次启动
    }
}

class ThreadState implements Runnable{
    @SneakyThrows
    @Override
    public void run() {
        for(int i=0;i<5;i++){
            //睡1s
            TimeUnit.MICROSECONDS.sleep(1);
        }
    }
}