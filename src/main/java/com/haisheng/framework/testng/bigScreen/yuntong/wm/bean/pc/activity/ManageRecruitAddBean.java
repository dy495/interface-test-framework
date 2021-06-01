package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 28.5. 创建招募活动 （谢）（2021-01-13）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
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