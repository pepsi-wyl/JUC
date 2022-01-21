package Test1;

/**
 * @author by pepsi-wyl
 * @date 2022-01-15 20:25
 */

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * 匿名内部类
 * 适用于创建启动线程次数较少的环境，书写更加简便
 */
public class T3 {
    public static void main(String[] args) {
        new Thread() {
            // 继承Thread类的方式 重新run方法
            @Override
            public void run() {
                System.out.println("匿名内部类继承Thread创建线程...");
            }
        }.start();
        new Thread(new Runnable() {
            // 实现Runnable的方式  实现run方法
            @Override
            public void run() {
                System.out.println("匿名内部类实现Runnable创建线程...");
            }
        }).start();

        Executors.newFixedThreadPool(1).submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println("匿名内部类实现Callable创建线程...");
                return null;
            }
        });

        /**
         * JDK8 Lambda表达式
         * 前提:函数式接口
         */
        new Thread(() -> System.out.println("lambda表达式实现Runnable创建线程...")).start();

        Executors.newFixedThreadPool(1).submit((Callable<Boolean>) () -> {
            System.out.println("lambda表达式实现Callable创建线程...");
            return null;
        });

    }
}
