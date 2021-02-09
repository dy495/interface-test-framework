package com.haisheng.framework.testng.bigScreen.crm.commonDs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import java.util.List;

public class JsonPathUtil {
    public static void main(String[] args) throws Exception{

//          String json="{\"code\":1000,\"data\":{\"list\":[{\"role_name\":\"超级管理员\",\"role_id\":9},{\"role_name\":\"总经理\",\"role_id\":10},{\"role_name\":\"销售总监\",\"role_id\":11},{\"role_name\":\"销售经理\",\"role_id\":12},{\"role_name\":\"销售顾问\",\"role_id\":13},{\"role_name\":\"销售前台\",\"role_id\":14},{\"role_name\":\"定损顾问\",\"role_id\":15},{\"role_name\":\"服务顾问\",\"role_id\":16},{\"role_name\":\"服务总监\",\"role_id\":18},{\"role_name\":\"市场总监\",\"role_id\":20},{\"role_name\":\"DCC销售顾问\",\"role_id\":23}]},\"source\":\"BUSINESS_PORSCHE\",\"message\":\"成功\",\"request_id\":\"d462259c-cbec-486f-9072-1d46813b5499\"}";
//        String jsonpath = "$.code==1000&&$.data.list[*].role_name&&$.data.list[*].role_id";
////          String json="{\"list\":[{\"role_name\":\"超级管理员\",\"role_id\":9},{\"role_name\":\"总经理\",\"role_id\":10},{\"role_name\":\"销售总监\",\"role_id\":11},{\"role_name\":\"销售经理\",\"role_id\":12},{\"role_name\":\"销售顾问\",\"role_id\":13},{\"role_name\":\"销售前台\",\"role_id\":14},{\"role_name\":\"定损顾问\",\"role_id\":15},{\"role_name\":\"服务顾问\",\"role_id\":16},{\"role_name\":\"服务总监\",\"role_id\":18},{\"role_name\":\"市场总监\",\"role_id\":20},{\"role_name\":\"DCC销售顾问\",\"role_id\":23}]}";
////          String jsonpath = "$.list[*].role_name0&&$.list[*].role_id";
////          spiltString(json,jsonpath);
//        String json1="{\"er_code\" : \"\"}";
//        String jsonpath1="$.er_code";
//        spiltString(json1,jsonpath1);


       String tt="{\n" +
               "    \"code\":200,\n" +
               "    \"msg\":\"SUCCESS\",\n" +
               "    \"sCode\":200,\n" +
               "    \"sMsg\":\"SUCCESS\",\n" +
               "    \"data\":[\n" +
               "        Object{...},\n" +
               "        {\n" +
               "            \"name\":\"应用退出(预置)\",\n" +
               "            \"key\":\"$$_session_end\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 车型-ID.4 CROZZ\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/car/id4-cartype-grey.html?type=248&id=170\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 车型-ID.4 CROZZ 耀夜首发版\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/car/id4-cartype-red.html?type=247&id=171\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 车型-参数配置-单款\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/car/parameter.v1.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 车型-参数配置-多款+对比\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/car/parameter-compare.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 车型-车型列表\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/car/model-list.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 档案-个人资料\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/my/myinfo.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 档案-我的订单\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/my/myorder.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 档案-我的订单-订单详情\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/my/myorder-info.html?id=202101151010139068561467\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 档案-我的帖子\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/my/mypost.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 档案-消息中心\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/my/message.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 档案-修改密码\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/my/updatepwd.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 服务-保值无忧\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/car/lease.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 服务-车主权益\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/car/rights.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 服务-充电解决方案\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/brand/charging-scheme.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 服务-大用户中心\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/car/customer-message.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 服务-一站式置换\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/car/replacement.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 购车工具-代理商查询\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/dealer/search.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 购车工具-金融计算器\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/car/calculator.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 购车工具-立即预定-车辆预定\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/car/reservation.html?id=&vid=170&order_status=\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 购车工具-立即预定-配置清单\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/car/matching-overview.html?id=&vid=170&order_status=\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 购车工具-用车成本计算器\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/car/analysis.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 购车工具-预约赏鉴\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/car/make-derve.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 品牌-ID. 分享\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/news/share.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 品牌-ID. 分享-ID. 分享详情页\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/news/share-details.html?id=1326\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 品牌-ID. 分享-ID.发帖\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/news/newpost.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 品牌-ID. 活动\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/news/activity.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 品牌-ID. 活动-ID. 活动详情页\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/news/activity-details.html?id=734&type=2\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 品牌-ID. 活动-ID. 立即参加活动\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/news/newpost.html?themeId=null\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 品牌-ID. 家族\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/brand/brand.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 品牌-ID. 新闻\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/news/index.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 品牌-ID. 新闻-ID. 新闻详情页\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/news/details.html?id=288\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID. 首页\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID.4 CROZZ 了解详情\",\n" +
               "            \"key\":\"Car-swiper\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID.4 CROZZ 首发限量版-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/id4-cartype-red.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID.车主权益大礼包 了解详情-ID.4 CROZZ 首发限量版\",\n" +
               "            \"key\":\"Id4-package\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID.车主权益大礼包 了解详情-首页\",\n" +
               "            \"key\":\"Member-rights-know-more\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID.分享-ID.分享列表页\",\n" +
               "            \"key\":\"Tab-idshare\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID.分享列表页-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/news/share.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID.分享详情页-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/news/share-details.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID.服务\",\n" +
               "            \"key\":\"block-service\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID.家族\",\n" +
               "            \"key\":\"block-brand\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID.前沿\",\n" +
               "            \"key\":\"block-news\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"id4-车型-灰\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/id4-cartype-grey.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID4.CROZZ\",\n" +
               "            \"key\":\"/allmodels.Model-list-click-2\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID4.CROZZ 首发限量版\",\n" +
               "            \"key\":\"/allmodels.Model-list-click-1\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"ID4.CROZZ 首发限量版\",\n" +
               "            \"key\":\"Model-list-click\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"sp-测试1\",\n" +
               "            \"key\":\"http://quickaplus.youmeng.winsenseos.com/\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"sp-测试2\",\n" +
               "            \"key\":\"http://quickaplus.youmeng.winsenseos.com/\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"编辑按钮-地区编辑\",\n" +
               "            \"key\":\"Info-edit\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"标签点击-个人资料-个人资料\",\n" +
               "            \"key\":\"user-center-tab-click\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"标签点击-外观设计\",\n" +
               "            \"key\":\"Exterior-tab-click\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"标签点击-先进科技\",\n" +
               "            \"key\":\"Technology-tab-click\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"标签点击-优雅内饰\",\n" +
               "            \"key\":\"Interior-tab-click\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"标签点击-卓越性能\",\n" +
               "            \"key\":\"Performance-tab-click\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"参数配置\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/parameter.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"查看更多\",\n" +
               "            \"key\":\"Read-more\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"查看购车合同\",\n" +
               "            \"key\":\"View-contract\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"查看购车协议\",\n" +
               "            \"key\":\"View-agreement\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"车辆认购书\",\n" +
               "            \"key\":\"https://id.faw-vw.com/brand/reserve-agreement.v2.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"车辆预定\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/reservation.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"车辆预定-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/matching-color.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"车轮选择-20英寸车轮\",\n" +
               "            \"key\":\"Select-tire-type\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"车型参数\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/parameter.v1.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"车型参数对比\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/parameter-compare.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"车型选配-测试\",\n" +
               "            \"key\":\"https://id-hub.faw-vw.t.ftms-wechat.com/car/matching.v2.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"车型选配概览\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/matching-overview.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"车型选择\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/matching.v2.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"车系选择-炫彩性能版\",\n" +
               "            \"key\":\"Select-series\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"充电解决方案\",\n" +
               "            \"key\":\"charge-solution\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"充电解决方案\",\n" +
               "            \"key\":\"https://id.faw-vw.com/brand/charging-describe.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"贷款额度查询\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/credit-helper.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"贷款额度结果\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/credit-result.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"大用户中心-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/customer-message.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"登录-ID.分享详情页\",\n" +
               "            \"key\":\"Login\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"登录-登录\",\n" +
               "            \"key\":\"login\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"登录-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/my/login.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"点赞\",\n" +
               "            \"key\":\"Article-like\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"底部导航栏\",\n" +
               "            \"key\":\"Footer-menu\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"顶部导航栏\",\n" +
               "            \"key\":\"header-menu\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"顶部导航栏\",\n" +
               "            \"key\":\"header-menu\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"订单详情\",\n" +
               "            \"key\":\"Order-Operation\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"订单详情\",\n" +
               "            \"key\":\"https://id.faw-vw.com/my/myorder-info-model.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"订单详情-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/my/myorder-info.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"订单状态\",\n" +
               "            \"key\":\"https://id.faw-vw.com/pay/pay-status.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"发布-ID.分享列表页\",\n" +
               "            \"key\":\"New-post\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"发布-ID.分享详情页\",\n" +
               "            \"key\":\"New-post\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"法律声明\",\n" +
               "            \"key\":\"https://id.faw-vw.com/brand/statement.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"返回首页\",\n" +
               "            \"key\":\"Order-cancel\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"发帖界面-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/news/newpost.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"分享\",\n" +
               "            \"key\":\"Article-share\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"分享-ID.分享详情页\",\n" +
               "            \"key\":\"Article-share\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"个人资料-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/my/myinfo.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"购车确认\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/buycar-information-confirm.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"购车协议\",\n" +
               "            \"key\":\"https://id.faw-vw.com/brand/reserve-agreement.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"购车信息-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/buycar-information.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"关于品牌-ID.分享列表页\",\n" +
               "            \"key\":\"Tab-brand\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"关于品牌-品牌活动列表\",\n" +
               "            \"key\":\"Tab-event\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"关于品牌-新闻动态列表\",\n" +
               "            \"key\":\"Tab-brand\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"话题点击\",\n" +
               "            \"key\":\"Hashtag-click\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"活动详情\",\n" +
               "            \"key\":\"https://id.faw-vw.com/news/activity-details.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"经销商查询\",\n" +
               "            \"key\":\"https://id.faw-vw.com/dealer/search.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"金牌顾问\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/financing.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"金融计算器\",\n" +
               "            \"key\":\"Finance-calculator\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"金融计算器\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/calculator.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"开启您的配置\",\n" +
               "            \"key\":\"Start-build-and-price\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"快捷登录\",\n" +
               "            \"key\":\"Mobile-login-tab\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"立即抢购首发限量款\",\n" +
               "            \"key\":\"Order-now\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"立即预订\",\n" +
               "            \"key\":\"Id4-Billboard\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"你的风格由你来定\",\n" +
               "            \"key\":\"Id4-build-and-price1\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"评论\",\n" +
               "            \"key\":\"Article-comment\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"品牌活动列表-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/news/activity.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"全部车型-页面\",\n" +
               "            \"key\":\"pages/allmodels\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"全部车型-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/model-list.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"取消-注册\",\n" +
               "            \"key\":\"Register-cancel\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"去选择轮胎\",\n" +
               "            \"key\":\"Select-tire\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"去选择内饰\",\n" +
               "            \"key\":\"Select-interior\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"去选择选装\",\n" +
               "            \"key\":\"Select-additional-package\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"融资租赁\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/lease.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"扫码进群活动\",\n" +
               "            \"key\":\"https://id.faw-vw.com/activity/20201124/index.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"上一步-车轮选择\",\n" +
               "            \"key\":\"Select-body-color\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"上一步-设计包选择\",\n" +
               "            \"key\":\"Select-interior\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"上一步-选装选择\",\n" +
               "            \"key\":\"Select-design-package\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"上一步-颜色选择\",\n" +
               "            \"key\":\"Start-build-and-price\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"设计包1\",\n" +
               "            \"key\":\"Select-design-package-type\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"视频\",\n" +
               "            \"key\":\"Homepage-video\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"首发车主权益-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/rights.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"授信成功\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/creditsuccess.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"授信失败\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/crediterror.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"首页-http\",\n" +
               "            \"key\":\"http://id.faw-vw.com\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"首页-https\",\n" +
               "            \"key\":\"https://id.faw-vw.com/\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"首页-index-https\",\n" +
               "            \"key\":\"https://id.faw-vw.com/index.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"首页banner\",\n" +
               "            \"key\":\"Homepage-billboard\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"搜索\",\n" +
               "            \"key\":\"Order-Search\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"帖子中心-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/my/mypost.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"提交-大用户中心\",\n" +
               "            \"key\":\"Customer-form\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"提交-购车信息\",\n" +
               "            \"key\":\"Order-Submit\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"提交-修改密码\",\n" +
               "            \"key\":\"pwdUpdate-form\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"提交-预约试驾\",\n" +
               "            \"key\":\"Test-Drive-form\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"提交-支付诚意金\",\n" +
               "            \"key\":\"Submit-payment\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"提交-注册\",\n" +
               "            \"key\":\"Register-submit\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"通用组件\",\n" +
               "            \"key\":\"Float-Buttons\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"退款申请\",\n" +
               "            \"key\":\"https://id.faw-vw.com/my/refund.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"退款详情\",\n" +
               "            \"key\":\"https://id.faw-vw.com/my/refund-details.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"外观设计 探索更多\",\n" +
               "            \"key\":\"Id4-exterior\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"完成配置\",\n" +
               "            \"key\":\"view-build-and-price\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"忘记密码\",\n" +
               "            \"key\":\"Forget-password\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"尾款支付-线上\",\n" +
               "            \"key\":\"https://id.faw-vw.com/pay/balance-payment-online.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"尾款支付-线下\",\n" +
               "            \"key\":\"https://id.faw-vw.com/pay/balance-payment-offline.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"我的订单-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/my/myorder.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"先进科技 探索更多\",\n" +
               "            \"key\":\"Id4-technology\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"销售合同\",\n" +
               "            \"key\":\"https://id.faw-vw.com/brand/sales-contract.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"新闻查看\",\n" +
               "            \"key\":\"View-news-detail\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"新闻动态-品牌活动列表\",\n" +
               "            \"key\":\"Tab-news\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"新闻动态列表-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/news/index.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"修改车辆配置\",\n" +
               "            \"key\":\"Change-build-and-price\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"修改付款人\",\n" +
               "            \"key\":\"Change-payer\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"修改购车方案\",\n" +
               "            \"key\":\"Change-buy-type\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"修改购车人信息\",\n" +
               "            \"key\":\"Change-buyer\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"修改交付地点\",\n" +
               "            \"key\":\"Change-delivery-address\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"修改密码-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/my/updatepwd.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"悬浮区域\",\n" +
               "            \"key\":\"Fixed-Header\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"选装2\",\n" +
               "            \"key\":\"Select-addition-type\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"询问最近经销商-ID.4 CROZZ 首发限量版\",\n" +
               "            \"key\":\"Block-dealer-locator\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"颜色选择\",\n" +
               "            \"key\":\"Select-color\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"颜色选择-科技灰内饰\",\n" +
               "            \"key\":\"Select-interior-type\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"隐私条款\",\n" +
               "            \"key\":\"https://id.faw-vw.com/brand/privacy-policy.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"用车成本\",\n" +
               "            \"key\":\"Usage-cost-estimate\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"用车成本分析\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/analysis.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"优雅内饰 探索更多\",\n" +
               "            \"key\":\"Id4-interior\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"预约试驾-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/make-derve.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"在线授信\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/credit.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"账号登录\",\n" +
               "            \"key\":\"Login-tab\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"找回密码\",\n" +
               "            \"key\":\"https://id.faw-vw.com/my/retrieve.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"支付成功\",\n" +
               "            \"key\":\"https://id.faw-vw.com/pay/pay-success.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"支付诚意金-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/pay/payon.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"支付错误\",\n" +
               "            \"key\":\"https://id.faw-vw.com/pay/error.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"支付提示\",\n" +
               "            \"key\":\"https://id.faw-vw.com/pay/paytips.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"支付选项点击-微信支付\",\n" +
               "            \"key\":\"Payment-method\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"置换服务\",\n" +
               "            \"key\":\"https://id.faw-vw.com/car/replacement.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"注册-ID.分享详情页\",\n" +
               "            \"key\":\"register\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"注册-登录\",\n" +
               "            \"key\":\"register\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"注册-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/my/register.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"卓越性能 探索更多\",\n" +
               "            \"key\":\"Id4-performance\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"主题点击\",\n" +
               "            \"key\":\"Topic-click\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"字母\",\n" +
               "            \"key\":\"zxcvbnm\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"字母\",\n" +
               "            \"key\":\"zxcvbnm\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"字母\",\n" +
               "            \"key\":\"zxcvbnm1\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"资讯详情\",\n" +
               "            \"key\":\"https://id.faw-vw.com/news/details.html\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"name\":\"咨询详情-页面\",\n" +
               "            \"key\":\"https://id.faw-vw.com/my/message.html\"\n" +
               "        }\n" +
               "    ],\n" +
               "    \"traceId\":\"f599b76a8ec4413088cd7bcc333051ad\",\n" +
               "    \"success\":true\n" +
               "}";
        ReadContext context = JsonPath.parse(tt);
        List<String> result = context.read("$.data[*].name");
        for (String a:result){
            System.out.println(a);
        }


    }
    public static void spiltString(String json, String str) throws Exception {
        for (String retval : str.split("&&")) {
            System.out.println("1:"+retval);
            if (retval.contains("==")) {
                checkequal(json,retval);
            } else if(retval.contains("!=")){
                checknotequal(json,retval);
            }
            else if(retval.contains("<=")||retval.contains(">")||retval.contains("<")){
                throw new Exception("暂不支持小于等于判断");
            }else {
                checkNotnull(json,retval);
            }
        }
    }

