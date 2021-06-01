package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.brand;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 30.11. 品牌车系详情（谢）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class CarStyleDetailBean implements Serializable {
    /**
     * 描述 车系id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 车系名称
     * 版本 v1.0
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 描述 生产商
     * 版本 v1.0
     */
    @JSONField(name = "manufacturer")
    private String manufacturer;

    /**
     * 描述 品牌id
     * 版本 v2.0
     */
    @JSONField(name = "brand_id")
    private Long brandId;

    /**
     * 描述 品牌名称
     * 版本 v1.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 车系上线时间
     * 版本 v1.0
     */
    @JSONField(name = "online_time")
    private String onlineTime;

}