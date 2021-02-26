package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.activity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

    /**
     * 小程序报名活动--gly
     */
    @Builder
    public class ArticleActivityRegisterScene extends BaseScene {
        private final Long id;
        private final JSONArray registerItems;

        @Override
        public JSONObject getRequestBody() {
            JSONObject object = new JSONObject();
            object.put("id", id);
            object.put("register_items", registerItems);
            return object;
        }

        @Override
        public String getPath() {
            return "/jiaochen/applet/granted/article/activity/register";
        }

        @Override
        public String getIpPort() {
            return null;
        }
}
