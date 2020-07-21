package com.ciba.http.client;

import com.ciba.http.constant.HttpConfig;
import com.ciba.http.constant.HttpConstant;

import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

/**
 * @author ciba
 * @description 描述
 * @date 2020/7/21
 */
public class BaseHttpClient<T extends BaseHttpClient> {
    private final HttpConfig httpConfig;
    private Map<String, String> headers;

    public BaseHttpClient() {
        httpConfig = createDefaultHttpConfig();
    }

    /**
     * 设置请求头
     */
    public T setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return (T) this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public T setContentType(String contentType) {
        httpConfig.setContentType(contentType);
        return (T) this;
    }

    public T setConnectTimeout(long connectTimeout) {
        httpConfig.setConnectTimeout(connectTimeout);
        return (T) this;
    }

    public T setReadTimeout(long readTimeout) {
        httpConfig.setReadTimeout(readTimeout);
        return (T) this;
    }

    public HostnameVerifier getHostnameVerifier() {
        return httpConfig.getHostnameVerifier();
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        httpConfig.setHostnameVerifier(hostnameVerifier);
    }

    public SSLSocketFactory getSslSocketFactory() {
        return httpConfig.getSslSocketFactory();
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        httpConfig.setSslSocketFactory(sslSocketFactory);
    }

    public HttpConfig getHttpConfig() {
        return httpConfig;
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
