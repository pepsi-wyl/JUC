package Test5;

import lombok.Data;
import lombok.SneakyThrows;

/**
 * @author by pepsi-wyl
 * @date 2022-01-17 21:04
 */

public class T3 {
    public static void main(String[] args) {
        DataSynchronized data = new DataSynchronized();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.increment();
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.decrement();
        }, "B").start();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.increment();
        }, "C").start();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.decrement();
        }, "D").start();

        // 虚假唤醒
        /**
         * 假设：
         *       1、A抢到锁执行 ++                   1
         * 　　   2、A执行notify发现没有人wait，继续拿着锁执行 ，A判断不通过，A阻塞    1
         *  　　　3、B抢到锁 ，B判断不通过，B阻塞   　   1
         *
         * 　 　　4、C 抢到锁 执行--     　          　0
         * 　 　　5、C 执行Notify 唤醒A， A执行++      1 　　　
         * 　 　　6、A 执行notify唤醒B ，B执行++       2  （注意这个地方恰巧唤醒B，那么B 从哪阻塞的就从哪唤醒，B继续执行wait下面的++操作，导致出现2）
         */

    }
}

/**
 * 等待 业务 通知
 */
// 资源类
class DataSynchronized {
    private int number = 0;

    // +1
    @SneakyThrows
    public synchronized void increment() {
        while (number != 0) {
            //等待
            this.wait();
        }
        //业务
        number++;
        System.out.println(Thread.currentThread().getName() + "==>" + number);
        //通知
        this.notifyAll();
    }

    // -1
    @SneakyThrows
    public synchronized void decrement() {
        while (number == 0) {
            //等待
            this.wait();
        }
        //业务
        number--;
        System.out.println(Thread.currentThread().getName() + "==>" + number);
        //通知
        this.notifyAll();
    }

}

