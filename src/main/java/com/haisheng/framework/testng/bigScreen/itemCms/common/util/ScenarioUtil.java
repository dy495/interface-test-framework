package com.haisheng.framework.testng.bigScreen.itemCms.common.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.itemCms.common.enumerator.EnumCloudSceneType;
import com.haisheng.framework.testng.bigScreen.itemCms.common.scene.LoginScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScenarioUtil extends BasicUtil {
    private final VisitorProxy visitor;

    public ScenarioUtil(VisitorProxy visitor) {
        super(visitor);
        this.visitor = visitor;
    }

    public void login(EnumAccount account) {
        String email = "28e3e02ba627a44c949d3ef94b217388";
        IScene scene = LoginScene.builder().email(account.getPhone()).password(email).build();
        visitor.setToken(scene);
    }


    public Long getDeploymentGroupId() {
        return visitor.isDaily() ? 72L : 0L;
    }

    public Long getDeploymentId() {
        return visitor.isDaily() ? 256L : 0L;
    }

    public String getCloudSceneType() {
        return EnumCloudSceneType.AUTO_DEFAULT.name();
    }

    public void main(String[] args) {
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        String accessKeyId = "LTAI5t8wVqrm9pfHZswwRok1";
        String accessKeySecret = "O1DToNZRCeRxVdVewpuJrg4sPKojHe";
        String bucketName = "retail-huabei2";
        String objectName = "test/日常/门店出入口数据0809.xlsx";
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//        OSSObject ossObject = ossClient.getObject(bucketName, objectName);
//        System.out.println("Object content:");
        System.err.println("开始下载");
        GetObjectRequest objectRequest = new GetObjectRequest(bucketName, objectName);
        String userDir = System.getProperty("user.dir");
        System.err.println(userDir);
//        File file = new File("D:\\JetBrains\\IntelliJ IDEAProjects\\interface-test-framework\\src\\main\\java\\com\\haisheng\\framework\\testng\\bigScreen\\itemCms\\common\\multimedia\\file\\门店出入口数据0809.xlsx");
//        ossClient.getObject(objectRequest, file);


//        BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
//        try {
//            while (true) {
//                String line = reader.readLine();
//                if (line == null) break;
//                System.out.println("\n" + line);
//            }
//            reader.close();
//            ossClient.shutdown();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
