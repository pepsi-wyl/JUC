package Test7;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author by pepsi-wyl
 * @date 2022-01-21 10:46
 */
public class T4 {

    public static void main(String[] args) {

        AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(1, 1);
        new Thread(() -> {
            System.out.println("A1=>" + atomicStampedReference.getStamp());  // 获得版本号

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(atomicStampedReference.compareAndSet
                    (1, 2, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1));
            System.out.println("A2=>" + atomicStampedReference.getStamp());
            System.out.println(atomicStampedReference.compareAndSet
                    (2, 1, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1));
            System.out.println("A3=>" + atomicStampedReference.getStamp());

        }, "A").start();

        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println("B1=>" + stamp);

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(atomicStampedReference.compareAndSet
                    (1, 20, stamp, stamp + 1));
            System.out.println("B2=>" + atomicStampedReference.getStamp());

        }, "A").start();

    }

}

// CAS CompareAndSet: 比较并交换
class CAS_T {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(2000);

        // compareAndSet(int expectedValue, int newValue)  期望 更新   如果与期望的值相同就更新否则就不更新

        // 期望的值为2000 就更新 并且返回更新成功与失败
        System.out.println(atomicInteger.compareAndSet(2000, 2021));

        atomicInteger.getAndIncrement();  // number++

    }


}
