package com.ciba.http.request;

import com.ciba.http.entity.Request;

/**
 * @author ciba
 * @description 网络请求的Runnable
 * @date 2018/11/29
 */
public class SyncRequest extends BaseRequest {

    public SyncRequest(Request request) {
        super(request);
    }

    public String run() {
        return execute();
    }
}
