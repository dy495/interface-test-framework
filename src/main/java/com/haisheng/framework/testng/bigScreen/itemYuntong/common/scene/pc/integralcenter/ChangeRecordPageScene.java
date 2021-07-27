package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 43.27. 客户积分变更记录分页 (谢志东) v2.2 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:29
 */
@Builder
public class ChangeRecordPageScene extends BaseScene {
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
     * 描述 变更时间范围查询开始日期
     * 是否必填 false
     * 版本 v2.2
     */
    private final String changeStart;

    /**
     * 描述 变更时间范围查询结束日期
     * 是否必填 false
     * 版本 v2.2
     */
    private final String changeEnd;

    /**
     * 描述 客户联系方式
     * 是否必填 false
     * 版本 v2.2
     */
    private final String customerPhone;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("change_start", changeStart);
        object.put("change_end", changeEnd);
        object.put("customer_phone", customerPhone);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/integral-center/customer-integral/change-record/page";
    }
}