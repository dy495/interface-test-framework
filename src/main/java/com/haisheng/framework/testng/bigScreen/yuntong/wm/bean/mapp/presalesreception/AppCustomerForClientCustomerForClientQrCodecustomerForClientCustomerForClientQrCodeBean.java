package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 8.1. 客户留咨二维码（池）v4.0 （2021-05-08 更新）
 *
 * @author wangmin
 * @date 2021-06-01 18:39:24
 */
@Data
public class AppCustomerForClientCustomerForClientQrCodecustomerForClientCustomerForClientQrCodeBean implements Serializable {
    /**
     * 描述 二维码
     * 版本 v4.0
     */
    @JSONField(name = "qr_code")
    private String qrCode;

}