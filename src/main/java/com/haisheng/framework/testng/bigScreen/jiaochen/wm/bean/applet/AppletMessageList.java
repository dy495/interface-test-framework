package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class AppletMessageList implements Serializable {
    @JSONField(name = "message_type_name")
    private String messageTypeName;

    @JSONField(name = "is_can_evaluate")
    private Boolean isCanEvaluate;

    @JSONField(name = "id")
    private Long id;
}
