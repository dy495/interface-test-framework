package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 积分变更记录记录
 *
 * @author wangmin
 * @date 2021/1/27 20:02
 */
@Data
public class ChangeRecord implements Serializable {

    @JSONField(name = "customer_phone")
    private String customerPhone;

    @JSONField(name = "integral")
    private String integral;

    @JSONField(name = "left")
    private String left;

    @JSONField(name = "operator_name")
    private String operatorName;

    @JSONField(name = "operator_account")
    private String operatorAccount;

    @JSONField(name = "remark")
    private String remark;


}
