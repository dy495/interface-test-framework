package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 消息管理 -> 消息表单
 */
@Builder
public class GroupTotalScene extends BaseScene {
    private final Integer pushTarget;
    private final List<Long> shopList;
    private final List<String> telList;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("push_target", pushTarget);
        object.put("shop_list", shopList);
        object.put("tel_list", telList);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/message-manage/group-total";
    }
}
