package com.haisheng.framework.testng.bigScreen.crm.wm.base.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.IEntity;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.util.ContainerEnum;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.Response;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.UrlToIoOutputUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基础工具类
 * 提供一些通用功能
 *
 * @author wangmin
 * @date 2021-05-14
 */
public class BasicUtil {
    public final static Logger logger = LoggerFactory.getLogger(BasicUtil.class);
    public final static Integer SIZE = 100;
    private final VisitorProxy visitor;

    public BasicUtil(VisitorProxy visitor) {
        this.visitor = visitor;
    }

    public <T> List<T> toJavaObjectList(@NotNull IScene scene, Class<T> tClass, String JSONArrayName) {
        JSONArray list = scene.invoke(visitor).getJSONArray(JSONArrayName);
        return list.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, tClass)).collect(Collectors.toList());
    }

    public <T> List<T> toJavaObjectList(@NotNull IScene scene, Class<T> tClass) {
        int total = scene.invoke(visitor).getInteger("total");
        return toJavaObjectList(scene, tClass, total);
    }

    public <T> List<T> toJavaObjectList(IScene scene, Class<T> tClass, Integer size) {
        List<T> list = new ArrayList<>();
        int s = CommonUtil.getTurningPage(size, SIZE);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(SIZE);
            JSONArray array = scene.invoke(visitor).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, tClass)).collect(Collectors.toList()));
        }
        return list;
    }

    public <T> List<T> toJavaObjectList(@NotNull IScene scene, Class<T> bean, String key, Object value) {
        List<T> list = new ArrayList<>();
        int total = scene.invoke(visitor).getInteger("total");
        int s = CommonUtil.getTurningPage(total, SIZE);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(SIZE);
            JSONArray array = scene.invoke(visitor).getJSONArray("list");
            T clazz = array.stream().map(e -> (JSONObject) e).filter(e -> e.getObject(key, value.getClass()).equals(value))
                    .findFirst().map(e -> JSONObject.toJavaObject(e, bean)).orElse(null);
            list.add(clazz);
        }
        return list;
    }

    public <T> T toJavaObject(@NotNull IScene scene, Class<T> tClass) {
        return toJavaObject(scene.invoke(visitor), tClass);
    }

    public <T> T toJavaObject(JSONObject object, Class<T> tClass) {
        return JSONObject.toJavaObject(object, tClass);
    }

    public <T> T toJavaObject(@NotNull IScene scene, Class<T> bean, String key, Object value) {
        int total = scene.invoke(visitor).getInteger("total");
        int s = CommonUtil.getTurningPage(total, SIZE);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(SIZE);
            JSONArray array = scene.invoke(visitor).getJSONArray("list");
            T clazz = array.stream().map(e -> (JSONObject) e).filter(e -> e.getObject(key, value.getClass())
                    .equals(value)).findFirst().map(e -> JSONObject.toJavaObject(e, bean)).orElse(null);
            if (clazz != null) {
                return clazz;
            }
        }
        return null;
    }

    public <T> T toFirstJavaObject(@NotNull IScene scene, Class<T> tClass) {
        JSONObject object = scene.invoke(visitor).getJSONArray("list").getJSONObject(0);
        return toJavaObject(object, tClass);
    }

    /**
     * 获取接口返回值message的list
     *
     * @param scene 接口
     * @return string[]
     */
    public String[] getMessageList(@NotNull IScene scene) {
        List<String> list = scene.getKeyList();
        logger.info("keyList is：{}", list);
        return list.stream().map(e -> JSONObject.toJavaObject(scene.remove(e).invoke(visitor, false), Response.class))
                .map(Response::getMessage).collect(Collectors.toList()).toArray(new String[list.size()]);
    }

    /**
     * 获取接口返回值
     *
     * @param scene 接口
     * @return 返回值内容
     */
    public Response getResponse(@NotNull IScene scene) {
        return JSONObject.toJavaObject(scene.invoke(visitor, false), Response.class);
    }

    public IRow[] getRows(String urlPath, String excelName) {
        //下载文件到resources/excel
        String outputPath = "/src/main/resources/excel/" + excelName;
        UrlToIoOutputUtil.toIoSave(urlPath, outputPath);
        String relativePath = "/excel/" + excelName;
        logger.info("relativePath is {}", relativePath);
        IEntity<?, ?>[] entities = new Factory.Builder().container(ContainerEnum.EXCEL.getContainer()).build().createE(relativePath);
        return Arrays.stream(entities).map(IEntity::getCurrent).toArray(IRow[]::new);
    }

}
