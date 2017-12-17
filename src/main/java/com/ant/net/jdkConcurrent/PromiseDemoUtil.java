package com.ant.net.jdkConcurrent;

import java.util.concurrent.*;

/**
 * jdk中promisedemo
 *
 * @author
 * @create 2017-12-17 22:41
 **/
public class PromiseDemoUtil {

    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        threadPoolExecutor = new ThreadPoolExecutor(1,
                Runtime.getRuntime().availableProcessors() * 2,
                10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(10),
                new ThreadFactory() {
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setDaemon(true);
                        return thread;
                    }
                },new ThreadPoolExecutor.AbortPolicy());
    }

    public static FutureTask ansyGet(final String msg){

        final Callable<String> callable = new Callable<String>() {

            public String call() throws Exception {
                //处理需要异步的业务逻辑
                System.out.println(msg);
                Thread.sleep(5000);
                String backMsg =msg + "123";
                return backMsg;
            }
        };

        final FutureTask task = new FutureTask(callable);
        threadPoolExecutor.execute(task);
        return task ;
    }

}
