package Test5;

/**
 * @author by pepsi-wyl
 * @date 2022-01-17 20:19
 */

// 企业 开发  减小耦合度
public class T1 {
    public static void main(String[] args) {
        TicketSynchronized ticket = new TicketSynchronized();

        new Thread(() -> {
            for (int i = 0; i < 20; i++) ticket.sale();
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 20; i++) ticket.sale();
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 20; i++) ticket.sale();
        }, "C").start();

    }
}

// 资源类
class TicketSynchronized {
    //属性
    private int number = 50;

    //方法
    public synchronized void sale() {
        if (number > 0) {
            System.out.println(Thread.currentThread().getName()+"买" + number-- + "  " + "剩余" + number);
        }
    }
}
