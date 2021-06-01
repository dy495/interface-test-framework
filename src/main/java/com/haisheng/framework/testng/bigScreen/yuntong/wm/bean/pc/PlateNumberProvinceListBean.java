package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 34.4. 省份车牌首字母接口
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class PlateNumberProvinceListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

}