package com.github.yoojia.next.react;

import java.util.concurrent.Callable;

/**
 * 任务调度器
 * @author YOOJIA.CHEN (yoojiachen@gmail.com)
 */
public interface Schedule {

    int FLAG_ON_CALLER = 20151010;
    int FLAG_ON_MAIN = 20151111;
    int FLAG_ON_THREADS = 20151212;

    /**
     * 根据给定的Flags来决定如何调度任务的执行
     * @param task 需要被调度执行的任务
     * @param flags 指定执行的Flags
     * @throws Exception 任务执行过程中可以抛出异常
     */
    void submit(Callable<Void> task, int flags) throws Exception;

    /**
     * 关闭任务调度器
     */
    void close();
}
