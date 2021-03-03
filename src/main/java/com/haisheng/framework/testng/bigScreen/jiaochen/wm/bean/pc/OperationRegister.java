package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 活动报名
 */
@Data
public class OperationRegister implements Serializable {
    @JSONField(name = "register_num")
    private Integer registerNum;
    @JSONField(name = "passed_num")
    private Integer passedNum;
    @JSONField(name = "id")
    private Long id;
    @JSONField(name = "title")
    private String title;
    @JSONField(name = "total_quota")
    private Integer totalQuota;
}