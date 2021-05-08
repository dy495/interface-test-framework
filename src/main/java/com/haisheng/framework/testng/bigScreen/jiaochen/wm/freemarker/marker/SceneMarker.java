package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.marker;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.attribute.ApiAttribute;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.attribute.SceneAttribute;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.enumerator.FileFormatEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.enumerator.KeywordEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.enumerator.DNSEnum;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 场景接口类Marker
 *
 * @author wangmin
 * @date 2021/3/8 19:57
 */
public class SceneMarker extends AbstractMarker {
    private String outputPath;
    private String className;
    private final String pathDesc;
    private final String suffix;
    private final String parentPath;
    private final String date;
    private final String urlPath;
    private final List<ApiAttribute> apiAttributeList;
    private final Map<String, Object> dataMap = new HashMap<>(32);

    protected SceneMarker(Builder builder) {
        super(builder);
        this.suffix = builder.sceneAttribute.getSuffix();
        this.date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
        this.parentPath = builder.parentPath;
        this.urlPath = builder.sceneAttribute.getUrl();
        this.pathDesc = builder.sceneAttribute.getPathDesc();
        this.apiAttributeList = builder.sceneAttribute.getApiAttributeList();

    }

    @Override
    public void init() {
        if (this.urlPath != null && this.apiAttributeList != null) {
            initial();
            dataMap.put("attrs", apiAttributeList);
            dataMap.put("date", date);
            logger.info("dataMap :{}", dataMap);
            logger.info("outputPath :{}", outputPath);
            logger.info("className :{}", className);
            logger.info("fileFormat :{}", FileFormatEnum.JAVA.getSuffix());
            setStructure(new Structure.Builder().dateMap(dataMap).outputPath(outputPath).className(className).fileFormat(FileFormatEnum.JAVA.getSuffix()).build());
        } else {
            logger.info("----------{}----------", "url为空或者结构文档数据为空 !");
        }
    }

    /**
     * 初始赋值
     */
    public void initial() {
        StringBuilder sb = new StringBuilder();
        String[] urlPathList = urlPathParse(urlPath);
        for (int i = 0; i < urlPathList.length; i++) {
            String s = KeywordEnum.transferEqualKeyword(urlPathList[i]);
            urlPathList[i] = s;
        }
        int index = urlPathList.length <= 3 ? urlPathList.length - 1 : 4 - 1;
        for (int i = index; i < urlPathList.length; i++) {
            if (i < urlPathList.length - 1) {
                sb.append(urlPathList[i]).append("_");
            } else {
                sb.append(urlPathList[i]);
            }
        }
        className = CommonUtil.lineToHump(sb.toString(), true) + suffix;
        className = urlPath.contains("app") ? "App" + className : urlPath.contains("applet") ? "Applet" + className : className;
        sb.setLength(0);
        for (int i = 1; i < index; i++) {
            sb.append(urlPathList[i]).append("/");
        }
        outputPath = parentPath + "/" + sb.toString().replaceAll("-|_", "");
        sb.setLength(0);
        String[] parentPathList = parentPath.split("/");
        int s = 0;
        for (int i = 0; i < parentPathList.length; i++) {
            s = parentPathList[i].equals("java") ? i + 1 : s;
        }
        for (int i = s; i < parentPathList.length; i++) {
            sb.append(parentPathList[i]).append(".");
        }
        if (index <= 1) {
            sb.replace(sb.length() - 1, sb.length(), "");
        }
        for (int i = 1; i < index; i++) {
            if (i < index - 1) {
                sb.append(urlPathList[i].replaceAll("-|_", "")).append(".");
            } else {
                sb.append(urlPathList[i].replaceAll("-|_", ""));
            }
        }
        String packageName = sb.toString();
        dataMap.put("packageName", packageName);
        dataMap.put("className", className);
        dataMap.put("path", urlExcludeIpPort(urlPath));
        dataMap.put("pathDesc", pathDesc);
    }

    public static class Builder extends AbstractMarker.AbstractBuilder<Builder> {
        private SceneAttribute sceneAttribute;
        private String parentPath;

        public Builder parentPath(String parentPath) {
            this.parentPath = parentPath;
            return this;
        }

        public Builder sceneAttribute(SceneAttribute sceneAttribute) {
            this.sceneAttribute = sceneAttribute;
            return this;
        }

        @Override
        protected IMarker build() {
            return new SceneMarker(this);
        }
    }

    public static String[] urlPathParse(String url) {
        String str = url.replace(DNSEnum.getContainAddress(url) + "/", "");
        String newStr = str.substring(0, 1).contains("/") ? str.replaceFirst("/", "") : str;
        return newStr.split("/");
    }

    public static String urlExcludeIpPort(String url) {
        String str = url.replace(DNSEnum.getContainAddress(url), "");
        return str.substring(0, 2).contains("//") ? str.replaceFirst("/", "") : str;
    }
}
