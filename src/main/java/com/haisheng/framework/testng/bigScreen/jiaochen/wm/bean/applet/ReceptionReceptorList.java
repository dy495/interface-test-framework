package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 售后接待员工列表
 * @author wangmin
 * @date 2021/1/29 17:08
 */
@Data
public class ReceptionReceptorList implements Serializable {
    @JSONField(name = "uid")
    private String uid;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "phone")
    private String phone;
}
