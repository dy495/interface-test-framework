package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/customer-manage/wechat-customer/page/export的接口
 *
 * @author wangmin
 * @date 2021-03-15 14:06:44
 */
@Builder
public class WechatCustomerPageExportScene extends BaseScene {
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
     * 描述 客户电话
     * 是否必填 false
     * 版本 1.0
     */
    private final String customerPhone;

    /**
     * 描述 活跃类型
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer activeType;

    /**
     * 描述 开始时间
     * 是否必填 false
     * 版本 v1.0
     */
    private final String startTime;

    /**
     * 描述 结束时间
     * 是否必填 false
     * 版本 v1.0
     */
    private final String endTime;

    /**
     * 描述 会员类型
     * 是否必填 false
     * 版本 v2.0
     */
    private final String vipType;

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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("customer_phone", customerPhone);
        object.put("active_type", activeType);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("vip_type", vipType);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/customer-manage/wechat-customer/page/export";
    }
}