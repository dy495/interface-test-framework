package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.record;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 19.3. 导出记录 v1.0 (池)
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class ExportPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    @Builder.Default
    private Integer page = 1;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    @Builder.Default
    private Integer size = 10;

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

    /**
     * 描述 ignore
     * 是否必填 false
     * 版本 -
     */
    private final Boolean isAll;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("user", user);
        object.put("type", type);
        object.put("isAll", isAll);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/record/export/page";
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}