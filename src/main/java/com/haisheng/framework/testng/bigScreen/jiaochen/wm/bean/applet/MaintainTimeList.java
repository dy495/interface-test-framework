package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangmin
 * @date 2021/1/29 14:26
 */
@Data
public class MaintainTimeList implements Serializable {
    @JSONField(name = "is_full")
    private Boolean isFull;
    @JSONField(name = "price")
    private String price;
    @JSONField(name = "id")
    private Integer id;
    @JSONField(name = "time")
    private String time;
}
