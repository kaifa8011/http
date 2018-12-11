package com.ciba.http.client;

import com.ciba.http.constant.HttpConfig;
import com.ciba.http.constant.HttpConstant;
import com.ciba.http.entity.Request;
import com.ciba.http.request.SyncRequest;

import java.util.Map;

/**
 * @author ciba
 * @description 网络请求
 * @date 2018/11/29
 */
public class SyncHttpClient {
    private final HttpConfig httpConfig;
    private Map<String, String> headers;

    public SyncHttpClient() {
        httpConfig = createDefaultHttpConfig();
    }

    /**
     * 设置请求头
     */
    public SyncHttpClient setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public SyncHttpClient setContentType(String contentType) {
        httpConfig.setContentType(contentType);
        return this;
    }

    public SyncHttpClient setConnectTimeout(long connectTimeout) {
        httpConfig.setConnectTimeout(connectTimeout);
        return this;
    }

    public SyncHttpClient setReadTimeout(long readTimeout) {
        httpConfig.setReadTimeout(readTimeout);
        return this;
    }

    /*******************************************get request**********************************************/
    public String get(String url, Map<String, String> params) {
        return get(url, params, headers);
    }

    /**
     * get请求
     *
     * @param url     ：请求地址
     * @param params  ：请求参数
     * @param headers ：请求头
     */
    public String get(String url, Map<String, String> params, Map<String, String> headers) {
        return sendRequest(HttpConstant.GET_METHOD, url, null, params, headers);
    }

    /*******************************************post request**********************************************/

    public String post(String url, Map<String, String> params) {
        return post(url, params, headers);
    }

    public String post(String url, Map<String, String> params, Map<String, String> headers) {
        return post(url, null, params, headers);
    }

    public String postJson(String url, String json) {
        return postJson(url, json, headers);
    }

    public String postJson(String url, String json, Map<String, String> headers) {
        return post(url, json, null, headers);
    }

    /**
     * post请求
     *
     * @param url     ：请求地址
     * @param json    ：直接上传的json数据
     * @param params  ：请求参数
     * @param headers ：请求头
     */
    private String post(String url, String json, Map<String, String> params, Map<String, String> headers) {
        return sendRequest(HttpConstant.POST_METHOD, url, json, params, headers);
    }

    /**
     * 发送请求
     *
     * @param requestMethod ：请求方式
     * @param url           ：请求地址
     * @param json          ：上传的json
     * @param params        ：上传的参数
     * @param headers       ：请求头
     */
    private String sendRequest(String requestMethod, String url, String json, Map<String, String> params
            , Map<String, String> headers) {
        Request request = new Request(requestMethod, url, httpConfig);
        request.setJson(json);
        request.setRequestParams(params);
        request.setHeaders(headers);
        return new SyncRequest(request).run();
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
}
