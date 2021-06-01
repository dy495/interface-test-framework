package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 28.7. 创建招募活动报名信息项列表 （谢）（2021-01-04）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class ManageRecruitRegisterInformationListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 报名信息类型 见字典《活动报名信息类型》
     * 版本 v2.0
     */
    @JSONField(name = "type")
    private Integer type;

    /**
     * 描述 信息展示名称
     * 版本 v2.0
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 描述 此项信息是否展示
     * 版本 v2.0
     */
    @JSONField(name = "is_show")
    private Boolean isShow;

    /**
     * 描述 此项信息是否必填
     * 版本 v2.0
     */
    @JSONField(name = "is_required")
    private Boolean isRequired;

    /**
     * 描述 提示语
     * 版本 -
     */
    @JSONField(name = "value_tips")
    private String valueTips;

    /**
     * 描述 自定义报名项内容,当type为自定义时必填
     * 版本 -
     */
    @JSONField(name = " custom_condition")
    private JSONObject  customCondition;

    /**
     * 描述 复选框选择项列表，当type为复选框时必填
     * 版本 v3.0
     */
    @JSONField(name = "boxes")
    private JSONArray boxes;

}