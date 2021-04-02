package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 26.6. 创建内容营销活动 （谢）v3.0（2021-03-16）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class ManageContentMarketingAddBean implements Serializable {
    /**
     * 描述 id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

}