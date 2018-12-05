package com.ciba.http.entity;

import com.ciba.http.constant.HttpConfig;

import java.util.Map;

/**
 * @author ciba
 * @description 描述
 * @date 2018/11/29
 */
public class Request {
    private String requestMethod;
    private String url;
    private String json;
    private HttpConfig httpConfig;
    private Map<String, String> requestParams;
    private Map<String, String> headers;

    public Request(String requestMethod, String url, HttpConfig httpConfig) {
        this.requestMethod = requestMethod;
        this.url = url;
        this.httpConfig = httpConfig;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpConfig getHttpConfig() {
        return httpConfig;
    }

    public void setHttpConfig(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Map<String, String> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(Map<String, String> requestParams) {
        this.requestParams = requestParams;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
