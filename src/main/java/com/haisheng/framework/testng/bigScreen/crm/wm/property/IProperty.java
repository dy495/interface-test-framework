package com.haisheng.framework.testng.bigScreen.crm.wm.property;

public interface IProperty {

    /**
     * 获取标识符，不应为空
     *
     * @return String
     */
    String getKey();

    /**
     * 获取名称，不应为空
     *
     * @return String
     */
    String getName();

    /**
     * 设置描述信息，建议description只存储非重要的描述信息，所以允许任意时刻进行修改
     *
     * @param description 描述信息
     */
    void setDescription(String description);

    /**
     * 获取描述，可能为空
     *
     * @return String
     */
    String getDescription();

    /**
     * 获取最近一次的错误信息
     */
    String getErrorMsg();

    /**
     * 清理错误信息string builder，避免内存溢出
     */
    void clearErrorMsg();
}
