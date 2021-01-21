package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.export;

/**
 * @author wangmin
 * @date  2020/12/22  18:01
 */
public enum ExportTypeEnum {
    /**
     * 导出全部
     */
    ALL(false),
    /**
     * 导出当前页
     */
    CURRENT_PAGE(true),
    /**
     * 导出特定数据
     */
    SPECIFIED_DATA(true);

    private boolean isNeedIds;

    ExportTypeEnum(boolean isNeedIds) {
        this.isNeedIds = isNeedIds;
    }

    public boolean isNeedIds() {
        return isNeedIds;
    }
}
