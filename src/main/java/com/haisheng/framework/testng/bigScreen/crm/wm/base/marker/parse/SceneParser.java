package com.haisheng.framework.testng.bigScreen.crm.wm.base.marker.parse;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.marker.attribute.ApiAttribute;
import org.jetbrains.annotations.NotNull;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 解析为Scene所需要的属性
 *
 * @author wangmin
 * @date 2021/3/15 12:31
 */
public class SceneParser extends AbstractHtmlParser {
    public SceneParser(Builder builder) {
        super(builder);
    }

    @Override
    protected List<ApiAttribute> getApiAttributeList(@NotNull Elements spreadElements) {
        if (spreadElements.size() == 2) {
            return spreadElements.first().select("tbody").select("tr").stream().map(tr -> tr.select("[class='tableblock']"))
                    .filter(tableBlocks -> !tableBlocks.text().contains("└─")).map(this::getApiAttribute).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public String getSuffix() {
        return "Scene";
    }

    public static class Builder extends AbstractBuilder<Builder, SceneParser> {

        @Override
        public SceneParser buildParser() {
            return new SceneParser(this);
        }
    }

    /**
     * 获取apiAttribute
     *
     * @param elements 从元素集合中收集
     * @return ApiAttribute
     */
    private ApiAttribute getApiAttribute(@NotNull Elements elements) {
        return new ApiAttribute.Builder().parameter(elements.get(0).text()).type(elements.get(1).text()).description(elements.get(2).text())
                .required(elements.get(3).text()).since(elements.get(4).text()).build();
    }
}
