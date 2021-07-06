package ${packageName};

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * ${pathDesc}
 *
 * @author wangmin
 * @date ${date}
 */
@Builder
public class ${className} extends BaseScene {
    <#-- 循环类型及属性 -->
    <#list attrs as attr>
    /**
     * 描述 ${attr.description}
     * 是否必填 ${attr.required}
     * 版本 ${attr.since}
     */
    private final ${attr.type} ${attr.buildParam};

    </#list>

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        <#list attrs as attr>
        object.put("${attr.parameter}", ${attr.buildParam});
        </#list>
        return object;
    }

    @Override
    public String getPath() {
        return "${path}";
    }
}