package com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
public class Api {
    private final String ipPort;
    private final String path;
    private final String requestBody;
    private final String method;
    private final String url;

    public Api(Builder builder) {
        this.path = builder.path;
        this.ipPort = builder.ipPort;
        this.method = builder.method;
        this.requestBody = builder.requestBody;
        this.url = builder.ipPort + builder.path;
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder {
        private String ipPort;
        private String path;
        private String method;
        private String requestBody;

        public Api build() {
            return new Api(this);
        }
    }
}