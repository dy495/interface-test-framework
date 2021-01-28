package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 卡券操作记录
 *
 * @author wangmin
 * @date 2021/1/25 14:58
 */
@Data
public class VoucherChangeRecord implements Serializable {
    @JSONField(name = "id")
    private Long id;

    /**
     * 时间
     */
    @JSONField(name = "time")
    private String time;

    /**
     * 操作人
     */
    @JSONField(name = "operate_sale_name")
    private String operateSaleName;

    /**
     * 操作人账号
     */
    @JSONField(name = "operate_sale_account")
    private String operateSaleAccount;

    /**
     * 操作角色
     */
    @JSONField(name = "operate_sale_role")
    private String operateSaleRole;

    /**
     * 操作事项
     */
    @JSONField(name = "change_item")
    private String changeItem;
}
