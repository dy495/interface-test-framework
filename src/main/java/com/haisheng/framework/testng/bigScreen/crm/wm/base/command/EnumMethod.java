package com.haisheng.framework.testng.bigScreen.crm.wm.base.command;

import lombok.Getter;

public enum EnumMethod {
    /**
     * HTTP的get请求
     */
    GET("get", "http", new GetCommand()),
    /**
     * http的post请求
     */
    POST("post", "http", new PostCommand()),

    /**
     * http的delete请求
     */
//    DELETE("delete", "http", new GetCommand()),
    /**
     * http的put请求
     */
//    PUT("put", "http", new PostCommand()),
    /**
     * http的post-multipart请求
     */
//    MULTIPART("multipart", "http", new MultipartCommand()),
    /**
     * hsf接口
     */
//    HSF("hsf", "hsf", null);
    ;

    EnumMethod(String name, String protocol, BaseCommand command) {
        this.name = name;
        this.command = command;
        this.protocol = protocol;
    }

    @Getter
    public String name;
    @Getter
    public String protocol;
    @Getter
    public BaseCommand command;


}
