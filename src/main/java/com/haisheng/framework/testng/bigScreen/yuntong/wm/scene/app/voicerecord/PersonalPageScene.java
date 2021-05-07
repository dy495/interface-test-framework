package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.voicerecord;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.2. 接待语音记录提交（谢）
 *
 * @author wangmin
 * @date 2021-05-07 19:05:22
 */
@Builder
public class PersonalPageScene extends BaseScene {
    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 上次请求最后值
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONObject lastValue;

    /**
     * 描述 员工id 为空则默认为登录账号uid
     * 是否必填 false
     * 版本 v1.0
     */
    private final String salesId;

    /**
     * 描述 查询数据周期 取值见字典表《数据查询周期》
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer dataCycleType;

    /**
     * 描述 自定义查询开始日期，周期类型为自定义时必填
     * 是否必填 false
     * 版本 v1.0
     */
    private final String startDate;

    /**
     * 描述 自定义查询结束日期，周期类型为自定义时必填
     * 是否必填 false
     * 版本 v1.0
     */
    private final String endDate;

    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String customerName;

    /**
     * 描述 排序环节 全部时传 null
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer orderLinkType;

    /**
     * 描述 排序字段 取值见《接待环节》
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer orderColumn;

    /**
     * 描述 是否倒序 不传默认false
     * 是否必填 false
     * 版本 v1.0
     */
    private final Boolean isReverse;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("last_value", lastValue);
        object.put("salesId", salesId);
        object.put("data_cycle_type", dataCycleType);
        object.put("start_date", startDate);
        object.put("end_date", endDate);
        object.put("customer_name", customerName);
        object.put("order_link_type", orderLinkType);
        object.put("order_column", orderColumn);
        object.put("is_reverse", isReverse);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/app/voice-record/personal-page";
    }
}