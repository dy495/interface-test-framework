package com.haisheng.framework.testng.bigScreen.itemBasic.base.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.util.CommonUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基础工具类 抽象
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
        JSONArray list = scene.visitor(visitor).execute().getJSONArray(JSONArrayName);
        return list.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, tClass)).collect(Collectors.toList());
    }

    public <T> List<T> toJavaObjectList(@NotNull IScene scene, Class<T> tClass) {
        int total = scene.visitor(visitor).execute().getInteger("total");
        return toJavaObjectList(scene, tClass, total);
    }

    public <T> List<T> toJavaObjectList(@NotNull IScene scene, Integer size, Class<T> tClass) {
        List<T> list = new ArrayList<>();
        int total = scene.visitor(visitor).execute().getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(size);
            JSONArray array = scene.visitor(visitor).execute().getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, tClass)).collect(Collectors.toList()));
        }
        return list;
    }

    public <T> List<T> toJavaObjectList(IScene scene, Class<T> tClass, Integer size) {
        List<T> list = new ArrayList<>();
        int s = CommonUtil.getTurningPage(size, SIZE);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(SIZE);
            JSONArray array = scene.visitor(visitor).execute().getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, tClass)).collect(Collectors.toList()));
        }
        return list;
    }

    public <T> List<T> toJavaObjectList(@NotNull IScene scene, Class<T> bean, String key, Object value) {
        List<T> list = new ArrayList<>();
        int total = scene.visitor(visitor).execute().getInteger("total");
        int s = CommonUtil.getTurningPage(total, SIZE);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(SIZE);
            JSONArray array = scene.visitor(visitor).execute().getJSONArray("list");
            array.stream().map(e -> (JSONObject) e).filter(e -> e.getObject(key, value.getClass()).equals(value))
                    .map(e -> JSONObject.toJavaObject(e, bean)).forEach(list::add);
        }
        return list;
    }

    public <T> T toJavaObject(@NotNull IScene scene, Class<T> tClass) {
        return toJavaObject(scene.visitor(visitor).execute(), tClass);
    }

    public <T> T toJavaObject(JSONObject object, Class<T> tClass) {
        return JSONObject.toJavaObject(object, tClass);
    }

    public <T> T toJavaObject(@NotNull IScene scene, Class<T> tClass, String key, Object value) {
        int total = scene.visitor(visitor).execute().getInteger("total");
        int s = CommonUtil.getTurningPage(total, SIZE);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(SIZE);
            JSONArray array = scene.visitor(visitor).execute().getJSONArray("list");
            T clazz = array.stream().map(e -> (JSONObject) e).filter(e -> e.getObject(key, value.getClass())
                    .equals(value)).findFirst().map(e -> JSONObject.toJavaObject(e, tClass)).orElse(null);
            if (clazz != null) {
                return clazz;
            }
        }
        return null;
    }

    public <T> T toJavaObject(@NotNull IScene scene, Integer size, Class<T> tClass, String key, Object value) {
        int total = scene.visitor(visitor).execute().getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(size);
            JSONArray array = scene.visitor(visitor).execute().getJSONArray("list");
            T clazz = array.stream().map(e -> (JSONObject) e).filter(e -> e.getObject(key, value.getClass())
                    .equals(value)).findFirst().map(e -> JSONObject.toJavaObject(e, tClass)).orElse(null);
            if (clazz != null) {
                return clazz;
            }
        }
        return null;
    }

    public <T> T toFirstJavaObject(@NotNull IScene scene, Class<T> tClass) {
        JSONArray array = scene.visitor(visitor).execute().getJSONArray("list");
        return array.size() == 0 ? null : toJavaObject(scene.visitor(visitor).execute().getJSONArray("list").getJSONObject(0), tClass);
    }

    public <K, V, T> V getValueByKey(@NotNull Map<K, V> map, T key) {
        return map.entrySet().stream().filter(e -> e.getKey().equals(key)).map(Map.Entry::getValue).findFirst().orElse(null);
    }
}
