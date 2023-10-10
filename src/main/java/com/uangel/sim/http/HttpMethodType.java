package com.uangel.sim.http;

import lombok.Getter;

/**
 * @author dajin kim
 */
@Getter
public enum HttpMethodType {
    GET("get"), POST("post"), PUT("put"), DELETE("delete");

    private final String str;

    HttpMethodType(String str) {
        this.str = str;
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
