package com.ciba.http.request;

import com.ciba.http.constant.HttpConstant;
import com.ciba.http.entity.Request;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author ciba
 * @description 描述
 * @date 2018/12/10
 */
public abstract class BaseRequest {
    protected int errorCode;
    protected String errorMessage;
    protected final Request request;

    public BaseRequest(Request request) {
        this.request = request;
    }

    protected String execute() {
        errorCode = HttpConstant.OTHER_CODE;
        errorMessage = HttpConstant.ERROR_MESSAGE_UNKNOW;

        URL url;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        InputStream errorStream = null;
        OutputStream outputStream = null;
        BufferedReader bufferedReader = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        String result = null;
        String uri = null;
        String paramsString = null;
        try {
            uri = request.getUrl();
            paramsString = RequestUtil.getSpliceParams(request.getRequestParams(), request.getHttpConfig().getCharsetName());
            url = RequestUtil.getUrl(uri, request.getRequestMethod(), paramsString);

            httpURLConnection = (HttpURLConnection) url.openConnection();

            RequestUtil.initPublicRequest(httpURLConnection, request);

            RequestUtil.setHeaders(request, httpURLConnection);

            if (HttpConstant.GET_METHOD.equalsIgnoreCase(request.getRequestMethod())) {
                httpURLConnection.connect();
            } else {
                outputStream = RequestUtil.initNotGetRequest(httpURLConnection, paramsString, request);
            }
            // 获取结果返回码
            int code = httpURLConnection.getResponseCode();
            if (HttpConstant.SUCCESS_CODE == code) {
                Map<String, String> requestParams = request.getRequestParams();
                // 获取网络的输入流
                inputStream = RequestUtil.getInputStream(request, httpURLConnection);
                if (requestParams != null
                        && HttpConstant.UNZLIB.equals(requestParams.get(HttpConstant.CIBA_UNZIP_KEY))) {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    int len = 1024;
                    byte tmp[] = new byte[len];
                    int i;
                    while ((i = inputStream.read(tmp, 0, len)) > 0) {
                        byteArrayOutputStream.write(tmp, 0, i);
                    }
                    result = RequestUtil.decompress(byteArrayOutputStream.toByteArray());
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, request.getHttpConfig().getCharsetName()));
                    // 最好在将字节流转换为字符流的时候 进行转码
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        builder.append(line);
                    }
                    result = builder.toString();
                }
            } else {
                errorStream = httpURLConnection.getErrorStream();
                failed(code, HttpConstant.ERROR_MESSAGE_CODE_NOT_200);
            }
        } catch (Exception e) {
            failed(HttpConstant.OTHER_CODE, e.getMessage());
        } finally {
            uri = null;
            paramsString = null;
            if (httpURLConnection != null) {
                // 最后记得关闭连接
                httpURLConnection.disconnect();
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                    byteArrayOutputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (errorStream != null) {
                try {
                    errorStream.close();
                    errorStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
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
        return result;
    }

    private void failed(int code, String message) {
        this.errorCode = code;
        this.errorMessage = message;
    }


}
