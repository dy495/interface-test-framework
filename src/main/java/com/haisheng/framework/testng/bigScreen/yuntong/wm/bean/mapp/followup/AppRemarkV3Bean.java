package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.followup;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.5. app 跟进列表备注 v3 (池)(2020-03-11)
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppRemarkV3Bean implements Serializable {
    /**
     * 描述 备注
     * 版本 v3.0
     */
    @JSONField(name = "remark")
    private String remark;

}