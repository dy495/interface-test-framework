package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 增发记录
 *
 * @author wangmin
 * @date 2021/1/27 20:02
 */
@Data
public class AdditionalRecord implements Serializable {

    /**
     * 增发数量
     */
    @JSONField(name = "additional_num")
    private String additionalNum;
}
