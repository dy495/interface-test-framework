package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.record;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 19.2. 导入记录导出 (华成裕) （2020-12-24）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class ImportRecordExportScene extends BaseScene {
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
     * 描述 导入位置
     * 是否必填 false
     * 版本 v1.0
     */
    private final String type;

    /**
     * 描述 操作人员
     * 是否必填 false
     * 版本 v1.0
     */
    private final String user;

    /**
     * 描述 导入时间(开始)
     * 是否必填 false
     * 版本 v1.0
     */
    private final String startTime;

    /**
     * 描述 导入结束时间
     * 是否必填 false
     * 版本 v1.0
     */
    private final String endTime;

    /**
     * 描述 导出类型 ALL：导出全部，CURRENT_PAGE：导出当前页，SPECIFIED_DATA：导出特定数据
     * 是否必填 true
     * 版本 v2.0
     */
    private final String exportType;

    /**
     * 描述 导出数据id列表，特定数据时必填
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray ids;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("type", type);
        object.put("user", user);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/record/import-record/export";
    }
}