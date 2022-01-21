package Test2;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 16:26
 */

public class T4 {
    public static void main(String[] args) {
        ThreadYield threadYield = new ThreadYield();
        Thread thread1 = new Thread(threadYield, "A");
        Thread thread2 = new Thread(threadYield, "B");
        thread1.start();
        thread2.start();
    }
}

//线程礼让 yield  礼让不一定成功(看CPU调度)
class ThreadYield implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " 开始执行!!!");
        Thread.yield();  //线程礼让
        System.out.println(Thread.currentThread().getName() + " 结束执行!!!");
    }
}
