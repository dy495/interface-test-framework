package com.haisheng.framework.testng.bigScreen.crm.wm.base.command;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class Api {
    private final String authorization;
    private final String referer;
    private final String contentType;
    private final JSONObject requestBody;
    private final Map<String, String> headers = new LinkedHashMap<>(32);
    private final Map<String, Object> urlParams = new LinkedHashMap<>(32);

    public Api(Builder builder) {
        this.authorization = builder.authorization;
        this.referer = builder.referer;
        this.requestBody = builder.requestBody;
        this.contentType = builder.contentType;
        this.headers.putAll(builder.headers);
    }

    public static class Builder {
        private String authorization;
        private String referer;
        private String contentType;
        private JSONObject requestBody;
        private final Map<String, String> headers = new LinkedHashMap<>(32);
        private final Map<String, Object> urlParams = new LinkedHashMap<>(32);

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

        public Builder referer(String referer) {
            this.referer = referer;
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
