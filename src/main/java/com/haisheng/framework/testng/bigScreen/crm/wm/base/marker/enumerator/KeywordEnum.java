package com.haisheng.framework.testng.bigScreen.crm.wm.base.marker.enumerator;


import com.google.common.base.Preconditions;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2021/3/11 15:38
 */
public enum KeywordEnum {

    PACKAGE("package", "Package"),

    IMPORT("import", "Import"),

    PUBLIC("public", "Public"),
    ;

    KeywordEnum(String keyword, String transfer) {
        this.keyword = keyword;
        this.transfer = transfer;
    }

    @Getter
    public final String keyword;
    @Getter
    public final String transfer;

    public static KeywordEnum findByKeyword(String keyword) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(keyword), "关键字不存在");
        Optional<KeywordEnum> any = Arrays.stream(values()).filter(e -> e.getKeyword().equals(keyword)).findAny();
        Preconditions.checkArgument(any.isPresent(), "关键字不存在");
        return any.get();
    }

    /**
     * 转换关键词
     *
     * @param str str
     * @return string
     */
    public static String transferEqualKeyword(String str) {
        return Arrays.stream(KeywordEnum.values()).filter(e -> str.equals(e.getKeyword())).map(e -> str.replace(e.getKeyword(), e.getTransfer())).findFirst().orElse(str);
    }
}
