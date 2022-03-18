# 线程基础
## 基础知识
### 线程由java开启？
开启线程的是底层C++语言(java无法直接操作硬件)
```java
public synchronized void start() {
      
        if (threadStatus != 0)
            throw new IllegalThreadStateException();

        group.add(this);

        boolean started = false;
        try {
            start0();
            started = true;
        } finally {
            try {
                if (!started) {
                    group.threadStartFailed(this);
                }
            } catch (Throwable ignore) {
                /* do nothing. If start0 threw a Throwable then
                  it will be passed up the call stack */
            }
        }
    }

    private native void start0();  // 底层C++代码
```
### 并发 并行
```java
并发（多线程操作同一个资源）
   CPU一核 快速交替
并行（多个人同时走）
   CPU多核 多个线程同时执行
   
public static void main(String[] args) {
   // 获取CPU的最大线程数
   System.out.println(Runtime.getRuntime().availableProcessors());
}

CPU密集型 IO密集型
```
## 创建线程
### 继承Thread
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-15 18:42
 */

//1.继承Thread
class ThreadMethod extends Thread{
    //2.重写run方法
    @Override
    public void run() {
        // 线程逻辑代码
        System.out.println("ThreadMethod创建线程...");
    }
}
```
```java
//3.创建ThreadMethod对象 调用其start方法开启线程
new ThreadMethod().start();
```
注意：

1. 调用run方法为new对象调用对象的方法 不能启动线程
1. 调用两次start方法将会抛出线程状态异常
### 实现Runnable接口
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-15 20:10
 */

//1.实现Runnable接口
class RunnableMethod implements Runnable {
    //2.重写run方法
    @Override
    public void run() {
        // 线程逻辑代码
        System.out.println("RunnableMethod创建线程...");
    }
}
```
```java
//3.创建Thread对象传入RunnableMethod对象 并调用start方法开启线程
new Thread(new RunnableMethod()).start();
```
注意：

1. 调用两次start方法将会抛出线程状态异常
### 实现Callable接口
可以有返回值 也可以抛出异常
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-15 21:21
 */

//1.实现Callable接口
class CallableMethod implements Callable<Boolean>{
    //2.实现call方法
    @Override
    public Boolean call() throws Exception {
        // Callable 可以具有返回值
        System.out.println("CallableMethod创建线程...");  // 存在缓存 
        return true;
    }
}
```
```java
FutureTask<Boolean> booleanFutureTask = new FutureTask<Boolean>(new CallableMethod());
new Thread(booleanFutureTask).start();
System.out.println(booleanFutureTask.get());
// booleanFutureTask.get()   会产生阻塞  异步操作 
```
```java
// 缓存测试
FutureTask<Boolean> booleanFutureTask = new FutureTask<>(new CallableMethod());
new Thread(booleanFutureTask).start();
new Thread(booleanFutureTask).start();
```
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642510167476-a56e47ba-d006-46ff-a7b7-7b5d4aa8a6e5.png#clientId=u127e53d3-0cd0-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=81&id=u8c83acdb&margin=%5Bobject%20Object%5D&name=image.png&originHeight=81&originWidth=334&originalType=binary&ratio=1&rotation=0&showTitle=false&size=41508&status=done&style=none&taskId=u53e0c3d3-a5b0-4c0f-be16-84a0b9177e2&title=&width=334)
### 匿名内部类
适用于创建启动线程次数较少的环境，书写更加简便
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-15 20:25
 */

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class T {
    public static void main(String[] args) {
        
        new Thread(){
            // 继承Thread类的方式 重新run方法
            @Override
            public void run() {
                System.out.println("匿名内部类继承Thread创建线程...");
            }
        }.start();
        
        new Thread(new Runnable() {
            // 实现Runnable的方式  实现run方法
            @Override
            public void run() {
                System.out.println("匿名内部类实现Runnable创建线程...");
            }
        }).start();
        
        Executors.newFixedThreadPool(1).submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println("匿名内部类实现Callable创建线程...");
                return null;
            }
        });
    
    }
}
```
### Lambda表达式
```java
/**
 * JDK8 Lambda表达式 语法糖
 */
new Thread(() -> System.out.println("lambda表达式实现Runnable创建线程...")).start();
        
Executors.newFixedThreadPool(1).submit((Callable<Boolean>) () -> {
     System.out.println("lambda表达式实现Callable创建线程...");
     return true;            //携带返回值
});
```
### 线程池技术
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-15 21:38
 */

import java.util.concurrent.*;

public class T {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        
        //创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(5);

        pool.execute(new Thread(new ThreadMethod()));                 // Thread
        pool.execute(new RunnableMethod());                           // Runnable
 
        System.out.println(pool.submit(new CallableMethod()).get());  // Callable
        
        pool.execute(new Thread() {                       // Thread
            @Override
            public void run() {
                System.out.println("匿名内部类继承Thread创建线程...");
            }
        });

        pool.execute(new Thread(new Runnable() {          // Runnable
            @Override
            public void run() {
                System.out.println("匿名内部类实现Runnable创建线程...");
            }
        }));

        pool.submit(new Callable<Boolean>() {              // Callable
            @Override
            public Boolean call() throws Exception {
                System.out.println("匿名内部类实现Callable创建线程...");
                return null;
            }
        });
        
        
        /**
         * JDK8 Lamda表达式
         */
        pool.execute(new Thread(() -> System.out.println("lambda表达式实现Runnable创建线程...")));

        pool.submit(() -> {
            System.out.println("lambda表达式实现Callable创建线程...");
            return true;
        });

    }
}
```
### Thread和Runnable
```java
/**
 * JDK 11 Thread类部分源码
 */

// Thread类 代理 Runnable接口 
class Thread implements Runnable {
    
    // 代理Runnable接口
    private Runnable target;

    // 构造方法
    public Thread() {
        this(null, null, "Thread-" + nextThreadNum(), 0);
    }
    
    public Thread(Runnable target) {
        this(null, target, "Thread-" + nextThreadNum(), 0);
    }
    
    // 调用Thread类的run方法       
    // 1.target不为空  调用Runnable的run
    // 2.target为空    调用继承Thread类的run方法
    @Override
    public void run() {
        if (target != null) {
            target.run();
        }
    }
}
  
```
### 例子
#### 例子1
用Thread和Runnable分别实现多线程图片下载
                                                                    导入依赖
```xml
   <dependencies>
        <!--commons-io操作工具-->
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.6</version>
        </dependency>
        <!--lombok-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>RELEASE</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>
```
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-15 18:42
 */

import lombok.*;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.net.URL;

public class T{
    public static void main(String[] args) {
        //3.ThreadMethodDown对象 调用其start方法开启线程
        new ThreadMethodDown("https://img-blog.csdnimg.cn/20201014180756919.png?x-oss-process=image/resize,m_fixed,h_64,w_64", "1.jpg").
                start();

        //3.创建Thread对象传入RunnableMethodDown对象 并调用start方法开启线程
        new Thread(new RunnableMethodDown("https://img-blog.csdnimg.cn/20201014180756919.png?x-oss-process=image/resize,m_fixed,h_64,w_64", "2.jpg")).
                start();
    }
}

/**
 * 下载器
 * 利用commons-io中的 copyURLToFile 从URL种下载图片到本地
 */
class webDownLoader {
    @SneakyThrows   // lombok 注解 用于异常捕获
    public static void downLoader(String url, String name) {
        FileUtils.copyURLToFile(new URL(url), new File(name));
    }
}

//1. 继承Thread类
class ThreadMethodDown extends Thread{

    /**
     * url 和 文件地址
     */
    private String url;
    private String name;

    // 构造器
    public ThreadMethodDown(String url, String name) {
        this.url = url;
        this.name = name;
    }

    //2.重写run方法
    @Override
    public void run() {
        webDownLoader.downLoader(url, name);
        System.out.println(name + "......");
    }
}

//1.实现Runnable接口
class RunnableMethodDown implements Runnable{
    
    /**
     * url 和 文件地址
     */
    private String url;
    private String name;

    // 构造器
    public RunnableMethodDown(String url, String name) {
        this.url = url;
        this.name = name;
    }

    //2.重写run方法
    @Override
    public void run() {
        webDownLoader.downLoader(url, name);
        System.out.println(name + "......");
    }
}
```
#### 例子2
用Thread和Runnable分别实现多线程对共享资源的操作
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-15 20:29
 */

public class T {
    public static void main(String[] args) {
        // 实现runnable方式创建   天然的数据共享
        RunnableMethodCount runnableMethodCount = new RunnableMethodCount();
        new Thread(runnableMethodCount).start();
        new Thread(runnableMethodCount).start();

        // 继承thread方式创建
        new ThreadMethodCount().start();
        new ThreadMethodCount().start();
    }
}

// Thread方式创建
class ThreadMethodCount extends Thread {
    private static int countThread = 100;
    @Override
    public void run() {
        while (true) {
            if (countThread <= 0) break;
            System.out.println(Thread.currentThread().getName() + " " + countThread--);
        }
    }
}

// runnable方式创建线程
class RunnableMethodCount implements Runnable {
    private int countRunnable = 100;
    @Override
    public void run() {
        while (true) {
            if (countRunnable <= 0) break;
            System.out.println(Thread.currentThread().getName() + " " + countRunnable--);
        }
    }
}
```
## 线程使用
### 线程停止
#### stop方法
弃用：@Deprecated(since="1.2")

1. 调用 stop() 方法会立刻停止 run() 方法中剩余的全部工作，包括在 catch 或 finally 语句中的，并抛出ThreadDeath异常(通常情况下此异常不需要显示的捕获)，因此可能会导致一些清理性的工作的得不到完成，如文件，数据库等的关闭。
1. 调用 stop() 方法会立即释放该线程所持有的所有的锁，导致数据得不到同步，出现数据不一致的问题。
#### 标志位终止线程
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 11:18
 */

class ThreadStop implements Runnable {
    
    // 设置线程标志位  
    // volatile修饰符用来保证其它线程读取的总是该变量的最新的值
    private volatile Boolean flag = true;

    // 编写该线程的stop方法
    public void stop() {
        flag = false;
    }

    @Override
    public void run() {
        while (flag) {
            System.out.println(Thread.currentThread().getName());
        }
    }
    
}
```
```java
@SneakyThrows
public static void main(String[] args) {
    ThreadStop threadStop = new ThreadStop();
    Thread thread = new Thread(threadStop);
    thread.start();
    Thread.sleep(100);
    threadStop.stop();
}
```
#### Interrupt方法
```java
// API 
public boolean Thread.isInterrupted()       //判断是否被中断
public static boolean Thread.interrupted()  //判断是否被中断，并清除当前中断状态
```
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 11:40
 */

class ThreadInterrupt implements Runnable {
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println(Thread.currentThread().getName());
        }
    }
}
```
```java

@SneakyThrows
public static void main(String[] args) {
    ThreadInterrupt threadInterrupt = new ThreadInterrupt();
    Thread thread = new Thread(threadInterrupt);
    thread.start();
    Thread.sleep(100);
    thread.interrupt();
}


```
### 线程休眠
#### sleep   不会释放锁
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 11:58
 */

class ThreadSleep implements Runnable{
    @SneakyThrows
    @Override
    public void run() {
        while (true){
            System.out.println(new Date(System.currentTimeMillis()));
            Thread.sleep(1000);
        }
    }
}
```
```java
public static void main(String[] args) {
     new Thread(new ThreadSleep()).start();  //开启线程
}
```
#### TimeUnit (企业常用)
注意：TimeUnit (企业常用)
           TimeUnit中封装了Thread.sleep方法 (快速完成时间转化)
