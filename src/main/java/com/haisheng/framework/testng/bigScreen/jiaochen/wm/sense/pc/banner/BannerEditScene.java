package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.banner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class BannerEditScene extends BaseScene {
    private final String bannerImgUrl1;
    private final Long articleId1;
    @Builder.Default
    private final Integer bannerId1 = 21;

    private final String bannerImgUrl2;
    private final Long articleId2;
    @Builder.Default
    private final Integer bannerId2 = 22;

    private final String bannerImgUrl3;
    private final Long articleId3;
    @Builder.Default
    private final Integer bannerId3 = 23;

    private final String bannerImgUrl4;
    private final Long articleId4;
    @Builder.Default
    private final Integer bannerId4 = 24;

    private final String bannerImgUrl5;
    private final Long articleId5;
    @Builder.Default
    private final Integer bannerId5 = 25;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject object1 = new JSONObject();
        object1.put("banner_id", bannerId1);
        object1.put("banner_select", "banner1");
        object1.put("banner_img_url", bannerImgUrl1);
        object1.put("article_id", articleId1);
        array.add(object1);
        JSONObject object2 = new JSONObject();
        object2.put("banner_id", bannerId2);
        object2.put("banner_select", "banner2");
        object2.put("banner_img_url", bannerImgUrl2);
        object2.put("article_id", articleId2);
        array.add(object2);
        JSONObject object3 = new JSONObject();
        object3.put("banner_id", bannerId3);
        object3.put("banner_select", "banner3");
        object3.put("banner_img_url", bannerImgUrl3);
        object3.put("article_id", articleId3);
        array.add(object3);
        JSONObject object4 = new JSONObject();
        object4.put("banner_id", bannerId4);
        object4.put("banner_select", "banner4");
        object4.put("banner_img_url", bannerImgUrl4);
        object4.put("article_id", articleId4);
        array.add(object4);
        JSONObject object5 = new JSONObject();
        object5.put("banner_id", bannerId5);
        object5.put("banner_select", "banner5");
        object5.put("banner_img_url", bannerImgUrl5);
        object5.put("article_id", articleId5);
        array.add(object5);
        object.put("list", array);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/banner/edit";
    }
}
