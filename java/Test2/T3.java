package Test2;

import lombok.SneakyThrows;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 11:58
 */

//线程休眠
public class T3 {

    public static void main(String[] args) {
        new Thread(new ThreadSleep()).start();  //开启线程
    }
}

class ThreadSleep implements Runnable{
    @SneakyThrows
    public static void main(String[] args) {
        TimeUnit.DAYS.sleep(1);          //天
        TimeUnit.HOURS.sleep(1);         //小时
        TimeUnit.MINUTES.sleep(1);       //分钟
        TimeUnit.SECONDS.sleep(1);       //秒
        TimeUnit.MILLISECONDS.sleep(1);  //毫秒
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true){
            System.out.println(new Date(System.currentTimeMillis()));
//            Thread.sleep(1000);
            TimeUnit.SECONDS.sleep(1);
        }
    }
}




