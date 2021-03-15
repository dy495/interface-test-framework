package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/manage/intelligent-remind/add的接口
 *
 * @author wangmin
 * @date 2021-03-15 10:12:39
 */
@Builder
public class IntelligentRemindAddScene extends BaseScene {
    /**
     * 描述 提醒项目（提醒类型）
     * 是否必填 true
     * 版本 v2.0
     */
    private final String item;

    /**
     * 描述 提醒说明
     * 是否必填 true
     * 版本 v2.0
     */
    private final String content;

    /**
     * 描述 奖励卡券
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray vouchers;

    /**
     * 描述 奖励卡券有效期天数
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer effectiveDays;

    /**
     * 描述 提醒天数
     * 是否必填 false
     * 版本 v2.0
     */
    private final String days;

    /**
     * 描述 提醒里程数
     * 是否必填 false
     * 版本 v2.0
     */
    private final String mileage;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("item", item);
        object.put("content", content);
        object.put("vouchers", vouchers);
        object.put("effective_days", effectiveDays);
        object.put("days", days);
        object.put("mileage", mileage);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/manage/intelligent-remind/add";
    }
}