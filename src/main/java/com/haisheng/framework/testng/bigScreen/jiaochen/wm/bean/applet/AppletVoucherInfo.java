package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangmin
 * @date 2021/1/26 15:31
 */
@Data
public class AppletVoucherInfo implements Serializable {

    /**
     * 客户卡券id
     */
    @JSONField(name = "id")
    private String id;

    /**
     * 卡券名称
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 卡券码
     */
    @JSONField(name = "voucher_code")
    private String voucherCode;

    /**
     * 状态
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 状态名称
     */
    @JSONField(name = "status_name")
    private String statusName;


}
