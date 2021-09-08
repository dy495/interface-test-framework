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
    <#assign page="page">
    <#assign size="size">
    <#-- 循环类型及属性 -->
    <#list attrs as attr>
    /**
     * 描述 ${attr.description}
     * 是否必填 ${attr.required}
     * 版本 ${attr.since}
     */
        <#if page==attr.buildParam>
    @Builder.Default
    private ${attr.type} ${attr.buildParam} = 1;

        <#elseif size==attr.buildParam>
    @Builder.Default
    private ${attr.type} ${attr.buildParam} = 10;

        <#else>
    private final ${attr.type} ${attr.buildParam};

        </#if>
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
    <#list attrs as attr>
        <#if size==attr.buildParam>
    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

        <#elseif page==attr.buildParam>

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

        </#if>
    </#list>
}