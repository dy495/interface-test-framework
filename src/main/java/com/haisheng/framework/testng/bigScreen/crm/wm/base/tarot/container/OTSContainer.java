package com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.container;

import com.alicloud.openservices.tablestore.ClientConfiguration;
import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.AlwaysRetryStrategy;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

/**
 * ots容器
 *
 * @author wangmin
 * @data 2021-06-15
 */
public class OTSContainer extends NoSqlContainer {
    private final String endPoint;
    private final String accessKeyId;
    private final String accessKeySecret;
    private final String instanceName;

    public OTSContainer(@NotNull Builder builder) {
        super(builder);
        this.endPoint = builder.endPoint;
        this.accessKeyId = builder.accessKeyId;
        this.accessKeySecret = builder.accessKeySecret;
        this.instanceName = builder.instanceName;
    }

    @Override
    SyncClient getSyncClient() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setConnectionTimeoutInMillisecond(5000);
        clientConfiguration.setSocketTimeoutInMillisecond(5000);
        clientConfiguration.setRetryStrategy(new AlwaysRetryStrategy());
        logger.info("instanceName is:{}", instanceName);
        return new SyncClient(this.endPoint, this.accessKeyId, this.accessKeySecret, this.instanceName);
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder<Builder, OTSContainer> {
        private String endPoint;
        private String accessKeyId;
        private String accessKeySecret;
        private String instanceName;

        @Override
        public OTSContainer buildContainer() {
            return new OTSContainer(this);
        }
    }
}
