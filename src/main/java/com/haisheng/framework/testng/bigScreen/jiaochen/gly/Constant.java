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
//                {"finish_end","reception_date"},
//                {"finish_start","reception_date"},
//                {"reception_end","reception_end"},
//                {"reception_start","reception_start"},
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
                {"create_date","create_date"},
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
//                {"create_end_time","create_end_time"},
//                {"create_start_time","create_start_time"},
//                {"order_end_time","order_end_time"},
//                {"order_start_time","order_start_time"}

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
                {"end_time", "end_time"},
                {"start_time","start_time"},
                {"customer_phone","customer_phone"},
        };
    }

    /**
     * @description:预约记录列表-筛选栏
     * @author: gly
     * @time: 2020-11-24
     */
    @DataProvider(name = "SELECT_appointmentRecordFilter")
    public static Object[][] appointmentRecordFilter_pram() {
        return new String[][]{
                {"plate_number", "plate_number"},
                {"customer_manager", "customer_manager"},
                {"shop_id", "shop_id"},
                {"customer_name", "customer_name"},
                {"confirm_status", "appointment_status_name"},
                {"customer_phone", "customer_phone"},
                {"is_overtime", "is_overtime"},
//                {"appointment_end", "appointment_end"},
//                {"appointment_start","appointment_start"},
//                {"confirm_end","confirm_end"}
//                {"confirm_start", "confirm_start"},
//                {"create_end","create_end"},
//                {"create_start","create_start"}
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
                {"creator", "creator"},
                {"is_diff", "is_diff"},
                {"is_self_verification","is_self_verification"},
                {"voucher_status","invalid_status"}

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
                {"start_time", "start_time"},
                {"end_time", "end_time"},
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
//                {"start_time", "send_time"},
//                {"end_time", "end_time"},
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
//                {"start_time", "start_time"},
//                {"end_time", "end_time"},
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
                {"send_type", "send_number"},
                {"sender","package_belongs"}
//                {"end_time", "end_time"},
//                {"start_time", "start_time"},

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
//                {"start_time", "start_time"},
//                {"end_time","end_time"}
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
//                {"end_date","end_date"},
//                {"start_date","start_date"},
//                {"register_end_date","register_end_date"},
//                {"register_start_date","register_start_date"}
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
//                {"start_date", "start_date"},
//                {"end_date", "end_date"},
//                {"register_start_date", "register_start_date"},
//                {"register_end_date", "register_end_date"},
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
//                {"end_time", "end_time"},
//                {"start_time", "start_time"},
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
//                {"import_date", "import_time"}
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
//                {"push_date", "start_time"}
        };
    }

}
