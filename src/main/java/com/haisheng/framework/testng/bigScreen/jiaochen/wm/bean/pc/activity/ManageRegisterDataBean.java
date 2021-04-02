package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.activity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 26.20. 活动报名数据 （谢）（2020-12-23）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class ManageRegisterDataBean implements Serializable {
    /**
     * 描述 活动名额
     * 版本 v2.0
     */
    @JSONField(name = "quota")
    private Integer quota;

    /**
     * 描述 报名总人数
     * 版本 v2.0
     */
    @JSONField(name = "total")
    private Integer total;

    /**
     * 描述 待审批人数
     * 版本 v2.0
     */
    @JSONField(name = "wait")
    private Integer wait;

    /**
     * 描述 报名成功人数
     * 版本 v2.0
     */
    @JSONField(name = "passed")
    private Integer passed;

    /**
     * 描述 报名失败人数
     * 版本 v2.0
     */
    @JSONField(name = "failed")
    private Integer failed;

}