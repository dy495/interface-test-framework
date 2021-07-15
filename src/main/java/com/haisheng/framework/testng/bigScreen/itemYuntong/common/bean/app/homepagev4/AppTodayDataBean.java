package com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.homepagev4;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.2. 今日数据
 *
 * @author wangmin
 * @date 2021-05-07 19:22:48
 */
@Data
public class AppTodayDataBean implements Serializable {

    @JSONField(name = "pre_pending_reception")
    private String prePendingReception;

    @JSONField(name = "pre_pending_follow")
    private String prePendingFollow;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "id")
    private String id;
}