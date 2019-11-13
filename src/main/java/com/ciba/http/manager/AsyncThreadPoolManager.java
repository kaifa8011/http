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

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 5));

    private AsyncThreadPoolManager() {
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, 20, 20L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(16),
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

