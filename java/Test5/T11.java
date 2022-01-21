package Test5;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author by pepsi-wyl
 * @date 2022-01-18 19:38
 */
public class T11 {

}

class Map_T{
    public static void main(String[] args) {
        //java.util.ConcurrentModificationException 并发修改异常
        HashMap<String, Object> map = new HashMap<>();
        for (int i = 1; i <= 100; i++) {
            new Thread(() -> {
                map.put(Thread.currentThread().getName(),UUID.randomUUID().toString().substring(0, 5));
                System.out.println(map);
            }, String.valueOf(i)).start();
        }
    }
}

class MapCollections{
    public static void main(String[] args) {
        // 通过Collections.synchronizedXXX 将集合转化为线程安全的
        Map<String, Object> map = Collections.synchronizedMap(new HashMap<>());
        for (int i = 1; i <= 100; i++) {
            new Thread(() -> {
                map.put(Thread.currentThread().getName(),UUID.randomUUID().toString().substring(0, 5));
                System.out.println(map);
            }, String.valueOf(i)).start();
        }
    }
}

class ConcurrentHashMap_T{
    public static void main(String[] args) {
        // ConcurrentHashMap 安全集合类
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        for (int i = 1; i <= 100; i++) {
            new Thread(() -> {
                map.put(Thread.currentThread().getName(),UUID.randomUUID().toString().substring(0, 5));
                System.out.println(map);
            }, String.valueOf(i)).start();
        }
    }
}
