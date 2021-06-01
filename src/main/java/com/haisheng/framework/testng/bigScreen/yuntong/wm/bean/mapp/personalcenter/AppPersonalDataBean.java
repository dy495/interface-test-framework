package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.personalcenter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 11.7. 我的数据（张小龙）（2020-12-16）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppPersonalDataBean implements Serializable {
    /**
     * 描述 核销码
     * 版本 v2.0
     */
    @JSONField(name = "verification_code")
    private String verificationCode;

}