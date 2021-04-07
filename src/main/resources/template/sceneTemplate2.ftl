
/**
     * ${pathDesc}的接口
     * @date ${date}
<#list attrs as attr>
     * ${attr.type} ${attr.buildParam} : ${attr.description}  是否必填 ${attr.required}  版本 ${attr.since}
</#list>
   */

public JSONObject ${className}(<#list attrs as attr> <#if attr_has_next >${attr.type} ${attr.buildParam},<#else >${attr.type} ${attr.buildParam}</#if></#list>) {
    String url = "${path}";
    JSONObject json = new JSONObject();
    <#list attrs as attr>
        json.put("${attr.parameter}", ${attr.buildParam});
    </#list>

    return invokeApi(url, json);
}
