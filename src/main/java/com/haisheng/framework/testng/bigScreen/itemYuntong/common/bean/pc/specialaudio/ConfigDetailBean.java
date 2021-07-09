package com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.pc.specialaudio;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 5.6. 条件配置详情（张）
 *
 * @author wangmin
 * @date 2021-06-03 17:30:48
 */
@Data
public class ConfigDetailBean implements Serializable {
    /**
     * 描述 最低时长 单位min 限制时长时必填
     * 版本 v1.0
     */
    @JSONField(name = "duration")
    private Integer duration;

    /**
     * 描述 最低分数 限制分数时必填
     * 版本 v1.0
     */
    @JSONField(name = "score")
    private Integer score;

    /**
     * 描述 区域合规配置列表 限制区域合规时必填
     * 版本 v1.0
     */
    @JSONField(name = "region_limits")
    private JSONArray regionLimits;

    /**
     * 描述 接待环节 使用"获取指定枚举值列表"接口获取 enum_type为 RECEPTION_LINKS
     * 版本 v1.0
     */
    @JSONField(name = "type")
    private Integer type;

    /**
     * 描述 设备id 获取设备列表 接口获取
     * 版本 v1.0
     */
    @JSONField(name = "device_id_list")
    private JSONArray deviceIdList;

}