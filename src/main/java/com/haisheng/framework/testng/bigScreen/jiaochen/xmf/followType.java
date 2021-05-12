package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import lombok.Getter;
//跟进任务类型
public enum followType {
    RENEWAL_INSURANCE("续保咨询", "renew_insurance") ,
    SALES("专属服务咨询(专属销售顾问)", "SALES") ,
    AFTER_SALES("专属服务咨询(专属售后顾问)", "after_sales") ,
    USED_CAR("二手车咨询", "used_car") ,
    USED_CAR_ASSESS("二手车评估", "used_car_assess") ,
    ONLINE_EXPERTS("在线专家咨询", "ONLINE_EXPERTS"),
    BUY_CAR_EVALUATE ("购车评价", "buy_car_evaluate_info") ,
    MAINTAIN_EVALUATE("保养评价", "MAINTAIN_EVALUATE_INFO") ,
    REPAIR_EVALUATE("维修评价", "REPAIR_EVALUATE_INFO") ;

    @Getter
    private final String name;
    @Getter
    private final String type;


    followType(String name, String type){
        this.name=name;
        this.type = type;
    }

}
