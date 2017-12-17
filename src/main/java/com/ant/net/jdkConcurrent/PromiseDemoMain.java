package com.ant.net.jdkConcurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author
 * @create 2017-12-17 22:57
 **/
public class PromiseDemoMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask task = PromiseDemoUtil.ansyGet("hello");
        if (!task.isDone()){
            String msg =(String)task.get();
            System.out.println(msg);
        }

    }
}
