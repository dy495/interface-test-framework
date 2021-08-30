package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 38.26. 客户积分变更 (谢志东) v2.2 （2021-03-15）
 *
 * @author wangmin
 * @date 2021-08-30 14:33:06
 */
@Builder
public class CustomerIntegralChangeScene extends BaseScene {
    /**
     * 描述 积分客户id
     * 是否必填 true
     * 版本 v2.2
     */
    private final Long id;

    /**
     * 描述 变更类型 ADD：增加，MINUS：减扣
     * 是否必填 true
     * 版本 v2.2
     */
    private final String changeType;

    /**
     * 描述 变更积分数
     * 是否必填 true
     * 版本 v2.2
     */
    private final Long integral;

    /**
     * 描述 变更说明/备注
     * 是否必填 true
     * 版本 v2.2
     */
    private final String remark;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("change_type", changeType);
        object.put("integral", integral);
        object.put("remark", remark);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/integral-center/customer-integral/change";
    }
}