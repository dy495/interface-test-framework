package com.haisheng.framework.testng.bigScreen.itemBasic.base.marker.parse;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.marker.attribute.ApiAttribute;
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
        // 判断是否有body，（有body的就是2个表，没body的就是1个表）
        if (spreadElements.size() == 2) {
            // 把body表（第一个表）中的每个元素的文本。且判断是否有下级参数（└─），没有则调用getApiAttribute方法
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
        // 依次获取表中提取到的各个元素值，存放到表数据类 ：ApiAttribute
        return new ApiAttribute.Builder().parameter(elements.get(0).text()).type(elements.get(1).text()).description(elements.get(2).text())
                .required(elements.get(3).text()).since(elements.get(4).text()).build();
    }
}
