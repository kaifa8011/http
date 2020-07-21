package com.ciba.http.client;

import com.ciba.http.constant.HttpConstant;
import com.ciba.http.entity.Request;
import com.ciba.http.request.SyncRequest;

import java.util.Map;

/**
 * @author ciba
 * @description 网络请求
 * @date 2018/11/29
 */
public class SyncHttpClient extends BaseHttpClient<SyncHttpClient> {

    public SyncHttpClient() {
        super();
    }

    /*******************************************get request**********************************************/
    public String get(String url, Map<String, String> params) {
        return get(url, params, getHeaders());
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
        return post(url, params, getHeaders());
    }

    public String post(String url, Map<String, String> params, Map<String, String> headers) {
        return post(url, null, params, headers);
    }

    public String postJson(String url, String json) {
        return postJson(url, json, getHeaders());
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
        Request request = new Request(requestMethod, url, getHttpConfig());
        request.setJson(json);
        request.setRequestParams(params);
        request.setHeaders(headers);
        return new SyncRequest(request).run();
    }
}
