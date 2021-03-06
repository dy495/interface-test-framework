package com.haisheng.framework.testng.bigScreen.itemMall.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemMall.common.bean.FullCourtDataBean;
import com.haisheng.framework.testng.bigScreen.itemMall.common.bean.FullCourtTrendBean;
import com.haisheng.framework.testng.bigScreen.itemMall.common.bean.RegionDataBean;
import com.haisheng.framework.testng.bigScreen.itemMall.common.bean.RegionTrendBean;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.LoginPcMall;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.realtime.FullCourtTrendScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.realtime.RegionRealTimeTrendScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.DingPushUtil;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.MD5Util;

import javax.validation.constraints.NotNull;
import java.util.List;
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
     * ????????????
     *
     * @param time ????????????
     * @return boolean
     */
    public boolean filterTime(@org.jetbrains.annotations.NotNull String time) {
        return time.compareTo("09:00") >= 0 && time.compareTo("22:00") <= 0;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param type    ????????????pv/uv
     * @param nowTime ????????????
     * @return FullCourtTrendBean
     */
    public FullCourtTrendBean findCurrentTimeData(String type, String nowTime) {
        IScene scene = FullCourtTrendScene.builder().type(type).build();
        List<FullCourtTrendBean> list = toJavaObjectList(scene, FullCourtTrendBean.class, "list");
        return list.stream().filter(e -> filterTime(e.getTime())).filter(e -> e.getTime().substring(0, 2).equals(nowTime)).findFirst().orElse(null);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param type    ????????????
     * @param region  ????????????
     * @param nowTime ????????????
     * @return JSONObject
     */
    public JSONObject findCurrentTimeData(String type, String region, String nowTime) {
        JSONObject rsp = RegionRealTimeTrendScene.builder().type(type).region(region).build().visitor(visitor).execute();
        List<JSONObject> list = rsp.getJSONArray("list").toJavaList(JSONObject.class);
        return list.stream().filter(e -> filterTime(e.getString("x_value"))).filter(e -> e.getString("x_value").substring(0, 2).equals(nowTime)).findFirst().orElse(null);
    }

    /**
     * ???????????????????????????0?????????
     *
     * @param seriesList ????????????
     * @param uvObj      uv??????object
     * @param pvObj      pv??????object
     * @return ????????????RegionTrendBean
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

    /**
     * ??????????????????????????????
     *
     * @param subjectName       ????????????
     * @param time              ??????
     * @param fullCourtDataBean fullCourtDataBean
     */
    public void sendMessage(String subjectName, String time, FullCourtDataBean fullCourtDataBean) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("#### ").append(subjectName).append(" ").append(time).append(" ").append("??????????????????").append("\n");
        sb.append("###### ").append("PV????????????").append(fullCourtDataBean.getPv()).append(" ").append("UV????????????").append(fullCourtDataBean.getUv());
        DingPushUtil util = new DingPushUtil();
        util.changeWeHook(DingWebhook.PV_UV_ACCURACY_GRP);
        util.send(sb.toString());
    }

    /**
     * ??????????????????????????????
     *
     * @param subjectName ????????????
     * @param time        ??????
     * @param list        List<RegionDataBean>
     */
    public void sendMessage(String subjectName, String time, List<RegionDataBean> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("#### ").append(subjectName).append(" ").append(time).append(" ").append("??????????????????").append("\n");
        list.forEach(regionData -> {
            String region = regionData.getRegion();
            List<RegionTrendBean> regionTrendBeanList = regionData.getRegionTrendBeanList();
            sb.append("###### **").append(region).append(" ????????????").append(regionTrendBeanList.size()).append("??????????????????0").append("**").append("\n");
            regionTrendBeanList.forEach(regionTrend -> sb.append("######  ").append(regionTrend.getName()).append("--").append(regionTrend.getKey()).append("\n"));
        });
        DingPushUtil util = new DingPushUtil();
        util.changeWeHook(DingWebhook.PV_UV_ACCURACY_GRP);
        util.send(sb.toString());
    }
}
