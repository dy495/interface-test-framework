package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 28.21. 活动数据 （谢）（2021-01-26）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class ManageDataBean implements Serializable {
    /**
     * 描述 全部活动
     * 版本 v2.0
     */
    @JSONField(name = "total")
    private Integer total;

    /**
     * 描述 待审批
     * 版本 v2.0
     */
    @JSONField(name = "wait")
    private Integer wait;

    /**
     * 描述 通过
     * 版本 v2.0
     */
    @JSONField(name = "passed")
    private Integer passed;

    /**
     * 描述 未通过
     * 版本 v2.0
     */
    @JSONField(name = "failed")
    private Integer failed;

}