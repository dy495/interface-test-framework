package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.commodity;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2021/1/11 15:51
 */
public enum DistributionMannerEnum {

    /**
     * 自提
     */
    takes("自提");


    private String name;

    DistributionMannerEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }



    public static DistributionMannerEnum findById(String type) {
        Optional<DistributionMannerEnum> any = Arrays.stream(values()).filter(t -> t.name().equals(type))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "配送方式不存在");
        return any.get();
    }
}
