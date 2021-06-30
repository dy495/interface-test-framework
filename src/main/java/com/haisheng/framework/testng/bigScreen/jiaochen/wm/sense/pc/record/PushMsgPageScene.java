package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.record;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 消息记录
 *
 * @author wangmin
 * @date 2021/1/28 16:12
 */
@Builder
public class PushMsgPageScene extends BaseScene {
    @Builder.Default
    private Integer size = 10;
    @Builder.Default
    private Integer page = 1;

    @Override
    public JSONObject getRequestBody() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size", size);
        jsonObject.put("page", page);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/record/push-msg/page";
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }
}
