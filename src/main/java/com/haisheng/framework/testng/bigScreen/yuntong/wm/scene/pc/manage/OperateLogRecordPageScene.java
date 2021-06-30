package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 12.1. 操作记录分页（谢）（2020-12-22）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class OperateLogRecordPageScene extends BaseScene {
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
     * 描述 数据类型 通用枚举接口获取，key为LOG_DATA_TYPE
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer dataType;

    /**
     * 描述 操作类型 通用枚举接口获取，key为LOG_OPERATION_TYPE
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer operationType;

    /**
     * 描述 操作账号
     * 是否必填 false
     * 版本 v2.0
     */
    private final String account;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("data_type", dataType);
        object.put("operation_type", operationType);
        object.put("account", account);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/manage/operate-log/record-page";
    }
}