package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import lombok.Getter;

public enum followType {
    RENEWAL_INSURANCE("续保咨询", "renew_insurance") ,
    SALES("专属服务咨询(专属销售顾问)", "sales") ,
    AFTER_SALES("专属服务咨询(专属售后顾问)", "after_sales") ,
    USED_CAR("二手车咨询", "used_car") ,
    USED_CAR_ASSESS("二手车评估", "used_car_assess") ,
    ONLINE_EXPERTS("在线专家咨询", "online_experts"),
    BUY_CAR_EVALUATE ("购车评价", "buy_car_evaluate_info") ,
    MAINTAIN_EVALUATE("保养评价", "maintain_evaluate_info") ,
    REPAIR_EVALUATE("维修评价", "repair_evaluate_info") ;

    @Getter
    private final String name;
    @Getter
    private final String type;


    followType(String name, String type){
        this.name=name;
        this.type = type;
    }

}
