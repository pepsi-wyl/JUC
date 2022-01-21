package Test3;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 19:24
 */
public class T3 {
//    @SneakyThrows
//    public static void main(String[] args) {
//        ArrayList<String> list = new ArrayList<>();
//
//        for (int i = 0; i < 10000; i++) {
//            new Thread(() -> {
//                /**
//                 * 同步块
//                 */
//                synchronized (list){
//                    list.add(Thread.currentThread().getName());
//                }
//            }).start();
//        }
//
//        Thread.sleep(4000);  //
//        System.out.println(list.size());
//
//    }

    @SneakyThrows
    public static void main(String[] args) {

        // JUC 安全类型集合
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                list.add(Thread.currentThread().getName());
            }).start();
        }
        Thread.sleep(4000);  //
        System.out.println(list.size());
    }
}
