package Test3;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 20:26
 */

import lombok.SneakyThrows;

public class T4 {
    public static void main(String[] args) {
        new MakeUp(0,"A").start();
        new MakeUp(1,"B").start();
    }
}

// 口红
class Lipstick {

}

// 镜子
class Mirror {

}

class MakeUp extends Thread {

    /**
     * 静态资源 只有一份
     */
    private static Lipstick lipstick = new Lipstick();
    private static Mirror mirror = new Mirror();

    private int chose;
    private String girlName;

    public MakeUp(int chose, String girlName) {
        this.chose = chose;
        this.girlName = girlName;
    }

    //化妆方法
    @SneakyThrows
    private void makeup() {
        if (chose == 0) {
            synchronized (lipstick) {
                System.out.println(this.girlName + "获得口红");
                Thread.sleep(1000);
                synchronized (mirror) {
                    System.out.println(this.girlName + "获得镜子");
                }
            }
        } else {
            synchronized (mirror) {
                System.out.println(this.girlName + "获得镜子");
                Thread.sleep(1000);
                synchronized (lipstick) {
                    System.out.println(this.girlName + "获得口红");
                }
            }
        }
//        if (chose == 0) {
//            synchronized (lipstick) {
//                System.out.println(this.girlName + "获得口红");
//                Thread.sleep(1000);
//            }
//            synchronized (mirror) {
//                System.out.println(this.girlName + "获得镜子");
//            }
//        } else {
//            synchronized (mirror) {
//                System.out.println(this.girlName + "获得镜子");
//                Thread.sleep(1000);
//            }
//            synchronized (lipstick) {
//                System.out.println(this.girlName + "获得口红");
//            }
//        }
    }

    @Override
    public void run() {
        makeup();
    }
}


