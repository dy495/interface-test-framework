package com.haisheng.framework.testng.bigScreen.itemCms.common.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.datacheck.AliyunConfig;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.itemCms.common.enumerator.EnumCloudSceneType;
import com.haisheng.framework.testng.bigScreen.itemCms.common.scene.LoginScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;

import java.io.File;

public class SceneUtil extends BasicUtil {
    private final VisitorProxy visitor;

    public SceneUtil(VisitorProxy visitor) {
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

    public File downloadFile() throws AssertionError {
        String tableName = System.getProperty("TABLE_NAME");
        Preconditions.checkArgument(tableName != null, "表名不可为空");
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        String accessKeyId = "LTAI5t8wVqrm9pfHZswwRok1";
        String accessKeySecret = "O1DToNZRCeRxVdVewpuJrg4sPKojHe";
        String bucketName = "retail-huabei2";
        String objectName = visitor.isDaily() ? "test/日常/" + tableName : "test/线上/" + tableName;
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        logger.info("文件开始下载");
        GetObjectRequest objectRequest = new GetObjectRequest(bucketName, objectName);
        String userDir = System.getProperty("user.dir");
        File file = new File(userDir + "/src/main/java/com/haisheng/framework/testng/bigScreen/itemCms/common/multimedia/file/" + tableName);
        logger.info(file.exists() ? "文件已存在" : "文件生成路径：{} ObjectMetadata：{}", file.getPath(), ossClient.getObject(objectRequest, file));
        return file;
    }

    public void deleteFile(File file) {
        logger.info(file == null ? "文件不存在" : file.exists() && file.isFile() ? "删除文件--" + file.delete() : "文件不存在或者不是文件");
    }
}
