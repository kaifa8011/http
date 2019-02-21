package com.ciba.http.client;

import android.os.Handler;
import android.os.Looper;

import com.ciba.http.constant.HttpConfig;
import com.ciba.http.constant.HttpConstant;
import com.ciba.http.entity.Request;
import com.ciba.http.listener.HttpListener;
import com.ciba.http.manager.AsyncThreadPoolManager;
import com.ciba.http.request.AsyncRequest;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ciba
 * @description 网络请求
 * @date 2018/11/29
 */
public class AsyncHttpClient {
    private final Map<HttpListener, List<WeakReference<Future<?>>>> listenerMap = new WeakHashMap<>();
    private final HttpConfig httpConfig;
    private final Handler handler;
    private ThreadPoolExecutor threadPool = AsyncThreadPoolManager.getInstance().getThreadPool();
    private Map<String, String> headers;

    public AsyncHttpClient() {
        httpConfig = createDefaultHttpConfig();
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 设置线程池
     */
    public AsyncHttpClient setThreadPool(ThreadPoolExecutor threadPool) {
        if (threadPool != null) {
            this.threadPool = threadPool;
        }
        return this;
    }

    /**
     * 设置请求头
     */
    public AsyncHttpClient setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public AsyncHttpClient setContentType(String contentType) {
        httpConfig.setContentType(contentType);
        return this;
    }

    public AsyncHttpClient setConnectTimeout(long connectTimeout) {
        httpConfig.setConnectTimeout(connectTimeout);
        return this;
    }

    public AsyncHttpClient setReadTimeout(long readTimeout) {
        httpConfig.setReadTimeout(readTimeout);
        return this;
    }

    /*******************************************get request**********************************************/
    public void get(String url, Map<String, String> params, HttpListener httpListener) {
        get(url, params, headers, httpListener);
    }

    /**
     * get请求
     *
     * @param url          ：请求地址
     * @param params       ：请求参数
     * @param headers      ：请求头
     * @param httpListener ：请求监听
     */
    public void get(String url, Map<String, String> params, Map<String, String> headers, HttpListener httpListener) {
        sendRequest(HttpConstant.GET_METHOD, url, null, params, headers, httpListener);
    }

    /*******************************************post request**********************************************/

    public void post(String url, Map<String, String> params, HttpListener httpListener) {
        post(url, params, headers, httpListener);
    }

    public void post(String url, Map<String, String> params, Map<String, String> headers, HttpListener httpListener) {
        post(url, null, params, headers, httpListener);
    }

    public void postJson(String url, String json, HttpListener httpListener) {
        postJson(url, json, headers, httpListener);
    }

    public void postJson(String url, String json, Map<String, String> headers, HttpListener httpListener) {
        post(url, json, null, headers, httpListener);
    }

    /**
     * post请求
     *
     * @param url          ：请求地址
     * @param json         ：直接上传的json数据
     * @param params       ：请求参数
     * @param headers      ：请求头
     * @param httpListener ：求情监听
     */
    private void post(String url, String json, Map<String, String> params, Map<String, String> headers, HttpListener httpListener) {
        sendRequest(HttpConstant.POST_METHOD, url, json, params, headers, httpListener);
    }

    /**
     * 发送请求
     *
     * @param requestMethod ：请求方式
     * @param url           ：请求地址
     * @param json          ：上传的json
     * @param params        ：上传的参数
     * @param headers       ：请求头
     * @param httpListener  ：监听器
     */
    private void sendRequest(String requestMethod, String url, String json, Map<String, String> params
            , Map<String, String> headers, HttpListener httpListener) {
        Request request = new Request(requestMethod, url, httpConfig);
        request.setJson(json);
        request.setRequestParams(params);
        request.setHeaders(headers);

        Future<?> future = threadPool.submit(new AsyncRequest(handler, request, listenerMap, httpListener));
        if (httpListener != null) {
            List<WeakReference<Future<?>>> futureList = listenerMap.get(httpListener);
            if (futureList == null) {
                futureList = new LinkedList<>();
                listenerMap.put(httpListener, futureList);
            }
            futureList.add(new WeakReference<Future<?>>(future));
        }
    }

    public void cancel(HttpListener httpListener) {
        cancel(httpListener, true);
    }

    /**
     * 取消这个监听下的请求
     *
     * @param httpListener ：网络监听
     * @param remove       ：是否移除改监听
     */
    public void cancel(HttpListener httpListener, boolean remove) {
        if (httpListener != null) {
            List<WeakReference<Future<?>>> futureList = listenerMap.get(httpListener);
            if (futureList != null && futureList.size() > 0) {
                for (int i = 0; i < futureList.size(); i++) {
                    WeakReference<Future<?>> future = futureList.get(i);
                    if (future != null && future.get() != null) {
                        future.get().cancel(true);
                    }
                }
            }
            if (remove) {
                listenerMap.remove(httpListener);
            }
        }
    }

    /**
     * 移除所有的请求
     */
    public void cancelAll() {
        Iterator<HttpListener> iterator = listenerMap.keySet().iterator();
        while (iterator.hasNext()) {
            cancel(iterator.next(), false);
        }
        listenerMap.clear();

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 创建默认的网络请求参数
     */
    private HttpConfig createDefaultHttpConfig() {
        return new HttpConfig(HttpConstant.DEFAULT_CONTENT_TYPE
                , HttpConstant.DEFAULT_ACCEPT
                , HttpConstant.DEFAULT_CHARSET_NAME
                , HttpConstant.DEFAULT_TIME_OUT
                , HttpConstant.DEFAULT_TIME_OUT
                , false);
    }

    public HttpConfig getHttpConfig() {
        return httpConfig;
    }
}
