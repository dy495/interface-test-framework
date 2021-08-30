package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.aftervoicerecord;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.3. 部门接待分页（华成裕）
 *
 * @author wangmin
 * @date 2021-08-30 14:39:23
 */
@Builder
public class AppAfterVoiceRecordDepartmentPageScene extends BaseScene {
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
     * 描述 排序字段 取值见《集团接待排序字典》
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
        object.put("data_cycle_type", dataCycleType);
        object.put("start_date", startDate);
        object.put("end_date", endDate);
        object.put("order_column", orderColumn);
        object.put("is_reverse", isReverse);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/app/after-voice-record/department-page";
    }
}