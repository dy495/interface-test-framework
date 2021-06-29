package com.haisheng.framework.testng.bigScreen.yuntong.wm.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.personaldata.AppPersonalOverviewBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.personaldata.AppReceptionLinkScoreBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.voicerecord.AppDepartmentPageBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.voicerecord.AppDetailBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.voicerecord.AppPersonalPageBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.personaldata.AppPersonalOverviewScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.personaldata.AppReceptionLinkScoreScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.voicerecord.AppDepartmentPageScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.voicerecord.AppDetailScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.voicerecord.AppPersonalPageScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.loginuser.LoginApp;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.loginuser.LoginPc;
import com.haisheng.framework.util.CommonUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessUtil extends BasicUtil {
    private final VisitorProxy visitor;

    public BusinessUtil(VisitorProxy visitor) {
        super(visitor);
        this.visitor = visitor;
    }

    /**
     * pc账号登录
     *
     * @param enumAccount 账号
     */
    public void loginPc(@NotNull EnumAccount enumAccount) {
        IScene scene = LoginPc.builder().phone(enumAccount.getPhone()).verificationCode(enumAccount.getPassword()).build();
        visitor.login(scene);
    }

    /**
     * app账号登陆
     *
     * @param enumAccount 账号
     */
    public void loginApp(@NotNull EnumAccount enumAccount) {
        IScene scene = LoginApp.builder().phone(enumAccount.getPhone()).verificationCode(enumAccount.getPassword()).build();
        visitor.login(scene);
    }

    /**
     * 获取app部门接待分页信息集合
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 接待信息集合
     */
    public List<AppDepartmentPageBean> getAppDepartmentPageList(int dataCycleType, String startDate, String endDate) {
        List<AppDepartmentPageBean> list = new ArrayList<>();
        JSONObject lastValue = null;
        JSONArray array;
        do {
            JSONObject response = AppDepartmentPageScene.builder().orderColumn(0).isReverse(false).dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).lastValue(lastValue).size(10).build().invoke(visitor);
            lastValue = response.getJSONObject("last_value");
            array = response.getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppDepartmentPageBean.class)).collect(Collectors.toList()));
        } while (array.size() == 10);
        return list;
    }

    /**
     * 获取app个人接待详情-记录分页
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 接待详情集合
     */
    public List<AppPersonalPageBean> getAppPersonalPageList(int dataCycleType, String salesId, String startDate, String endDate) {
        List<AppPersonalPageBean> list = new ArrayList<>();
        JSONObject lastValue = null;
        JSONArray array;
        do {
            JSONObject response = AppPersonalPageScene.builder().orderColumn(100).isReverse(false).dataCycleType(dataCycleType).salesId(salesId).startDate(startDate).endDate(endDate).lastValue(lastValue).size(10).build().invoke(visitor);
            lastValue = response.getJSONObject("last_value");
            array = response.getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppPersonalPageBean.class)).collect(Collectors.toList()));
        } while (array.size() == 10);
        return list;
    }

    /**
     * 获取个人数据总览
     *
     * @param dataCycleType 时间类型
     * @param salesId       销售id
     * @param startDate     开始时间
     * @param endDate       结束时间
     * @return 数据总览
     */
    public AppPersonalOverviewBean getAppPersonalOverview(int dataCycleType, String salesId, String startDate, String endDate) {
        IScene scene = AppPersonalOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).salesId(salesId).build();
        return toJavaObject(scene, AppPersonalOverviewBean.class);
    }

    /**
     * 获取接待详情
     *
     * @param receptionId 接待id
     * @return 接待详情
     */
    public AppDetailBean getAppVoiceRecordDetail(long receptionId) {
        IScene scene = AppDetailScene.builder().id(receptionId).build();
        return toJavaObject(scene, AppDetailBean.class);
    }


    public List<AppReceptionLinkScoreBean> getAppReceptionLinkScore(int dataCycleType, String salesId, String startDate, String endDate) {
        JSONObject response = AppReceptionLinkScoreScene.builder().dataCycleType(dataCycleType).salesId(salesId).startDate(startDate).endDate(endDate).build().invoke(visitor);
        return response.getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppReceptionLinkScoreBean.class)).collect(Collectors.toList());
    }

    public int getAppReceptionLinkScoreAverage(int dataCycleType, String salesId, String startDate, String endDate) {
        return getAppReceptionLinkScore(dataCycleType, salesId, startDate, endDate).stream().mapToInt(AppReceptionLinkScoreBean::getPersonAverageScore).sum();
    }


    /**
     * 获取接待详情中的平均分
     *
     * @param receptionId 接待id
     * @return 平均分
     */
    public Integer getAverageScoreByReceptionDetail(Long receptionId) {
        IScene scene = AppDetailScene.builder().id(receptionId).build();
        JSONArray scores = toJavaObject(scene, AppDetailBean.class).getScores();
        if (scores == null) {
            return null;
        }
        int scoreSum = scores.stream().map(e -> (JSONObject) e).mapToInt(e -> e.getInteger("score")).sum();
        return CommonUtil.getIntRatio(scoreSum, 5);
    }

    /**
     * 获取接待话术得分
     *
     * @param receptionId 接待id
     * @param type        话术id
     * @return 分数
     */
    public Integer getScoreByType(Long receptionId, Integer type) {
        IScene scene = AppDetailScene.builder().id(receptionId).build();
        JSONArray scores = toJavaObject(scene, AppDetailBean.class).getScores();
        if (scores == null) {
            return null;
        }
        return scores.stream().map(e -> (JSONObject) e).filter(e -> e.getInteger("type").equals(type)).map(e -> e.getInteger("score")).findFirst().orElse(0);
    }

    /**
     * 获取总分
     *
     * @param receptionId 接待id
     * @return 总分
     */
    public Integer getScoreSum(Long receptionId) {
        IScene scene = AppDetailScene.builder().id(receptionId).build();
        JSONArray scores = toJavaObject(scene, AppDetailBean.class).getScores();
        return scores == null ? 0 : scores.stream().map(e -> (JSONObject) e).mapToInt(e -> e.getInteger("score")).sum();
    }


}