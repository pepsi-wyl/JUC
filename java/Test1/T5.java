package Test1;

/**
 * @author by pepsi-wyl
 * @date 2022-01-15 20:29
 */

// 多个线程操作同一个对象
public class T5 {
    public static void main(String[] args) {
        // 实现runnable方式创建   天然的数据共享
        RunnableMethodCount runnableMethodCount = new RunnableMethodCount();
        new Thread(runnableMethodCount).start();
        new Thread(runnableMethodCount).start();

        // 继承thread方式创建
        new ThreadMethodCount().start();
        new ThreadMethodCount().start();
    }
}

// Thread方式创建
class ThreadMethodCount extends Thread {
    private static int countThread = 100;
    @Override
    public void run() {
        while (true) {
            if (countThread <= 0) break;
            System.out.println(Thread.currentThread().getName() + " " + countThread--);
        }
    }
}

// runnable方式创建线程
class RunnableMethodCount implements Runnable {
    private int countRunnable = 100;
    @Override
    public void run() {
        while (true) {
            if (countRunnable <= 0) break;
            System.out.println(Thread.currentThread().getName() + " " + countRunnable--);
        }
    }
}





