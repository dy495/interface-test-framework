package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class DetailMessage implements Serializable {

    /**
     * 数据源名称
     */
    private String name;
    /**
     * 不存在重复结果
     */
    private Integer noReception;
    /**
     * 存在重复结果
     */
    private Integer hasReception;
}
