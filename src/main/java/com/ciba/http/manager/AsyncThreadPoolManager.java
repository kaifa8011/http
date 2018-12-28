package com.ciba.http.manager;

import java.util.concurrent.LinkedBlockingQueue;
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
        threadPool = new ThreadPoolExecutor(2, 20, 20L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(32),
                new ThreadPoolExecutor.DiscardOldestPolicy());
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