```java
public void sleep(long timeout) throws InterruptedException {
    if (timeout > 0) {
        long ms = toMillis(timeout);
        int ns = excessNanos(timeout, ms);
        Thread.sleep(ms, ns);
    }
}
```
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 11:58
 */

class ThreadSleep implements Runnable{
    
    @SneakyThrows
    public static void main(String[] args) {
        TimeUnit.DAYS.sleep(1);          //天
        TimeUnit.HOURS.sleep(1);         //小时
        TimeUnit.MINUTES.sleep(1);       //分钟
        TimeUnit.SECONDS.sleep(1);       //秒
        TimeUnit.MILLISECONDS.sleep(1);  //毫秒
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true){
            System.out.println(new Date(System.currentTimeMillis()));
            TimeUnit.SECONDS.sleep(1);
        }
    }
    
}
```
```java
public static void main(String[] args) {
     new Thread(new ThreadSleep()).start();  //开启线程
}
```
#### wait/sleep
   区别

- wait 来自 Object  
- sleep 来自 Thread

- wait会释放锁
- sleep不会释放锁

- wait 需要等待
- sleep 任何时候都可以睡
### 线程礼让
#### yield
线程礼让 yield  礼让不一定成功(看CPU调度)
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 16:26
 */

class ThreadYield implements Runnable{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+" 开始执行!!!");
        Thread.yield();  //线程礼让
        System.out.println(Thread.currentThread().getName()+" 结束执行!!!");
    }
}
```
```java
public static void main(String[] args) {
     ThreadYield threadYield = new ThreadYield();
     Thread thread1 = new Thread(threadYield, "A");
     Thread thread2 = new Thread(threadYield, "B");
     thread1.start();
     thread2.start();
}
```
### 线程插队
#### join
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 16:33
 */

class ThreadJoin implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("我是VIP......");
        }
    }
}
```
```java
@SneakyThrows
public static void main(String[] args) {
    ThreadJoin threadJoin = new ThreadJoin();
    Thread thread = new Thread(threadJoin);
    thread.start();
    for(int i=0;i<10;i++){
        if (i==5) thread.join();
        System.out.println(i);
    }
}
```
### 
### 线程优先级
#### priority
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 17:19
 */

// 线程的优先级  priority
class ThreadPriority implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " " + Thread.currentThread().getPriority());
    }
}
```
```java
public static void main(String[] args) {

     ThreadPriority threadPriority = new ThreadPriority();

     Thread thread1 = new Thread(threadPriority);
     Thread thread2 = new Thread(threadPriority);
     Thread thread3 = new Thread(threadPriority);
     Thread thread4 = new Thread(threadPriority);
     Thread thread5 = new Thread(threadPriority);

     // Thread.XXX
     thread1.setPriority(Thread.MIN_PRIORITY);   // 1
     thread2.setPriority(Thread.NORM_PRIORITY);  // 5
     thread3.setPriority(Thread.MAX_PRIORITY);   // 10

     // 数字
     thread4.setPriority(9);                     // 9

     // 线程系统默认优先级  5 NORM_PRIORITY
     System.out.println(Thread.currentThread().getPriority());

     thread1.start();
     thread2.start();
     thread3.start();
     thread4.start();
     thread5.start();
```
注意： 

- 设置线程优先级   需要在start之前设置
- 优先级只是概率问题 不能绝对的顺序执行
### 用户线程和守护线程
#### 基本理解
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 17:36
 */

// 用户线程
class UserThread implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("userThread");
        }
    }
}

// 守护线程
class DaemonThread implements Runnable {
    @Override
    public void run() {
        while (true) {
            System.out.println("daemonThread");
        }
    }
}
```
```java
public static void main(String[] args) {
    Thread daemonThread = new Thread(new DaemonThread());
    Thread userThread = new Thread(new UserThread());
    daemonThread.setDaemon(true);   //设置线程为守护线程
    daemonThread.start();           //开启线程
    userThread.start();             //开启线程
}
```
注意：

- 用户线程 main.....  JVM必须等待用户线程执行完毕
- 守护线程 gc......     JVM不必等待守护线程执行完毕
### 线程状态
#### 6种
```java
public enum State {
    /**
     * 新生
     */
    NEW,

    /**
     * 运行
     */
    RUNNABLE,

    /**
     * 阻塞
     */
    BLOCKED,

    /**
     * 等待
     */
     WAITING,

    /**
     * 超时等待
     */
     TIMED_WAITING,

    /**
     * 终止
     */
     TERMINATED;
}
```
#### state
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 16:53
 */

class ThreadState implements Runnable{
    @SneakyThrows
    @Override
    public void run() {
        for(int i=0;i<5;i++){
            //睡1s
            TimeUnit.MICROSECONDS.sleep(1);
        }
    }
}
```
```java
@SneakyThrows
    public static void main(String[] args) {
        ThreadState threadState = new ThreadState();

        Thread thread = new Thread(threadState);
        System.out.println(thread.getState());  // NEW

        thread.start();
        System.out.println(thread.getState());  // RUNNABLE

        while (thread.getState()!=Thread.State.TERMINATED){  // TIMED_WAITING
            System.out.println(thread.getState());
        }

        System.out.println(thread.getState());  // TERMINATED

        thread.start();  // 线程结束之后不能再次启动
    }
```
注意: 线程结束之后不能再次启动
## 线程同步

- synchronized             方法 默认为this对象
- synchronized(obj){ }   块    通常使用共享资源对象
### 线程不安全实例修改为线程安全
#### 例一 
```java
import lombok.SneakyThrows;
import java.util.ArrayList;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 19:24
 */
public class T {
    @SneakyThrows
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        
        for (int i = 0; i < 10000; i++) {
            // 多个线程操作ArrayList 线程不安全
            new Thread(() -> {
                list.add(Thread.currentThread().getName());
            }).start();
        }

        Thread.sleep(4000);  //

        System.out.println(list.size());
    }
}
```
```java
import lombok.SneakyThrows;
import java.util.ArrayList;

/**
 * @author by pepsi-wyl
 * @date 2022-01-16 19:24
 */
public class T3 {
    @SneakyThrows
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                /**
                 * 同步块
                 */
                synchronized (list){
                    list.add(Thread.currentThread().getName());
                }
            }).start();
        }

        Thread.sleep(4000);  //
        System.out.println(list.size());

    }
}
```

#### 例二
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 18:20
 */

import lombok.SneakyThrows;

class BuyTicket implements Runnable {

    private int ticketNums = 10;  // 票数
    private Boolean flag = true;  // 标志位

    //买票
    @SneakyThrows
    public void buy() {

        // 无票处理
        if (ticketNums <= 0) {
            flag = false;
            return;
        }

        //模拟买票延时
        Thread.sleep(100);

        //买票
        System.out.println(Thread.currentThread().getName() + " " + ticketNums--);

    }

    @Override
    public void run() {
        while (flag) {   // 线程标志位为true
            buy();
        }
    }
    
}


public class T {
    public static void main(String[] args) {
        BuyTicket station = new BuyTicket();
        Thread thread1 = new Thread(station, "A");
        Thread thread2 = new Thread(station, "B");
        Thread thread3 = new Thread(station, "C");
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
```
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 18:20
 */

import lombok.SneakyThrows;

class BuyTicket implements Runnable {

    private int ticketNums = 10;  // 票数
    private Boolean flag = true;  // 标志位

    /**
     * synchronized 同步方法 this对象(当前对象)
     */
    //买票
    @SneakyThrows
    public synchronized void buy() {

        // 无票处理
        if (ticketNums <= 0) {
            flag = false;
            return;
        }

        //模拟买票延时
        Thread.sleep(100);

        //买票
        System.out.println(Thread.currentThread().getName() + " " + ticketNums--);

    }

    @Override
    public void run() {
        while (flag) {   // 线程标志位为true
            buy();
        }
    }
    
}


public class T {
    public static void main(String[] args) {
        BuyTicket station = new BuyTicket();
        Thread thread1 = new Thread(station, "A");
        Thread thread2 = new Thread(station, "B");
        Thread thread3 = new Thread(station, "C");
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
```
#### 例三
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 18:30
 */

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
class Count {
    private int money;
    private String name;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Drawing extends Thread {

    private Count count = null;
    private String userName;
    private int drawingMoney;  //取钱数
    private int nowMoney;      //现在手里的钱

    @Override
    @SneakyThrows
    public void run() {

        if (count.getMoney() - drawingMoney < 0) {
            System.out.println(Thread.currentThread().getName() + " 余额不足!!!");
            return;
        }

        Thread.sleep(100);

        count.setMoney(count.getMoney() - this.getDrawingMoney());
        this.setNowMoney(this.getNowMoney() + this.getDrawingMoney());
        System.out.println("余额：" + count.getMoney() + "   " + userName + " 手里的钱:" + this.getNowMoney());
    }
}


public class T {
    public static void main(String[] args) {
        Count count = new Count(100, "ID000001");
        new Drawing(count, "A", 50, 0).start();
        new Drawing(count, "B", 60, 0).start();
    }
}
```
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 18:30
 */

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
class Count {
    private int money;
    private String name;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Drawing extends Thread {

    private Count count = null;  //共享的Count
    private String userName;
    private int drawingMoney;  //取钱数
    private int nowMoney;      //现在手里的钱

    @Override
    @SneakyThrows
    public void run() {
        /**
         * synchronized(obj){} 同步块  默认obj使用同步的资源对象
         */
        synchronized (count) {

            if (count.getMoney() - drawingMoney < 0) {
                System.out.println(this.getUserName() + " 余额不足!!!");
                return;
            }

            Thread.sleep(100);

            count.setMoney(count.getMoney() - this.getDrawingMoney());
            this.setNowMoney(this.getNowMoney() + this.getDrawingMoney());
            System.out.println(count.getName()+"余额：" + count.getMoney());
            System.out.println(userName + " 手里的钱:" + this.getNowMoney());

        }
    }
}


public class T {
    public static void main(String[] args) {
        Count count = new Count(100, "ID000001");
        new Drawing(count, "A", 50, 0).start();
        new Drawing(count, "B", 60, 0).start();
    }
}
```
### 死锁
多个线程互相抱着对方需要的资源，然后形成僵持。
#### 4个必要条件

1. 互斥条件  一个资源只能每次被一个进程使用
1. 请求与保持条件  一个进程因请求资源而阻塞时，对已获得的资源保持不放
1. 不剥夺条件  进程已获得的资源，再未使用完之前不能强行剥夺
1. 循环等待条件  若干进程之间形成一种头尾相接的循环等待资源关系  
#### 实例
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 20:26
 */

import lombok.SneakyThrows;


// 口红
class Lipstick {

}

// 镜子
class Mirror {

}

class MakeUp extends Thread {

    /**
     * 静态资源 只有一份
     */
    private static Lipstick lipstick = new Lipstick();
    private static Mirror mirror = new Mirror();

    private int chose;
    private String girlName;

    public MakeUp(int chose, String girlName) {
        this.chose = chose;
        this.girlName = girlName;
    }

    //化妆方法
    @SneakyThrows
    private void makeup() {
        if (chose == 0) {
            synchronized (lipstick) {
                System.out.println(this.girlName + "获得口红");
                Thread.sleep(1000);
            }
            synchronized (mirror) {
                System.out.println(this.girlName + "获得镜子");
            }
        } else {
            synchronized (mirror) {
                System.out.println(this.girlName + "获得镜子");
                Thread.sleep(1000);
            }
            synchronized (lipstick) {
                System.out.println(this.girlName + "获得口红");
            }
        }
    }

    @Override
    public void run() {
        makeup();
    }
}


public class T {
    public static void main(String[] args) {
        new MakeUp(0,"A").start();
        new MakeUp(1,"B").start();
    }
}
```
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 20:26
 */

import lombok.SneakyThrows;


// 口红
class Lipstick {

}

// 镜子
class Mirror {

}

class MakeUp extends Thread {

    /**
     * 静态资源 只有一份
     */
    private static Lipstick lipstick = new Lipstick();
    private static Mirror mirror = new Mirror();

    private int chose;
    private String girlName;

    public MakeUp(int chose, String girlName) {
        this.chose = chose;
        this.girlName = girlName;
    }

    //化妆方法
    @SneakyThrows
    private void makeup() {
        if (chose == 0) {
            synchronized (lipstick) {
                System.out.println(this.girlName + "获得口红");
                Thread.sleep(1000);
                synchronized (mirror) {
                    System.out.println(this.girlName + "获得镜子");
                }
            }
        } else {
            synchronized (mirror) {
                System.out.println(this.girlName + "获得镜子");
                Thread.sleep(1000);
                synchronized (lipstick) {
                    System.out.println(this.girlName + "获得口红");
                }
            }
        }
    }

    @Override
    public void run() {
        makeup();
    }
}


public class T {
    public static void main(String[] args) {
        new MakeUp(0,"A").start();
        new MakeUp(1,"B").start();
    }
}
```
### JUC安全类型的集合
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 19:24
 */

import lombok.SneakyThrows;
import java.util.concurrent.CopyOnWriteArrayList;

public class T {
   
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

```
### lock锁
JDK5.0 新增

- 通过显示定义同步锁对象实现同步。同步锁使用Lock对象充当
- ReentrantLock(可重入锁)实现了Lock，可以显示加锁、释放锁
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 20:57
 */

class BuyTicketLock implements Runnable {

    private int ticketNums = 10;  // 票数
    private Boolean flag = true;  // 标志位

    private ReentrantLock lock=new ReentrantLock();  // 定义可重入锁

    @SneakyThrows
    @Override
    public void run() {
        while (flag) {   // 线程标志位为true
            try {
                lock.lock();
                // 无票处理
                if (ticketNums <= 0) {
                    flag = false;
                    return;
                }
                //模拟买票延时
                Thread.sleep(100);
                //买票
                System.out.println(Thread.currentThread().getName() + " " + ticketNums--);
            }finally {
                lock.unlock();
            }
        }
    }

}


public class T {
    public static void main(String[] args) {
        BuyTicketLock station = new BuyTicketLock();
        Thread thread1 = new Thread(station, "A");
        Thread thread2 = new Thread(station, "B");
        Thread thread3 = new Thread(station, "C");
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
```
## 线程通信

- wait()  表示线程一直等待，直到其他线程通知，与sleep不同，会释放
- wait(long timeout) 表示线程等待指定的毫秒数
- notify() 唤醒一个处于等待状态的线程
- notifyAll() 唤醒同一个对象上所有调用wait()方法的线程，优先级比较高的线程优先调度

  注意： 均为object类的方法
### 生产者消费者模式
#### 管程法
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 21:09
 */

import lombok.*;

// 产品
@Data
@AllArgsConstructor
class Chicken {
    private int id;
}

// 生产者
class Productor extends Thread {
    
    SynContainer container;

    public Productor(SynContainer container) {
        this.container = container;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            container.push(new Chicken(i));
            System.out.println("生产了" + i + "只鸡!!!");
        }
    }
    
}

// 消费者
class Consumer extends Thread {
    
    SynContainer container;

    public Consumer(SynContainer container) {
        this.container = container;
    }

    @SneakyThrows
    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            System.out.println("消费了" + container.pop().getId() + "只鸡!!!");
            Thread.sleep(100);
        }
    }
    
}

// 缓存区
class SynContainer {

