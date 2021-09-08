package com.haisheng.framework.testng.bigScreen.itemBasic.base.scene;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Api {
    private final String ipPort;
    private final String path;
    private final String requestBody;
    private final String method;
    private final String url;
    private final Map<String, Object> formDataParam = new HashMap<>();

    public Api(Builder builder) {
        this.path = builder.path;
        this.ipPort = builder.ipPort;
        this.method = builder.method;
        this.requestBody = builder.requestBody;
        this.url = builder.ipPort + builder.path;
        this.formDataParam.putAll(builder.formDataParam);
    }

    public static class Builder {
        private String ipPort;
        private String path;
        private String method;
        private String requestBody;
        private final Map<String, String> formDataParam = new HashMap<>();

        public Builder ipPort(String ipPort) {
            this.ipPort = ipPort;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder requestBody(String requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public Builder formDataParam(String key, String value) {
            this.formDataParam.put(key, value);
            return this;
        }

        public Api build() {
            return new Api(this);
        }
    }
}