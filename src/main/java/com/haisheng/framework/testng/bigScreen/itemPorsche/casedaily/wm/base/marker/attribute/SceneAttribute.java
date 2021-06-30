package com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.marker.attribute;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 接口场景类模板需要的属性
 *
 * @author wangmin
 * @date 2021/3/8 14:32
 */
@Getter
@Setter
public class SceneAttribute implements Serializable {
    private String pathDesc;
    private String url;
    private String suffix;
    private List<ApiAttribute> apiAttributeList;

    @Override
    public String toString() {
        return "SceneAttribute:{"
                + "    link :" + pathDesc
                + "    url :" + url
                + "    suffix ：" + suffix
                + "    List<ApiAttribute> :" + apiAttributeList
                + "}";
    }
}
