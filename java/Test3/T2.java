package Test3;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 18:30
 */

// 不安全案例  取钱
public class T2 {
    public static void main(String[] args) {
        Count count = new Count(100, "ID000001");
        new Drawing(count, "A", 50, 0).start();
        new Drawing(count, "B", 60, 0).start();
    }
}


@Data
@NoArgsConstructor
@AllArgsConstructor
class Count {
    private int money;
    private String name;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Drawing extends Thread {

    private Count count = null;  //共享的Count
    private String userName;
    private int drawingMoney;  //取钱数
    private int nowMoney;      //现在手里的钱

    @Override
    @SneakyThrows
    public void run() {
        /**
         * synchronized(obj){} 同步块  默认obj使用同步的资源对象
         */
        synchronized (count) {

            if (count.getMoney() - drawingMoney < 0) {
                System.out.println(this.getUserName() + " 余额不足!!!");
                return;
            }

            Thread.sleep(100);

            count.setMoney(count.getMoney() - this.getDrawingMoney());
            this.setNowMoney(this.getNowMoney() + this.getDrawingMoney());
            System.out.println(count.getName()+"余额：" + count.getMoney());
            System.out.println(userName + " 手里的钱:" + this.getNowMoney());

        }
    }
}
