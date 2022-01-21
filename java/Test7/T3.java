package Test7;

/**
 * @author by pepsi-wyl
 * @date 2022-01-20 21:32
 */
public class T3 {
}

// 静态内部类
class Holder {

    private Holder() {
    }

    public static Holder getInstance() {
        return InnerClass.HOLDER;
    }

    public static class InnerClass {
        private static final Holder HOLDER = new Holder();
    }

}
