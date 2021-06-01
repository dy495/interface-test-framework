package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.banner;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 31.2. banner列表详情
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class ListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "banner_id")
    private Long bannerId;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "banner_select")
    private String bannerSelect;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "customer_type")
    private String customerType;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "banner_img_url")
    private String bannerImgUrl;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "article_id")
    private Long articleId;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "article_title")
    private String articleTitle;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "banner_img_file")
    private String bannerImgFile;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "request_id")
    private String requestId;

}