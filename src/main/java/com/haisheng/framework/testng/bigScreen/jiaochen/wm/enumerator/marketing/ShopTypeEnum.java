package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/11/13 3:26 PM
 * @desc
 */
public enum ShopTypeEnum {
    /**
     *
     */
    SHOP(0, "门店业务", "本司员工"),
    DIFF(1, "异业合作", "异业伙伴"),
    ALL(2, "全部", "全部");

    private final Integer id;

    private final String name;

    private final String desc;

    ShopTypeEnum(Integer id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public static ShopTypeEnum findById(Integer id) {
        Preconditions.checkArgument(null != id, "业务类型不存在");
        Optional<ShopTypeEnum> any = Arrays.stream(values()).filter(v -> v.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "业务类型不存在");
        return any.get();
    }
}