    // 容器的大小
    private Chicken[] chickens = new Chicken[10];
    private static int count = 0;

    // 生产者放入产品
    @SneakyThrows
    public synchronized void push(Chicken chicken) {
        // 容器满
        if (count == chickens.length) {
            //通知消费者消费 生产等待
            this.wait();
        }
        // 加入容器
        chickens[count++] = chicken;
        // 通知消费者消费
        this.notifyAll();
    }

    // 消费者消费产品
    @SneakyThrows
    public synchronized Chicken pop() {
        //容器为空
        if (count == 0) {
            // 通知生产者生产 消费等待
            this.wait();
        }
        // 从容器种取出
        Chicken chicken = chickens[--count];
        // 通知生产者者消费
        this.notifyAll();
        return chicken;
    }
    
}


// 生产者消费者模式  管程法
public class T {
    public static void main(String[] args) {
        SynContainer container = new SynContainer();
        new Productor(container).start();
        new Consumer(container).start();
    }
}
```
#### 信号灯法
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-16 22:23
 */

import lombok.*;

// 生产者--->演员
class Player extends Thread {
    TV tv;

    public Player(TV tv) {
        this.tv = tv;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            this.tv.play((i % 2 == 0)?"节目A":"节目B");
        }
    }
}

// 消费者--->观众
class Watcher extends Thread {
    TV tv;

    public Watcher(TV tv) {
        this.tv = tv;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            this.tv.watch();
        }
    }
}

// 产品--->节目
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
class TV {

    // 演员表演 观众等待 T
    // 观众观看 演员等待 F
    private Boolean flag = true;
    private String voice;   // 表演的节目

    // 表演
    @SneakyThrows
    public synchronized void play(String voice) {
        // 观众观看 演员等待 F
        if (!flag) {
            this.wait();
        }
        System.out.println("演员表演了" + voice);
        //通知观众观看
        this.voice = voice;
        this.notifyAll();
        this.flag = !this.flag;
    }

    // 观看
    @SneakyThrows
    public synchronized void watch() {
        // true 演员表演 观众等待
        if (flag) {
            this.wait();
        }
        System.out.println("观众观看了" + voice);
        //通知演员表演
        this.notifyAll();
        this.flag = !this.flag;
    }

}

// 生产者消费者模式  信号灯法
public class T {
    public static void main(String[] args) {
        TV tv = new TV();
        new Player(tv).start();
        new Watcher(tv).start();
    }
}
```
# JUC
## 简介
java.util.concurrent
## synchronized和lock
### synchronized
```java

/**
 * @author by pepsi-wyl
 * @date 2022-01-17 20:19
 */

// 企业 开发  减小耦合度

// 资源类
class TicketSynchronized {
    //属性
    private int number = 50;

    //方法
    public synchronized void sale() {
        if (number > 0) {
            System.out.println(Thread.currentThread().getName()+"买" + number-- + "  " + "剩余" + number);
        }
    }
}

public class T {
    public static void main(String[] args) {
        TicketSynchronized ticket = new TicketSynchronized();

        new Thread(() -> {
            for (int i = 0; i < 20; i++) ticket.sale();
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 20; i++) ticket.sale();
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 20; i++) ticket.sale();
        }, "C").start();

    }
}
```
### lock
```java
// 可重入锁
ReentrantLock lock = new ReentrantLock();

/**
 * 公平锁    (排队)
 * 不公平锁  (插队)
 */

// 默认构造  不公平
public ReentrantLock() {
    sync = new NonfairSync();
}

// 有参构造  true 公平   false 不公平
public ReentrantLock(boolean fair) {
    sync = fair ? new FairSync() : new NonfairSync();
}

// 不公平锁
static final class NonfairSync extends Sync {
    private static final long serialVersionUID = 7316153563782823691L;
    protected final boolean tryAcquire(int acquires) {
       return nonfairTryAcquire(acquires);
    }
}

// 公平锁
static final class FairSync extends Sync {
    private static final long serialVersionUID = -3000897897090466540L;  
    @ReservedStackAccess
    protected final boolean tryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            if (!hasQueuedPredecessors() &&compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        }
        else if (current == getExclusiveOwnerThread()) {
            int nextc = c + acquires;
            if (nextc < 0)
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }
}
```
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-17 20:19
 */

// 企业 开发  减小耦合度

// 资源类
class TicketLock {
    //属性
    private int number = 50;

    // 公平锁true  非公平锁false 默认
    ReentrantLock lock = new ReentrantLock();

    //方法
    public void sale() {
        lock.lock(); //加锁
        try {
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + "买" + number-- + "  " + "剩余" + number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}


public class T {
    public static void main(String[] args) {
        TicketLock ticket = new TicketLock();

        new Thread(() -> {
            for (int i = 0; i < 20; i++) ticket.sale();
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 20; i++) ticket.sale();
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 20; i++) ticket.sale();
        }, "C").start();

    }
}
```
### 区别

1. Synchronized是内置的关键字     Lock是一个java类
1. Synchronized无法判断锁的状态  Lock是可以判断是否获取到了锁
1. Synchronized会自动释放锁         Lock必须手动释放(否则死锁)
1. Synchronized线程会一直等待     Lock不一定会一直等 lock.tryLock()
1. Synchronized可重入锁，不可以中断，非公平    Lock 可重入锁，可以判断锁，非公平/公平
1. Synchronized适合锁少量的代码同步问题    Lock适合锁大量的代码同步问题
## 生产者消费者
### synchronized
#### synchronized
```java
import lombok.SneakyThrows;

/**
 * @author by pepsi-wyl
 * @date 2022-01-17 21:04
 */

public class T {
    public static void main(String[] args) {
        DataSynchronized data = new DataSynchronized();
        
        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.increment();
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.decrement();
        }, "B").start();
        
    }
}


/**
 * 等待 业务 通知
 */
// 资源类
// 资源类
class DataSynchronized {
    private int number = 0;

    // +1
    @SneakyThrows
    public synchronized void increment() {
        if (number != 0) {
            //等待
            this.wait();
        }
        //业务
        number++;
        System.out.println(Thread.currentThread().getName() + "==>" + number);
        //通知
        this.notifyAll();
    }

    // -1
    @SneakyThrows
    public synchronized void decrement() {
        if (number == 0) {
            //等待
            this.wait();
        }
        //业务
        number--;
        System.out.println(Thread.currentThread().getName() + "==>" + number);
        //通知
        this.notifyAll();
    }

}
```
```java
// 存在虚假唤醒问题
public class T {
    public static void main(String[] args) {
        DataSynchronized data = new DataSynchronized();
        
        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.increment();
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.decrement();
        }, "B").start();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.increment();
        }, "C").start();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.decrement();
        }, "D").start();

        // 虚假唤醒
        /**
         * 假设：
         *       1、A抢到锁执行 ++                   1
         * 　　   2、A执行notify发现没有人wait，继续拿着锁执行 ，A判断不通过，A阻塞    1
         *  　　　3、B抢到锁 ，B判断不通过，B阻塞   　   1
         *
         * 　 　　4、C 抢到锁 执行--     　          　0
         * 　 　　5、C 执行Notify 唤醒A， A执行++      1 　　　
         * 　 　　6、A 执行notify唤醒B ，B执行++       2  （注意这个地方恰巧唤醒B，那么B 从哪阻塞的就从哪唤醒，B继续执行wait下面的++操作，导致出现2）
         */
    }
}
```
#### 解决虚假唤醒
if---->while
**最关键的是：**
**1、首先，每次工作结束都需要唤醒所有线程来任我挑选**
**2、其次，你争抢到锁，我会对你进行一次有效判断，合格才放行 （while）**
```java
import lombok.SneakyThrows;

