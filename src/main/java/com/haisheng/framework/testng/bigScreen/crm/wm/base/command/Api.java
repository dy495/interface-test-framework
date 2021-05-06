package com.haisheng.framework.testng.bigScreen.crm.wm.base.command;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class Api {
    private final String authorization;
    private final String contentType;
    private final JSONObject requestBody;
    private final EnumMethod method;
    private final Map<String, String> headers = new LinkedHashMap<>(32);
    private final Map<String, Object> urlParams = new LinkedHashMap<>(32);

    public Api(Builder builder) {
        this.method = builder.method;
        this.authorization = builder.authorization;
        this.requestBody = builder.requestBody;
        this.contentType = builder.contentType;
        this.headers.putAll(builder.headers);
        this.urlParams.putAll(builder.urlParams);
    }

    public static class Builder {
        private EnumMethod method;
        private String authorization;
        private String contentType;
        private JSONObject requestBody;
        private final Map<String, String> headers = new LinkedHashMap<>(32);
        private final Map<String, Object> urlParams = new LinkedHashMap<>(32);

        public Builder method(EnumMethod method) {
            this.method = method;
            return this;
        }

        public Builder requestBody(JSONObject requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder authorization(String authorization) {
            this.authorization = authorization;
            return this;
        }

        public Builder urlParam(String key, Object value) {
            this.urlParams.put(key, value);
            return this;
        }

        public Api build() {
            return new Api(this);
        }
    }
}
