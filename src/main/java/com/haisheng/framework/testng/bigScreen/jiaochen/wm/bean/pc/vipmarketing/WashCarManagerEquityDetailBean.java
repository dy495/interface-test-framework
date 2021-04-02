package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vipmarketing;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.7. 洗车权益说明(查看)
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class WashCarManagerEquityDetailBean implements Serializable {
    /**
     * 描述 洗车权益说明
     * 版本 v2.0
     */
    @JSONField(name = "equity_detail")
    private String equityDetail;

}