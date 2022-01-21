package Test1;

/**
 * @author by pepsi-wyl
 * @date 2022-01-15 20:10
 */

/**
 * 创建线程方式二 ： 实现Runnable接口
 * 可以避免OOP单继承局限性
 */

public class T2 {
    public static void main(String[] args) {
        //3.创建Thread对象传入RunnableMethod对象 并调用start方法开启线程
        new Thread(new RunnableMethod()).start();

        //注意:调用两次start方法将会抛出线程状态异常
    }
}

//1.实现Runnable接口
class RunnableMethod implements Runnable {
    //2.重写run方法
    @Override
    public void run() {
        // 线程逻辑代码
        System.out.println("RunnableMethod创建线程...");
    }
}



