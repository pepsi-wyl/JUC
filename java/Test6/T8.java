package Test6;

import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author by pepsi-wyl
 * @date 2022-01-20 17:21
 */
public class T8 {
}

// 异步调用 类似于ajax
class Future_T {

    @SneakyThrows
    public static void main(String[] args) {

        // 没有返回值  runAsync
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);  // 模拟真实业务
                System.out.println(Thread.currentThread().getName() + "runAsync=>Void");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("main 线程");
        future.get();     // 获取执行请求
        System.out.println("main 线程");


        // 有返回值  supplyAsync
        CompletableFuture<String> supplyFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);  // 模拟真实业务
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Thread.currentThread().getName() + "runAsync=>String";
        });

        System.out.println
                (
                        supplyFuture.
                                whenComplete((t, u) -> {
                                    System.out.println("t=>" + t);  // 正常的结果
                                    System.out.println("u=>" + u);  // 异常的信息
                                }).
                                exceptionally((e) -> {              // 异常的结果
                                    System.out.println(e.getMessage());
                                    return "error";
                                }).
                                get() // 得到结果
                );
    }
}
