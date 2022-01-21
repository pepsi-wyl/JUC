package Test1;

/**
 * @author by pepsi-wyl
 * @date 2022-01-15 18:42
 */

/**
 * 创建线程方式一 ： 继承Thread类
 * 不可以避免OOP单继承局限性
 */


public class T1 {
    public static void main(String[] args) {
        //3.创建ThreadMethod对象 调用其start方法开启线程
        new ThreadMethod().start();

        //注意: 调用run方法为new对象调用对象的方法 不能启动线程
        //new ThreadMethod().run();

        //注意:调用两次start方法将会抛出线程状态异常
    }
}

//1.继承Thread
class ThreadMethod extends Thread{
    //2.重写run方法
    @Override
    public void run() {
        // 线程逻辑代码
        System.out.println("ThreadMethod创建线程...");
    }
}
