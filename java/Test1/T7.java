package Test1;

import java.util.concurrent.*;

/**
 * @author by pepsi-wyl
 * @date 2022-01-15 21:38
 */

public class T7 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(10);

        pool.execute(new Thread(new ThreadMethod()));               //Thread
        pool.execute(new RunnableMethod());                         //Runnable

        System.out.println(pool.submit(new CallableMethod()).get());  //Callable

        pool.execute(new Thread() {                                  //Thread
            @Override
            public void run() {
                System.out.println("匿名内部类继承Thread创建线程...");
            }
        });

        pool.execute(new Thread(new Runnable() {                     //Runnable
            @Override
            public void run() {
                System.out.println("匿名内部类实现Runnable创建线程...");
            }
        }));

        pool.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println("匿名内部类实现Callable创建线程...");
                return true;
            }
        });

        /**
         * JDK8 Lambda表达式
         */
        pool.execute(new Thread(() -> System.out.println("lamda表达式实现Runnable创建线程...")));

        pool.submit(() -> {
            System.out.println("lamda表达式实现Callable创建线程...");
            return true;
        });

    }
}


