package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.activity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 26.16. 活动推广 （谢）（2020-12-23）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class ManagePromotionBean implements Serializable {
    /**
     * 描述 小程序二维码url
     * 版本 v2.0
     */
    @JSONField(name = "applet_code_url")
    private String appletCodeUrl;

}