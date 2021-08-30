package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.monthlytask;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 15.2. 
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class MonthlyTaskDetailScene extends BaseScene {
    /**
     * 描述 销售id
     * 是否必填 false
     * 版本 v5.0
     */
    private final String saleId;

    /**
     * 描述 月份
     * 是否必填 false
     * 版本 v5.0
     */
    private final String month;

    /**
     * 描述 销售任务数量
     * 是否必填 false
     * 版本 v5.0
     */
    private final Integer receptionTaskNum;

    /**
     * 描述 试驾任务数量
     * 是否必填 false
     * 版本 v5.0
     */
    private final Integer testDriveTaskNum;

    /**
     * 描述 订车任务数量
     * 是否必填 false
     * 版本 v5.0
     */
    private final Integer orderTaskNum;

    /**
     * 描述 交车任务数量
     * 是否必填 false
     * 版本 v5.0
     */
    private final Integer deliverTaskNum;

    /**
     * 描述 唯一id
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("sale_id", saleId);
        object.put("month", month);
        object.put("reception_task_num", receptionTaskNum);
        object.put("test_drive_task_num", testDriveTaskNum);
        object.put("order_task_num", orderTaskNum);
        object.put("deliver_task_num", deliverTaskNum);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/monthly-task/detail";
    }
}