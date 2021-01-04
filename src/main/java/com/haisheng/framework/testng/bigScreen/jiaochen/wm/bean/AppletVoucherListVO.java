package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class AppletVoucherListVO implements Serializable {
    @JSONField(name = "id")
    public Long id;
    @JSONField(name = "title")
    public String title;
}
