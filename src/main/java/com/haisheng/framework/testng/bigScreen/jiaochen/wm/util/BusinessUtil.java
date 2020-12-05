package com.haisheng.framework.testng.bigScreen.jiaochen.wm.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumContent;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.PushMessage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.CreatePackage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.PackageFormPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shop.ShopAdd;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.Create;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VerificationPeople;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherFormPage;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 业务场景工具
 */
public class BusinessUtil {
    ScenarioUtil jc = ScenarioUtil.getInstance();
    private static final int size = 100;

    public void login(EnumAccount account) {
        if (account.getEnvironment().equals("daily")) {
            jc.pcLogin(account.getPhone(), account.getPassword());
        }
    }

    /**
     * 创建卡券
     *
     * @param stock 创建数量
     */
    public String createVoucher(Long stock) {
        String voucherName = getVoucherName();
        IScene scene = Create.builder().voucherPic(getPicPath()).voucherName(voucherName).subjectType(getSubjectType())
                .voucherDescription(getDesc()).subjectId(getSubjectId(getSubjectType())).stock(stock).cost(getCost(stock))
                .shopType(0).shopIds(getShopIds()).selfVerification(true).build();
        jc.invokeApi(scene);
        return voucherName;
    }

    public String getDesc() {
        return EnumContent.B.getContent();
    }

    /**
     * 获取成本
     *
     * @param stock 卡券数量
     * @return 卡券成本
     */
    public Double getCost(Long stock) {
        if (StringUtils.isEmpty(stock)) {
            return null;
        }
        return stock == 0 ? (double) 10 : (double) 50;
    }

    /**
     * 获取图片
     *
     * @return picPath
     */
    public String getPicPath() {
        String path = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/卡券图.jpg";
        String picture = new ImageUtil().getImageBinary(path);
        return jc.pcFileUpload(picture, false, 1.5).getString("pic_path");
    }

    /**
     * 获取主体类型
     *
     * @return subjectType
     */
    public String getSubjectType() {
        JSONArray array = jc.pcSubjectList().getJSONArray("list");
        return array.getJSONObject(0).getString("subject_key");
    }

    /**
     * 获取主体id
     *
     * @return subjectId
     */
    public Long getSubjectId(String subjectType) {
        if (StringUtils.isEmpty(subjectType)) {
            return null;
        }
        switch (subjectType) {
            case "STORE":
                return getShopIds().get(0);
            case "BRAND":
                return getBrandIds().get(0);
            default:
                return null;
        }
    }

    /**
     * 获取品牌id
     *
     * @return brandIds
     */
    public List<Long> getBrandIds() {
        List<Long> brandIds = new ArrayList<>();
        JSONArray array = jc.pcUserRangeDetail().getJSONArray("list");
        Long brandId = array.getJSONObject(0).getLong("id");
        brandIds.add(brandId);
        return brandIds;
    }

    /**
     * 获取门店id
     *
     * @return shopIds
     */
    public List<Long> getShopIds() {
        List<Long> shopIds = new ArrayList<>();
        JSONArray array = jc.pcShopList().getJSONArray("list");
        Long shopId = array.getJSONObject(0).getLong("shop_id");
        shopIds.add(shopId);
        return shopIds;
    }

