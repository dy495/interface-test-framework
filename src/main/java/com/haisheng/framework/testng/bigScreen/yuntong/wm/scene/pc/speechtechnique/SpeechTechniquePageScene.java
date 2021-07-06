package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.speechtechnique;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.2. 话术分页（谢）
 *
 * @author wangmin
 * @date 2021-05-31 16:28:11
 */
@Builder
public class SpeechTechniquePageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    @Builder.Default
    private Integer page = 1;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    @Builder.Default
    private Integer size = 10;

    /**
     * 描述 接待环节 使用"获取指定枚举值列表"接口获取 enum_type为 RECEPTION_LINKS,或查看字典表《接待环节》
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/speech-technique/page";
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