package Test2;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 17:19
 */

// 线程的优先级
public class T7 {
    public static void main(String[] args) {

        ThreadPriority threadPriority = new ThreadPriority();

        Thread thread1 = new Thread(threadPriority);
        Thread thread2 = new Thread(threadPriority);
        Thread thread3 = new Thread(threadPriority);
        Thread thread4 = new Thread(threadPriority);
        Thread thread5 = new Thread(threadPriority);

        /**
         * 设置线程优先级   需要在start之前设置
         * 优先级只是概率问题 不能绝对的顺序执行
         */

        // Thread.XXX
        thread1.setPriority(Thread.MIN_PRIORITY);   // 1
        thread2.setPriority(Thread.NORM_PRIORITY);  // 5
        thread3.setPriority(Thread.MAX_PRIORITY);   // 10

        // 数字
        thread4.setPriority(9);                     // 9

        // 线程系统默认优先级  5 NORM_PRIORITY
        System.out.println(Thread.currentThread().getPriority());

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

    }
}


// 线程的优先级  priority
class ThreadPriority implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " " + Thread.currentThread().getPriority());
    }
}