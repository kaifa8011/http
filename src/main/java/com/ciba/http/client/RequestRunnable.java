package com.ciba.http.client;

import android.os.Handler;
import android.text.TextUtils;

import com.ciba.http.constant.HttpConstant;
import com.ciba.http.entity.Request;
import com.ciba.http.listener.HttpListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author ciba
 * @description 网络请求的Runnable
 * @date 2018/11/29
 */
public class RequestRunnable implements Runnable {
    private final Handler handler;
    private final Request request;
    private final Map<HttpListener, List<WeakReference<Future<?>>>> listenerMap;
    private final HttpListener httpListener;

    public RequestRunnable(Handler handler, Request request, Map<HttpListener, List<WeakReference<Future<?>>>> listenerMap
            , HttpListener httpListener) {
        this.handler = handler;
        this.request = request;
        this.listenerMap = listenerMap;
        this.httpListener = httpListener;
    }

    @Override
    public void run() {
        onStart();
        URL url;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            // 获取拼接的请求参数
            String paramsString = getSpliceParams();

            if (!TextUtils.isEmpty(paramsString) && HttpConstant.GET_METHOD.equalsIgnoreCase(request.getRequestMethod())) {
                // 如果参数不为空且请求方式是GET直接拼接到请求地址
                url = new URL(request.getUrl() + "?" + paramsString);
            } else {
                url = new URL(request.getUrl());
            }
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Content-Type", request.getHttpConfig().getContentType());
            // 设置接收返回值的格式
            httpURLConnection.setRequestProperty("Accept", request.getHttpConfig().getAccept());
            // 设置请求头
            setHeaders(httpURLConnection);
            // 设置请求方式
            httpURLConnection.setRequestMethod(request.getRequestMethod());
            // 设置链接超时时间
            httpURLConnection.setConnectTimeout((int) request.getHttpConfig().getConnectTimeout());
            httpURLConnection.setReadTimeout((int) request.getHttpConfig().getReadTimeout());
            // 发送POST请求必须设置如下两行
            // 设置运行输入
            httpURLConnection.setDoInput(true);
            // 设置运行输出
            httpURLConnection.setDoOutput(true);
            // 设置是否使用缓存
            httpURLConnection.setUseCaches(request.getHttpConfig().isUseCaches());

            if (!HttpConstant.GET_METHOD.equalsIgnoreCase(request.getRequestMethod())) {
                // 如果请求方式不是GET则将参数添加到body
                PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
                // 发送请求参数（请求中有Json则发送Json否则发送拼接的参数）
                printWriter.write(TextUtils.isEmpty(request.getJson()) ? paramsString : request.getJson());
                // flush输出流的缓冲
                printWriter.flush();
                printWriter.close();
            }
            // 获取结果返回码
            int code = httpURLConnection.getResponseCode();
            if (HttpConstant.SUCCESS_CODE == code) {
                // 获取网络的输入流
                inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, request.getHttpConfig().getCharsetName()));
                //最好在将字节流转换为字符流的时候 进行转码
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
                onSuccess(builder.toString());
            } else {
                onFailed(code, "response code not 200");
            }
        } catch (IOException e) {
            onFailed(HttpConstant.OTHER_CODE, e.getMessage());
        } finally {
            if (httpURLConnection != null) {
                // 最后记得关闭连接
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    bufferedReader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 设置请求头
     */
    private void setHeaders(HttpURLConnection httpURLConnection) {
        Map<String, String> headers = request.getHeaders();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 将请求参数Map拼接成字符串
     *
     * @return ：拼接完成的请求参数字符串
     */
    private String getSpliceParams() {
        StringBuilder params = new StringBuilder();
        Map<String, String> requestParams = request.getRequestParams();
        if (requestParams != null) {
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                params.append(entry.getKey());
                params.append("=");
                params.append(entry.getValue());
                params.append("&");
            }
        }
        if (params.length() > 0) {
            params.deleteCharAt(params.lastIndexOf("&"));
        }
        return params.toString();
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
     *
     * @param code  ：失败码
     * @param error ：错误信息
     */
    private void onFailed(final int code, final String error) {
        if (canCallBack()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    httpListener.onRequestFailed(code, error);
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
