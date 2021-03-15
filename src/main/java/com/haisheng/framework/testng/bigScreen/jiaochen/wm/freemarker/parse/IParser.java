package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.parse;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker.scenemaker.SceneAttribute;

import java.util.List;

/**
 * 解析接口
 *
 * @author wangmin
 * @date 2021/3/15 13:40
 */
public interface IParser {

    List<SceneAttribute> getSceneAttributeList();

    void setHtmlUrl(String htmlUrl);

    String getSuffix();
}
