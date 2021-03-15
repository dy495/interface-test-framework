package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker.ApiAttribute;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangmin
 * @date 2021/3/15 12:31
 */
public class BeanParse extends AbstractParse {

    public BeanParse(Builder builder) {
        super(builder);
    }

    @Override
    protected List<ApiAttribute> getApiAttributeList(Elements spreadElements) {
        Element spread = spreadElements.size() == 1 ? spreadElements.first() : spreadElements.size() == 2 ? spreadElements.get(1) : null;
        if (spread != null) {
            Elements trs = spread.select("tbody").select("tr");
            List<ApiAttribute> apiAttributeList = new ArrayList<>();
            for (Element tr : trs) {
                Elements tableBlocks = tr.select("[class='tableblock']");
                if (tableBlocks.text().contains("└─")) {
                    if (!tableBlocks.get(0).text().contains("any object")) {
                        ApiAttribute apiAttribute = new ApiAttribute.Builder()
                                .parameter(tableBlocks.get(0).text().replaceAll("\u00a0", "").replaceAll("└─", ""))
                                .type(tableBlocks.get(1).text())
                                .description(tableBlocks.get(2).text())
                                .since(tableBlocks.get(3).text())
                                .build();
                        apiAttributeList.add(apiAttribute);
                    }
                }
            }
            return apiAttributeList;
        }
        return null;
    }

    @Override
    public String getSuffix() {
        return "Bean";
    }

    public static class Builder extends AbstractBuilder {

        public BeanParse build() {
            return new BeanParse(this);
        }
    }
}
