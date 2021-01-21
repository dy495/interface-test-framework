package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.common;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date  2020/10/15  10:52
 */
public enum ImportStatusEnum {
    /**
     * 导入完成
     */
    IMPORTING("导入中", false),
    SUCCESS("成功", true),
    FAILED("失败", false);

    private String statusName;

    private boolean isCanDownload;

    ImportStatusEnum(String statusName, boolean isCanDownload) {
        this.statusName = statusName;
        this.isCanDownload = isCanDownload;
    }

    public String getStatusName() {
        return statusName;
    }

    public boolean isCanDownload() {
        return isCanDownload;
    }

    public static ImportStatusEnum findByType(String type) {
        Optional<ImportStatusEnum> any = Arrays.stream(values()).filter(e -> e.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "导入状态不正确");
        return any.get();
    }
}
