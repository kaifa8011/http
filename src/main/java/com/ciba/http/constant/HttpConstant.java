package com.ciba.http.constant;

/**
 * @author ciba
 * @description 描述
 * @date 2018/12/3
 */
public class HttpConstant {
    public static final String POST_METHOD = "POST";
    public static final String GET_METHOD = "GET";

    public static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String DEFAULT_ACCEPT = "application/json";
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";

    public static final String CIBA_UNZIP_KEY = "CIBA_UNZIP_KEY";
    public static final String UNZLIB = "UNZLIB";

    public static final long DEFAULT_TIME_OUT = 5000;
    public static final int SUCCESS_CODE = 200;
    public static final int OTHER_CODE = -1001;

    public static final String ERROR_MESSAGE_CODE_NOT_200 = "response code not 200";
    public static final String ERROR_MESSAGE_UNKNOW = "unknow error";
}
