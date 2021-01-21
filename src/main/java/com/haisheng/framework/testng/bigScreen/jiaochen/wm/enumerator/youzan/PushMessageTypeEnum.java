package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.youzan;


import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2021/1/15 11:23
 */
public enum PushMessageTypeEnum {

    /**
     * 上架
     */
    PAY("trade_TradePaid","交易支付消息");



    private String name;

    private String value;

    PushMessageTypeEnum(String name,String value ) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }


    public String getValue() {
        return value;
    }


    public static PushMessageTypeEnum findByName(String name) {
        Optional<PushMessageTypeEnum> any = Arrays.stream(values()).filter(t -> t.getName().equals(name)).findAny();
        return any.orElse(null);
    }
}
