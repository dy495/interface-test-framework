package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 28.19. 活动推广 （谢）（2020-12-23）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
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