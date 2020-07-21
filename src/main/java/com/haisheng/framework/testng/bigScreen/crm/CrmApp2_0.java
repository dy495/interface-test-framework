package com.haisheng.framework.testng.bigScreen.crm;

import com.haisheng.framework.model.experiment.base.BaseTest;
import com.haisheng.framework.model.experiment.base.RequestLog;
import com.haisheng.framework.model.experiment.scene.CustomerListPCScene;
import okhttp3.Response;
import org.testng.annotations.Test;

import java.time.LocalDate;

/**
 * @author wangmin
 * @date 2020/7/21 14:32
 */
public class CrmApp2_0 extends BaseTest {

    @Test
    public void testA() {
        String startTime = LocalDate.now().toString();
        String endTime = startTime;
        LocalDate now = LocalDate.now();
        RequestLog<Response> log = new CustomerListPCScene.Builder().endTime(endTime).startTime(startTime).build().run();

        System.out.println(log.toString());


    }
}
