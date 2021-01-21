package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.export;

/**
 * @author wangmin
 * @date  2020/12/22  18:01
 */
public enum ExportStatusEnum {

    /**
     * 导出中
     */
    EXPORT_ING("导出中"),
    /**
     * 导出完成
     */
    EXPORT_DONE("导出完成"),
    /**
     * 导出失败
     */
    EXPORT_FAIL("导出失败"),
    ;

    private String statusName;

    public String getStatusName() {
        return statusName;
    }

    ExportStatusEnum(String statusName) {
        this.statusName = statusName;
    }
}
