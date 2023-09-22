package com.uangel.sim.http;

/**
 * @author dajin kim
 */
public enum HttpEnums {
    CONTENT_TYPE("content-type"), AUTHORIZATION("authorization"),
    APPLICATION_JSON("application/json");


    private final String str;

    HttpEnums(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}
