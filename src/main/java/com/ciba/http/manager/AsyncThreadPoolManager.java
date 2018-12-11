package com.ciba.http.manager;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ciba
 * @description 描述
 * @date 2018/12/5
 */
public class AsyncThreadPoolManager {
    private static AsyncThreadPoolManager instance;
    private ThreadPoolExecutor threadPool;


    private AsyncThreadPoolManager() {
        threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

    public static AsyncThreadPoolManager getInstance() {
        if (instance == null) {
            synchronized (AsyncThreadPoolManager.class) {
                if (instance == null) {
                    instance = new AsyncThreadPoolManager();
                }
            }
        }
        return instance;
    }


    public ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }
}

