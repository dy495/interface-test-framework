package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.SelectReception;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 增发记录
 *
 * @author wangmin
 * @date 2021/1/27 20:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EvaluatePage extends SelectReception {

    /**
     * 描述
     */
    @JSONField(name = "describe")
    private String describe;

    /**
     * 客户经理
     */
    @JSONField(name = "service_sale_name")
    private String serviceSaleName;

    /**
     * 评价内容
     */
    @JSONField(name = "suggestion")
    private String suggestion;

    /**
     * 星级
     */
    @JSONField(name = "score")
    private Integer score;

    /**
     * 跟进备注
     */
    @JSONField(name = "follow_up_remark")
    private String followUpRemark;

}
