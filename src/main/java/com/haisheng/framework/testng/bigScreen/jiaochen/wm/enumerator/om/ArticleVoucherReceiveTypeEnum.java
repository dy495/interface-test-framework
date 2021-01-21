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
public enum ArticleVoucherReceiveTypeEnum {
    /**
     * 卡券领取方式
     */
    SIGN_UP("报名成功后领取"),
    ARTICLE_BUTTON("页面领取"),
    ;

    private String typeName;

    ArticleVoucherReceiveTypeEnum(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public static ArticleVoucherReceiveTypeEnum findByName(String name) {
        Optional<ArticleVoucherReceiveTypeEnum> any
                = Arrays.stream(values()).filter(s -> s.name().equals(name)).findAny();
        Preconditions.checkArgument(any.isPresent(), "卡券状态枚举不存在");
        return any.get();
    }
}
