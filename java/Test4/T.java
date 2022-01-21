package Test4;

import lombok.*;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 22:23
 */

public class T {
    public static void main(String[] args) {
        TV tv = new TV();
        new Player(tv).start();
        new Watcher(tv).start();
    }
}

// 生产者--->演员
class Player extends Thread {
    TV tv;

    public Player(TV tv) {
        this.tv = tv;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            this.tv.play((i % 2 == 0) ? "节目A" : "节目B");
        }
    }
}

// 消费者--->观众
class Watcher extends Thread {
    TV tv;

    public Watcher(TV tv) {
        this.tv = tv;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            this.tv.watch();
        }
    }
}

// 产品--->节目
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
class TV {

    // 演员表演 观众等待 T
    // 观众观看 演员等待 F
    private Boolean flag = true;
    private String voice;   // 表演的节目

    // 表演
    @SneakyThrows
    public synchronized void play(String voice) {
        // 观众观看 演员等待 F
        if (!flag) {
            this.wait();
        }
        System.out.println("演员表演了" + voice);
        //通知观众观看
        this.voice = voice;
        this.notifyAll();
        this.flag = !this.flag;
    }

    // 观看
    @SneakyThrows
    public synchronized void watch() {
        // true 演员表演 观众等待
        if (flag) {
            this.wait();
        }
        System.out.println("观众观看了" + voice);
        //通知演员表演
        this.notifyAll();
        this.flag = !this.flag;
    }

}