package com.haisheng.framework.model.experiment.scene;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.experiment.core.Api;
import com.haisheng.framework.model.experiment.base.RequestLog;
import com.haisheng.framework.model.experiment.commend.EnumCommendMethod;
import lombok.Setter;
import lombok.experimental.Accessors;
import okhttp3.Response;

/**
 * APP创建客户接口
 *
 * @author wangmin
 * @date 2020/7/21 11:11
 */
public class AddCustomerAppHttpScene extends BaseHttpScene {

    private final String customerId;
    private final String analysisCustomerId;
    private final int customerLevel;
    private final int customerSelectType;
    private final String customerName;
    private final String customerPhone;
    private final int visitCount;
    private final int belongsArea;
    private final String alreadyCar;
    private final int testDriveCar;
    private final int sehandAssess;
    private final int carAssess;
    private final String preBuyTime;
    private final int likeCar;
    private final String compareCar;
    private final int showPrice;
    private final int payType;
    private final int buyCar;
    private final int buyCarType;
    private final int buyCarAttribute;
    private final String reamrks;
    private final String comment;
    private final String nextReturnVisitDate;

    AddCustomerAppHttpScene(Builder builder) {
        super(builder);
        this.customerId = builder.customerId;
        this.analysisCustomerId = builder.analysisCustomerId;
        this.customerLevel = builder.customerLevel;
        this.customerSelectType = builder.customerSelectType;
        this.customerName = builder.customerName;
        this.customerPhone = builder.customerPhone;
        this.visitCount = builder.visitCount;
        this.belongsArea = builder.belongsArea;
        this.alreadyCar = builder.alreadyCar;
        this.testDriveCar = builder.testDriveCar;
        this.sehandAssess = builder.sehandAssess;
        this.carAssess = builder.carAssess;
        this.preBuyTime = builder.preBuyTime;
        this.likeCar = builder.likeCar;
        this.compareCar = builder.compareCar;
        this.showPrice = builder.showPrice;
        this.payType = builder.payType;
        this.buyCar = builder.buyCar;
        this.buyCarType = builder.buyCarType;
        this.buyCarAttribute = builder.buyCarAttribute;
        this.reamrks = builder.reamrks;
        this.comment = builder.comment;
        this.nextReturnVisitDate = builder.nextReturnVisitDate;
    }

    @Override
    public RequestLog<Response> invokeApi() {
        JSONObject body = new JSONObject();
        body.put("customerId", customerId);
        body.put("analysisCustomerId", analysisCustomerId);
        body.put("customerLevel", customerLevel);
        body.put("customerSelectType", customerSelectType);
        body.put("customerName", customerName);
        body.put("customerPhone", customerPhone);
        body.put("visitCount", visitCount);
        body.put("belongsArea", belongsArea);
        body.put("alreadyCar", alreadyCar);
        body.put("testDriveCar", testDriveCar);
        body.put("sehandAssess", sehandAssess);
        body.put("carAssess", carAssess);
        body.put("preBuyTime", preBuyTime);
        body.put("likeCar", likeCar);
        body.put("compareCar", compareCar);
        body.put("showPrice", showPrice);
        body.put("payType", payType);
        body.put("buyCar", buyCar);
        body.put("buyCarType", buyCarType);
        body.put("buyCarAttribute", buyCarAttribute);
        body.put("reamrks", reamrks);
        body.put("comment", comment);
        body.put("nextReturnVisitDate", nextReturnVisitDate);
        Api api = new Api.Builder().mediaType("application/json")
                .method(EnumCommendMethod.POST)
                .requestBody(JSON.toJSONString(body))
                .build();
        try {
            return getUser().invoke(api, "");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder<Builder> {
        private String customerId;
        private String analysisCustomerId;
        private int customerLevel;
        private int customerSelectType;
        private String customerName;
        private String customerPhone;
        private int visitCount;
        private int belongsArea;
        private String alreadyCar;
        private int testDriveCar;
        private int sehandAssess;
        private int carAssess;
        private String preBuyTime;
        private int likeCar;
        private String compareCar;
        private int showPrice;
        private int payType;
        private int buyCar;
        private int buyCarType;
        private int buyCarAttribute;
        private String reamrks;
        private String comment;
        private String nextReturnVisitDate;

        @Override
        public AddCustomerAppHttpScene build() {
            return new AddCustomerAppHttpScene(this);
        }
    }
}
