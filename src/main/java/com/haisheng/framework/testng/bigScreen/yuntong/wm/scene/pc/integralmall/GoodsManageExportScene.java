package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.integralmall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 26.25. 商品导出 （华成裕）（2020-12-24）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class GoodsManageExportScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 商品名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String goodsName;

    /**
     * 描述 商品品牌
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long goodsBrand;

    /**
     * 描述 商品状态
     * 是否必填 false
     * 版本 v2.0
     */
    private final String goodsStatus;

    /**
     * 描述 一级品类
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long firstCategory;

    /**
     * 描述 二级品类
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long secondCategory;

    /**
     * 描述 三级品类
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long thirdCategory;

    /**
     * 描述 导出类型 ALL：导出全部，CURRENT_PAGE：导出当前页，SPECIFIED_DATA：导出特定数据
     * 是否必填 true
     * 版本 v2.0
     */
    private final String exportType;

    /**
     * 描述 导出数据id列表，特定数据时必填
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray ids;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("goods_name", goodsName);
        object.put("goods_brand", goodsBrand);
        object.put("goods_status", goodsStatus);
        object.put("first_category", firstCategory);
        object.put("second_category", secondCategory);
        object.put("third_category", thirdCategory);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/integral-mall/goods-manage/export";
    }
}