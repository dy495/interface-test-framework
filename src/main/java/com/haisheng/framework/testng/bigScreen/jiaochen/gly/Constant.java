package com.haisheng.framework.testng.bigScreen.jiaochen.gly;

import org.testng.annotations.DataProvider;

public class Constant {

    /**
     * @description:接待管理-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_ReceptionManageFilter")
    public static Object[][]receptionManageFilter_pram() {
        return new String[][]{
                {"plate_number", "plate_number"},
                {"reception_sale_id", "reception_sale_name"},
                {"customer_name","customer_name"},
                {"reception_status","reception_status"},
                {"customer_phone","customer_phone"},
                {"reception_type","reception_type"},
                {"shop_id","shop_id"},
        };
    }

    /**
     * @description:销售管理列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_PreSleCustomerManageFilter")
    public static Object[][] preSleCustomerManage_pram() {
        return new String[][]{
                {"customer_name", "customer_name"},
                {"customer_phone", "customer_phone"},
                {"sale_name","sale_name"},
        };
    }

    /**
     * @description:V3.1售后管理列表-筛选栏
     * @author: gly
     * @time: 2021/3/26
     */
    @DataProvider(name = "SELECT_AfterSleCustomerManageFilter")
    public static Object[][] afterSleCustomerManage_pram() {
        return new String[][]{
                {"vehicle_chassis_code", "vehicle_chassis_code"},
                {"customer_name","repair_customer_name"},
                {"customer_phone","repair_customer_phone"},
                {"registration_status","registration_status_name"},
                {"brand_id","brand_name"},
                {"shop_id","shop_id"},

//                {"vehicle_chassis_code","vehicle_chassis_code"},
//                {"shop_id","shop_id"},
//                {"shop_id","shop_id"},

        };
    }

