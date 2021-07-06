package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.packagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 17.11. 套餐购买记录导出 （华成裕）（2020-12-23）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class BuyPackageRecordExportScene extends BaseScene {
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
     * 描述 套餐名
     * 是否必填 false
     * 版本 v1.0
     */
    private final String packageName;

    /**
     * 描述 发放人
     * 是否必填 false
     * 版本 v1.0
     */
    private final String sender;

    /**
     * 描述 发放时间范围查询开始日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String startTime;

    /**
     * 描述 发放时间范围查询结束日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String endTime;

    /**
     * 描述 支付类型 通用枚举接口，key为 PACKAGE_PAY_TYPE
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer sendType;

    /**
     * 描述 客户手机号
     * 是否必填 false
     * 版本 v2.3
     */
    private final String customerPhone;

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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("package_name", packageName);
        object.put("sender", sender);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("send_type", sendType);
        object.put("customer_phone", customerPhone);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/package-manage/buy-package-record/export";
    }
}