package Test4;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 21:09
 */

import lombok.*;

//生产者消费者模式
public class T1 {
    public static void main(String[] args) {
        SynContainer container = new SynContainer();
        new Productor(container).start();
        new Consumer(container).start();
    }
}


// 产品
@Data
@AllArgsConstructor
class Chicken {
    private int id;
}

// 生产者
class Productor extends Thread {
    SynContainer container;

    public Productor(SynContainer container) {
        this.container = container;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            container.push(new Chicken(i));
            System.out.println("生产了" + i + "只鸡!!!");
        }
    }
}

// 消费者
class Consumer extends Thread {
    SynContainer container;

    public Consumer(SynContainer container) {
        this.container = container;
    }

    @SneakyThrows
    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            System.out.println("消费了" + container.pop().getId() + "只鸡!!!");
            Thread.sleep(100);
        }
    }
}

// 缓存区
class SynContainer {

    // 容器的大小
    private Chicken[] chickens = new Chicken[10];
    private static int count = 0;

    // 生产者放入产品
    @SneakyThrows
    public synchronized void push(Chicken chicken) {
        // 容器满
        if (count == chickens.length) {
            //通知消费者消费 生产等待
            this.wait();
        }
        // 加入容器
        chickens[count++] = chicken;
        // 通知消费者消费
        this.notifyAll();
    }

    // 消费者消费产品
    @SneakyThrows
    public synchronized Chicken pop() {
        //容器为空
        if (count == 0) {
            // 通知生产者生产 消费等待
            this.wait();
        }
        // 从容器种取出
        Chicken chicken = chickens[--count];
        // 通知生产者者消费
        this.notifyAll();
        return chicken;
    }
}



