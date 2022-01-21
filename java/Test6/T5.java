package Test6;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author by pepsi-wyl
 * @date 2022-01-20 11:50
 */

public class T5 {

    public static void main(String[] args) {
//        new Function<>()
    }
}

// Function 函数型接口
class Function_T {
    public static void main(String[] args) {

        Function<String, String> function = new Function<>() {
            @Override
            public String apply(String str) {
                return str;
            }
        };
        System.out.println(function.apply("String"));

        // Lambda 方式写法
        Function<String, String> functionLambda = str -> str;
        System.out.println(functionLambda.apply("StringLambda"));
    }
}

// Predicate 断定型接口
class Predicate_T {
    public static void main(String[] args) {
        Predicate<String> predicate = new Predicate<>() {
            @Override
            public boolean test(String str) {  // 判断字符串是否为空
                return str.isEmpty();
            }
        };
        System.out.println(predicate.test(""));

        // Lambda 方式写法
        Predicate<String> predicateLambda = str -> str.isEmpty();
        System.out.println(predicate.test(""));
    }
}

// Consumer 消费型接口
class Consumer_T {
    public static void main(String[] args) {
        Consumer<Integer> consumer = new Consumer<>() {
            @Override
            public void accept(Integer number) {
                System.out.println("消费" + number);
            }
        };
        consumer.accept(100);

        // lambda
        Consumer<Integer> consumerLambda = number -> System.out.println("消费" + number);
        consumerLambda.accept(1000);
    }
}

// Supplier 供给型接口
class Supplier_T {
    public static void main(String[] args) {
        Supplier<Integer> supplier = new Supplier<>() {
            @Override
            public Integer get() {
                return 1000;
            }
        };
        System.out.println(supplier.get());

        // lambda
        Supplier<Integer> supplierLambda = () -> 1000;
        System.out.println(supplierLambda.get());
    }
}



