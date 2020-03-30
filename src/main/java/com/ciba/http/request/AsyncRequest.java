package com.ciba.http.request;

import android.os.Handler;
import android.text.TextUtils;

import com.ciba.http.entity.Request;
import com.ciba.http.listener.HttpListener;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author ciba
 * @description 网络请求的Runnable
 * @date 2018/11/29
 */
public class AsyncRequest extends BaseRequest implements Runnable {
    private final Handler handler;
    private final HttpListener httpListener;
    private final Map<HttpListener, List<WeakReference<Future<?>>>> listenerMap;

    public AsyncRequest(Handler handler, Request request
            , Map<HttpListener, List<WeakReference<Future<?>>>> listenerMap
            , HttpListener httpListener) {
        super(request);
        this.handler = handler;
        this.listenerMap = listenerMap;
        this.httpListener = httpListener;
    }

    @Override
    public void run() {
        onStart();
        String result = execute();
        if (resultCode != HttpURLConnection.HTTP_OK && TextUtils.isEmpty(result)) {
            onFailed();
        } else {
            onSuccess(result);
        }
    }

    /**
     * 请求开始
     */
    private void onStart() {
        if (canCallBack()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    httpListener.onRequestStart();
                }
            });
        }
    }

    /**
     * 接口调用失败了
     */
    private void onFailed() {
        if (canCallBack()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    httpListener.onRequestFailed(errorCode, errorMessage);
                }
            });
        }
    }

    /**
     * 接口调用成功了
     *
     * @param result ：返回结果
     */
    private void onSuccess(final String result) {
        if (canCallBack()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    httpListener.onRequestSuccess(result);
                    if (needResponseHeader) {
                        httpListener.onRequestSuccess(result, responseHeader);
                    }
                }
            });
        }
    }

    /**
     * @return ：是否是可以回调
     */
    private boolean canCallBack() {
        return handler != null && httpListener != null && listenerMap != null && listenerMap.get(httpListener) != null;
    }
}
