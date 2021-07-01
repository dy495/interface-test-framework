package com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
public class Api {
    private final String method;
    private final String url;
    private final String requestBody;

    public Api(Builder builder) {
        this.method = builder.method;
        this.requestBody = builder.requestBody;
        this.url = builder.url;
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder {
        private String method;
        private String url;
        private String requestBody;

        public Api build() {
            return new Api(this);
        }
    }
}