package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class DetailMessage implements Serializable {

    private String name;

    private Integer noReception;

    private Integer hasReception;
}
