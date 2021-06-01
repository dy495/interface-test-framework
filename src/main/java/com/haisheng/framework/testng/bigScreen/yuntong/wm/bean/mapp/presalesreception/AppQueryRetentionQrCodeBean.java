package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 7.12. 获取留资二维码 v3.1 （2021-05-06）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppQueryRetentionQrCodeBean implements Serializable {
    /**
     * 描述 留资二维码
     * 版本 v1.0
     */
    @JSONField(name = "qr_code_url")
    private String qrCodeUrl;

}