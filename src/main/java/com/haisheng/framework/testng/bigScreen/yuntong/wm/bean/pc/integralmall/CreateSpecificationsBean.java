package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 25.17. 创建商品规格 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class CreateSpecificationsBean implements Serializable {
    /**
     * 描述 常见成功返回id
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

}