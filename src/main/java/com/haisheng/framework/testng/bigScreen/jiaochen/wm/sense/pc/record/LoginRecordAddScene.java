package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.record;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 19.7. 添加登陆日志（后端接口，前端无需处理）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class LoginRecordAddScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final String saleId;

    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final Long loginTime;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("sale_id", saleId);
        object.put("login_time", loginTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/record/login-record/add";
    }
}