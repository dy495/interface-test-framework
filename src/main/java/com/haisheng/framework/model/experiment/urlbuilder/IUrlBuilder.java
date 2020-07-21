package com.haisheng.framework.model.experiment.urlbuilder;

import java.util.Map;

/**
 * url预处理算法接口,不包括？后的参数
 */
public interface IUrlBuilder {
    /**
     * 对url进行预处理
     *
     * @param address 基础url
     * @param apiName 接口的名称或者表达式
     * @return String 拼接好的url
     */
    String build(String address, String apiName);

    /**
     * 生成url
     *
     * @param address 根地址
     * @param apiName 接口的名称或者表达式
     * @param others  其他参数
     * @return String 拼接好的url
     */
    String build(String address, String apiName, String[] others);

    /**
     * 生成url
     *
     * @param address 根地址
     * @param apiName 接口的名称或者表达式
     * @param others  其他参数
     * @return String 拼接好的url
     */
    String build(String address, String apiName, Map<String, String> others);
}
