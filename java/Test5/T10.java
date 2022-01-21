package Test5;

import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author by pepsi-wyl
 * @date 2022-01-18 19:27
 */
public class T10 {
}

class Set_T{
    public static void main(String[] args) {
        //java.util.ConcurrentModificationException 并发修改异常
        HashSet<String> set = new HashSet<>();
        for (int i = 1; i <= 100; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 5));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
    }
}

class SetCollections{
    public static void main(String[] args) {
        // 通过Collections.synchronizedXXX 将集合转化为线程安全的
        Set<String> set = Collections.synchronizedSet(new HashSet<String>());
        for (int i = 1; i <= 100; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 5));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
    }
}

class CopyOnWriteArraySet_T{
    @SneakyThrows
    public static void main(String[] args) {
        // 采用CopyOnWriteArraySet 安全集合类
        CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<>();
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 5));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
    }
}
