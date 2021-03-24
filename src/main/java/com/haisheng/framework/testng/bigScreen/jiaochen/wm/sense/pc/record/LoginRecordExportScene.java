package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.record;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.8. 登陆日志导出 (杨) v3.0的接口
 *
 * @author wangmin
 * @date 2021-03-24 14:32:26
 */
@Builder
public class LoginRecordExportScene extends BaseScene {
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
     * 描述 登陆日期
     * 是否必填 false
     * 版本 v3.0
     */
    private final String loginDate;

    /**
     * 描述 登陆名称
     * 是否必填 false
     * 版本 v3.0
     */
    private final String loginName;

    /**
     * 描述 登陆账号
     * 是否必填 false
     * 版本 v3.0
     */
    private final String loginAccount;

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
        object.put("login_date", loginDate);
        object.put("login_name", loginName);
        object.put("login_account", loginAccount);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/record/login-record/export";
    }
}