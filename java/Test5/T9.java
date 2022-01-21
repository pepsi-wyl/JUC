package Test5;

import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author by pepsi-wyl
 * @date 2022-01-18 18:06
 */
public class T9 {

}

class List_T {
    @SneakyThrows
    public static void main(String[] args) {
        //java.util.ConcurrentModificationException 并发修改异常
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 5));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }
}

class ListCollections{
    @SneakyThrows
    public static void main(String[] args) {
        // 通过Collections.synchronizedXXX 将集合转化为线程安全的
        List<String> list = Collections.synchronizedList(new ArrayList<>());
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 5));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }
}

class CopyOnWriteArrayList_T{
    @SneakyThrows
    public static void main(String[] args) {
        // 采用CopyOnWriteArrayList 安全集合类
        // CopyOnWrite 写入时复制  COW 计算机程序设计领域的一种优化策略
        CopyOnWriteArrayList<Object> list = new CopyOnWriteArrayList<>();
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 5));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }
}

