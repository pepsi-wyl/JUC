package Test1;

import lombok.SneakyThrows;
import java.util.concurrent.*;

/**
 * @author by pepsi-wyl
 * @date 2022-01-15 21:21
 */

/**
 * 创建线程方式三 ： 实现Callable接口
 * 可以有返回值 也可以抛出异常
 */
public class T6 {
    @SneakyThrows
    public static void main(String[] args) {
//        //3.创建线程池
//        ExecutorService pool = Executors.newFixedThreadPool(10);
//        //4.调用submit方法
//        Future<Boolean> future = pool.submit(new CallableMethod());
//        //5.get()得到返回值
//        System.out.println(future.get());

        FutureTask<Boolean> booleanFutureTask = new FutureTask<>(new CallableMethod());
        new Thread(booleanFutureTask).start();
        new Thread(booleanFutureTask).start();

//        System.out.println(booleanFutureTask.get());
//        System.out.println(booleanFutureTask.get());


    }
}

//1.实现Callable接口
class CallableMethod implements Callable<Boolean>{
    //2.实现call方法
    @Override
    public Boolean call() throws Exception {
        // Callable 可以具有返回值
        System.out.println("CallableMethod创建线程...");
        return true;
    }
}
