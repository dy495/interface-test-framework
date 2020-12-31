package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.banner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

@Builder
public class BannerEdit extends BaseScene {
    private final String bannerImgUrl1;
    private final String articleId1;

    private final String bannerImgUrl2;
    private final String articleId2;

    private final String bannerImgUrl3;
    private final String articleId3;

    private final String bannerImgUrl4;
    private final String articleId4;

    private final String bannerImgUrl5;
    private final String articleId5;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject object1 = new JSONObject();
        object1.put("banner_id", 21);
        object1.put("banner_select", "banner1");
        object1.put("banner_img_url", bannerImgUrl1);
        object1.put("article_id", articleId1);
        array.add(object1);
        JSONObject object2 = new JSONObject();
        object2.put("banner_id", 22);
        object2.put("banner_select", "banner2");
        object2.put("banner_img_url", bannerImgUrl2);
        object2.put("article_id", articleId2);
        array.add(object2);
        JSONObject object3 = new JSONObject();
        object3.put("banner_id", 23);
        object3.put("banner_select", "banner3");
        object3.put("banner_img_url", bannerImgUrl3);
        object3.put("article_id", articleId3);
        array.add(object3);
        JSONObject object4 = new JSONObject();
        object4.put("banner_id", 24);
        object4.put("banner_select", "banner4");
        object4.put("banner_img_url", bannerImgUrl4);
        object4.put("article_id", articleId4);
        array.add(object4);
        JSONObject object5 = new JSONObject();
        object5.put("banner_id", 25);
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
