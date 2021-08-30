package com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.gly;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator.CameraStatusEnum;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.equipmentmanagement.auth.AllDeviceListScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.equipmentmanagement.device.DevicePageScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.shop.collection.ShopDeviceListScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class VideoSurveillanceCase extends TestCaseCommon implements TestCaseStd {
    private final EnumTestProduct product = EnumTestProduct.XD_DAILY;
    public VisitorProxy visitor = new VisitorProxy(product);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);
    StoreScenarioUtil su = StoreScenarioUtil.getInstance();
    public Long shopId = 28758L;
    public String shopName = "巡店测试门店1";
    CommonConfig commonConfig = new CommonConfig();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_XUNDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.GLY.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(product.getShopId()).setReferer(product.getReferer()).setRoleId(product.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
//        user.loginPc(AccountEnum.YUE_XIU_DAILY);
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        su.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
    }


    /**
     * [视频监控]摄像头数量=【设备管理】所有状态设备和--ok
     */
    @Test(description = "[视频监控]摄像头数量=【设备管理】所有状态设备和")
    public void videoSurveillanceData1() {
        try {
            int num = 0;
            //获取视频监控中门店的列表
            IScene scene = AllDeviceListScene.builder().build();
            JSONObject response = scene.visitor(visitor).execute().getJSONObject("total_status");
            //获取视频监控中的摄像头总数量
            String[] deviceArray = response.getString("device").split("/");
            int deviceSum = Integer.parseInt(deviceArray[1]);
            //设备管理中的列表条数
            IScene scene1 = DevicePageScene.builder().page(1).size(10).type("CAMERA").build();
            JSONObject response1 = scene1.visitor(visitor).execute();
            //摄像头的总数量
//           int cameraSum=response1.getInteger("total");
            //计算type为【WEB_CAMERA】的条数
            int pages = response1.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = DevicePageScene.builder().page(page).size(10).type("CAMERA").build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String type = list.getJSONObject(i).getString("type");
                    if (type.equals("WEB_CAMERA") || type.equals("UNIVIEW_PLATFORM") || type.equals("AI_CAMERA")) {
                        num++;
                    }
                }
            }
            System.out.println("视频监控中的摄像头总数量:" + deviceSum + "  设备管理中的摄像头的总数量:" + num);
            Preconditions.checkArgument(deviceSum == num, "视频监控中的摄像头总数量:" + deviceSum + "  设备管理中的摄像头的总数量:" + num);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("[视频监控]摄像头数量=【设备管理】所有状态设备和");
        }
    }

    /**
     * 门店摄像头运行中数量之和=门店列表下方的摄像头展示运行中设备数--ok
     */
    @Test(description = "门店摄像头运行中数量之和=门店列表下方的摄像头展示运行中设备数")
    public void videoSurveillanceData2() {
        try {
            int num = 0;
            //获取视频监控中门店的列表
            IScene scene = AllDeviceListScene.builder().build();
            JSONObject response = scene.visitor(visitor).execute();
            //获取视频监控中的运行中的摄像头数量
            String[] deviceArray = response.getJSONObject("total_status").getString("device").split("/");
            int deviceSum = Integer.parseInt(deviceArray[0]);
            //遍历列表的在线摄像头的数量
            JSONArray list = response.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String[] deviceStatus = list.getJSONObject(i).getString("device_status").split("/");
                num += Integer.parseInt(deviceStatus[0]);
            }
            System.out.println("视频监控右下角展示的运行中数量为：" + deviceSum + "  遍历列表中所有的运行中的数量为：" + num);
            Preconditions.checkArgument(deviceSum == num, "视频监控右下角展示的运行中数量为：" + deviceSum + "  遍历列表中所有的运行中的数量为：" + num);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店摄像头运行中数量之和=门店列表下方的摄像头展示运行中设备数");
        }
    }

    /**
     * 门店列表摄像头数量之和=门店列表下方的摄像头展示设备总数--ok
     */
    @Test(description = "门店列表摄像头数量之和=门店列表下方的摄像头展示设备总数")
    public void videoSurveillanceData3() {
        try {
            int num = 0;
            //获取视频监控中门店的列表
            IScene scene = AllDeviceListScene.builder().build();
            JSONObject response = scene.visitor(visitor).execute();
            //获取视频监控中的运行中的摄像头数量
            String[] deviceArray = response.getJSONObject("total_status").getString("device").split("/");
            int deviceSum = Integer.parseInt(deviceArray[1]);
            //遍历列表的在线摄像头的数量
            JSONArray list = response.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String[] deviceStatus = list.getJSONObject(i).getString("device_status").split("/");
                num += Integer.parseInt(deviceStatus[1]);
            }
            System.out.println("视频监控右下角展示的运行中数量为：" + deviceSum + "  遍历列表中所有的运行中的数量为：" + num);
            Preconditions.checkArgument(deviceSum == num, "视频监控右下角展示的摄像头的总数量为：" + deviceSum + "  遍历列表中所有中的数摄像头总数为：" + num);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店列表摄像头数量之和=门店列表下方的摄像头展示设备总数");
        }
    }

    /**
     * 门店摄像头不可用数量=【设备管理】此门店设备【已停止】【部署中】【掉线】【视频流错误】【未部署】数量之和--ok
     */
    @Test(description = "门店摄像头不可用数量=【设备管理】此门店设备【已停止】【部署中】【掉线】【视频流错误】【未部署】数量之和")
    public void videoSurveillanceData4() {
        try {
            //获取视频监控中门店的列表
            IScene scene = AllDeviceListScene.builder().build();
            JSONObject response = scene.visitor(visitor).execute();
            //获取视频监控中的运行中的摄像头数量
            String[] deviceArray = response.getJSONObject("total_status").getString("device").split("/");
            int deviceSum = Integer.parseInt(deviceArray[1]);
            int deviceIng = Integer.parseInt(deviceArray[0]);
            //不可运行的数量
            int notRunNum = deviceSum - deviceIng;

            //【设备管理中】视频流错误的设备数量
            int streamErrorNum = DevicePageScene.builder().page(1).size(10).type("CAMERA").deviceStatus(CameraStatusEnum.STREAM_ERROR.getDeviceStatus()).build().visitor(visitor).execute().getInteger("total");
            //【设备管理中】掉线的设备数量
            int offLineNum = DevicePageScene.builder().page(1).size(10).type("CAMERA").deviceStatus(CameraStatusEnum.OFFLINE.getDeviceStatus()).build().visitor(visitor).execute().getInteger("total");
            //【设备管理中】已停止的设备数量
            int stoppedNum = DevicePageScene.builder().page(1).size(10).type("CAMERA").deviceStatus(CameraStatusEnum.STOPPED.getDeviceStatus()).build().visitor(visitor).execute().getInteger("total");
            //【设备管理中】部署中的设备数量
            int deploymentNum = DevicePageScene.builder().page(1).size(10).type("CAMERA").deviceStatus(CameraStatusEnum.DEPLOYMENT_ING.getDeviceStatus()).build().visitor(visitor).execute().getInteger("total");
            //【设备管理中】未部署的设备数量
            int unDeploymentNum = 0;
            //设备管理中的筛选符合条件的未部署的设备数量
            IScene scene1 = DevicePageScene.builder().page(1).size(10).type("CAMERA").deviceStatus(CameraStatusEnum.UN_DEPLOYMENT.getDeviceStatus()).build();
            JSONObject response1 = scene1.visitor(visitor).execute();
            int pages = response1.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = DevicePageScene.builder().page(page).size(10).type("CAMERA").deviceStatus(CameraStatusEnum.UN_DEPLOYMENT.getDeviceStatus()).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String type = list.getJSONObject(i).getString("type");
                    if (type.equals("WEB_CAMERA") || type.equals("UNIVIEW_PLATFORM") || type.equals("AI_CAMERA")) {
                        unDeploymentNum++;
                    }
                }
            }

            //【设备管理】中不可用设备的总数
            int notRunNum2 = streamErrorNum + offLineNum + stoppedNum + deploymentNum + unDeploymentNum;
            Preconditions.checkArgument(notRunNum2 == notRunNum, "设备管理中不可用的数量为：" + notRunNum2 + " 视频监控中的不可用的数量为：" + notRunNum);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店摄像头不可用数量=【设备管理】此门店设备【已停止】【部署中】【掉线】【视频流错误】【未部署】数量之和");
        }
    }

    /**
     * 摄像头总设备数=不可用数量+运行中设备数--ok
     */
    @Test(description = "摄像头总设备数=不可用数量+运行中设备数")
    public void videoSurveillanceData5() {
        try {
            int num = 0;
            //获取视频监控中门店的列表
            IScene scene = AllDeviceListScene.builder().build();
            JSONObject response = scene.visitor(visitor).execute();
            //获取视频监控中的运行中的摄像头数量
            String[] deviceArray = response.getJSONObject("total_status").getString("device").split("/");
            //视频监控中设备数量的总数
            int deviceSum = Integer.parseInt(deviceArray[1]);
            //运行中设备的总数
            int runningSum = Integer.parseInt(deviceArray[0]);

            //获取不可用门店的列表,计算不可用的设备的数量
            IScene scene1 = AllDeviceListScene.builder().available(0).build();
            JSONObject response1 = scene1.visitor(visitor).execute();
            JSONArray list = response1.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONArray deviceList = list.getJSONObject(i).getJSONArray("device_list");
                num += deviceList.size();
            }
            Preconditions.checkArgument(deviceSum == runningSum + num, "视频监控的设备上的总数是：" + deviceSum + "  视频监控中的运行中的数量为：" + runningSum + "  设备管理中的不可运行数：" + num);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("摄像头总设备数=不可用数量+运行中设备数");
        }
    }

    /**
     * 单个门店在线摄像头<=单个门店总摄像数--ok
     */
    @Test(description = "单个门店在线摄像头<=单个门店总摄像数")
    public void videoSurveillanceData6() {
        try {
            //获取视频监控中门店的列表
            IScene scene = AllDeviceListScene.builder().build();
            JSONObject response = scene.visitor(visitor).execute();
            //遍历列表的在线摄像头的数量
            JSONArray list = response.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String[] deviceStatus = list.getJSONObject(i).getString("device_status").split("/");
                //视频监控中设备数量的总数
                int deviceSum = Integer.parseInt(deviceStatus[1]);
                //运行中设备的总数
                int runningSum = Integer.parseInt(deviceStatus[0]);
                Preconditions.checkArgument(deviceSum >= runningSum, "视频监控的设备上的总数是：" + deviceSum + "  视频监控中的运行中的数量为：" + runningSum);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("单个门店在线摄像头<=单个门店总摄像数");
        }
    }

    /**
     * 【全部】设备=运行中设备+不可用设备之和--ok
     */
    @Test(description = "【全部】设备=运行中设备+不可用设备之和")
    public void videoSurveillanceData7() {
        try {
            int allNum = 0;
            int runningNum = 0;
            int notRunNum = 0;
            //获取门店的列表,计算不可用的设备的数量
            IScene scene1 = AllDeviceListScene.builder().available(0).build();
            JSONObject response1 = scene1.visitor(visitor).execute();
            JSONArray list1 = response1.getJSONArray("list");
            for (int i = 0; i < list1.size(); i++) {
                JSONArray deviceList = list1.getJSONObject(i).getJSONArray("device_list");
                notRunNum += deviceList.size();
            }
            //获取门店的列表,计算总设备的数量
            IScene scene = AllDeviceListScene.builder().build();
            JSONObject response = scene.visitor(visitor).execute();
            JSONArray list = response.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONArray deviceList = list.getJSONObject(i).getJSONArray("device_list");
                allNum += deviceList.size();
            }
            //获取门店的列表,计算总设备的数量
            IScene scene2 = AllDeviceListScene.builder().available(1).build();
            JSONObject response2 = scene2.visitor(visitor).execute();
            JSONArray list2 = response2.getJSONArray("list");
            for (int i = 0; i < list2.size(); i++) {
                JSONArray deviceList = list2.getJSONObject(i).getJSONArray("device_list");
                runningNum += deviceList.size();
            }
            System.out.println("视频监控的设备上的总数是：" + allNum + "  视频监控中的运行中的数量为：" + runningNum + "  设备管理中的不可运行数：" + notRunNum);
            Preconditions.checkArgument(allNum == runningNum + notRunNum, "视频监控的设备上的总数是：" + allNum + "  视频监控中的运行中的数量为：" + runningNum + "  设备管理中的不可运行数：" + notRunNum);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【全部】设备=运行中设备+不可用设备之和");
        }
    }

    /**
     * 门店总数=运行中门店+不可用的门店--ok
     */
    @Test(description = "门店总数=运行中门店+不可用的门店")
    public void videoSurveillanceData8() {
        try {
            int allNum = 0;
            int runningNum = 0;
            int notRunNum = 0;
            //获取门店的列表,计算总设备的数量
            IScene scene2 = AllDeviceListScene.builder().build();
            JSONObject response2 = scene2.visitor(visitor).execute();
            JSONArray list2 = response2.getJSONArray("list");
            allNum = list2.size();

            //获取设备不在线和在线的的门店数量
            IScene scene1 = AllDeviceListScene.builder().build();
            JSONObject response1 = scene1.visitor(visitor).execute();
            JSONArray list1 = response1.getJSONArray("list");
            for (int i = 0; i < list1.size(); i++) {
                String[] deviceStatus = list1.getJSONObject(i).getString("device_status").split("/");
                int num = Integer.parseInt(deviceStatus[0]);
                if (num == 0) {
                    notRunNum++;
                } else {
                    runningNum++;
                }
            }
            Preconditions.checkArgument(allNum == runningNum + notRunNum, "视频监控的门店上的总数是：" + allNum + "  视频监控中设备在线的门店为：" + runningNum + "  设备管理中的设备不可运行的门店数：" + notRunNum);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店总数=运行中门店+不可用的门店");
        }
    }

    /**
     * 收藏的门店数量<=全部门店的数量--ok
     */
    @Test(description = "收藏的门店数量<=全部门店的数量")
    public void videoSurveillanceData9() {
        try {
            int allNum = 0;
            int collectionNum = 0;
            //获取门店的列表,计算总设备的数量
            IScene scene2 = AllDeviceListScene.builder().build();
            JSONObject response2 = scene2.visitor(visitor).execute();
            JSONArray list2 = response2.getJSONArray("list");
            allNum = list2.size();

            //获取设备不在线和在线的的门店数量
            IScene scene1 = ShopDeviceListScene.builder().build();
            JSONObject response1 = scene1.visitor(visitor).execute();
            JSONArray list1 = response1.getJSONArray("list");
            collectionNum = list1.size();
            Preconditions.checkArgument(allNum >= collectionNum, "视频监控的设备上的总数是：" + allNum + "  视频监控中的收藏门店的数量为：" + collectionNum);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("收藏的门店数量<=全部门店的数量");
        }
    }

    /**
     * 门店设备总数=门店设备不可用数+设备运行中数量--ok
     */
    @Test(description = "门店设备总数=门店设备不可用数+设备运行中数量")
    public void videoSurveillanceData10() {
        try {
            int allNum = 0;
            int runningNum = 0;
            int notRunNum = 0;
            //获取门店的列表,计算总设备的数量
            IScene scene = AllDeviceListScene.builder().build();
            JSONObject response = scene.visitor(visitor).execute();
            JSONArray list = response.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONArray deviceList = list.getJSONObject(i).getJSONArray("device_list");
                allNum += deviceList.size();
                for (int j = 0; j < deviceList.size(); j++) {
                    String deviceStatus = deviceList.getJSONObject(j).getString("device_status");
                    if (deviceStatus.equals("RUNNING")) {
                        runningNum++;
                    } else {
                        notRunNum++;
                    }
                }
                System.out.println("视频监控的设备上的总数是：" + allNum + "  视频监控中的运行中的数量为：" + runningNum + "  设备管理中的不可运行数：" + notRunNum);
                Preconditions.checkArgument(allNum == runningNum + notRunNum, "视频监控的设备上的总数是：" + allNum + "  视频监控中的运行中的数量为：" + runningNum + "  设备管理中的不可运行数：" + notRunNum);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店设备总数=门店设备不可用数+设备运行中数量");
        }
    }


}
