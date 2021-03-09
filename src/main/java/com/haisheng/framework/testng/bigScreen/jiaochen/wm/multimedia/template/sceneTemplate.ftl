package ${packageName};

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class ${className} extends BaseScene {
    <#-- 循环类型及属性 -->
    <#list attrs as attr>
    private final ${attr.type} ${attr.name};
    </#list>

    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        <#list attrs as attr>
        object.put("${attr.buildName}",${attr.name});
        </#list>
        return object;
    }

    @Override
    public String getPath() {
        return "${path}";
    }
}