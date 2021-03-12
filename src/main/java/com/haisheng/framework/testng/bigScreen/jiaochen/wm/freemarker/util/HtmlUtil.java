package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.util;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker.ApiAttribute;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker.SceneAttribute;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangmin
 * @date 2021/3/10 10:33
 */
public class HtmlUtil {
    public static final Logger logger = LoggerFactory.getLogger(HtmlUtil.class);

    public static List<SceneAttribute> parseHtml(String htmlUrl) {
        try {
            String pageWaitJS = getPageWaitJS(htmlUrl);
            Document document = Jsoup.parse(pageWaitJS);
            List<SceneAttribute> sceneAttributeList = new ArrayList<>();
            logger.info("html开始收集");
            Element content = document.getElementById("content");
            Elements sect1s = content.getElementsByClass("sect1");
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
                    if (spreadElements.size() == 2) {
                        Elements trs = spreadElements.first().select("tbody").select("tr");
                        List<ApiAttribute> attributeList = new ArrayList<>();
                        for (Element tr : trs) {
                            Elements tableBlocks = tr.select("[class='tableblock']");
                            if (!tableBlocks.get(0).text().contains("└─")) {
                                ApiAttribute apiAttribute = new ApiAttribute.Builder()
                                        .parameter(tableBlocks.get(0).text())
                                        .type(tableBlocks.get(1).text())
                                        .description(tableBlocks.get(2).text())
                                        .required(tableBlocks.get(3).text())
                                        .since(tableBlocks.get(4).text())
                                        .build();
                                attributeList.add(apiAttribute);
                                sceneAttribute.setApiAttributeList(attributeList);
                            }
                        }
                    }
                    sceneAttributeList.add(sceneAttribute);
                }
            }
            logger.info("html收集结束");
            return sceneAttributeList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPageWaitJS(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpget);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity, "UTF-8");
    }
}
