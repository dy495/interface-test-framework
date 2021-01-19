package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class OperationArticle implements Serializable {
    @JSONField(name = "title")
    private String title;
    @JSONField(name = "id")
    private Long id;
}
