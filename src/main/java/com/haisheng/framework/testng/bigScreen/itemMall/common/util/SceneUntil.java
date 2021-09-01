package com.haisheng.framework.testng.bigScreen.itemMall.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumDingTalkWebHook;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemMall.common.bean.RegionDataBean;
import com.haisheng.framework.testng.bigScreen.itemMall.common.bean.RegionTrendBean;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.LoginPcMall;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.realtime.RegionRealTimeTrendScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.DingPushUtil;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.MD5Util;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SceneUntil extends BasicUtil {

    private final VisitorProxy visitor;

    public SceneUntil(VisitorProxy proxy) {
        super(proxy);
        this.visitor = proxy;
    }

    public void loginPc(@NotNull AccountEnum enumAccount) {
        String password = new MD5Util().getMD5(enumAccount.getPassword());
        IScene scene = LoginPcMall.builder().password(password).username(enumAccount.getUsername()).type(0).build();
        login(scene);
    }

    public void login(IScene scene) {
        EnumTestProduct oldProduce = visitor.getProduct();
        EnumTestProduct newProduce = visitor.isDaily() ? EnumTestProduct.MALL_DAILY_SSO : EnumTestProduct.MALL_ONLINE_SSO;
        visitor.setProduct(newProduce);
        visitor.login(scene);
        visitor.setProduct(oldProduce);
    }

    /**
     * 筛选时间
     *
     * @param time 时间字段
     * @return boolean
     */
    public boolean filterTime(@org.jetbrains.annotations.NotNull String time) {
        return time.compareTo("09:00") >= 0 && time.compareTo("22:00") <= 0;
    }

    /**
     * 找到当前数据时间的指定类型数据
     *
     * @param type    数据类型
     * @param region  区域类型
     * @param nowTime 当前时间
     * @return JSONObject
     */
    public JSONObject findCurrentTimeData(String type, String region, String nowTime) {
        JSONObject rsp = RegionRealTimeTrendScene.builder().type(type).region(region).build().visitor(visitor).execute();
        List<JSONObject> list = rsp.getJSONArray("list").toJavaList(JSONObject.class);
        return list.stream().filter(e -> filterTime(e.getString("x_value"))).filter(e -> e.getString("x_value").substring(0, 2).equals(nowTime)).findFirst().orElse(null);
    }

    /**
     * 找到当前时间数值为0的数据
     *
     * @param seriesList 系列列表
     * @param uvObj      uv数据object
     * @param pvObj      pv数据object
     * @return 数据集合RegionTrendBean
     */
    public List<RegionTrendBean> findCurrentTimeValueIsZeroData(JSONArray seriesList, JSONObject uvObj, JSONObject pvObj) {
        List<RegionTrendBean> regionTrendBeanList = seriesList.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, RegionTrendBean.class)).collect(Collectors.toList());
        for (RegionTrendBean regionTrendBean : regionTrendBeanList) {
            uvObj.entrySet().stream().filter(o -> o.getKey().equals(regionTrendBean.getKey()))
                    .forEach(o -> regionTrendBean.setUv((Integer) o.getValue()));
            pvObj.entrySet().stream().filter(o -> o.getKey().equals(regionTrendBean.getKey()))
                    .forEach(o -> regionTrendBean.setPv((Integer) o.getValue()));
        }
        return regionTrendBeanList.stream().filter(e -> e.getPv() == null || e.getPv() == 0 || e.getUv() == null || e.getUv() == 0).collect(Collectors.toList());
    }

    public void sendMessage(String subjectName, String time, Map<String, Integer> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("##### ").append(subjectName).append(" ").append(time).append("数据为：").append("\n");
        sb.append("###### ");
        map.forEach((key, value) -> sb.append(key).append("数据为：").append(value).append(" "));
        DingPushUtil util = new DingPushUtil();
        util.changeWeHook(DingWebhook.PV_UV_ACCURACY_GRP);
        util.send(sb.toString());
    }

    public void sendMessage(String subjectName, String time, List<RegionDataBean> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("#### ").append(subjectName).append(" ").append(time).append("数据监控").append("\n");
        list.forEach(regionData -> {
            String region = regionData.getRegion();
            List<RegionTrendBean> regionTrendBeanList = regionData.getRegionTrendBeanList();
            sb.append("###### **").append(region).append(" 共有以下").append(regionTrendBeanList.size()).append("个区域数据为0").append("**").append("\n");
            regionTrendBeanList.forEach(regionTrend -> sb.append("######  ").append(regionTrend.getName()).append("--").append(regionTrend.getKey()).append("\n"));
        });
        DingPushUtil util = new DingPushUtil();
        util.changeWeHook(DingWebhook.PV_UV_ACCURACY_GRP);
        util.send(sb.toString());
    }
}
