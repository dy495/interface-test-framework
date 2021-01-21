package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/11/19 11:03 AM
 * @desc
 */
public enum PackageUseTypeEnum {
    /**
     * 接待车辆
     */
    RECEPTION_CAR(0, "接待车辆"),
    ALL_CAR(1, "全部车辆"),
    ;

    private Integer id;

    private String name;

    PackageUseTypeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public static PackageUseTypeEnum findById(Integer id) {
        Optional<PackageUseTypeEnum> any = Arrays.stream(values()).filter(t -> t.id.equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "限用车辆类型不存在");
        return any.get();
    }
}
