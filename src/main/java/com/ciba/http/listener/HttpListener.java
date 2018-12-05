package com.ciba.http.listener;

/**
 * @author ciba
 * @description 描述
 * @date 2018/11/29
 */
public interface HttpListener {
    /**
     * 网络请求开始了
     */
    void onRequestStart();

    /**
     * 网络请求失败
     *
     * @param code  ：错误码
     * @param error ：错误信息
     */
    void onRequestFailed(int code, String error);

    /**
     * 网络请求成功
     *
     * @param result ：返回结果
     */
    void onRequestSuccess(String result);
}
