package com.ciba.http.request;

import android.text.TextUtils;

import com.ciba.http.constant.HttpConstant;
import com.ciba.http.entity.Request;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * @author ciba
 * @description 描述
 * @date 2018/12/10
 */
public class RequestUtil {
    public static URL getUrl(String uri, String requestMethod, String paramsString) throws MalformedURLException {
        String tempUri = null;
        if (HttpConstant.GET_METHOD.equalsIgnoreCase(requestMethod) && !TextUtils.isEmpty(paramsString)) {
            // 如果参数不为空且请求方式是GET直接拼接到请求地址
            tempUri = uri + "?" + paramsString;
        }
        return new URL(tempUri == null ? uri : tempUri);
    }

    /**
     * 设置请求头
     */
    public static void setHeaders(Request request, HttpURLConnection httpURLConnection) {
        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip,deflate");
        httpURLConnection.setRequestProperty("accept", "*/*");
        httpURLConnection.setRequestProperty("connection", "Keep-Alive");
        httpURLConnection.setRequestProperty("Content-Type", request.getJson() == null
                ? request.getHttpConfig().getContentType()
                : HttpConstant.JSON_CONTENT_TYPE);
        httpURLConnection.setRequestProperty("Accept", request.getHttpConfig().getAccept());
        // 设置请求头
        Map<String, String> headers = request.getHeaders();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 公共请求参数初始化
     *
     * @throws ProtocolException
     */
    public static void initPublicRequest(HttpURLConnection httpURLConnection, Request request) throws ProtocolException {
        // 设置请求方式
        httpURLConnection.setRequestMethod(request.getRequestMethod());
        // 设置链接超时时间
        httpURLConnection.setConnectTimeout((int) request.getHttpConfig().getConnectTimeout());
        httpURLConnection.setReadTimeout((int) request.getHttpConfig().getReadTimeout());
        httpURLConnection.setAllowUserInteraction(false);
        httpURLConnection.setInstanceFollowRedirects(false);
        // 设置运行输入
        httpURLConnection.setDoInput(true);
        // 设置是否使用缓存
        httpURLConnection.setUseCaches(request.getHttpConfig().isUseCaches());
    }

    /**
     * 不是GET请求的初始化
     *
     * @throws IOException
     */
    public static OutputStream initNotGetRequest(HttpURLConnection httpURLConnection, String paramsString, Request request) throws IOException {
        OutputStream outputStream = null;
        // 设置运行输出
        httpURLConnection.setDoOutput(true);
        // 设置是否使用缓存
        httpURLConnection.setUseCaches(false);
        String postParams = request.getJson() != null ? request.getJson() : paramsString;
        if (postParams != null) {
            byte[] bytes = postParams.getBytes(request.getHttpConfig().getCharsetName());
            httpURLConnection.setFixedLengthStreamingMode(bytes.length);
            outputStream = httpURLConnection.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        }
        return outputStream;
    }

    /**
     * Open the {@link InputStream} of an Http response. This method supports
     * GZIP and DEFLATE responses.
     */
    public static InputStream getInputStream(Request request, HttpURLConnection conn) throws IOException {
        final List<String> contentEncodingValues = conn.getHeaderFields().get("Content-Encoding");
        if (contentEncodingValues != null) {
            for (final String contentEncoding : contentEncodingValues) {
                if (contentEncoding != null) {
                    if (contentEncoding.contains("gzip")) {
                        return new GZIPInputStream(conn.getInputStream());
                    }
                    if (contentEncoding.contains("deflate")) {
                        return new InflaterInputStream(conn.getInputStream(), new Inflater(true));
                    }
                }
            }
        }
        return conn.getInputStream();
    }


    /**
     * 将请求参数Map拼接成字符串
     *
     * @return ：拼接完成的请求参数字符串
     */
    public static String getSpliceParams(Map<String, String> requestParams, String charsetName) throws UnsupportedEncodingException {
        StringBuilder params = new StringBuilder();
        if (requestParams != null) {
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key == null || value == null) {
                    continue;
                }
                params.append(key.trim());
                params.append("=");
                params.append(URLEncoder.encode(value.trim(), charsetName));
                params.append("&");
            }
        }
        if (params.length() > 0) {
            params.deleteCharAt(params.lastIndexOf("&"));
        }
        return params.toString();
    }
}
