package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.om;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * 文章卡券状态
 *
 * @author huachengyu
 * @date  2020/7/6  14:56
 */
public enum ArticleVoucherStatusEnum {
    /**
     * 显示
     */
    NOT_RECEIVED("未领取", "立即领取"),
    RECEIVED("已领取", "立即使用"),
    USED("已使用", "已使用"),
    ;

    private String typeName;

    private String desc;

    ArticleVoucherStatusEnum(String typeName, String desc) {
        this.typeName = typeName;
        this.desc = desc;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getDesc() {
        return desc;
    }

    public static ArticleVoucherStatusEnum findByName(String name) {
        Optional<ArticleVoucherStatusEnum> any
                = Arrays.stream(values()).filter(s -> s.name().equals(name)).findAny();
        Preconditions.checkArgument(any.isPresent(), "卡券状态枚举不存在");
        return any.get();
    }

}
