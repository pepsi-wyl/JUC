package Test5;

import lombok.SneakyThrows;
import lombok.Synchronized;

import java.util.concurrent.TimeUnit;

/**
 * @author by pepsi-wyl
 * @date 2022-01-18 17:52
 */
public class T8 {
    public static void main(String[] args) {
        PhonePro phonePro1 = new PhonePro();
        PhonePro phonePro2 = new PhonePro();
        new Thread(()->{phonePro1.sendMsg();}).start();
        new Thread(()->{phonePro2.call();}).start();
    }
}

class PhonePro{

    @SneakyThrows
    @Synchronized
    public static void sendMsg() {    //  static synchronized 锁为clazz对象
        TimeUnit.SECONDS.sleep(4);  // sleep不释放锁
        System.out.println("发短信");
    }

    @Synchronized
    public void call() {      //   synchronized   锁为this对象
        System.out.println("打电话");
    }

    public void hello() {      //  无锁
        System.out.println("你好");
    }

}
