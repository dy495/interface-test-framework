package com.haisheng.framework.testng.bigScreen.yuntong.wm.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.LoginApp;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.LoginPc;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.voicerecord.AppDepartmentPageBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.voicerecord.AppDetailBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.voicerecord.AppPersonalPageBean;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.voicerecord.AppDepartmentPageScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.voicerecord.AppDetailScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.voicerecord.AppPersonalPageScene;
import com.haisheng.framework.util.CommonUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessUtil extends BasicUtil {
    private final VisitorProxy visitor;

    /**
     * 构造函数私有化。外部不可调用构造函数
     *
     * @param visitor 访问者
     */
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
        LoginPc.builder().phone(enumAccount.getPhone()).verificationCode(enumAccount.getPassword()).build().invoke(visitor);
    }

    /**
     * app账号登陆
     *
     * @param enumAccount 账号
     */
    public void loginApp(@NotNull EnumAccount enumAccount) {
        LoginApp.builder().phone(enumAccount.getPhone()).verificationCode(enumAccount.getPassword()).build().invoke(visitor);
    }

    /**
     * 获取app部门接待分页信息集合
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 接待信息集合
     */
    public List<AppDepartmentPageBean> getAppDepartmentPageList(String startDate, String endDate) {
        List<AppDepartmentPageBean> list = new ArrayList<>();
        JSONObject lastValue = null;
        do {
            JSONObject response = AppDepartmentPageScene.builder().startDate(startDate).endDate(endDate).lastValue(lastValue).size(20).build().invoke(visitor);
            lastValue = response.getJSONObject("last_value");
            list.addAll(response.getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppDepartmentPageBean.class)).collect(Collectors.toList()));
        } while (list.size() == 20);
        return list;
    }

    /**
     * 获取app个人接待详情-记录分页
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 接待详情集合
     */
    public List<AppPersonalPageBean> getAppPersonalPageList(String salesId, String startDate, String endDate) {
        List<AppPersonalPageBean> list = new ArrayList<>();
        JSONObject lastValue = null;
        do {
            JSONObject response = AppPersonalPageScene.builder().salesId(salesId).startDate(startDate).endDate(endDate).lastValue(lastValue).size(20).build().invoke(visitor);
            lastValue = response.getJSONObject("last_value");
            list.addAll(response.getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppPersonalPageBean.class)).collect(Collectors.toList()));
        } while (list.size() == 20);
        return list;
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
        return CommonUtil.getIntRatio(scoreSum, 5, 0);
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
