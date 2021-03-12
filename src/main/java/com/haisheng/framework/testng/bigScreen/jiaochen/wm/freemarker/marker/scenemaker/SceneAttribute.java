package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker;

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
    private String link;
    private String url;
    private List<ApiAttribute> apiAttributeList;

    @Override
    public String toString() {
        return "SceneAttribute:{"
                + "    link :" + link
                + "    url :" + url
                + "    List<ApiAttribute> :" + apiAttributeList
                + "}";
    }
}