    /**
     * 获取卡券名称
     *
     * @return voucherName
     */
    public String getVoucherName() {
        int num = CommonUtil.getRandom(1, 100000);
        String voucherName = "立减" + num + "元代金券";
        VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder().voucherName(voucherName);
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            builder.page(i).size(size);
            JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
            if (array.isEmpty()) {
                return voucherName;
            }
            for (int j = 0; j < array.size(); j++) {
                if (!array.getJSONObject(j).getString("voucher_name").equals(voucherName)) {
                    return voucherName;
                }
            }
        }
        return getVoucherName();
    }

    /**
     * 增发卡券
     *
     * @param voucherName 增发的卡券名
     * @param num         增发数量
     */
    public void addVoucher(String voucherName, Integer num) {
        IScene scene = VoucherFormPage.builder().voucherName(voucherName).build();
        int id = CommonUtil.getIntField(jc.invokeApi(scene), 0, "id");
        //增发
        jc.pcAddVoucher((long) id, num);
    }

    /**
     * 作废卡券
     *
     * @param voucherName 被作废卡券的卡券名
     */
    public void invalidVoucher(String voucherName) {
        //获取创建的卡券id
        IScene scene = VoucherFormPage.builder().voucherName(voucherName).build();
        JSONObject response = jc.invokeApi(scene);
        Integer id = CommonUtil.getIntField(response, 0, "id");
        //作废
        jc.pcInvalidVoucher((long) id);
    }

    /**
     * 获取重复的核销人员
     *
     * @return 电话号
     */
    public String getRepetitionVerificationPhone() {
        IScene scene = VerificationPeople.builder().build();
        return CommonUtil.getStrField(jc.invokeApi(scene), 0, "verification_phone");
    }

    /**
     * 创建套餐
     *
     * @return 套餐名
     */
    public String createPackage() {
        String packageName = getPackageName();
        IScene scene = CreatePackage.builder().packageName(getPackageName()).validity("30").packageDescription(getDesc())
                .subjectType(getSubjectType()).subjectId(getSubjectId(getSubjectType())).voucherList(getVoucherList())
                .packagePrice(5000.00).status(true).shopIds(getShopIds()).build();
        jc.invokeApi(scene);
        return packageName;
    }

    /**
     * 获取套餐名
     *
     * @return 套餐名
     */
    public String getPackageName() {
        int num = CommonUtil.getRandom(1, 10000);
        String packageName = "立减套餐" + num;
        PackageFormPage.PackageFormPageBuilder builder = PackageFormPage.builder().packageName(packageName);
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            builder.page(i).size(size);
            JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
            if (array.isEmpty()) {
                return packageName;
            }
            for (int j = 0; j < array.size(); j++) {
                if (!array.getJSONObject(j).getString("package_name").equals(packageName)) {
                    return packageName;
                }
            }
        }
        return getPackageName();
    }

    /**
     * 获取卡券集合
     *
     * @return 卡券集合
     */
    public JSONArray getVoucherList() {
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        JSONArray list = jc.pcVoucherList().getJSONArray("list");
        if (list.isEmpty()) {
            //创建卡券
            String voucherName = createVoucher(1L);
            //审核通过
            applyVoucher(voucherName, "1");

        }
        long id = jc.pcVoucherList().getJSONArray("list").getJSONObject(0).getLong("id");
        object.put("voucher_id", id);
        object.put("voucher_count", 10);
        array.add(object);
        return array;
    }


    /**
     * 卡券审批
     *
     * @param voucherName 卡券名称
     * @param status      通过 1/拒绝2
     */
    public void applyVoucher(String voucherName, String status) {
        ApplyPage.ApplyPageBuilder builder = ApplyPage.builder().name(voucherName);
        int total = jc.invokeApi(builder.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        int id = 0;
        for (int i = 1; i < s; i++) {
            builder.page(i).size(size);
            JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
            for (int j = 0; j < array.size(); j++) {
                if (array.getJSONObject(j).getString("status_name").equals("审核中")) {
                    id = array.getJSONObject(j).getInteger("id");
                    break;
                }
            }
        }
        //审批
        jc.pcApplyApproval((long) id, status);
    }

    /**
     * 发消息
     */
    public void pushMessage() {
        String beginDate = DateTimeUtil.getFormat(new Date());
        String endDate = DateTimeUtil.addDayFormat(new Date(), 10);
        List<String> telList = new ArrayList<>();
        telList.add("");
        List<Long> voucherList = new ArrayList<>();
        voucherList.add(1L);
        IScene scene = PushMessage.builder().pushTarget("SHOP_CUSTOMER").telList(telList).messageName("测试消息")
                .messageContent("bababab").type(0).voucherOrPackageList(voucherList)
                .beginUseTime(beginDate).endUseTime(endDate).sendTime(30L).build();
        jc.invokeApi(scene);
    }

    /**
     * 添加活动
     */
    public void articleAdd() {

    }


    /**
     * 添加门店
     */
    public void shopAdd() {
        List<Long> list = new ArrayList<>();
        IScene scene = ShopAdd.builder().avatarPath("").simpleName("").name("").brandList(list).districtCode("")
                .address("").saleTel("").serviceTel("").longitude(2.2).latitude(2.2).appointmentStatus("ENABLE")
                .washingStatus("ENABLE").build();
        jc.invokeApi(scene);
    }
}
