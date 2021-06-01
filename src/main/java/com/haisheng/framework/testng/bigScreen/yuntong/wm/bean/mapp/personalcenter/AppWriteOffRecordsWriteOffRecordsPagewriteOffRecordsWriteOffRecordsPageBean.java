package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.personalcenter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 10.2. app 我的核销记录分页（张小龙）
 *
 * @author wangmin
 * @date 2021-06-01 18:39:24
 */
@Data
public class AppWriteOffRecordsWriteOffRecordsPagewriteOffRecordsWriteOffRecordsPageBean implements Serializable {
    /**
     * 描述 总数 首次查询或刷新时返回
     * 版本 v1.0
     */
    @JSONField(name = "total")
    private Long total;

    /**
     * 描述 本次查询最后一条数据主键
     * 版本 v1.0
     */
    @JSONField(name = "last_value")
    private JSONObject lastValue;

    /**
     * 描述 展示列（部分接口返回列按权限展示时需要）
     * 版本 v4.0
     */
    @JSONField(name = "key_list")
    private JSONArray keyList;

    /**
     * 描述 key名称（展示列名称）
     * 版本 v4.0
     */
    @JSONField(name = "key_name")
    private String keyName;

    /**
     * 描述 key值（实际取值key）
     * 版本 v4.0
     */
    @JSONField(name = "key_value")
    private String keyValue;

    /**
     * 描述 返回的结果list
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "type_name")
    private String typeName;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "card_name")
    private String cardName;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "user_name")
    private String userName;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "card_number")
    private String cardNumber;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "write_off_time")
    private String writeOffTime;

}