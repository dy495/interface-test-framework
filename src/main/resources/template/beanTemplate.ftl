package ${packageName};

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * ${path}的bean
 *
 * @author wangmin
 * @date ${date}
 */
@Data
public class ${className} implements Serializable {
    <#-- 循环类型及属性 -->
    <#list attrs as attr>
    /**
     * 描述 ${attr.description}
     * 版本 ${attr.since}
     */
    @JSONField(name = "${attr.parameter}")
    private ${attr.type} ${attr.buildParam};

    </#list>
}