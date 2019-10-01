package com.mufeng.skydog.bean;

import lombok.Data;
@Data
public class SdDataSourceConfig {
    private String id;
    private String code;
    private String driverClass;
    private String jdbcUrl;
    private String user;
    private String password;
    private Integer minPoolSize;
    private Integer maxPoolSize;
    private Integer maxIdleTime;
    private Integer idleConnectionTestPeriod;

    private String gmtCreated;

    private String gmtModified;
}
