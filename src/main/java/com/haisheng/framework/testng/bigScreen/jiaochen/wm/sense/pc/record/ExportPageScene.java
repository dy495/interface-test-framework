package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.record;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/record/export/page的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:17
 */
@Builder
public class ExportPageScene extends BaseScene {
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
     * 描述 导出时间-开始
     * 是否必填 false
     * 版本 v1.0
     */
    private final String startTime;

    /**
     * 描述 导出时间-结束
     * 是否必填 false
     * 版本 v1.0
     */
    private final String endTime;

    /**
     * 描述 操作人员
     * 是否必填 false
     * 版本 v1.0
     */
    private final String user;

    /**
     * 描述 导出位置
     * 是否必填 false
     * 版本 v1.0
     */
    private final String type;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("user", user);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/record/export/page";
    }
}