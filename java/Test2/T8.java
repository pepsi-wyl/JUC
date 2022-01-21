package Test2;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 17:36
 */

/**
 * 用户线程 main..... JVM必须等待用户线程执行完毕
 * 守护线程 gc......  JVM不必等待守护线程执行完毕
 */

// 守护线程
public class T8 {
    public static void main(String[] args) {
        Thread daemonThread = new Thread(new DaemonThread());
        Thread userThread = new Thread(new UserThread());
        daemonThread.setDaemon(true);   //设置线程为守护线程
        daemonThread.start();           //开启线程
        userThread.start();             //开启线程
    }
}

// 用户线程
class UserThread implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("userThread");
        }
    }
}

// 守护线程
class DaemonThread implements Runnable {
    @Override
    public void run() {
        while (true) {
            System.out.println("daemonThread");
        }
    }
}