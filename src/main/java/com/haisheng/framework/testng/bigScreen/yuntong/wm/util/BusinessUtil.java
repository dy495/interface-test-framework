package com.haisheng.framework.testng.bigScreen.yuntong.wm.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
    private static volatile BusinessUtil INSTANCE = null;
    private static VisitorProxy visitorProxy;

    /**
     * 构造函数私有化。外部不可调用构造函数
     *
     * @param visitor 访问者
     */
    private BusinessUtil(VisitorProxy visitor) {
        super(visitor);
        visitorProxy = visitor;
    }

    /**
     * 配置一个懒汉单例
     *
     * @param visitor 访问者
     * @return BusinessUtil
     */
    public static synchronized BusinessUtil getInstance(VisitorProxy visitor) {
        if (INSTANCE == null) {
            INSTANCE = new BusinessUtil(visitor);
            if (visitorProxy != visitor) {
                INSTANCE = new BusinessUtil(visitor);
            }
        }
        return INSTANCE;
    }

    /**
     * pc账号登录
     *
     * @param enumAccount 账号
     */
    public void loginPc(@NotNull EnumAccount enumAccount) {
        LoginPc.builder().phone(enumAccount.getPhone()).verificationCode(enumAccount.getPassword()).build().invoke(visitorProxy);
    }

    /**
     * app账号登陆
     *
     * @param enumAccount 账号
     */
    public void loginApp(@NotNull EnumAccount enumAccount) {
        LoginApp.builder().phone(enumAccount.getPhone()).verificationCode(enumAccount.getPassword()).build().invoke(visitorProxy);
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
            JSONObject response = AppDepartmentPageScene.builder().startDate(startDate).endDate(endDate).lastValue(lastValue).size(20).build().invoke(visitorProxy);
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
            JSONObject response = AppPersonalPageScene.builder().salesId(salesId).startDate(startDate).endDate(endDate).lastValue(lastValue).size(20).build().invoke(visitorProxy);
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
        return CommonUtil.getIntValue(scoreSum, 5, 0);
    }
}