/**
 * @author by pepsi-wyl
 * @date 2022-01-17 21:04
 */

public class T {
    public static void main(String[] args) {
        
        DataSynchronized data = new DataSynchronized();
        
        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.increment();
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.decrement();
        }, "B").start();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.increment();
        }, "C").start();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.decrement();
        }, "D").start();
    }
}

/**
 * 等待 业务 通知
 */
// 资源类
class DataSynchronized {
    private int number = 0;

    // +1
    @SneakyThrows
    public synchronized void increment() {
        while (number != 0) {
            //等待
            this.wait();
        }
        //业务
        number++;
        System.out.println(Thread.currentThread().getName() + "==>" + number);
        //通知
        this.notifyAll();
    }

    // -1
    @SneakyThrows
    public synchronized void decrement() {
        while (number == 0) {
            //等待
            this.wait();
        }
        //业务
        number--;
        System.out.println(Thread.currentThread().getName() + "==>" + number);
        //通知
        this.notifyAll();
    }

}
```
### lock
#### lock
```java
import lombok.SneakyThrows;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author by pepsi-wyl
 * @date 2022-01-17 21:04
 */

public class T {
    public static void main(String[] args) {

        DataLock data = new DataLock();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.increment();
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.decrement();
        }, "B").start();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.increment();
        }, "C").start();

        new Thread(() -> {
            for (int i = 0; i < 3; i++) data.decrement();
        }, "D").start();

    }
}

/**
 * 等待 业务 通知
 */
// 资源类
class DataLock {
    private Integer number = 0;

    /**
     *
     */
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    
    /**
     * condition.await();       等待
     * condition.signalAll();   唤醒全部
     */
    
    // +1
    @SneakyThrows
    public void increment() {
        lock.lock();
        try {
            while (number != 0) {
                //等待
                condition.await();
            }
            //业务
            number++;
            System.out.println(Thread.currentThread().getName() + "==>" + number);
            //通知
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // -1
    @SneakyThrows
    public void decrement() {
        lock.lock();
        try {
            while (number == 0) {
                //等待
                condition.await();
            }
            //业务
            number--;
            System.out.println(Thread.currentThread().getName() + "==>" + number);
            //通知
            condition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
```
#### Condition精准通知和唤醒线程
```java
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author by pepsi-wyl
 * @date 2022-01-18 11:28
 */

public class T {
    public static void main(String[] args) {
        DataConcurrent data = new DataConcurrent();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) data.printA();
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) data.printB();
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) data.printC();
        }, "C").start();

    }
}


// 资源类 Concurrent精准通知线程唤醒
class DataConcurrent {

    private int number = 1;

    private Lock lock = new ReentrantLock();

    // 三个线程标记
    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();
    private Condition conditionC = lock.newCondition();

