package com.haisheng.framework.testng.operationcenter.dmp;

/**
 * @author huachengyu
 * @date 2018-08-27 14:00
 **/
public enum StatusCode {

    // 通用状态码
    /**
     * 成功
     */
    SUCCESS(1000, "success"),

    /**
     * 参数错误
     */
    BAD_REQUEST(1001, "invalid arguments"),

    /**
     * 请求处理超时
     */
    INTERNAL_TIME_OUT(1002, "internal timeout"),

    /**
     * 系统繁忙，请稍后重试
     */
    SYSTEM_BUSY(1003, "system busy"),

    /**
     * 请求实体过大
     */
    REQUEST_ENTITY_TOO_LARGE(1004, "request entity too large"),

    /**
     * 系统错误(联系技术人员排查)
     */
    INTERNAL_SERVER_ERROR(1005, "internal service error"),

    /**
     * 下载失败
     */
    DOWNLOAD_FAIL(1006, "download error"),

    /**
     * 图片大小错误
     */
    IMAGE_SIZE_ERROR(1007, "image size error"),

    /**
     * 图片格式错误
     */
    IMAGE_DECODE_ERROR(1008, "image decode error"),

    /**
     * 未知错误(联系技术人员排查)
     */
    UNKNOWN_ERROR(1009, "unknown error"),

    /**
     * 数据库读写错误
     */
    DB_ERROR(1010, "db error"),

    /**
     * 分布式调度错误
     */
    TASK_DISPATCH_ERROR(1011, "task dispatch error"),

    /**
     * 网络请求错误
     */
    NET_ERROR(1012, "net error"),

    /**
     * 数据库中已存在该条数据
     */
    DB_EXIST(1013, "db exist"),

    /**
     * 上传失败
     */
    UPLOAD_FAIL(1014, "upload error"),

    // 系统状态码
    /**
     * Authorization授权失败
     */
    UN_AUTHORIZED(2001, "authorized error"),

    /**
     * 服务端拒绝请求(黑名单/限流)
     */
    FORBIDDEN(2002, "request forbidden"),

    /**
     * ROUTER找不到
     */
    API_NOT_FOUND(2003, "api not found"),

    /**
     * 人体质量不合格
     */
    DEVICE_MANAGE_ERROR(2004, "device manage error"),

    // 业务状态码
    /**
     * 没有找到人脸
     */
    NO_FACE(3001, "no face"),

    /**
     * 人脸质量不合格
     */
    FACE_UNQUALIFIED(3002, "face unqualified"),

    /**
     * 因相关部分计算错误造成的失败
     */
    RELATED_FAIL(3003, "relative fail"),

    /**
     * 注册数量超过组上限
     */
    REGISTER_LIMIT(3004, "register limit"),

    /**
     * 组被锁定（不支持增删改操作，查询可以继续）
     */
    GROUP_LOCK(3005, "group db lock"),

    /**
     * 人脸组缓存加载中……
     */
    GROUP_RUNNING(3006, "group init is running"),

    /**
     * 没有找到需要查询的组
     */
    NO_GROUP(3007, "no group"),

    /**
     * 算法计算错误
     */
    ALG_ERROR(3008, "algorithm error"),

    /**
     * 算法特征提取错误
     */
    ALG_FEATURE_ERROR(3009, "algorithm feature error"),

    /**
     * 算法未知错误
     */
    ALG_UNKOWN_ERROR(3010, "algorithm unknown error"),

    /**
     * 人体质量不合格
     */
    BODY_UNQUALIFIED(3011, "body unqualified"),

    /**
     * 货架-未盘货完成
     */
    STOCKTAKING_UNFINISHED(4001,"stocktaking unfinished"),

    /**
     * 货架-未知动作类型
     */
    ACTION_NOT_FOUND(4002, "action not found"),

    /**
     * 基础信息服务异常
     */
    BUSINESS_CHECK_ERROR(5001,"business check error"),

    /**
     * 基础信息服务接口权限校验异常
     */
    INTERFACE_AUTH_CHECK_ERROR(5002,"interface_auth check error")
    ;

    private Integer code;
    private String message;

    StatusCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static StatusCode codeOf(Integer code) {
        for (StatusCode statusCode : StatusCode.values()) {
            if (statusCode.code.equals(code)) {
                return statusCode;
            }
        }
        return UNKNOWN_ERROR;
    }

    public static StatusCode nameOf(String name) {
        for (StatusCode resultCode : StatusCode.values()) {
            if (resultCode.name().equals(name)) {
                return resultCode;
            }
        }
        return UNKNOWN_ERROR;
    }
}
