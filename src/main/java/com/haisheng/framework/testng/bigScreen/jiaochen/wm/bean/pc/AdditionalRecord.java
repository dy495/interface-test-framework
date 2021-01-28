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
public class AdditionalRecord extends SelectReception {

    /**
     * 增发数量
     */
    @JSONField(name = "additional_num")
    private String additionalNum;
}
