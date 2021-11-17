package com.winowsi.search.thread;


import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.*;

/**
 * x
 * 线程池的使用
 *
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/10 16:55
 */

class ThreadPoolExecutors {
    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,
                10, 100L, TimeUnit.SECONDS
                , new ArrayBlockingQueue<>(100),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程:" + Thread.currentThread().getId());
            int i = 10 / 0;
            System.out.println("运行结果:" + i);
            return i;
        }, threadPoolExecutor).whenComplete((res, exception) -> {
            //捕获异常和执行结果
            System.out.println("执行成功后的结果:" + res + "失败的异常:" + exception);
        }).exceptionally(throwable -> {
            //感知异常返回默认值
            return 10;
        });
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }


}