    /**
     * @description:小程序客户管理列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_weChatSleCustomerManageFilter")
    public static Object[][] weChatSleCustomerManage_pram() {
        return new String[][]{
                {"customer_phone","customer_phone"},
                {"vip_type","vip_type_name"},
        };
    }

    /**
     * @description:V2.0预约记录列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_appointmentRecordFilter")
    public static Object[][] appointmentRecordFilter_pram() {
        return new String[][]{
                {"plate_number", "plate_number"},
                {"shop_id", "shop_id"},
                {"customer_name", "customer_name"},
                {"confirm_status", "appointment_status_name"},
                {"customer_phone", "customer_phone"},
                {"is_overtime", "is_overtime"},
                {"service_sale_id", "customer_manager"},
        };
    }

    /**
     * @description:保养配置列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_maintainFilter")
    public static Object[][] maintainFilter_pram() {
        return new String[][]{
                {"brand_name", "brand_name"},
                {"manufacturer", "manufacturer"},
                {"car_model", "model"},
                {"year", "year"},
                {"car_style","style_name"}
        };
    }

    /**
     * @description:卡券管理列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_voucherFormFilter")
    public static Object[][] voucherFormFilter_pram(){
        return new String[][]{
                {"subject_name", "subject_name"},
                {"voucher_name", "voucher_name"},
                {"creator_name", "creator_name"},
                {"creator_account", "creator_account"},
                {"voucher_status","voucher_status"},
//                {"voucher_type","voucher_name"}   返回字段中没有此字段

        };
    }

    /**
     * @description:发卡记录列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_sendRecordFilter")
    public static Object[][] sendRecordFilter_pram(){
        return new String[][]{
                {"voucher_name", "voucher_name"},
                {"sender", "sender"},

        };
    }

    /**
     * @description:核销记录列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_verificationRecordFilter")
    public static Object[][] verificationRecordFilter_pram(){
        return new String[][]{
                {"voucher_name", "voucher_name"},
                {"customer_name", "customer_name"},
                {"customer_phone", "customer_phone"},
                {"verify_code", "verification_code"},
                {"verify_sale_phone", "verification_account"},
                {"verify_channel", "verification_channel_name"},

        };
    }

    /**
     * @description:核销人员记录列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_verificationPeopleFilter")
    public static Object[][] verificationPeopleFilter_pram(){
        return new String[][]{
                {"verification_person", "verification_person"},
                {"verification_phone", "verification_phone"},
                {"verification_code", "verification_code"},
        };
    }

    /**
     * @description:套餐表单列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_packageFormFilter")
    public static Object[][] packageFormFilter_pram(){
        return new String[][]{
                {"package_name", "package_name"},
                {"creator", "creator"},
                {"package_status","status"},
                {"shop_id", "shop_name"},
        };
    }

    /**
     * @description:套餐购买买记录-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_buyPackageRecordFilter")
    public static Object[][] buyPackageRecordFilter_pram(){
        return new String[][]{
                {"package_name", "package_name"},
                {"send_type", "pay_type_name"},
                {"sender","package_belongs"},
                {"customer_phone", "customer_phone"},

        };
    }

    /**
     * @description:消息表单-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_messageFormFilter")
    public static Object[][] messageFormFilter_pram(){
        return new String[][]{
                {"shop_id", "shop_id"},
                {"customer_name", "customer_name"},
                {"message_type", "message_type_name"},
                {"send_account","send_account"}

        };
    }

    /**
     * @description:文章表单-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_articleFilter")
    public static Object[][] articleFilter_pram(){
        return new String[][]{
                {"title", "title"},

        };
    }

    /**
     * @description:报名列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_registerListFilter")
    public static Object[][] registerListFilter_pram(){
        return new String[][]{
                {"title", "title"},

        };
    }

    /**
     * @description:报名审批页-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_approvalListFilter")
    public static Object[][] approvalListFilter_pram(){
        return new String[][]{
                {"customer_name", "participant_name"},
                {"phone", "phone"},
                {"status", "status_name"},
        };
    }

    /**
     * @description:卡券申请-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_applyListFilter")
    public static Object[][] applyListFilter_pram(){
        return new String[][]{
                {"name", "name"},
                {"apply_name", "apply_name"},
                {"status", "status_name"},
                {"subject_type", "subject_name"},

        };
    }

    /**
     * @description:门店列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_shopListFilter")
    public static Object[][] shopListFilter_pram(){
        return new String[][]{
                {"name", "name"},
        };
    }

    /**
     * @description:品牌列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_brandListFilter")
    public static Object[][] brandListFilter_pram(){
        return new String[][]{
                {"name", "name"},
                {"first_letter","first_letter"}
        };
    }
    /**
     * @description:车系列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_carStyleListFilter")
    public static Object[][] carStyleListFilter_pram(){
        return new String[][]{
                {"name", "name"},
        };
    }

    /**
     * @description:车型列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_carModelListFilter")
    public static Object[][] carModelListFilter_pram(){
        return new String[][]{
                {"name", "name"},
                {"year", "year"},
        };
    }

    /**
     * @description:角色列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_roleListFilter")
    public static Object[][] roleListFilter_pram(){
        return new String[][]{
                {"name", "name"},
        };
    }

    /**
     * @description:员工列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_staffListFilter")
    public static Object[][] satffListFilter_pram(){
        return new String[][]{
                {"name", "name"},
                {"phone", "phone"},
                {"role_id", "role_id"},
                {"shop_id", "shop_id"},
        };
    }

    /**
     * @description:导入记录列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_importListFilter")
    public static Object[][] importListFilter_pram(){
        return new String[][]{
                {"type", "type"},
                {"user", "user_name"},
        };
    }

    /**
     * @description:导出记录列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_exportListFilter")
    public static Object[][] exportListFilter_pram(){
        return new String[][]{
                {"type", "type"},
                {"user", "user_name"}
        };
    }

    /**
     * @description:消息记录列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_pushMsgListFilter")
    public static Object[][] pushMsgListFilter_pram(){
        return new String[][]{
                {"customer_type", "customer_type"},
                {"message_type", "message_type"},

        };
    }
    /**
     * @description:v2.0-洗车管理列表-筛选栏
     * @author: gly
     * @time: 2020-2-1
     */
    @DataProvider(name = "SELECT_washCarManagerFilter")
    public static Object[][] washCarManagerListFilter_pram(){
        return new String[][]{
                {"customerName", "customer_name"},
                {"customer_type", "customer_vip_type"},
                {"shop_id", "shop_name"},
                {"phone", "phone"},
//                {"wash_start_time", "wash_car_date"},
//                {"wash_end_time", "wash_car_date"},

        };
    }

