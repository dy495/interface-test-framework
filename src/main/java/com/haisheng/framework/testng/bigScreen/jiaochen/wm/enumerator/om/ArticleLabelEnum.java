package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.om;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 */
public enum ArticleLabelEnum {

    /**
     * 优惠、特价、福利、红包、礼品、热销、推荐
     **/
    PREFERENTIAL("优惠"),


    /**
     * 特价
     **/
    BARGAIN("特价"),

    /**
     * 福利
     **/
    WELFARE("福利"),

    /**
     * 红包
     **/
    RED_PAPER("红包"),

    /**
     * 礼品
     **/
    GIFT("礼品"),

    /**
     * 热销
     **/
    SELL_WELL("热销"),

    /**
     * 推荐
     **/
    RECOMMEND("推荐"),

    ;

    private String desc;

    ArticleLabelEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static ArticleLabelEnum findByName(String name) {
        Optional<ArticleLabelEnum> any
                = Arrays.stream(values()).filter(s -> s.name().equals(name)).findAny();
        Preconditions.checkArgument(any.isPresent(), "标签类型枚举不存在");
        return any.get();
    }
}
