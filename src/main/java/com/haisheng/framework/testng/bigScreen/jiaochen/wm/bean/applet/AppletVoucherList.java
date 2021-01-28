package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class AppletVoucherList implements Serializable {
    @JSONField(name = "id")
    private Long id;
    @JSONField(name = "title")
    private String title;
}
