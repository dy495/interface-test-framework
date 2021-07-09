package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.personaldata;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.5. 接待总平均分值趋势（谢）
 *
 * @author wangmin
 * @date 2021-05-08 20:23:15
 */
@Builder
public class AppReceptionAverageScoreTrendScene extends BaseScene {
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
     * 描述 员工id 为空则默认为登录账号uid
     * 是否必填 false
     * 版本 v1.0
     */
    private final String salesId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("data_cycle_type", dataCycleType);
        object.put("start_date", startDate);
        object.put("end_date", endDate);
        object.put("salesId", salesId);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/app/personal-data/reception-average-score-trend";
    }
}