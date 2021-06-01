package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 25.19. 商品规格详情 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class SpecificationsDetailBean implements Serializable {
    /**
     * 描述 规格id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 规格名称
     * 版本 v2.0
     */
    @JSONField(name = "specifications_name")
    private String specificationsName;

    /**
     * 描述 所属品类
     * 版本 v2.0
     */
    @JSONField(name = "belongs_category")
    private Long belongsCategory;

    /**
     * 描述 品类列表
     * 版本 v2.0
     */
    @JSONField(name = "specifications_list")
    private JSONArray specificationsList;

    /**
     * 描述 规格id
     * 版本 -
     */
    @JSONField(name = "specifications_id")
    private Long specificationsId;

    /**
     * 描述 规格参数
     * 版本 v2.0
     */
    @JSONField(name = "specifications_item")
    private String specificationsItem;

    /**
     * 描述 商品数量
     * 版本 v2.0
     */
    @JSONField(name = "num")
    private Integer num;

}