package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 8.3. 接待评价二维码（池）v4.0 （2021-05-08 更新）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppEvaluateQrCodeBean implements Serializable {
    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "evaluate_qr_code_url")
    private String evaluateQrCodeUrl;

}