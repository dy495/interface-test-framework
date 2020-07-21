package com.haisheng.framework.model.experiment.base;

/**
 * 常量工具类，如与平台无关的换行符、分隔符等
 */
public final class Constants {
    /**
     * 换行符
     */
    public static final String SYSTEM_LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * URI中的分隔符
     */
    public static final String SYSTEM_FILE_SEPARATOR = System.getProperty("file.separator");

    /**
     * SFTP端口
     */
    public static final int SFTP_REQ_PORT = 22;
    /**
     * SFTP超时时间
     */
    public static final int SFTP_REQ_TIMEOUT = 60000;

    /**
     * ZIP压缩缓冲
     */
    public static final int ZIP_BUFFER = 1024;

    /**
     * DOCTYPE_HTML标识
     */
    public static final String DOCTYPE_HTML = "<!doctype html>";
}
