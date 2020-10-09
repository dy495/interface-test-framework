package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.experiment.enumerator.EnumAddress;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.base.BaseScene;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录接口
 * 测试使用
 *
 * @author wangmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class LoginScene extends BaseScene {
    private int type;
    private String username;
    private String password;

    @Override
    public JSONObject getJson() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("username", username);
        object.put("password", password);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche-login";
    }

    @Override
    public String getIpPort() {
        return EnumAddress.PORSCHE.getAddress();
    }
}
