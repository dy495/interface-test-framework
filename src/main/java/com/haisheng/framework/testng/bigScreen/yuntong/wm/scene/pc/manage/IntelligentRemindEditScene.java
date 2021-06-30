package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 24.2. 编辑智能提醒项 （谢）（2021-01-05）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class IntelligentRemindEditScene extends BaseScene {
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

    /**
     * 描述 提醒项目id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("item", item);
        object.put("content", content);
        object.put("vouchers", vouchers);
        object.put("effective_days", effectiveDays);
        object.put("days", days);
        object.put("mileage", mileage);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/manage/intelligent-remind/edit";
    }
}