package com.haisheng.framework.testng.service;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;

public final class ApiRequest implements Serializable, Cloneable {
    private String uid;
    @JSONField(
            name = "app_id"
    )
    private String appId;
    @JSONField(
            name = "request_id"
    )
    private String requestId;
    private String version;
    private String router;
    @JSONField(
            name = "client_info"
    )
    private ai.winsense.model.ApiRequest.ClientInfo clientInfo = new ai.winsense.model.ApiRequest.ClientInfo();
    private ai.winsense.model.ApiRequest.Data data = new ai.winsense.model.ApiRequest.Data();

    public String getUid() {
        return this.uid;
    }

    public String getAppId() {
        return this.appId;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public String getVersion() {
        return this.version;
    }

    public String getRouter() {
        return this.router;
    }

    public ai.winsense.model.ApiRequest.ClientInfo getClientInfo() {
        return this.clientInfo;
    }

    public ai.winsense.model.ApiRequest.Data getData() {
        return this.data;
    }

    public static class Data {
        @JSONField(
                name = "device_id"
        )
        private String deviceId;
        private String[] resource;
        @JSONField(
                name = "resource_unencrypted_idx"
        )
        private Integer[] resourceUnencryptedIdx;
        @JSONField(
                name = "biz_data"
        )
        private Object bizData;
        @JSONField(
                name = "sec_key_path"
        )
        private String[] secKey;

        public Data() {
        }

        public String getDeviceId() {
            return this.deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String[] getResource() {
            return this.resource;
        }

        public void setResource(String[] resource) {
            this.resource = resource;
        }

        public Integer[] getResourceUnencryptedIdx() {
            return this.resourceUnencryptedIdx;
        }

        public void setResourceUnencryptedIdx(Integer[] resourceUnencryptedIdx) {
            this.resourceUnencryptedIdx = resourceUnencryptedIdx;
        }

        public Object getBizData() {
            return this.bizData;
        }

        public void setBizData(Object bizData) {
            this.bizData = bizData;
        }

        public String[] getSecKey() {
            return this.secKey;
        }

        public void setSecKey(String[] secKey) {
            this.secKey = secKey;
        }
    }

    public static class ClientInfo {
        private String ip;
        private String location;
        private String mac;
        @JSONField(
                name = "host_name"
        )
        private String hostName;

        public ClientInfo() {
        }

        public String getIp() {
            return this.ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getLocation() {
            return this.location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getMac() {
            return this.mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getHostName() {
            return this.hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public void setClientInfo(ai.winsense.model.ApiRequest.ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public void setData(ai.winsense.model.ApiRequest.Data data) {
        this.data = data;
    }
}
