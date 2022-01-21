package Test1;

import lombok.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

/**
 * @author by pepsi-wyl
 * @date 2022-01-15 18:42
 */

/**
 * Thread练习  多线程下载图片
 */

public class T4{
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

