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
public class Login extends BaseScene {
    private int type;
    private String username;
    private String password;

    @Override
    public JSONObject invokeApi() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("username", username);
        object.put("password", password);
        return execute(object, false);
    }

    @Override
    public String getPath() {
        return EnumAddress.PORSCHE.getAddress();
    }

    @Override
    public String getIpPort() {
        return "/porsche-login";
    }
}
