package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.attribute.ApiAttribute;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.attribute.SceneAttribute;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
@Setter
public abstract class AbstractHtmlParser implements IParser<SceneAttribute> {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractHtmlParser.class);
    private String htmlUrl;

    public AbstractHtmlParser(@NotNull AbstractBuilder<?, ?> builder) {
        this.htmlUrl = builder.htmlUrl;
    }

    @Override
    public abstract String getSuffix();

    @Override
    public SceneAttribute[] getAttributes() {
        logger.info("html开始解析");
        List<SceneAttribute> sceneAttributeList = new ArrayList<>();
        Elements sect1s = getElementById().getElementsByClass("sect1");
        for (Element sect1 : sect1s) {
            Elements sect2s = sect1.getElementsByClass("sect2");
            for (Element sect2 : sect2s) {
                SceneAttribute sceneAttribute = new SceneAttribute();
                String link = sect2.select("h3").select("[class='link']").text();
                sceneAttribute.setPathDesc(link);
                String url = sect2.select("[class='paragraph']").stream().filter(e -> e.select("strong")
                        .text().equals("URL:")).map(e -> e.select("a").select("[class='bare']")).map(e -> e.text()
                        .replaceAll("\u00a0", "")).findFirst().orElse(null);
                sceneAttribute.setUrl(url);
                Elements spreadElements = sect2.select("[class='tableblock frame-all grid-all spread']");
                List<ApiAttribute> apiAttributeList = getApiAttributeList(spreadElements);
                sceneAttribute.setApiAttributeList(apiAttributeList);
                sceneAttribute.setSuffix(getSuffix());
                sceneAttributeList.add(sceneAttribute);
            }
        }
        logger.info("html解析结束");
        final int size = sceneAttributeList.size();
        return sceneAttributeList.toArray(new SceneAttribute[size]);
    }

    /**
     * 获取后端接口的请求参数
     *
     * @param elements 元素集合
     * @return 接口参数属性list
     */
    protected abstract List<ApiAttribute> getApiAttributeList(Elements elements);

    public abstract static class AbstractBuilder<T extends AbstractBuilder<?, ?>, R extends AbstractHtmlParser> {
        private String htmlUrl;

        public T htmlUrl(String htmlUrl) {
            this.htmlUrl = htmlUrl;
            return (T) this;
        }

        public abstract R buildParser();

        public R build() {
            Preconditions.checkArgument(htmlUrl != null, "html地址为空");
            return buildParser();
        }
    }

    /**
     * 通过url获取html数据
     *
     * @return html
     */
    @Nullable
    private String getHtmlStrByUrl() {
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

    /**
     * 获取content
     *
     * @return content
     */
    private Element getElementById() {
        String htmlStr = getHtmlStrByUrl();
        Document document = Jsoup.parse(htmlStr);
        return document.getElementById("content");
    }
}
