package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.departmentdata;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 4.3. 阶段数据轮播列表（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class AppLinkDataCarouselBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 接待环节 见字典《接待环节》
     * 版本 v1.0
     */
    @JSONField(name = "type")
    private Integer type;

    /**
     * 描述 接待环节名称 见字典《接待环节》
     * 版本 v1.0
     */
    @JSONField(name = "type_name")
    private String typeName;

    /**
     * 描述 平均分
     * 版本 v1.0
     */
    @JSONField(name = "average_score")
    private Integer averageScore;

    /**
     * 描述 合格分
     * 版本 v1.0
     */
    @JSONField(name = "qualified_score")
    private Integer qualifiedScore;

    /**
     * 描述 平均分达成率
     * 版本 v1.0
     */
    @JSONField(name = "average_ratio")
    private Integer averageRatio;

    /**
     * 描述 合格率
     * 版本 v1.0
     */
    @JSONField(name = "qualified_ratio")
    private Integer qualifiedRatio;

}