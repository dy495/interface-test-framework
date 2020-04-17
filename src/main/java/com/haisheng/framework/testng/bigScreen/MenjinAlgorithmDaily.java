
package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.codec.binary.Base64;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.QADbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author : lvxueqing
 * @date :  2020/04/13  10:26
 */

public class MenjinAlgorithmDaily {


    Menjin menjin = new Menjin();
    //    ----------------------------------------------变量定义--------------------------------------------------------------------
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String failReason = "";

    private String response = "";

    private boolean FAIL = false;

    private Case aCase = new Case();

    CheckUtil checkUtil = new CheckUtil();

    private QADbUtil qaDbUtil = new QADbUtil();

    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;

    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");


    private void checkResult(String result, String... checkColumnNames) {
        logger.info("result = {}", result);
        JSONObject res = JSONObject.parseObject(result);
        if (!res.getInteger("code").equals(1000)) {
            throw new RuntimeException("result code is " + res.getInteger("code") + " not success code");
        }
        for (String checkColumn : checkColumnNames) {
            Object column = res.getJSONObject("data").get(checkColumn);
            logger.info("{} : {}", checkColumn, column);
            if (column == null) {
                throw new RuntimeException("result does not contains column " + checkColumn);
            }
        }
    }




    private void setBasicParaToDB(Case aCase, String ciCaseName, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }


    //@AfterClass //还没改
    public void clean() {
        qaDbUtil.closeConnection();
        dingPushFinal();
    }

    //@BeforeMethod //还没改
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }

