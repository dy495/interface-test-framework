package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.shop;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 25.7. 查看门店洗车二维码(池)
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class ShowShopExtensionBean implements Serializable {
    /**
     * 描述 洗车核销二维码
     * 版本 v2.0
     */
    @JSONField(name = "wash_car_qr_code")
    private String washCarQrCode;

}