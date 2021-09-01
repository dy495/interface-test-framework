package com.haisheng.framework.testng.bigScreen.itemMall.common.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RegionDataBean implements Serializable {

    private String region;

    List<RegionTrendBean> regionTrendBeanList;
}
