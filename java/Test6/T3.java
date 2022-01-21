package Test6;

import lombok.SneakyThrows;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author by pepsi-wyl
 * @date 2022-01-19 20:33
 */

public class T3 {
    public static void main(String[] args) {

    }
}

// 阻塞队列
class BlockingQueue_T {
    public static void main(String[] args) {
        t4();
    }

    //抛出异常  add remove element
    public static void t1() {

        // 队列的大小
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);

        System.out.println(blockingQueue.element());

        // add 抛出异常
        blockingQueue.add("A");
        blockingQueue.add("B");
        blockingQueue.add("C");
        blockingQueue.add("D");

        // remove  抛出异常
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());

    }

    //有返回值 不抛出异常  offer  poll peek
    public static void t2() {
        // 队列的大小
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);

        System.out.println(blockingQueue.peek());

        // offer 不抛出异常
        System.out.println(blockingQueue.offer("A"));
        System.out.println(blockingQueue.offer("B"));
        System.out.println(blockingQueue.offer("C"));
        System.out.println(blockingQueue.offer("D"));

        // poll  不抛出异常
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
    }

    // 等待阻塞
    @SneakyThrows
    public static void t3() {
        // 队列的大小
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);

        // put 等待
        blockingQueue.put("A");
        blockingQueue.put("B");
        blockingQueue.put("C");
        blockingQueue.put("D");

        // take 等待
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
    }

    // 超时等待
    @SneakyThrows
    public static void t4() {
        // 队列的大小
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);

        // put 等待
        System.out.println(blockingQueue.offer("A", 1, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("B", 1, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("C", 1, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("D", 1, TimeUnit.SECONDS));  // 等待1s 结束

        // take 等待
        System.out.println(blockingQueue.poll(1, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(1, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(1, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(1, TimeUnit.SECONDS));       // 等待1s 结束
    }

}

// 同步队列
class SynchronousQueue_T {
    public static void main(String[] args) {

        // 同步队列
        BlockingQueue<String> queue = new SynchronousQueue<>();

        new Thread(() -> {
            try {

                System.out.println(Thread.currentThread().getName()+"put 1");
                queue.put("1");
                TimeUnit.SECONDS.sleep(2);

                System.out.println(Thread.currentThread().getName()+"put 2");
                queue.put("2");
                TimeUnit.SECONDS.sleep(2);

                System.out.println(Thread.currentThread().getName()+"put 3");
                queue.put("3");
                TimeUnit.SECONDS.sleep(2);

            }catch (Exception e){
                e.printStackTrace();
            }
        },"A").start();

        new Thread(() -> {
            try {

                System.out.println(Thread.currentThread().getName()+"=>"+queue.take());
                System.out.println(Thread.currentThread().getName()+"=>"+queue.take());
                System.out.println(Thread.currentThread().getName()+"=>"+queue.take());

            }catch (Exception e){
                e.printStackTrace();
            }
        },"B").start();

    }
}



