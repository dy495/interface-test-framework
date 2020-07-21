package com.haisheng.framework.model.experiment.core;

import com.haisheng.framework.model.experiment.commend.EnumCommendMethod;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * API对象
 * 提供所有请求需要的参数
 *
 * @author wangmin
 * @date 2020/7/20 21:35
 */
@Getter
public class Api {
    private final String mediaType;
    private final Map<String, String> headers = new LinkedHashMap<>(32);
    private final Map<String, String> urlParam = new LinkedHashMap<>(32);
    private final String requestBody;
    private final String apiName;
    private final boolean ignoreSSL;
    private final EnumCommendMethod method;


    Api(Builder builder) {
        this.headers.putAll(builder.headers);
        this.mediaType = builder.mediaType;
        this.requestBody = builder.requestBody;
        this.ignoreSSL = builder.ignoreSSL;
        this.method = builder.method;
        this.apiName = builder.apiName;
        this.urlParam.putAll(builder.urlParam);
    }

    public static class Builder {
        private Map<String, String> headers = new LinkedHashMap<>(32);
        private Map<String, String> urlParam = new LinkedHashMap<>(32);
        private String mediaType = "application/json";
        private String requestBody;
        private boolean ignoreSSL = true;
        private EnumCommendMethod method = EnumCommendMethod.POST;
        private String apiName;

        public Builder requestBody(String body) {
            this.requestBody = body;
            return this;
        }

        public Builder mediaType(String mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public Builder ignoreSSL(boolean ignoreSSL) {
            this.ignoreSSL = ignoreSSL;
            return this;
        }

        public Builder method(EnumCommendMethod method) {
            this.method = method;
            return this;
        }

        public Builder apiName(String apiName) {
            this.apiName = apiName;
            return this;
        }

        public Builder urlParam(String key, String value) {
            this.urlParam.put(key,value);
            return this;
        }

        public Api build() {
            return new Api(this);
        }
    }
}