    public void printA() {
        lock.lock();
        try {
            // 等待
            while (number != 1) {
                conditionA.await();   // A 线程阻塞
            }
            // 业务
            number = 2;
            System.out.println(Thread.currentThread().getName());
            // 通知
            conditionB.signal();      // B 线程唤醒
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printB() {
        lock.lock();
        try {
            // 等待
            while (number != 2) {
                conditionB.await();   // B 线程阻塞
            }
            // 业务
            number = 3;
            System.out.println(Thread.currentThread().getName());
            // 通知
            conditionC.signal();     // C 线程唤醒
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printC() {
        lock.lock();
        try {
            // 等待
            while (number != 3) {
                conditionC.await();   // C 线程阻塞
            }
            // 业务
            number = 1;
            System.out.println(Thread.currentThread().getName());
            // 通知
            conditionA.signal();     // A 线程唤醒
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
```
## 八锁现象
### 非static
```java
class Phone {

    @SneakyThrows
    @Synchronized
    public void sendMsg() {    //  synchronized 锁为this对象
        TimeUnit.SECONDS.sleep(4);  // sleep不释放锁
        System.out.println("发短信");
    }

    @Synchronized
    public void call() {      //  synchronized 锁为 this对象
        System.out.println("打电话");
    }

    public void hello(){      //  无锁
        System.out.println("你好");
    }

}
```
#### 组一
```java
public class T {

    @SneakyThrows
    public static void main(String[] args) {

        Phone phone = new Phone();

        new Thread(() -> {    // 锁为phone对象
            phone.sendMsg();
        }).start();
 
        TimeUnit.SECONDS.sleep(1);
        
        new Thread(() -> {   // 锁为phone对象
            phone.call();
        }).start();
        
        // 由于synchronized锁为phone对象 发送消息和打电话为同一把锁 ，sleep不会释放锁
        // 即使发送消息进程在sleep 打电话进程也无法进入
    }
}
```
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642498281745-5d48ff51-c9ab-4c39-9280-bd7ff78f92f5.png#clientId=uabf09a46-415f-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=78&id=u11171de0&margin=%5Bobject%20Object%5D&name=image.png&originHeight=78&originWidth=379&originalType=binary&ratio=1&rotation=0&showTitle=false&size=42973&status=done&style=none&taskId=uf1b04f86-7acb-43c0-ab51-1d604e90fa3&title=&width=379)
#### 组二
```java
public class T {

    @SneakyThrows
    public static void main(String[] args) {

        Phone phone = new Phone();

        new Thread(() -> {     // 锁为phone对象
            phone.sendMsg();
        }).start();

        
        TimeUnit.SECONDS.sleep(1);
        
        // hello 方法为无锁方法 不受锁的影响
        new Thread(() -> {
            phone.hello();
        }).start();

    }
}
```
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642498513468-a8460ea2-218d-4d5d-a5b1-dff0f3350133.png#clientId=uabf09a46-415f-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=83&id=u982d197f&margin=%5Bobject%20Object%5D&name=image.png&originHeight=83&originWidth=389&originalType=binary&ratio=1&rotation=0&showTitle=false&size=46942&status=done&style=none&taskId=u5eb8ef90-dc6c-49c6-b4ea-fed233d047b&title=&width=389)
#### 组三
```java
public class T {

    @SneakyThrows
    public static void main(String[] args) {

        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> {
            phone1.sendMsg();   // 锁为this对象 phone1对象
        }).start();

        new Thread(() -> {
            phone2.call();      // 锁为this对象 phone2对象
        }).start();

        // 两个线程为不同的锁 互不影响
    }
}
```
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642498228198-bd0d2355-62fc-4796-8021-ca24b4ab221c.png#clientId=uabf09a46-415f-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=80&id=ubcd41d6c&margin=%5Bobject%20Object%5D&name=image.png&originHeight=80&originWidth=394&originalType=binary&ratio=1&rotation=0&showTitle=false&size=44405&status=done&style=none&taskId=u9d4314fd-80ec-4dcb-be1a-69737d77c76&title=&width=394)
### static
```java
class PhoneStatic {

    @SneakyThrows
    @Synchronized
    public static void sendMsg() {    // static synchronized 锁为clazz对象
        TimeUnit.SECONDS.sleep(4);    // sleep不释放锁
        System.out.println("发短信");
    }

    @Synchronized
    public static void call() {      //  static synchronized   锁为clazz对象
        System.out.println("打电话");
    }

    public void hello() {      //  无锁
        System.out.println("你好");
    }

}
```
#### 组一
```java
public class T {

    @SneakyThrows
    public static void main(String[] args) {
        PhoneStatic phoneStatic = new PhoneStatic();

        new Thread(()->{      // static synchronized 锁为clazz对象
            phoneStatic.sendMsg();  
        }).start();

         
        TimeUnit.SECONDS.sleep(1);
        
        new Thread(()->{    // static synchronized 锁为clazz对象
            phoneStatic.call();
        }).start();
        
        // 由于synchronized锁为clazz对象 发送消息和打电话为同一把锁 ，sleep不会释放锁
        // 即使发送消息进程在sleep 打电话进程也无法进入

    }
}
```
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642499154088-665aa1fc-9891-40ad-981d-3cfe45590d05.png#clientId=uabf09a46-415f-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=93&id=ue5a1b45a&margin=%5Bobject%20Object%5D&name=image.png&originHeight=93&originWidth=392&originalType=binary&ratio=1&rotation=0&showTitle=false&size=52199&status=done&style=none&taskId=u8a4906dc-61c5-43d7-8179-28b93eae942&title=&width=392)
#### 组二
```java
public class T {

    @SneakyThrows
    public static void main(String[] args) {
        PhoneStatic phoneStatic = new PhoneStatic();

        new Thread(()->{    // static synchronized 锁为clazz对象
            phoneStatic.sendMsg();
        }).start();

        TimeUnit.SECONDS.sleep(1);
        
        new Thread(()->{   // 普通方法无锁
            phoneStatic.hello();
        }).start();

    }
}
```
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642499178093-82042afe-e42b-41ad-a9a6-8e568decf0d6.png#clientId=uabf09a46-415f-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=88&id=u855231ab&margin=%5Bobject%20Object%5D&name=image.png&originHeight=88&originWidth=415&originalType=binary&ratio=1&rotation=0&showTitle=false&size=52096&status=done&style=none&taskId=u8b49a3e9-aaf7-4344-aa24-e5a6ce8702a&title=&width=415)
#### 组三
```java
public class T {

    @SneakyThrows
    public static void main(String[] args) {
        
        PhoneStatic phoneStatic1 = new PhoneStatic();
        PhoneStatic phoneStatic2 = new PhoneStatic();
        
        new Thread(()->{   // static synchronized 锁为clazz对象
            phoneStatic1.sendMsg();
        }).start();

        TimeUnit.SECONDS.sleep(1);
        
        new Thread(()->{   // static synchronized 锁为clazz对象
            phoneStatic2.call();
        }).start();
        
        // 锁都为clazz对象 谁先执行 谁先握锁

    }
}
```
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642499240426-0e0a390f-39ea-4ef5-837a-4527ff92d6e5.png#clientId=uabf09a46-415f-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=111&id=uad8e1df5&margin=%5Bobject%20Object%5D&name=image.png&originHeight=111&originWidth=422&originalType=binary&ratio=1&rotation=0&showTitle=false&size=66936&status=done&style=none&taskId=uaceee9b4-989f-4c08-89b9-0517dcb8b8f&title=&width=422)
### 组合
```java
class PhonePro{

    @SneakyThrows
    @Synchronized
    public static void sendMsg() {    //  static synchronized 锁为clazz对象
        TimeUnit.SECONDS.sleep(4);  // sleep不释放锁
        System.out.println("发短信");
    }

    @Synchronized
    public void call() {      //   synchronized   锁为this对象
        System.out.println("打电话");
    }

    public void hello() {      //  无锁
        System.out.println("你好");
    }

}
```
#### 组一
```java
public class T {
    public static void main(String[] args) {
        PhonePro phonePro = new PhonePro();
        new Thread(()->{phonePro.sendMsg();}).start();  // 锁为clazz对象

        new Thread(()->{phonePro.call();}).start();     // 锁为phonePro对象
        
        // 两个线程之间不存在 锁的问题 
    }
}
```
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642499898205-71c660b9-1008-4ae9-ae42-689f60ee8988.png#clientId=uabf09a46-415f-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=86&id=u0773d278&margin=%5Bobject%20Object%5D&name=image.png&originHeight=86&originWidth=346&originalType=binary&ratio=1&rotation=0&showTitle=false&size=42593&status=done&style=none&taskId=ue5bbac62-5e4a-42d6-9124-ffeb22dff37&title=&width=346)
#### 组二
```java
public class T {
    public static void main(String[] args) {
        PhonePro phonePro1 = new PhonePro();
        PhonePro phonePro2 = new PhonePro();
        new Thread(()->{phonePro1.sendMsg();}).start();  // 锁为clazz对象
        new Thread(()->{phonePro2.call();}).start();     // 锁为phonePro2对象
      
        // 两个现线程之间互不干扰
    }
}
```
小结:

- new this对象
- static clazz对象 唯一
## 集合
### 并发修改异常
传统的集合类都存在并发安全问题
```java
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
```
### synchronizedXXX
Collections集合工具类中定义了将传统集合转化为线程安全的方法
```java
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
```
### JUC
#### 实现
```java
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
```
#### 源码分析
CopyOnWriteArrayList  读多写少的并发场景
```java
    public boolean add(E e) {
        synchronized (lock) {
            Object[] es = getArray();
            int len = es.length;
            es = Arrays.copyOf(es, len + 1);  //在有写操作的时候会copy一份数据，然后写完再设置成新的数据
            es[len] = e;
            setArray(es);
            return true;
        }
    }
```
CopyOnWriteArrayList  读多写少的并发场景  通过[动态数组(CopyOnWriteArrayList)](http://www.cnblogs.com/skywang12345/p/3498483.html)
```java
     public boolean add(E e) {
        return al.addIfAbsent(e);
     }

     public boolean addIfAbsent(E e) {
        Object[] snapshot = getArray();
        return indexOfRange(e, snapshot, 0, snapshot.length) < 0
            && addIfAbsent(e, snapshot);
     }

     private boolean addIfAbsent(E e, Object[] snapshot) {
        synchronized (lock) {
            Object[] current = getArray();
            int len = current.length;
            if (snapshot != current) {
                // Optimize for lost race to another addXXX operation
                int common = Math.min(snapshot.length, len);
                for (int i = 0; i < common; i++)
                    if (current[i] != snapshot[i]
                        && Objects.equals(e, current[i]))
                        return false;
                if (indexOfRange(e, current, common, len) >= 0)
                        return false;
            }
            Object[] newElements = Arrays.copyOf(current, len + 1);
            newElements[len] = e;
            setArray(newElements);
            return true;
        }
    }
```
## 3个常用辅助类
### CountDownLatch  减法计数器
```java
// CountDownLatch  减法计数器  -1
class CountDownLatch_T {
    @SneakyThrows
    public static void main(String[] args) {

        // 参数为要计数的数
        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 0; i < 3; i++) {
            final int temp = i + 1;  // 解决线程内部访问不到 i 变量的问题
            new Thread(() -> {
                System.out.println(temp + " Go Out");
                countDownLatch.countDown();   // 数量减一
            }, String.valueOf(i)).start();
        }

        countDownLatch.await();               // 等待计数器归零(await被唤醒)所有受其影响而等待的线程将会被激活
        // 该处阻塞------------------------------<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        System.out.println("关闭");   // 在计数器归零前等待
        
    }
}
```
### CyclicBarrier  栅栏
```java
// CyclicBarrier  栅栏
class CyclicBarrier_T {
    public static void main(String[] args) throws Exception {

        // 参数 线程的个数 和 Runnable参数(最后一个到达线程要做的任务)
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> System.out.println("全部进入"));

        for (int i = 1; i <= 3; i++) {   // 3个线程
            new Thread(() -> {
                try {

                    System.out.println(Thread.currentThread().getName() + "开始等待其他线程");
                    cyclicBarrier.await();                   // 开始等待
                    //当线程到达栅栏位置时将调用await方法

                    /**
                     *  调用await方法的线程告诉CyclicBarrier自己已经到达同步点，然后当前线程被阻塞。
                     *  直到parties个参与线程调用了await方法，CyclicBarrier同样提供带超时时间的await和不带超时时间的await方法：
                     */

                    System.out.println(Thread.currentThread().getName() + "开始执行");
                    Thread.sleep(1000);// 工作线程开始处理，这里用Thread.sleep()来模拟业务处理
                    System.out.println(Thread.currentThread().getName() + "执行完毕");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();
        }

        // 该处不阻塞-----------------------------<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        System.out.println("关闭");

    }
}
```
### Semaphore 信号量
```java
/**
 * 并发限制 控制最大线程数
 */
// Semaphore 信号量
class Semaphore_T {
    public static void main(String[] args) {

        // 线程数量   模拟一共有几个车位
        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                try {

                    /**
                     * 获得  如果已满,则等待
                     */
                    semaphore.acquire();  // 得到

                    System.out.println(Thread.currentThread().getName()+"抢到车位!!!");
                    TimeUnit.SECONDS.sleep(1);   // 模拟真实业务
                    System.out.println(Thread.currentThread().getName()+"离开车位!!!");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {

                    /**
                     * 释放，唤醒等待的线程
                     */
                    semaphore.release();  // 释放

                }
            },String.valueOf(i+1)).start();
        }
        
    }
}
```
## ReadWriteLock
更加细粒的锁

- 读 读 可以共存
- 读 写 不能共存
- 写 写 不能共存

- 独占锁(写锁)  一次只能被一个线程占有
- 共享锁(读锁) 多个线程可以同时占有
### 写入插队
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-19 19:53
 */

class Cache {

    // map集合
    private volatile Map<String, Object> map = new HashMap<>();

    // 写
    public void put(String key, Object value) {
        System.out.println(Thread.currentThread().getName() + "开始写");
        map.put(key, value);
        System.out.println(Thread.currentThread().getName() + "写完毕");
    }

    // 写
    public void get(String key) {
        System.out.println(Thread.currentThread().getName() + "开始读");
        map.get(key);
        System.out.println(Thread.currentThread().getName() + "写完写");
    }

    public static void main(String[] args) {
        Cache cache = new Cache();

        for (int i = 0; i < 10; i++) {
            final int temp = i;   // lambda 表达式访问不到i 需要final充当中间变量
            new Thread(() -> {
                cache.put(String.valueOf(temp + 1), temp + 1); // 写操作
            }, String.valueOf(i + 1)).start();
        }

        for (int i = 0; i < 10; i++) {
            final int temp = i;
            new Thread(() -> {
                cache.get(String.valueOf(temp + 1));           // 读操作
            }, String.valueOf(i + 1)).start();
        }
    }
    
}
```

### 加锁
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-19 19:53
 */

class CacheLock {
    private volatile Map<String, Object> map = new HashMap<>();

    // 读写锁 更加细粒度的控制
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    // 写
    public void put(String key, Object value) {
        readWriteLock.writeLock().lock();       // 加写锁
        try {
            System.out.println(Thread.currentThread().getName() + "开始写");
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + "写完毕");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readWriteLock.writeLock().unlock(); // 写锁释放
        }
    }

    // 读
    public void get(String key) {
        readWriteLock.readLock().lock();        // 加读锁
        try {
            System.out.println(Thread.currentThread().getName() + "开始读");
            map.get(key);
            System.out.println(Thread.currentThread().getName() + "读完毕");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readLock().unlock();   // 读锁释放
        }
    }

    public static void main(String[] args) {
        CacheLock cacheLock = new CacheLock();

        // 十个线程 写
        for (int i = 0; i < 10; i++) {
            // lambda 表达式访问不到i 需要final充当中间变量
            final int temp = i;
            new Thread(() -> {
                cacheLock.put(String.valueOf(temp + 1), temp + 1);    // 写操作
            }, String.valueOf(i + 1)).start();
        }

        // 十个线程 读
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            new Thread(() -> {
                cacheLock.get(String.valueOf(temp + 1));              // 读操作
            }, String.valueOf(i + 1)).start();
        }
    }
    
}
```
## BlockingQueue(阻塞队列)
### 队列类
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642597233259-0f5c856e-bbe1-4640-b60c-9a2fec18c6ab.png#clientId=u8f185273-a772-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=508&id=u6cf43dd8&margin=%5Bobject%20Object%5D&name=image.png&originHeight=508&originWidth=645&originalType=binary&ratio=1&rotation=0&showTitle=false&size=226850&status=done&style=none&taskId=u43deb0c3-96c7-4fca-867e-b9a3886978c&title=&width=645)
### 4组API
| 方式 | 抛出异常 | 有返回值,不抛出异常 | 阻塞等待 | 超时等待 |
| --- | --- | --- | --- | --- |
| 添加 |   add |   offer |  put |   offer |
| 移除 |   remove |   poll |  take |   poll |
| 判断队列首部 |   element |   peek |   --- |   --- |

```java
    //抛出异常  add remove element
    public static void t1(){

        // 队列的大小
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);

        System.out.println(blockingQueue.element());   // 异常

        // add 抛出异常
        blockingQueue.add("A");
        blockingQueue.add("B");
        blockingQueue.add("C");
        blockingQueue.add("D");            // 异常

        // remove  抛出异常
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());    //异常

    }
```
```java
    //有返回值 不抛出异常  offer  poll
    public static void t2(){
        // 队列的大小
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);

        System.out.println(blockingQueue.peek()); // null

        // offer 不抛出异常
        System.out.println(blockingQueue.offer("A"));
        System.out.println(blockingQueue.offer("B"));
        System.out.println(blockingQueue.offer("C"));  // true
        System.out.println(blockingQueue.offer("D"));  // false

        // poll  不抛出异常
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());      // null
    }
```
```java
    // 等待阻塞
    @SneakyThrows
    public static void t3() {
        // 队列的大小
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);

        // put 等待
        blockingQueue.put("A");
        blockingQueue.put("B");
        blockingQueue.put("C");
        blockingQueue.put("D");   // 死死的等待

        // take 等待
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());  // 死死的等待
    }
