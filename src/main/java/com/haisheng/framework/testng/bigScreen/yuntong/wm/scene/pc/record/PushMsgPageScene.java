package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.record;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.5. 消息记录 v1.0 (池)
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class PushMsgPageScene extends BaseScene {
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
     * 描述 推送原因
     * 是否必填 false
     * 版本 v1.0
     */
    private final String messageType;

    /**
     * 描述 客户类型
     * 是否必填 false
     * 版本 1.0
     */
    private final String customerType;

    /**
     * 描述 推送时间(开始)
     * 是否必填 false
     * 版本 -
     */
    private final String startTime;

    /**
     * 描述 推送时间(结束)
     * 是否必填 false
     * 版本 -
     */
    private final String endTime;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String group;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("message_type", messageType);
        object.put("customer_type", customerType);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("group", group);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/record/push-msg/page";
    }
}