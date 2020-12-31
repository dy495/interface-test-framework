package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum EnumArticleStatus {
    /**
     * 显示
     */
    SHOW("显示中", true),
    SCHEDULING("排期中", true),
    EXPIRE("已过期", false),
    REMOVE("已下架", false);

    private final String typeName;

    private final boolean isValidStatus;

    EnumArticleStatus(String typeName, boolean isValidStatus) {
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

    public static EnumArticleStatus findByDateRange(LocalDate start, LocalDate end) {
        LocalDate today = LocalDate.now();
        if (start.isAfter(today)) {
            return EnumArticleStatus.SCHEDULING;
        }
        if (end.isBefore(today)) {
            return EnumArticleStatus.EXPIRE;
        }
        return EnumArticleStatus.SHOW;
    }

    public static EnumArticleStatus findByName(String name) {
        return Arrays.stream(values()).filter(s -> s.name().equals(name))
                .findAny().orElse(REMOVE);
    }

}