    /**
     * @description:v2.0-调整洗车记录-筛选栏
     * @author: gly
     * @time: 2020-2-2
     */
    @DataProvider(name = "SELECT_adjustNumberRecordFilter")
    public static Object[][] adjustNumberRecordFilter_pram(){
        return new String[][]{
                {"customer_name", "customer_name"},
                {"customer_phone", "customer_phone"},
                {"customer_type", "customer_vip_type_name"},
                {"adjust_shop_id", "adjust_shop_name"},
//                {"adjust_start_time", "adjust_start_time"},
//                {"adjust_end_time", "adjust_end_time"},

        };
    }

    /**
     * @description:v2.0-优惠券领取记录-筛选栏
     * @author: gly
     * @time: 2020-2-2
     */
    @DataProvider(name = "SELECT_voucherManageSendRecordFilter")
    public static Object[][] voucherManageSendRecordFilter_pram(){
        return new String[][]{
                {"receiver", "customer_name"},
                {"receive_phone", "customer_phone"},
                {"voucher_name", "voucher_name"},
                {"send_channel", "send_channel_name"},
        };
    }

    /**
     * @description:v2.0-优惠券作废记录-筛选栏
     * @author: gly
     * @time: 2020-2-2
     */
    @DataProvider(name = "SELECT_voucherInvalidPageFilter")
    public static Object[][] voucherInvalidPageFilter_pram(){
        return new String[][]{
                {"receiver", "customer_name"},
                {"receive_phone", "customer_phone"},
                {"invalid_name", "invalid_name"},
                {"invalidPhone", "invalid_phone"},
                {"voucher_name", "voucher_name"}

        };
    }
    /**
     * @description:v2.0-PC道路救援-筛选栏
     * @author: gly
     * @time: 2020-2-2
     */
    @DataProvider(name = "SELECT_rescuePageFilter")
    public static Object[][] rescuePageFilter_pram(){
        return new String[][]{
                {"customer_name", "customer_name"},
                {"vip_type", "vip_type_name"},
                {"customer_phone", "customer_phone"},
                {"shop_id", "shop_name"},
//                {"dial_start", "dial_start"},
//                {"dial_end", "dial_end"},

        };
    }

    /**
     * @description:v2.0-PC评价列表-筛选栏
     * @author: gly
     * @time: 2020-2-2
     */
    @DataProvider(name = "SELECT_evaluatePageFilter")
    public static Object[][] evaluatePageFilter_pram(){
        return new String[][]{
                {"plate_number", "plate_number"},
                {"service_sale_id", "service_sale_name"},
//                {"evaluate_type", "evaluate_type_name"},
                {"shop_id", "shop_id"},
                {"customer_name", "customer_name"},
                {"score", "score"},
                {"is_follow_up", "follow_up_remark"},
                {"customer_phone", "customer_phone"},
                {"is_have_msg", "evaluate_time"},
//                {"source_create_start", "source_create_start"},
//                {"source_create_end", "source_create_end"},
//                {"evaluate_start", "evaluate_start"},
//                {"evaluate_end", "evaluate_end"},



        };
    }

    /**
     * @description:v2.0-PC商城套餐-筛选栏
     * @author: gly
     * @time: 2020-2-2
     */
    @DataProvider(name = "SELECT_storeCommodityPageFilter")
    public static Object[][] storeCommodityPageFilter_pram(){
        return new String[][]{
                {"commodity_name", "commodity_name"},
//                {"start_create_date", "start_create_date"},
//                {"end_create_date", "end_create_date"},

        };
    }

    /**
     * @description:v2.0-PC精品商城-商城订单
     * @author: gly
     * @time: 2020-2-3
     */
    @DataProvider(name = "SELECT_storeOrderPageFilter")
    public static Object[][] storeOrderPageFilter_pram(){
        return new String[][]{
                {"order_number", "order_number"},
                {"commodity_name", "commodity_name"},
                {"bind_phone", "bind_phone"},
//                {"start_pay_time", "start_pay_time"},
//                {"end_pay_time", "end_pay_time"},

        };
    }

