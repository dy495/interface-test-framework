package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.pc.manage.VoiceEvaluationPageBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.pc.sensitivewords.LabelListBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.pc.specialaudio.SpecialAudioPageBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.dataprovider.DataClass;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.aftermanage.VoiceDetailScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.aftermanage.EvaluationPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.aftersensitivewords.AfterSensitiveBehaviorPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.aftersensitivewords.AfterSensitiveWordsLabelListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.aftersensitivewords.AfterSensitiveWordsPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.afterspecialaudio.AfterSpecialAudioPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.afterspeechtechnique.AfterSpeechTechniquePageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AfterSaleVoiceDataManagerCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCT = EnumTestProduct.YT_DAILY_GK;
    private static final EnumAccount ACCOUNT = EnumAccount.YT_DAILY_YS;
    private final VisitorProxy visitor = new VisitorProxy(PRODUCT);
    private final SceneUtil util = new SceneUtil(visitor);

    @Override
    public void initial() {
        logger.debug("before class initial");
        //替换checklist的相关信息
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCT.getDesc() + commonConfig.checklistQaOwner);
        //放入shopId
        commonConfig.setShopId(ACCOUNT.getShopId()).setRoleId(ACCOUNT.getRoleId()).setProduct(PRODUCT.getAbbreviation());
        beforeClassInit(commonConfig);
        util.loginApp(ACCOUNT);
    }

    @Override
    public void clean() {
        afterClassClean();
    }

    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        logger.logCaseStart(caseResult.getCaseName());
    }

    @Test(description = "语音评鉴列表信息与客户详情页信息一致")
    public void voiceEvaluation_data_2() {
        try {
            IScene voiceEvaluationPageScene = EvaluationPageScene.builder().build();
            long evaluationTotal = voiceEvaluationPageScene.visitor(visitor).execute().getLong("total");
            visitor.setProduct(EnumTestProduct.YT_DAILY_JD);
            IScene preSalesReceptionPageScene = PreSalesReceptionPageScene.builder().build();
            long receptionTotal = preSalesReceptionPageScene.visitor(visitor).execute().getLong("total");
            CommonUtil.valueView(evaluationTotal, receptionTotal);
            Preconditions.checkArgument(evaluationTotal <= receptionTotal, "语音评鉴列表数：" + evaluationTotal + " 销售接待页列表数：" + receptionTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            visitor.setProduct(EnumTestProduct.YT_DAILY_GK);
            saveData("语音评鉴列表数<=销售接待页列表数");
        }
    }

    @Test(description = "语音评鉴列表详情接待得分 = 5个环节分数之和 / 5， 四舍五入取整")
    public void voiceEvaluation_data_3() {
        try {
            IScene voiceEvaluationPageScene = EvaluationPageScene.builder().enterStatus(1).build();
            List<VoiceEvaluationPageBean> voiceEvaluationPageList = util.toJavaObjectList(voiceEvaluationPageScene, VoiceEvaluationPageBean.class);
            voiceEvaluationPageList.stream().filter(e -> e.getEvaluateScore() != null).filter(e -> e.getEvaluateScore() != 0)
                    .map(e -> VoiceDetailScene.builder().id(e.getId()).build().visitor(visitor).execute()).forEach(object -> {
                int averageScore = object.getInteger("average_score");
                int scoreSum = object.getJSONArray("scores").stream().map(e -> (JSONObject) e).mapToInt(e -> e.getInteger("score")).sum();
                int mathResult = CommonUtil.getRoundIntRatio(scoreSum, 5);
                CommonUtil.valueView(averageScore, mathResult);
                Preconditions.checkArgument(averageScore == mathResult, "语音评鉴列表详情接待得分：" + averageScore + " 5个环节分数之和 / 5， 四舍五入取整：" + mathResult);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("语音评鉴列表详情接待得分 = 5个环节分数之和 / 5， 四舍五入取整");
        }
    }

    @Test(description = "建议中的环节内容 是 各环节得分标签中置灰 的子集")
    public void voiceEvaluation_data_4() {
        try {
            IScene voiceEvaluationPageScene = EvaluationPageScene.builder().enterStatus(1).build();
            List<VoiceEvaluationPageBean> voiceEvaluationPageList = util.toJavaObjectList(voiceEvaluationPageScene, VoiceEvaluationPageBean.class);
            voiceEvaluationPageList.stream().filter(e -> e.getEvaluateScore() != null).filter(e -> e.getEvaluateScore() != 0)
                    .map(e -> VoiceDetailScene.builder().id(e.getId()).build().visitor(visitor).execute()).forEach(object -> {
                int adviceListSize = object.getJSONArray("advice_list").size();
                int count = (int) object.getJSONArray("link_label_list").stream().map(link -> (JSONObject) link)
                        .mapToLong(link -> link.getJSONArray("labels").stream().map(label -> (JSONObject) label)
                                .filter(label -> !label.getBoolean("is_hit")).count()).sum();
                CommonUtil.valueView(adviceListSize, count);
                Preconditions.checkArgument(adviceListSize <= count, "建议中的环节内容条数：" + adviceListSize + " 各环节得分标签置灰的个数：" + count);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("建议中的环节内容 是 各环节得分标签中 置灰环节 的子集");
        }
    }

    @Test(description = "各环节得分 约等于 该环节的 高亮标签数/总标签数 * 100", dataProvider = "receptionType", dataProviderClass = DataClass.class, enabled = false)
    public void voiceEvaluation_data_5(String typeName, int type) {
        try {
            IScene voiceEvaluationPageScene = EvaluationPageScene.builder().enterStatus(1).build();
            List<VoiceEvaluationPageBean> voiceEvaluationPageList = util.toJavaObjectList(voiceEvaluationPageScene, VoiceEvaluationPageBean.class);
            voiceEvaluationPageList.stream().filter(e -> e.getEvaluateScore() != null).filter(e -> e.getEvaluateScore() != 0)
                    .map(e -> VoiceDetailScene.builder().id(e.getId()).build().visitor(visitor).execute()).forEach(object -> {
                JSONArray linkLabelList = object.getJSONArray("link_label_list");
                JSONArray labels = linkLabelList.stream().map(e -> (JSONObject) e).filter(e -> e.getString("type_name").equals(typeName)).map(e -> e.getJSONArray("labels")).findFirst().orElse(null);
                Preconditions.checkNotNull(labels, object.getString("name") + " " + typeName + "标签为空");
                int isHitCount = (int) labels.stream().map(e -> (JSONObject) e).filter(e -> e.getBoolean("is_hit")).count();
                CommonUtil.valueView(isHitCount, labels.size());
                int mathResult = (int) Math.round(((double) isHitCount / (double) labels.size()) * 100);
                int score = object.getJSONArray("scores").stream().map(e -> (JSONObject) e).filter(e -> e.getInteger("type").equals(type)).map(e -> e.getInteger("score")).findFirst().orElse(0);
                CommonUtil.valueView(mathResult, score);
                Preconditions.checkArgument(mathResult == score, object.getString("name") + " " + typeName + "得分：" + score + " 标签计算结果：" + mathResult);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("各环节得分 约等于 该环节的 高亮标签数/总标签数 * 100");
        }
    }

    @Test(description = "柱状图总 各标签数量 = 列表中该标签数量之和", dataProvider = "receptionWord", dataProviderClass = DataClass.class)
    public void voiceEvaluation_data_6(String word) {
        try {
            IScene labelListScene = AfterSensitiveWordsLabelListScene.builder().build();
            List<LabelListBean> labelList = util.toJavaObjectList(labelListScene, LabelListBean.class, "list");
            int labelCount = labelList.stream().filter(e -> e.getWords().equals(word)).map(LabelListBean::getCount).findFirst().orElse(0);
            IScene sensitiveBehaviorPageScene = AfterSensitiveBehaviorPageScene.builder().build();
            List<JSONObject> list = util.toJavaObjectList(sensitiveBehaviorPageScene, JSONObject.class);
            int count = (int) list.stream().filter(e -> e.getString("words").equals(word)).count();
            CommonUtil.valueView(labelCount, count);
            Preconditions.checkArgument(labelCount == count, word + " 在敏感词标签中数量：" + labelCount + " 在行为记录中数量：" + count);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("柱状图总 各标签数量 = 列表中该标签数量之和");
        }
    }

    @Test(description = "柱状图数量之和 = 行为记录列表数")
    public void voiceEvaluation_data_7() {
        try {
            IScene labelListScene = AfterSensitiveWordsLabelListScene.builder().build();
            List<LabelListBean> labelList = util.toJavaObjectList(labelListScene, LabelListBean.class, "list");
            int countSum = labelList.stream().mapToInt(LabelListBean::getCount).sum();
            int total = AfterSensitiveBehaviorPageScene.builder().build().visitor(visitor).execute().getInteger("total");
            CommonUtil.valueView(countSum, total);
            Preconditions.checkArgument(countSum == total, "柱状图数量之和" + countSum + " 行为记录列表数：" + total);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("柱状图数量之和 = 行为记录列表数");
        }
    }

    @Test(description = "行为记录列表中的 敏感词 和敏感词风控 均为 【敏感词设置】中的子集")
    public void voiceEvaluation_data_8() {
        try {
            IScene sensitiveWordsPageScene = AfterSensitiveWordsPageScene.builder().build();
            List<JSONObject> sensitiveWordsPageList = util.toJavaObjectList(sensitiveWordsPageScene, JSONObject.class);
            List<String> wordList = sensitiveWordsPageList.stream().map(e -> e.getString("words")).collect(Collectors.toList());
            IScene sensitiveBehaviorPageScene = AfterSensitiveBehaviorPageScene.builder().build();
            List<JSONObject> list = util.toJavaObjectList(sensitiveBehaviorPageScene, JSONObject.class);
            list.forEach(e -> Preconditions.checkArgument(wordList.contains(e.getString("words")), "敏感词设置中不包含敏感词：" + e.getString("words")));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("行为记录列表中的 敏感词 和敏感词风控 均为 【敏感词设置】中的子集");
        }
    }

    @Test(description = "特殊音频审核列表数量<=语音评鉴列表数")
    public void voiceEvaluation_data_9() {
        try {
            int voiceEvaluationPageTotal = EvaluationPageScene.builder().build().visitor(visitor).execute().getInteger("total");
            int specialAudioPageSceneTotal = AfterSpecialAudioPageScene.builder().build().visitor(visitor).execute().getInteger("total");
            Preconditions.checkArgument(voiceEvaluationPageTotal >= specialAudioPageSceneTotal, "特殊音频审核列表数量：" + specialAudioPageSceneTotal + " 语音评鉴列表数：" + voiceEvaluationPageTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("特殊音频审核列表数量<=语音评鉴列表数");
        }
    }

    @Test(description = "话术考核设置--筛选全部列表条数=筛选各话术环节的列表条数之和")
    public void voiceEvaluation_data_10() {
        try {
            int total = AfterSpeechTechniquePageScene.builder().build().visitor(visitor).execute().getInteger("total");
            int[] ints = {100, 200, 300, 400, 500};
            int sum = Arrays.stream(ints).mapToObj(type -> AfterSpeechTechniquePageScene.builder().type(type).build()).map(speechTechniquePageScene -> util.toJavaObjectList(speechTechniquePageScene, JSONObject.class)).mapToInt(List::size).sum();
            CommonUtil.valueView(total, sum);
            Preconditions.checkArgument(total == sum, "筛选全部列表条数：" + total + "筛选各话术环节的列表条数之和：" + sum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("话术考核设置--筛选全部列表条数=筛选各话术环节的列表条数之和");
        }
    }

    @Test(description = "话术考核设置--筛选全部列表条数=筛选各话术环节的列表条数之和")
    public void voiceEvaluation_data_11() {
        try {
            IScene specialAudioPageScene = AfterSpecialAudioPageScene.builder().build();
            List<SpecialAudioPageBean> list = util.toJavaObjectList(specialAudioPageScene, SpecialAudioPageBean.class);
            list.forEach(e -> {
                long id = e.getId();
                String receptorName = e.getReceptorName();
                String receptionTime = e.getReceptionTime();
                int score = e.getScore();
                IScene voiceEvaluationPageScene = EvaluationPageScene.builder().build();
                List<VoiceEvaluationPageBean> voiceEvaluationPageList = util.toJavaObjectList(voiceEvaluationPageScene, VoiceEvaluationPageBean.class);
                voiceEvaluationPageList.stream().filter(a -> a.getId().equals(id)).forEach(a -> {
                    String name = a.getReceptorName();
                    String time = a.getReceptionTime();
                    int evaluateScore = a.getEvaluateScore();
                    CommonUtil.valueView(receptorName, receptionTime, score, name, time, evaluateScore);
                    Preconditions.checkArgument(receptorName.equals(name), "特殊音频审核接待顾问：" + receptorName, " 语音评鉴接待顾问：" + name);
                    Preconditions.checkArgument(time.contains(receptionTime), "特殊音频审核接待日期：" + receptionTime, " 语音评鉴接待日期：" + time);
                    Preconditions.checkArgument(score == evaluateScore, "特殊音频审核接待分数：" + evaluateScore, " 语音评鉴接待分数：" + score);
                });
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("话术考核设置--筛选全部列表条数=筛选各话术环节的列表条数之和");
        }
    }
}
