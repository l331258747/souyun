package com.xrwl.owner.Frame.auxiliary;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理的工具类，封装类
 *
 *
 * 2018.05.29
 * 线程池的管理 ，通过java 中的api实现管理
 * 采用conncurrent框架： 非常成熟的并发框架 ，特别在匿名线程管理非常优秀的
 */
public class ThreadManager {
    //通过ThreadPoolExecutor的代理类来对线程池的管理
    private static ThreadPollProxy mThreadPollProxy;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;//线程池空闲时，线程存活的时间

    //单列对象
    public static ThreadPollProxy getThreadPollProxy() {
        //第一次判断减少不必要等待时间
        if (mThreadPollProxy == null) {
            synchronized (ThreadPollProxy.class) {
                //第二次判断防止生成两个对象
                if (mThreadPollProxy == null) {
                    mThreadPollProxy = new ThreadPollProxy(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE);
                }
            }
        }
        return mThreadPollProxy;
    }

    //通过ThreadPoolExecutor的代理类来对线程池的管理
    public static class ThreadPollProxy {
        private ThreadPoolExecutor poolExecutor;//线程池执行者 ，java内部通过该api实现对线程池管理
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;

        public ThreadPollProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        //对外提供一个执行任务的方法
        public void execute(Runnable r) {
            if (poolExecutor == null || poolExecutor.isShutdown()) {
                poolExecutor = new ThreadPoolExecutor(
                        //核心线程数量
                        corePoolSize,
                        //最大线程数量
                        maximumPoolSize,
                        //当线程空闲时，保持活跃的时间
                        keepAliveTime,
                        //时间单元 ，分钟
                        TimeUnit.MINUTES,
                        //线程任务队列 
                        new LinkedBlockingQueue<Runnable>(),
                        //创建线程的工厂
                        Executors.defaultThreadFactory());
            }
            poolExecutor.execute(r);
        }
    }
}
