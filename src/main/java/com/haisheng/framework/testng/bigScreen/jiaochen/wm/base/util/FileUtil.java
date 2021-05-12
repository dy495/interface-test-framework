package com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;

public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 获取资源文件在当前项目下的绝对路径
     *
     * @param relativePath 资源的相对路径
     * @return String 资源的绝对路径
     */
    public static String getResourcePath(String relativePath) {
        logger.info(relativePath);
        String str = Objects.requireNonNull(FileUtil.class.getClassLoader().getResource(relativePath)).getPath();
        String path = null;
        try {
            path = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return path;
    }


}
