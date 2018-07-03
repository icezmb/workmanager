package com.alibaba.android.arouter.demo.workmanager;

import android.text.TextUtils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkThreadPool {
    private final long mKeepAliveTime = 3;
    private final ThreadPoolExecutor mThreadPool;
    private final ThreadFactory mThreadFactory;
    private final LinkedBlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<Runnable>();
    private final String mName;

    public WorkThreadPool(String name, int corePoolSize, int maximumPoolSize) {
        mName = TextUtils.isEmpty(name) ? "thread_pool_" + System.currentTimeMillis() : name;
        mThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread(r, new StringBuffer(mName).append("-thread_").append(mCount.getAndIncrement()).toString());
            }
        };

        mThreadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                mKeepAliveTime, TimeUnit.SECONDS, mQueue, mThreadFactory, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                if (!executor.isShutdown()) {
                    executor.getQueue().poll();
                    executor.submit(r);
                }
            }
        });
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return mThreadPool;
    }
}