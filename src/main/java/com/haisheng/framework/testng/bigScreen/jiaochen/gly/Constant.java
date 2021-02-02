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
                {"reception_sale_name", "reception_sale_name"},
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
     * @description:售后管理列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_AfterSleCustomerManageFilter")
    public static Object[][] afterSleCustomerManage_pram() {
        return new String[][]{
                {"vehicle_chassis_code", "vehicle_chassis_code"},
                {"customer_name","repair_customer_name"},
                {"customer_phone","repair_customer_phone"},

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
                {"vip_type","vip_type"}
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
                {"service_sale_id", "service_sale_id"},
                {"shop_id", "shop_id"},
                {"customer_name", "customer_name"},
                {"confirm_status", "appointment_status_name"},
                {"customer_phone", "customer_phone"},
                {"is_overtime", "is_overtime"},
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
                {"voucher_type","voucher_type"}

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
                {"sender", "sender"},

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
                {"package_status","status"}
//                {"shop_name", "shop_name"},
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
                {"sender","package_belongs"}

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
//                {"apply_group", "apply_group"},

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
    @DataProvider(name = "SELECT_satffListFilter")
    public static Object[][] satffListFilter_pram(){
        return new String[][]{
                {"role_name", "name"},
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
                {"export_time", "export_time"},
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
                {"customer_name", "customer_name"},
                {"customer_vip_type", "customer_vip_type"},
//                {"wash_start_time", "wash_car_date"},
//                {"wash_end_time", "wash_car_date"},
                {"shop_id", "shop_name"},
                {"phone", "phone"},

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
//                {"adjust_start_time", "adjust_start_time"},
//                {"adjust_end_time", "adjust_end_time"},
                {"adjust_shop_id", "adjust_shop_id"},
                {"customer_type", "customer_type"},

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
                {"receiver", "receiver"},
                {"receive_phone", "receive_phone"},
                {"use_status", "use_status"},
//                {"start_time", "start_time"},
//                {"end_time", "end_time"},
//                {"use_start_time", "use_start_time"},
//                {"use_end_time", "use_end_time"},
                {"customer_label", "customer_label"},
                {"id","id"},


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
                {"receiver", "receiver"},
                {"receive_phone", "receive_phone"},
//                {"start_time", "start_time"},
//                {"end_time", "end_time"},
                {"invalid_name", "invalid_name"},
                {"invalid_phone", "invalid_phone"},
//                {"invalid_start_time", "invalid_start_time"},
//                {"invalid_end_time", "invalid_end_time"},
                {"id","id"},


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
                {"vip_type", "vip_type"},
                {"customer_phone", "customer_phone"},
                {"shop_id", "shop_id"},
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
                {"service_sale_id", "service_sale_id"},
                {"evaluate_type", "evaluate_type"},
                {"shop_id", "shop_id"},
                {"customer_name", "customer_name"},
                {"score", "score"},
                {"is_follow_up", "is_follow_up"},
                {"customer_phone", "customer_phone"},
                {"is_have_msg", "is_have_msg"},
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
                {"start_create_date", "start_create_date"},
                {"end_create_date", "end_create_date"},

        };
    }










}
