package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker.ApiAttribute;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangmin
 * @date 2021/3/15 12:31
 */
public class SceneParser extends AbstractParser {
    public SceneParser(AbstractBuilder builder) {
        super(builder);
    }

    @Override
    protected List<ApiAttribute> getApiAttributeList(@NotNull Elements spreadElements) {
        if (spreadElements.size() == 2) {
            Elements trs = spreadElements.first().select("tbody").select("tr");
            List<ApiAttribute> apiAttributeList = new ArrayList<>();
            for (Element tr : trs) {
                Elements tableBlocks = tr.select("[class='tableblock']");
                if (!tableBlocks.text().contains("└─")) {
                    ApiAttribute apiAttribute = new ApiAttribute.Builder()
                            .parameter(tableBlocks.get(0).text())
                            .type(tableBlocks.get(1).text())
                            .description(tableBlocks.get(2).text())
                            .required(tableBlocks.get(3).text())
                            .since(tableBlocks.get(4).text())
                            .build();
                    apiAttributeList.add(apiAttribute);
                }
            }
            return apiAttributeList;
        }
        return null;
    }

    @Override
    public String getSuffix() {
        return "Scene";
    }

    public static class Builder extends AbstractBuilder {

        @Override
        IParser buildParas() {
            return new SceneParser(this);
        }
    }
}