```
```java
    // 超时等待
    @SneakyThrows
    public static void t4() {
        // 队列的大小
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);

        // put 等待
        System.out.println(blockingQueue.offer("A", 1, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("B", 1, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("C", 1, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("D", 1, TimeUnit.SECONDS));  // 等待1s 结束

        // take 等待
        System.out.println(blockingQueue.poll(1,TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(1,TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(1,TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(1,TimeUnit.SECONDS));       // 等待1s 结束
    }
```
### SynchronousQueue 同步队列
put 进去之后,必须take出来,才能继续put
```java
// 同步队列
class SynchronousQueue_T {
    public static void main(String[] args) {

        // 同步队列
        BlockingQueue<String> queue = new SynchronousQueue<>();

        new Thread(() -> {
            try {

                System.out.println(Thread.currentThread().getName()+"put 1");
                queue.put("1");
                TimeUnit.SECONDS.sleep(2);

                System.out.println(Thread.currentThread().getName()+"put 2");
                queue.put("2");
                TimeUnit.SECONDS.sleep(2);

                System.out.println(Thread.currentThread().getName()+"put 3");
                queue.put("3");
                TimeUnit.SECONDS.sleep(2);

            }catch (Exception e){
                e.printStackTrace();
            }
        },"A").start();

        new Thread(() -> {
            try {

                System.out.println(Thread.currentThread().getName()+"=>"+queue.take());
                System.out.println(Thread.currentThread().getName()+"=>"+queue.take());
                System.out.println(Thread.currentThread().getName()+"=>"+queue.take());

            }catch (Exception e){
                e.printStackTrace();
            }
        },"B").start();
        
    }
}
```
## 线程池(重点)
三大方法,七大参数,四种拒绝策略
### 池化技术
事先准备好一些资源,从池子中拿===>优化资源的使用===>池化技术
线程池 连接池 内存池 对象池......
#### 好处 

1. 降低资源的消耗
1. 提高响应的速度
1. 方便管理

            线程复用,可以控制最大并发数,管理线程
### 三大方法
创建线程池
#### Executors.newSingleThreadExecutor
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-19 21:58
 */

// 单个线程
class SingleThreadExecutor_T {
    public static void main(String[] args) {
        // 单个线程  只有一个线程同时执行
        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " OK");
            });
        }
        pool.shutdown(); // 关闭线程池
    }
}

```
#### Executors._newFixedThreadPool_(number)
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-19 21:58
 */

// 固定线程大小的线程池
class FixedThreadPool_T{
    public static void main(String[] args) {
        // 固定线程大小的线程池  最大支持同时执行的线程数
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " OK");
            });
        }
        pool.shutdown();  // 关闭线程池
    }
}
```
#### Executors._newCachedThreadPool_
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-19 21:58
 */

// 可伸缩
class CachedThreadPool_T{
    public static void main(String[] args) {
        // 可伸缩 随着线程的数量自由伸缩
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " OK");
            });
        }
        pool.shutdown();  // 关闭线程池
    }
}
```
#### 三大方法源码分析
```java
// newSingleThreadExecutor
public static ExecutorService newSingleThreadExecutor() {
      return new FinalizableDelegatedExecutorService
           (new ThreadPoolExecutor(1, 1,0L, TimeUnit.MILLISECONDS,
                                   new LinkedBlockingQueue<Runnable>()));
}

// newFixedThreadPool
public static ExecutorService newFixedThreadPool(int nThreads) {
      return new ThreadPoolExecutor(nThreads, nThreads,
                                   0L, TimeUnit.MILLISECONDS,
                                   new LinkedBlockingQueue<Runnable>());
}

// newCachedThreadPool
public static ExecutorService newCachedThreadPool() {
      return new ThreadPoolExecutor(0, Integer.MAX_VALUE,  // 21亿 
                                   60L, TimeUnit.SECONDS,
                                   new SynchronousQueue<Runnable>());
}

Integer.MAX_VALUE,  // 21亿 可能会堆积大量的请求 导致OOM
```
### 七大参数
#### 七大参数源码分析
```java
// ThreadPoolExecutor 7大参数
public ThreadPoolExecutor(int corePoolSize,     // 核心线程池大小
                          int maximumPoolSize,  // 最大核心线程池大小
                          long keepAliveTime,   // 超时没有人调用就会释放
                          TimeUnit unit,        // 超时单位
                          BlockingQueue<Runnable> workQueue, // 阻塞队列
                          ThreadFactory threadFactory,   // 线程工厂,创建线程
                          RejectedExecutionHandler handler) //拒绝策略
{ 
   if (corePoolSize < 0 ||
        maximumPoolSize <= 0 ||
        maximumPoolSize < corePoolSize ||
        keepAliveTime < 0)
        throw new IllegalArgumentException();
   if (workQueue == null || threadFactory == null || handler == null)
        throw new NullPointerException();
   this.corePoolSize = corePoolSize;
   this.maximumPoolSize = maximumPoolSize;
   this.workQueue = workQueue;
   this.keepAliveTime = unit.toNanos(keepAliveTime);
   this.threadFactory = threadFactory;
   this.handler = handler;
}
```
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642645783987-20470e0e-75e7-49db-890e-a1b444fb44b6.png#clientId=u07fee60f-35af-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=521&id=u716f91af&margin=%5Bobject%20Object%5D&name=image.png&originHeight=521&originWidth=856&originalType=binary&ratio=1&rotation=0&showTitle=false&size=143570&status=done&style=none&taskId=u28afed10-c542-40b4-aeb3-64df64b614d&title=&width=856)
#### 自定义线程类
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642647057978-f78ead20-0542-4a37-937e-6a3571e55e3e.png#clientId=u07fee60f-35af-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=262&id=ub4ab6add&margin=%5Bobject%20Object%5D&name=image.png&originHeight=262&originWidth=870&originalType=binary&ratio=1&rotation=0&showTitle=false&size=87094&status=done&style=none&taskId=u78e1ce4f-66e3-4930-ad9a-26051a6aaaa&title=&width=870)
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-19 21:58
 */

// ThreadPoolExecutor 原生方法创建线程池
class ThreadPoolExecutor_T {
    public static void main(String[] args) {

        ExecutorService pool = new ThreadPoolExecutor(
                2, // 核心线程池大小                       有2个窗口办理
                5, // 最大核心线程池大小                    一共有5个窗口 3个窗口没有开
                3,  // 保持连接时间                         办理业务的最大时间
                TimeUnit.SECONDS,  // 保持连接时间单位      时间单位
                new LinkedBlockingDeque<>(3), // 阻塞队列                     侯客厅
                Executors.defaultThreadFactory(),     // 线程工厂,创建线程
                new ThreadPoolExecutor.DiscardOldestPolicy()  // 拒绝策略     候车厅满 继续来人怎么办
        );

        // 最大承载  队列 + 最大核心线程池大小
        for (int i = 1; i <= 10; i++) {
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " OK");
            });
        }

        pool.shutdown();  // 关闭线程池

    }
}
```
### 四大竞争策略
#### 源码分析
```java
    // new ThreadPoolExecutor.CallerRunsPolicy()    哪里来的回哪里
    public static class CallerRunsPolicy implements RejectedExecutionHandler {
        public CallerRunsPolicy() { }
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            if (!e.isShutdown()) {
                r.run();
            }
        }
    }

    // new ThreadPoolExecutor.AbortPolicy()         丢掉任务 抛出异常
    public static class AbortPolicy implements RejectedExecutionHandler {
        public AbortPolicy() { }
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            throw new RejectedExecutionException("Task " + r.toString() +
                                                 " rejected from " +
                                                 e.toString());
        }
    }

    //  new ThreadPoolExecutor.DiscardPolicy()       丢掉任务 不抛出异常
    public static class DiscardPolicy implements RejectedExecutionHandler {
        public DiscardPolicy() { } 
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        }
    }

    // new ThreadPoolExecutor.DiscardOldestPolicy() 尝试和第一个竞争，失败则丢掉异常  不抛出异常
    public static class DiscardOldestPolicy implements RejectedExecutionHandler {
        public DiscardOldestPolicy() { }
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            if (!e.isShutdown()) {
                e.getQueue().poll();
                e.execute(r);
            }
        }
    }
```
### CPU密集型、IO密集型
#### CPU密集型（CPU-bound）
CPU密集型也叫计算密集型，指的是系统的硬盘、内存性能相对CPU要好很多，此时，系统运作大部分的状况是CPU Loading 100%，CPU要读/写I/O(硬盘/内存)，I/O在很短的时间就可以完成，而CPU还有许多运算要处理，CPU Loading很高。
在多重程序系统中，大部份时间用来做计算、逻辑判断等CPU动作的程序称之CPU bound。例如一个计算圆周率至小数点一千位以下的程序，在执行的过程当中绝大部份时间用在三角函数和开根号的计算，便是属于CPU bound的程序。
CPU bound的程序一般而言CPU占用率相当高。这可能是因为任务本身不太需要访问I/O设备，也可能是因为程序是多线程实现因此屏蔽掉了等待I/O的时间。
**CPU密集型任务，就需要尽量压榨CPU，参考值可以设为 _N_cpu+1**
```java
(Runtime.getRuntime().availableProcessors()) + 1
```
#### IO密集型（I/O bound）
IO密集型指的是系统的CPU性能相对硬盘、内存要好很多，此时，系统运作，大部分的状况是CPU在等I/O (硬盘/内存) 的读/写操作，此时CPU Loading并不高。
I/O bound的程序一般在达到性能极限时，CPU占用率仍然较低。这可能是因为任务本身需要大量I/O操作，而pipeline做得不是很好，没有充分利用处理器能力。
**IO密集型任务，参考值可以设置为 2 * _N_cpu**
```java
(Runtime.getRuntime().availableProcessors()) * 2
```
## 四大函数式接口(必须掌握)
lambda表达式 链式编程 函数式接口 Stream流式计算
@FunctionalInterface   简化编程模型，在框架底层大量使用
### Function 函数型接口
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-20 11:50
 */

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
```
### Predicate 断定型接口
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-20 11:50
 */

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
```
### Consumer 消费型接口
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-20 11:50
 */

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
```
### Supplier 供给型接口
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-20 11:50
 */

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
```
## Stream流式计算
### 基本操作
计算交给流来操作
```java
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * @author by pepsi-wyl
 * @date 2022-01-20 16:04
 */

