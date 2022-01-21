package Test6;

import lombok.*;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * @author by pepsi-wyl
 * @date 2022-01-20 16:24
 */
public class T7 {
    public static void main(String[] args) {
        System.out.println(LongStream.rangeClosed(1L, 10_0000_0000L).parallel().reduce(0, (num1, num2) -> Long.sum(num1, num2)));
        System.out.println(LongStream.rangeClosed(1L, 10_0000_0000L).parallel().reduce(0, Long::sum));
    }

}


// ForkJoin
class ForkJoin_T extends RecursiveTask<Long> {

    private Long start;
    private Long end;

    private Long temp = 10000L;   // 临界值

    public ForkJoin_T(Long start, Long end) {
        this.start = start;
        this.end = end;
    }

    // 计算方法
    @Override
    protected Long compute() {
        if (end - start < temp) {
            Long sum = 0L;
            for (Long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        }

        // 递归
        Long middle = (start + end) / 2;                     // 中间值
        ForkJoin_T task1 = new ForkJoin_T(start, middle);    // 拆分任务，把任务压入线程队列
        ForkJoin_T task2 = new ForkJoin_T(middle + 1, end);  // 拆分任务，把任务压入线程队列
        task1.fork();                                        // 任务执行
        task2.fork();                                        // 任务执行
        return task1.join() + task2.join();                  // 得到结果
    }

    @SneakyThrows
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();  // 利用ForkJoinPool执行 任务
        ForkJoinTask<Long> submit = pool.submit(new ForkJoin_T(1L, 10_0000_0000L));  // 提交任务
        System.out.println(submit.get());         // 获取结果
    }

}


