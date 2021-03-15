package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker.ApiAttribute;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker.SceneAttribute;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangmin
 * @date 2021/3/15 12:32
 */

public abstract class AbstractParse implements IParse {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractParse.class);
    @Setter
    private String htmlUrl;

    public AbstractParse(AbstractParse.AbstractBuilder builder) {
        this.htmlUrl = builder.htmlUrl;
    }

    public List<SceneAttribute> getSceneAttributeList() {
        Preconditions.checkArgument(htmlUrl != null, "html地址为空");
        logger.info("html开始解析");
        List<SceneAttribute> sceneAttributeList = new ArrayList<>();
        Elements sect1s = getElementById("content").getElementsByClass("sect1");
        for (Element sect1 : sect1s) {
            Elements sect2s = sect1.getElementsByClass("sect2");
            for (Element sect2 : sect2s) {
                SceneAttribute sceneAttribute = new SceneAttribute();
                String link = sect2.select("h3").select("[class='link']").text();
                sceneAttribute.setLink(link);
                Elements urlElements = sect2.select("[class='paragraph']").stream().filter(e -> e.select("strong").text().equals("URL:")).map(e -> e.select("a").select("[class='bare']")).findFirst().orElse(null);
                String url = urlElements == null ? null : urlElements.text().replaceAll("\u00a0", "");
                sceneAttribute.setUrl(url);
                Elements spreadElements = sect2.select("[class='tableblock frame-all grid-all spread']");
                List<ApiAttribute> apiAttributeList = getApiAttributeList(spreadElements);
                sceneAttribute.setApiAttributeList(apiAttributeList);
                sceneAttribute.setSuffix(getSuffix());
                sceneAttributeList.add(sceneAttribute);
            }
        }
        logger.info("html解析结束");
        return sceneAttributeList;
    }

    protected abstract List<ApiAttribute> getApiAttributeList(Elements spreadElements);

    protected String getHtmlStrByUrl() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(htmlUrl);
            CloseableHttpResponse response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Element getElementById(String id) {
        String htmlStr = getHtmlStrByUrl();
        Document document = Jsoup.parse(htmlStr);
        return document.getElementById(id);
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public abstract static class AbstractBuilder {
        private String htmlUrl;

        public abstract IParse build();
    }
}