    public static void checkNotnull(String json, String retval) throws Exception {
        if (retval.contains("*")) {
            ReadContext context = JsonPath.parse(json);
            List<Object> result = context.read(retval);
            for (Object ob : result) {
                System.out.println(ob);
                if (ob == null || result.size() == 0){
                    throw new Exception("except:not null"+ ",actual:"+ob);
                }
            }
        } else {
            Object result = JSONPath.read(json, retval);
            if (result==null||result.equals("")) {
                throw new Exception("except:not null" + ",actual:"+result );
            }
        }
    }

    public static void checkequal(String json, String retval) throws Exception {
        String arr[] = retval.split("==");
        if (arr[0].contains("*")) {
            ReadContext context = JsonPath.parse(json);
            List<Object> result = context.read(arr[0]);
            for (Object ob : result) {
                if (!ob.equals(arr[1])) {
                    throw new Exception("except:" + arr[1] + ",actual:" + result);
                }
            }
        } else {
            Object result = JSONPath.read(json, arr[0]);
            if (!result.toString().equals(arr[1])) {
                throw new Exception("except:" + arr[1] + ",actual:" + result);
            }
        }
    }
    public static void checknotequal(String json, String retval) throws Exception {
        String arr[] = retval.split("!=");
        if (arr[0].contains("*")) {
            ReadContext context = JsonPath.parse(json);
            List<Object> result = context.read(arr[0]);
            for (Object ob : result) {
                if (ob.equals(arr[1])) {
                    throw new Exception("except:" + arr[1] + ",actual:" + result);
                }
            }
        } else {
            Object result = JSONPath.read(json, arr[0]);
            if (result.toString().equals(arr[1])) {
                throw new Exception("except:" + arr[1] + ",actual:" + result);
            }
        }
    }

    public static void checkebig(String json, String retval) throws Exception {
        String arr[] = retval.split(">=");
        if (arr[0].contains("*")) {
            ReadContext context = JsonPath.parse(json);
            List<Object> result = context.read(arr[0]);
            for (Object ob : result) {
                if (Long.parseLong(ob.toString()) >= Long.parseLong(arr[1])) {
                    throw new Exception("except:" + arr[1] + ",actual:" + ob);
                }
            }
        } else {
            Object result = JSONPath.read(json, arr[0]);
            if (Long.parseLong(result.toString()) >= Long.parseLong(arr[1])) {
                throw new Exception("except:" + arr[1] + ",actual:" + result);
            }
        }
    }

}
