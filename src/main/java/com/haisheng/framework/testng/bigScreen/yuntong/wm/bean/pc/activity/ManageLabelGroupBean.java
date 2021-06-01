package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 28.11. 人群标签组 （谢）（2021-01-20）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class ManageLabelGroupBean implements Serializable {
    /**
     * 描述 总人数
     * 版本 v2.0
     */
    @JSONField(name = "total")
    private Long total;

    /**
     * 描述 标签组
     * 版本 v2.0
     */
    @JSONField(name = "labelGroupRes")
    private JSONArray labelGroupRes;

    /**
     * 描述 人群标签id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Integer id;

    /**
     * 描述 人群标签描述
     * 版本 v2.0
     */
    @JSONField(name = "desc")
    private String desc;

}