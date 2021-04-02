package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.voucher;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 6.5. 审批数据统计（张小龙） v3.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class ApplyApprovalInfoBean implements Serializable {
    /**
     * 描述 全部审核
     * 版本 v3.0
     */
    @JSONField(name = "total_approval")
    private Long totalApproval;

    /**
     * 描述 待审核
     * 版本 v3.0
     */
    @JSONField(name = "wait_approval")
    private Long waitApproval;

    /**
     * 描述 审核通过
     * 版本 v3.0
     */
    @JSONField(name = "pass_approval")
    private Long passApproval;

    /**
     * 描述 审核未通过
     * 版本 v3.0
     */
    @JSONField(name = "fail_approval")
    private Long failApproval;

}