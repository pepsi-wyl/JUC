package Test5;

import lombok.SneakyThrows;
import lombok.Synchronized;

import java.util.concurrent.TimeUnit;

/**
 * @author by pepsi-wyl
 * @date 2022-01-18 16:55
 */

public class T6 {

    @SneakyThrows
    public static void main(String[] args) {

        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> {
            phone1.sendMsg();
        }).start();

//        new Thread(() -> {
//            phone2.call();
//        }).start();

        // 由于synchronized锁为phone对象 发送消息和打电话为同一把锁 ，sleep不会释放锁
        // 即使发送消息进程在sleep 打电话进程也无法进入
//        TimeUnit.SECONDS.sleep(1);
//        new Thread(() -> {
//            phone1.call();
//        }).start();
//
//        // hello 方法为我锁方法 不受锁得影响
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            phone1.hello();
        }).start();

    }
}

class Phone {

    @SneakyThrows
    @Synchronized
    public void sendMsg() {    //  synchronized 锁为this对象
        TimeUnit.SECONDS.sleep(4);  // sleep不释放锁
        System.out.println("发短信");
    }

    @Synchronized
    public void call() {      //  synchronized 锁为 this对象
        System.out.println("打电话");
    }

    public void hello() {      //  无锁
        System.out.println("你好");
    }

}
