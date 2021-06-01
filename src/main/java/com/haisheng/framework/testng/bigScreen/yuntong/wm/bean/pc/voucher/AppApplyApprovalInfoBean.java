package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.voucher;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 8.5. 审批数据统计（张小龙） v3.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class AppApplyApprovalInfoBean implements Serializable {
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