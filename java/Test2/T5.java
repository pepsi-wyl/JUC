package Test2;

import lombok.SneakyThrows;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 16:33
 */
public class T5 {
    @SneakyThrows
    public static void main(String[] args) {
        ThreadJoin threadJoin = new ThreadJoin();
        Thread thread = new Thread(threadJoin);
        thread.start();
        for(int i=0;i<10;i++){
            if (i==5) thread.join();
            System.out.println(i);
        }
    }
}

// 线程插队 join 插队一定成功
class ThreadJoin implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("我是VIP......");
        }
    }
}
