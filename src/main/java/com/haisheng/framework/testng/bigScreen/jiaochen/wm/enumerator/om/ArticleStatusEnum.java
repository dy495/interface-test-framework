package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.om;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangmin
 * @date  2020/7/6  14:56
 */
public enum ArticleStatusEnum {
    /**
     * 显示
     */
    SHOW("显示中", true),
    REMOVE("已下架", false);

    private String typeName;

    private boolean isValidStatus;

    ArticleStatusEnum(String typeName, boolean isValidStatus) {
        this.typeName = typeName;
        this.isValidStatus = isValidStatus;
    }

    public String getTypeName() {
        return typeName;
    }

    public static List<String> findValidStatusList() {
        return Arrays.stream(values()).filter(t -> t.isValidStatus)
                .map(Enum::name).collect(Collectors.toList());
    }

    public static ArticleStatusEnum findByName(String name) {
        return Arrays.stream(values()).filter(s->s.name().equals(name))
                .findAny().orElse(REMOVE);
    }

}