    /**
     * @description:v2.0-PC精品商城-分销员管理
     * @author: gly
     * @time: 2020-2-3
     */
    @DataProvider(name = "SELECT_storeSalesPageFilter")
    public static Object[][] storeSalesPageFilter_pram(){
        return new String[][]{
                {"sales_phone", "sales_phone"},
                {"shop_id", "shop_name"},

        };
    }

    /**
     * @description:v2.0-PC活动管理列表
     * @author: gly
     * @time: 2020-3-3
     */
    @DataProvider(name = "SELECT_activityManagePageFilter")
    public static Object[][] activityManagePageFilter_pram(){
        return new String[][]{
                {"creator_account", "creator_account"},
                {"creator_name", "creator_name"},
                {"status", "approval_status"},
                {"subject_type", "subject_type"},

        };
    }

    /**
     * @description:v2.0-PC活动管理-报名列表
     * @author: gly
     * @time: 2020-2-3
     */
    @DataProvider(name = "SELECT_registerPageFilter")
    public static Object[][] registerPageFilter_pram(){
        return new String[][]{
                {"status", "status"},

        };
    }

    /**
     * @description:v2.0-PC卡券管理-增发记录
     * @author: gly
     * @time: 2020-3-16
     */
    @DataProvider(name = "SELECT_voucherManageAdditionalRecordFilter")
    public static Object[][] voucherManageAdditionalRecordFilter_pram(){
        return new String[][]{
                {"voucher_name", "voucher_name"},
                {"sale_name", "operate_sale_name"},
                {"sale_phone", "operate_sale_account"}

        };
    }

    /**
     * @description:v3.1在线专家列表
     * @author: gly
     * @time: 2020-3-26
     */
    @DataProvider(name = "SELECT_onlineExpertsPageListLRecordFilter")
    public static Object[][] onlineExpertsPageListRecordFilter_pram(){
        return new String[][]{
                {"shop_id", "shop_name"},
                {"follow_login_name", "follow_login_name"},
                {"follow_sales_name", "follow_sales_name"},
                {"customer_name", "customer_name"},
                {"customer_phone", "customer_phone"},
                {"is_over_time", "is_over_time"},

        };
    }

    /**
     * @description:v3.1专属服务列表
     * @author: gly
     * @time: 2020-3-26
     */
    @DataProvider(name = "SELECT_dedicatedServicePageListLRecordFilter")
    public static Object[][] dedicatedServicePageListRecordFilter_pram(){
        return new String[][]{
                {"shop_id", "shop_name"},
                {"follow_login_name", "follow_login_name"},
                {"follow_sales_name", "follow_sales_name"},
                {"customer_name", "customer_name"},
                {"customer_phone", "customer_phone"},
                {"is_over_time", "is_over_time"},
                {"car_model_id", "car_model_id"},

        };
    }

    /**
     * @description:v3.1销售客户接待列表
     * @author: gly
     * @time: 2020-3-26
     */
    @DataProvider(name = "SELECT_preSalesReceptionPageRecordFilter")
    public static Object[][] preSalesReceptionPageRecordFilter_pram(){
        return new String[][]{
                {"shop_id", "shop_name"},
                {"customer_name", "customer_name"},
                {"phone", "phone"},
                {"car_style_id", "car_style_id"},
                {"pre_sale_name", "pre_sale_name"},
                {"pre_sale_account", "pre_sale_account"},
        };
    }

    /**
     * @description:v3.1销售客户接待列表
     * @author: gly
     * @time: 2020-3-26
     */
    @DataProvider(name = "SELECT_lossCustomerPageRecordFilter")
    public static Object[][] lossCustomerPageRecordFilter_pram(){
        return new String[][]{
                {"customer_name", "customer_phone"},
                {"customer_name", "customer_name"},
                {"vehicle_chassis_code", "vehicle_chassis_code"},
        };
    }

    /**
     * @description:v3.1销售客户接待列表
     * @author: gly
     * @time: 2020-3-26
     */
    @DataProvider(name = "SELECT_loginLogStaffRecordFilter")
    public static Object[][] loginLogStaffRecordFilter_pram(){
        return new String[][]{
                {"name", "name"},
                {"phone", "phone"},
        };
    }













}