//--------------------------------绑定组关系，设备id不存在 就失败-------------------------------

    /**
     * 绑定设备组关系-存在的设备id，不存在的组名称，第一次绑定
     */
    @Test
    public void bindGroupExistIDNotName() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：绑定设备组关系-存在的设备id，不存在的组名称，第一次绑定\n";

        String key = "";

        try {
            String deviceID = "7376096262751232";
            String groupName = "group" + System.currentTimeMillis();
            JSONObject single = menjin.bindGroup(deviceID,groupName);
            int code = single.getInteger("code");
            Preconditions.checkArgument(code==1000,"状态码不正确，期待1000，实际"+code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 绑定设备组关系-存在的设备id，不存在的组名称，重复绑定
     */
    @Test
    public void bindGroupExistIDName2() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：绑定设备组关系-存在的设备id，存在的组名称，重复绑定\n";

        String key = "";

        try {
            String deviceID = "7362126091322368";
            String groupName = "group" + System.currentTimeMillis();
            //第一次
            menjin.bindGroup(deviceID,groupName);
            //重复
            JSONObject single = menjin.bindGroup(deviceID,groupName);
            int code = single.getInteger("code");
            Preconditions.checkArgument(code==1000,"状态码不正确，期待1000，实际"+code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 绑定设备组关系-存在的设备id，存在的组名称
     * 后续要改：先创建设备，再绑在固定的组名上
     */
    @Test
    public void bindGroupExistIDandName() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：绑定设备组关系-存在的设备id，存在的组名称\n";

        String key = "";

        try {
            String deviceID = "7362126091322368";
            String groupName = "";//存在的组名称
            JSONObject single = menjin.bindGroup(deviceID,groupName);
            int code = single.getInteger("code");
            Preconditions.checkArgument(code==1000,"状态码不正确，期待1000，实际"+code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 绑定设备组关系-不存在的设备id，不存在的组名称 应失败
     */
    @Test
    public void bindGroupNotIDNotName() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：绑定设备组关系- 不存在的设备id，不存在的组名称\n";

        String key = "";

        try {
            String deviceID = "notexistdevice";
            String groupName = "group" + System.currentTimeMillis();
            JSONObject single = menjin.bindGroup(deviceID,groupName);
            int code = single.getInteger("code");
            Preconditions.checkArgument(code==1,"应失败，状态码"+code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     * 绑定设备组关系-不存在的设备id，存在的组名称 应失败
     *
     */
    @Test
    public void bindGroupNotEIDExistName() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：绑定设备组关系- 不存在的设备id，不存在的组名称\n";

        String key = "";

        try {
            String deviceID = "7362126091322368";
            String notexistdeviceID = "notexistdevice";
            String groupName = "groupname1";
            //先绑定一下，保证组名存在
            menjin.bindGroup(deviceID,groupName);
            //不存在的设备id绑定存在的组名
            JSONObject single = menjin.bindGroup(notexistdeviceID,groupName);
            int code = single.getInteger("code");
            Preconditions.checkArgument(code==1,"应失败，状态码"+code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    // -----------解除设备组关系------

    /**
     * 解除设备组关系-未绑定过-不存在的设备id，不存在的组名称 应失败
     */
    @Test
    public void unbindGroupNotIDNotName() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：解除设备组关系-未绑定过-不存在的设备id，不存在的组名称\n";

        String key = "";

        try {
            String deviceID = "notexistdevice";
            String groupName = "group" + System.currentTimeMillis();
            JSONObject single = menjin.unbindGroup(deviceID,groupName);
            int code = single.getInteger("code");
            Preconditions.checkArgument(code==1,"期待失败"+code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 解除设备组关系-未绑定过-不存在的设备id，存在的组名称 应失败
     */
    @Test
    public void unbindGroupNotIDExistName() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：解除设备组关系-未绑定过-不存在的设备id，存在的组名称\n";

        String key = "";

        try {
            String deviceID = "notexistdevice"+System.currentTimeMillis();
            String groupName = ""; //存在的组名称
            JSONObject single = menjin.unbindGroup(deviceID,groupName);
            int code = single.getInteger("code");
            Preconditions.checkArgument(code==1,"期待失败"+code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 解除设备组关系-未绑定过-存在的设备id，不存在的组名称
     */
    @Test
    public void unbindGroupExistIDNotName() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：解除设备组关系-未绑定过-存在的设备id，不存在的组名称\n";

        String key = "";

        try {
            String deviceID = "7362126091322368";
            String groupName = "group" + System.currentTimeMillis();
            JSONObject single = menjin.unbindGroup(deviceID,groupName);
            int code = single.getInteger("code");
            Preconditions.checkArgument(code==1,"期待失败"+code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 解除设备组关系-未绑定过-存在的设备id，存在的组名称
     */
    @Test
    public void unbindGroupExistIDExistName() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：解除设备组关系-未绑定过-存在的设备id，存在的组名称\n";

        String key = "";

        try {
            String deviceID = "7362126091322368";
            String groupName = ""; //存在的组名称
            JSONObject single = menjin.unbindGroup(deviceID,groupName);
            int code = single.getInteger("code");
            Preconditions.checkArgument(code==1,"期待失败"+code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 解除设备组关系-绑定过-存在的设备id，不存在的组名称
     */
    @Test
    public void unbindGroupExistBind() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：解除存在的设备组关系\n";

        String key = "";

        try {
            String deviceID = "7362126091322368";
            String groupName = "group" + System.currentTimeMillis();
            //先新建组关系
            menjin.bindGroup(deviceID,groupName);
            //解绑
            JSONObject single = menjin.unbindGroup(deviceID,groupName);
            int code = single.getInteger("code");
            Preconditions.checkArgument(code==1000,"解除失败，期待1000，实际"+code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    //-------------人脸库添加---------
    /**
     * 人脸库添加-不存在的用户id，不存在的组名称，图片地址可访问 应失败
     */
    @Test
    public void stockAddNotExistid1() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库添加-不存在的用户id，不存在的组名称，图片地址可访问\n";

        String key = "";

        try {
            String groupName = "group" + System.currentTimeMillis();
            String userID = "user" + System.currentTimeMillis();
            String picUrl = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl);
            int code = single.getInteger("code");
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(code==1,"应失败，状态码"+code);
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }
    /**
     * 人脸库添加-不存在的用户id，不存在的组名称，图片地址不可访问 应失败
     */
    @Test
    public void stockAddNotExistid2() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库添加-不存在的用户id，不存在的组名称，图片地址不可访问\n";

        String key = "";

        try {
            String groupName = "group" + System.currentTimeMillis();
            String userID = "user" + System.currentTimeMillis();
            String picUrl = "wertyuio2345678";
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl);
            int code = single.getInteger("code");
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(code==1,"应失败，状态码"+code);
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 人脸库添加-不存在的用户id，存在的组名称，图片地址可访问 应失败
     */
    @Test
    public void stockAddNotExistid3() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库添加-不存在的用户id，存在的组名称，图片地址可访问\n";

        String key = "";

        try {
            String groupName = "";//存在的组名称

            String userID2 = "user2" + System.currentTimeMillis();
            String picUrl2 = menjin.lxq;
            JSONObject single = menjin.stockAdd(groupName,userID2,picUrl2);
            int code = single.getInteger("code");
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(code==1,"应失败，状态码"+code);
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 人脸库添加-不存在的用户id，存在的组名称，图片地址不可访问 应失败
     */
    @Test
    public void stockAddNotExistid4() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库添加-不存在的用户id，存在的组名称，图片地址不可访问\n";

        String key = "";

        try {
            String groupName = ""; //存在的组名称

            String userID2 = "user2" + System.currentTimeMillis();
            String picUrl2 = "qwertyui2345678";
            JSONObject single = menjin.stockAdd(groupName,userID2,picUrl2);
            int code = single.getInteger("code");
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(code==1,"应失败，状态码"+code);
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     * 人脸库添加-存在的用户id，不存在的组名称，图片地址可访问
     */
    @Test
    public void stockAddExistid1() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库添加-存在的用户id，不存在的组名称，图片地址可访问\n";

        String key = "";

        try {
            String groupName = "group" + System.currentTimeMillis();
            String userID = "";//存在的用户id
            String picUrl = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl);
            int code = single.getInteger("code");
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(code==1000,"添加失败，状态码"+code);
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 人脸库添加-存在的用户id，不存在的组名称，图片地址不可访问
     */
    @Test
    public void stockAddExistid2() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库添加-存在的用户id，不存在的组名称，图片地址不可访问\n";

        String key = "";

        try {
            String groupName = "group" + System.currentTimeMillis();
            String userID = ""; //存在的用户id
            String picUrl = "wertyuio2345678";
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl);
            int code = single.getInteger("code");
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(code==1,"应失败，状态码"+code);
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 人脸库添加-存在的用户id，存在的组名称，图片地址可访问
     */
    @Test
    public void stockAddExistid3() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库添加-存在的用户id，存在的组名称，图片地址可访问\n";

        String key = "";

        try {
            String groupName = "" ;//存在的组名称
            String userID = ""; //存在的用户id
            String picUrl = menjin.lxq;
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl);
            int code = single.getInteger("code");
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(code==1000,"添加失败，状态码"+code);
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 人脸库添加-存在的用户id，存在的组名称，图片地址不可访问
     */
    @Test
    public void stockAddExistid4() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库添加-存在的用户id，存在的组名称，图片地址不可访问\n";

        String key = "";

        try {
            String groupName = ""; //存在的组名称
            String userID = ""; //存在的用户id

            String picUrl = "qwertyui2345678";
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl);
            int code = single.getInteger("code");
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(code==1,"应失败，状态码"+code);
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     * 相同组名称，相同的已存在用户id，相同URL
     */
    @Test
    public void stockaddre1() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库重复添加-相同用户id，相同组名称，相同图片地址\n";

        String key = "";

        try {
            String groupName = "group" + System.currentTimeMillis();
            String userID = ""; //存在的用户id
            String picUrl = menjin.lxq;
            //第一次
            menjin.stockAdd(groupName,userID,picUrl);
            //重复
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl);
            int code = single.getInteger("code");
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(code==1000,"重复添加失败，状态码"+code);
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 相同组名称，相同的已存在用户id，不同URL
     */
    @Test
    public void stockaddre2() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库重复添加-相同用户id，相同组名称，不同图片地址\n";

        String key = "";

        try {
            String groupName = "group" + System.currentTimeMillis();
            String userID = ""; //存在的用户id
            String picUrl = menjin.lxq;
            //第一次
            menjin.stockAdd(groupName,userID,picUrl);
            //重复
            String picUrl2 = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl2);
            int code = single.getInteger("code");
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(code==1000,"重复添加失败，状态码"+code);
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 不同组名称，相同的已存在用户id，相同URL
     */
    @Test
    public void stockaddre3() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库重复添加-相同用户id，相同组名称，相同图片地址\n";

        String key = "";

        try {
            String groupName1 = "group" + System.currentTimeMillis();
            String userID = ""; //存在的用户id
            String picUrl = menjin.lxq;
            //第一次
            menjin.stockAdd(groupName1,userID,picUrl);
            //重复
            String groupName2 = "group" + System.currentTimeMillis();
            JSONObject single = menjin.stockAdd(groupName2,userID,picUrl);
            int code = single.getInteger("code");
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(code==1000,"重复添加失败，状态码"+code);
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 不同组名称，相同的已存在用户id，不同URL
     */
    @Test
    public void stockaddre4() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库重复添加-相同用户id，相同组名称，相同图片地址\n";

        String key = "";

        try {
            String groupName = "group" + System.currentTimeMillis();
            String userID = ""; //存在的用户id
            String picUrl = menjin.lxq;
            //第一次
            menjin.stockAdd(groupName,userID,picUrl);
            //重复
            String groupName1 = "group" + System.currentTimeMillis();
            String picUrl1 = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName1,userID,picUrl1);
            int code = single.getInteger("code");
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(code==1000,"重复添加失败，状态码"+code);
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     * 相同组名称，不同的已存在用户id，相同URL
     */
    @Test
    public void stockaddre5() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库重复添加-不同用户id，相同组名称，相同图片地址\n";

        String key = "";

        try {
            String groupName = "group" + System.currentTimeMillis();
            String userID = ""; //存在的用户id
            String userID1 = ""; //存在的用户id

            String picUrl = menjin.lxq;
            //第一次
            menjin.stockAdd(groupName,userID1,picUrl);
            //重复
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl);
            int code = single.getInteger("code");
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(code==1000,"重复添加失败，状态码"+code);
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 相同组名称，不同的已存在用户id，不同URL
     */
    @Test
    public void stockaddre6() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库重复添加-不同用户id，相同组名称，不同图片地址\n";

        String key = "";

        try {
            String groupName = "group" + System.currentTimeMillis();
            String userID = ""; //存在的用户id
            String userID1 = ""; //存在的用户id
            String picUrl = menjin.lxq;
            //第一次
            menjin.stockAdd(groupName,userID1,picUrl);
            //重复
            String picUrl2 = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl2);
            int code = single.getInteger("code");
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(code==1000,"重复添加失败，状态码"+code);
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 不同组名称，不同的已存在用户id，相同URL
     */
    @Test
    public void stockaddre7() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库重复添加-不同用户id，不同组名称，相同图片地址\n";

        String key = "";

        try {
            String groupName = "group" + System.currentTimeMillis();
            String userID = ""; //存在的用户id
            String picUrl = menjin.lxq;
            //第一次
            menjin.stockAdd(groupName,userID,picUrl);
            //重复
            String groupName1 = "group" + System.currentTimeMillis();
            String userID1 = ""; //存在的用户id
            JSONObject single = menjin.stockAdd(groupName1,userID1,picUrl);
            int code = single.getInteger("code");
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(code==1000,"重复添加失败，状态码"+code);
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 不同组名称， 不同的已存在用户id，不同URL
     */
    @Test
    public void stockaddre8() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库重复添加-相同用户id，相同组名称，不同图片地址\n";

        String key = "";

        try {
            String groupName = "group" + System.currentTimeMillis();
            String userID = ""; //存在的用户id
            String picUrl = menjin.lxq;
            //第一次
            menjin.stockAdd(groupName,userID,picUrl);
            //重复
            String picUrl2 = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl2);
            int code = single.getInteger("code");
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(code==1000,"重复添加失败，状态码"+code);
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    //--------------------人脸库删除face-----------------
    /**
     * 不填写删除类型，组名，用户id，人脸id对应
     * 新增人脸库->删除
     */
    @Test
    public void stockDeleteFace() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库删除Face-组名，用户id，人脸id对应\n";

        String key = "";

        try {
            //添加人脸库
            String groupName = "group" + System.currentTimeMillis();
            String userID = "";//存在的用户id
            String picUrl = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl);
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");
            //删除Face
            JSONObject single1 = menjin.stockDelete(groupName,userID,faceid,"");
            int code1 = single1.getInteger("code");
            Preconditions.checkArgument(code1==1000,"删除失败，状态码" + code1);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 不填写删除类型，组名，用户id，人脸id对应
     * 新增人脸库->删除->查找无结果
     */
    @Test
    public void stockDeleteFaceWithSearch() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库删除Face-组名，用户id，人脸id对应\n";

        String key = "";

        try {
            //添加人脸库
            String groupName = "group" + System.currentTimeMillis();
            String userID = "";//存在的用户id
            String picUrl = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl);
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");
            //删除Face
            JSONObject single1 = menjin.stockDelete(groupName,userID,faceid,"");
            int code1 = single1.getInteger("code");
            Preconditions.checkArgument(code1==1000,"删除失败，状态码" + code1);

            //查询无结果
            String [] grouplist = new String[]{groupName};

            JSONObject single2 = menjin.stockSearch(grouplist,picUrl,"","","","","");
            int code2 = single2.getInteger("code");
            JSONArray faces = single2.getJSONArray("faces");
            Preconditions.checkArgument(code2==1000,"查询失败，状态码"+ code2);
            Preconditions.checkArgument(faces.size()==0,"删除后查询有结果");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 不填写删除类型，组名，用户id，人脸id不对应
     * 新增人脸库->删除
     */
    @Test
    public void stockDeleteFace1() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库删除Face-组名，用户id，人脸id不对应\n";

        String key = "";

        try {
            //添加人脸库
            String groupName = "group" + System.currentTimeMillis();
            String userID = "";//存在的用户id
            String picUrl = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl);
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");
            //删除Face
            String groupName2 = "group" + System.currentTimeMillis();
            JSONObject single1 = menjin.stockDelete(groupName2,userID,faceid,"");
            int code1 = single1.getInteger("code");
            Preconditions.checkArgument(code1==1,"期待删除失败，状态码" + code1);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 填写删除类型FACE，人脸id不填写
     * 新增人脸库->删除
     */
    @Test
    public void stockDeleteFaceNoID() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库删除FACE类型，人脸id不填写\n";

        String key = "";

        try {
            //添加人脸库
            String groupName = "group" + System.currentTimeMillis();
            String userID = "";//存在的用户id
            String picUrl = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl);
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");
            //删除Face
            String groupName2 = "group" + System.currentTimeMillis();
            JSONObject single1 = menjin.stockDelete(groupName2,userID,"","FACE");
            int code1 = single1.getInteger("code");
            Preconditions.checkArgument(code1==1,"期待删除失败，状态码" + code1);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 删除类型FACE ，组名，用户id，人脸id对应
     * 新增人脸库->删除
     */
    @Test
    public void stockDeleteFaceWithType() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库删除类型为Face\n";

        String key = "";

        try {
            //添加人脸库
            String groupName = "group" + System.currentTimeMillis();
            String userID = "";//存在的用户id
            String picUrl = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl);
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");
            //删除Face
            JSONObject single1 = menjin.stockDelete(groupName,userID,faceid,"FACE");
            int code1 = single1.getInteger("code");
            Preconditions.checkArgument(code1==1000,"删除失败，状态码" + code1);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 删除类型FACE，组名，用户id，人脸id对应
     * 新增人脸库->删除->查找对应组无结果
     */
    @Test
    public void stockDeleteFaceWithTypeSearch() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库删除Face-组名，用户id，人脸id对应\n";

        String key = "";

        try {
            //添加人脸库
            String groupName = "group" + System.currentTimeMillis();
            String userID = "";//存在的用户id
            String picUrl = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName,userID,picUrl);
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");
            //删除Face
            JSONObject single1 = menjin.stockDelete(groupName,userID,faceid,"FACE");
            int code1 = single1.getInteger("code");
            Preconditions.checkArgument(code1==1000,"删除失败，状态码" + code1);

            //查询无结果
            String [] grouplist = new String[]{groupName};

            JSONObject single2 = menjin.stockSearch(grouplist,picUrl,"","","","","");
            int code2 = single2.getInteger("code");
            JSONArray faces = single2.getJSONArray("faces");
            Preconditions.checkArgument(code2==1000,"查询失败，状态码"+ code2);
            Preconditions.checkArgument(faces.size()==0,"删除后查询有结果");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 删除类型FACE，组名，用户id，人脸id对应
     * 新增人脸库->删除->查找其他组有结果
     */
    @Test
    public void stockDeleteFaceWithTypeSearch1() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库删除Face-组名，用户id，人脸id对应\n";

        String key = "";

        try {
            //在一个组添加人脸库
            String groupName1 = "group" + System.currentTimeMillis();
            String userID = "";//存在的用户id
            String picUrl = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName1,userID,picUrl);
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");
            //在另一个组添加人脸库
            String groupName2 = "group2" + System.currentTimeMillis();

            //删除Face
            JSONObject single1 = menjin.stockDelete(groupName1,userID,faceid,"FACE");
            int code1 = single1.getInteger("code");
            Preconditions.checkArgument(code1==1000,"删除失败，状态码" + code1);

            //查询另一个组有结果
            String [] grouplist = new String[]{groupName2};

            JSONObject single2 = menjin.stockSearch(grouplist,picUrl,"","","","","");
            int code2 = single2.getInteger("code");
            JSONArray faces = single2.getJSONArray("faces");
            Preconditions.checkArgument(code2==1000,"查询失败，状态码"+ code2);
            Preconditions.checkArgument(faces.size()>0,"删除后查询无结果");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 删除类型USER，组名，用户id，人脸id对应
     * 新增人脸库->删除
     */
    @Test
    public void stockDeleteUSER() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库删除USER\n";

        String key = "";

        try {
            //在一个组添加人脸库
            String groupName1 = "group" + System.currentTimeMillis();
            String userID = "";//存在的用户id
            String picUrl = menjin.yhs;
            String picUrl2 = menjin.personWithMask;
            JSONObject single = menjin.stockAdd(groupName1,userID,picUrl);
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");
            //在另一个组添加人脸库
            String groupName2 = "group" + System.currentTimeMillis();
            JSONObject single2 = menjin.stockAdd(groupName2,userID,picUrl2);
            String faceid2 = single2.getString("face_id");

            //删除User
            JSONObject single1 = menjin.stockDelete(groupName1,userID,faceid,"USER");
            int code1 = single1.getInteger("code");
            Preconditions.checkArgument(code1==1000,"删除失败，状态码" + code1);


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 删除类型USER，组名，用户id，人脸id对应
     * 新增人脸库->删除->查找全部组无结果
     */
    @Test
    public void stockDeleteUSERWithSearch() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库删除USER后，查询所有组无结果\n";

        String key = "";

        try {
            //在一个组添加人脸库
            String groupName1 = "group" + System.currentTimeMillis();
            String userID = "";//存在的用户id
            String picUrl = menjin.yhs;
            String picUrl2 = menjin.personWithMask;
            JSONObject single = menjin.stockAdd(groupName1,userID,picUrl);
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");
            //在另一个组添加人脸库
            String groupName2 = "group" + System.currentTimeMillis();
            menjin.stockAdd(groupName2,userID,picUrl2);


            //删除User
            JSONObject single1 = menjin.stockDelete(groupName1,userID,faceid,"USER");
            int code1 = single1.getInteger("code");
            Preconditions.checkArgument(code1==1000,"删除失败，状态码" + code1);

            //查询另一个组有结果
            String [] grouplist = new String[]{groupName2,groupName1};

            JSONObject single2 = menjin.stockSearch(grouplist,picUrl,"","","","","");
            int code2 = single2.getInteger("code");
            JSONArray faces = single2.getJSONArray("faces");
            Preconditions.checkArgument(code2==1000,"查询失败，状态码"+ code2);
            Preconditions.checkArgument(faces.size()==0,"删除后查询有结果");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 删除类型USER，无faceid
     */
    @Test
    public void stockDeleteUSERNoID() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库删除User,不传faceid\n";

        String key = "";

        try {
            //在一个组添加人脸库
            String groupName1 = "group" + System.currentTimeMillis();
            String userID = "";//存在的用户id
            String picUrl = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName1,userID,picUrl);
            String faceid = single.getString("face_id");
            Preconditions.checkArgument(!faceid.equals(""),"face_id返回空");

            //删除User
            JSONObject single1 = menjin.stockDelete(groupName1,userID,"","USER");
            int code1 = single1.getInteger("code");
            Preconditions.checkArgument(code1==1,"应失败，状态码" + code1);


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    //-------------------人脸库删除组--------------------

    /**
     * 人脸库删除不存在的组
     */
    @Test
    public void groupDeleteNotExist() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库删除不存在的组\n";

        String key = "";

        try {

            JSONObject single = menjin.stockDeleteGroup("qwertyui234567");
            int code = single.getInteger("code");
            Preconditions.checkArgument(code==1,"应失败，状态码" + code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     * 人脸库删除存在的组
     */
    @Test
    public void stockDeleteExist() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库删除存在的组\n";

        String key = "";

        try {
            //在一个组添加人脸库
            String groupName1 = "group" + System.currentTimeMillis();
            String userID = "";//存在的用户id
            String picUrl = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName1,userID,picUrl);

            //删除组
            JSONObject single1 = menjin.stockDeleteGroup(groupName1);
            int code1 = single1.getInteger("code");
            Preconditions.checkArgument(code1==1000,"删除失败，状态码" + code1);


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    //--------------人脸库copy---------------
    /**
     * 人脸库copy，faceid不空
     */
    @Test
    public void stockCopyFaceIDNotnull() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库copy，faceid不空\n";

        String key = "";

        try {
            //在一个组添加人脸库
            String groupName1 = "fromgroup" + System.currentTimeMillis();//from存在
            String groupName2 = "togroup" + System.currentTimeMillis();//to不存在
            String userID = "";//存在的用户id
            String picUrl = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName1,userID,picUrl);
            String faceid = single.getString("face_id");

            JSONObject single2 = menjin.stockCopy(userID,faceid,groupName1,groupName2);
            int code = single2.getInteger("code");
            Preconditions.checkArgument(code==1000,"copy失败，状态码" + code);

            //没判断to是否存在




        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 人脸库copy，faceid空
     */
    @Test
    public void stockCopyFaceIDnull() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库copy，faceid空\n";

        String key = "";

        try {
            //在一个组添加人脸库
            String groupName1 = "fromgroup" + System.currentTimeMillis();//from存在
            String groupName2 = "togroup" + System.currentTimeMillis();//to不存在
            String userID = "";//存在的用户id
            String picUrl = menjin.yhs;
            String picUrl2 = menjin.personWithMask;
            menjin.stockAdd(groupName1,userID,picUrl);
            menjin.stockAdd(groupName1,userID,picUrl2);

            JSONObject single2 = menjin.stockCopy(userID,"",groupName1,groupName2);
            int code = single2.getInteger("code");
            Preconditions.checkArgument(code==1000,"copy失败，状态码" + code);

            //没判断to是否存在




        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 人脸库copy，from不存在
     */
    @Test
    public void stockCopyNotExistFrom() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库copy，from不存在\n";

        String key = "";

        try {
            //在一个组添加人脸库
            String groupName1 = "fromgroup" + System.currentTimeMillis();//from存在
            String groupName2 = "togroup" + System.currentTimeMillis();//to不存在
            String userID = "";//存在的用户id
            String picUrl = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName1,userID,picUrl);
            String faceid = single.getString("face_id");

            JSONObject single2 = menjin.stockCopy(userID,faceid,groupName2,groupName1);
            int code = single2.getInteger("code");
            Preconditions.checkArgument(code==1,"应失败，状态码" + code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    //------人脸库查询-----
    /**
     * 人脸库查询，result_num>2000
     */
    @Test
    public void stockSearchNumGT2000() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸库copy，result_num=3000\n";

        String key = "";

        try {
            //在一个组添加人脸库
            String groupName1 = "fromgroup" + System.currentTimeMillis();
            String userID = "";//存在的用户id
            String picUrl = menjin.yhs;
            JSONObject single = menjin.stockAdd(groupName1,userID,picUrl);
            String faceid = single.getString("face_id");
            String [] grouplist = new String[]{groupName1};
            //搜索
            JSONObject single2 = menjin.stockSearch(grouplist,picUrl,"","3000","","","");
            int code = single2.getInteger("code");
            Preconditions.checkArgument(code==1,"应失败，状态码" + code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }





//    ---------------------------------------------------通用方法--------------------------------------------------------------

    private void saveData(Case aCase, String ciCaseName, String caseName, String caseDescription) {
        //setBasicParaToDB(aCase, ciCaseName, caseName, caseDescription);
        //qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());
            dingPush("门禁日常-算法层接口 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    private void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {
//            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        msg = msg.replace("java.lang.Exception: ", "异常：");
        alarmPush.dailyRgn(msg);
        this.FAIL = true;
        Assert.assertNull(aCase.getFailReason());
    }

    private void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

            //15898182672 华成裕
            //18513118484 杨航
            //15011479599 谢志东
            //18600872221 蔡思明
            String[] rd = {"18513118484", //杨航
                    "15011479599", //谢志东
                    "15898182672"}; //华成裕
            alarmPush.alarmToRd(rd);
        }
    }


    private static String getImgStr(String imgFile) { //图片转base64
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }

      // public static void main(String[] args) throws Exception {// ---不用理我！
        //String path = "src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/分辨率较低.png";
        //String imgbese = getImgStr(path);
        //String img_path="data:image/jpeg;base64,"+imgbese ;
        //System.out.println(imgbese);
        //System.out.println(img_path);

           /**
           String config = "{\n" +
                   "                \"pass_num\":\"通行次数, 若为-1则无次数限制\",\n" +
                   "                    \"start_time\":\"Long型时间戳, 通行时间限制. 若为-1则无时间限制\",\n" +
                   "                    \"end_time\":\"Long型时间戳, 通行时间限制. 若为-1则无时间限制\"\n" +
                   "            }";
           JSONObject auth_config = JSON.parseObject(config);
           System.out.println(auth_config);

            */

   // }




}