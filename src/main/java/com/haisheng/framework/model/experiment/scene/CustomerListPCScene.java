package com.haisheng.framework.model.experiment.scene;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.experiment.base.RequestLog;
import com.haisheng.framework.model.experiment.commend.EnumCommendMethod;
import com.haisheng.framework.model.experiment.core.Api;
import com.haisheng.framework.model.experiment.core.EnumUser;
import lombok.Setter;
import lombok.experimental.Accessors;
import okhttp3.Response;

/**
 * @author wangmin
 * @date 2020/7/21 14:04
 */
public class CustomerListPCScene extends BaseHttpScene {
    private final String startTime;
    private final String endTime;

    protected CustomerListPCScene(Builder builder) {
        super(builder);
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
    }

    @Override
    protected RequestLog<Response> invokeApi() {
        JSONObject body = new JSONObject();
        body.put("startTime", startTime);
        body.put("endTime", endTime);
        Api api = new Api.Builder()
                .apiName("porsche/customer/list")
                .mediaType("application/json")
                .method(EnumCommendMethod.POST)
                .requestBody(JSON.toJSONString(body))
                .build();
        try {
            return getUser().invoke(api, EnumAddress.WIN_SENSE.getAddress());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder<Builder> {
        public Builder() {
            user(EnumUser.WANG_MIN.getUser());
        }

        private String startTime;
        private String endTime;

        @Override
        public CustomerListPCScene build() {
            return new CustomerListPCScene(this);
        }
    }
}
