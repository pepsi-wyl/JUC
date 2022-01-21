package Test5;

import lombok.SneakyThrows;
import lombok.Synchronized;

import java.util.concurrent.TimeUnit;

/**
 * @author by pepsi-wyl
 * @date 2022-01-18 17:14
 */
public class T7 {

    @SneakyThrows
    public static void main(String[] args) {

        PhoneStatic phoneStatic1 = new PhoneStatic();

        PhoneStatic phoneStatic2 = new PhoneStatic();

        new Thread(()->{
            phoneStatic1.sendMsg();
        }).start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(()->{
            phoneStatic2.call();
        }).start();

    }
}

class PhoneStatic {

    @SneakyThrows
    @Synchronized
    public static void sendMsg() {    //  static synchronized 锁为clazz对象
        TimeUnit.SECONDS.sleep(4);  // sleep不释放锁
        System.out.println("发短信");
    }

    @Synchronized
    public static void call() {      //  static synchronized   锁为clazz对象
        System.out.println("打电话");
    }

    public void hello() {      //  无锁
        System.out.println("你好");
    }

}