public class Stream_T {
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
```
### 并行流计算大数
```java
System.out.println(LongStream.rangeClosed(1L, 10_0000_0000L).parallel().reduce(0, (num1, num2) -> Long.sum(num1, num2)));
System.out.println(LongStream.rangeClosed(1L, 10_0000_0000L).parallel().reduce(0, Long::sum));
```
## ForkJoin
### 简介
JDK 7 并发执行任务,提高效率,大数据量 (大任务拆分成小任务)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642667285120-1293184e-3dd8-4552-bc92-64a9b809a870.png#clientId=u07fee60f-35af-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=554&id=uf2050300&margin=%5Bobject%20Object%5D&name=image.png&originHeight=554&originWidth=772&originalType=binary&ratio=1&rotation=0&showTitle=false&size=206340&status=done&style=none&taskId=uf5991b15-35c2-4bd4-a687-98685a0a38d&title=&width=772)
### 特点
         工作窃取      维护的都是双端队列
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642667472193-792f83ec-2e1f-4739-8660-fb11a72ed015.png#clientId=u07fee60f-35af-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=501&id=u1206b858&margin=%5Bobject%20Object%5D&name=image.png&originHeight=501&originWidth=301&originalType=binary&ratio=1&rotation=0&showTitle=false&size=53343&status=done&style=none&taskId=u5f45a669-7d64-43e1-a396-e103603a76c&title=&width=301)
### 例子
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-20 16:24
 */

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
        ForkJoinTask<Long> submit = pool.submit(new ForkJoin_T(1L, 10_0000_0000L)); // 提交任务
        System.out.println(submit.get());        // 获取结果
    }
    
}
```
## 异步回调
Futrue
### 基本使用
#### 无返回值
```java
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
        future.get();    // 获取执行请求
        System.out.println("main 线程");
    }
}
```
#### 有返回值
```java
// 异步调用 类似于ajax
class Future_T {
    @SneakyThrows
    public static void main(String[] args) {
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
```
## JMM （java memory model）
### 简介
         java内存模型，不存在的东西      概念，约定
### 内存划分
         JVM在设计时候考虑到，如果JAVA线程每次读取和写入变量都直接操作主内存，对性能影响比较大，所以每条线程拥有各自的工作内存，工作内存中的变量是主内存中的一份拷贝，线程对变量的读取和写入，直接在工作内存中操作，而不能直接去操作主内存中的变量。
         JMM规定了内存主要划分为主内存和工作内存两种。
![1102674-20180815143324915-2024156794.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642679220860-45b19ae0-98cb-401f-ba30-4b11ea37926c.png#clientId=u07fee60f-35af-4&crop=0&crop=0&crop=1&crop=1&from=drop&id=udf7fe5c3&margin=%5Bobject%20Object%5D&name=1102674-20180815143324915-2024156794.png&originHeight=536&originWidth=916&originalType=binary&ratio=1&rotation=0&showTitle=false&size=55583&status=done&style=none&taskId=u5fd67b92-57a9-4aac-8f2e-00e8c692a71&title=)
### 内存交互操作
#### 8种操作

- lock     （锁定）：作用于主内存的变量，把一个变量标识为线程独占状态
- unlock （解锁）：作用于主内存的变量，它把一个处于锁定状态的变量释放出来，释放后的变量才可以被其他线程锁定
- read    （读取）：作用于主内存变量，它把一个变量的值从主内存传输到线程的工作内存中，以便随后的load动作使用
- load     （载入）：作用于工作内存的变量，它把read操作从主存中变量放入工作内存中
- use      （使用）：作用于工作内存中的变量，它把工作内存中的变量传输给执行引擎，每当虚拟机遇到一个需要使用到变量的值，就会使用到这个指令
- assign  （赋值）：作用于工作内存中的变量，它把一个从执行引擎中接受到的值放入工作内存的变量副本中
- store    （存储）：作用于主内存中的变量，它把一个从工作内存中一个变量的值传送到主内存中，以便后续的write使用
- write （写入）  ：作用于主内存中的变量，它把store操作从工作内存中得到的变量的值放入主内存的变量中

![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642678884260-81541145-3289-4ae6-99bb-8be0e636bd07.png#clientId=u07fee60f-35af-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=443&id=uffe96c9b&margin=%5Bobject%20Object%5D&name=image.png&originHeight=443&originWidth=657&originalType=binary&ratio=1&rotation=0&showTitle=false&size=51112&status=done&style=none&taskId=u45672944-d61f-464f-8b12-8cfbe429420&title=&width=657)
#### 同步约定

- 不允许read和load、store和write操作之一单独出现。即使用了read必须load，使用了store必须write
- 不允许线程丢弃他最近的assign操作，即工作变量的数据改变了之后，必须告知主存
- 不允许一个线程将没有assign的数据从工作内存同步回主内存
- 一个新的变量必须在主内存中诞生，不允许工作内存直接使用一个未被初始化的变量。就是怼变量实施use、store操作之前，必须经过assign和load操作
- 一个变量同一时间只有一个线程能对其进行lock。多次lock后，必须执行相同次数的unlock才能解锁
- 如果对一个变量进行lock操作，会清空所有工作内存中此变量的值，在执行引擎使用这个变量前，必须重新load或assign操作初始化变量的值
- 如果一个变量没有被lock，就不能对其进行unlock操作。也不能unlock一个被其他线程锁住的变量
- 对一个变量进行unlock操作之前，必须把此变量同步回主内存
### 问题
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642678956682-32970ed5-b3c0-4446-ae2c-7348f586ed50.png#clientId=u07fee60f-35af-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=488&id=ud64c6db6&margin=%5Bobject%20Object%5D&name=image.png&originHeight=488&originWidth=872&originalType=binary&ratio=1&rotation=0&showTitle=false&size=72645&status=done&style=none&taskId=u21d6449a-9c8a-46fd-b4c0-a2c38049236&title=&width=872)
```java
import lombok.SneakyThrows;
import java.util.concurrent.TimeUnit;

/**
 * @author by pepsi-wyl
 * @date 2022-01-20 18:28
 */
public class T {

    private static int number = 0;
    @SneakyThrows
    public static void main(String[] args) {
        new Thread(()->{  // 线程对主内存的变化不知道
            while (number==0){

            }
        }).start();
        
        TimeUnit.SECONDS.sleep(1);  // 修改number的值
        number++;
        System.out.println(number);  
        // 线程并没有停下来
    }
    
}
```
### Volatile

- 保证可见性
- 不保证原子性
- 禁止指令重排
#### 保证可见性
```java
import lombok.SneakyThrows;
import java.util.concurrent.TimeUnit;

/**
 * @author by pepsi-wyl
 * @date 2022-01-20 18:28
 */

public class T {

    private static volatile int number = 0;  // 保证可见性
    
    @SneakyThrows
    public static void main(String[] args) {
        new Thread(()->{  // 线程对主内存的变化不知道
            while (number==0){

            }
        }).start();
        
        TimeUnit.SECONDS.sleep(1);  
        number++;                    // 修改number的值
        System.out.println(number); 
    }
    
}
```
#### 不保证原子性
原子性：不可分割   线程A在执行任务的时候，不能被打扰，也不能分割。要么同时成功，要么失败。
```java
/**
 * @author by pepsi-wyl
 * @date 2022-01-20 18:28
 */

// 不保证原子性
class volatile_T {

    // 不保证原子性
    private volatile static int number = 0;  

    // 加操作
    public static void add() {
        number++;      // 不是原子性操作           
    }

    public static void main(String[] args) {

        for (int i = 1; i <= 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    add();
                }
            }).start();
        }

        while (Thread.activeCount() > 2) {
            Thread.yield();
        }

        System.out.println(number);

    }
}
```
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642681337226-e56dd093-d8d9-4161-be47-9a70d9688ec8.png#clientId=ud1c2f481-6aa0-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=107&id=ud92d29a6&margin=%5Bobject%20Object%5D&name=image.png&originHeight=107&originWidth=557&originalType=binary&ratio=1&rotation=0&showTitle=false&size=95598&status=done&style=none&taskId=u4bf76426-f8eb-4e66-b567-ab215e4bcb1&title=&width=557)
#### 解决不保证原子性 （原子类）
```java
// 保证原子性
class volatile_T {


    // 原子类 保证原子性
    private volatile static AtomicInteger number = new AtomicInteger();

    // 加操作
    public static void add() {
        number.getAndIncrement();   // CAS 底层并发控制
    }

    public static void main(String[] args) {

        for (int i = 1; i <= 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    add();
                }
            }).start();
        }

        while (Thread.activeCount() > 2) {
            Thread.yield();
        }

        System.out.println(number);

    }
}
```
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642681689159-e88788fc-486d-4a66-aba8-22d5544a5014.png#clientId=ud1c2f481-6aa0-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=111&id=u6660ac61&margin=%5Bobject%20Object%5D&name=image.png&originHeight=111&originWidth=427&originalType=binary&ratio=1&rotation=0&showTitle=false&size=72703&status=done&style=none&taskId=u1abf9445-4261-4e33-b2e8-24bb68a2bc9&title=&width=427)
#### 禁止指令重排
       指令重排

1. 计算机并不是按照写的代码那样去执行
1. 源代码------>编辑器优化的重排------>指令并行可能重排------>内存系统的重排------>执行
1. 处理器在进行指令重排的时候，会考虑数据间的依赖性

多线程操作数据 如果指令重排可能会影响另一个线程

volatile如何避免指令重排
   内存屏障。CPU指令。作用：                   ------> 在单例模式种使用的最多

- 保证特定的操作的执行顺序
- 可以保证某些变量的内存可见性（利用这些特性实现可见性）

![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642682953983-eb2e0f1f-6541-4cdc-a5d3-87909a26d41d.png#clientId=ud1c2f481-6aa0-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=458&id=u62135d05&margin=%5Bobject%20Object%5D&name=image.png&originHeight=458&originWidth=292&originalType=binary&ratio=1&rotation=0&showTitle=false&size=65182&status=done&style=none&taskId=ue0a5e481-3dad-4cb7-8490-f875a69b969&title=&width=292)
## 单例模式
### 饿汉式
饿汉式是在初始化的时候就将单例对象创建出来。通常，通过属性new创建自身。该方式不存在线程安全的问题（JVM保证线程安全），但会造成内存资源的浪费。
```java
// 饿汉式 单例模式
class Hungry {

    // 耗费空间的资源  饿汉式 可能会浪费空间
    private byte[] data = new byte[1024 * 1024 * 4];

    private Hungry() {
    }

    private final static Hungry HUNGRY = new Hungry();

    public static Hungry getInstance() {  // 得到实例
        return HUNGRY;
    }
}
```
### 懒汉式
懒汉式是在第一次使用的时候，才将单例对象创建出来。该方式存在线程安全的问题，但不会造成内存资源的浪费。
#### 基本框架
getInstance并发下存在问题 可能产生多个对象(非单例)
```java
// 懒汉式 单例模式
class Hungry {

    private Hungry() {
        System.out.println(Thread.currentThread().getName() + " OK");
    }

    private static Hungry HUNGRY;

    public static Hungry getInstance() {   // 得到实例
        if (HUNGRY == null) {
            HUNGRY = new Hungry();      // 在用的时候进行创建
        }
        return HUNGRY;
    }

    // 并发下存在问题
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> getInstance(), String.valueOf(i + 1)).start();
        }
    }
}
```
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642685284732-a813485f-5c66-4255-97cf-b54d86ceaa4e.png#clientId=ud1c2f481-6aa0-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=270&id=ue2e3dfc6&margin=%5Bobject%20Object%5D&name=image.png&originHeight=270&originWidth=354&originalType=binary&ratio=1&rotation=0&showTitle=false&size=128542&status=done&style=none&taskId=uc67ec0ab-731e-4848-99a4-d88dbb8b3b1&title=&width=354)
#### DCL 
```java
// 懒汉式 单例模式
class Hungry {

    private Hungry() {
        System.out.println(Thread.currentThread().getName() + " OK");
    }

    private static Hungry HUNGRY;

    // DCL   懒汉式单例
    public static Hungry getInstance() {           // 得到实例
        // 第一层非空校验
        if (HUNGRY == null) {
            // 加同步锁，保证只有一个线程进入
            synchronized (Hungry.class) {
                // 第二层非空校验，防止在第一次非空校验时，两个线程拿到的都是null对象而创建两次。
                if (HUNGRY == null) {
                    HUNGRY = new Hungry();      // 在用的时候进行创建
                }
            }
        }
        return HUNGRY;
    }

    //DCL 双重检测锁模式
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> getInstance(), String.valueOf(i + 1)).start();
        }
    }
}
```
   new对象  非原子性操作  指令重排

- 1.分配内存空间
- 2.执行构造方法，初始化对象
- 3.把对象指向空间

问题：如果一个线程先指向空间 其他线程可能放回null
#### DCL+volatile
```java
// 懒汉式 单例模式
class Hungry {

    private Hungry() {
        System.out.println(Thread.currentThread().getName() + " OK");
    }

    // volatile
    private volatile static Hungry HUNGRY;         // 防止指令重排

    // DCL   懒汉式单例
    public static Hungry getInstance() {           // 得到实例
        // 第一层非空校验
        if (HUNGRY == null) {
            // 加同步锁，保证只有一个线程进入
            synchronized (Hungry.class) {
                // 第二层非空校验，防止在第一次非空校验时，两个线程拿到的都是null对象而创建两次。
                if (HUNGRY == null) {
                    HUNGRY = new Hungry();      // 在用的时候进行创建
                }
            }
        }
        return HUNGRY;
    }

    //DCL + volatile
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> getInstance(), String.valueOf(i + 1)).start();
        }
    }
}
```
#### 防止反射和序列化破坏单例模式
**1.反射，通过反射获取单例对象的构造器，暴力破解后即可创建多个不同实例**
**2.序列化，通过深克隆复制对象，可生成多个实例。怎么防止：重写在单例对象中readObject()方法。**
```java
// 懒汉式 单例模式
class Hungry_T4 implements Serializable {

    private volatile static Boolean pepsi_wyl = true;  // 加密变量------<<<<<<<<<<<<<<<
    private volatile static Hungry_T4 HUNGRY_T4;       // 防止指令重排

    /**
     * 防止反射操作 破坏单例模式
     */
    private Hungry_T4() {
        // 这里双重校验，也是防止两个线程拿到的都是true，而创建了两个实例
        if (pepsi_wyl) {
            synchronized (Hungry_T4.class) {
                if (pepsi_wyl) {
                    // 为第一次创建，将pepsi_wyl设置为true
                    pepsi_wyl = false;
                } else {
                    // pepsi_wyl为true，表示之前创建过了，需要抛出异常
                    throw new RuntimeException("单例对象创建重复异常");
                }
            }
        }
    }

    //DCL + volatile
    public static Hungry_T4 getInstance() {           // 得到实例
        // DCL 双重检测锁模式  懒汉式单例
        if (HUNGRY_T4 == null) {
            synchronized (Hungry_T4.class) {
                if (HUNGRY_T4 == null) {
                    HUNGRY_T4 = new Hungry_T4();      // 在用的时候进行创建
                }
            }
        }
        return HUNGRY_T4;
    }

    /** 单例对象实现了Serializable接口，通过重写readResolve()禁止程序通过深拷贝创建多个实例，达到破坏单例对象的目的 */
    private Object readResolve(){
        return Hungry_T4.class;
    }
}
```
### 静态内部类
```java
// 静态内部类
class Holder {

    private Holder() {
    }

    // 使用内部类（JVM保证），创建单例对象
    public static class InnerClass {
        private static final Holder HOLDER = new Holder();
    }
    
    // 对外提供公共方法，通过调用内部类的属性，返回获取的创建好的单例对象
    public static Holder getInstance() {
        return InnerClass.HOLDER;
    }
    
}
```
## CAS（乐观锁）

- synchronized是悲观锁，这种线程一旦得到锁，其他需要锁的线程就挂起的情况就是悲观锁。
- CAS操作的就是乐观锁，每次不加锁而是假设没有冲突而去完成某项操作，如果因为冲突失败就重试，直到成功为止。
### 简介
CAS是英文单词Compare And Swap的缩写，翻译过来就是比较并替换。
CAS机制当中使用了3个基本操作数：内存地址V，旧的预期值A，要修改的新值B。更新一个变量的时候，只有当变量的预期值A和内存地址V当中的实际值相同时，才会将内存地址V对应的值修改为B。否则将会从新尝试（自旋锁）。
### compareAndSet基本操作
```java
// CAS CompareAndSet: 比较并交换
class CAS_T {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(2000);
        
// 如果与期望的值相同就更新否则就不更新        
// compareAndSet(int expectedValue, int newValue)  期望 更新   
        
        // 期望的值为2000 就更新 并且返回更新成功与失败
        System.out.println(atomicInteger.compareAndSet(2000, 2021)); 
        
    }
}
```
### getAndIncrement源码分析
number++ 的操作
```java
    public final int getAndIncrement() {
        return U.getAndAddInt(this, VALUE, 1);
    }

    @HotSpotIntrinsicCandidate
    public final int getAndAddInt(Object o, long offset, int delta) {
        int v;
        do {                                            // 自旋锁
            v = getIntVolatile(o, offset);
        } while (!weakCompareAndSetInt(o, offset, v, v + delta));
        return v;
    }
    // 拿到内存位置的最新值v，使用CAS尝试修将内存位置的值修改为目标值v+delta，如果修改失败，则获取该内存位置的新值v，然后继续尝试，直至修改成功。
```
### ![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642736570362-36281220-ba10-4172-82fe-8af78cc297b8.png#clientId=uf72b9424-b333-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=393&id=u30092764&margin=%5Bobject%20Object%5D&name=image.png&originHeight=393&originWidth=702&originalType=binary&ratio=1&rotation=0&showTitle=false&size=269192&status=done&style=none&taskId=ua6668aea-926c-4659-892d-a780cf86ee5&title=&width=702)
### 缺点

1. 循环时间长开销很大。（自旋锁）
1. 只能保证一个共享变量的原子操作。
1. ABA问题。
### ABA问题 (狸猫换太子)
   CAS 的使用流程通常如下：

- 1）首先从地址 V 读取值 A；
- 2）根据 A 计算目标值 B；
- 3）通过 CAS 以原子的方式将地址 V 中的值从 A 修改为 B。

如果在这段期间它的值曾经被改成了B，后来又被改回为A，那CAS操作就会误认为它从来没有被改变过。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642737275991-54df7142-0112-4458-9b33-a365a73c9c50.png#clientId=uf72b9424-b333-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=298&id=udf9efcea&margin=%5Bobject%20Object%5D&name=image.png&originHeight=298&originWidth=511&originalType=binary&ratio=1&rotation=0&showTitle=false&size=59642&status=done&style=none&taskId=u4e9a1450-2394-4ddd-b734-b3ecb80b58e&title=&width=511)
### 原子引用
解决ABA漏洞 （带版本号，时间戳） AtomicStampedReference
```java
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
                    (1, 2, atomicStampedReference.getStamp(), 
                     atomicStampedReference.getStamp() + 1));
            System.out.println("A2=>" + atomicStampedReference.getStamp());
            
            System.out.println(atomicStampedReference.compareAndSet
                    (2, 1, atomicStampedReference.getStamp(), 
                     atomicStampedReference.getStamp() + 1));
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
```
## 锁
### 公平锁  非公平锁
公平锁：公平 不能插队，先来后到
非公平锁：不公平 可以插队（默认为不公平）
```java
/**
 * 公平锁    (排队)
 * 不公平锁  (插队)
 */

// 默认构造  不公平
public ReentrantLock() {
    sync = new NonfairSync();
}

// 有参构造  true 公平   false 不公平
public ReentrantLock(boolean fair) {
    sync = fair ? new FairSync() : new NonfairSync();
}

// 不公平锁
static final class NonfairSync extends Sync {
    private static final long serialVersionUID = 7316153563782823691L;
    protected final boolean tryAcquire(int acquires) {
       return nonfairTryAcquire(acquires);
    }
}

// 公平锁
static final class FairSync extends Sync {
    private static final long serialVersionUID = -3000897897090466540L;  
    @ReservedStackAccess
    protected final boolean tryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            if (!hasQueuedPredecessors() &&compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        }
        else if (current == getExclusiveOwnerThread()) {
            int nextc = c + acquires;
            if (nextc < 0)
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }
}
```
### 乐观锁 悲观锁

- synchronized是悲观锁，这种线程一旦得到锁，其他需要锁的线程就挂起的情况就是悲观锁。
- CAS操作的就是乐观锁，每次不加锁而是假设没有冲突而去完成某项操作，如果因为冲突失败就重试，直到成功为止。
### 可重入锁(递归锁)
synchronized 和lock都是可重入锁
获得一把锁的时候，默认里面的锁也会获得 但是锁必须配对使用，(加锁于解锁配对)
### ![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642748254273-a0e816f8-2dcb-4aec-b00c-c694cda2d188.png#clientId=uf72b9424-b333-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=463&id=u9e26c518&margin=%5Bobject%20Object%5D&name=image.png&originHeight=463&originWidth=724&originalType=binary&ratio=1&rotation=0&showTitle=false&size=88218&status=done&style=none&taskId=ud687472d-ea62-45c0-b2b3-e3563d85ed3&title=&width=724)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642748632444-e591f01e-3a9b-4cf4-aabd-14cfa8e39891.png#clientId=uf72b9424-b333-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=495&id=u44fbfdf1&margin=%5Bobject%20Object%5D&name=image.png&originHeight=495&originWidth=638&originalType=binary&ratio=1&rotation=0&showTitle=false&size=162427&status=done&style=none&taskId=ue93fa55b-117a-4dbd-88c3-d9ebf3138da&title=&width=638)
### 自旋锁 spinLock
#### JDK实例
```java
    @HotSpotIntrinsicCandidate
    public final int getAndAddInt(Object o, long offset, int delta) {
        int v;
        do {                                            // 自旋锁
            v = getIntVolatile(o, offset);
        } while (!weakCompareAndSetInt(o, offset, v, v + delta));
        return v;
    }
```
#### 自定义
```java
import lombok.SneakyThrows;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author by pepsi-wyl
 * @date 2022-01-21 14:33
 */

class SpinLock {

    // CAS
    private AtomicReference<Thread> atomicReference = new AtomicReference<Thread>();

    // 加锁
    @SneakyThrows
    public void myLock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "======>" + "myLock");
        // 自旋锁
        while (!atomicReference.compareAndSet(null, thread)) {  // 为true是
            System.out.println(thread.getName() + "自旋......");  //
            TimeUnit.SECONDS.sleep(1);
        }
    }

    // 解锁
    public void myUnLock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "======>" + "myUnLock");
        // 解自旋锁
        atomicReference.compareAndSet(thread, null);
    }

    @SneakyThrows
    public static void main(String[] args) {

        SpinLock lock = new SpinLock();

        new Thread(() -> {
            lock.myLock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.myUnLock();
            }
        }, "A").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            lock.myLock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.myUnLock();
            }
        }, "B").start();

    }
}
```
### 死锁
多个线程互相抱着对方需要的资源，然后形成僵持。
#### 4个必要条件

1. 互斥条件  一个资源只能每次被一个进程使用
1. 请求与保持条件  一个进程因请求资源而阻塞时，对已获得的资源保持不放
1. 不剥夺条件  进程已获得的资源，再未使用完之前不能强行剥夺
1. 循环等待条件  若干进程之间形成一种头尾相接的循环等待资源关系  

![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642751109444-c88b1e70-c127-46a6-a9e7-a41cfc8630da.png#clientId=uf72b9424-b333-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=305&id=u01f0bb49&margin=%5Bobject%20Object%5D&name=image.png&originHeight=305&originWidth=571&originalType=binary&ratio=1&rotation=0&showTitle=false&size=85837&status=done&style=none&taskId=uc090760a-c38f-4f0c-8e3a-d3b06b51a5b&title=&width=571)
#### 排查死锁

- 使用 jps-l 定位线程号   
- 使用 jstack-进程号 找到死锁问题  (查看堆栈信息)

![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642751695068-1c001663-858e-4ceb-939f-b44090c8085a.png#clientId=uf72b9424-b333-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=264&id=u93dfeaa7&margin=%5Bobject%20Object%5D&name=image.png&originHeight=264&originWidth=716&originalType=binary&ratio=1&rotation=0&showTitle=false&size=274143&status=done&style=none&taskId=u18793372-be44-43be-909c-54b00c16c22&title=&width=716)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1642751708828-23b62259-828c-48b3-9173-5d6bd3f624fe.png#clientId=uf72b9424-b333-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=316&id=ue5bed3b5&margin=%5Bobject%20Object%5D&name=image.png&originHeight=316&originWidth=599&originalType=binary&ratio=1&rotation=0&showTitle=false&size=284845&status=done&style=none&taskId=ue6549abe-2949-499e-9637-72d37ca23d7&title=&width=599)
