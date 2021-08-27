package com.haisheng.framework.testng.bigScreen.itemBasic.base.marker.parse;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.marker.attribute.ApiAttribute;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.marker.attribute.SceneAttribute;
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
        // 初始化一个list  泛型SceneAttribute：存放页面提取全部所需属性的类
        List<SceneAttribute> sceneAttributeList = new ArrayList<>();
        // 获取所有需要的一级元素：接口分类列表
        Elements sect1s = getElementById().getElementsByClass("sect1");
        for (Element sect1 : sect1s) {
            // 获取所有需要的二级元素：每个接口
            Elements sect2s = sect1.getElementsByClass("sect2");
            for (Element sect2 : sect2s) {
                // 每个二级元素都初始化创建SceneAttribute，用于存放
                SceneAttribute sceneAttribute = new SceneAttribute();
                // 获取这个接口的标题，存放到属性类中
                String link = sect2.select("h3").select("[class='link']").text();
                sceneAttribute.setPathDesc(link);
                // 获取到URL（获取所有项筛选标题为URL的文本）
                String url = sect2.select("[class='paragraph']").stream().filter(e -> e.select("strong")
                        .text().equals("URL:")).map(e -> e.select("a").select("[class='bare']")).map(e -> e.text()
                        .replaceAll("\u00a0", "")).findFirst().orElse(null);
                // 存放到类属性中
                sceneAttribute.setUrl(url);
                // 获取页面中的body表元素，和response表元素（有的没有body表）
                Elements spreadElements = sect2.select("[class='tableblock frame-all grid-all spread']");
                // 用实子类的重写的getApiAttributeList方法，返回一个存放参数表中数据的类（ApiAttribute）list
                List<ApiAttribute> apiAttributeList = getApiAttributeList(spreadElements);
                // 把数据放到SceneAttribute中
                sceneAttribute.setApiAttributeList(apiAttributeList);
                // 调用子类的重写方法，返回 Bean/Scene
                sceneAttribute.setSuffix(getSuffix());
                // 最终放到list中
                sceneAttributeList.add(sceneAttribute);
            }
        }
        logger.info("html解析结束");
        // 转为用数组存贮
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
