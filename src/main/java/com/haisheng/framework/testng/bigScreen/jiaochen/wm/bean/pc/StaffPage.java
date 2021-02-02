package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 员工管理页
 *
 * @author wangmin
 * @date 2021/2/1 11:01
 */
@Data
public class StaffPage implements Serializable {
    @JSONField(name = "uid")
    private String uid;
    @JSONField(name = "phone")
    private String phone;
    @JSONField(name = "name")
    private String name;
}
