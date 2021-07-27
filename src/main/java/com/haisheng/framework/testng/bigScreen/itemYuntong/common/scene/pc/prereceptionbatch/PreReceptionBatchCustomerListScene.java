package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.prereceptionbatch;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 16.3. 批次分页列表 （华）（2021-07-12）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class PreReceptionBatchCustomerListScene extends BaseScene {
    /**
     * 描述 批次Id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long batchId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("batch_id", batchId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/pre-reception-batch/customer-list";
    }
}