package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 11.3. 智能提醒项分页 （谢）（2021-01-05）
 *
 * @author wangmin
 * @date 2021-03-31 12:29:35
 */
@Builder
public class IntelligentRemindPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 提醒项目（提醒类型）名
     * 是否必填 false
     * 版本 v2.0
     */
    private final String item;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("item", item);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/manage/intelligent-remind/page";
    }
}