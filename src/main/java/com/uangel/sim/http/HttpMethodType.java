package com.uangel.sim.http;

/**
 * @author dajin kim
 */
public enum HttpMethodType {
    GET("get"), POST("post"), PUT("put"), DELETE("delete");

    private final String str;

    HttpMethodType(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public static HttpMethodType getMethodType(String method) {
        switch (method.toLowerCase()) {
            case "post":
                return POST;
            case "put":
                return PUT;
            case "delete":
                return DELETE;
            default:
                return GET;
        }
    }

}
