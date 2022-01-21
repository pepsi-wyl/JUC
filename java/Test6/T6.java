package Test6;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * @author by pepsi-wyl
 * @date 2022-01-20 16:04
 */
public class T6 {
    public static void main(String[] args) {

        User u1 = new User(1, "a", 21);
        User u2 = new User(2, "b", 22);
        User u3 = new User(3, "c", 23);
        User u4 = new User(4, "d", 24);
        User u5 = new User(6, "e", 25);

        // 集合存储
        List<User> users = Arrays.asList(u1, u2, u3, u4, u5);

        // Stream流计算  链式编程
        users.stream()
                .filter(u -> u.getId() % 2 == 0)        // id 是偶数
                .filter(u -> u.getAge() > 23)           // age>23
                .map(u -> u.getName().toUpperCase(Locale.ROOT))  // name 转化为大写
                .sorted((Comparator.reverseOrder()))    // name 倒序排列
                .limit(1)                               // limit 实现分页
                .forEach(System.out::println);          // forEach 遍历输出

    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
class User {
    private int id;
    private String name;
    private int age;
}
