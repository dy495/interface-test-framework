package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker.ApiAttribute;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 解析为Bean所需要的属性
 *
 * @author wangmin
 * @date 2021/3/15 12:31
 */
public class BeanParser extends AbstractHtmlParser {
    public BeanParser(Builder builder) {
        super(builder);
    }

    @Override
    protected List<ApiAttribute> getApiAttributeList(@NotNull Elements spreadElements) {
        Element spread = spreadElements.size() == 1 ? spreadElements.first() : spreadElements.size() == 2 ? spreadElements.get(1) : null;
        if (spread != null) {
            return spread.select("tbody").select("tr").stream().map(tr -> tr.select("[class='tableblock']"))
                    .filter(tableBlocks -> tableBlocks.text().contains("└─") && !tableBlocks.get(0).text().contains("any object"))
                    .map(this::getApiAttribute).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public String getSuffix() {
        return "Bean";
    }

    public static class Builder extends AbstractBuilder<Builder, BeanParser> {

        @Override
        public BeanParser buildParser() {
            return new BeanParser(this);
        }
    }

    /**
     * 获取apiAttribute
     *
     * @param elements 从元素集合中收集
     * @return ApiAttribute
     */
    private ApiAttribute getApiAttribute(Elements elements) {
        return new ApiAttribute.Builder().parameter(elements.get(0).text().replaceAll("\u00a0", "").replaceAll("└─", ""))
                .type(elements.get(1).text()).description(elements.get(2).text()).since(elements.get(3).text()).build();
    }
}
