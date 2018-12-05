package com.ciba.http.constant;

/**
 * @author ciba
 * @description 网络配置信息
 * @date 2018/11/29
 */
public class HttpConfig {
    private String contentType;
    private String accept;
    private String charsetName;
    private long connectTimeout;
    private long readTimeout;
    private boolean useCaches;

    public HttpConfig(String contentType, String accept, String charsetName
            , long connectTimeout, long readTimeout, boolean useCaches) {
        this.contentType = contentType;
        this.accept = accept;
        this.charsetName = charsetName;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.useCaches = useCaches;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    public boolean isUseCaches() {
        return useCaches;
    }

    public void setUseCaches(boolean useCaches) {
        this.useCaches = useCaches;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }
}
