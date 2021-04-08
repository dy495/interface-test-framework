package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsParamBean implements Serializable {

    private Long firstCategory;
    private Long secondCategory;
    private Long thirdCategory;
    private Long brandId;
    private Long specificationsId;
    private Long specificationsIdTwo;
}
