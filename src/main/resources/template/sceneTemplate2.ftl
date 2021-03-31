   /**
     * ${pathDesc}的接口
     * @author wangmin
     * @date ${date}
     */
<#list attrs as attr>
    /**
    * 描述 ${attr.description}
    * 是否必填 ${attr.required}
    * 版本 ${attr.since}
    */
</#list>

public JSONObject ${className}( <#list attrs as attr> ${attr.type} ${attr.buildParam}, </#list>) {

    String url = "${path}";

    JSONObject json = new JSONObject();

    <#list attrs as attr>
        object.put("${attr.parameter}", ${attr.buildParam});
    </#list>

    return invokeApi(url, json);
}
