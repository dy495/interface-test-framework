package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 26.5. 创建招募活动 （谢）（2021-01-13）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class ManageRecruitAddBean implements Serializable {
    /**
     * 描述 id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

}