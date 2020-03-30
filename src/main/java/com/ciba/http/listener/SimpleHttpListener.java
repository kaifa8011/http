package com.ciba.http.listener;

import java.util.List;
import java.util.Map;

/**
 * @author ciba
 * @description 描述
 * @date 2018/12/6
 */
public class SimpleHttpListener implements HttpListener {
    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFailed(int code, String error) {

    }

    @Override
    public void onRequestSuccess(String result) {

    }

    @Override
    public void onRequestSuccess(String result, Map<String, List<String>> responseHeader) {

    }
}
